package world.entities;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import statics.G;

/**
 * Created by domin on 10 Aug 2017.
 */
public  class BodyBuilder <T extends Shape>{
    private T shape;
    private Vec2 position = new Vec2(0, 0);
    private float density = 1.0f;
    private float friction = 0.3f;
    private boolean fixedRotation = false;
    private Integer userData = null;
    private CreateWheelJoint createWheelJoint = null;

    public BodyBuilder(T shape){
        this.shape = shape;
    }

    public BodyBuilder<T> setPosition(Vec2 position) {
        this.position = position;
        return this;
    }

    public BodyBuilder<T> setDensity(float density) {
        this.density = density;
        return this;
    }

    public BodyBuilder<T> setFriction(float friction) {
        this.friction = friction;
        return this;
    }

    public BodyBuilder<T> setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
        return this;
    }

    public BodyBuilder<T> setUserData(Integer userData) {
        this.userData = userData;
        return this;
    }

    public BodyBuilder<T> setWheelJoint(Body wheel) {

        return this;
    }

    public Body build(){
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.position.set(position);
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.fixedRotation = fixedRotation;
        Body body = G.world.createBody(bodyDef);

        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;

        body.createFixture(fixtureDef);

        if (userData != null) body.setUserData(userData);

        if (createWheelJoint != null) createWheelJoint.create(body);

        return body;
    }

    private interface CreateWheelJoint {
        void create(Body body);
    }
}
