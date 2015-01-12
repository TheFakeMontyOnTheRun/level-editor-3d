package br.odb.gamelib.android.geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import br.odb.libstrip.GeneralQuad;
import br.odb.libstrip.IndexedSetFace;
import android.graphics.Color;

public class GLES1SquareFactory {

	private static GLES1SquareFactory instance;

	public static GLES1SquareFactory getInstance() {

		if (instance == null)
			instance = new GLES1SquareFactory();

		return instance;
	}

	// ------------------------------------------------------------------------------------------------------------
	public static GLES1Square MakeXY(float x1, float y1, float x2, float y2,
			float z, int aColor, boolean generateBuffers) {
		GLES1Square s = new GLES1Square();
		s.vertices[0] = x1;
		s.vertices[1] = y1;
		s.vertices[2] = z;
		s.vertices[3] = x2;
		s.vertices[4] = y1;
		s.vertices[5] = z;
		s.vertices[6] = x1;
		s.vertices[7] = y2;
		s.vertices[8] = z;

		s.vertices[9] = x2;
		s.vertices[10] = y1;
		s.vertices[11] = z;
		s.vertices[12] = x1;
		s.vertices[13] = y2;
		s.vertices[14] = z;

		s.vertices[15] = x2;
		s.vertices[16] = y2;
		s.vertices[17] = z;

		s.color[0] = Color.red(aColor) / 255.0f;
		s.color[1] = Color.green(aColor) / 255.0f;
		s.color[2] = Color.blue(aColor) / 255.0f;
		s.color[3] = 1.0f;

		for (int c = 0; c < s.vertices.length; ++c) {
			s.verticesBits[c] = Float.floatToRawIntBits(s.vertices[c]);
		}

		for (int c = 0; c < s.color.length; ++c) {
			s.colorBits[c] = Float.floatToRawIntBits(s.color[c]);
		}
		// s.color[4] = Color.red(aColor) / 255.0f;
		// s.color[5] = Color.green(aColor) / 255.0f;
		// s.color[6] = Color.blue(aColor) / 255.0f;
		// s.color[7] = 1.0f;

		// s.color[8] = Color.red(aColor) / 255.0f;
		// s.color[9] = Color.green(aColor) / 255.0f;
		// s.color[10] = Color.blue(aColor) / 255.0f;
		// s.color[11] = 1.0f;

		// s.color[ 12 ] = Color.red( aColor ) / 255.0f;
		// s.color[ 13 ] = Color.green( aColor ) / 255.0f;
		// s.color[ 14 ] = Color.blue( aColor ) / 255.0f;
		// s.color[ 15 ] = 1.0f;
		if (generateBuffers) {

			ByteBuffer byteBuf = ByteBuffer
					.allocateDirect(s.vertices.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			s.vertexBuffer = byteBuf.asFloatBuffer();
			s.vertexBuffer.put(s.vertices);
			s.vertexBuffer.position(0);

			byteBuf = ByteBuffer.allocateDirect(s.color.length * 4 * 2);
			byteBuf.order(ByteOrder.nativeOrder());
			s.colorBuffer = byteBuf.asFloatBuffer();
			s.colorBuffer.put(s.color);
			s.colorBuffer.put(s.color);
			s.colorBuffer.position(0);

		}

		return s;
	}

	// ------------------------------------------------------------------------------------------------------------
	public static GLES1Square MakeXZ(float x1, float z1, float x2, float z2,
			float y, int aColor, boolean generateBuffers) {
		GLES1Square s = new GLES1Square();
		s.vertices[0] = x1;
		s.vertices[1] = y;
		s.vertices[2] = z2;
		s.vertices[3] = x2;
		s.vertices[4] = y;
		s.vertices[5] = z2;
		s.vertices[6] = x1;
		s.vertices[7] = y;
		s.vertices[8] = z1;

		s.vertices[9] = x2;
		s.vertices[10] = y;
		s.vertices[11] = z2;
		s.vertices[12] = x1;
		s.vertices[13] = y;
		s.vertices[14] = z1;

		s.vertices[15] = x2;
		s.vertices[16] = y;
		s.vertices[17] = z1;

		s.color[0] = Color.red(aColor) / 255.0f;
		s.color[1] = Color.green(aColor) / 255.0f;
		s.color[2] = Color.blue(aColor) / 255.0f;
		s.color[3] = 1.0f;

		for (int c = 0; c < s.vertices.length; ++c) {
			s.verticesBits[c] = Float.floatToRawIntBits(s.vertices[c]);
		}

		for (int c = 0; c < s.color.length; ++c) {
			s.colorBits[c] = Float.floatToRawIntBits(s.color[c]);
		}

		// s.color[4] = Color.red(aColor) / 255.0f;
		// s.color[5] = Color.green(aColor) / 255.0f;
		// s.color[6] = Color.blue(aColor) / 255.0f;
		// s.color[7] = 1.0f;

		// s.color[8] = Color.red(aColor) / 255.0f;
		// s.color[9] = Color.green(aColor) / 255.0f;
		// s.color[10] = Color.blue(aColor) / 255.0f;
		// s.color[11] = 1.0f;

		// s.color[ 12 ] = Color.red( aColor ) / 255.0f;
		// s.color[ 13 ] = Color.green( aColor ) / 255.0f;
		// s.color[ 14 ] = Color.blue( aColor ) / 255.0f;
		// s.color[ 15 ] = 1.0f;

		if (generateBuffers) {

			ByteBuffer byteBuf = ByteBuffer
					.allocateDirect(s.vertices.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			s.vertexBuffer = byteBuf.asFloatBuffer();
			s.vertexBuffer.put(s.vertices);
			s.vertexBuffer.position(0);

			byteBuf = ByteBuffer.allocateDirect(s.color.length * 4 * 2);
			byteBuf.order(ByteOrder.nativeOrder());
			s.colorBuffer = byteBuf.asFloatBuffer();
			s.colorBuffer.put(s.color);
			s.colorBuffer.put(s.color);
			s.colorBuffer.position(0);
		}

		return s;
	}

	// ------------------------------------------------------------------------------------------------------------
	public static GLES1Square MakeYZ(float y1, float z1, float y2, float z2,
			float x, int aColor, boolean generateBuffers) {
		GLES1Square s = new GLES1Square();
		s.vertices[0] = x;
		s.vertices[1] = y2;
		s.vertices[2] = z2;
		s.vertices[3] = x;
		s.vertices[4] = y2;
		s.vertices[5] = z1;
		s.vertices[6] = x;
		s.vertices[7] = y1;
		s.vertices[8] = z2;

		s.vertices[9] = x;
		s.vertices[10] = y2;
		s.vertices[11] = z1;
		s.vertices[12] = x;
		s.vertices[13] = y1;
		s.vertices[14] = z2;

		s.vertices[15] = x;
		s.vertices[16] = y1;
		s.vertices[17] = z1;

		s.color[0] = Color.red(aColor) / 255.0f;
		s.color[1] = Color.green(aColor) / 255.0f;
		s.color[2] = Color.blue(aColor) / 255.0f;
		s.color[3] = 1.0f;

		for (int c = 0; c < s.vertices.length; ++c) {
			s.verticesBits[c] = Float.floatToRawIntBits(s.vertices[c]);
		}

		for (int c = 0; c < s.color.length; ++c) {
			s.colorBits[c] = Float.floatToRawIntBits(s.color[c]);
		}

		// s.color[4] = Color.red(aColor) / 255.0f;
		// s.color[5] = Color.green(aColor) / 255.0f;
		// s.color[6] = Color.blue(aColor) / 255.0f;
		// s.color[7] = 1.0f;

		// s.color[8] = Color.red(aColor) / 255.0f;
		// s.color[9] = Color.green(aColor) / 255.0f;
		// s.color[10] = Color.blue(aColor) / 255.0f;
		// s.color[11] = 1.0f;

		// s.color[ 12 ] = Color.red( aColor ) / 255.0f;
		// s.color[ 13 ] = Color.green( aColor ) / 255.0f;
		// s.color[ 14 ] = Color.blue( aColor ) / 255.0f;
		// s.color[ 15 ] = 1.0f;

		if (generateBuffers) {
			ByteBuffer byteBuf = ByteBuffer
					.allocateDirect(s.vertices.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			s.vertexBuffer = byteBuf.asFloatBuffer();
			s.vertexBuffer.put(s.vertices);
			s.vertexBuffer.position(0);

			byteBuf = ByteBuffer.allocateDirect(s.color.length * 4 * 2);
			byteBuf.order(ByteOrder.nativeOrder());
			s.colorBuffer = byteBuf.asFloatBuffer();
			s.colorBuffer.put(s.color);
			s.colorBuffer.put(s.color);
			s.colorBuffer.position(0);
		}

		return s;
	}
}
