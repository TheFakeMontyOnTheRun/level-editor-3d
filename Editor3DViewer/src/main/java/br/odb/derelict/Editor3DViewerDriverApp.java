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
import br.odb.libstrip.Decal;
import br.odb.libstrip.GeneralTriangle;

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
			//private SVGGraphic graphic;
			//private GeneralTriangle[] decal;

			@Override
			public void run() {

				Editor3DViewer canvas = new Editor3DViewer();

				try {
					FileInputStream fis = new FileInputStream(
							System.getProperty( "user.home" ) + "/prison.opt.xml");
					 
					world = WorldLoader.build(fis);
					canvas.tesselator.generateSubSectorQuadsForWorld(world);
					
//					FileInputStream filePath = new FileInputStream(
//							System.getProperty( "user.home" ) + "/title.bin");
					
					//decal = Decal.loadGraphic( filePath, 800, 480 );
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
				
//				for ( GeneralTriangle gt : decal ) {
//					gt.x0 += canvas.cameraPosition.x;
//					gt.x1 += canvas.cameraPosition.x;
//					gt.x2 += canvas.cameraPosition.x;
//
//					gt.y0 += canvas.cameraPosition.y;
//					gt.y1 += canvas.cameraPosition.y;
//					gt.y2 += canvas.cameraPosition.y;
//					
//					gt.z0 += canvas.cameraPosition.z;
//					gt.z1 += canvas.cameraPosition.z;
//					gt.z2 += canvas.cameraPosition.z;
//					
//					canvas.polysToRender.add( gt );
//				}				
				
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
