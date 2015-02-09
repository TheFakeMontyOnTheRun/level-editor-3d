package br.odb.leveleditor3d.android;

import br.odb.utils.math.Vec3;

/**
 * Created by monty on 2/8/15.
 */
public class LightSource {

    public final Vec3 position = new Vec3();
    public int intensity = 128;

    public LightSource( Vec3 position, int intensity ) {
        this.position.set( position );
        this.intensity = intensity;
    }
}
