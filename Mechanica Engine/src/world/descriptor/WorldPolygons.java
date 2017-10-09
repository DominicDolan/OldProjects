package world.descriptor;

import models.TexturedModel;
import org.jbox2d.common.Vec2;
import renderengine.Painter;
import statics.F;
import statics.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by domin on 21 Apr 2017.
 */
public class WorldPolygons {
    private Ground ground;
    public Map<String, Vec2> keyLocations = new HashMap<>();

    private List<TexturedModel> groundModels;
    private Vec2 location;


    public WorldPolygons(Vec2 location){
        this.location = location;
    }

    public void addKeyLocation(String name, Vec2 keyLocation){
        keyLocations.put(name, keyLocation.add(location));
    }

    public void setPolygons(List<Vec2[]> polygons){
        for (int i = 0; i < polygons.size(); i++) {
            F.addAll(polygons.get(i), location);
        }

        ground = new Ground(polygons);
    }


    public void setGroundModels(){
        groundModels = new ArrayList<>();
        for (int i = 0; i < ground.polygons.size(); i++) {
            float[] vertices = F.vecArraytoFloats(ground.polygons.get(i));
            groundModels.add(new TexturedModel(Loader.loadRawModel(vertices), 0));
        }
    }

    public void drawGround(Painter g){
        int count = groundModels.size();
        for (int i = 0; i < count; i++) {
            g.drawFillModel(groundModels.get(i));
        }
    }

    public Vec2 getLocation() {
        return location;
    }
}
