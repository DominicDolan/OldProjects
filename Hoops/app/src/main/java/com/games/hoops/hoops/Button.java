package com.games.hoops.hoops;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by domin on 29 Jun 2016.
 */
public class Button {
    private Rect rect;
    int x,y,width,height;
    public Bitmap image;
    private boolean playPressed;
    private boolean isActive = true, isVisible = true;
    private int sound;
    private boolean hasSound = false;

    public Button(int x, int y, int width, int height, Bitmap image){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rect = new Rect(x,y,x+width,y+height);
        this.image = image;
    }

    public boolean contains(int x, int y){
        return rect.contains(x,y);
    }

    public void set(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rect = new Rect(x,y,x+width,y+height);
    }

    public boolean isPressed(MotionEvent e, int x, int y){
        if (e.getAction()==MotionEvent.ACTION_DOWN && isActive){
            if (contains(x, y)){
                playPressed = true;
                if (hasSound) {
                    Assets.playSound(sound);
                }
            } else playPressed = false;

        }
        if (e.getAction()==MotionEvent.ACTION_UP && isActive){
            if (contains(x, y) && playPressed){
                return true;
            } else playPressed = false;

        }
        return false;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setSound (int soundID){
        hasSound = true;
        sound = soundID;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
