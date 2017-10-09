package com.games.hoops.hoops;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by domin on 4 Jun 2016.
 */
public class InputHandler implements View.OnTouchListener, SensorEventListener {
    private State currentState;

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int scaledX = (int) ((event.getX() / v.getWidth()) * MainActivity.GAME_WIDTH);
        int scaledY = (int) ((event.getY() / v.getHeight()) * MainActivity.GAME_HEIGHT);
        return currentState.onTouch(event, scaledX, scaledY);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        currentState.onSensorChanged(x,y,z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
