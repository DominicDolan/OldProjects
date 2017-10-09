package com.games.hoops.hoops;

import android.graphics.Bitmap;

/**
 * Created by domin on 30 Jun 2016.
 */
public class Background {
    private int color;
    private Bitmap image,distantfloor,thumbnail;
    public int[] starLocations;
    public TextureAttributes floorTexture1,floorTexture2;


    public Background(Settings settings){
        floorTexture1 = new TextureAttributes();
        floorTexture2 = new TextureAttributes();

        set(settings);

    }

    public void set(Settings settings){
        switch(settings.getBackground()){
            case Settings.BACKGROUND_NY:
                image = Assets.skyline;
                thumbnail = Assets.thumbnailNY;
                distantfloor = Assets.distantsea;
                color = 0xFF006994;

                floorTexture1.setFloorTextures(Assets.wave);
                floorTexture1.setWavelength(0.4f);
                floorTexture1.setIsFlat(true);

                floorTexture2.setFloorTextures(Assets.foam);
                floorTexture2.setWavelength(1.5f);
                floorTexture2.setIsFlat(false);
                floorTexture2.newAnchorPoints(Assets.foam.length);
                for (int i = 0; i < Assets.foam.length; i++) {
                    floorTexture2.setAnchorPoint(0.5f,i);
                }

                starLocations = Settings.STAR_LOCATIONS_NY;

                break;
            case Settings.BACKGROUND_MOON:
                image = Assets.moonbackground;
                thumbnail = Assets.thumbnailMoon;
                distantfloor = Assets.moondistance;
                color = 0xFF618092;

                floorTexture1.setFloorTextures(Assets.earthstones);
                floorTexture1.setWavelength(0.2f);
                floorTexture1.setIsFlat(true);

                floorTexture2.setFloorTextures(Assets.earthcraters);
                floorTexture2.setWavelength(4.0f);
                floorTexture2.setIsFlat(false);
                floorTexture2.newAnchorPoints(2);
                floorTexture2.setAnchorPoint(0.5f,0);
                floorTexture2.setAnchorPoint(0.5f,1);

                starLocations = Settings.STAR_LOCATIONS_MOON;

                break;
            case Settings.BACKGROUND_PARIS:


                break;
            case Settings.BACKGROUND_SATURN:
                image = Assets.saturnbackground;
                thumbnail = Assets.thumbnailSaturn;
                distantfloor = Assets.saturndistance;
                color = 0xFF895D3A;

                floorTexture1.setFloorTextures(Assets.saturnstones);
                floorTexture1.setWavelength(0.2f);
                floorTexture1.setIsFlat(true);

                floorTexture2.setFloorTextures(Assets.saturncraters);
                floorTexture2.setWavelength(4.0f);
                floorTexture2.newAnchorPoints(2);
                floorTexture2.setAnchorPoint(0.5f,0);
                floorTexture2.setAnchorPoint(0.5f,1);
                floorTexture2.setIsFlat(false);

                starLocations = Settings.STAR_LOCATIONS_SATURN;

                break;
            case Settings.BACKGROUND_COLORS:
                image = Assets.colorsbackground;
                thumbnail = Assets.colorsbackground;
                distantfloor = Assets.blankimage;
                color = 0xFFFFFFFF;

                floorTexture1.setFloorTextures(Assets.saturnstones);
                floorTexture2.setFloorTextures(Assets.saturncraters);

                starLocations = Settings.STAR_LOCATIONS_COLORS;
                break;
            default:

                break;
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(Settings settings) {

    }

    public Bitmap getDistantfloor() {
        return distantfloor;
    }

    public void setDistantfloor(Settings settings) {

    }

    public Bitmap getImage() {
        return image;
    }

    public static Bitmap getThumbnail(int iD){
        switch (iD){
            case Settings.BACKGROUND_NY:
                return Assets.thumbnailNY;
            case Settings.BACKGROUND_COLORS:
                return Assets.colorsbackground;
            case Settings.BACKGROUND_MOON:
                return Assets.thumbnailMoon;
            case Settings.BACKGROUND_SATURN:
                return Assets.thumbnailSaturn;
            default:
                return Assets.colorsbackground;
        }
    }


    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public class TextureAttributes{
        private Bitmap[] floorTextures;
        private float wavelength = 1;
        private float[] anchorPoint;
        private boolean isFlat = false;

        public float[] getAnchorPoints() {
            return anchorPoint;
        }

        public float getAnchorPoint(int i) {
            return anchorPoint[i];
        }

        public void newAnchorPoints(int quantity){
            anchorPoint = new float[quantity];
        }

        public void setAnchorPoint(float anchorPoint, int i) {
            this.anchorPoint[i] = anchorPoint;
        }

        public Bitmap[] getFloorTextures() {
            return floorTextures;
        }

        public void setFloorTextures(Bitmap[] floorTextures) {
            this.floorTextures = floorTextures;
        }

        public boolean isFlat() {
            return isFlat;
        }

        public void setIsFlat(boolean isFlat) {
            this.isFlat = isFlat;
        }

        public float getWavelength() {
            return wavelength;
        }

        public void setWavelength(float wavelength) {
            this.wavelength = wavelength;
        }
    }
}
