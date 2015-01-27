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
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralPolygon;
import br.odb.libstrip.IndexedSetFace;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;
// GL2 constants


public class Editor3DViewer extends GLCanvas implements GLEventListener,
		KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3207880799571393702L;

	final List<GeneralPolygon> polysToRender = new ArrayList<GeneralPolygon>();
	public final Vec3 cameraPosition = new Vec3();
	float angle = 0.0f;
	private GLU glu;

	public Editor3DViewer() {
		this.addGLEventListener(this);
		this.addKeyListener(this);
	}
	


	public void loadGeometryFromScene(GroupSector sector) {

		for (IndexedSetFace isf : sector.mesh.faces) {
			polysToRender.add((GeneralPolygon) isf);
		}

		for (SpaceRegion sr : sector.getSons()) {
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

		Vec3 v;
		Color c;
		
		drawGridLines( gl );

		
		gl.glBegin(GL_TRIANGLES);

		for (GeneralPolygon poly : this.polysToRender) {
			
			c = poly.color;

			gl.glColor3f(c.r / 255.0f, c.g / 255.0f, c.b / 255.0f );
			
			v = poly.getVertex(0);
			gl.glVertex3f(v.x, v.y, v.z);
			
			v = poly.getVertex(1);
			gl.glVertex3f(v.x, v.y, v.z);
			
			v = poly.getVertex(2);
			gl.glVertex3f(v.x, v.y, v.z);
			
		}

		gl.glEnd();
	}


	private void drawGridLines(GL2 gl) {

		gl.glBegin( GL2.GL_LINES );
		
			for ( int x = 0; x < 255; x += 10 ) {
				
				gl.glVertex3f( (float)x, 0.0f, 0.0f);
				gl.glVertex3f( (float)x, 0.0f, 255.0f);
			}
			
			for ( int y = 0; y < 255; y += 10 ) {
				
				gl.glVertex3f( 0.0f, y, 255.0f);
				gl.glVertex3f( 255.0f, y, 255.0f);
				
			}
			
			for ( int z = 0; z < 255; z += 10 ) {
				
				gl.glVertex3f( 0.0f, 0.0f,  z);
				gl.glVertex3f( 0.0f, 255.0f, z);
			}
		gl.glEnd();		
	}



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



	public void setScene(World world) {
		this.polysToRender.clear();
		this.loadGeometryFromScene( world.masterSector );
		
	}
}