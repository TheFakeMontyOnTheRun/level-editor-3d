package br.odb.gamelib.android.geometry;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by monty on 7/19/15.
 */
public class GLESShadowMap {

    int[] fb, depthRb, renderTex;
    IntBuffer texBuffer;
}
