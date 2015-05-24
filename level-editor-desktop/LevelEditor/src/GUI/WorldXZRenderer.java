/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import br.odb.libscene.Sector;
import br.odb.utils.math.Vec2;
import br.odb.utils.math.Vec3;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author monty
 */
public class WorldXZRenderer extends SwingWorldRenderer {
    
    int zoom;
    
    /**
     *
     * @param context
     */
    @Override
    public void draw( EditorContext context ) {
        
        Vec2 v2_0;
        Vec3 v3_0;
        Vec2 v2_1;
        Vec3 v3_1;
        
        Graphics g = getGraphics();

        int index = 0;
        
        g.setColor( java.awt.Color.WHITE );
        
        g.fillRect( 0, 0, container.getWidth(), container.getHeight() );
        
        v3_0 = new Vec3( 0, 0, 0 );
        v2_0 = getVec2for( v3_0 );

        v3_1 = new Vec3( 255, 255, 255 );
        v2_1 = getVec2for( v3_1 );    
        
        g.setColor( java.awt.Color.BLACK );
        
        g.drawRect( ( int )v2_0.x, ( int )v2_0.y, ( int )( v2_1.x - v2_0.x ), ( int )( v2_1.y - v2_0.y ) );
        
        for ( Sector s : context.getWorld() ) {
            
            if ( !s.isMaster() )
                continue;
            
            if ( index++ == 0 )
                continue;

            v3_0 = new Vec3( s.getX0(), s.getY0(), s.getZ0() );
            v2_0 = getVec2for( v3_0 );

            v3_1 = new Vec3( s.getX1(), s.getY1(), s.getZ1() );
            v2_1 = getVec2for( v3_1 );            
            
            g.setColor( java.awt.Color.decode( s.getColor( 4 ).getHTMLColor() ) );
            g.fillRect( ( int )v2_0.x, ( int )v2_0.y, ( int )( v2_1.x - v2_0.x ), ( int )( v2_1.y - v2_0.y ) );
            
            g.setColor( context.getCurrentSector() == s ? java.awt.Color.RED : java.awt.Color.GREEN );
            g.drawRect( ( int )v2_0.x, ( int )v2_0.y, ( int )( v2_1.x - v2_0.x ), ( int )( v2_1.y - v2_0.y ) );

        }
        g.setColor( Color.BLACK );
        
        v3_0 = new Vec3( context.getCursor().x, context.getCursor().y, context.getCursor().z );
        v2_0 = getVec2for( v3_0 );
        
        g.drawLine( (int)v2_0.x, 0, (int)v2_0.x, ( int )dimensions.y );        
        g.drawLine( 0, (int)v2_0.y, (int)dimensions.x, (int)v2_0.y );
    }

    public WorldXZRenderer(JPanel panel) {
        super(panel);
    }   
    

    @Override
    public Vec2 getVec2for(Vec3 v) {
        
        float dim = dimensions.x;
        
        if ( dim > dimensions.y )
            dim = dimensions.y;
        
        Vec2 toReturn = new Vec2();
        toReturn.x = v.x * ( dim / ( 255.0f * zoom ) );
        toReturn.y = v.z * ( dim / ( 255.0f * zoom ) );
        
        return toReturn;
    }

    @Override
    public Vec3 getVec3for(Vec2 v) {
                
        float dim = dimensions.x;
        
        if ( dim > dimensions.y )
            dim = dimensions.y;
        
        return new Vec3( v.x * ( ( 255.0f * zoom ) / dim ), Float.NaN, v.y * ( ( 255.0f * zoom ) / dim ) );
    }

    @Override
    public void zoom(int value ) {
        this.zoom = value;
    }
}
