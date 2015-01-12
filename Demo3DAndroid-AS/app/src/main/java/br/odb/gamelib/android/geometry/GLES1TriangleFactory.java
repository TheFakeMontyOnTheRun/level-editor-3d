package br.odb.gamelib.android.geometry;
 
import br.odb.libstrip.AbstractTriangleFactory;
import br.odb.libstrip.IndexedSetFace;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;

public class GLES1TriangleFactory implements AbstractTriangleFactory {

	private static GLES1TriangleFactory instance;

	public static GLES1TriangleFactory getInstance() {

		if (instance == null)
			instance = new GLES1TriangleFactory();

		return instance;
	}

	@Override
	public GLES1Triangle makeTrig(float x0, float y0, float z0, float x1,

	float y1, float z1, float x2, float y2, float z2, int color, Vec3 lightDirection) {
		GLES1Triangle toReturn = new GLES1Triangle();
		toReturn.x0 = x0;
		toReturn.x1 = x1;
		toReturn.x2 = x2;
		toReturn.y0 = y0;
		toReturn.y1 = y1;
		toReturn.y2 = y2;
		toReturn.z0 = z0;
		toReturn.z1 = z1;
		toReturn.z2 = z2;
		Color c = new Color(color);

		float lightFactor = 1;
		
		if ( lightDirection != null ) {
			
			Vec3 normal = toReturn.makeNormal().normalized();
			lightFactor = 0.8f + ( normal.dotProduct( lightDirection.normalized() ) * 0.2f );
		}
		
		toReturn.r = c.r * lightFactor;
		toReturn.g = c.g * lightFactor;
		toReturn.b = c.b * lightFactor;
		toReturn.a = c.a * lightFactor;

		toReturn.flushToGLES();
		return toReturn;
	}

	public GLESIndexedSetFace makeTrigFrom(IndexedSetFace isf) {

		Vec3 v0 = isf.getVertex( 0 );
		Vec3 v1 = isf.getVertex( 1 );
		Vec3 v2 = isf.getVertex( 2 );
		
		return makeTrig( v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, isf.getColor().getARGBColor(), null );
	}
}
