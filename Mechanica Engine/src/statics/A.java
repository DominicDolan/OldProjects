package statics;

import animation.AnimateValue;

/**
 * Created by domin on 30 Mar 2017.
 */

public class A {
    private static AnimateValue a;
    private static int sinValue, sin2Seconds, line, exponential, boing;

    public static void init(){
        a = new AnimateValue();
        sinValue = addSineWaveAnimation(1,1,0);
        sin2Seconds = addSineWaveAnimation(0.5f,1,0);
        line = addLinearAnimation(0,1,1);
    }

    public static int addLinearAnimation(float initialValue, float finalValue, float time){
        return a.addLinearAnimation(initialValue,finalValue,time);
    }

    public static int addSineWaveAnimation(float frequency, float amplitude, float phase){
        return a.addSineWaveAnimation(frequency,amplitude,phase);
    }

    public static float getSineValue(){
        return a.getValue(sinValue);
    }

    public static float getLinearValue(){
        return a.getValue(line);
    }

    public static float getSine2Seconds(){
        return a.getValue(sin2Seconds);
    }

    public static void playLinear(){
        a.playWhenReady(line);
    }

    public static void update(float delta){
        a.update(delta);
    }
}
