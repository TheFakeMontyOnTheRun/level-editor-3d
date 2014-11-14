package br.odb.demo3d;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.opengl.GLSurfaceView;
import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESRenderer;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libstrip.IndexedSetFace;

public class SceneView extends GLSurfaceView {

	GLESRenderer renderer;
	
	public SceneView(Context context, InputStream vertex, InputStream fragment ) {
		super(context);
		setEGLContextClientVersion(2);
		try {
			String vertexShader = readFully( vertex, "utf8" );
			String fragmentShader = readFully( fragment, "utf8" );
			renderer = new GLESRenderer( 1000, vertexShader, fragmentShader);
			
//			GLES1Triangle trig = GLES1TriangleFactory.getInstance().makeTrig( -1.0f, 1.0f, -5.0f, 1.0f, 1.0f, -5.0f, 0.0f, -1.0f, -5.0f, 0xFFFF0000, null );
//			trig.flushToGLES();
//			
//			renderer.addGeometryToScene( trig );
			
			setRenderer(renderer);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private void loadGeometryFromScene(GroupSector sector) {
		
		for ( IndexedSetFace isf : sector.mesh.faces ) {
			renderer.addGeometryToScene( GLES1TriangleFactory.getInstance().makeTrigFrom( isf ) );
		}
		
		for ( SpaceRegion sr : sector.getSons() ) {
			if ( sr instanceof GroupSector ) {
				loadGeometryFromScene( (GroupSector) sr );
			}
		}
	}
	
	public void setScene( World scene ) {
		renderer.clearScreenGeometry();
		loadGeometryFromScene( scene.masterSector );
	}
	
	
	public String readFully(InputStream inputStream, String encoding)
	        throws IOException {
	    return new String(readFully(inputStream), encoding);
	}    

	private byte[] readFully(InputStream inputStream)
	        throws IOException {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int length = 0;
	    while ((length = inputStream.read(buffer)) != -1) {
	        baos.write(buffer, 0, length);
	    }
	    return baos.toByteArray();
	}

}
