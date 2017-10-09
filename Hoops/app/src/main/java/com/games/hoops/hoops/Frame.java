package com.games.hoops.hoops;

import android.graphics.Bitmap;

/**
 * Created by domin on 29 Jul 2016.
 */
public class Frame {
    private Bitmap image;
    private double duration;

    public Frame(Bitmap image, double duration) {
        this.image = image;
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public Bitmap getImage() {
        return image;
    }
}
