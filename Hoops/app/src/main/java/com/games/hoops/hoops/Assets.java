package com.games.hoops.hoops;

import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by domin on 4 Jun 2016.
 */
public class Assets {
    private static SoundPool soundPool;
    private static MediaPlayer mediaPlayer;
    public static Bitmap welcome,sea,distantsea,skyline,playbutton,soccerball, basketball, standardball,lockedball, golfball;
    public static Bitmap panel, saturndistance, saturnbackground, thumbnailNY, thumbnailSaturn, thumbnailMoon, thumbnailLocked;
    public static Bitmap exitX, menu, restartbutton, menubutton, star, emptystar, moonbackground, moondistance, colorsbackground;
    public static Bitmap blankimage, lockedoverlay, heart, endscreen, lockImage, settingsIcon, settingsButtonIcon, toggleButtonOff, toggleButtonOn;
    public static Bitmap textBox, storeIcon, honeycomb, honeycombflip, fivestar, starchest, tickIcon, xIcon, asteroid;
    public static Bitmap[] wave, foam, saturncraters, saturnstones, earthcraters, earthstones, lock;
    public static FramesAnimation unlockAnimation, astroidFire;
    public static Typeface hobo;
    public static int buttonSelect, playButtonSound, gothoop;

    // 1. Load Assets, initialize Frames and Animations.
    public static void load() {
        loadSounds();

        blankimage = loadBitmap("blank.png",true);

        welcome = loadBitmap("welcome.png", false);
        sea = loadBitmap("seatexture.png",true);
        distantsea = loadBitmap("distantsea.png",true);
        skyline = loadBitmap("skyline.png",1280,false);
        playbutton = loadBitmap("playbutton.png",true);
        restartbutton = loadBitmap("restartbutton.png",true);
        menubutton = loadBitmap("menubutton.png", true);
        settingsButtonIcon = loadBitmap("settingsbutton.png",true);
        settingsIcon = loadBitmap("settingsicon.png",true);
        storeIcon = loadBitmap("store-button.png",130,true);
        fivestar = loadBitmap("fivestar.png",true);
        starchest = loadBitmap("starchest.png",true);

        saturndistance = loadBitmap("saturnmoon/saturndistance.png",true);
        saturnbackground = loadBitmap("saturnmoon/saturnbackground.png",1280,false);
        panel = loadBitmap("panel.png",true);
        exitX = loadBitmap("exitx.png",true);
        menu = loadBitmap("menu.png",true);
        star = loadBitmap("star.png",true);
        emptystar = loadBitmap("emptystar.png",true);
        heart = loadBitmap("health.png",true);
        honeycomb = loadBitmap("uibackground1.png",325, true);
        tickIcon = loadBitmap("tick.png",true);
        xIcon = loadBitmap("x.png",true);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        honeycombflip = Bitmap.createBitmap(honeycomb, 0, 0, honeycomb.getWidth(), honeycomb.getHeight(), m, false);

        soccerball = loadBitmap("soccerball.png",true);
        basketball = loadBitmap("basketball.png",true);
        standardball = loadBitmap("standardball.png",true);
        lockedball = loadBitmap("lockedball.png",true);
        golfball = loadBitmap("golfball.png", 100, 100, true);
        asteroid = loadBitmap("astroid.png",true);

        thumbnailNY = loadBitmap("thumbnail-ny.png",false);
        thumbnailSaturn = loadBitmap("thumbnail-saturn.png",false);
        thumbnailMoon = loadBitmap("earthmoon/moonthumbnail.png",false);
        thumbnailLocked = loadBitmap("thumbnail-locked.png",false);
        lockedoverlay = loadBitmap("lockedoverlay.png",true);

        moonbackground = loadBitmap("earthmoon/moonbackground.png",1280,false);
        moondistance = loadBitmap("earthmoon/moondistance.png",true);

        colorsbackground = loadBitmap("colorsbackground.png",1280,false);
        endscreen = loadBitmap("endscreen.png",true);

        toggleButtonOff = loadBitmap("tickbox-01.png",true);
        toggleButtonOn = loadBitmap("tickbox-02.png",true);
        textBox = loadBitmap("textbox.png",true);

        wave = new Bitmap[16];
        foam = new Bitmap[3];

        saturncraters = new Bitmap[2];
        saturnstones = new Bitmap[4];

        earthcraters = new Bitmap[2];
        earthstones = new Bitmap[4];

        lock = new Bitmap[12];
        lockImage = loadBitmap("lock/lock-01.png",true);

        int j;
        for (int i = 0; i < 12; i++) {
            j = i+1;
            if(j<10) {
                lock[i] = loadBitmap("lock/lock-0" + j + ".png", true);
            } else {
                lock[i] = loadBitmap("lock/lock-" + j + ".png", true);
            }
        }

        Frame[] lockFrames = new Frame[12];

        for (int i = 0; i < 12; i++) {
            if (i==3) {
                lockFrames[3] = new Frame(lock[3], 0.5f);
            } else if (i==11){
                lockFrames[11] = new Frame(lock[11], 0.5f);
            }else {
                lockFrames[i] = new Frame(lock[i], 0.07f);
            }
        }

        unlockAnimation = new FramesAnimation(lockFrames);

        Frame[] astroidFrames = new Frame[3];
        astroidFrames[0] = new Frame(loadBitmap("astroid/astroid-01.png",true), 0.1f);
        astroidFrames[1] = new Frame(loadBitmap("astroid/astroid-02.png",true), 0.1f);
        astroidFrames[2] = new Frame(loadBitmap("astroid/astroid-03.png",true), 0.1f);
        astroidFire = new FramesAnimation(astroidFrames);

        for (int i=0; i<16;i++){
            j = (4+i)%4+1;
            wave[i] = loadBitmap("wave/seawaves"+j+".png",true);
        }
        for (int i=1; i<=3;i++){
            foam[i-1] = loadBitmap("wave/seafoam"+i+".png",true);
        }
        saturncraters[0] = loadBitmap("saturnmoon/saturnmoon-02.png",310,155,true);
        saturncraters[1] = loadBitmap("saturnmoon/saturnmoon-03.png",310,140,true);
        saturnstones[0] = loadBitmap("saturnmoon/saturnmoon-01.png",true);
        for (int i=1; i<4;i++){
            j = i+3;
            saturnstones[i] = loadBitmap("saturnmoon/saturnmoon-0"+j+".png",true);
        }

        earthcraters[0] = loadBitmap("earthmoon/moons-01.png",true);
        earthcraters[1] = loadBitmap("earthmoon/moons-02.png",true);

        for (int i=0; i<4;i++){
            j = i+3;
            earthstones[i] = loadBitmap("earthmoon/moons-0"+j+".png",true);
        }

        hobo = Typeface.createFromAsset(MainActivity.assets, "fonts/HoboStd.otf");
    }

    // 2. Load sounds here
    private static void loadSounds() {
        buttonSelect = loadSound("button.wav");
        playButtonSound = loadSound("playbutton.wav");
        gothoop = loadSound("gothoop.wav");
    }

    public static void onResume() {
        loadSounds();
        // May be a good place to play music.
        playMusic(MainActivity.sGame.getCurrentMusic(),true);
    }

	/*
	 * The Following Methods do NOT need to be changed.
	 */

    public static void onPause() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    private static Bitmap loadBitmap(String filename, boolean transparency) {
        InputStream inputStream = null;
        try {
            inputStream = MainActivity.assets.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (transparency) {
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        } else {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                options);
        return bitmap;
    }

    private static Bitmap loadBitmap(String filename, int width, int height, boolean transparency){
        Bitmap bitmap = loadBitmap(filename,transparency);
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
        return bitmap;
    }

    private static Bitmap loadBitmap(String filename, int width, boolean transparency){
        Bitmap bitmap = loadBitmap(filename,transparency);
        int height = (int) (((float)bitmap.getHeight()/bitmap.getWidth())*width);
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static int loadSound(String filename) {
        int soundID = 0;
        if (soundPool == null) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(25)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build())
                    .build();
        }
        try {
            soundID = soundPool.load(MainActivity.assets.openFd(filename),
                    1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundID;
    }

    public static void playSound(int soundID) {
        if (soundPool != null && MainActivity.sGame.getSettings().isSoundEffectsOn()) {
            soundPool.play(soundID, 1, 1, 1, 0, 1);
        } else Log.d("Game","Null Sound ID: "+soundID);
    }

    public static void playMusic(String filename, boolean looping) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            AssetFileDescriptor afd = MainActivity.assets.openFd(filename);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(looping);

            if (MainActivity.sGame.getSettings().isMusicPlaying()) {
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic(){
        mediaPlayer.stop();
        mediaPlayer = null;
    }

    public static  void startMusic(){
        playMusic(MainActivity.sGame.getCurrentMusic(),true);
    }
}
