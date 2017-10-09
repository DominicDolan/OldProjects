package com.games.hoops.hoops;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private Bitmap gameImage;
    private Rect gameImageSrc;
    private Rect gameImageDst;
    private Canvas gameCanvas;
    private Painter graphics;

    private Thread gameThread;
    private volatile boolean running = false;
    private volatile State currentState;
    private Settings settings;
    private String currentMusic;

    private SharedPreferences prefs;

    private Vibrator vibrator;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private InputHandler inputHandler;
    private long framerateTimer =0;
    private int frameCounter = 0;
    private String framerate = "0 fps";

    public GameView(Context context, int gameWidth, int gameHeight) {
        super(context);
        gameImage = Bitmap.createBitmap(gameWidth, gameHeight,
                Bitmap.Config.RGB_565);
        gameImageSrc = new Rect(0, 0, gameImage.getWidth(),
                gameImage.getHeight());
        gameImageDst = new Rect();
        gameCanvas = new Canvas(gameImage);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        settings = new Settings();
        prefs = context.getSharedPreferences("COIL_PREFS",0);
        setSettingsFromPrefs(settings, prefs);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        graphics = new Painter(gameCanvas);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initInput();
                if (currentState == null) {
                    setCurrentState(new LoadState());
                }
                initGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pauseGame();
            }

        });
    }

    public void init(Context context, int gameWidth, int gameHeight){
        gameImage = Bitmap.createBitmap(gameWidth, gameHeight,
                Bitmap.Config.RGB_565);
        gameImageSrc = new Rect(0, 0, gameImage.getWidth(),
                gameImage.getHeight());
        gameImageDst = new Rect();
        gameCanvas = new Canvas(gameImage);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        settings = new Settings();
        prefs = context.getSharedPreferences("COIL_PREFS",0);
        setSettingsFromPrefs(settings, prefs);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        graphics = new Painter(gameCanvas);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initInput();
                if (currentState == null) {
                    setCurrentState(new LoadState());
                }
                initGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pauseGame();
            }

        });
    }

    public void setCurrentState(State newState) {
        System.gc();
        newState.init();
        currentState = newState;
        inputHandler.setCurrentState(currentState);
    }

    private void initInput() {
        if (inputHandler == null) {
            inputHandler = new InputHandler();
        }
        setOnTouchListener(inputHandler);
        mSensorManager.registerListener(inputHandler, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initGame() {
        running = true;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    private void pauseGame() {
        running = false;
        while (gameThread.isAlive()) {
            try {
                gameThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }

    private void updateAndRender(long delta) {
        currentState.update(delta / 1000f);
        currentState.render(graphics);
//        showFramerate(delta);
        renderGameImage();
    }

    public void showFramerate(long delta){
        framerateTimer += delta;
        frameCounter++;
        if (framerateTimer >= 1000){
            framerate = ""+frameCounter+" fps";
            framerateTimer = 0;
            frameCounter = 0;
        }
        graphics.setColor(0xFF00FF00);
        graphics.setTypeface(null);
        graphics.drawString(framerate, 0, 0, 70, 20);
    }

    private void renderGameImage() {
        Canvas screen = getHolder().lockCanvas();
        if (screen != null) {
            screen.getClipBounds(gameImageDst);
            screen.drawBitmap(gameImage, gameImageSrc, gameImageDst, null);
            getHolder().unlockCanvasAndPost(screen);
        }
    }

    public void run() {
        long updateDurationMillis = 0;
        long sleepDurationMillis = 0;
        while (running) {
            long beforeUpdateRender = System.nanoTime();
            long deltaMillis = sleepDurationMillis + updateDurationMillis;
            updateAndRender(deltaMillis);
            updateDurationMillis = (System.nanoTime() - beforeUpdateRender) / 1000000L;
            sleepDurationMillis = Math.max(2, 17 - updateDurationMillis);
            try {
                Thread.sleep(sleepDurationMillis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setSettingsFromPrefs (Settings settings, SharedPreferences prefs){
        settings.setBackground(prefs.getInt("BACKGROUND",0))
                .setPlayer(prefs.getInt("PLAYER",0))
                .setTotalStars(prefs.getInt("STARS",0))
                .setHighScore(prefs.getInt("HIGHSCORE",0))
                .setStarsObtained(prefs.getInt("NY_STARS",0),Settings.BACKGROUND_NY)
                .setStarsObtained(prefs.getInt("MOON_STARS",0),Settings.BACKGROUND_MOON)
                .setStarsObtained(prefs.getInt("SATURN_STARS",0),Settings.BACKGROUND_SATURN)
                .setStarsObtained(prefs.getInt("COLORS_STARS",0),Settings.BACKGROUND_COLORS)
                .setSoccerUnlocked(prefs.getBoolean("SOCCER_UNLOCKED",true))
                .setStandardUnlocked(prefs.getBoolean("STANDARD_UNLOCKED",true))
                .setBasketballUnlocked(prefs.getBoolean("BASKETBALL_UNLOCKED",false))
                .setGolfUnlocked(prefs.getBoolean("GOLFBALL_UNLOCKED", false))
                .setAsteroidUnlocked(prefs.getBoolean("ASTROID_UNLOCKED",false))
                .setShowTips(prefs.getBoolean("SHOW_TIPS",true));

        Tips.noTipCount = prefs.getInt("NO_TIPS_COUNT", 0);
        Tips.speedUpTipCount = prefs.getInt("SPEED_UP_TIPS_COUNT", 0);
        Tips.healthTipCount = prefs.getInt("HEALTH_TIPS_COUNT", 0);
        Tips.nextStarTipCount = prefs.getInt("NEXT_STAR_TIPS_COUNT", 0);
        Tips.edgeTipCount = prefs.getInt("EDGE_TIPS_COUNT", 0);
        Tips.totalStarsTipCount = prefs.getInt("TOTAL_STARS_TIPS_COUNT", 0);
        Tips.newSkinsTipCount = prefs.getInt("NEW_SKINS_TIPS_COUNT", 0);
    }

    private void setPrefsFromSettings (Settings settings, SharedPreferences prefs){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("BACKGROUND",settings.getBackground())
                .putInt("PLAYER",settings.getPlayer())
                .putInt("STARS",settings.getTotalStars())
                .putInt("HIGHSCORE",settings.getHighScore())
                .putInt("NY_STARS",settings.getStarsObtained(Settings.BACKGROUND_NY))
                .putInt("MOON_STARS",settings.getStarsObtained(Settings.BACKGROUND_MOON))
                .putInt("SATURN_STARS",settings.getStarsObtained(Settings.BACKGROUND_SATURN))
                .putInt("COLORS_STARS",settings.getStarsObtained(Settings.BACKGROUND_COLORS))
                .putBoolean("SOCCER_UNLOCKED", settings.isSoccerUnlocked())
                .putBoolean("BASKETBALL_UNLOCKED", settings.isBasketballUnlocked())
                .putBoolean("GOLFBALL_UNLOCKED", settings.isGolfUnlocked())
                .putBoolean("STANDARD_UNLOCKED", settings.isStandardUnlocked())
                .putBoolean("ASTROID_UNLOCKED", settings.isAsteroidUnlocked())
                .putBoolean("SHOW_TIPS", settings.isShowTips())
                .putInt("NO_TIPS_COUNT", Tips.noTipCount)
                .putInt("SPEED_UP_TIPS_COUNT", Tips.speedUpTipCount)
                .putInt("HEALTH_TIPS_COUNT", Tips.healthTipCount)
                .putInt("NEXT_STAR_TIPS_COUNT", Tips.nextStarTipCount)
                .putInt("EDGE_TIPS_COUNT", Tips.edgeTipCount)
                .putInt("TOTAL_STARS_TIPS_COUNT", Tips.totalStarsTipCount)
                .putInt("NEW_SKINS_TIPS_COUNT", Tips.newSkinsTipCount)
                .apply();
    }

    public Settings getSettings() {
        return settings;
    }

    public String getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(String currentMusic) {
        this.currentMusic = currentMusic;
        Assets.playMusic(currentMusic,true);
    }

    public void vibrate (long millisceonds){
        vibrator.vibrate(millisceonds);
    }

    public void onResume() {
        // TODO Auto-generated method stub

    }

    public void onPause() {
        setPrefsFromSettings(settings,prefs);

    }

}
