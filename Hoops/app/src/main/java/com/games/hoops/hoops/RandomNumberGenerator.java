package com.games.hoops.hoops;

import java.util.Random;

/**
 * Created by domin on 11 Jun 2016.
 */
public class RandomNumberGenerator {
    private static Random rand = new Random();

    public static int getRandIntBetween(int lowerBound, int upperBound) {
        return rand.nextInt(upperBound - lowerBound) + lowerBound;
    }

    public static int getRandInt(int upperBound) {
        return rand.nextInt(upperBound);
    }

    public static float getRandFloatBetween(float lowerBound, float upperBound) {
        return rand.nextFloat()*(upperBound - lowerBound) + lowerBound;
    }

    public static float getRandFloat(float upperBound) {
        return rand.nextFloat()*(upperBound);
    }
}

