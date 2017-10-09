package com.dubul.dire.orbitalwatch;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.wearable.watchface.CanvasWatchFaceService;

/**
 * Created by domin on 13 Aug 2016.
 */
public class Button {
    private RectF rect;
    float x,y,width,height;
    public Bitmap image;
    private boolean playPressed;
    private boolean isActive = true;
    private boolean hasBackground = false;
    private int backgroundColor;
    public int insetTop = 20, insetBottom= 20, insetLeft = 20, insetRight = 20;
    private boolean startAnimation = false, animationHasStarted = false, animationHasFinished = false;
    Matrix matrix;

    public Button(int x, int y, int width, int height, Bitmap image){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rect = new RectF(x,y,x+width,y+height);
        this.image = image;
        matrix = new Matrix();
    }

    public boolean contains(int x, int y){
        return rect.contains(x,y);
    }

    public boolean isPressed(int tapType, int x, int y, long eventTime){
        boolean isTapped = false;
        if(isActive) {
            switch (tapType) {
                case CanvasWatchFaceService.TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case CanvasWatchFaceService.TAP_TYPE_TOUCH_CANCEL:
                    isTapped = false;
                    break;
                case CanvasWatchFaceService.TAP_TYPE_TAP:
                    if (rect.contains(x, y)) {
                        isTapped = true;
                    }
                    break;
            }
        }
        return isTapped;
    }

    public void setY(float y){
        this.y = y;
        rect.set(x,y,x+width,y+height);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setOvalBackground(boolean hasBackground){
        this.hasBackground = hasBackground;
    }

    public RectF getRect() {
        return rect;
    }

    public boolean hasBackground() {
        return hasBackground;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setImageInsets(int top, int bottom, int left, int right ){
        float oldWidth = image.getWidth();
        float oldHeight = image.getHeight();
        float scaleWidth = (width - left - right)/oldWidth;
        float scaleHeight = (height - top - bottom)/oldHeight;
        matrix.reset();
        matrix.postScale(scaleWidth,scaleHeight);
        matrix.postTranslate(x,y);

        insetTop = top;
        insetBottom = bottom;
        insetLeft = left;
        insetRight = right;
        image = Bitmap.createScaledBitmap(image,(int)width - left - right,(int)height - top - bottom,true);
    }

    public boolean isActive() {
        return isActive;
    }
}
