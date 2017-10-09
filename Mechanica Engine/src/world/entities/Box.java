package world.entities;

import org.jbox2d.common.Vec2;

/**
 * Created by domin on 5 Apr 2017.
 */

public class Box extends RawEntity {
    public Box(Vec2 position, float width, float height) {
        super(null, null);
        Vec2[] shape = new Vec2[4];
        shape[0] = new Vec2(-width/2,  height/2);
        shape[1] = new Vec2(-width/2, -height/2);
        shape[2] = new Vec2( width/2, -height/2);
        shape[3] = new Vec2( width/2,  height/2);
        set(position, shape);
    }
}
