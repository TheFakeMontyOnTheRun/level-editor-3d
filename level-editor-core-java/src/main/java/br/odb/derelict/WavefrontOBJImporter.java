package br.odb.derelict;

import java.io.InputStream;
import java.util.List;

import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralTriangleMesh;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;

public class WavefrontOBJImporter {

	public static World build(InputStream meshData, InputStream materialData) {
		WavefrontOBJLoader loader = new WavefrontOBJLoader( new GeneralTriangleFactory() );
		List< Material > materials = WavefrontMaterialLoader.parseMaterials( materialData );
		World world;
		GroupSector sector;
		GroupSector master = new GroupSector("root");
		List<GeneralTriangleMesh> meshes = loader.loadMeshes( meshData, materials );
		
		for (GeneralTriangleMesh m : meshes) {

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
