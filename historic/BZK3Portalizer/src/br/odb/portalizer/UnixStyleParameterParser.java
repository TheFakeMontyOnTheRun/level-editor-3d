package br.odb.portalizer;

import java.util.HashMap;

public class UnixStyleParameterParser implements ParameterParser {

	@Override
	public HashMap<String, String> parse(String[] parms) {

		HashMap<String, String>  toReturn = new HashMap<String, String>();
		
		String current;
		
		for ( int c = 0; c < parms.length; ++c ) {
			current = parms[ c ];
			
			if ( current.startsWith( getParameterMarker() ) ) {
				
			}
		}		
		
		return toReturn;
	}

	@Override
	public String getParameterMarker() {
		return "--";
	}

	@Override
	public String getValueMarker() {
		return " ";
	}
}
