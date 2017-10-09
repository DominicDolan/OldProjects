package statics;

import font.FontType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import renderengine.DisplayManager;
import renderengine.Painter;

/**
 * Created by domin on 30 Mar 2017.
 */

public class G {
    public static boolean LANDSCAPE = true;
    public static int WIDTH = LANDSCAPE? 1280:720;
    public static int HEIGHT = LANDSCAPE? 720:1280;
    public static double RATIO = (double) WIDTH/(double) HEIGHT;
    public static double dpi;
    public static World world;
    public static Painter g;
    public static FontType arial;

    public static void init(float viewHeight, float gravity, boolean landscape, int resolution){
        LANDSCAPE = true;
        WIDTH = landscape? (int) (resolution * RATIO) : resolution;
        HEIGHT = landscape? resolution:(int) (resolution * RATIO) ;

        W.init(viewHeight);
        A.init();
        arial = Loader.loadFont("arial");
        world = new World(new Vec2(0, gravity));
        g = new Painter();
        DisplayManager.initDisplay();
    }

    public static void update(float delta){
        A.update(delta);
        world.step(delta, 8, 3);
    }

}
