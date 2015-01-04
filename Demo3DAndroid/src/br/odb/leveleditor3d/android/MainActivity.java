package br.odb.leveleditor3d.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneTesselator;
import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.libscene.WorldLoader;
import br.odb.libscene.SpaceRegion;
import br.odb.utils.math.Vec3;

public class MainActivity extends Activity {

	SceneView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			InputStream vertexShader;
			vertexShader = getAssets().open("vertex.glsl");
			InputStream fragmentShader = getAssets().open("fragment.glsl");
			view = new SceneView(this, vertexShader, fragmentShader);
			setContentView(view);
			view.renderer.angle = 180.0f;
			InputStream fileInput = getAssets().open("prison.opt.xml");
			World world = WorldLoader.build(fileInput);
			// world.checkForHardLinks();
			view.setScene(SceneTesselator.generateSubSectorQuadsForWorld(world));

			final List<SpaceRegion> srs = world.getAllRegionsAsList();
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
						System.out.println( "testing...testing...testing..." );
						SpaceRegion sr;
						
						inside = false;
						
						for (int c = 0; c < srs.size(); ++c) {

							sr = srs.get(c);

							if (sr instanceof Sector) {
								if (sr.contains(view.renderer.camera)) {
									System.out.println("got inside " + c);
									lastValidPosition.set( view.renderer.camera );
									inside = true;
								}								
							}
							
						}
						
						if (!inside ) {
							view.renderer.camera.set( lastValidPosition );
						}
					}
				}
			}).start();			

			for (index = size - 1; index >= 0; --index) {
				if (srs.get(index) instanceof GroupSector) {
					view.renderer.camera
							.set(srs.get(index).getAbsoluteCenter());
					return;
				}
			}



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
