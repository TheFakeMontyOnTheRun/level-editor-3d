package br.odb.derelict;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST; // GL constants
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;
import br.odb.utils.Color;
import br.odb.utils.Direction;
import br.odb.utils.math.Vec3;
// GL2 constants


public class Editor3DViewer extends GLCanvas implements GLEventListener,
		KeyListener
//		,Runnable 
		{
	
//	public static final String SERVER = "http://192.241.246.87:8080/MServerTest";
//	public static final String SERVER = "http://127.0.0.1:8080/MServerTest";
	/**
	 * 
	 */
	private static final long serialVersionUID = 3207880799571393702L;

	final List<GeneralTriangle> polysToRender = new ArrayList<>();
	final List<GeneralTriangle> cube = new ArrayList<>();
	
	final List<Vec3> actors = new ArrayList<>();
	
	public final Vec3 cameraPosition = new Vec3();
	float angle = 0.0f;
	private GLU glu;

	private SpaceRegion currentSector;

	SceneTesselator tesselator;

	public Editor3DViewer() {
		this.addGLEventListener(this);
		this.addKeyListener(this);
		
		tesselator = new SceneTesselator( new GeneralTriangleFactory() );
//		SpaceRegion sr = new SpaceRegion( "dummy" );
//		sr.size.scale( 10 );
//		for ( Direction d : Direction.values() ) {
//			for ( GeneralTriangle trig : tesselator.generateQuadFor( d, sr ) ) {
//				cube.add( changeHue( trig ) );
//			}
//		}
		
//		new Thread( this ).start();
	}
		
    public GeneralTriangle changeHue( GeneralTriangle trig ) {
        trig.material = new Material( null, new Color( trig.material.mainColor ), null, null, null );

        switch ( trig.hint ) {
            case W:
                trig.material.mainColor.multiply( 0.1f );
                break;
            case E:
                trig.material.mainColor.multiply( 0.4f );
                break;
            case N:
                trig.material.mainColor.multiply( 0.2f );
                break;
            case S:
                trig.material.mainColor.multiply( 0.6f );
                break;
            case FLOOR:
                trig.material.mainColor.multiply( 0.9f );
                break;
            case CEILING:
                trig.material.mainColor.multiply( 0.3f );
                break;
        }
        
        trig.material.mainColor.a = 255;
        
        return trig;

    }


	public void loadGeometryFromScene(GroupSector sector) {

		for (GeneralTriangle isf : sector.mesh.faces) {
			
			changeHue( isf );
			polysToRender.add( isf );
		}

		for (SceneNode sr : sector.getSons()) {
			if (sr instanceof GroupSector) {
				loadGeometryFromScene((GroupSector) sr);
			}
		}
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);
		gl.glEnable( GL.GL_BLEND );
		gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		
		GL2 gl = drawable.getGL().getGL2();

		if (height == 0)
			height = 1;
		
		float aspect = (float) width / height;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, aspect, 0.1, 10000.0f);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	}


	@Override
	public void display(GLAutoDrawable drawable) {
		
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef( -cameraPosition.x, -cameraPosition.y, -cameraPosition.z); 

		Color c;
		
		//drawGridLines( gl );

//		gl.glBegin( GL2.GL_LINES );
//			Vec3 pos;
//			Vec3 center;
//			
//			for ( SpaceRegion sr : this.sectorsToDisplay ) {
//
//				pos = sr.getAbsolutePosition();
//				center = sr.getAbsoluteCenter();
//				
//				gl.glColor3f( 0.0f, 0.0f, 0.0f );
//				
//				gl.glVertex3f( pos.x, pos.y, pos.z );
//				gl.glVertex3f( pos.x + sr.size.x, pos.y, pos.z);
//
//				gl.glVertex3f( pos.x, pos.y, pos.z );
//				gl.glVertex3f( pos.x, pos.y + sr.size.y, pos.z);
//
//				gl.glVertex3f( pos.x, pos.y, pos.z );
//				gl.glVertex3f( pos.x, pos.y, pos.z + sr.size.z );
//				
//			}		
//		gl.glEnd();
		
		
		gl.glBegin(GL_TRIANGLES);

		synchronized( actors ) {
			for ( Vec3 p : actors ) {
				drawCube( gl, p );
			}
		}
		
		for (GeneralTriangle poly : this.polysToRender) {
			
			c = poly.material.mainColor;

			gl.glColor4f(c.r / 255.0f, c.g / 255.0f, c.b / 255.0f, c.a / 255.0f );
			
			gl.glVertex3f( poly.x0, poly.y0, poly.z0);
			gl.glVertex3f( poly.x1, poly.y1, poly.z1);
			gl.glVertex3f( poly.x2, poly.y2, poly.z2);
			
		}

		gl.glEnd();
	}


	private void drawCube( GL2 gl, Vec3 p) {
		for (GeneralTriangle poly : this.cube) {
			
			gl.glColor4f( poly.material.mainColor.r / 255.0f,
					poly.material.mainColor.g / 255.0f,
					poly.material.mainColor.b / 255.0f,
					poly.material.mainColor.a / 255.0f					
					);
			
			gl.glVertex3f( poly.x0 + p.x, poly.y0 + p.y, poly.z0 + p.z );
			gl.glVertex3f( poly.x1 + p.x, poly.y1 + p.y, poly.z1 + p.z );
			gl.glVertex3f( poly.x2 + p.x, poly.y2 + p.y, poly.z2 + p.z );
			
		}		
	}

//	private void drawGridLines(GL2 gl) {
//
//		gl.glBegin( GL2.GL_LINES );
//		
//			for ( int x = 0; x < 255; x += 10 ) {
//				
//				gl.glVertex3f( (float)x, 0.0f, 0.0f);
//				gl.glVertex3f( (float)x, 0.0f, 255.0f);
//			}
//			
//			for ( int y = 0; y < 255; y += 10 ) {
//				
//				gl.glVertex3f( 0.0f, y, 255.0f);
//				gl.glVertex3f( 255.0f, y, 255.0f);
//				
//			}
//			
//			for ( int z = 0; z < 255; z += 10 ) {
//				
//				gl.glVertex3f( 0.0f, 0.0f,  z);
//				gl.glVertex3f( 0.0f, 255.0f, z);
//			}
//		gl.glEnd();		
//	}



	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		float scale = 10.0f;
		
		switch (keyCode) {
		case KeyEvent.VK_R:
			recalculateVisibility();
			break;
		case KeyEvent.VK_A:
			cameraPosition.y += scale / 2.0f;
			break;
		case KeyEvent.VK_Z:
			cameraPosition.y -= scale / 2.0f;
			break;

		case KeyEvent.VK_P:
			
			
			actors.add( new Vec3( cameraPosition ) );
			this.repaint();
			break;
		case KeyEvent.VK_LEFT:
			angle -= 10.0f;
			break;
		case KeyEvent.VK_RIGHT:
			angle += 10.0f;
			break;
		case KeyEvent.VK_UP:
			cameraPosition.x += scale * Math.sin(angle * (Math.PI / 180.0f));
			cameraPosition.z -= scale * Math.cos(angle * (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_DOWN:
			cameraPosition.x -= scale * Math.sin(angle * (Math.PI / 180.0f));
			cameraPosition.z += scale * Math.cos(angle * (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_COMMA:
			cameraPosition.x += scale * Math.sin(( angle - 90.0f ) * (Math.PI / 180.0f));
			cameraPosition.z -= scale * Math.cos(( angle - 90.0f )* (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_PERIOD:
			cameraPosition.x += scale * Math.sin( (angle + 90.0f ) * (Math.PI / 180.0f));
			cameraPosition.z -= scale * Math.cos( (angle + 90.0f ) * (Math.PI / 180.0f));
			break;

		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
		}

	}


	private void recalculateVisibility() {
		currentSector = world.masterSector.pick( this.cameraPosition );
		
		
		
		if ( currentSector != null ) {
			
			Sector visitingSon = (Sector) currentSector;
			GroupSector visitingParent = (GroupSector) visitingSon.parent;
			this.polysToRender.clear();
			this.world.masterSector.clearMeshes();
			
			while( visitingSon != null ) {
				
				if ( visitingSon.parent != visitingParent ) {
					visitingParent = (GroupSector) visitingSon.parent;
					tesselator.generateSubSectorMeshForSector( visitingParent );
				}
				
				visitingSon = (Sector) world.masterSector.getChild( visitingSon.links[ 0 ] );
			}
			
			this.loadGeometryFromScene( world.masterSector );
		}
	}


	private World world;
	
	public void setScene(World world) {
		this.polysToRender.clear();
		this.world = world;
		world.masterSector.size.set( 1024.0f, 1024.0f, 1024.0f );
		this.loadGeometryFromScene( world.masterSector );
	}

	public void spawnCube(Vec3 position ) {
		actors.add( position );		
	}

//	void sendPosition( int id ) throws IOException {
//		
//		String query = String.format("id=%s&x=%s&y=%s&z=%s",
//				URLEncoder.encode( "" + id, "UTF8"),
//			     URLEncoder.encode( "" + cameraPosition.x, "UTF8"), 
//			     URLEncoder.encode( "" + cameraPosition.y, "UTF8"),
//			     URLEncoder.encode( "" + cameraPosition.z, "UTF8")
//			     );
//		
//		
//		String received = blockSendHTTPGet( SERVER + "/Server?" + query );
//		String[] positions = received.split( ";" );
//		
//		String[] coords;
//		Vec3 v;
//		
//		synchronized( actors ) {
//			
//			actors.clear();
//			
//			for ( String pos : positions ) {
//				coords = pos.split( "[ ]+");
//				v = new Vec3();
//				
//				v.x = Float.parseFloat( coords[ 0 ] );
//				v.y = Float.parseFloat( coords[ 1 ] );
//				v.z = Float.parseFloat( coords[ 2 ] );
//				
//				actors.add( v );
//			}
//		}
//	}
//	
//	String blockSendHTTPGet(final String url) {
//			String msg = "";
//			try {
//				URL urlObj = new URL( url );
//				URLConnection connection;
//				connection = urlObj.openConnection();
//				HttpURLConnection httpConnection = (HttpURLConnection) connection;
//				
//				int responseCode = httpConnection.getResponseCode();
//				
//				if ( responseCode == HttpURLConnection.HTTP_OK ) {
//					InputStream in = httpConnection.getInputStream();
//					
//					InputStreamReader i=new InputStreamReader( in);
//					BufferedReader str=new BufferedReader(i);
//					StringBuilder sb = new StringBuilder();
//				    String line;
//	                while ((line=str.readLine())!=null)
//	                {
//	                   sb.append(line);
//	                }
//					
//					msg=sb.toString();
//					
//					
//				} else {
//					System.out.println( "Error code: " + responseCode );
//				}
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			return msg;
//	}
	
//	@Override
//	public void run() {
//		
//		String data = "" + blockSendHTTPGet( SERVER + "/GetId" ).trim().charAt( 0 );
//		
//		if( data == null || data.length() == 0 ) {
//			return;
//		}
//		
//		int id = Integer.parseInt( data );
//		
//		System.out.println( "Player Id:" + id );
//		
//		while( true ) {
//		
//			try {
//				sendPosition( id );
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//				Thread.sleep( 50 );
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}