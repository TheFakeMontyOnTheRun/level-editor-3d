/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import br.odb.utils.math.Vec2;
import br.odb.utils.math.Vec3;

/**
 *
 * @author monty
 */
abstract class WorldRenderer {
    
    final Vec2 dimensions;
    final Vec2 viewport;
    
    public WorldRenderer( Vec2 dimensions, Vec2 viewport ) {
        this.dimensions = dimensions;
        this.viewport = viewport;
    }
    
    public abstract Vec2 getVec2for( Vec3 v );
    public abstract Vec3 getVec3for( Vec2 v );
    public abstract void draw( EditorContext context );
    public abstract void zoom(int intValue);
}