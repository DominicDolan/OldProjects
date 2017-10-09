package com.games.hoops.hoops;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by domin on 2 Jul 2016.
 */
public class PlayerSelectionState extends State {

    Button standard,soccer,basketball,golfball, astroid, exitButton, xButton, tickButton;
    Bitmap playerHolder;
    int playerHolderID;
    boolean showPanel = false, goToStore = false;
    Settings settings;

    @Override
    public void init() {
        settings = MainActivity.sGame.getSettings();
        MainActivity.showAd(!settings.isPremium());

        int buttonWidth, buttonHeight;
        int buttonX, buttonY;
        int nextRow, nextColumn;

        buttonWidth = 160;
        buttonHeight = 160;

        buttonX = width/4 - buttonWidth/2;
        buttonY = height/3 - buttonHeight/2;

        nextColumn = width/4;
        nextRow = height/3;

        standard = new Button(buttonX,buttonY,buttonWidth,buttonHeight,Assets.standardball);
        standard.setSound(Assets.buttonSelect);
        soccer = new Button(buttonX+nextColumn,buttonY,buttonWidth,buttonHeight,Assets.soccerball);
        soccer.setSound(Assets.buttonSelect);
        basketball = new Button(buttonX + 2*nextColumn,buttonY,buttonWidth,buttonHeight,Assets.basketball);
        basketball.setSound(Assets.buttonSelect);
        astroid = new Button(buttonX,buttonY+nextRow,buttonWidth,buttonHeight,Assets.asteroid);
        astroid.setSound(Assets.buttonSelect);
        golfball = new Button(buttonX+nextColumn,buttonY+nextRow,buttonWidth,buttonHeight,Assets.golfball);
        golfball.setSound(Assets.buttonSelect);

        exitButton = new Button(20,20,100,100,Assets.exitX);

        tickButton = new Button(-100,-100,100,100, Assets.tickIcon);
        xButton = new Button(-100,-100,100,100, Assets.xIcon);
        tickButton.setActive(false);
        xButton.setActive(false);

        playerHolder = Assets.standardball;
    }

    @Override
    public void update(float delta) {
        UIBackground.update(delta);
        
        standard.setVisible(settings.isStandardUnlocked());
        soccer.setVisible(settings.isSoccerUnlocked());
        basketball.setVisible(settings.isBasketballUnlocked());
        astroid.setVisible(settings.isAsteroidUnlocked());
        golfball.setVisible(settings.isGolfUnlocked());
        Assets.astroidFire.update(delta);
        if (tickButton != null && xButton != null){
            tickButton.setActive(showPanel);
            xButton.setActive(showPanel);
        }
    }

    @Override
    public void render(Painter g) {
        UIBackground.render(g);
        g.setTypeface(Assets.hobo);
        g.setColor(Color.WHITE);
        g.drawCenteredString("Total Stars: "+settings.getTotalStars(),width/2,100, 500);

        g.drawImage(Assets.lockedball,standard.x,standard.y,standard.width,standard.height);
        g.drawImage(Assets.lockedball,soccer.x,soccer.y,soccer.width,soccer.height);
        g.drawImage(Assets.lockedball,basketball.x,basketball.y,basketball.width,basketball.height);
        g.drawImage(Assets.lockedball,astroid.x,astroid.y,astroid.width,astroid.height);
        g.drawImage(Assets.lockedball,golfball.x,golfball.y,golfball.width,golfball.height);
        
        g.drawButton(standard);
        g.drawButton(soccer);
        g.drawButton(basketball);
        g.drawButton(astroid);
        g.drawButton(golfball);
        g.drawButton(exitButton);

        if (showPanel){
            drawPanelToast(g);
        }
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if(standard.isPressed(e,scaledX,scaledY)){
            if (settings.isStandardUnlocked()) {
                settings.setPlayer(Settings.PLAYER_STANDARD);
                setCurrentState(new MenuState());
            } else {
                showPanel = true;
                MainActivity.showAd(false);
                playerHolder = Assets.standardball;
                playerHolderID = Settings.PLAYER_STANDARD;
            }
        }
        if(soccer.isPressed(e,scaledX,scaledY)){
            if (settings.isSoccerUnlocked()) {
                settings.setPlayer(Settings.PLAYER_SOCCER);
                setCurrentState(new MenuState());
            } else {
                showPanel = true;
                MainActivity.showAd(false);
                playerHolder = Assets.soccerball;
                playerHolderID = Settings.PLAYER_SOCCER;
            }
        }
        if(basketball.isPressed(e,scaledX,scaledY)){
            if (settings.isBasketballUnlocked()) {
                settings.setPlayer(Settings.PLAYER_BASKETBALL);
                setCurrentState(new MenuState());
            } else {
                showPanel = true;
                MainActivity.showAd(false);
                playerHolder = Assets.basketball;
                playerHolderID = Settings.PLAYER_BASKETBALL;
            }
        }
        if(astroid.isPressed(e,scaledX,scaledY)){
            if (settings.isAsteroidUnlocked()) {
                settings.setPlayer(Settings.PLAYER_ASTEROID);
                setCurrentState(new MenuState());
            } else {
                showPanel = true;
                MainActivity.showAd(false);
                playerHolder = Assets.asteroid;
                playerHolderID = Settings.PLAYER_ASTEROID;
            }
        }
        if(golfball.isPressed(e,scaledX,scaledY)){
            if (settings.isGolfUnlocked()) {
                settings.setPlayer(Settings.GOLF_BALL);
                setCurrentState(new MenuState());
            } else {
                showPanel = true;
                MainActivity.showAd(false);
                playerHolder = Assets.golfball;
                playerHolderID = Settings.GOLF_BALL;
            }
        }

        if(exitButton.isPressed(e,scaledX,scaledY)){
            setCurrentState(new MenuState());
        }

        if (tickButton.isPressed(e,scaledX,scaledY)){
            if (!goToStore) {
                settings.setPlayerUnlocked(playerHolderID);
                settings.setTotalStars(settings.getTotalStars()-1);
                showPanel = false;
                MainActivity.showAd(true);
            } else {
                setCurrentState(new StoreState());
            }
        }

        if (xButton.isPressed(e,scaledX,scaledY)){
            showPanel = false;
            goToStore = false;
            MainActivity.showAd(true);
        }

        return true;
    }

    @Override
    public void onSensorChanged(float x, float y, float z) {

    }

    private void drawPanelToast(Painter g){
        int panelWidth = width/2;
        int panelHeight = height/2 + 70;
        int top = height/2 - height/4;
        int bottom = top + panelHeight;
        int left = width/2 - panelWidth/2;
        int right = left + panelWidth;
        int buttonSize = 220;
        xButton.set(left + 40,bottom - buttonSize/2,buttonSize,buttonSize);
        tickButton.set(right - buttonSize - 40,bottom-buttonSize/2,buttonSize,buttonSize);

        xButton.setActive(true);
        tickButton.setActive(true);

        g.drawImage(Assets.panel,left,top,panelWidth,panelHeight);


        g.setColor(Color.WHITE);
        g.setTypeface(Assets.hobo);
        if (playerHolderID==Settings.PLAYER_ASTEROID) {
            int fireSize = (int) ((30f / 11f) * 200);
            int fireTop = top - (int) ((20f / 11f) * 230);
            Assets.astroidFire.render(g, width / 2 - fireSize / 2, fireTop, fireSize, fireSize);
        }
        g.drawCenteredImage(playerHolder, width/2, top, 200,200);
        if (settings.getTotalStars() == 0){
            g.drawCenteredString("stars to unlock this Skin", width / 2, top + 240, panelWidth - 60);
            g.drawCenteredString("You do not have enough", width / 2, top + 180);
            g.drawCenteredString("Do you want more stars from the store?", width / 2, top + 300, panelWidth - 60);
            goToStore = true;
        } else {
            g.drawCenteredString("Do you want to spend 1 star to", width / 2, top + 220, panelWidth - 60);
            g.drawCenteredString("unlock this Skin", width / 2, top + 280);
        }

        g.drawButton(xButton);
        g.drawButton(tickButton);
    }
}
