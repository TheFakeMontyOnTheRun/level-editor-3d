package br.odb.portalizer;

import java.util.HashMap;

public interface ParameterParser {
	HashMap< String, String > parse( String[] parms );
	String getParameterMarker();
	String getValueMarker();
}
