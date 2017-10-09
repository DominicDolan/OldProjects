package com.games.hoops.hoops;

import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by domin on 26 Aug 2016.
 */
public class SettingsState extends State {
    Settings settings;
    PlayState playState;

    ToggleButton soundEffects;
    ToggleButton music;
    Button stats, about, exitx;
    boolean statsScreen, aboutScreen, actionDown;

    @Override
    public void init() {
        settings = MainActivity.sGame.getSettings();

        MainActivity.showAd(!settings.isPremium());

        soundEffects = new ToggleButton(1000,10,80,80, Assets.toggleButtonOn,Assets.toggleButtonOff);
        music = new ToggleButton(1000,100,80,80, Assets.toggleButtonOn,Assets.toggleButtonOff);

        soundEffects.setToggle(settings.isSoundEffectsOn());
        music.setToggle(settings.isMusicPlaying());

        int buttonWidth = 430, buttonHeight = 160;
        stats = new Button(width/2-buttonWidth/2,height - 2*buttonHeight - 20 - 110,buttonWidth,buttonHeight,Assets.textBox);
        about = new Button(width/2-buttonWidth/2,height - buttonHeight - 110,buttonWidth,buttonHeight,Assets.textBox);
        exitx = new Button(40,40,90,90,Assets.exitX);
    }

    @Override
    public void update(float delta) {
        UIBackground.update(delta);
    }

    @Override
    public void render(Painter g) {
        if (!aboutScreen && !statsScreen) {
            UIBackground.render(g);
        }

        g.setColor(0xFF40C35A);
        g.setTypeface(Assets.hobo);
        g.drawCenteredString("Sound Effects", width/2,80,400);
        g.drawCenteredString("Music", width/2,160);
        g.drawToggleButton(music);
        g.drawToggleButton(soundEffects);

        g.drawButton(stats);
        g.drawButton(about);
        g.drawButton(exitx);

        g.setTypeface(Assets.hobo);
        g.setColor(0xFF7FC578);
        int statsBottom = stats.y+stats.height, aboutBottom = about.y + about.height;
        g.drawCenteredString("Stats",width/2,statsBottom - 40,stats.width - 180);
        g.drawCenteredString("About",width/2,aboutBottom - 40,about.width - 180);

        if (aboutScreen || statsScreen) {
            UIBackground.render(g);
            g.drawButton(exitx);
        }

        if (statsScreen){
            g.setTextSize(130);
            g.setColor(Color.WHITE);
            g.drawCenteredString("Coming Soon",width/2,height/2);
        }

        if (aboutScreen){
            g.setColor(Color.WHITE);
            g.fillRoundRect(width/7,height/7,5*width/7,5*height/7,80);
            g.setColor(0xFFBBBBBB);
            g.setTextSize(45);
            g.drawCenteredString("All Right Reserved", width/2, height/2 - 180);
            g.drawCenteredString("2016", width/2, height/2 - 110);
            g.drawString("Music:",width/7+100,height/2);
            g.drawCenteredString("\"Cool Adventure Intro\"",width/2, height/2 + 70);
            g.drawCenteredString("by Eric Matyas", width/2, height/2 + 140);
            g.drawCenteredString("www.soundimage.org", width/2, height/2 + 210);
        }
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (music.isPressed(e,scaledX,scaledY)){
            settings.setMusicPlaying(music.getToggle());
            if (music.getToggle()){
                Assets.startMusic();
            } else {
                Assets.stopMusic();
            }
            return true;
        }
        if (soundEffects.isPressed(e,scaledX,scaledY)){
            settings.setMusicPlaying(soundEffects.getToggle());
            return true;
        }
        if (stats.isPressed(e,scaledX,scaledY)){
            statsScreen = true;
        }

        if (about.isPressed(e,scaledX,scaledY)){
            aboutScreen = true;
        }

        if (exitx.isPressed(e,scaledX,scaledY) && !(statsScreen || aboutScreen)){
            if (playState !=null){
                setCurrentState(playState);
            } else {
                setCurrentState(new MenuState());
            }
        }

        if (statsScreen && e.getAction()==MotionEvent.ACTION_DOWN){
            actionDown = true;
        }

        if (aboutScreen && e.getAction()==MotionEvent.ACTION_DOWN){
            actionDown = true;
        }

        if (statsScreen && e.getAction()==MotionEvent.ACTION_UP && actionDown){
            actionDown = false;
            statsScreen = false;
        }

        if (aboutScreen && e.getAction()==MotionEvent.ACTION_UP && actionDown){
            actionDown = false;
            aboutScreen = false;
        }
        return true;
    }

    @Override
    public void onSensorChanged(float x, float y, float z) {

    }

    public SettingsState setPreviousState (PlayState playState){
        this.playState = playState;
        return this;
    }
}
