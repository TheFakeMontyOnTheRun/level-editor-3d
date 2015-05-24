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
public class WorldIso3DRenderer extends SwingWorldRenderer {
    private int zoom;

    public void draw( EditorContext context) {
        
        Graphics g = getGraphics();
        
        int index = 0;
        
        for (Sector s : context.getWorld()) {

            if (!s.isMaster()) {
                continue;
            }
            
            if ( index++ == 0 )
                continue;

            for (int f = 0; f < 6; ++f) {
                if (!s.isOpenAt(f)) {
                    drawFaceFor(g, s, f);
                }
            }
        }
    }

    public WorldIso3DRenderer(JPanel panel) {
        super(panel);
    }   
    

    private void drawFaceFor(Graphics g, Sector s, int f) {
        
        g.setColor(java.awt.Color.decode(s.getColor(5).getHTMLColor()));
        Vec3 v3 = new Vec3();

        Vec2 v2_0;
        Vec2 v2_1;
        Vec2 v2_2;
        Vec2 v2_3;

        v3.set(s.getX0(), s.getY0(), s.getZ0());
        v2_0 = getVec2for(v3);

        v3.set(s.getX1(), s.getY0(), s.getZ1());
        v2_1 = getVec2for(v3);

        g.drawRect((int) v2_0.x, (int) v2_0.y, (int) (v2_1.x - v2_0.x), (int) (v2_1.y - v2_0.y));

        v3.set(s.getX0(), s.getY1(), s.getZ0());
        v2_2 = getVec2for(v3);

        v3.set(s.getX1(), s.getY1(), s.getZ1());
        v2_3 = getVec2for(v3);

        g.drawRect((int) v2_2.x, (int) v2_2.y, (int) (v2_3.x - v2_2.x), (int) (v2_2.y - v2_2.y));

        g.drawLine((int) v2_0.x, (int) v2_0.y, (int) v2_2.x, (int) v2_2.y);
        g.drawLine((int) v2_1.x, (int) v2_0.y, (int) v2_3.x, (int) v2_2.y);

        g.drawLine((int) v2_0.x, (int) v2_1.y, (int) v2_2.x, (int) v2_3.y);
        g.drawLine((int) v2_1.x, (int) v2_1.y, (int) v2_3.x, (int) v2_3.y);

        v3.set(s.getX0(), s.getY1(), s.getZ0());
        v2_0 = getVec2for(v3);

        v3.set(s.getX1(), s.getY1(), s.getZ1());
        v2_1 = getVec2for(v3);

        g.drawRect((int) v2_0.x, (int) v2_0.y, (int) (v2_1.x - v2_0.x), (int) (v2_1.y - v2_0.y));
    }

    @Override
    public Vec2 getVec2for(Vec3 v3) {
        return new Vec2(v3.x + v3.y, v3.z + v3.y);
    }

    @Override
    public Vec3 getVec3for(Vec2 v) {
        return new Vec3( v.x, 0, v.y );
    }

    @Override
    public void zoom(int value) {
        this.zoom = value;
    }
}
