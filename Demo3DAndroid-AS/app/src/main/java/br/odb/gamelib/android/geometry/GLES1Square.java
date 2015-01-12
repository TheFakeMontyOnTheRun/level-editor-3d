/**
 * @author Daniel Monteiro
 * */

package br.odb.gamelib.android.geometry;
import static android.opengl.GLES10.glColorPointer;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import br.odb.libstrip.AbstractSquare;
import br.odb.libstrip.IndexedSetFace;
import br.odb.utils.Color;
import br.odb.utils.Utils;
import br.odb.utils.math.Vec3;

//import android.opengl.GLES20;

public class GLES1Square implements AbstractSquare, GLESIndexedSetFace {

	public float[] color;
	public FloatBuffer colorBuffer;
	private float offset;

	public boolean outline;

	public FloatBuffer vertexBuffer;
	public float[] vertices;
	private boolean visible = true;
	public int[] colorBits;
	public int[] verticesBits;
	private float[] textureCoordinates;

	// ------------------------------------------------------------------------------------------------------------
	public GLES1Square() {
		vertices = new float[18];
		color = new float[4];
		
		verticesBits = new int[18];
		colorBits = new int[4];
		
	}


	// ------------------------------------------------------------------------------------------------------------
	@Override
	public void draw(GL10 gl) {
		
		if ( visible ) {
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
			}

		// if ( outline ) {
		//
		// gl.glColor4f( 1.0f, 0.0f, 0.0f, 1.0f );
		// gl.glDisableClientState(GL10.GL_COLOR_ARRAY );
		// gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, vertices.length / 3);
		// gl.glEnableClientState(GL10.GL_COLOR_ARRAY );
		// outline = false;
		// }
		//visible = false;
	}
	
	@Override
	public void draw() {
		
		if ( visible ) {
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
		}
		
		// if ( outline ) {
		//
		// gl.glColor4f( 1.0f, 0.0f, 0.0f, 1.0f );
		// gl.glDisableClientState(GL10.GL_COLOR_ARRAY );
		// gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, vertices.length / 3);
		// gl.glEnableClientState(GL10.GL_COLOR_ARRAY );
		// outline = false;
		// }
		//visible = false;
	}

	public void flushToGLES() {

		// ByteBuffer byteBuf = ByteBuffer.allocateDirect( vertices.length * 4
		// );
		// byteBuf.order( ByteOrder.nativeOrder() );
		// vertexBuffer = byteBuf.asFloatBuffer();
		// vertexBuffer.put( vertices );
		// vertexBuffer.position(0);
		//
		// byteBuf = ByteBuffer.allocateDirect( color.length * 4);
		// byteBuf.order(ByteOrder.nativeOrder());
		// colorBuffer = byteBuf.asFloatBuffer();
		// colorBuffer.put( color );
		// colorBuffer.position(0);
	}

	@Override
	public Color getColor() {

		return new Color(color[0], color[1], color[2]);
	}

	// ------------------------------------------------------------------------------------------------------------

	@Override
	public float getColor(int i) {
		return color[i] + offset;
	}

	@Override
	public float[] getColorData() {

		return color;
	}

	
	 public void drawGLES2( int vertexHandle, int colorHandle, int notused ) {
	 GLES20.glVertexAttribPointer( vertexHandle, 3, GLES20.GL_FLOAT, false, 0,
	 vertexBuffer );
	 GLES20.glEnableVertexAttribArray( vertexHandle );
	 GLES20.glVertexAttribPointer( colorHandle, 4, GLES20.GL_FLOAT, false, 0,
	 colorBuffer );
	 GLES20.glEnableVertexAttribArray( colorHandle );
	
	 GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4 );
	 }


	/**
	 * @return the offset
	 */
	public float getOffset() {
		return offset;
	}

	@Override
	public int getTotalIndexes() {
		return 4;
	}

	@Override
	public Vec3 getVertex(int c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float[] getVertexData() {

		return vertices;
	}


	@Override
	public IndexedSetFace makeCopy() {

		GLES1Square s = new GLES1Square();
		System.arraycopy(vertices, 0, s.vertices, 0, vertices.length);
		System.arraycopy(verticesBits, 0, s.verticesBits, 0, verticesBits.length);

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(s.vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		s.vertexBuffer = byteBuf.asFloatBuffer();
		s.vertexBuffer.put(s.vertices);
		s.vertexBuffer.position(0);

		System.arraycopy(color, 0, s.color, 0, color.length);
		System.arraycopy(colorBits, 0, s.colorBits, 0, colorBits.length);

		byteBuf = ByteBuffer.allocateDirect(s.color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		s.colorBuffer = byteBuf.asFloatBuffer();
		s.colorBuffer.put(s.color);
		s.colorBuffer.position(0);

		return s;
	}

	@Override
	public void setColor( Color c) {
		this.color[0] = c.a;
		this.color[1] = c.r;
		this.color[2] = c.g;
		this.color[3] = c.b;
		
		for ( int d = 0; d < color.length; ++d ) {
			colorBits[ d ] = Float.floatToRawIntBits( color[ d ] );
		}

	}

	// ------------------------------------------------------------------------------------------------------------
	public void setColor(float R, float G, float B, float A) {
		color[0] = R;
		color[1] = G;
		color[2] = B;
		color[3] = A;

		for ( int c = 0; c < color.length; ++c ) {
			colorBits[ c ] = Float.floatToRawIntBits( color[ c ] );
		}

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);

	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(float offset) {
		this.offset = offset;
	}


	// ------------------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		String toReturn = new String("");
		toReturn += String.valueOf(this.vertices[0]);
		toReturn += ",";
		toReturn += String.valueOf(this.vertices[1]);
		toReturn += ",";
		toReturn += String.valueOf(this.vertices[2]);
		toReturn += ",";
		toReturn += String.valueOf(this.vertices[3]);
		toReturn += ",";
		toReturn += String.valueOf(this.vertices[4]);
		toReturn += ",";
		toReturn += String.valueOf(this.vertices[5]);

		toReturn += "-";
		toReturn += String.valueOf(this.color[0]);
		toReturn += ",";
		toReturn += String.valueOf(this.color[1]);
		toReturn += ",";
		toReturn += String.valueOf(this.color[2]);
		toReturn += ",";
		toReturn += String.valueOf(this.color[3]);

		return toReturn;
	}

	@Override
	public void setColorWithOffset(Color baseColor, int candelas) {
		
		float normalizer = 1.0f / 255.0f;
//		float R = Math.max( Utils.clamp( ( baseColor.getR() + candelas ), 0.0f, 254.0f ) * normalizer, color[ 0 ] ) ;
//		float G = Math.max( Utils.clamp( ( baseColor.getG() + candelas ), 0.0f, 254.0f ) * normalizer, color[ 1 ] ) ;
//		float B = Math.max( Utils.clamp( ( baseColor.getB() + candelas ), 0.0f, 254.0f ) * normalizer, color[ 2 ] ) ;

		float R = Utils.clamp( ( baseColor.r + candelas ), 0.0f, 255.0f ) * normalizer ;
		float G = Utils.clamp( ( baseColor.g + candelas ), 0.0f, 255.0f ) * normalizer ;
		float B = Utils.clamp( ( baseColor.b + candelas ), 0.0f, 255.0f ) * normalizer ;		
		
		color[0] = R;
		color[1] = G;
		color[2] = B;
		
		for ( int c = 0; c < color.length; ++c ) {
			colorBits[ c ] = Float.floatToRawIntBits( color[ c ] );
		}
	}


	@Override
	public void setTextureCoordenates(float[] fs) {
		this.textureCoordinates = fs;
	}
}
