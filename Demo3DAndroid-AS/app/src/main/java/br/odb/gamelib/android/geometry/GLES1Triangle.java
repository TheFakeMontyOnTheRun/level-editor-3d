package br.odb.gamelib.android.geometry;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import br.odb.libstrip.GeneralTriangle;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;

import static android.opengl.GLES10.glColorPointer;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glVertexPointer;

/**
 * @author monty
 */
public class GLES1Triangle extends GeneralTriangle {

    //private FloatBuffer textureBuffer;
    private FloatBuffer colorBuffer;
    private FloatBuffer vertexBuffer;
    int[] verticesBits = new int[9];
    private float[] color = new float[12];
    int[] colorBits = new int[4];
    public int light = 0;
    //private float[] textureCoordinates;

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    public void draw(GL10 gl) {

        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, getVertexData().length / 3);

    }

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    public void draw() {

        glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        glDrawArrays(GL10.GL_TRIANGLES, 0, getVertexData().length / 3);
    }

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    @Override
    public void flush() {

        super.flush();

        float[] vertices = getVertexData();
        float[] oneColor;

        if (material != null) {
            oneColor = material.mainColor.getFloatData();
        } else {
            oneColor = new Color(0xFFFFFFFF).getFloatData();
        }

        for (int c = 0; c < 3; ++c) {
            for (int d = 0; d < 4; ++d) {
                color[(c * 4) + (d)] = oneColor[d];
            }
        }

        for (int c = 0; c < vertices.length; ++c) {
            verticesBits[c] = Float.floatToRawIntBits(vertices[c]);
        }

        for (int c = 0; c < colorBits.length; ++c) {
            colorBits[c] = Float.floatToRawIntBits(color[c]);
        }

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(color.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuf.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

    }

    /**
     *
     */
    @Override
    public GLES1Triangle makeCopy() {
        GLES1Triangle t = new GLES1Triangle();
        t.material = material;
        t.x0 = x0;
        t.x1 = x1;
        t.x2 = x2;

        t.y0 = y0;
        t.y1 = y1;
        t.y2 = y2;

        t.z0 = z0;
        t.z1 = z1;
        t.z2 = z2;

        t.flush();

        return t;
    }

    public void drawGLES2(int vertexHandle, int colorHandle, int textureHandle) {

        float[] vertices = getVertexData();

        GLES20.glVertexAttribPointer(vertexHandle, vertices.length / 3,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(vertexHandle);

        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0,
                colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        if (textureHandle != -1) {
            /*
            textureBuffer.position(0);
			GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT,
					false, 0, textureBuffer);

			GLES20.glEnableVertexAttribArray(textureHandle);
			*/
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    // ------------------------------------------------------------------------------------------------------------

    public Vec3 makeNormal() {

        Vec3 v1;
        Vec3 v2;
        Vec3 vn;

        v1 = new Vec3(x1 - x0, y1 - y0, z1 - z0);
        v2 = new Vec3(x2 - x0, y2 - y0, z2 - z0);

        vn = v1.crossProduct(v2);

        return vn;
    }

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    public void flatten(float z) {
        z0 = z1 = z2 = z;
    }

    public void flatten() {

        flatten(-1.2f);
    }

    public void multiplyColor(float factor) {

        for (int c = 0; c < color.length; ++c)
            color[c] *= factor;

        for (int d = 0; d < colorBits.length; ++d) {
            colorBits[d] = Float.floatToRawIntBits(color[d]);
        }
    }

    public void setTextureCoordenates(float[] fs) {
        /*
		this.textureCoordinates = fs;

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoordinates);
		textureBuffer.position(0);
		*/
    }

}
