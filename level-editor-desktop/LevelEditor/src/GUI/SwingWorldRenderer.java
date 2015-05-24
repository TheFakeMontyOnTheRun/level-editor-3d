/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import br.odb.utils.math.Vec2;
import br.odb.utils.math.Vec3;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author monty
 */
abstract class SwingWorldRenderer extends WorldRenderer {
    
    JPanel container;
    
    public SwingWorldRenderer( JPanel panel ) {
        super( SwingUtils.getVec2For( panel.getSize() ), SwingUtils.getVec2For( panel.getVisibleRect().getLocation() ) );
        
        container = panel;
    }    
    
    
    public Graphics getGraphics() {
        return container.getGraphics();
    }

    abstract public Vec2 getVec2for(Vec3 v);

    abstract public Vec3 getVec3for(Vec2 v);   
    
    void drawTriangle(Vec3 p0, Vec3 p1, Vec3 p2) {
        
        Graphics g = getGraphics();
        
        Vec2 p0_2 = getVec2for( p0 );
        Vec2 p1_2 = getVec2for( p1 );
        Vec2 p2_2 = getVec2for( p2 );
        
        g.setColor( Color.PINK );
        g.drawLine( ( int )p0_2.x, ( int )p0_2.y, ( int )p1_2.x, ( int )p1_2.y );
        g.drawLine( ( int )p0_2.x, ( int )p0_2.y, ( int )p2_2.x, ( int )p2_2.y );
        g.drawLine( ( int )p2_2.x, ( int )p2_2.y, ( int )p1_2.x, ( int )p1_2.y );
    }
}
