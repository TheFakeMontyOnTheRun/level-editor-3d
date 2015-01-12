package br.odb.gamelib.android;

import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESMesh;
import br.odb.libstrip.AbstractTriangle;
import br.odb.libstrip.IndexedSetFace;
import br.odb.libstrip.Mesh;
import br.odb.libstrip.MeshFactory;
import br.odb.utils.math.Vec3;

public class AndroidMeshFactory extends MeshFactory {

	@Override
	public IndexedSetFace makeTrig(float x, float y, float z, float x2,
			float y2, float z2, float x3, float y3, float z3, int color,
			Vec3 defaultLightVector) {
		return GLES1TriangleFactory.getInstance().makeTrig( x, y, z, x2, y2, z2, x3, y3, z3, color, defaultLightVector );
	}

	@Override
	public Mesh emptyMeshNamed(String name ) {
		return new GLESMesh( name );
	}

	@Override
	public AbstractTriangle[][] newTriangleGroups(int i) {
		// TODO Auto-generated method stub
		return new GLES1Triangle[ i ][];
	}
}
