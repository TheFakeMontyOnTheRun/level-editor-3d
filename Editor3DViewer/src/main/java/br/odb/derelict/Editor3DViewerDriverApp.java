/**
 * 
 */
package br.odb.derelict;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.odb.libscene.SceneNode;
import br.odb.libscene.World;
import br.odb.libscene.builders.WorldLoader;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.builders.GeneralTriangleFactory;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * @author monty
 *
 */
public class Editor3DViewerDriverApp {
	
	private static final int CANVAS_WIDTH = 640;
	private static final int CANVAS_HEIGHT = 480;
	private static final int FPS = 60;
	
	public static void main(String[] args) {
		new Thread(new Runnable() {
			private World world;

			@Override
			public void run() {

				Editor3DViewer canvas = new Editor3DViewer();

				try {
					FileInputStream fis = new FileInputStream(
							System.getProperty( "user.home" ) + "/prison.opt.xml");
					world = WorldLoader.build(fis);
					//world.checkForHardLinks_new();
					new SceneTesselator( new GeneralTriangleFactory() ).generateSubSectorQuadsForWorld(world);

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
				final List<SceneNode> srs = world.getAllRegionsAsList();
				canvas.cameraPosition.set( srs.get( srs.size() - 1 ).getAbsolutePosition() );
			

				canvas.setScene( world );
				System.out.println( "loaded " + canvas.polysToRender.size() + " polys" );
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH,
						CANVAS_HEIGHT));

				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

				// Create the top-level container
				final JFrame frame = new JFrame(); // Swing's JFrame or AWT's
													// Frame
				frame.getContentPane().add(canvas);

				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {

						new Thread() {
							@Override
							public void run() {
								if (animator.isStarted())
									animator.stop();
								System.exit(0);
							}
						}.start();
					}
				});
				frame.setTitle( "3D View" );
				frame.pack();
				frame.setVisible(true);
				animator.start(); // start the animation loop
			}
		}).start();
	}
}
