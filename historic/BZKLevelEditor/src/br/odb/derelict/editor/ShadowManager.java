/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.odb.derelict.editor;

import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.utils.math.Vec3;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author monty
 */
class ShadowManager {
    
    private static class LightRegion {
        
        public final Vec3 center;
        public final float a0;
        public final float a1;
        private final float length;



        private LightRegion( Vec3 center, float a0, float a1, float length ) {
            this.center = center;
            this.a0 = a0;
            this.a1 = a1;
            this.length = length;
        }

        private Collection<? extends LightRegion> split(Sector s) {
            
            ArrayList< LightRegion > toReturn = new ArrayList< LightRegion >();
            
            toReturn.add( this );
            
            
            return toReturn;
        }

        private void draw(SwingWorldRenderer worldRenderer) {
            
            float rad = (float) (Math.PI / 180.0f);
            Vec3 p0 = center;
            Vec3 p1 = new Vec3( (float)( center.x - Math.sin( a0 * rad ) * length ), (float)center.y, (float)( center.z - Math.cos( a0 * rad ) * length ) );
            Vec3 p2 = new Vec3( (float)( center.x - Math.sin( a1 * rad ) * length ), (float)center.y, (float)( center.z - Math.cos( a1 * rad ) * length ) );
            
            worldRenderer.drawTriangle( p0, p1, p2 );
        }
    }
    
    private final World world;

    ShadowManager(World world) {
        this.world = world;
    }

    void draw(Vec3 lightPoint, SwingWorldRenderer worldRenderer ) {
        
        LightRegion lightRegion = new LightRegion( lightPoint, 0.0f, 45.0f, 50.0f );      
        
        LightRegion[] splitten = split( lightRegion );
        
        for ( LightRegion lr : splitten ) {
            lr.draw( worldRenderer );
        }
    }    
    
    LightRegion[] split( LightRegion lr ) {
        LightRegion[] toReturn = null;
        ArrayList<LightRegion> workArea = new ArrayList<LightRegion>();
        
        for ( Sector s : world ) { 
            if ( s.contains( lr.center ) )
                workArea.addAll( lr.split( s ) );
        }
        
        toReturn = new LightRegion[ workArea.size() ];
        toReturn = workArea.toArray( toReturn );
        
        return toReturn;
    }
}