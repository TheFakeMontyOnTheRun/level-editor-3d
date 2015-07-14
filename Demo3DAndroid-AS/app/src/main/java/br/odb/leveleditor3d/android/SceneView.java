package br.odb.leveleditor3d.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.odb.SceneActorNode;
import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESMesh;
import br.odb.gamelib.android.geometry.GLESRenderer;
import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;

//public class SceneView extends GLSurfaceView implements Runnable {
public class SceneView extends GLSurfaceView {
    private final Context context;

//    final public Map<LightSource, GroupSector> lightsForPlace = new HashMap<LightSource, GroupSector>();
//    LightSource light0 = new LightSource(new Vec3(), 128);
//    final public ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
//    public void lit(GroupSector s, LightSource ls) {
//
//        for (GeneralTriangle isf : s.mesh.faces) {
//            //         ( (GLES1Triangle ) isf ).light = ls.intensity;
//        }
//    }
//
//    public void findPlaceForLightSource(LightSource ls, World world) {
//        for (SceneNode sr : world.getAllRegionsAsList()) {
//            if (sr instanceof GroupSector) {
//                if (((GroupSector) sr).isInside(ls.position)) {
//                    lightsForPlace.put(ls, (GroupSector) sr);
//                    return;
//                }
//            }
//        }
//    }
//
//    public void processLights(World world) {
//        for (LightSource ls : lightSources) {
//            findPlaceForLightSource(ls, world);
//        }
//    }


//    public static final String SERVER = "http://192.241.246.87:8080/MServerTest";
//
//
//    void sendPosition(int id) throws IOException {
//
//        String query = String.format("id=%s&x=%s&y=%s&z=%s",
//                URLEncoder.encode("" + id, "UTF8"),
//                URLEncoder.encode("" + renderer.camera.x, "UTF8"),
//                URLEncoder.encode("" + renderer.camera.y, "UTF8"),
//                URLEncoder.encode("" + renderer.camera.z, "UTF8")
//        );
//
//
//        String received = blockSendHTTPGet(SERVER + "/Server?" + query);
//        String[] positions = received.split(";");
//
//        String[] coords;
//        Vec3 v;
//
//        synchronized (renderer.meshes) {
//
//            renderer.actors.clear();
//            renderer.meshes.clear();
//
//            for (String pos : positions) {
//                coords = pos.split("[ ]+");
//                v = new Vec3();
//
//                v.x = Float.parseFloat(coords[0]);
//                v.y = Float.parseFloat(coords[1]);
//                v.z = Float.parseFloat(coords[2]);
//
//                spawnActor(v);
//            }
//        }
//    }
//
//    String blockSendHTTPGet(final String url) {
//        String msg = "";
//        try {
//            URL urlObj = new URL(url);
//            URLConnection connection;
//            connection = urlObj.openConnection();
//            HttpURLConnection httpConnection = (HttpURLConnection) connection;
//
//            int responseCode = httpConnection.getResponseCode();
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                InputStream in = httpConnection.getInputStream();
//
//                InputStreamReader i = new InputStreamReader(in);
//                BufferedReader str = new BufferedReader(i);
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = str.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                msg = sb.toString();
//
//
//            } else {
//                System.out.println("Error code: " + responseCode);
//            }
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return msg;
//    }
//
//    @Override
//    public void run() {
//
//        if ( true ) {
//            return;
//        }
//
//        String data = "" + blockSendHTTPGet(SERVER + "/GetId").trim().charAt(0);
//
//        if (data == null || data.length() == 0) {
//            return;
//        }
//
//        int id = Integer.parseInt(data);
//
//        System.out.println("Player Id:" + id);
//
//        while (true) {
//
//            try {
//                sendPosition(id);
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }

    public void spawnActor(Vec3 v, float angleXZ) {
        SceneActorNode actor = new SceneActorNode( "actor@" + v.toString() );
        actor.localPosition.set( v );
        actor.angleXZ = angleXZ;
        renderer.actors.add( actor );
    }

    GLESRenderer renderer;
    int polyCount = 0;


    public SceneView(Context context) {
        super(context);

        this.context = context;
    }

    public SceneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(InputStream vertex, InputStream fragment) {

        setEGLContextClientVersion(2);

        try {
            String vertexShader =  LevelEditor3DApplication.readFully(vertex, "utf8");
            String fragmentShader = LevelEditor3DApplication.readFully(fragment, "utf8");

            renderer = new GLESRenderer(10000, vertexShader, fragmentShader);

            initDefaultMeshForActor();

            setRenderer(renderer);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initDefaultMeshForActor() throws IOException {
        GeneralTriangleMesh enemy;
        WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
        List<Material> mats = matLoader.parseMaterials( context.getAssets().open( "gargoyle.mtl" ) );

        WavefrontOBJLoader loader = new WavefrontOBJLoader( new GLES1TriangleFactory() );
        ArrayList<GeneralTriangleMesh> mesh = (ArrayList<GeneralTriangleMesh>) loader.loadMeshes( context.getAssets().open("gargoyle.obj"), mats );

        enemy = mesh.get( 0 );

        for ( GeneralTriangle gt : enemy.faces ) {
            renderer.sampleEnemy.faces.add(GLES1TriangleFactory.getInstance().makeTrigFrom(gt));
        }
    }

    public void onLeft() {
        renderer.camera.angleXZ -= 10;
    }

    public void onRight() {
        renderer.camera.angleXZ += 10;
    }

    public void onWalkForward() {
        renderer.camera.localPosition.x += 10 * Math.sin(renderer.camera.angleXZ
                * (Math.PI / 180.0f));
        renderer.camera.localPosition.z -= 10 * Math.cos(renderer.camera.angleXZ
                * (Math.PI / 180.0f));
    }

    @Override
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
                renderer.camera.localPosition.x -= 10 * Math.sin(renderer.camera.angleXZ
                        * (Math.PI / 180.0f));
                renderer.camera.localPosition.z += 10 * Math.cos(renderer.camera.angleXZ
                        * (Math.PI / 180.0f));
                break;

            case KeyEvent.KEYCODE_COMMA:
                renderer.camera.localPosition.x += 10 * Math.sin((renderer.camera.angleXZ - 90)
                        * (Math.PI / 180.0f));
                renderer.camera.localPosition.z -= 10 * Math.cos((renderer.camera.angleXZ - 90)
                        * (Math.PI / 180.0f));
                break;
            case KeyEvent.KEYCODE_PERIOD:
                renderer.camera.localPosition.x -= 10 * Math.sin((renderer.camera.angleXZ - 90)
                        * (Math.PI / 180.0f));
                renderer.camera.localPosition.z += 10 * Math.cos((renderer.camera.angleXZ - 90)
                        * (Math.PI / 180.0f));
                break;

            case KeyEvent.KEYCODE_A:
                renderer.camera.localPosition.y += 10.0;
                break;
            case KeyEvent.KEYCODE_Z:
                renderer.camera.localPosition.y -= 10.0;
                break;
            case KeyEvent.KEYCODE_Q:
                System.exit(0);
                break;
            case KeyEvent.KEYCODE_BACK:
                return false;
        }


        Log.d("head", "v:" + renderer.camera.angleXZ);

        return true;
    }

    private void loadGeometryFromScene(GroupSector sector) {

        for (GeneralTriangle isf : sector.mesh.faces) {
            ++polyCount;
            renderer.changeHue((GLES1Triangle) isf);
            isf.flush();
            renderer.addToVA((GLES1Triangle) isf);
        }

        for (SceneNode sr : sector.getSons()) {
            if (sr instanceof GroupSector) {
                loadGeometryFromScene((GroupSector) sr);
            }
        }
    }

    public void setScene(World scene) {
        renderer.clearScreenGeometry();
        loadGeometryFromScene(scene.masterSector);
        renderer.flush();
    }
}

