package vn.tiki.mab.distribution;

import vn.tiki.mab.utils.number.NumberUtils;

public class OfferContext extends AbstractContext {

    private int bestPrice;
    private int bestPDD;

    public OfferContext(int bestPrice, int bestPDD) {
        this.bestPrice = bestPrice;
        this.bestPDD = bestPDD;
    }

    @Override
    public void sample() {
        bestPrice = NumberUtils.nextUniform() >= 0.5 ? 1 : 0;
        bestPDD = NumberUtils.nextUniform() >= 0.5 ? 1 : 0;
    }

    @Override
    public double[] toArray() {
        double[] ret = {bestPrice, bestPDD};
        return ret;
    }

    @Override
    public String toString() {
        return String.format("bestPrice: %d, bestPDD: %d", bestPrice, bestPDD);
    }

    public int getBestPrice() {
        return bestPrice;
    }

    public int getBestPDD() {
        return bestPDD;
    }
}
