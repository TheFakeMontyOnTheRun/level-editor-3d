package br.odb.leveleditor3d.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.KeyEvent;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.odb.libstrip.builders.GeneralTriangleFactory;
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
//    private void spawnCube(Vec3 v) {
//        GLESMesh cube = new GLESMesh("" + renderer.cubes.size());
//        renderer.actors.add(v);
//        renderer.meshes.add(cube);
//        renderer.initCube(cube);
//        cube.translate(v);
//    }
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
//                spawnCube(v);
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

    GLESRenderer renderer;
    int polyCount = 0;
    GeneralTriangleMesh enemy;

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
            String vertexShader = readFully(vertex, "utf8");
            String fragmentShader = readFully(fragment, "utf8");

            renderer = new GLESRenderer(10000, vertexShader, fragmentShader,
                    this.getContext());


            WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
            List<Material> mats = matLoader.parseMaterials( context.getAssets().open( "gargoyle.mtl" ) );

            WavefrontOBJLoader loader = new WavefrontOBJLoader( new GLES1TriangleFactory() );
            ArrayList<GeneralTriangleMesh> mesh = (ArrayList<GeneralTriangleMesh>) loader.loadMeshes( context.getAssets().open("gargoyle.obj"), mats );

            enemy = mesh.get( 0 );
//            enemy.scale( 10.0f );
            renderer.meshes.add( enemy );


//            setFocusable(true);
//            setClickable(true);
//            setLongClickable(true);
//            setFocusableInTouchMode(true);
//            requestFocus();
            setRenderer(renderer);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onLeft() {
        renderer.angle -= 10;
    }

    public void onRight() {
        renderer.angle += 10;
    }

    public void onWalkForward() {
        renderer.camera.x += 10 * Math.sin(renderer.angle
                * (Math.PI / 180.0f));
        renderer.camera.z -= 10 * Math.cos(renderer.angle
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
                renderer.camera.x -= 10 * Math.sin(renderer.angle
                        * (Math.PI / 180.0f));
                renderer.camera.z += 10 * Math.cos(renderer.angle
                        * (Math.PI / 180.0f));
                break;

            case KeyEvent.KEYCODE_COMMA:
                renderer.camera.x += 10 * Math.sin((renderer.angle - 90)
                        * (Math.PI / 180.0f));
                renderer.camera.z -= 10 * Math.cos((renderer.angle - 90)
                        * (Math.PI / 180.0f));
                break;
            case KeyEvent.KEYCODE_PERIOD:
                renderer.camera.x -= 10 * Math.sin((renderer.angle - 90)
                        * (Math.PI / 180.0f));
                renderer.camera.z += 10 * Math.cos((renderer.angle - 90)
                        * (Math.PI / 180.0f));
                break;

            case KeyEvent.KEYCODE_A:
                renderer.camera.y += 10.0;
                break;
            case KeyEvent.KEYCODE_Z:
                renderer.camera.y -= 10.0;
                break;
            case KeyEvent.KEYCODE_Q:
                System.exit(0);
                break;
            case KeyEvent.KEYCODE_BACK:
                return false;
        }

        enemy.translateTo( renderer.camera.add( new Vec3( 0.0f, 0.0f, 0.0f ) ) );

        return true;
    }


    public void changeHue(GLES1Triangle trig) {
        trig.material = new Material(null, new Color(trig.material.mainColor), null, null, null);

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

    private void loadGeometryFromScene(GroupSector sector) {

        GLES1TriangleFactory factory = GLES1TriangleFactory.getInstance();
        GLES1Triangle trig;

        for (GeneralTriangle isf : sector.mesh.faces) {
            ++polyCount;
            changeHue((GLES1Triangle) isf);
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

    public String readFully(InputStream inputStream, String encoding)
            throws IOException {
        return new String(readFully(inputStream), encoding);
    }

    private byte[] readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toByteArray();
    }
}

