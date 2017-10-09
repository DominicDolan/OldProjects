package statics;

import matrices.ProjectionMatrix;
import matrices.ViewMatrix;

/**
 * Created by domin on 30 Mar 2017.
 */

public class W {
    public static ViewMatrix viewMatrix, UIView;
    public static ProjectionMatrix projectionMatrix;
    private static double cameraZ;
    private static double width, height;
    private static double viewX, viewY;

    public static void init(double viewHeight){
        height = viewHeight;
        viewMatrix = new ViewMatrix();
        UIView = new ViewMatrix();
        projectionMatrix = new ProjectionMatrix();
        set();
    }

    private static void set(){

        //View Matrix:
        width = height* G.RATIO;
        double fov = Math.toRadians(projectionMatrix.getFov());

        //δ = 2*atan(d/2D)
        //δ is angular diameter, 70 degrees.
        //d is actual size, 10
        //D is distance away. ?
        //d/2*tan(δ/2) = D

        cameraZ = width/(2*Math.tan(fov/2));
        viewMatrix.setTranslate(0,0, (float) (cameraZ));

        //UIMatrix
        width = height* G.RATIO;
        fov = Math.toRadians(projectionMatrix.getFov());

        //δ = 2*atan(d/2D)
        //δ is angular diameter, 70 degrees.
        //d is actual size, 10
        //D is distance away. ?
        //d/2*tan(δ/2) = D

        double cameraZUI = G.WIDTH/(2*Math.tan(fov/2));

        UIView.setTranslate(0,0, (float) (cameraZUI));

        Units.init();
//        State.updateUnits();
//        Painter.updateUnits();
    }

    public static void setView(double x, double y){
        viewX = x;
        viewY = y;
        viewMatrix.setTranslate((float) x, (float) y, (float) (cameraZ));
    }

    public static double getWidth() {
        return width;
    }

    public static void setWidth(double width) {
        W.width = width;
        height = width/ G.RATIO;
        set();
    }

    public static double getHeight() {
        return height;
    }

    public static void setHeight(double height) {
        W.height = height;
        set();
    }

    public static double getViewX() {
        return viewX;
    }

    public static double getViewY() {
        return viewY;
    }
}
