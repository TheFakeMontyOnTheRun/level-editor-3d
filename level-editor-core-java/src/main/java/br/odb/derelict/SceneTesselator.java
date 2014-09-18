package br.odb.derelict;

import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralQuad;
import br.odb.utils.Direction;

public class SceneTesselator {

	public static World generateQuadsForWorld(World world) {
	
		generateMeshForSector( world.masterSector );
		
		return world;
	}

	private static void generateMeshForSector(GroupSector sector) {
		GeneralQuad quad;
		sector.mesh.clear();
		
		if ( sector.colorForDirection.get( Direction.CEILING ) != null ) {
			quad = new GeneralQuad();
			quad.vertex[ 0 ].set( sector.position );
			quad.vertex[ 2 ].set( sector.position.add( sector.size ) );
			quad.color.set( sector.colorForDirection.get( Direction.CEILING ) );
			sector.mesh.addFace( quad );
		}

		if ( sector.colorForDirection.get( Direction.FLOOR ) != null ) {
			quad = new GeneralQuad();
			quad.vertex[ 0 ].set( sector.position );
			quad.vertex[ 2 ].set( sector.position.add( sector.size ) );
			quad.color.set( sector.colorForDirection.get( Direction.FLOOR ) );
			sector.mesh.addFace( quad );
		}
		
		for ( SpaceRegion sr : sector.sons ) {
			if ( sr instanceof GroupSector ) {
				generateMeshForSector( (GroupSector) sr );
			}
		}
		
	}
}
