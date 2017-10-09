package com.dubul.dire.orbitalwatch;

/**
 * Created by domin on 8 Aug 2016.
 */
public class Settings {
    private int screenWidth;
    private int screenHeight;
    private OrbitalCalendar calendar;
    private float minutesRadius,hoursRadius;
    private float orbitalTextSize = 25f, dateTextSize = 26f, secondsTextSize = 30f;

    private float pathAngleLeft = 155f;
    private float pathAngleRight = 25f;

    private boolean is24Hour = true;
    private boolean showFunctions = true;
    private boolean showDate = true;

    public Settings(){
        calendar = new OrbitalCalendar();
        hoursRadius = (screenWidth - 40)/2;
        minutesRadius = (screenWidth - 100)/2;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public OrbitalCalendar getCalendar() {
        return calendar;
    }

    public float getHoursRadius() {
        return hoursRadius;
    }

    public void setHoursRadius(float hoursRadius) {
        this.hoursRadius = hoursRadius;
    }

    public float getMinutesRadius() {
        return minutesRadius;
    }

    public void setMinutesRadius(float minutesRadius) {
        this.minutesRadius = minutesRadius;
    }

    public float getPathAngleLeft() {
        return pathAngleLeft;
    }

    public void setPathAngleLeft(float pathAngleLeft) {
        this.pathAngleLeft = pathAngleLeft;
    }

    public float getPathAngleRight() {
        return pathAngleRight;
    }

    public void setPathAngleRight(float pathAngleRight) {
        this.pathAngleRight = pathAngleRight;
    }

    public float getOrbitalTextSize() {
        return orbitalTextSize;
    }

    public void setOrbitalTextSize(float orbitalTextSize) {
        this.orbitalTextSize = orbitalTextSize;
    }

    public float getDateTextSize() {
        return dateTextSize;
    }

    public void setDateTextSize(float dateTextSize) {
        this.dateTextSize = dateTextSize;
    }

    public float getSecondsTextSize() {
        return secondsTextSize;
    }

    public void setSecondsTextSize(float secondsTextSize) {
        this.secondsTextSize = secondsTextSize;
    }

    public boolean is24Hour() {
        return is24Hour;
    }

    public Settings setIs24Hour(boolean is24Hour) {
        this.is24Hour = is24Hour;
        return this;
    }

    public boolean isShowFunctions() {
        return showFunctions;
    }

    public Settings setShowFunctions(boolean showFunctions) {
        this.showFunctions = showFunctions;
        return this;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public Settings setShowDate(boolean showDate) {
        this.showDate = showDate;
        return this;
    }
}
