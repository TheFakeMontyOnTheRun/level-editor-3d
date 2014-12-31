package br.odb.leveleditor3d.android;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import br.odb.gamelib.android.geometry.GLES1Square;
import br.odb.gamelib.android.geometry.GLES1SquareFactory;
import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESRenderer;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libstrip.IndexedSetFace;

public class SceneView extends GLSurfaceView {

	GLESRenderer renderer;

	public SceneView(Context context, InputStream vertex, InputStream fragment) {
		super(context);
		setEGLContextClientVersion(2);
		try {
			String vertexShader = readFully(vertex, "utf8");
			String fragmentShader = readFully(fragment, "utf8");
			renderer = new GLESRenderer(1000, vertexShader, fragmentShader,
					this.getContext());

			// GLES1Triangle trig = GLES1TriangleFactory.getInstance().makeTrig(
			// -1.0f, 1.0f, -5.0f, 1.0f, 1.0f, -5.0f, 0.0f, -1.0f, -5.0f,
			// 0xFFFFFF00, null);
			// trig.flushToGLES();
			// trig.setTextureCoordenates(new float[] { 0.0f, 0.0f, 0.0f, 1.0f,
			// 1.0f, 0.0f });
			//
			// renderer.addGeometryToScene(trig);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		Log.d( "Demo3D" , "angle: " + renderer.angle );
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			renderer.angle -= 10;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			renderer.angle += 10;
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			renderer.camera.x += 10 * Math.sin(renderer.angle
					* (Math.PI / 180.0f));
			renderer.camera.z -= 10 * Math.cos(renderer.angle
					* (Math.PI / 180.0f));
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

	private void loadGeometryFromScene(GroupSector sector) {

		for (IndexedSetFace isf : sector.mesh.faces) {
			renderer.addGeometryToScene(GLES1TriangleFactory.getInstance()
					.makeTrigFrom(isf));
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
