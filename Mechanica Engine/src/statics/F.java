package statics;


import org.jbox2d.common.Vec2;

/**
 * Created by domin on 30 Mar 2017.
 */

public class F {
    public static float a, b, g, r;

    public static void splitColor(int color){
        int alpha = (color & 0xFF);
        color = (color - alpha) >> 8;
        int blue = (color & 0xFF);
        color = (color - blue) >> 8;
        int green = (color & 0xFF);
        color = (color - green) >> 8;
        int red = (color & 0xFF);
        a = alpha/255f; b = blue/255f; g = green/255f; r = red/255f;
    }

    public static float getDistance(Vec2 v1, Vec2 v2){
        return (float) Math.hypot(v2.x - v1.x, v2.y - v2.y);
    }

    public static void multiplyAll(Vec2[] array, float multiplicator){
        for (int i = 0; i < array.length; i++) {
            array[i].set(array[i].mul(multiplicator));
        }
    }

    public static void addAll(Vec2[] array, Vec2 add){
        for (int i = 0; i < array.length; i++) {
            array[i].set(array[i].add(add));
        }
    }

    public static float[] vecArraytoFloats(Vec2[] vectors){
        int vertexCount = vectors.length;
        float[] vertices = new float[vertexCount*3];
        for (int i = 0; i < vertexCount; i++) {
            vertices[3*i] = vectors[i].x;
            vertices[3*i + 1] = vectors[i].y;
            vertices[3*i + 2] = 0;
        }
        return vertices;
    }

    public static Vec2 getCentroid(Vec2[] vectors){
        float xTotal = 0, yTotal = 0;
        int number = vectors.length;
        for (int i = 0; i < number; i++) {
            xTotal += vectors[i].x;
            yTotal += vectors[i].y;
        }

        return new Vec2(xTotal/number, yTotal/number);

    }

    public static float minX(Vec2[] vectors){
        int number = vectors.length;
        float minX = vectors[0].x;
        for (int i = 1; i < number; i++) {
            float vecX = vectors[i].x;
            if (vecX < minX) minX = vecX;
        }
        return minX;
    }

    public static float maxX(Vec2[] vectors){
        int number = vectors.length;
        float maxX = vectors[0].x;
        for (int i = 1; i < number; i++) {
            float vecX = vectors[i].x;
            if (vecX > maxX) maxX = vecX;
        }
        return maxX;
    }


    public static float minY(Vec2[] vectors){
        int number = vectors.length;
        float minY = vectors[0].y;
        for (int i = 1; i < number; i++) {
            float vecY = vectors[i].y;
            if (vecY < minY) minY = vecY;
        }
        return minY;
    }

    public static float maxY(Vec2[] vectors){
        int number = vectors.length;
        float maxY = vectors[0].y;
        for (int i = 1; i < number; i++) {
            float vecY = vectors[i].y;
            if (vecY > maxY) maxY = vecY;
        }
        return maxY;
    }

    public static String printVectors(Vec2[] vectors){
        String string = "";
        int count = vectors.length;
        for (int i = 0; i < count; i++) {
            string = string + " (" + vectors[i].x + ", " + vectors[i].y + ")";
        }
        return string;
    }
}
