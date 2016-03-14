package br.odb.moonshot;

import br.odb.liboldfart.SimpleWavefrontOBJLoader;
import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.World;
import br.odb.libstrip.TriangleMesh;
import br.odb.libstrip.builders.GeneralTriangleFactory;

import java.io.InputStream;
import java.util.List;

public class WavefrontOBJImporter {

	public static World build(InputStream meshData, InputStream materialData) {
		SimpleWavefrontOBJLoader loader = new SimpleWavefrontOBJLoader( new GeneralTriangleFactory() );
		WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
		World world;
		GroupSector sector;
		GroupSector master = new GroupSector("root");
		List<TriangleMesh> meshes = loader.loadMeshes( meshData, matLoader.parseMaterials( materialData ) );
		
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
