package com.games.hoops.hoops;

import android.graphics.Bitmap;
import android.view.MotionEvent;

/**
 * Created by domin on 26 Aug 2016.
 */
public class ToggleButton {
    private boolean toggle = false;
    private Bitmap trueImage, falseImage;
    public int x, y, width, height;
    private boolean playPressed = false;

    public ToggleButton(int x, int y, int width, int height, Bitmap trueImage, Bitmap falseImage){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.trueImage = trueImage;
        this.falseImage = falseImage;
    }

    public void setToggle(boolean toggle){
        this.toggle = toggle;
    }

    public boolean getToggle() {
        return toggle;
    }

    public Bitmap getImage(){
        if(toggle){
            return trueImage;
        }else {
            return falseImage;
        }
    }

    public boolean isPressed(MotionEvent e, int x, int y){
        if (e.getAction()==MotionEvent.ACTION_DOWN){
            if (contains(x, y)){
                playPressed = true;
            } else playPressed = false;

        }
        if (e.getAction()==MotionEvent.ACTION_UP){
            if (contains(x, y) && playPressed){
                toggle = !toggle;
                return true;
            } else playPressed = false;

        }
        return false;
    }

    public boolean contains(int x, int y){
        if (x > this.x && x < this.x + height && y > this.y && y< this.y + this.height){
            return true;
        } else {
            return false;
        }
    }
}
