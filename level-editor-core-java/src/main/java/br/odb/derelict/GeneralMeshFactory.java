package br.odb.derelict;

import br.odb.libstrip.AbstractTriangle;
import br.odb.libstrip.IndexedSetFace;
import br.odb.libstrip.Mesh;
import br.odb.libstrip.MeshFactory;
import br.odb.utils.math.Vec3;

public class GeneralMeshFactory extends MeshFactory {

	@Override
	public Mesh emptyMeshNamed(String meshName) {
		return null;
	}

	@Override
	public IndexedSetFace makeTrig(float arg0, float arg1, float arg2,
			float arg3, float arg4, float arg5, float arg6, float arg7,
			float arg8, int arg9, Vec3 arg10) {

		return null;
	}

	@Override
	public AbstractTriangle[][] newTriangleGroups(int arg0) {
		return null;
	}
}
