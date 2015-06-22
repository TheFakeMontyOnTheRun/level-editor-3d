/**
 * @author Daniel Monteiro
 */

package br.odb.gamelib.android.geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.utils.math.Vec3;

/*
 * Concerns:
 * - show the world from the camera's view.
 * - do it in the most efficient way
 * - so, work to keep showing stuff efficiently
 * */

public class GLESRenderer implements GLSurfaceView.Renderer {

    public final Vec3 camera = new Vec3();
    private final int polycount;
    private final Context context;
    public float angle;
    private GLESVertexArrayManager fixedGeometryManager;
    //final GLESVertexArrayManager manager = new GLESVertexArrayManager();

    final HashMap<Material, ArrayList< GLES1Triangle> > staticGeometryToAdd= new HashMap<Material, ArrayList<GLES1Triangle>>();
    final HashMap<Material, GLESVertexArrayManager > managers = new HashMap<Material, GLESVertexArrayManager >();
    final private ArrayList<GLES1Triangle> sceneGeometryToRender;
    final public ArrayList<GLES1Triangle> fixedScreenShapesToRender;
    final public ArrayList<GLES1Triangle> screenShapesToRender;
    public final ArrayList<GLESMesh> meshes = new ArrayList<>();
    private boolean shouldCheckForBailingOut;

    final public ArrayList<Vec3> actors = new ArrayList<Vec3>();
    public final List<GLES1Triangle> sampleEnemy = new ArrayList<GLES1Triangle>();

    //GLES2 stuff
    private int mProgram;
    private int maPositionHandle;
    private int colorHandle;
    private String vertexShaderCode;
    private String fragmentShaderCode;
    	int textureIndex;
    private int mTextureCoordinateHandle = -1;
    private int mTextureUniformHandle = -1;
    private int muMVPMatrixHandle;
    private float[] mMVPMatrix = new float[16];
    private float[] mMMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    public volatile boolean ready;

    /**
     * @param type       GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @param shaderCode proper code for shader
     * @return
     */
    private int loadShader(int type, String shaderCode) {

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);


        return shader;
    }

    /**
     * @param maxVisiblePolys
     * @param vertexShader
     * @param fragmentShader
     * @param context
     */
    public GLESRenderer(int maxVisiblePolys, String vertexShader,
                        String fragmentShader, Context context) {
        super();
        this.context = context;
        this.polycount = maxVisiblePolys;
        this.vertexShaderCode = vertexShader;
        this.fragmentShaderCode = fragmentShader;

        sceneGeometryToRender = new ArrayList<GLES1Triangle>();
        screenShapesToRender = new ArrayList<GLES1Triangle>();
        fixedScreenShapesToRender = new ArrayList<GLES1Triangle>();
    }

    void initManagerForMaterial( Material mat, int polys ) {

        GLESVertexArrayManager manager = new GLESVertexArrayManager();

        manager.init(polys);
        manager.flush();

        if ( this.textureIndex != -1 ) {
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
     * @param isf
     */
    public void addGeometryToScene(GLES1Triangle isf) {
		isf.setTextureCoordenates( new float[] {
				0.0f, 0.0f,
		        0.0f, 1.0f,
		        1.0f, 0.0f
				}

				);

        sceneGeometryToRender.add(isf);
    }

    /**
     * @param s
     */
    public void addGeometryToScreen(GLES1Triangle s) {
        screenShapesToRender.add(s);

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

        Matrix.frustumM(mProjMatrix, 0, xmin, xmax, ymin, ymax, 0.1f, 10000.0f);
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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


		textureIndex = -1;//loadTexture( context , R.drawable.tex );

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES10.GL_DEPTH_TEST);

        GLES20.glDepthFunc(GLES10.GL_LEQUAL);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

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

        if ( this.textureIndex != -1 ) {
            mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
            mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIndex);
            GLES20.glUniform1i(mTextureUniformHandle, 0);
        }




        if (ready) {
            renderSceneGLES20();
        }
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
     * @param Angle
     */
    public void setAngle(float Angle) {
        angle = Angle;
        this.needsToResetView(true);
    }

    /**
     *
     */
    private void setCamera() {
        mVMatrix = new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                1,};

        Matrix.rotateM(mVMatrix, 0, angle, 0, 1.0f, 0);

        Matrix.translateM(mVMatrix, 0, -camera.x, -camera.y, -camera.z);

        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

        // Apply a ModelView Projection transformation
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

    }

    /**
     *
     */
    private void renderSceneGLES20() {


        GLES20.glUseProgram(mProgram);


        if (shouldCheckForBailingOut) {
            return;
        }


        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

        setCamera();

        if (fixedGeometryManager != null) {
            fixedGeometryManager.flush();
            fixedGeometryManager.drawGLES2(maPositionHandle, colorHandle, this.mTextureCoordinateHandle);
        }

        for (GLES1Triangle face : sceneGeometryToRender) {
            face.drawGLES2(maPositionHandle, colorHandle, this.mTextureCoordinateHandle );
        }

        synchronized (meshes) {
            for (GeneralTriangleMesh mesh : meshes) {
                drawMeshGLES2(mesh);
            }
        }


        GLESVertexArrayManager manager;

        for ( Material mat : managers.keySet() ) {

            manager = managers.get( mat );
            manager.flush();
            manager.drawGLES2(maPositionHandle, colorHandle, this.mTextureCoordinateHandle);
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


    public void initCube(GeneralTriangleMesh cubeToInit) {
        for (GLES1Triangle t : sampleEnemy) {
            cubeToInit.faces.add(t.makeCopy());
        }
    }

    /**
     * @param fastReset
     */
    public synchronized void needsToResetView(boolean fastReset) {
        shouldCheckForBailingOut = fastReset;
    }

    /**
     * @param graphic
     */
    public void addToFixedGeometryToScreen(GLES1Triangle[] graphic) {

        for ( GLES1Triangle t : graphic ) {
            t.flatten(-1.0f);
            t.flush();
            this.fixedScreenShapesToRender.add(t);
        }
    }

    /**
     *
     */
    public void detach() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (fixedGeometryManager != null)
            fixedGeometryManager.reinit();

        angle = 0;

        for ( GLESVertexArrayManager manager : managers.values() ) {
            manager.reinit();
        }

        sceneGeometryToRender.clear();
        screenShapesToRender.clear();
        meshes.clear();
        fixedGeometryManager = null;
    }

    /**
     *
     */
    public void clearScreenGeometry() {
        screenShapesToRender.clear();

        for ( GLESVertexArrayManager manager : managers.values() ) {
            manager.clear();
        }
    }

    /**
     * @param graphic
     */
    public void addToMovingGeometryToScreen(GLES1Triangle[] graphic) {

        for ( GLES1Triangle t : graphic ) {
            t.flatten(-1.0f);
            t.flush();
            this.addGeometryToScreen(t);
        }
    }

    public void flush() {

        for ( Material m : staticGeometryToAdd.keySet() ) {
            initManagerForMaterial( m, staticGeometryToAdd.get( m ).size() );
            for ( GLES1Triangle t : staticGeometryToAdd.get( m ) ) {
                addToVAForReal( t );
            }
            staticGeometryToAdd.get( m ).clear();
        }

        staticGeometryToAdd.clear();
    }
}
