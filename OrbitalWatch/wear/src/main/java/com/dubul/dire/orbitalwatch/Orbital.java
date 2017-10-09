/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dubul.dire.orbitalwatch;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;
import android.view.SurfaceHolder;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't shown. On
 * devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient mode.
 */
public class Orbital extends CanvasWatchFaceService {
    /**
     * Update rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    public static Bitmap backgroundDay, backgroundMorning, backgroundEvening, backgroundNight;
    public static Bitmap accessTime, timerIcon, playIcon, watchIcon, resetIcon, pauseIcon, backIcon;
    public static Typeface typeface;
    public static Settings settings;
    private static SharedPreferences prefs;

    public static void setSettingsFromPrefs(){
        settings.setIs24Hour(prefs.getBoolean("CLOCK_FORMAT",true))
                .setShowFunctions(prefs.getBoolean("FUNCTIONS",true))
                .setShowDate(prefs.getBoolean("DATE",true));
    }

    public static void setPrefsFromSettings(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("CLOCK_FORMAT", settings.is24Hour())
                .putBoolean("FUNCTIONS", settings.isShowFunctions())
                .putBoolean("DATE", settings.isShowDate())
                .apply();
    }

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<Orbital.Engine> mWeakReference;

        public EngineHandler(Orbital.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            Orbital.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    public static long currentTimeMillis(){
        return System.nanoTime()/1000000;
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        private int currentSecond = 0;
        private int currentMinute = 0;

        final Handler mUpdateTimeHandler = new EngineHandler(this);
        boolean mRegisteredTimeZoneReceiver = false;

        Calendar calendar;
        boolean mAmbient;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;
        private boolean ambientModeChanged = false;


        private MainState mainState;
        private OrbitalCalendar t;
        private Painter painter;

        long updateDurationMillis = 0;
        private boolean started = false;
        private long beforeUpdateRender = System.nanoTime();

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            settings = new Settings();
            prefs = getSharedPreferences("ORBITAL_PREFS",0);
            setSettingsFromPrefs();

            setWatchFaceStyle(new WatchFaceStyle.Builder(Orbital.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setHotwordIndicatorGravity(Gravity.TOP)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            Resources resources = Orbital.this.getResources();

            typeface = Typeface.createFromAsset(getAssets(), "fonts/arialrounded.ttf");

            mainState = new MainState();
            t = settings.getCalendar();

            backgroundDay = BitmapFactory.decodeResource(getResources(), R.drawable.watch_wallpaper_day);
            backgroundMorning = BitmapFactory.decodeResource(getResources(), R.drawable.watch_wallpaper_morning);
            backgroundEvening = BitmapFactory.decodeResource(getResources(), R.drawable.watch_wallpaper_evening);
            backgroundNight = BitmapFactory.decodeResource(getResources(), R.drawable.watch_wallpaper_night);

            accessTime = BitmapFactory.decodeResource(getResources(),R.drawable.ic_access_time_white);
            timerIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_timer_white);
            playIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_play_arrow_white);
            watchIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_watch_white);
            pauseIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_pause_white);
            resetIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_restore_white);
            backIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_arrow_back_white);

            calendar = new GregorianCalendar();
            Date date = new Date();
            calendar.setTime(date);

        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            setPrefsFromSettings();
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            mAmbient = inAmbientMode;
            ambientModeChanged = true;
            mAmbient = inAmbientMode;
            if (mLowBitAmbient) {
                painter.setAntiAlias(!inAmbientMode);
            }
            invalidate();

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        /**
         * Captures tap event (and tap type) and toggles the backgroundDay color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Resources resources = Orbital.this.getResources();
            mainState.onTapCommand(tapType,x,y,eventTime);
            invalidate();
        }


        public void updateAndRender(Canvas canvas, long millis){
            settings.getCalendar().update();
            if (mAmbient) {
                if (settings.getCalendar().getMinute() != currentMinute || ambientModeChanged) {
                    currentMinute = settings.getCalendar().getMinute();
                    ambientModeChanged = false;
                    mainState.update(millis / 1000f);
                    mainState.ambientRender(painter);
                }
            } else /*if (t.getSecond()!=currentSecond && !mAmbient)*/{
                currentSecond = t.getSecond();
                mainState.update(millis / 1000f);
                mainState.render(painter);
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            holder.addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                                           int width, int height) {
                    started = false;
                    settings.setScreenWidth(width);
                    settings.setScreenHeight(height);
                    settings.setHoursRadius(width/2 - 20);
                    settings.setMinutesRadius(width/2 - 50);
                    mainState.init();

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    setPrefsFromSettings();
                }

            });
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            if (!started){
                started = true;
                painter = new Painter(canvas);
            }

            long deltaMillis = updateDurationMillis;
            updateAndRender(canvas, deltaMillis);
            updateDurationMillis = (System.nanoTime() - beforeUpdateRender) / 1000000L;
            beforeUpdateRender = System.nanoTime();
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
//            Orbital.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
//            Orbital.this.unregisterReceiver(mTimeZoneReceiver);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }
    }
}
