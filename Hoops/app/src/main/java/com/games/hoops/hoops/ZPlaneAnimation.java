package com.games.hoops.hoops;

import android.graphics.Bitmap;

/**
 * Created by domin on 28 Jun 2016.
 */
public class ZPlaneAnimation {
    private boolean initiated =false;
    private int img = 1;
    private Bitmap[] images;
    private boolean isFlat = false;
    private boolean hasImages = false;

    private float[] pixelX, pixelY, pixelWidth, pixelHeight, aspectRatio;
    private float[] physicalX, physicalY, physicalZ, physicalMag, physicalWidth,physicalHeight,physicalDepth;
    private float[] anchorPoints;

    private int screenWidth = MainActivity.GAME_WIDTH;
    private int screenHeight = MainActivity.GAME_HEIGHT;

    private final static double PI = Math.PI;
    private float fov = (float)(1.2);
    private float angleToPixels = ((float)screenWidth)/fov;
    private boolean[] created;
    private int quantity;
    private int[] imageIndex;

    private float startingDistanceZ = 100;
    private float endingDistanceZ = -1.0f;
    private float wavelength;
    private float physicalXlowerbound = 0, physicalXupperbounds = 0;
    private boolean hasStarted = false;
    private float horizon = screenHeight/2;
    private float velocity = 0;
    private float scale = 1.0f/20;
    private float altitude = 0;
    private int imageQuantity;
    private float timer = 0;

    public void init(int quantity, Bitmap image){
        initiated = true;
        img = 0;
        images = new Bitmap[1];
        images[0]=image;
        hasImages = true;
        this.quantity = quantity;
        init();
    }

    public void init (float startingDistance,float startingVelocity ,float wavelength){
        initiated = true;
        this.startingDistanceZ = startingDistance;
        this.wavelength = wavelength;
        this.velocity = startingVelocity;
        quantity = (int) (startingDistance/(startingVelocity*wavelength));

        init();
    }

    public void init(Bitmap[] images){
        this.quantity = images.length;
        this.images=images;

        init();
    }

    public void init(){

        pixelX = new float[quantity];
        pixelY = new float[quantity];
        pixelWidth = new float[quantity];
        pixelHeight = new float[quantity];

        physicalX = new float[quantity];
        physicalY = new float[quantity];
        physicalZ = new float[quantity];
        physicalMag = new float[quantity];
        physicalWidth = new float[quantity];
        physicalHeight = new float[quantity];
        physicalDepth = new float[quantity];

        if (anchorPoints == null && hasImages){
            anchorPoints = new float[quantity];
            for (int i = 0; i < images.length; i++) {anchorPoints[i] = 0;}
        }

        created = new boolean[quantity];
        imageIndex = new int[quantity];
        aspectRatio = new float[quantity];

        float distanceApartZ = velocity*wavelength;


        for (int i=0;i<quantity;i++){
            if (hasImages) {
                imageIndex[i] = img * RandomNumberGenerator.getRandInt(images.length);
                physicalWidth[i] = images[img * imageIndex[i]].getWidth() * scale;
                aspectRatio[i] = images[img * imageIndex[i]].getWidth() / images[img * imageIndex[i]].getHeight();
            } else {
                physicalWidth[i] = 100.0f*scale;
                aspectRatio[i] = 1;
            }
            physicalHeight[i] = physicalWidth[i]/aspectRatio[i];
            physicalDepth[i] = physicalHeight[i];
            if (hasStarted) {
                physicalX[i] = RandomNumberGenerator.getRandFloatBetween(physicalXlowerbound, physicalXupperbounds);
                physicalZ[i] = (i * distanceApartZ + 5);
                physicalMag[i] = getMagnitude(physicalY[i],physicalZ[i],physicalX[i]);

                double delta = (2 * atan(physicalWidth[i], (2 * physicalMag[i])));
                pixelWidth[i] = (float) (delta*angleToPixels);

                if (isFlat){
                    float apparentHeight = (float) (physicalDepth[i]*Math.sin(atan(physicalY[i],physicalZ[i]+physicalDepth[i])));
                    delta = (2 * atan(apparentHeight, (2 * physicalMag[i])));
                    pixelHeight[i] = (float) (delta * angleToPixels);
                }
                else {
                    delta = (2 * atan(physicalHeight[i], (2 * physicalMag[i])));
                    pixelHeight[i] = (float) (delta * angleToPixels);
                }

                delta = 2 * atan(physicalY[i], (2 * physicalZ[i]));
                pixelY[i] = (float) ((screenWidth / fov) * (delta));
            }
            created[i] = hasStarted;
            if (hasImages) {
                physicalY[i] = altitude - physicalHeight[i] * anchorPoints[imageIndex[i]];
            } else {
                physicalY[i] = altitude;
            }
        }
    }

    public void update(float delta){
        timer += delta;
//        if (!hasStarted && timer <= wavelength*(quantity+1)){
//            for (int i = 0; i < quantity; i++) {
//                if (timer < wavelength*i){
//                    created[i] = false;
//                } else {
//                    created[i] = true;
//                }
//            }
//        }

        for (int i = 0; i<quantity;i++){
            if (!created[i]){
                if (!hasStarted && timer < wavelength*i){
                    created[i] = false;
                } else {
                    created[i] = true;
                }

                if (hasImages) {
                    imageIndex[i] = img * RandomNumberGenerator.getRandInt(images.length);
                    float imageWidth = images[imageIndex[i]].getWidth();
                    physicalWidth[i] = imageWidth * scale;
                    aspectRatio[i] = images[imageIndex[i]].getWidth() / images[imageIndex[i]].getHeight();
                }
                physicalHeight[i] = physicalWidth[i]/aspectRatio[i];
                physicalDepth[i] = physicalHeight[i];

//                physicalZ[i] = physicalZ[(quantity+(i-1))%quantity]+velocity* wavelength;
                physicalZ[i] = startingDistanceZ;
                physicalMag[i] = getMagnitude(physicalY[i],physicalZ[i], physicalX[i]);
                physicalX[i] = RandomNumberGenerator.getRandFloatBetween(physicalXlowerbound,physicalXupperbounds);
            }

            if (created[i]){
                double deltaAngle = (2 * atan(physicalWidth[i], (2 * physicalMag[i])));
                pixelWidth[i] = (float) (deltaAngle*angleToPixels);

                if (isFlat){
                    float apparentHeight = (float) (physicalDepth[i]*Math.sin(atan(physicalY[i],physicalZ[i]+physicalDepth[i])));
                    deltaAngle = (2 * atan(apparentHeight, (2 * physicalMag[i])));
                    pixelHeight[i] = (float) (deltaAngle * angleToPixels);
                }
                else {
                    deltaAngle = (2 * atan(physicalHeight[i], (2 * physicalMag[i])));
                    pixelHeight[i] = (float) (deltaAngle * angleToPixels);
                }

                deltaAngle = 2 * atan(physicalY[i], (2 * physicalZ[i]));
                pixelY[i] = horizon + (float) (deltaAngle*angleToPixels);
                pixelX[i] = (float) ((screenWidth)/2 + (screenWidth /fov)*(2*atan(physicalX[i],(2*physicalMag[i]))));
                physicalZ[i] -= velocity*delta;
                physicalMag[i] = getMagnitude(physicalY[i],physicalZ[i],physicalX[i]);
            }

            if (pixelY[i] > screenHeight || pixelX[i] + pixelWidth[i]<0 || pixelX[i] > screenWidth || physicalZ[i]<endingDistanceZ){
                created[i] = false;
            }

        }
    }

    public void render (Painter g){
        int min = minValueIndex(physicalZ);
        int j;
        for(int i = quantity; i > 0; i--){
            j = min + i;
            j = (quantity+(j))%quantity;
            g.drawImage(images[imageIndex[j]], (pixelX[j]), pixelY[j], pixelWidth[j], pixelHeight[j]);
        }
    }

    public double atan(double opposite, double adjacent){
        return Math.atan2(opposite,adjacent);
    }

    public int minValueIndex (float[] values){
        int min = 0;
        float current;
        for (int i = 0; i< values.length; i++){
            current = values[i];
            if (values[min] < current){
                min = i;
            }
        }

        return min;
    }

    public void setImages(Bitmap[] images) {
        hasImages = true;
        imageQuantity = images.length;
        this.images = images;
    }

    public void setXbounds(float physicalXlowerbound,float physicalXupperbounds) {
        this.physicalXlowerbound = physicalXlowerbound;
        this.physicalXupperbounds = physicalXupperbounds;
    }

    public void setWavelength(float wavelength) {
        this.wavelength = wavelength;
    }

    public void setFlat(boolean isFlat) {
        this.isFlat = isFlat;
    }

    public void setFov(float fov) {
        angleToPixels = ((float)screenWidth)/fov;
        this.fov = fov;
    }

    public void setIsStarted(boolean isStarted) {
        this.hasStarted = isStarted;
    }

    public void setStartingDistanceZ(float startingDistanceZ) {
        this.startingDistanceZ = startingDistanceZ;
    }


    public void setEndingDistanceZ(float endingDistanceZ) {
        this.endingDistanceZ = endingDistanceZ;
    }

    private float getMagnitude(float x, float y, float z){
        return (float)Math.sqrt(x * x + y * y + z*z);
    }

    public void setHorizon(float horizon) {
        this.horizon = horizon;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public void setAltitude(float altitude){
        this.altitude = altitude;
    }

    public void setAnchorPoints(float[] anchorPoints) {
        this.anchorPoints = anchorPoints;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPhysicalZ(int i) {
        return physicalZ[i];
    }

    public float getScreenPositionX(int i) {
        return pixelX[i];
    }

    public float getScreenPositionY(int i) {
        return pixelY[i];
    }

    public float getPixelWidth(int i) {
        return pixelWidth[i];
    }

    public float getPhysicalWidth(int i){
        return physicalWidth[i];
    }

    public void setPhysicalWidth(float physicalWidth, int i) {
        this.physicalWidth[i] = physicalWidth;
    }

    public boolean isCreated (int i){
        return created[i];
    }

    public void setPhysicalWidth(float width){
        scale = width/100;
    }


}
