package br.odb.leveleditor3d.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.Direction;
import br.odb.utils.math.Vec3;

public class UseCardboardActivity extends CardboardActivity {

    private static final String TAG = "MainActivity";
    private CardboardRenderer renderer;
    volatile World world;
    volatile InputStream fileInput;
    private CardboardView cardboardView;

    private class LevelLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                //world = WorldLoader.build(fileInput);
                world = (World) new ObjectInputStream(fileInput).readObject();

//                GroupSector theCube = new GroupSector( "thecube" );

//                world = new World( new GroupSector( "master" ) );

//                Sector subCube = new Sector( "subcube" );
//                world.masterSector.addChild( theCube );
//                theCube.localPosition.set( 1, -10 ,1 );
//                theCube.size.set( 50, 50, 50 );
//                subCube.size.set( theCube.size );
//                theCube.addChild( subCube );
//
//                theCube.shades.put(Direction.N, new Material( Direction.N + "_", new Color( 255, 0, 0 ), null, null, null ) );
//                theCube.shades.put(Direction.S, new Material( Direction.N + "_", new Color( 0, 255, 0 ), null, null, null ) );
//                theCube.shades.put(Direction.W, new Material( Direction.N + "_", new Color( 0, 0, 255 ), null, null, null ) );
//                theCube.shades.put(Direction.E, new Material( Direction.N + "_", new Color( 255, 255, 0 ), null, null, null ) );
//                theCube.shades.put(Direction.FLOOR, new Material( Direction.N + "_", new Color( 0, 255, 255 ), null, null, null ) );
//                theCube.shades.put(Direction.CEILING, new Material( Direction.N + "_", new Color( 255, 0, 255 ), null, null, null ) );

                SceneTesselator tesselator = new SceneTesselator(new GLES1TriangleFactory());
                tesselator.generateSubSectorQuadsForWorld(world);

                renderer.setScene(world);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            renderer.ready = true;
            Toast.makeText( UseCardboardActivity.this, "Done!", Toast.LENGTH_SHORT ).show();

            renderer.cameraNode.angleXZ = 180.0f;
            final List<SceneNode> srs = world.getAllRegionsAsList();

            int size = srs.size();
            int index = 0;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    Vec3 lastValidPosition = new Vec3();
                    boolean inside;
                    while (true) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        SceneNode sr;

                        inside = false;

                        for (int c = 0; c < srs.size(); ++c) {

                            sr = srs.get(c);

                            if (sr instanceof Sector) {
                                if (((Sector) sr).isInside(renderer.cameraNode.localPosition)) {
                                    // System.out.println("got inside " + c);
                                    lastValidPosition.set(renderer.cameraNode.localPosition);
                                    inside = true;
                                }
                            }

                        }

                        if (!inside) {
                            renderer.cameraNode.localPosition.set(lastValidPosition);
                        }
                    }
                }
            }).start();

            for (index = size - 1; index >= 0; --index) {
                if (srs.get(index) instanceof GroupSector) {
                    renderer.cameraNode.localPosition.set(((GroupSector) srs.get(index)).getAbsoluteCenter());
//                    Vec3 pos = new Vec3(view.renderer.camera.localPosition);
//                    view.spawnActor(pos.add(new Vec3(10.0f, 0.0f, 10.0f)), 180.0f);
//                    view.spawnActor(pos.add(new Vec3(30.0f, 0.0f, 30.0f)), 0.0f);
//                    view.spawnActor(pos.add(new Vec3(20.0f, 0.0f, 20.0f)), 90.0f);
//                    view.renderer.ready = true;
                    return;
                }
            }
        }
    }

    /**
     * Sets the view to our CardboardView and initializes the transformation matrices we will use
     * to render our scene.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_ui);
        cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        this.renderer = new CardboardRenderer( this );
        cardboardView.setRenderer( renderer );
        setCardboardView(cardboardView);
//        cardboardView.setVRModeEnabled( false );

        InputStream vertexShaderIS = null;
        InputStream fragmentShaderIS = null;
        String vertexShader = null;
        String fragmentShader = null;

        try {
            Toast.makeText( this, "Loading files...", Toast.LENGTH_SHORT ).show();
            fileInput = getAssets().open( "floor1.opt.ser" );
            vertexShaderIS = getAssets().open("vertex.glsl");
            fragmentShaderIS = getAssets().open("fragment.glsl");
            vertexShader = readFully(vertexShaderIS, "utf8");
            fragmentShader = readFully(fragmentShaderIS, "utf8");
            LevelLoader loader = new LevelLoader();
            loader.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDefaultMeshForActor() throws IOException {
        GeneralTriangleMesh enemy;
        WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
        List<Material> mats = matLoader.parseMaterials(getAssets().open("gargoyle.mtl"));

        WavefrontOBJLoader loader = new WavefrontOBJLoader( new GLES1TriangleFactory() );
        ArrayList<GeneralTriangleMesh> mesh = (ArrayList<GeneralTriangleMesh>) loader.loadMeshes( getAssets().open("gargoyle.obj"), mats );

        enemy = mesh.get( 0 );

        for ( GeneralTriangle gt : enemy.faces ) {
            renderer.sampleEnemy.faces.add(GLES1TriangleFactory.getInstance().makeTrigFrom(gt));
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            return true;
        }


        boolean consumed = renderer.onKeyDown( keyCode, event );

        this.cardboardView.invalidate();

        return consumed;
    }
}
