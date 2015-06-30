package br.odb.leveleditor3d.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Presentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaRouter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewManager;

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

public class MainActivity extends Activity implements View.OnClickListener {


    //    volatile int gameId;
//    volatile int playerId;

//    void startNetGame() {
//        String response = makeRequest("http://localhost:8080/multiplayer-server/GetGameId?gameType=1");
//        gameId = Integer.parseInt(response);
//    }
//
//    public static String makeRequest(String url) {
//
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse response = httpclient.execute(new HttpGet(url));
//            StatusLine statusLine = response.getStatusLine();
//
//            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                response.getEntity().writeTo(out);
//                String responseString = out.toString();
//                out.close();
//
//
//                return responseString;
//
//            } else {
//                //Closes the connection.
//                response.getEntity().getContent().close();
//                return null;
//            }
//        } catch (Exception e) {
//            return null;
//        }
//    }

    volatile ProgressDialog progressDialog;
    volatile String filename;
    volatile SceneView view;
    volatile World world;
    volatile InputStream vertexShader;
    volatile InputStream fragmentShader;
    volatile InputStream fileInput;
    private MediaRouter mMediaRouter;
    private MediaRouter.RouteInfo mRouteInfo;



//    SVGRenderingNode[] nodes = new SVGRenderingNode[ 4 ];
//    int currentFrame;
//    private DisplayList gunImages;

//    void updateOverlay() {
//
//        for( SVGRenderingNode node : nodes ) {
//            node.setVisible( false );
//        }
//
//        nodes[ currentFrame ].setVisible(true);
//
//        Log.d("frame", "" + currentFrame);
//
//        currentFrame = ( currentFrame + 1 ) % 4;
//    }


    private class LevelLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                //world = WorldLoader.build(fileInput);
                world = (World) new ObjectInputStream(fileInput).readObject();

                SceneTesselator tesselator = new SceneTesselator(new GLES1TriangleFactory());

                tesselator.generateSubSectorQuadsForWorld(world);

                view.setScene(world);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            view.renderer.camera.angleXZ = 180.0f;
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
                                if (((Sector) sr).isInside(view.renderer.camera.localPosition)) {
                                   // System.out.println("got inside " + c);
                                    lastValidPosition.set(view.renderer.camera.localPosition);
                                    inside = true;
                                }
                            }

                        }

                        if (!inside) {
                            view.renderer.camera.localPosition.set(lastValidPosition);
                        }

                        map.position.set( view.renderer.camera .localPosition);
                    }
                }
            }).start();

            progressDialog.cancel();

            for (index = size - 1; index >= 0; --index) {
                if (srs.get(index) instanceof GroupSector) {
                    view.renderer.camera.localPosition
                            .set(((GroupSector) srs.get(index)).getAbsoluteCenter());
                    Vec3 pos = new Vec3(view.renderer.camera.localPosition);


                    view.spawnActor(pos.add(new Vec3(10.0f, 0.0f, 10.0f)), 180.0f);
                    view.spawnActor(pos.add(new Vec3(30.0f, 0.0f, 30.0f)), 0.0f);
                    view.spawnActor(pos.add(new Vec3(20.0f, 0.0f, 20.0f)), 90.0f);
                    view.renderer.ready = true;
                    return;
                }
            }
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

    //    attemptToUseSecondScreenWith(findViewById(R.id.svScene));
    }

    private void attemptToUseSecondScreenWith( View view ) {
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

//    DisplayList loadGunImages() throws IOException {
//        SVGGraphic walk1 = SVGParsingUtils.readSVG(getAssets().open("guns_walk1.svg"));
//        SVGGraphic walk2 = SVGParsingUtils.readSVG(getAssets().open("guns_walk2.svg"));
//        SVGGraphic walk3 = SVGParsingUtils.readSVG(getAssets().open("guns_walk3.svg"));
//        SVGGraphic walk4 = SVGParsingUtils.readSVG(getAssets().open("guns_walk4.svg"));
//
//        nodes[ 0 ] = new SVGRenderingNode( walk1, "overlay-walk1" );
//        nodes[ 1 ] = new SVGRenderingNode( walk2, "overlay-walk2" );
//        nodes[ 2 ] = new SVGRenderingNode( walk3, "overlay-walk3" );
//        nodes[ 3 ] = new SVGRenderingNode( walk4, "overlay-walk4" );
//
//        DisplayList dl = new DisplayList( "overlay");
//        dl.setItems( nodes );
//
//        return dl;
//    }
//
//    private void setOverlay( GameView overlay, DisplayList dl ) {
//
//        if ( overlay == null ) {
//            return;
//        }
//
//        for ( SVGRenderingNode node : nodes ) {
//            node.graphic = node.graphic.scaleTo(overlay.getWidth(), overlay.getHeight());
//        }
//
//        overlay.setRenderingContent( dl );
//    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            return true;
        }
        return view.onKeyDown( keyCode, event );
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private final static class GamePresentation extends Presentation {

        final View canvas;

        public GamePresentation(Context context, Display display, View gameView ) {
            super(context, display);

            this.canvas = gameView;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView( canvas);
        }
    }
}
