package br.odb.derelict;

import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.World;
import br.odb.libstrip.GeneralTriangle;

public class SVGRenderer {

	private static String generateSVGForSector(GroupSector sector) {
		
		StringBuilder sb = new StringBuilder( "<g id='" + sector.id + "' >" );
				
		for ( GeneralTriangle isf : sector.mesh.faces ) {
			sb.append( "<rect " );
			
			sb.append( " x = '" + isf.getVertex( 0 ).x + "' " );
			sb.append( " y = '" + isf.getVertex( 0 ).z + "' " );
			sb.append( " width = '" + ( isf.getVertex( 2 ).x - isf.getVertex( 0 ).x ) + "' " );
			sb.append( " height = '" + ( isf.getVertex( 2 ).z - isf.getVertex( 0 ).z ) + "' " );
			
			sb.append( " style = 'fill: " + isf.material.mainColor.getHTMLColor() + ";' " );
			
			sb.append( " />\n" );
		}
		
		for ( SceneNode sr : sector.getSons() ) {
			if ( sr instanceof GroupSector ) {
				sb.append( generateSVGForSector( (GroupSector) sr ) );
			}
		}
		
		sb.append( "</g>" );
		
		return sb.toString();
	}

	
	public static String renderXZ( World world ) {
		
		StringBuilder sb = new StringBuilder();
		sb.append( "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n<svg xmlns='http://www.w3.org/2000/svg'>\n" );
		sb.append( generateSVGForSector( world.masterSector ) );
		sb.append( "\n</svg>" );
		
		return sb.toString();
	}
}
