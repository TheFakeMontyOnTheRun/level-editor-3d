/**
 * @author Daniel Monteiro
 */

package br.odb.gamelib.android.geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.odb.SceneActorNode;
import br.odb.libscene.CameraNode;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;

/*
 * Concerns:
 * - show the world from the camera's view.
 * - do it in the most efficient way
 * - so, work to keep showing stuff efficiently
 * */

public class GLESRenderer implements GLSurfaceView.Renderer {

    public final CameraNode camera = new CameraNode( "mainCamera" );

    private final int polycount;


    final HashMap<Material, GLESVertexArrayManager > managers = new HashMap<Material, GLESVertexArrayManager >();

    final HashMap<Material, ArrayList< GLES1Triangle> > staticGeometryToAdd= new HashMap<>();

    public final GLESMesh sampleEnemy = new GLESMesh( "sample-enemy" );

    public final ArrayList<GLESMesh> meshes = new ArrayList<>();

    final public List<SceneActorNode> actors = new ArrayList<>();

    //GLES2 stuff
    private int mProgram; //manage the fragShader-vertShader coupling.

    private int maPositionHandle; //manage the coordinates for a given mesh

    private int colorHandle;
    private String vertexShaderCode;
    private String fragmentShaderCode;

    int rockTextureIndex;
    private int mTextureCoordinateHandle = -1;
    private int mTextureUniformHandle = -1;


    private int muMVPMatrixHandle;

    br.odb.utils.math.Matrix mvpMatrix = new br.odb.utils.math.Matrix( 4, 4 );
    br.odb.utils.math.Matrix mMatrix = new br.odb.utils.math.Matrix( 4, 4 );
    br.odb.utils.math.Matrix vMatrix = new br.odb.utils.math.Matrix( 4, 4 );
    br.odb.utils.math.Matrix projectionMatrix = new br.odb.utils.math.Matrix( 4, 4 );

    public volatile boolean ready;

    private final Map< String, Integer > shaders = new HashMap<>();

    /**
     * @param type       GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @param shaderCode proper code for shader
     * @return
     */
    public int loadShader(int type, String name, String shaderCode) {

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        shaders.put( name, shader );

        return shader;
    }

    public void changeHue(GLES1Triangle trig) {
        trig.material = new Material(null, new Color(trig.material.mainColor), null, null);

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

    /**
     * @param maxVisiblePolys
     * @param vertexShader
     * @param fragmentShader
     */
    public GLESRenderer(int maxVisiblePolys, String vertexShader,
                        String fragmentShader ) {
        super();
        this.polycount = maxVisiblePolys;
        this.vertexShaderCode = vertexShader;
        this.fragmentShaderCode = fragmentShader;
    }

    void initManagerForMaterial( Material mat, int polys ) {

        GLESVertexArrayManager manager = new GLESVertexArrayManager( polys );

        if ( this.rockTextureIndex != -1 ) {
            manager.setTextureCoordenates( new float[] {
                            0.0f, 0.0f,
                            0.0f, 1.0f,
                            1.0f, 0.0f
                    }

            );
        }

        managers.put( mat, manager );
    }

    /**
     * @param context
     * @param resourceId
     * @return
     */
    public static int loadTexture(final Context context, final int resourceId) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    /**
     *
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        onSurfaceChangedGLES20(width, height);
    }

    /**
     * @param width
     * @param height
     */
    public void onSurfaceChangedGLES20(int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        float xmin, xmax, ymin, ymax;

        ymax = (float) (0.1f * Math.tan(45.0f * Math.PI / 360.0));
        ymin = -ymax;
        xmin = ymin * ratio;
        xmax = ymax * ratio;

        Matrix.frustumM( projectionMatrix.values, 0, xmin, xmax, ymin, ymax, 0.1f, 10000.0f);
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        Matrix.setLookAtM(vMatrix.values, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    /**
     *
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        onSurfaceCreatedGLES20(config);
    }

    /**
     * @param config
     */
    public void onSurfaceCreatedGLES20(EGLConfig config) {


		rockTextureIndex = -1;//loadTexture( context , R.drawable.tex );

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, "defaultVertexShader", vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, "defaultFragmentShader", fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        colorHandle = GLES20.glGetAttribLocation(mProgram, "a_color");
    }

    /**
     *
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (!ready) {
            return;
        }
        renderSceneGLES20();
    }

    private void prepareTextureWithIndex(int textureIndex) {
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIndex);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
    }

    /**
     * @param mesh
     */
    private void drawMeshGLES2(GeneralTriangleMesh mesh) {
        synchronized ( mesh ) {
            for (GeneralTriangle face : mesh.faces) {
                ((GLES1Triangle) face).drawGLES2(maPositionHandle, colorHandle, this.mTextureCoordinateHandle);
            }
        }
    }



    /**
     *
     */
    private void renderSceneGLES20() {

        if ( this.rockTextureIndex != -1 ) {
            prepareTextureWithIndex(rockTextureIndex);
        }

        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

        setCamera();
        drawPerMaterialStaticMesh();
        drawMeshes();
    }

    private void drawPerMaterialStaticMesh() {
        for (GLESVertexArrayManager manager : managers.values()) {
            manager.draw(maPositionHandle, colorHandle, this.mTextureCoordinateHandle);
        }
    }

//Took this form http://stackoverflow.com/questions/10551225/opengl-es-2-0-rotating-object-around-itself-on-android
//reading it, makes a lot of sense. But I believe I can take this strategy and modify my stuff to use it.

    public void transform(float angleXZ, Vec3 trans ) {
        Matrix.setIdentityM(mMatrix.values, 0);
        Matrix.translateM(mMatrix.values, 0, trans.x, trans.y, trans.z);
        Matrix.rotateM(mMatrix.values, 0, angleXZ, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mvpMatrix.values, 0, vMatrix.values, 0, mMatrix.values, 0);
        Matrix.multiplyMM(mvpMatrix.values, 0, projectionMatrix.values, 0, mvpMatrix.values,   0);
    }

    /**
     *
     */
    private void setCamera() {
        vMatrix.setAsIdentity();

        Matrix.rotateM(vMatrix.values, 0, camera.angleXZ, 0, 1.0f, 0);
        Matrix.translateM(vMatrix.values, 0, -camera.localPosition.x, -camera.localPosition.y, -camera.localPosition.z);
        Matrix.multiplyMM(mvpMatrix.values, 0, vMatrix.values, 0, mMatrix.values, 0);
        Matrix.multiplyMM(mvpMatrix.values, 0, projectionMatrix.values, 0, mvpMatrix.values, 0);
        Matrix.multiplyMM(mvpMatrix.values, 0,  projectionMatrix.values, 0, vMatrix.values, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix.values, 0);
    }

    private void drawMeshes() {
        synchronized (meshes) {
            for ( SceneActorNode actor : actors ) {

                transform(actor.angleXZ, actor.localPosition );
                GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix.values, 0);
                drawMeshGLES2(sampleEnemy);
            }
        }
    }

    /**
     * @param face
     */
    public void addToVA(GLES1Triangle face) {

        if ( !staticGeometryToAdd.containsKey( face.material ) ) {
            staticGeometryToAdd.put(face.material, new ArrayList<GLES1Triangle>());

        }

        staticGeometryToAdd.get( face.material ).add(face);
    }

    private void addToVAForReal(GLES1Triangle face) {

        GLESVertexArrayManager manager;

        if ( !managers.containsKey( face.material ) ) {
            initManagerForMaterial( face.material, polycount / 10 );
        }
        manager = managers.get(face.material );
        manager.pushIntoFrameAsStatic(face.getVertexData(), face.material.mainColor.getFloatData());
    }

    public void flush() {

        for ( Material m : staticGeometryToAdd.keySet() ) {
            initManagerForMaterial(m, staticGeometryToAdd.get(m).size());
            for ( GLES1Triangle t : staticGeometryToAdd.get( m ) ) {
                addToVAForReal( t );
                t.clear();
            }
            staticGeometryToAdd.get( m ).clear();
            staticGeometryToAdd.remove( staticGeometryToAdd.get( m ) );
        }

        staticGeometryToAdd.clear();

        for ( GLESVertexArrayManager manager : managers.values() ) {
            manager.uploadToGPU();
        }
        ready = true;
    }
}
