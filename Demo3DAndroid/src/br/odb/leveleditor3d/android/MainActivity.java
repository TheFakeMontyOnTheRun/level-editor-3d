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
			view.renderer.camera.set( -162.0f, 13.0f, -141.0f );
			InputStream fileInput = getAssets().open( "prison.xml" );
			World world = WorldLoader.build( fileInput );			
			view.setScene( SceneTesselator.generateQuadsForWorld( world ) );

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
