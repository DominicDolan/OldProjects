package world.descriptor;


import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import statics.G;

import java.util.List;

/**
 * Created by domin on 3 Apr 2017.
 */

public class Ground {
    public Body ground;
    private BodyDef bodyDef;
    private EdgeShape shape;
    private FixtureDef fixtureDef;
    public List<Vec2[]> polygons;

    public Ground(List<Vec2[]> polygons){
        this.polygons = polygons;
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        ground = G.world.createBody(bodyDef);

        shape = new EdgeShape();

        fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.6f;


        for (Vec2[] vertices: polygons) {
            for (int i = 0; i < vertices.length-1; i++) {
                shape.set(vertices[i], vertices[i+1]);
                ground.createFixture(fixtureDef);
            }
            shape.set(vertices[vertices.length-1], vertices[1]);
            ground.createFixture(fixtureDef);
        }

    }

    public void setPosition(Vec2 position){
        ground.setTransform(position, 0);
    }

}
