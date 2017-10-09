package world.entities;

import org.jbox2d.common.Vec2;

/**
 * Created by domin on 6 Apr 2017.
 */

public class Diamond extends RawEntity {
    protected Diamond(int type, int colorOrImage){
        super(type, colorOrImage);
    }

    public Diamond(Vec2 position, float width, float height) {
        super(COLOR, 0x45FF00FF);
        Vec2[] shape = new Vec2[4];
        shape[0] = new Vec2( 0,  height/2);
        shape[1] = new Vec2(  width/2, 0);
        shape[2] = new Vec2( 0, -height/2);
        shape[3] = new Vec2( -width/2,  0);
        set(position, shape);
    }

    public Diamond(int image, Vec2 position, float width, float height){
        super(IMAGE, image);

        set(position, width, height);
    }

    public void set(Vec2 position, float width, float height) {
        Vec2[] shape = new Vec2[4];
        shape[0] = new Vec2( 0,  height/2);
        shape[1] = new Vec2(  width/2, 0);
        shape[2] = new Vec2( 0, -height/2);
        shape[3] = new Vec2( -width/2,  0);

        super.set(position, shape);
    }
}
