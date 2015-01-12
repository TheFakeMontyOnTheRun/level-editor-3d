package br.odb.gamelib.android.geometry;
 
import static android.opengl.GLES10.glColorPointer;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import br.odb.libstrip.VertexArrayManager;
import android.opengl.GLES20;
import android.util.Log;

public class GLESVertexArrayManager extends VertexArrayManager {


	int capacity;
	private boolean init;
	private int length;
	private int currentVertexPosition;
	private FloatBuffer colorBuffer;
	private FloatBuffer vertexBuffer;
	private IntBuffer colorIntBuffer;
	private IntBuffer vertexIntBuffer;
	private ByteBuffer colorByteBuffer;
	private ByteBuffer vertexByteBuffer;
	private int numFaces;


	public GLESVertexArrayManager() {
		super();
	}
	
	final public void drawGLES2( int vertexHandle, int colorHandle ) {

		 GLES20.glVertexAttribPointer( vertexHandle, 3,	 GLES20.GL_FLOAT, false, 0, vertexBuffer );
		 GLES20.glEnableVertexAttribArray( vertexHandle );

		 GLES20.glVertexAttribPointer( colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer );
		 GLES20.glEnableVertexAttribArray( colorHandle );
		
		 GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, currentVertexPosition / 3 );

	}

	final public void draw(GL10 gl) {
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, currentVertexPosition / 3);
	}

	final public void draw() {
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		glDrawArrays(GL10.GL_TRIANGLES, 0, currentVertexPosition / 3);
	}
	
	final public void flush() {
		
		vertexBuffer.position(0);
		colorBuffer.position(0);
		vertexByteBuffer.position(0);
		colorByteBuffer.position(0);
		vertexIntBuffer.position(0);
		colorIntBuffer.position(0);
	}

	final public void init(int numFaces) {
		Log.d("bzk3", "init VA manager with " + numFaces + " positions");
		
		// 6 verteces (2 triangles) with 3 floats for coordinates and 4 color channels. each of those take 4 bytes. 
		capacity = numFaces * ( ( 6 * ( 3 + 4) ) * 4 );
		
		
		vertexByteBuffer = ByteBuffer.allocateDirect( capacity ).order(ByteOrder.nativeOrder());		
		vertexIntBuffer = vertexByteBuffer.asIntBuffer();
		vertexBuffer = vertexByteBuffer.asFloatBuffer();
		
		colorByteBuffer = ByteBuffer.allocateDirect( capacity ).order(ByteOrder.nativeOrder());
		colorBuffer = colorByteBuffer.asFloatBuffer();
		colorIntBuffer = colorByteBuffer.asIntBuffer();
		
		
		
		this.numFaces = numFaces;
		init = true;
		resetState();
	}

	final public void onFrameRenderingFinished() {
		resetState();
		this.vertexBuffer.rewind();
		this.colorBuffer.rewind();
		this.vertexByteBuffer.rewind();
		this.colorByteBuffer.rewind();
		this.vertexIntBuffer.rewind();
		this.colorIntBuffer.rewind();
		length = 0;
	}
	
	final public void pushIntoFrameAsStatic( int[] vertexData, int[] colorData ) {
		
		try {
			
			if (init && (length < capacity)) {
				
				vertexIntBuffer.put(vertexData);
				currentVertexPosition += vertexData.length;
				length += vertexData.length * 4;
				
				for ( int c = 0; c < ( vertexData.length / 3 ); ++c )
					colorIntBuffer.put(colorData);
				
				vertexBuffer.position( vertexIntBuffer.position() );
				vertexByteBuffer.position( vertexIntBuffer.position() * 4 );
				
				colorBuffer.position( colorIntBuffer.position() );
				colorByteBuffer.position( colorIntBuffer.position() * 4 );

			} else {
				Log.d( "derelict", "estouro de limite de buffer de renderiza????o" );
			}
		} catch (BufferOverflowException e) {
			e.printStackTrace();
			Log.d("bzk3", "length: " + length + " capacity: " + capacity);
		}
	}

	final public void pushIntoFrameAsStatic(float[] vertexData, float[] colorData ) {
		try {

			if (init && (length < capacity)) {

				vertexBuffer.put(vertexData);
				currentVertexPosition += vertexData.length;
				length += vertexData.length * 4;
				
				for ( int c = 0; c < ( vertexData.length / 3 ); ++c )
					colorBuffer.put(colorData);
				
				
				vertexIntBuffer.position( vertexBuffer.position() );
				vertexByteBuffer.position( vertexBuffer.position() * 4 );
				
				colorIntBuffer.position( colorBuffer.position() );
				colorByteBuffer.position( colorBuffer.position() * 4 );
			} else {
				Log.d( "derelict", "estouro de limite de buffer de renderiza????o" );
			}
		} catch (BufferOverflowException e) {
			e.printStackTrace();
			Log.d("bzk3", "length: " + length + " capacity: " + capacity);
		}

	}

	final private void resetState() {
		currentVertexPosition = 0;
	}

	public void reinit() {
		
		onFrameRenderingFinished();		
	}

	public void clear() {
		init( numFaces );		
	}
}
