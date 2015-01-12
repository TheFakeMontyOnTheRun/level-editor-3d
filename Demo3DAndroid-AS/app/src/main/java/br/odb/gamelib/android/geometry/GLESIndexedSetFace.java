package br.odb.gamelib.android.geometry;
 
import javax.microedition.khronos.opengles.GL10;

import br.odb.libstrip.IndexedSetFace;

public interface GLESIndexedSetFace extends IndexedSetFace {
	public void draw(GL10 gl);

	public float getColor(int i);

	public float[] getColorData();

	public float[] getVertexData();

	public void draw();

	public void drawGLES2(int positionHandle, int colorHandle, int textureHandle );

	public void setTextureCoordenates(float[] fs);

	
}
