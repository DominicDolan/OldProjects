package com.dubul.dire.orbitalwatch;

/**
 * Created by domin on 8 Aug 2016.
 */
public abstract class State {
    public final static float PI = (float) Math.PI;

    public abstract void init();

    public abstract void update(float delta);

    public abstract void render(Painter c);

    public abstract void ambientRender(Painter c);

}
