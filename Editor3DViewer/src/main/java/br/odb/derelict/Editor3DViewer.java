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
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;
// GL2 constants


public class Editor3DViewer extends GLCanvas implements GLEventListener,
		KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3207880799571393702L;

	final List<GeneralTriangle> polysToRender = new ArrayList<>();
	public final Vec3 cameraPosition = new Vec3();
	float angle = 0.0f;
	private GLU glu;

	public Editor3DViewer() {
		this.addGLEventListener(this);
		this.addKeyListener(this);
	}
	
    public void changeHue( GeneralTriangle trig ) {
        trig.material = new Material( null, new Color( trig.material.mainColor ), null, null, null );

        switch ( trig.hint ) {
            case W:
                trig.material.mainColor.multiply( 0.8f );
                break;
            case E:
                trig.material.mainColor.multiply( 0.6f );
                break;
            case N:
                trig.material.mainColor.multiply( 0.4f );
                break;
            case S:
                trig.material.mainColor.multiply( 0.2f );
                break;
            case FLOOR:
                trig.material.mainColor.multiply( 0.9f );
                break;
            case CEILING:
                trig.material.mainColor.multiply( 0.1f );
                break;
        }
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

		for (GeneralTriangle poly : this.polysToRender) {
			
			c = poly.material.mainColor;

			gl.glColor4f(c.r / 255.0f, c.g / 255.0f, c.b / 255.0f, 0.5f );
			
			gl.glVertex3f( poly.x0, poly.y0, poly.z0);
			gl.glVertex3f( poly.x1, poly.y1, poly.z1);
			gl.glVertex3f( poly.x2, poly.y2, poly.z2);
			
		}

		gl.glEnd();
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

		switch (keyCode) {

		case KeyEvent.VK_A:
			cameraPosition.y += 10.0f;
			break;
		case KeyEvent.VK_Z:
			cameraPosition.y -= 10.0f;
			break;

		case KeyEvent.VK_LEFT:
			angle -= 10.0f;
			break;
		case KeyEvent.VK_RIGHT:
			angle += 10.0f;
			break;
		case KeyEvent.VK_UP:
			cameraPosition.x += 10 * Math.sin(angle * (Math.PI / 180.0f));
			cameraPosition.z -= 10 * Math.cos(angle * (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_DOWN:
			cameraPosition.x -= 10 * Math.sin(angle * (Math.PI / 180.0f));
			cameraPosition.z += 10 * Math.cos(angle * (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_COMMA:
			cameraPosition.x += 10 * Math.sin(( angle - 90.0f ) * (Math.PI / 180.0f));
			cameraPosition.z -= 10 * Math.cos(( angle - 90.0f )* (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_PERIOD:
			cameraPosition.x += 10 * Math.sin( (angle + 90.0f ) * (Math.PI / 180.0f));
			cameraPosition.z -= 10 * Math.cos( (angle + 90.0f ) * (Math.PI / 180.0f));
			break;

		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
		}

	}


	List< SceneNode > sectorsToDisplay = new ArrayList<>();
	
	public void setScene(World world) {
		this.polysToRender.clear();
		this.loadGeometryFromScene( world.masterSector );
		
		
		sectorsToDisplay.addAll( world.getAllRegionsAsList() );
	}
}