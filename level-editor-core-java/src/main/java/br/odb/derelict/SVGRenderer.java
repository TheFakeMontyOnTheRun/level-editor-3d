package br.odb.derelict;

import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralQuad;
import br.odb.libstrip.IndexedSetFace;
import br.odb.utils.Direction;

public class SVGRenderer {

	private static String generateSVGForSector(GroupSector sector) {
		
		String toReturn = "";
				
//		if ( sector.colorForDirection.get( Direction.CEILING ) == null ) {
//			quad = new GeneralQuad();
//			quad.vertex[ 0 ].set( sector.position );
//			quad.vertex[ 2 ].set( sector.position.add( sector.size ) );
//			quad.color.set( sector.colorForDirection.get( Direction.CEILING ) );
//			sector.mesh.addFace( quad );
//		}
//
//		if ( sector.colorForDirection.get( Direction.FLOOR ) == null ) {
//			quad = new GeneralQuad();
//			quad.vertex[ 0 ].set( sector.position );
//			quad.vertex[ 2 ].set( sector.position.add( sector.size ) );
//			quad.color.set( sector.colorForDirection.get( Direction.FLOOR ) );
//			sector.mesh.addFace( quad );
//		}
		
		for ( IndexedSetFace isf : sector.mesh.faces ) {
			toReturn += "<rect ";
			
			toReturn += " x = '" + isf.getVertex( 0 ).x + "' ";
			toReturn += " y = '" + isf.getVertex( 0 ).z + "' ";
			toReturn += " id = '" + sector.id + "' ";
			toReturn += " width = '" + ( isf.getVertex( 2 ).x - isf.getVertex( 0 ).x ) + "' ";
			toReturn += " height = '" + ( isf.getVertex( 2 ).z - isf.getVertex( 0 ).z ) + "' ";
			
			toReturn += " style = 'fill: " + isf.getColor().getHTMLColor() + ";' ";
			
			toReturn += " />\n";
		}
		
		for ( SpaceRegion sr : sector.sons ) {
			if ( sr instanceof GroupSector ) {
				toReturn += generateSVGForSector( (GroupSector) sr );
			}
		}
		
		return toReturn;
	}

	
	public static String renderXZ( World world ) {
		String toReturn= "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n<svg xmlns='http://www.w3.org/2000/svg'>\n" + generateSVGForSector( world.masterSector ) + "\n</svg>";
		return toReturn;
	}
}
