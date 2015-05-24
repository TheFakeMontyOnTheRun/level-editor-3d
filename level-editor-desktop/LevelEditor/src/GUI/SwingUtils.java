/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import br.odb.utils.math.Vec2;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author monty
 */
class SwingUtils {
    public static Vec2 getVec2For( Dimension dim ) {
        
        Vec2 toReturn = new Vec2();
        toReturn.x = dim.width;
        toReturn.y = dim.height;
        
        return toReturn;        
    }
    
    public static Vec2 getVec2For( Point p ) {
        
        Vec2 toReturn = new Vec2();
        toReturn.x = p.x;
        toReturn.y = p.y;
        
        return toReturn;        
    }    
}
