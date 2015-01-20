package br.odb.derelict;

import java.io.InputStream;
import java.util.ArrayList;

import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.World;
import br.odb.libstrip.Mesh;

public class WavefrontOBJImporter {

	public static World build(InputStream meshData, InputStream materialData) {
		WavefrontOBJLoader loader = new WavefrontOBJLoader();
		World world;
		GroupSector sector;
		GroupSector master = new GroupSector("root");
		ArrayList<Mesh> meshes = loader.loadMeshes("root", meshData, materialData);
		
		for (Mesh m : meshes) {

			if ( m.faces.size() > 0) {
				System.out.println( "sector: " + m.name );
				sector = GroupSector.getConvexHull( 1, m );

				if (!sector.isDegenerate()) {
					master.addChild( sector );
				}
			}
		}

		world = new World( master );
		
		return world;
	}

}
