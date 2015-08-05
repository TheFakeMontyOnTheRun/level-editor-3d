package br.odb.derelict;

import java.io.InputStream;
import java.util.List;

import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.World;
import br.odb.libstrip.TriangleMesh;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;

public class WavefrontOBJImporter {

	public static World build(InputStream meshData, InputStream materialData) {
		WavefrontOBJLoader loader = new WavefrontOBJLoader( new GeneralTriangleFactory() );
		WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
		List< Material > materials = matLoader.parseMaterials( materialData );
		World world;
		GroupSector sector;
		GroupSector master = new GroupSector("root");
		List<TriangleMesh> meshes = loader.loadMeshes( meshData, materials );
		
		for (TriangleMesh m : meshes) {

			if ( m.faces.size() > 0) {
				System.out.println( "sector: " + m.name );
				sector = GroupSector.getConvexHull( m );

				if (!sector.isDegenerate()) {
					master.addChild( sector );
				}
			}
		}

		world = new World( master );
		
		return world;
	}

}
