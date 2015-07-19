package br.odb.gamelib.android.geometry;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLESVertexArrayManager {

    private int capacity;
    private int length;
    private FloatBuffer colorBuffer;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private int numFaces;

    private int vertexVBOIndex =  -1;
    private int colorVBOIndex = -1;
    boolean ready;
    int[] vboData;

    public GLESVertexArrayManager( int polys ) {

        this.numFaces = polys;

        Log.d("bzk3", "init VA manager with " + numFaces + " positions");

        // 6 verteces (2 triangles) with 3 floats for coordinates and 4 color channels. each of those take 4 bytes.
        capacity = numFaces * ((6 * (3 + 4)) * 4);

        vertexBuffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void uploadToGPU() {

        vertexBuffer.position(0);
        colorBuffer.position(0);

//        vboData = new int[ 1 ];
//        GLES20.glGenBuffers(1, vboData, 0);
//        vertexVBOIndex = vboData[ 0 ];
//
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexVBOIndex);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, length, vertexBuffer, GLES20.GL_STATIC_DRAW);
//        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, length, vertexBuffer);
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
//
//        vboData = new int[ 1 ];
//        GLES20.glGenBuffers(1, vboData, 0);
//        colorVBOIndex = vboData[ 0 ];
//
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorVBOIndex);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * length / 3, colorBuffer, GLES20.GL_STATIC_DRAW);
//        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, 4 * length / 3, colorBuffer);
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
//
//        vertexBuffer.clear();
//        colorBuffer.clear();

        ready = true;
    }

    final public void draw(int vertexHandle, int colorHandle, int textureHandle) {

        if ( !ready ) {
            return;
        }

//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexVBOIndex);
        GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(vertexHandle);
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

//        GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, colorVBOIndex);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        if (textureHandle != -1) {
//            textureBuffer.position(0);
//            GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT,
//                    false, 0, textureBuffer);
//
//            GLES20.glEnableVertexAttribArray(textureHandle);
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, length / 12);
    }

    public void setTextureCoordenates(float[] fs) {
        ByteBuffer byteBuf;
        //4 vectors of 3 floats (having 4 bytes each)
        byteBuf = ByteBuffer.allocateDirect( 3 * 4 * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(fs);
        textureBuffer.position(0);
    }

    final public void pushIntoFrameAsStatic(float[] vertexData, float[] colorData) {
        try {

            if ( length < capacity ) {

                vertexBuffer.put(vertexData);
                length += vertexData.length * 4;

                for (int c = 0; c < (vertexData.length / 3); ++c) {
                    colorBuffer.put(colorData);
                }
            }
        } catch (BufferOverflowException e) {
            e.printStackTrace();
            Log.d("bzk3", "length: " + length + " capacity: " + capacity);
            throw e;
        }
    }
}
