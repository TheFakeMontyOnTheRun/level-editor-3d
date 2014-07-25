/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.odb.derelict.editor;

import br.odb.libscene.Sector;

/**
 *
 * @author monty
 */
class SVGSectorAdapter {

    static String toTopViewRect(Sector s) {
        String toReturn = "";
        
        toReturn += "<rect ";
        
        toReturn += " id = '";
        toReturn += s.getExtraInformation();
        toReturn += "'";
        
        toReturn += " x = '";
        toReturn += s.getX0();
        
        toReturn += "' y = '";
        toReturn += s.getZ0();
        
        toReturn += "' width = '";
        toReturn += s.getDX();
        
        toReturn += "' height = '";
        toReturn += s.getDZ();
        
        toReturn += "' style = 'fill:" + s.getColor( 4 ).getHTMLColor();
        
        
        toReturn += "'>\n";
        toReturn += "</rect>\n";
        
        return toReturn;
    }
    
}
