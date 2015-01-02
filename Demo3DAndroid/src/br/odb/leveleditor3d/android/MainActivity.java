package br.odb.leveleditor3d.android;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import br.odb.libscene.SceneTesselator;
import br.odb.libscene.World;
import br.odb.libscene.WorldLoader;
import br.odb.libscene.SpaceRegion;

public class MainActivity extends Activity {

	SceneView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			InputStream vertexShader;
			vertexShader = getAssets().open( "vertex.glsl" );
			InputStream fragmentShader = getAssets().open( "fragment.glsl" );
			view = new SceneView( this, vertexShader, fragmentShader );
			setContentView( view );
			view.renderer.angle = 180.0f;
			InputStream fileInput = getAssets().open( "prison.opt.xml" );
			World world = WorldLoader.build( fileInput );			
			view.setScene( SceneTesselator.generateSubSectorQuadsForWorld( world ) );

			view.renderer.camera.set( ( (SpaceRegion) world.masterSector.getSons().toArray()[ 0 ] ).getAbsolutePosition() );

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
