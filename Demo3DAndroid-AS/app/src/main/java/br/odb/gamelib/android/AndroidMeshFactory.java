package br.odb.gamelib.android;

import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESMesh;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;
import br.odb.utils.math.Vec3;

public class AndroidMeshFactory {

	public GeneralTriangle makeTrig(float x, float y, float z, float x2,
			float y2, float z2, float x3, float y3, float z3, Material color,
			Vec3 defaultLightVector) {
		return GLES1TriangleFactory.getInstance().makeTrig( x, y, z, x2, y2, z2, x3, y3, z3, color, defaultLightVector );
	}

	public GeneralTriangleMesh emptyMeshNamed(String name ) {
		return new GLESMesh( name );
	}

	public GeneralTriangle[][] newTriangleGroups(int i) {
		// TODO Auto-generated method stub
		return new GLES1Triangle[ i ][];
	}
}
