package br.odb.leveleditor3d.android;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Space;

import br.odb.gamelib.android.geometry.GLES1Square;
import br.odb.gamelib.android.geometry.GLES1SquareFactory;
import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESRenderer;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libstrip.IndexedSetFace;
import br.odb.utils.math.Vec3;

public class SceneView extends GLSurfaceView {

    final public ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
    final public Map< LightSource, GroupSector > lightsForPlace = new HashMap< LightSource, GroupSector>();

    LightSource light0 = new LightSource( new Vec3(), 128);

    public void lit( GroupSector s, LightSource ls ) {

        for ( IndexedSetFace isf : s.mesh.faces ) {
   //         ( (GLES1Triangle ) isf ).light = ls.intensity;
        }
    }

    public void findPlaceForLightSource( LightSource ls, World world ) {
        for (SpaceRegion sr : world.getAllRegionsAsList() ) {
            if ( sr instanceof GroupSector ) {
                if ( sr.isInside( ls.position ) ) {
                    lightsForPlace.put( ls, (GroupSector)sr );
                    return;
                }
            }
        }
    }

    public void processLights( World world ) {
        for ( LightSource ls : lightSources ) {
            findPlaceForLightSource( ls, world );
        }
    }


    GLESRenderer renderer;

    public SceneView( Context context ) {
        super( context );
    }

    public SceneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init( InputStream vertex, InputStream fragment) {


		setEGLContextClientVersion(2);
		try {
			String vertexShader = readFully(vertex, "utf8");
			String fragmentShader = readFully(fragment, "utf8");
			renderer = new GLESRenderer( 10000, vertexShader, fragmentShader,
					this.getContext());


			setFocusable(true);
			setClickable(true);
			setLongClickable(true);
			setFocusableInTouchMode(true);
			requestFocus();

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

		return true;
	}


    public void changeHue( GLES1Triangle trig ) {
        switch ( trig.hint ) {
            case W:
                trig.r *= 0.8;
                trig.g *= 0.8;
                trig.b *= 0.8;
                break;
            case E:
                trig.r *= 0.6;
                trig.g *= 0.6;
                trig.b *= 0.6;
                break;
            case N:
                trig.r *= 0.4;
                trig.g *= 0.4;
                trig.b *= 0.4;
                break;
            case S:
                trig.r *= 0.2;
                trig.g *= 0.2;
                trig.b *= 0.2;
                break;
            case FLOOR:
                trig.r *= 0.9;
                trig.g *= 0.9;
                trig.b *= 0.9;
                break;
            case CEILING:
                trig.r *= 0.1;
                trig.g *= 0.1;
                trig.b *= 0.1;
                break;

        }
    }

    int polyCount = 0;

    private void loadGeometryFromScene(GroupSector sector) {

        GLES1TriangleFactory factory = GLES1TriangleFactory.getInstance();
        GLES1Triangle trig;
		for (IndexedSetFace isf : sector.mesh.faces) {
            ++polyCount;
            changeHue( (GLES1Triangle) isf );
            ((GLES1Triangle) isf).flushToGLES();
            renderer.addGeometryToScene((GLES1Triangle) isf);
            //renderer.addToVA((br.odb.gamelib.android.geometry.GLESIndexedSetFace) isf);
		}

		for (SpaceRegion sr : sector.getSons()) {
			if (sr instanceof GroupSector) {
				loadGeometryFromScene((GroupSector) sr);
			}
		}
	}

	public void setScene(World scene) {
		renderer.clearScreenGeometry();
		loadGeometryFromScene(scene.masterSector);

        int lightsLeft = 20;
        int skip = 2;
        for (SpaceRegion sr : scene.getAllRegionsAsList() ) {
            if ( sr instanceof GroupSector && lightsLeft > 0 ) {

                skip--;

                if ( skip == 0 ) {
                    skip = 2;
                    lightsLeft--;

                    lightSources.add( new LightSource( sr.getAbsoluteCenter(), 128 ) );
                }
            }
        }


        processLights( scene );
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
