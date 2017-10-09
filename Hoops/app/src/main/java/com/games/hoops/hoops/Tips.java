package com.games.hoops.hoops;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by domin on 24 Oct 2016.
 */
public class Tips {
    public static int width = MainActivity.GAME_WIDTH, height = MainActivity.GAME_HEIGHT;
    public static final int NO_TIP = 0;
    public static final int EDGE_TIP = 1;
    public static final int NEXT_STAR_TIP = 2;
    public static final int HEALTH_TIP = 3;
    public static final int SPEED_UP_TIP = 4;
    public static final int TOTAL_STARS_TIP = 5;
    public static final int NEW_SKINS_TIP = 6;

    public static int noTipCount = 0;
    public static int edgeTipCount = 0;
    public static int nextStarTipCount = 0;
    public static int healthTipCount = 0;
    public static int speedUpTipCount = 0;
    public static int totalStarsTipCount = 0;
    public static int newSkinsTipCount = 0;

    public static int tipID = 0;

    public static Settings settings;
    private static int[] starLocations;
    private static int starsObtained;

    public static void init(){
        settings = MainActivity.sGame.getSettings();

    }

    public static void update(){
        int tmpTip = tipID;
        if ((noTipCount == 10 || noTipCount == 40 || noTipCount == 70) && newSkinsTipCount <=3){
            tipID = NEW_SKINS_TIP;
            noTipCount++;
        } else if ((noTipCount == 20 || noTipCount == 60 || noTipCount == 80) && totalStarsTipCount <=3){
            tipID = TOTAL_STARS_TIP;
            noTipCount++;
        } else if ((noTipCount == 15 || noTipCount == 30 || noTipCount == 50) && speedUpTipCount <= 3) {
            tipID = SPEED_UP_TIP;
            noTipCount++;
        } else if ((noTipCount == 12 || noTipCount == 25 || noTipCount == 35 || noTipCount == 45) && speedUpTipCount <= 4){
            tipID = NEXT_STAR_TIP;
            noTipCount++;
        } else {
            tipID = tmpTip;
        }

        if ((tipID == EDGE_TIP && edgeTipCount >=3) || (tipID == EDGE_TIP && noTipCount%2 == 0)){
            tipID = NO_TIP;
        }
        if ((tipID == NEXT_STAR_TIP && nextStarTipCount >=4) || (tipID == NEXT_STAR_TIP && noTipCount%2 == 1)){
            tipID = NO_TIP;
        }
        if ((tipID == HEALTH_TIP && healthTipCount >= 3) || (tipID == HEALTH_TIP && noTipCount%3 == 0)){
            tipID = NO_TIP;
        }

        starsObtained = settings.getStarsObtained(Settings.BACKGROUND_NY);
        int starCount = Settings.STAR_LOCATIONS_NY.length;

        if (settings.getBackground()==Settings.BACKGROUND_COLORS){
            starsObtained = settings.getStarsObtained(Settings.BACKGROUND_COLORS);
            starCount = Settings.STAR_LOCATIONS_COLORS.length;
        } else if (settings.getBackground()==Settings.BACKGROUND_MOON){
            starsObtained = settings.getStarsObtained(Settings.BACKGROUND_MOON);
            starCount = Settings.STAR_LOCATIONS_MOON.length;
        } else if (settings.getBackground()==Settings.BACKGROUND_SATURN){
            starsObtained = settings.getStarsObtained(Settings.BACKGROUND_SATURN);
            starCount = Settings.STAR_LOCATIONS_SATURN.length;
        }

        if (tipID == NEXT_STAR_TIP && starsObtained >= starCount){
            tipID = NO_TIP;
        }

        switch (tipID){
            case EDGE_TIP:
                edgeTipCount++;
                break;
            case NEXT_STAR_TIP:
                nextStarTipCount++;
                starLocations = Settings.STAR_LOCATIONS_NY;

                if (settings.getBackground()==Settings.BACKGROUND_COLORS){
                    starsObtained = settings.getStarsObtained(Settings.BACKGROUND_COLORS);
                    starLocations = Settings.STAR_LOCATIONS_COLORS;
                } else if (settings.getBackground()==Settings.BACKGROUND_MOON){
                    starsObtained = settings.getStarsObtained(Settings.BACKGROUND_MOON);
                    starLocations = Settings.STAR_LOCATIONS_MOON;
                } else if (settings.getBackground()==Settings.BACKGROUND_SATURN){
                    starsObtained = settings.getStarsObtained(Settings.BACKGROUND_SATURN);
                    starLocations = Settings.STAR_LOCATIONS_SATURN;
                }
                break;
            case HEALTH_TIP:
                healthTipCount++;
                break;
            case SPEED_UP_TIP:
                speedUpTipCount++;
                break;
            case TOTAL_STARS_TIP:
                totalStarsTipCount++;

                break;
            case NEW_SKINS_TIP:
                newSkinsTipCount++;
                break;
            default:
                noTipCount++;
                break;


        }

    }

    public static void render(Painter g){
        int panelWidth = (int) (0.8f*width);
        int panelHeight = (int) (0.8f*height);
        int panelTop = (height - panelHeight)/2;
        int panelLeft = (width - panelWidth)/2;

        g.setColor(Color.WHITE);
        if (tipID != NO_TIP){
            g.drawCenteredImage(Assets.panel,width/2,height/2,panelWidth, panelHeight);
            g.drawString("TIP:",panelLeft + 75, panelTop + 150, 200);
            g.drawCenteredString("Touch to continue",width/2, panelTop+panelHeight - 50, 400);
        }

        g.setColor(0xFFF9A919);
        switch (tipID){
            case EDGE_TIP:
                g.drawCenteredString("Hitting the edge will", width/2, height/2, (panelWidth - 150));
                g.drawCenteredString("cause instant death", width/2, height/2 + 100);
                break;
            case NEXT_STAR_TIP:
                g.drawCenteredString("You will find the next star", width/2, height/2, (panelWidth - 150));
                g.drawCenteredString("after you get a score of " + starLocations[starsObtained], width/2, height/2 + 100);
                break;
            case HEALTH_TIP:
                g.drawCenteredString("You will lose a heart", width/2, height/2, (panelWidth - 150));
                g.drawCenteredString("if you miss a hoop", width/2, height/2 + 100);
                break;
            case SPEED_UP_TIP:
                g.drawCenteredString("The longer you survive", width/2, height/2, (panelWidth - 150));
                g.drawCenteredString("the faster you go", width/2, height/2 + 100);

                break;
            case TOTAL_STARS_TIP:
                g.drawCenteredString("Only stars you haven't found before", width/2, height/2, (panelWidth - 150));
                g.drawCenteredString("will add to your total stars", width/2, height/2 + 100);

                break;
            case NEW_SKINS_TIP:
                g.drawCenteredString("You can trade in a Star to unlock", width/2, height/2, (panelWidth - 150));
                g.drawCenteredString("a new Skin in the Store", width/2, height/2 + 100);
                break;
            default:
                break;


        }
    }
}
