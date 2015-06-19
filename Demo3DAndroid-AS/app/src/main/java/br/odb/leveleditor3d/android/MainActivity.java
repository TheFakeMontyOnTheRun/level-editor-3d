package br.odb.leveleditor3d.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Presentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaRouter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import br.odb.gamelib.android.GameView;
import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.GeneralTriangle;
import br.odb.utils.Direction;
import br.odb.utils.math.Vec3;

public class MainActivity extends Activity implements View.OnClickListener {

    volatile ProgressDialog progressDialog;
    volatile String filename;
    volatile SceneView view;
    volatile World world;
    volatile InputStream vertexShader;
    volatile InputStream fragmentShader;
    volatile InputStream fileInput;
    volatile int gameId;
    volatile int playerId;
    private MediaRouter mMediaRouter;
    private MediaRouter.RouteInfo mRouteInfo;

    private class LevelLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                //world = WorldLoader.build(fileInput);

                world = (World) new ObjectInputStream(fileInput).readObject();


                SceneTesselator tesselator = new SceneTesselator(new GLES1TriangleFactory());

                tesselator.generateSubSectorQuadsForWorld(world);

                view.setScene(world);


                SpaceRegion sr = new SpaceRegion("dummy");

                sr.size.scale(10);

                for (Direction d : Direction.values()) {
                    for (GeneralTriangle trig : tesselator.generateQuadFor(d, sr)) {

                        view.changeHue((GLES1Triangle) trig);
                        view.renderer.cube.add((GLES1Triangle) trig);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            view.renderer.angle = 180.0f;
            final MapView map = (MapView) findViewById( R.id.map );
            final List<SceneNode> srs = world.getAllRegionsAsList();

            for ( SceneNode sn : srs ) {
                if ( sn instanceof GroupSector ) {
                    map.sectors.add((GroupSector) sn);
                }
            }

            int size = srs.size();
            int index = 0;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    new Thread(view).start();

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
                                if (((Sector) sr).isInside(view.renderer.camera)) {
                                    System.out.println("got inside " + c);
                                    lastValidPosition.set(view.renderer.camera);
                                    inside = true;
                                    view.lit((GroupSector) sr.parent, view.light0);
                                }
                            }

                        }

                        if (!inside) {
                            view.renderer.camera.set(lastValidPosition);
                        }

                        map.position.set( view.renderer.camera );
                    }
                }
            })
                    .start()
            ;

            progressDialog.cancel();
            view.renderer.ready = true;

            for (index = size - 1; index >= 0; --index) {
                if (srs.get(index) instanceof GroupSector) {
                    view.renderer.camera
                            .set(((GroupSector) srs.get(index)).getAbsoluteCenter());
                    return;
                }
            }
        }
    }

    void startNetGame() {
        String response = makeRequest("http://localhost:8080/multiplayer-server/GetGameId?gameType=1");
        gameId = Integer.parseInt(response);
    }

    public static String makeRequest(String url) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();


                return responseString;

            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnWalk).setOnClickListener(this);

        try {
            vertexShader = getAssets().open("vertex.glsl");
            fragmentShader = getAssets().open("fragment.glsl");
            filename = getIntent().getStringExtra("level");
            fileInput = getAssets().open(filename);

        } catch (IOException e) {
            e.printStackTrace();
        }


        view = (SceneView) this.findViewById(R.id.svScene);
        view.init(vertexShader, fragmentShader);

        LevelLoader loader = new LevelLoader();

        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...");
        loader.execute();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mMediaRouter = (MediaRouter)getSystemService(Context.MEDIA_ROUTER_SERVICE);

            mRouteInfo = mMediaRouter.getSelectedRoute( MediaRouter.ROUTE_TYPE_LIVE_VIDEO );

            if ( mRouteInfo != null ) {

                Display presentationDisplay = mRouteInfo.getPresentationDisplay();

                if ( presentationDisplay != null ) {
                    ((ViewManager) view.getParent()).removeView( view );
                    Presentation presentation = new GamePresentation( this, presentationDisplay, view );
                    presentation.show();
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnLeft:
                this.view.onLeft();
                break;

            case R.id.btnRight:
                this.view.onRight();
                break;

            case R.id.btnWalk:
                this.view.onWalkForward();
                break;

        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private final static class GamePresentation extends Presentation {

        final SceneView canvas;

        public GamePresentation(Context context, Display display, SceneView gameView ) {
            super(context, display);

            this.canvas = gameView;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // Be sure to call the super class.
            super.onCreate(savedInstanceState);

            // Get the resources for the context of the presentation.
            // Notice that we are getting the resources from the context of the presentation.
            Resources r = getContext().getResources();

            // Inflate the layout.
            setContentView(canvas );
        }
    }
}
