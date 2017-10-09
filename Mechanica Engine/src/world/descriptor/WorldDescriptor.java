package world.descriptor;

import models.TexturedModel;
import org.jbox2d.common.Vec2;
import org.lwjgl.util.vector.Matrix4f;
import renderengine.Painter;
import statics.F;
import statics.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 3 Apr 2017.
 */

public class WorldDescriptor {
    private WorldGraphics graphics;
    private WorldPolygons objects;
    private boolean drawGround = false;
    private int color;
    private Vec2 location = new Vec2(0,0);

    private List<TexturedModel> graphicsModels;

    public WorldDescriptor(WorldGraphics graphics, WorldPolygons objects){
        this.graphics = graphics;
        this.objects = objects;
        this.location = objects.getLocation();
//        Matrix4f matrixTmp = new Matrix4f();
//        for (Matrix4f matrix : graphics.matrices) {
//            matrixTmp.setIdentity();
//            Matrix4f.translate(new Vector3f(location.x, location.y, 0), matrixTmp, matrixTmp);
//            Matrix4f.mul(matrixTmp, matrix, matrix);
//        }
//
//        for (int i = 0; i < graphics.polygons.size(); i++) {
//            F.addAll(graphics.polygons.get(i), location);
//        }
        setGraphicsModels(graphics.polygons);
    }

    public void setGraphicsModels(List<Vec2[]> polygons){
        graphicsModels = new ArrayList<>();
        for (int i = 0; i < polygons.size(); i++) {
            float[] vertices = F.vecArraytoFloats(polygons.get(i));
            graphicsModels.add(new TexturedModel(Loader.loadRawModel(vertices), 0));
        }
    }

    public void setGroundModels(int color){
        this.color = color;
        this.drawGround = true;
        objects.setGroundModels();
    }
//
    public void render(Painter g){
        int count;
        if(drawGround){
            g.setColor(color);
            objects.drawGround(g);
        }


        count = graphicsModels.size();
        for (int i = 0; i < count; i++) {
            g.setColor(getColor(i));
            g.drawFillModel(graphicsModels.get(i));
        }

        count = graphics.imageNumber();
        for (int i = 0; i < count; i++) {
            g.drawImage(getTexture(i), getMatrix(i));
        }

    }



    public int getTexture(int i){
        return graphics.textureIDs.get(i);
    }

    public Matrix4f getMatrix(int i){
        return graphics.matrices.get(i);
    }

    public int getColor(int i){
        long color = graphics.colors.get(i);
        return (int)color;
    }

    public int imageNumber(){
        return graphics.textureIDs.size();
    }

    public Vec2 getLocation(String key){
        return objects.keyLocations.get(key);
    }
}
