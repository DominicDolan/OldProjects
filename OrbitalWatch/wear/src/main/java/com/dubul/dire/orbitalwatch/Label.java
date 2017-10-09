package com.dubul.dire.orbitalwatch;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by domin on 8 Aug 2016.
 */
public class Label {
    public static final int MINUTES = 0, HOURS = 1, CURRENT = 0, NEXT = 1, THIRD = 2, FOURTH = 3;

    boolean is24Hour;
    int totalHours;
    String name;
    Rect bounds;
    Paint textPaint;

    int type = 0;
    int position = 0;

    int width;
    int height;
    float locationX;
    float locationY;
    float rotation;
    float radius=140;
    float angularSize;
    float padding = 1;
    boolean inPath = false;
    float indAdjustment = 0, allAdjustment = 0;

    int currentLabel, nextLabel, thirdLabel, fourthLabel;
    int currentCurrentLabel, currentQuadrant;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public void update(){

        if (type == MINUTES){
            int minute = Orbital.settings.getCalendar().getMinute();
            float minuteRot = (float) (3 * (Math.PI / 2f + (minute / 30f * Math.PI)));
            setRotation(Math.round(ensureDegrees((minuteRot * 180 / (float) Math.PI)+ indAdjustment + allAdjustment)));

            currentLabel = (int) (5*Math.floor((float)(minute)/5f));
            nextLabel = (currentLabel + 5)%60;
            thirdLabel = (nextLabel+5)%60;
            fourthLabel = (thirdLabel+5)%60;

            updateQuadrant();

            switch (position){
                case CURRENT:
                    setName("" + currentLabel);
                    break;
                case NEXT:
                    setName("" + nextLabel);
                    break;
                case THIRD:
                    setName(""+thirdLabel);
                    break;
                case FOURTH:
                    setName(""+fourthLabel);
                    break;
                default:

                    break;
            }
        } else if (type == HOURS){
            is24Hour = Orbital.settings.is24Hour();
            if (is24Hour){
                totalHours = 24;
            } else{
                totalHours = 12;
            }

            float minute = Orbital.settings.getCalendar().getMinute();
            int hour = Orbital.settings.getCalendar().getHour();
            float hourRot = (float) (3 * (Math.PI / 2f + ((hour+minute/60f) / 6f * Math.PI)));
            setRotation(Math.round(ensureDegrees((hourRot * 180 / (float) Math.PI)+ indAdjustment + allAdjustment)));

            if (is24Hour) {
                currentLabel = hour;
                nextLabel = (currentLabel + 1) % 24;
                thirdLabel = (nextLabel + 1) % 24;
                fourthLabel = (thirdLabel + 1) % 24;
            } else {
                currentLabel = ((hour+1)%12)-1;
                nextLabel = ((currentLabel + 2) % 12)-1;
                thirdLabel = ((nextLabel + 2) % 12)-1;
                fourthLabel = ((thirdLabel + 2) % 12)-1;
            }

            updateQuadrant();

            switch (position){
                case CURRENT:
                    setName("" + currentLabel);
                    break;
                case NEXT:
                    setName("" + nextLabel);
                    break;
                case THIRD:
                    setName(""+thirdLabel);
                    break;
                case FOURTH:
                    setName(""+fourthLabel);
                    break;
                default:

                    break;
            }
        }
    }
    public void setRotation(float rotation) {
        this.rotation = ensureDegrees(rotation);

        if (rotation <= (155 - angularSize/2 - padding) && rotation >= (25 + angularSize/2 + padding)){
            inPath = true;
        }
        else inPath = false;
    }

    public void setName(String name) {
        this.name = name;
        textPaint.getTextBounds(name, 0, name.length(), bounds);
        setWidth(bounds.width());
        setHeight(bounds.height());

        angularSize = (width*180)/((float)(radius*Math.PI));
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Label (int position, int type){
        this.type = type;
        this.position = position;

        is24Hour = Orbital.settings.is24Hour();
        if (is24Hour){
            totalHours = 24;
        } else{
            totalHours = 12;
        }

        name = "00";

        bounds = new Rect();
        textPaint = new Paint();
        textPaint.setTextSize(Orbital.settings.getOrbitalTextSize());
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.getTextBounds(name,0,name.length(),bounds);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Orbital.typeface);
        setWidth(bounds.width());
        setHeight(bounds.height());

        angularSize = (width*180)/((float)(radius*Math.PI));


        if (type == MINUTES){
            setUpMinutes();
        }
        else if (type == HOURS) {
            setUpHours();
        }

        currentQuadrant = (int) Math.floor(ensureDegrees(rotation+1)/90f);
        currentCurrentLabel = currentLabel;
    }

    public void setUpMinutes(){

        int minute = Orbital.settings.getCalendar().getMinute();
        currentLabel = (int) (5*Math.floor((float)(minute)/5f));
        nextLabel = (currentLabel + 5)%60;
        thirdLabel = (nextLabel+5)%60;
        fourthLabel = (thirdLabel+5)%60;

        float minLength = Orbital.settings.getMinutesRadius();
        setRadius(minLength);

        float minRot = (float) (3 * (Math.PI / 2f + (minute / 30f * Math.PI)));
        float currentRotation = minRot * 180 / (float) Math.PI;
        currentRotation = setToQuadrant2(currentRotation);
        switch (position){
            case CURRENT:
                setName("" + currentLabel);
                setRotation(currentRotation);
                break;
            case NEXT:
                setRotation(currentRotation - 90);
                indAdjustment -= 90;
                setName("" + nextLabel);
                break;
            case THIRD:
                setRotation(currentRotation - 180);
                indAdjustment -= 180;
                setName(""+thirdLabel);
                break;
            case FOURTH:
                setRotation(currentRotation + 90);
                indAdjustment += 90;
                setName(""+fourthLabel);
                break;
            default:

                break;
        }
    }

    public void setUpHours(){

        currentLabel = Orbital.settings.getCalendar().getHour();

        nextLabel = (currentLabel + 1)%totalHours;
        thirdLabel = (nextLabel+1)%totalHours;
        fourthLabel = (thirdLabel+1)%totalHours;

        float hrLength = Orbital.settings.getHoursRadius();
        setRadius(hrLength);

        int currentHour = Orbital.settings.getCalendar().getHour();
        float hrRot = (float) (3 * (Math.PI / 2f + (currentHour / 6f * Math.PI)));

        float currentRotation = hrRot * 180 / (float) Math.PI;
        currentRotation = setToQuadrant2(currentRotation);
        switch (position){
            case CURRENT:
                setRotation(currentRotation);
                setName("" + currentHour);
                break;
            case NEXT:
                setRotation(currentRotation - 90);
                indAdjustment = -90;
                setName(""+nextLabel);
                break;
            case THIRD:
                setRotation(currentRotation - 180);
                indAdjustment = -180;
                setName(""+thirdLabel);
                break;
            case FOURTH:
                setRotation(currentRotation + 90);
                indAdjustment = 90;
                setName(""+fourthLabel);
                break;
            default:

                break;
        }
    }

    public float setToQuadrant2(float angle){
        while (angle >= 360){
            angle = angle - 360;
        }

        while (angle < 0){
            angle = angle + 360;
        }

        if (angle >= 0 && angle <90){
            angle = ensureDegrees(angle+90);
            allAdjustment = 90;
        }
        else if (angle >= 180 && angle <270){
            angle = ensureDegrees(angle-90);
            allAdjustment = -90;
        }
        else if (angle >= 270 && angle <360){
            angle = ensureDegrees(angle-180);
            allAdjustment = -180;
        }

        return angle;
    }

    public float ensureDegrees(float angle){
        angle = angle % 360;

        if (angle <0) {
            angle += 360;
        }
        else if (angle > 360){
            angle -= 360;
        }

        return angle;
    }
    
    public int getQuadrant(){
        return currentQuadrant;
    }

    public void updateQuadrant() {
        if (currentLabel != currentCurrentLabel){
            currentQuadrant = (int) Math.floor(ensureDegrees(rotation+1.5f)/90f);
            currentCurrentLabel = currentLabel;
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
