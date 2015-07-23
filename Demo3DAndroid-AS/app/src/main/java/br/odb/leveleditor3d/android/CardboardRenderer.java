package br.odb.leveleditor3d.android;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.KeyEvent;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

import br.odb.SceneActorNode;
import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESMesh;
import br.odb.gamelib.android.geometry.GLESVertexArrayManager;
import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.CameraNode;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;

/**
 * Created by monty on 7/2/15.
 */
public class CardboardRenderer implements CardboardView.StereoRenderer {

    public final CameraNode cameraNode = new CameraNode( "mainCamera" );

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 10000.0f;
    private static final float CAMERA_Z = 0.01f;

    final HashMap<Material, GLESVertexArrayManager > managers = new HashMap<>();

    private static final String TAG = CardboardRenderer.class.getSimpleName();

    private final Context context;

    private int defaultProgram;

    private int positionParam;
    private int colorParam;
    private int modelViewProjectionParam;

    private br.odb.utils.math.Matrix camera = new br.odb.utils.math.Matrix( 4, 4 );
    private br.odb.utils.math.Matrix view = new br.odb.utils.math.Matrix( 4, 4 );
    private br.odb.utils.math.Matrix modelViewProjection = new br.odb.utils.math.Matrix( 4, 4 );

    private float[] forwardVector = new float[ 3 ];

    public final ArrayList<GLESMesh> meshes = new ArrayList<>();
    public final GLESMesh sampleEnemy = new GLESMesh( "sample-enemy" );
    final public List<SceneActorNode> actors = new ArrayList<>();

    volatile boolean ready = false;

    final HashMap<Material, ArrayList< GLES1Triangle> > staticGeometryToAdd= new HashMap<>();
    private int polycount = 10000;

    CardboardRenderer(Context context) {
        this.context = context;
    }

    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
     *
     * @param type The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The shader object handler.
     */
    private int loadGLShader(int type, int resId) {
        String code = readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     */
    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }


    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    /**
     * Creates the buffers we use to store information about the 3D world.
     *
     * <p>OpenGL doesn't use Java arrays, but rather needs data in a format it can understand.
     * Hence we use ByteBuffers.
     *
     * @param config The EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well.

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, R.raw.light_vertex);
        int passthroughShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.passthrough_fragment);

        defaultProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(defaultProgram, vertexShader);
        GLES20.glAttachShader(defaultProgram, passthroughShader);
        GLES20.glLinkProgram(defaultProgram);
        GLES20.glUseProgram(defaultProgram);

        positionParam = GLES20.glGetAttribLocation(defaultProgram, "a_Position");
        colorParam = GLES20.glGetAttribLocation(defaultProgram, "a_Color");
        modelViewProjectionParam = GLES20.glGetUniformLocation(defaultProgram, "u_MVP");

        GLES20.glEnableVertexAttribArray(positionParam);
        GLES20.glEnableVertexAttribArray(colorParam);

        // Object first appears directly in front of user.
        checkGLError("onSurfaceCreated");
    }

    /**
     * Converts a raw text file into a string.
     *
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The context of the text file, or null in case of error.
     */
    private String readRawTextFile(int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prepares OpenGL ES before we draw a frame.
     *
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(camera.values, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        checkGLError("onReadyToDraw");

        headTransform.getEulerAngles(forwardVector, 0);

        cameraNode.angleXZ = (float) (( forwardVector[ 1 ] * ( 180 / Math.PI ) ));

        while( cameraNode.angleXZ < 0 ) {
            cameraNode.angleXZ += 360.0;
        }

        while( cameraNode.angleXZ > 360 ) {
            cameraNode.angleXZ -= 360.0;
        }
    }

    public void initDefaultMeshForActor() throws IOException {
        GeneralTriangleMesh enemy;
        WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
        List<Material> mats = matLoader.parseMaterials( context.getAssets().open( "gargoyle.mtl" ) );

        WavefrontOBJLoader loader = new WavefrontOBJLoader( new GLES1TriangleFactory() );
        ArrayList<GeneralTriangleMesh> mesh = (ArrayList<GeneralTriangleMesh>) loader.loadMeshes( context.getAssets().open("gargoyle.obj"), mats );

        enemy = mesh.get( 0 );

        for ( GeneralTriangle gt : enemy.faces ) {
            sampleEnemy.faces.add(GLES1TriangleFactory.getInstance().makeTrigFrom(gt));
        }
    }

    /**
     * Draws a frame for an eye.
     *
     * @param eye The eye to render. Includes all required transformations.
     */
    @Override
    public void onDrawEye(Eye eye) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if ( ready ) {
            // Build the ModelView and ModelViewProjection matrices
            float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);

            GLES20.glUseProgram(defaultProgram);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            // Apply the eye transformation to the camera.
            Matrix.multiplyMM(view.values, 0, eye.getEyeView(), 0, camera.values, 0);
            Matrix.translateM(view.values, 0, -cameraNode.localPosition.x, -cameraNode.localPosition.y, -cameraNode.localPosition.z);
            Matrix.multiplyMM(modelViewProjection.values, 0, perspective, 0, view.values, 0);

            // Set the ModelViewProjection matrix in the shader.
            GLES20.glUniformMatrix4fv(modelViewProjectionParam, 1, false, modelViewProjection.values, 0);

            drawPerMaterialStaticMesh();
            drawMeshes(eye);
        }
    }

    public void transform( Eye eye, float angleXZ, Vec3 trans) {
        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
        Matrix.multiplyMM(view.values, 0, eye.getEyeView(), 0, camera.values, 0);
        Matrix.translateM(view.values, 0, -cameraNode.localPosition.x, -cameraNode.localPosition.y, -cameraNode.localPosition.z);
        Matrix.translateM(view.values, 0, trans.x, trans.y, trans.z);
        Matrix.multiplyMM(modelViewProjection.values, 0, perspective, 0, view.values, 0);
    }

    private void drawMeshes( Eye eye ) {
        synchronized (meshes) {
            for ( SceneActorNode actor : actors ) {

                transform( eye, actor.angleXZ, actor.localPosition );
                GLES20.glUniformMatrix4fv(modelViewProjectionParam, 1, false, modelViewProjection.values, 0);
                drawMeshGLES2(sampleEnemy);
            }
        }
    }

    public void spawnActor(Vec3 v, float angleXZ) {
        SceneActorNode actor = new SceneActorNode( "actor@" + v.toString() );
        actor.localPosition.set( v );
        actor.angleXZ = angleXZ;
        actors.add( actor );
    }

    /**
     * @param mesh
     */
    private void drawMeshGLES2(GeneralTriangleMesh mesh) {
        synchronized ( mesh ) {
            for (GeneralTriangle face : mesh.faces) {
                ((GLES1Triangle) face).drawGLES2(positionParam, colorParam, -1);
            }
        }
    }

    private void drawPerMaterialStaticMesh() {

        GLESVertexArrayManager manager;

        for ( Material mat : managers.keySet() ) {

            manager = managers.get( mat );
            manager.draw(positionParam, colorParam, -1);
        }
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    public void setScene(World scene) {
        loadGeometryFromScene(scene.masterSector);
        flush();
    }

    public void addToVA(GLES1Triangle face) {

        if ( !staticGeometryToAdd.containsKey( face.material ) ) {
            staticGeometryToAdd.put(face.material, new ArrayList<GLES1Triangle>());

        }

        staticGeometryToAdd.get( face.material ).add(face);
    }

    private void loadGeometryFromScene(GroupSector sector) {

        for (GeneralTriangle isf : sector.mesh.faces) {
            changeHue((GLES1Triangle) isf);
            isf.flush();
            addToVA((GLES1Triangle) isf);
        }

        for (SceneNode sr : sector.getSons()) {
            if (sr instanceof GroupSector) {
                loadGeometryFromScene((GroupSector) sr);
            }
        }
    }

    public void changeHue(GLES1Triangle trig) {
        trig.material = new Material(null, new Color(trig.material.mainColor), null, null );

        switch (trig.hint) {
            case W:
                trig.material.mainColor.multiply(0.8f);
                break;
            case E:
                trig.material.mainColor.multiply(0.6f);
                break;
            case N:
                trig.material.mainColor.multiply(0.4f);
                break;
            case S:
                trig.material.mainColor.multiply(0.2f);
                break;
            case FLOOR:
                trig.material.mainColor.multiply(0.9f);
                break;
            case CEILING:
                trig.material.mainColor.multiply(0.1f);
                break;
        }
    }

    void initManagerForMaterial( Material mat, int polys ) {

        GLESVertexArrayManager manager = new GLESVertexArrayManager( polys );
        managers.put( mat, manager );
    }


    public void flush() {

        for ( Material m : staticGeometryToAdd.keySet() ) {
            initManagerForMaterial( m, staticGeometryToAdd.get( m ).size() );
            for ( GLES1Triangle t : staticGeometryToAdd.get( m ) ) {
                addToVAForReal( t );
            }
            staticGeometryToAdd.get( m ).clear();
        }

        for ( GLESVertexArrayManager manager : managers.values() ) {
            manager.uploadToGPU();
        }

        staticGeometryToAdd.clear();
    }

    private void addToVAForReal(GLES1Triangle face) {

        GLESVertexArrayManager manager;

        if ( !managers.containsKey( face.material ) ) {
            initManagerForMaterial( face.material, polycount / 10 );
        }
        manager = managers.get(face.material );
        manager.pushIntoFrameAsStatic(face.getVertexData(), face.material.mainColor.getFloatData());
    }

    public void onLeft() {
        cameraNode.angleXZ -= 10;
    }

    public void onRight() {
        cameraNode.angleXZ += 10;
    }

    public void onWalkForward() {
        cameraNode.localPosition.x -= 10 * Math.sin(cameraNode.angleXZ* (Math.PI / 180.0f));
        cameraNode.localPosition.z -= 10 * Math.cos(cameraNode.angleXZ* (Math.PI / 180.0f));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                onLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                onRight();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                onWalkForward();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                cameraNode.localPosition.x += 10 * Math.sin(cameraNode.angleXZ
                        * (Math.PI / 180.0f));
                cameraNode.localPosition.z += 10 * Math.cos(cameraNode.angleXZ
                        * (Math.PI / 180.0f));
                break;

            case KeyEvent.KEYCODE_COMMA:
                cameraNode.localPosition.x += 10 * Math.sin((cameraNode.angleXZ - 90)
                        * (Math.PI / 180.0f));
                cameraNode.localPosition.z -= 10 * Math.cos((cameraNode.angleXZ - 90)
                        * (Math.PI / 180.0f));
                break;
            case KeyEvent.KEYCODE_PERIOD:
                cameraNode.localPosition.x -= 10 * Math.sin((cameraNode.angleXZ - 90)
                        * (Math.PI / 180.0f));
                cameraNode.localPosition.z += 10 * Math.cos((cameraNode.angleXZ - 90)
                        * (Math.PI / 180.0f));
                break;

            case KeyEvent.KEYCODE_A:
                cameraNode.localPosition.y += 10.0;
                break;
            case KeyEvent.KEYCODE_Z:
                cameraNode.localPosition.y -= 10.0;
                break;
            case KeyEvent.KEYCODE_Q:
                System.exit(0);
                break;
            case KeyEvent.KEYCODE_BACK:
                return false;
        }

        return true;
    }
}
