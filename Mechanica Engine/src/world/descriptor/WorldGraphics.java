package world.descriptor;

import org.jbox2d.common.Vec2;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import statics.F;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 20 Apr 2017.
 */
public class WorldGraphics {
    private Vec2 location;
    public List<Matrix4f> matrices = new ArrayList<>();
    public List<Integer> textureIDs = new ArrayList<>();
    public List<Vec2[]> polygons = new ArrayList<>();
    public List<Long> colors = new ArrayList<>();

    public WorldGraphics(Vec2 location){
        this.location = location;
    }

    public void addImage(Matrix4f matrix, int texture){
        Matrix4f matrixTmp = new Matrix4f();
        matrixTmp.setIdentity();
        Matrix4f.translate(new Vector3f(location.x, location.y, 0), matrixTmp, matrixTmp);
        Matrix4f.mul(matrixTmp, matrix, matrix);
        matrices.add(matrix);

        textureIDs.add(texture);
    }

    public void addPolygon(Vec2[] vertices, long color){
        F.addAll(vertices, location);
        polygons.add(vertices);
        colors.add(color);
    }

    public int getTexture(int i){
        return textureIDs.get(i);
    }

    public Matrix4f getMatrix(int i){
        return matrices.get(i);
    }

    public int getColor(int i){
        long color = colors.get(i);
        return (int)color;
    }

    public int imageNumber(){
        return textureIDs.size();
    }


}
