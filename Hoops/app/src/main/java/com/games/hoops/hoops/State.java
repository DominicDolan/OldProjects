package com.games.hoops.hoops;

import android.view.MotionEvent;

/**
 * Created by domin on 4 Jun 2016.
 */
public abstract class State {
    int width = MainActivity.GAME_WIDTH;
    int height = MainActivity.GAME_HEIGHT;
    int centerX = width/2;
    int centerY = height/2;
    float PI = (float) Math.PI;

    public void setCurrentState(State newState) {
        MainActivity.sGame.setCurrentState(newState);
    }

    public void setCurrentMusic(String music){
        MainActivity.sGame.setCurrentMusic(music);
    }

    public void vibrate (long milliseconds){
        MainActivity.sGame.vibrate(milliseconds);
    }

    public abstract void init();

    public abstract void update(float delta);

    public abstract void render(Painter g);

    public abstract boolean onTouch(MotionEvent e, int scaledX, int scaledY);

    public abstract void onSensorChanged(float x, float y, float z);

    public double asin(double oppOverHyp){return Math.asin(oppOverHyp);}
    public double acos(double adjOverHyp){return Math.acos(adjOverHyp);}
    public double atan(double oppOverHyp){return Math.atan(oppOverHyp);}
    public double atan2(double opposite, double adjacent){return Math.atan2(opposite, adjacent);}

    public double sin(double angle){return Math.sin(angle);}
    public double cos(double angle){return Math.cos(angle);}
    public double tan(double angle){return Math.tan(angle);}

    public double logn(double n, double x){return Math.log(x)/Math.log(n);}

    public float minValueofArray (float[] values){
        float min = values[0],current;
        for (int i = 0; i< values.length; i++){
            current = values[i];
            if (min < current){
                min = current;
            }
        }

        return min;
    }

    public int minValueIndex (float[] values){
        int min = 0;
        float current;
        for (int i = 0; i< values.length; i++){
            current = values[i];
            if (values[min] < current){
                min = i;
            }
        }

        return min;
    }
}
