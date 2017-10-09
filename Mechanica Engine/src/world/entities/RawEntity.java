package world.entities;

import models.TexturedModel;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import statics.G;
import statics.F;
import statics.Loader;


/**
 * Created by domin on 5 Apr 2017.
 */

public class RawEntity {
    protected boolean fixedRotation = false;
    protected float density = 1;
    protected float friction = 0.5f;

    public static final int COLOR = 0, IMAGE = 1, ANIMATION = 2;
    public int type;
    public Body body;
    public int image;
    public int color = 0x45FF00FF;
    public TexturedModel model;
    public Vec2 centroid;
    public float imageX, imageY, imageWidth, imageHeight;

    protected RawEntity(int type, int colorOrImage){
        this.type = type;
        if (type == COLOR)
            this.color = colorOrImage;
        else if (type == IMAGE)
            this.image = colorOrImage;
    }

    public RawEntity(Vec2 position, Vec2[] shape){
        type = COLOR;
        if (position!=null && shape != null)
            set(position, shape);
    }

    public RawEntity(Vec2 position, Vec2[] shape, int color){
        this.color = color;
        type = COLOR;
        if (position!=null && shape != null)
            set(position, shape);
    }

    public RawEntity(int image, Vec2 position, Vec2[] shape){
        this.image = image;
        type = IMAGE;
        if (position!=null && shape != null) {
            set(position, shape);
        }
    }

    public RawEntity(int image, Vec2 position, Vec2[] shape, Vec2 imagePosition){
        this.image = image;
//        setImage(imagePosition, shape);
        type = IMAGE;
        if (position!=null && shape != null)
            set(position, shape);
    }

    public void set(Vec2 position, Vec2[] shape){
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;

        bodyDef.position.set(position);
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.fixedRotation = fixedRotation;

        body = G.world.createBody(bodyDef);

        PolygonShape characterShape = new PolygonShape();
        characterShape.set(shape, shape.length);

        fixtureDef.shape = characterShape;
        fixtureDef.friction = friction;
        body.createFixture(fixtureDef);

        centroid = F.getCentroid(shape);

        if (type == COLOR)
            setRawModel(shape);
        else if (type == IMAGE)
            setImage(shape);
    }

    private void setRawModel(Vec2[] shape){
        float[] vertices = F.vecArraytoFloats(shape);
        model = new TexturedModel(Loader.loadRawModel(vertices), 0);
    }

    private void setImage(Vec2[] shape){
        imageX = centroid.x - F.minX(shape);
        imageY = centroid.y - F.minY(shape);
        imageWidth = F.maxX(shape) - F.minX(shape);
        imageHeight = F.maxY(shape) - F.minY(shape);
    }

    public Vec2 getPosition(){
        return body.getPosition();
    }

    public float getRadians(){
        return body.getAngle();
    }

    public void setVelocity(Vec2 vector){
        body.setLinearVelocity(vector);
    }

    public Vec2 getVelocity(){
        return body.getLinearVelocity();
    }
}
