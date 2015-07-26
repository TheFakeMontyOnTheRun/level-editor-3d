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

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libscene.builders.WorldLoader;
import br.odb.utils.math.Vec3;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * @author monty
 *
 */
public class Editor3DViewerDriverApp {
	
	private static final int CANVAS_WIDTH = 640;
	private static final int CANVAS_HEIGHT = 480;
	private static final int FPS = 60;
	
	public static World loadMap(String filename)
			throws FileNotFoundException, IOException, SAXException,
			ParserConfigurationException {
		
		FileInputStream fis = new FileInputStream(
				System.getProperty( "user.home" ) + filename );
		 
		World world = WorldLoader.build(fis);
		
		return world;
	}	

	
	public static void main(String[] args) {
		final Editor3DViewer canvas = new Editor3DViewer();
		
		new Thread(new Runnable() {
			private World world;

			@Override
			public void run() {
				try {					
					world = Editor3DViewerDriverApp.loadMap( "/prison.opt.xml" );
					canvas.tesselator.generateSubSectorQuadsForWorld(world);
					canvas.setScene( world );
					canvas.initDefaultActorModel();
					//canvas.applyDecalToSector("/title.bin", Direction.FLOOR, "Cube.002_Cube.112" );
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
				
				createScene(canvas);
				
				System.out.println( "loaded " + canvas.polysToRender.size() + " polys" );
				
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH,
						CANVAS_HEIGHT));

				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

				final JFrame frame = new JFrame();
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

			private void createScene(Editor3DViewer canvas) {
			
				SpaceRegion sr = (SpaceRegion) world.masterSector.getChild( "Cube" );
				canvas.getCurrentCameraNode().localPosition.set( sr.getAbsolutePosition().add( new Vec3( sr.size.x / 2.0f, sr.size.y / 2.0f, sr.size.z / 2.0f ) ) );
				canvas.spawnDefaultActor( canvas.getCurrentCameraNode().localPosition.add( new Vec3( 5.0f, 0.0f, 5.0f ) ), 0.0f );
				canvas.spawnDefaultActor( new Vec3( 0.0f, 0.0f, 0.0f ), 0.0f );
			}
		}).start();
	}
}
