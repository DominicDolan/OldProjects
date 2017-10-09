package com.games.hoops.hoops;

import android.graphics.Bitmap;

/**
 * Created by domin on 30 Jun 2016.
 */
public class Player {
    private Bitmap image;
    private int playerCount=5;

    public Player(Settings settings){
        switch(settings.getPlayer()){
            case Settings.PLAYER_SOCCER:
                image = Assets.soccerball;

                break;
            case Settings.PLAYER_BASKETBALL:
                image = Assets.basketball;

                break;
            case Settings.GOLF_BALL:
                image = Assets.golfball;

                break;
            case Settings.PLAYER_ASTEROID:
                image = Assets.asteroid;

                break;
            case Settings.PLAYER_STANDARD:
                image = Assets.standardball;

                break;
            default:

                break;
        }

    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getImage(int index){
        Bitmap image = Assets.soccerball;
        switch(index){
            case Settings.PLAYER_SOCCER:
                image = Assets.soccerball;

                break;
            case Settings.PLAYER_BASKETBALL:
                image = Assets.basketball;

                break;
            case Settings.GOLF_BALL:
                image = Assets.golfball;

                break;
            case Settings.PLAYER_STANDARD:
                image = Assets.standardball;

                break;
            case Settings.PLAYER_ASTEROID:
                image = Assets.asteroid;
                break;
            default:
                image = Assets.soccerball;
                break;
        }
        return image;
    }

    public void setImage(Settings settings) {
        switch(settings.getPlayer()){
            case Settings.PLAYER_SOCCER:


                break;
            case Settings.PLAYER_BASKETBALL:


            case Settings.PLAYER_ASTEROID:


                break;
            case Settings.PLAYER_STANDARD:


                break;
            default:

                break;
        }
    }

    public int getPlayerCount() {
        return playerCount;
    }
}
