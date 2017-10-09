package animation;

/**
 * Created by domin on 15 Jul 2016.
 */
public class AnimateValue {
    private static final int TYPE_LINEAR = 0;
    private static final int TYPE_SIN_WAVE = 2;
    private static final int TYPE_QUADRATIC = 3;
    private static final int TYPE_EXPONENTIAL = 4;

    private static final float PI = (float) Math.PI;

    private float[] values, value1, value2;
    private float[] timing, value3, instanceList, speed;
    private int[] animationType;
    private boolean[] looping, isActive, isPaused, reversed;
    private int max = 30;
    private int quantity = 0;
    private int instances = 0;

    public AnimateValue(){
        values = new float[max];
        value1 = new float[max];
        value2 = new float[max];

        speed = new float[max];

        timing = new float[max];
        value3 = new float[max];

        animationType = new int[max];

        looping = new boolean[max];
        isActive = new boolean[max];
        isPaused = new boolean[max];
        reversed = new boolean[max];

        for (int i = 0; i < max; i++) {
            looping[i] = true;
            isActive[i] = true;
            isPaused[i] = false;
            reversed[i] = false;
            speed[i] = 1;
        }

        instanceList = new float[100];
    }
    public AnimateValue(int maxNumber){
        max = maxNumber;
        values = new float[max];
        timing = new float[max];
    }

    public int addLinearAnimation(float initialValue, float finalValue, float time){
        this.value1[quantity] = initialValue;
        this.value2[quantity] = finalValue;
        values[quantity] = initialValue;

        value3[quantity] = time;
        timing[quantity] = 0;

        isActive[quantity] = true;
        animationType[quantity] = TYPE_LINEAR;

        quantity++;
        return quantity-1;

    }

    //top(t) = Asin(2πft + φ)
    //frequency -> value3
    //amplitude -> value2
    //phase -> value1
    public int addSineWaveAnimation(float frequency, float amplitude, float phase){
        this.value1[quantity] = phase;
        this.value2[quantity] = amplitude;

        value3[quantity] = frequency;
        values[quantity] = (float) (amplitude*Math.sin(2*Math.PI*frequency*0 + phase));
        timing[quantity] = 0;

        isActive[quantity] = true;
        animationType[quantity] = TYPE_SIN_WAVE;

        quantity++;
        return quantity-1;
    }

    public int addExponentialAnimation(float rate, float start){
        this.value1[quantity] = start;
        this.value2[quantity] = rate;

        values[quantity] = (float) (start*Math.pow(rate, 0));

        isActive[quantity] = true;
        animationType[quantity] = TYPE_EXPONENTIAL;

        quantity++;
        return quantity-1;
    }

    public void start(int i){
//        values[i] = value1[i];
        timing[i] = 0;
        isActive[i] = true;
    }

    public void playWhenReady(int i){
        if (!isPlaying(i)) {
            values[i] = value1[i];
            timing[i] = 0;
            isActive[i] = true;
        }
    }

    public void reset(int i){
        values[i] = value1[i];
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
            values[iD] = value1[iD];
            timing[iD] = 0;
            isActive[iD] = true;
            instances++;
        }
    }

    public boolean isPlaying(int i){
        return isActive[i] && !isPaused[i];
    }

    public void stop(int i){
        isActive[i] = false;
    }

    public void pause(int i) {
        isPaused[i] = true;
    }

    public void play(int i){
        isPaused[i] = false;
    }

    public void forward(int i){
        reversed[i] = false;
    }

    public void reverse(int i){
        reversed[i] = true;
    }

    public void setSpeed(float speed, int i){
        this.speed[i] = speed;
    }

    public void update (float delta){
        for (int i = 0; i < quantity; i++) {
            if (isActive[i]) switch (animationType[i]) {
                case TYPE_LINEAR:updateLinear(isPaused[i] ? 0 : speed[i]*delta, i);
                    break;
                case TYPE_QUADRATIC:
                    break;
                case TYPE_SIN_WAVE:updateSineWave(isPaused[i] ? 0 : speed[i]*delta, i);
                    break;
                case TYPE_EXPONENTIAL:updateExponential(isPaused[i] ? 0 : speed[i]*delta, i);
                    break;
            }
        }
    }

    private void updateLinear(float delta, int i){
        if (timing[i] < value3[i] - delta && !reversed[i])
            timing[i] += delta;
        else if (timing[i] > 0 + delta && reversed[i])
            timing[i] -= delta;
        else if (looping[i]) {
            if (!reversed[i]) {
                timing[i] = 0;
                values[i] = value1[i];
            } else {
                timing[i] = value3[i];
                values[i] = value1[i] + (value2[i] - value1[i]) * (timing[i] / value3[i]);
            }
        } else {
            if(!reversed[i])
                timing[i] = value3[i];
            else
                timing[i] = 0;
            values[i] = value1[i] + (value2[i] - value1[i]) * (timing[i] / value3[i]);
//            timing[i] = value3[i];
//            isPaused[i] = true;
        }

        values[i] = value1[i] + (value2[i] - value1[i]) * (timing[i] / value3[i]);
    }

    public void updateQuadratic(float delta, int i){
        //TODO
    }


    private void updateSineWave(float delta, int i){
        //top(t) = Asin(2πft + φ)
        float f = value3[i], A = value2[i], p = value1[i];

        if (!reversed[i]) timing[i] = (timing[i] + delta) % (1.0f / f);
        else timing[i] = (timing[i] - delta) % (1.0f / f);
        values[i] = (float) (A * Math.sin(2 * PI * f * timing[i] + p));
    }

    private void updateExponential(float delta, int i){
        float start = value1[i], rate = value2[i];
        if (!reversed[i]) timing[i] += delta;
        else if (timing[i] >=0) timing[i] -= delta;
        values[i] = (float) (start*Math.pow(rate, -timing[i]));
    }

    public float getValue(int i){
        return values[i];
    }

    public void setLooping(boolean looping, int i){
        this.looping[i] = looping;
    }
}
