package com.dubul.dire.orbitalwatch;

import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Created by domin on 8 Aug 2016.
 */
public class MainState extends State{
    private int width;
    private int height;
    private float fortyEighths;
    private float centerX,centerY;
    private OrbitalCalendar t;
    private Button startTimerButton, backButton, playTimerButton, pauseTimerButton, resetTimerButton;
    private boolean isInTimer = false;
    String stopWatchHours = "0", stopWatchMinutes = "00", stopWatchSeconds = "00";
    long stopWatchStart;
    float stopWatchTime;
    long stopWatchPausedTime, stopWatchLastPausedTime = 0, stopWatchTotalPausedTime = 0;
    boolean stopWatchIsOpen = false, stopWatchIsPlaying = false, stopWatchIsPaused = false;

    float pathAngleLeft, pathAngleRight;

    LabelManager minuteLabels, hourLabels;
    private long stopWatchPauseStart = 0;
    private boolean showFunctions;

    @Override
    public void init() {
        showFunctions = false;
//        showFunctions = Orbital.settings.isShowFunctions();

        width = Orbital.settings.getScreenWidth();
        height = Orbital.settings.getScreenHeight();

        fortyEighths = width/48f;

        pathAngleLeft = Orbital.settings.getPathAngleLeft();
        pathAngleRight = Orbital.settings.getPathAngleRight();


        Orbital.backgroundDay = Bitmap.createScaledBitmap(Orbital.backgroundDay, width, height, true);
        Orbital.backgroundEvening = Bitmap.createScaledBitmap(Orbital.backgroundEvening, width, height, false);
        Orbital.backgroundMorning = Bitmap.createScaledBitmap(Orbital.backgroundMorning, width, height, false);
        Orbital.backgroundNight = Bitmap.createScaledBitmap(Orbital.backgroundNight, width, height, false);

        t = Orbital.settings.getCalendar();

        int buttonRadius = (int) (12*fortyEighths);
        int buttonsX = width/6 - buttonRadius/2;
        int buttonsY = 3*height/4 - buttonRadius/2;
        int nextColumn = width/3;
        int buttonColor = 0xFF1976D2;
        startTimerButton = new Button(buttonsX,buttonsY,buttonRadius,buttonRadius,Orbital.timerIcon);
        startTimerButton.setOvalBackground(showFunctions);
        startTimerButton.setBackgroundColor(buttonColor);
        startTimerButton.setImageInsets(13,17,15,15);

        backButton = new Button(buttonsX,buttonsY,buttonRadius,buttonRadius,Orbital.backIcon);
        backButton.setOvalBackground(true);
        backButton.setBackgroundColor(buttonColor);
        backButton.setImageInsets(15,15,15,15);
        backButton.setActive(false);

        playTimerButton = new Button(buttonsX + 2*nextColumn,buttonsY,buttonRadius,buttonRadius,Orbital.playIcon);
        playTimerButton.setOvalBackground(true);
        playTimerButton.setBackgroundColor(buttonColor);
        playTimerButton.setImageInsets(15,15,15,15);
        playTimerButton.setActive(false);

        pauseTimerButton = new Button(buttonsX + 2*nextColumn,buttonsY,buttonRadius,buttonRadius,Orbital.pauseIcon);
        pauseTimerButton.setOvalBackground(true);
        pauseTimerButton.setBackgroundColor(buttonColor);
        pauseTimerButton.setImageInsets(15,15,15,15);
        pauseTimerButton.setActive(false);

        resetTimerButton = new Button(buttonsX + nextColumn,buttonsY,buttonRadius,buttonRadius,Orbital.resetIcon);
        resetTimerButton.setOvalBackground(true);
        resetTimerButton.setBackgroundColor(buttonColor);
        resetTimerButton.setImageInsets(14,15,15,16);
        resetTimerButton.setActive(false);

        centerX = width/2f;
        centerY = height/2f;

        Orbital.settings.setMinutesRadius(centerX - 7f*fortyEighths);
        Orbital.settings.setHoursRadius(centerY - 3f*fortyEighths);

        Orbital.settings.setSecondsTextSize(4.5f*fortyEighths);
        Orbital.settings.setOrbitalTextSize(3.8f*fortyEighths);
        Orbital.settings.setDateTextSize(3f*fortyEighths);

        minuteLabels = new LabelManager(Label.MINUTES);
        hourLabels = new LabelManager(Label.HOURS);
// Setting a start and end date using a range of 1 week before this moment.
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        cal.setTime(now);
//        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.WEEK_OF_YEAR, -1);
//        long startTime = cal.getTimeInMillis();
//
//        DataReadRequest readRequest = new DataReadRequest.Builder()
//                // The data request can specify multiple data types to return, effectively
//                // combining multiple data queries into one call.
//                // In this example, it's very unlikely that the request is for several hundred
//                // datapoints each consisting of a few steps and a timestamp.  The more likely
//                // scenario is wanting to see how many steps were walked per day, for 7 days.
//                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                // Analogous to a "Group By" in SQL, defines how data should be aggregated.
//                // bucketByTime allows for a time span, whereas bucketBySession would allow
//                // bucketing by "sessions", which would need to be defined in code.
//                .bucketByTime(1, TimeUnit.DAYS)
//                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                .build();

    }

    @Override
    public void update(float delta) {
//        showFunctions = Orbital.settings.isShowFunctions();

        minuteLabels.update();
        hourLabels.update();
        if (showFunctions) {
            if (stopWatchIsPlaying || stopWatchIsPaused) {
                stopWatchUpdate();
            } else {
                stopWatchTime = 0;
                stopWatchHours = "0";
                stopWatchMinutes = "00";
                stopWatchSeconds = "00";
            }
        } else {
            isInTimer = false;
        }
    }

    private void stopWatchUpdate(){
        if (!stopWatchIsPaused) {
            stopWatchLastPausedTime = stopWatchTotalPausedTime;
            stopWatchTime = (Orbital.currentTimeMillis() - (stopWatchStart+stopWatchTotalPausedTime)) / 1000f;
            int seconds = (int) stopWatchTime % 60;
            if (seconds < 10) {
                stopWatchSeconds = "0" + seconds;
            } else {
                stopWatchSeconds = "" + seconds;
            }

            int minutes = (int) Math.floor(stopWatchTime / 60f) % 60;
            if (minutes < 10) {
                stopWatchMinutes = "0" + minutes;
            } else {
                stopWatchMinutes = "" + minutes;
            }

            stopWatchHours = "" + (int) Math.floor(stopWatchTime / (60f * 60f)) % 60;
        } else {
            stopWatchPausedTime = Orbital.currentTimeMillis() - stopWatchPauseStart;
            stopWatchTotalPausedTime = stopWatchLastPausedTime + stopWatchPausedTime;
        }
    }

    @Override
    public void render(Painter c) {
        if(!isInTimer) {
            startTimerButton.setActive(true);
        } else {
            backButton.setActive(true);
        }


        if (t.getHour()>=6 && t.getHour() < 12)
            c.drawImage(Orbital.backgroundMorning,0,0);
        else if (t.getHour()>=12 && t.getHour() < 18)
            c.drawImage(Orbital.backgroundDay,0,0);
        else if (t.getHour()>=18 && t.getHour() < 23)
            c.drawImage(Orbital.backgroundEvening,0,0);
        else if (t.getHour()>=23 || t.getHour() < 6)
            c.drawImage(Orbital.backgroundNight,0,0);

        minuteLabels.render(c);
        hourLabels.render(c);

        drawDate(c);

        c.setTextSize(Orbital.settings.getSecondsTextSize());
        if (!isInTimer) {
            c.drawCenteredString(String.valueOf(t.getSecond()), centerX, centerY - (centerX - 50) / 2);
        } else {
            c.setColor(0xFFEEEEEE);
            float left = 1*fortyEighths, top = (36 - 6 - 4)*fortyEighths;
            c.fillRoundRect(left,top,46*fortyEighths,20*fortyEighths,10);
            if (stopWatchIsPlaying) {
                c.drawCenteredString(stopWatchHours + ":" + stopWatchMinutes + ":" + stopWatchSeconds, centerX, centerY - (centerX - 50) / 2);
            } else if (stopWatchIsPaused){
                if (t.getSecond()%2==0) {
                    c.drawCenteredString(stopWatchHours + ":" + stopWatchMinutes + ":" + stopWatchSeconds, centerX, centerY - (centerX - 50) / 2);
                }
            } else {
                c.drawCenteredString("0:00:00", centerX, centerY - (centerX - 50) / 2);
            }
        }

        if (showFunctions) {
            c.drawButton(startTimerButton);
            c.drawButton(backButton);
            c.drawButton(playTimerButton);
            c.drawButton(pauseTimerButton);
            c.drawButton(resetTimerButton);
        }

//        hourLabels.logRender(c,0);
//        minuteLabels.logRender(c,1);

    }

    @Override
    public void ambientRender(Painter c) {
        startTimerButton.setActive(false);

        c.setColor(Color.BLACK);
        c.fillRect(0,0,width,height);

        minuteLabels.render(c);
        hourLabels.render(c);

        drawDate(c);
    }

    public void drawDate(Painter c) {
        if (Orbital.settings.isShowDate()) {
            c.setColor(0xFFFFFFFF);
            c.setTextSize(Orbital.settings.getDateTextSize());
            c.setTypeface(Orbital.typeface);
            c.drawRightString(String.valueOf(t.getDay()) + " " + t.getMonth(), 2 * centerX - 20, centerY - 20);
            c.drawString(t.getDayofWeek(), 20, centerY - 20);
        }
    }

    public void onTapCommand(int tapType, int x, int y, long eventTime){


        if (startTimerButton.isPressed(tapType,x,y,eventTime)){
            isInTimer = true;
        }
        else if (backButton.isPressed(tapType,x,y,eventTime)){
            isInTimer = false;
        }

        if (playTimerButton.isPressed(tapType,x,y,eventTime)){
            if (!stopWatchIsPaused){
                stopWatchStart = Orbital.currentTimeMillis();
            }
            stopWatchIsPlaying = true;
            stopWatchIsPaused = false;
        } else if (pauseTimerButton.isPressed(tapType,x,y,eventTime)){
            stopWatchPauseStart = Orbital.currentTimeMillis();
            stopWatchIsPaused = true;
            stopWatchIsPlaying = false;
        }
        if (resetTimerButton.isPressed(tapType,x,y,eventTime)){
            stopWatchTime = 0;
            stopWatchTotalPausedTime = 0;
            stopWatchIsPlaying = false;
            stopWatchIsPaused = false;
        }

        if (isInTimer){
            startTimerButton.setActive(false);
            backButton.setActive(true);
            if(stopWatchIsPlaying){
                playTimerButton.setActive(false);
                pauseTimerButton.setActive(true);
                resetTimerButton.setActive(true);
            } else if (stopWatchIsPaused){
                playTimerButton.setActive(true);
                pauseTimerButton.setActive(false);
                resetTimerButton.setActive(true);
            } else {
                playTimerButton.setActive(true);
                pauseTimerButton.setActive(false);
                resetTimerButton.setActive(false);
            }
        } else {
            backButton.setActive(false);
            playTimerButton.setActive(false);
            pauseTimerButton.setActive(false);
            resetTimerButton.setActive(false);
            startTimerButton.setActive(true);
        }
    }
}
