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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.SceneNode;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libscene.builders.WorldLoader;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;
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
							System.getProperty( "user.home" ) + "/floor1.opt.xml");
					 
					world = WorldLoader.build(fis);
					canvas.tesselator.generateSubSectorQuadsForWorld(world);
					
//					FileInputStream filePath = new FileInputStream(
//							System.getProperty( "user.home" ) + "/title.bin");
					
					//decal = Decal.loadGraphic( filePath, 800, 480 );
										
		            WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
		            List<Material> mats = matLoader.parseMaterials( new FileInputStream(
							System.getProperty( "user.home" ) + "/gargoyle.mtl") );


		            WavefrontOBJLoader loader = new WavefrontOBJLoader( new GeneralTriangleFactory() );
		            ArrayList<GeneralTriangleMesh> mesh = (ArrayList<GeneralTriangleMesh>) loader.loadMeshes( new FileInputStream(
							System.getProperty( "user.home" ) + "/gargoyle.obj"), mats );

		            for ( GeneralTriangle gt : mesh.get( 0 ).faces ) {
		            	canvas.cube.add( gt );
		            }
		            
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
				SpaceRegion sr = (SpaceRegion) world.masterSector.getChild( "Cube.002_Cube.112" );
				canvas.cameraPosition.set( sr.getAbsolutePosition().add( new Vec3( sr.size.x / 2.0f, sr.size.y / 2.0f, sr.size.z / 2.0f ) ) );
			
				canvas.setScene( world );
				canvas.spawnCube( canvas.cameraPosition.add( new Vec3( 5.0f, 0.0f, 5.0f ) ) );
				
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
