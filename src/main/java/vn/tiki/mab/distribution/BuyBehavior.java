package vn.tiki.mab.distribution;

import vn.tiki.mab.utils.number.NumberUtils;

public class BuyBehavior {

    public int sample(CustomerContext customerContext, OfferContext offerContext) {
        double value = customerContext.getCheap() * offerContext.getBestPrice() +
                customerContext.getFast() * offerContext.getBestPDD();

        return NumberUtils.nextUniform() < 0.5 * value ? 1 : 0;
    }
}
