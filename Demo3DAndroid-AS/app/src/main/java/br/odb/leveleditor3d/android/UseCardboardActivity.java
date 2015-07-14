package br.odb.leveleditor3d.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.libscene.util.SceneTesselator;
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
                world = (World) new ObjectInputStream(fileInput).readObject();
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

        try {
            Toast.makeText( this, "Loading files...", Toast.LENGTH_SHORT ).show();
            fileInput = getAssets().open( "floor1.opt.ser" );
            LevelLoader loader = new LevelLoader();
            loader.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
