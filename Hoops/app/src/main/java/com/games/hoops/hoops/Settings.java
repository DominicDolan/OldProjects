package com.games.hoops.hoops;

import android.graphics.Bitmap;

/**
 * Created by domin on 30 Jun 2016.
 */
public class Settings {
    public final static int BACKGROUND_NY=0;
    public final static int BACKGROUND_MOON=1;
    public final static int BACKGROUND_SATURN=2;
    public final static int BACKGROUND_PARIS=3;
    public final static int BACKGROUND_COLORS=4;

    public final static int PLAYER_SOCCER=0;
    public final static int PLAYER_BASKETBALL=1;
    public final static int PLAYER_STANDARD=2;
    public final static int GOLF_BALL=3;
    public final static int PLAYER_ASTEROID =4;

    private boolean showTips = true;
    private boolean soccerUnlocked = true;
    private boolean basketballUnlocked = false;
    private boolean standardUnlocked = true;
    private boolean golfUnlocked = false;
    private boolean asteroidUnlocked = true;

    public final static int[] STARS_TO_UNLOCK = {0,6,10,0,2};
    private final int levelNumber = 5;

    public final static int[] STAR_LOCATIONS_NY = {5,12,20,30};
    public final static int[] STAR_LOCATIONS_COLORS = {10,20,30,40};
    public final static int[] STAR_LOCATIONS_MOON = {10,25,35,45};
    public final static int[] STAR_LOCATIONS_SATURN = {15,30,40,60};

    private int[] starsObtained;
    private boolean[] levelsUnlocked;

    private int background;
    private int player;
    private int totalStars;
    private int highScore;

    private boolean musicPlaying = true, soundEffectsOn = true;
    private boolean isPremium = false;

    public Settings(){
        starsObtained = new int[levelNumber];
        levelsUnlocked = new boolean[levelNumber];
    }

    public int getPlayer() {
        return player;
    }

    public Settings setPlayer(int player) {
        this.player = player;
        return this;
    }

    public void setPlayerUnlocked(int player){
        switch (player){
            case PLAYER_STANDARD:
                setStandardUnlocked(true);
                break;
            case PLAYER_SOCCER:
                setSoccerUnlocked(true);
                break;
            case GOLF_BALL:
                setGolfUnlocked(true);
                break;
            case PLAYER_BASKETBALL:
                setBasketballUnlocked(true);
                break;
            case PLAYER_ASTEROID:
                setAsteroidUnlocked(true);
                break;
            default:
                break;
        }
    }

    public int getBackground() {
        return background;
    }



    public Settings setBackground(int background) {
        this.background = background;
        return this;
    }

    public int getTotalStars() {
        return totalStars;
    }

    public Settings setTotalStars(int totalStars) {
        this.totalStars = totalStars;
        return this;
    }

    public Settings setHighScore(int highScore){
        this.highScore = highScore;
        return this;
    }

    public int getHighScore(){
        return this.highScore;
    }

    public Settings setStarsObtained(int value, int i){
        starsObtained[i] = value;
        int totalStars = 0;
        for (int j = 0; j < starsObtained.length; j++) {
            totalStars += starsObtained[j];
        }
        setTotalStars(totalStars);
        return this;
    }

    public int getStarsObtained(int i){
        return starsObtained[i];
    }

    public Settings setLevelUnlocked(boolean value, int i){
        levelsUnlocked[i] = value;
        return this;
    }

    public boolean getLevelUnlocked(int i){
        return levelsUnlocked[i];
    }

    public boolean isSoundEffectsOn() {
        return soundEffectsOn;
    }

    public Settings setSoundEffectsOn(boolean soundEffectsOn) {
        this.soundEffectsOn = soundEffectsOn;
        return this;
    }

    public boolean isLocked(int playerID){
        switch (playerID){
            case PLAYER_STANDARD:
                return standardUnlocked;
            case PLAYER_SOCCER:
                return soccerUnlocked;
            case PLAYER_BASKETBALL:
                return basketballUnlocked;
            case GOLF_BALL:
                return golfUnlocked;
            case PLAYER_ASTEROID:
                return asteroidUnlocked;
            default:
                return true;
        }
    }

    public boolean isMusicPlaying() {
        return musicPlaying;
    }

    public Settings setMusicPlaying(boolean musicPlaying) {
        this.musicPlaying = musicPlaying;
        return this;
    }

    public Settings setAsteroidUnlocked(boolean asteroidUnlocked) {
        this.asteroidUnlocked = asteroidUnlocked;
        return this;
    }

    public Settings setBasketballUnlocked(boolean basketballUnlocked) {
        this.basketballUnlocked = basketballUnlocked;
        return this;
    }

    public Settings setGolfUnlocked(boolean golfUnlocked) {
        this.golfUnlocked = golfUnlocked;
        return this;
    }

    public Settings setLevelsUnlocked(boolean[] levelsUnlocked) {
        this.levelsUnlocked = levelsUnlocked;
        return this;
    }

    public Settings setSoccerUnlocked(boolean soccerUnlocked) {
        this.soccerUnlocked = soccerUnlocked;
        return this;
    }

    public Settings setStandardUnlocked(boolean standardUnlocked) {
        this.standardUnlocked = standardUnlocked;
        return this;
    }

    public boolean isAsteroidUnlocked() {
        return asteroidUnlocked;
    }



    public boolean isBasketballUnlocked() {
        return basketballUnlocked;
    }

    public boolean isGolfUnlocked() {
        return golfUnlocked;
    }

    public boolean isSoccerUnlocked() {
        return soccerUnlocked;
    }

    public boolean isStandardUnlocked() {
        return standardUnlocked;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public boolean isShowTips() {
        return showTips;
    }

    public Settings setShowTips(boolean showTips) {
        this.showTips = showTips;
        return this;
    }
}
