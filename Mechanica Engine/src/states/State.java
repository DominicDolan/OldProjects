package states;


import renderengine.Painter;
import statics.G;

/**
 * Created by domin on 22 Mar 2017.
 */

public abstract class State {
    public int width = G.WIDTH;
    public int height = G.HEIGHT;
    int centerX = width/2;
    int centerY = height/2;
    float PI = (float) Math.PI;
    public volatile static State currentState;
    private boolean initialised = false;

    public static void setCurrentState(State state){
        System.gc();
        if (!state.initialised)
            state.init();
        state.initialised = true;
        currentState = state;
    }

    public abstract void init();

    public abstract void update(float delta);

    public abstract void render(Painter g);

//    public abstract boolean onTouch(MotionEvent e, double x, double y, double x2, double y2);
//    public abstract boolean onTap(double x, double y);

    public double asin(double oppOverHyp){return Math.asin(oppOverHyp);}
    public double acos(double adjOverHyp){return Math.acos(adjOverHyp);}
    public double atan(double oppOverHyp){return Math.atan(oppOverHyp);}
    public double atan2(double opposite, double adjacent){return Math.atan2(opposite, adjacent);}

    public float sin(double angle){return (float) Math.sin(angle);}
    public float cos(double angle){return (float) Math.cos(angle);}
    public double tan(double angle){return Math.tan(angle);}

    public double logn(double n, double x){return Math.log(x)/Math.log(n);}

    public float max(float n1, float n2){return Math.max(n1,n2);}
    public float min(float n1, float n2){return Math.min(n1,n2);}

    public boolean contains(float x, float y, float left, float top, float right, float bottom){
        return ((y>=top && y<=bottom) && (x>=left && x<=right));
    }
}
