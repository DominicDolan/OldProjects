package animation;

import statics.Loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 29 Jul 2016.
 */
public class FramesAnimation {
    private int[] frameIDs;
    private double[] frameDurations;
    private double[] frameEndTimes;
    private int currentFrameIndex = 0;
    private double totalDuration = 0;
    private double currentTime = 0;
    private boolean paused;

    public FramesAnimation(String name, double frameDuration) {
        List<Integer> frameIDList = new ArrayList<>();
        frameIDs = new int[10];

        int i = 1;
        String[] parts = name.split("~");
        name = parts[0];
        String suffix  = parts.length > 1 ? parts[1]: "";
        String fileName = "frames/" + name + "/" + name + "-1" + suffix;
        while (Loader.doesFileExist(fileName + ".png")){

            frameIDList.add(Loader.loadTexture(fileName));

            i++;
            fileName = "frames/" + name + "/" + name + "-" + i + suffix;
        }

        try {
             if (i==1) throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int frameCount = frameIDList.size();
        frameIDs = new int[frameCount];
        frameEndTimes = new double[frameCount];
        frameDurations = new double[frameCount];

        for (int j = 0; j < frameCount; j++) {
            frameIDs[j] = frameIDList.get(j);
            frameDurations[j] = frameDuration;
            totalDuration += frameDuration;
            frameEndTimes[j] = totalDuration;
        }


    }

    public void setSpeed(float scale){
        for (int i = 0; i < frameIDs.length; i++) {
            frameDurations[i] = (frameDurations[i]*scale);
            totalDuration += frameDurations[i];
            frameEndTimes[i] = totalDuration;
        }
    }

    public void pauseForFrame(){
        paused = true;
    }

    public synchronized void update(float increment) {
        if(!paused) {
            currentTime += increment;
        } else
            paused = false;

        if (currentTime > totalDuration) {
            wrapAnimation();
        }
        while (currentTime > frameEndTimes[currentFrameIndex]) {
            currentFrameIndex++;
        }
    }

    private synchronized void wrapAnimation() {
        currentFrameIndex = 0;
        currentTime %= totalDuration;
    }

    public synchronized void start(){
        wrapAnimation();
    }

    public int getCurrentFrameID(){
        return frameIDs[currentFrameIndex];
    }

}
