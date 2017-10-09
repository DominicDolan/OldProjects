package statics;

/**
 * Created by domin on 30 Mar 2017.
 */

public class Units {
    private static double pixel;
    private static double cm;
    private static double inch = 0.393701;
    private static double x48th, y48th;
    private static double x12th, y12th;
    public static final double radians = Math.PI/180.0;
    public static final double degrees = 180.0/Math.PI;

    public static void init(){
        y48th = W.getHeight()/48.0;
        x48th = W.getWidth()/48.0;

        y12th = W.getHeight()/12.0;
        x12th = W.getWidth()/12.0;

        pixel = W.getWidth()/ G.WIDTH;

        cm = G.dpi*inch*pixel;
    }

    public static double pixel() {
        return pixel;
    }

    public static double cm() {
        return cm;
    }

    public static double x48th() {
        return x48th;
    }

    public static double y48th() {
        return y48th;
    }

    public static double x12th() {
        return x12th;
    }

    public static double y12th() {
        return y12th;
    }
}
