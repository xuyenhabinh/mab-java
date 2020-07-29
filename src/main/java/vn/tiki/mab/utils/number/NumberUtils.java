package vn.tiki.mab.utils.number;

import java.util.Random;

public class NumberUtils {

    private static Random random = new Random();

    public static double clip(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static double nextGaussian() {
        return random.nextGaussian();
    }

    public static double nextGaussian(double mean, double variance) {
        return mean + random.nextGaussian() * variance;
    }

    public static double nextUniform() {
        return random.nextDouble();
    }

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
