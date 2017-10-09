package com.games.hoops.hoops;

/**
 * Created by domin on 15 Jul 2016.
 */
public class AnimateValue {
    private static final int TYPE_LINEAR = 0;
    private static final int TYPE_SIN_WAVE = 2;
    private static final int TYPE_QUADRATIC = 3;

    private static final float PI = (float) Math.PI;

    private float[] values, initialValue, finalValue;
    private float[] timing, finalTime, instanceList;
    private int[] animationType;
    private boolean[] looping, isActive;
    private int max = 30;
    private int quantity = 0;
    private int instances = 0;

    public AnimateValue(){
        values = new float[max];
        initialValue = new float[max];
        finalValue = new float[max];

        timing = new float[max];
        finalTime = new float[max];

        animationType = new int[max];

        looping = new boolean[max];
        isActive = new boolean[max];

        for (int i = 0; i < max; i++) {
            looping[i] = true;
            isActive[i] = true;
        }

        instanceList = new float[100];
    }
    public AnimateValue(int maxNumber){
        max = maxNumber;
        values = new float[max];
        timing = new float[max];
    }

    public int addLinearAnimation(float initialValue, float finalValue, float time){
        this.initialValue[quantity] = initialValue;
        this.finalValue[quantity] = finalValue;
        values[quantity] = initialValue;

        finalTime[quantity] = time;
        timing[quantity] = 0;

        isActive[quantity] = true;
        animationType[quantity] = TYPE_LINEAR;

        quantity++;
        return quantity-1;

    }

    //y(t) = Asin(2πft + φ)
    //frequency -> finalTime
    //amplitude -> finalValue
    //phase -> initialValue
    public int addSineWaveAnimation(float frequency, float amplitude, float phase){
        this.initialValue[quantity] = phase;
        this.finalValue[quantity] = amplitude;

        finalTime[quantity] = frequency;
        values[quantity] = (float) (amplitude*Math.sin(2*Math.PI*frequency*0 + phase));
        timing[quantity] = 0;

        isActive[quantity] = true;
        animationType[quantity] = TYPE_SIN_WAVE;

        quantity++;
        return quantity-1;
    }

    public void play(int i){
        values[i] = initialValue[i];
        timing[i] = 0;
        isActive[i] = true;
    }

    public void reset(int i){
        values[i] = initialValue[i];
        timing[i] = 0;
    }

    public void playOnce (int iD, int instance){
        float instanceValue = Float.valueOf("" + String.valueOf(iD) + "." + String.valueOf(instance));
        boolean alreadyUsed = false;
        for (int i = 0; i < instances; i++) {
            if (instanceList[i]== instanceValue){
                alreadyUsed = true;
                break;
            }
            else {
                alreadyUsed = false;
            }
        }
        if (!alreadyUsed) {
            instanceList[instances] = instanceValue;
            values[iD] = initialValue[iD];
            timing[iD] = 0;
            isActive[iD] = true;
            instances++;
        }
    }

    public boolean isPlaying(int i){
        return isActive[i];
    }

    public void stop(int i){
        isActive[i] = false;
    }

    public void update (float delta){
        for (int i = 0; i < quantity; i++) {
            if (isActive[i]) {
                switch (animationType[i]) {
                    case TYPE_LINEAR:
                        if (timing[i] < finalTime[i] - delta)
                            timing[i] += delta;
                        else if (looping[i]) {
                            timing[i] = 0;
                            values[i] = initialValue[i];
                        } else {
                            timing[i] = finalTime[i];
                            isActive[i] = false;
                        }

                        values[i] = initialValue[i] + (finalValue[i] - initialValue[i]) * (timing[i] / finalTime[i]);

                        break;
                    case TYPE_QUADRATIC:

                        break;
                    case TYPE_SIN_WAVE:
                        //y(t) = Asin(2πft + φ)
                        float f = finalTime[i], A = finalValue[i], p = initialValue[i];
                        timing[i] = (timing[i] + delta) % (1.0f / f);

                        values[i] = (float) (A * Math.sin(2 * PI * f * timing[i] + p));
                        break;
                }
            }
        }
    }

    public float getValue(int i){
        return values[i];
    }

    public void setLooping(boolean looping, int i){
        this.looping[i] = looping;
    }
}
