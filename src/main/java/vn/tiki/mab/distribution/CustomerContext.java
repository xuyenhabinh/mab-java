package vn.tiki.mab.distribution;

import vn.tiki.mab.utils.number.NumberUtils;

public class CustomerContext extends AbstractContext {

    private double cheap;
    private double fast;

    @Override
    public void sample() {
        double stochasticCheap = 0.1 * NumberUtils.nextGaussian() + 0.7;
        cheap = NumberUtils.clip(stochasticCheap, 0.0, 1.0);
        double stochasticFast = 0.1 * NumberUtils.nextGaussian() + 0.6;
        fast = NumberUtils.clip(stochasticFast, 0.0, 1.0);
    }

    @Override
    public double[] toArray() {
        double[] ret = {cheap, fast};
        return ret;
    }

    @Override
    public String toString() {
        return String.format("cheap: %.2f, fast %.2f", cheap, fast);
    }

    public double getCheap() {
        return cheap;
    }

    public double getFast() {
        return fast;
    }
}
