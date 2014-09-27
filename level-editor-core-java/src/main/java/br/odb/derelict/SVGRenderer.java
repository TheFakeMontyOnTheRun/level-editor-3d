package br.odb.derelict;

import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libstrip.IndexedSetFace;

public class SVGRenderer {

	private static String generateSVGForSector(GroupSector sector) {
		
		String toReturn = "<g id='" + sector.id + "' >";
				
		for ( IndexedSetFace isf : sector.mesh.faces ) {
			toReturn += "<rect ";
			
			toReturn += " x = '" + isf.getVertex( 0 ).x + "' ";
			toReturn += " y = '" + isf.getVertex( 0 ).z + "' ";
			toReturn += " width = '" + ( isf.getVertex( 2 ).x - isf.getVertex( 0 ).x ) + "' ";
			toReturn += " height = '" + ( isf.getVertex( 2 ).z - isf.getVertex( 0 ).z ) + "' ";
			
			toReturn += " style = 'fill: " + isf.getColor().getHTMLColor() + ";' ";
			
			toReturn += " />\n";
		}
		
		for ( SpaceRegion sr : sector.getSons() ) {
			if ( sr instanceof GroupSector ) {
				toReturn += generateSVGForSector( (GroupSector) sr );
			}
		}
		
		toReturn += "</g>";
		
		return toReturn;
	}

	
	public static String renderXZ( World world ) {
		String toReturn= "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n<svg xmlns='http://www.w3.org/2000/svg'>\n" + generateSVGForSector( world.masterSector ) + "\n</svg>";
		return toReturn;
	}
}
