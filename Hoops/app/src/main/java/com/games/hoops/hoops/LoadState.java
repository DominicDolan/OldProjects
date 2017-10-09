package com.games.hoops.hoops;

import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by domin on 4 Jun 2016.
 */
public class LoadState extends State {

    private MenuState menuState;

    @Override
    public void init() {
        Assets.load();
        menuState = new MenuState();
        UIBackground.init();
    }

    @Override
    public void update(float delta) {
        setCurrentState(menuState);
        Assets.astroidFire.update(delta);
    }

    @Override
    public void render(Painter g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,width,height);
        Assets.astroidFire.render(g,width/2-200,height/2-200,400,400);
    }



    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        return false;
    }

    @Override
    public void onSensorChanged(float x, float y, float z) {

    }
}
