package com.games.hoops.hoops;

import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by domin on 2 Jul 2016.
 */
public class BackgroundSelectionState extends State {

    Button ny,saturn, moon, colors,exitButton, xButton, tickButton;
    int x,y,buttonWidth, buttonHeight, starCount, starsObtained, totalStarsObtained, starsToUnlock;
    int[] starLocations;
    Settings settings;
    private boolean showPanel = false;

    @Override
    public void init() {
        settings = MainActivity.sGame.getSettings();

        MainActivity.showAd(false);

        int buttonX, buttonY;
        int nextRow, nextColumn;

        buttonWidth = (width-140)/2 - 60;
        buttonHeight = (int) (((float)height/width)*buttonWidth);

        buttonX = 140+30;
        buttonY = 50;

        nextColumn = buttonWidth+60;
        nextRow = buttonHeight+60;

        ny = new Button(buttonX,buttonY,buttonWidth,buttonHeight,Assets.thumbnailNY);
        ny.setSound(Assets.buttonSelect);
        colors = new Button(buttonX+nextColumn,buttonY,buttonWidth,buttonHeight,Assets.colorsbackground);
        colors.setSound(Assets.buttonSelect);

        moon = new Button(buttonX,buttonY+nextRow,buttonWidth,buttonHeight,Assets.thumbnailMoon);
        moon.setSound(Assets.buttonSelect);
        saturn = new Button(buttonX+nextColumn,buttonY+nextRow,buttonWidth,buttonHeight,Assets.thumbnailSaturn);
        saturn.setSound(Assets.buttonSelect);

        totalStarsObtained = settings.getTotalStars();

        exitButton = new Button(20,20,100,100,Assets.exitX);
    }

    @Override
    public void update(float delta) {
        UIBackground.update(delta);
    }

    @Override
    public void render(Painter g) {
        UIBackground.render(g);

        g.drawButton(ny);
        x = ny.x;
        y = ny.y;
        starCount = Settings.STAR_LOCATIONS_NY.length;
        starsObtained = settings.getStarsObtained(Settings.BACKGROUND_NY);
        starLocations = Settings.STAR_LOCATIONS_NY;
        drawUnlockedOverlay(g);

        g.drawButton(colors);
        x = colors.x;
        y = colors.y;
        starsToUnlock = Settings.STARS_TO_UNLOCK[Settings.BACKGROUND_COLORS];
        if (totalStarsObtained<starsToUnlock){
            drawLockedOverlay(g);
        } else {
            starCount = Settings.STAR_LOCATIONS_COLORS.length;
            starsObtained = settings.getStarsObtained(Settings.BACKGROUND_COLORS);
            starLocations = Settings.STAR_LOCATIONS_COLORS;
            drawUnlockedOverlay(g);
        }

        x = saturn.x;
        y = saturn.y;
        g.drawButton(saturn);
        starsToUnlock = Settings.STARS_TO_UNLOCK[Settings.BACKGROUND_SATURN];
        if (totalStarsObtained<starsToUnlock){
            drawLockedOverlay(g);
        } else {
            starCount = Settings.STAR_LOCATIONS_SATURN.length;
            starsObtained = settings.getStarsObtained(Settings.BACKGROUND_SATURN);
            starLocations = Settings.STAR_LOCATIONS_MOON;
        }

        x = moon.x;
        y = moon.y;
        g.drawButton(moon);
        starsToUnlock = Settings.STARS_TO_UNLOCK[Settings.BACKGROUND_MOON];
        if (totalStarsObtained<starsToUnlock){
            drawLockedOverlay(g);
        }else {
            starCount = Settings.STAR_LOCATIONS_MOON.length;
            starsObtained = settings.getStarsObtained(Settings.BACKGROUND_MOON);
            starLocations = Settings.STAR_LOCATIONS_MOON;
            drawUnlockedOverlay(g);
        }

        if (showPanel) {
            drawPanelToast(g);
        }

        g.drawButton(exitButton);
    }

    private void drawLockedOverlay(Painter g){
        g.drawImage(Assets.lockedoverlay,x,y,buttonWidth,buttonHeight);

        if (starsToUnlock - totalStarsObtained != 1) {
            g.drawString("Find " + (starsToUnlock - totalStarsObtained) + " more stars to unlock this level", x + 10, y + buttonHeight - 10, buttonWidth - 20);
        } else {
            g.drawString("Find 1 more star to unlock this level", x + 10, y + buttonHeight - 10, buttonWidth - 20);
        }
    }

    private void drawUnlockedOverlay(Painter g){
        int starSize = 50;

        g.setColor(0x99BBBBBB);
        g.fillRect(x,y+buttonHeight - 20 - 2*starSize,buttonWidth,20 + 2*starSize);
        g.setColor(Color.YELLOW);
        for (int i = 0; i < starCount; i++) {
            if (starCount-i>starsObtained) {
                g.drawImage(Assets.emptystar,  x + buttonWidth - 20 - (i+1) * starSize, y+buttonHeight-10 - 2*starSize, starSize - 5, starSize - 5);
            } else {
                g.drawImage(Assets.star, x + buttonWidth - 20 - (i+1) * starSize, y + buttonHeight - 10 - 2*starSize, starSize - 5, starSize - 5);
            }
        }

        g.drawString("" + starsObtained + "/" + starCount + " Stars Found",x+10,y + buttonHeight - 2*starSize - 10, buttonWidth - starSize*starCount - 40, starSize);
        if (starsObtained < starCount) {
            g.drawString("Get a score of " + starLocations[starsObtained] + " to find the next star", x + 10, y + buttonHeight -10, buttonWidth -20);
        }
    }

    private void drawPanelToast(Painter g){
        int panelWidth = width/2;
        int panelHeight = height/2 + 70;
        int top = height/2 - height/4;
        int bottom = top + panelHeight;
        int left = width/2 - panelWidth/2;
        int right = left + panelWidth;
        int buttonSize = 220;
        if(xButton == null){
            xButton = new Button(left + 40,bottom - buttonSize/2,buttonSize,buttonSize,Assets.xIcon);
        }
        if (tickButton==null){
            tickButton = new Button(right - buttonSize - 40,bottom-buttonSize/2,buttonSize,buttonSize,Assets.tickIcon);
        }
        xButton.setActive(true);
        tickButton.setActive(true);

        g.drawImage(Assets.panel,left,top,panelWidth,panelHeight);

        g.setColor(Color.WHITE);
        g.setTypeface(Assets.hobo);
        g.drawCenteredString("You do not have enough stars to",width/2,top + 70, panelWidth-60);
        g.drawCenteredString("unlock this level", width/2, top + 120);
        g.drawCenteredString("Do you want to get",width/2,top + 220, panelWidth - 120);
        g.drawCenteredString("more Stars now?", width/2, top + 280);

        g.drawButton(xButton);
        g.drawButton(tickButton);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if(xButton!=null && tickButton !=null){
            if (xButton.isPressed(e,scaledX,scaledY)){
                showPanel = false;
                xButton.setActive(false);
                tickButton.setActive(false);
                return true;
            }
            if (tickButton.isPressed(e,scaledX,scaledY)){
                setCurrentState(new StoreState());
                return true;
            }
        }

        if(ny.isPressed(e,scaledX,scaledY)){
            settings.setBackground(Settings.BACKGROUND_NY);
            setCurrentState(new MenuState());
        }
        if(saturn.isPressed(e,scaledX,scaledY)){
            starsToUnlock = Settings.STARS_TO_UNLOCK[Settings.BACKGROUND_SATURN];
            if (totalStarsObtained<starsToUnlock){
                showPanel = true;
            }
            else {
                settings.setBackground(Settings.BACKGROUND_SATURN);
                setCurrentState(new MenuState());
            }
        }
        if(moon.isPressed(e,scaledX,scaledY)){
            starsToUnlock = Settings.STARS_TO_UNLOCK[Settings.BACKGROUND_MOON];
            if (totalStarsObtained<starsToUnlock){
                showPanel = true;
            }
            else {
                settings.setBackground(Settings.BACKGROUND_MOON);
                setCurrentState(new MenuState());
            }
        }
        if(colors.isPressed(e,scaledX,scaledY)){
            starsToUnlock = Settings.STARS_TO_UNLOCK[Settings.BACKGROUND_COLORS];
            if (totalStarsObtained<starsToUnlock){
                showPanel = true;
            }
            else {
                settings.setBackground(Settings.BACKGROUND_COLORS);
                setCurrentState(new MenuState());
            }
        }
        if(exitButton.isPressed(e,scaledX,scaledY)){
            setCurrentState(new MenuState());
        }

        return true;
    }

    @Override
    public void onSensorChanged(float x, float y, float z) {

    }

    public void setSettings(Settings settings){
        this.settings = settings;
    }

}
