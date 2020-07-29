package vn.tiki.mab.simulation;

import vn.tiki.mab.algs.AbstractAlgs;
import vn.tiki.mab.distribution.BuyBehavior;
import vn.tiki.mab.distribution.CustomerContext;
import vn.tiki.mab.distribution.OfferContext;

import java.util.ArrayList;

public class Simulation {

    private AbstractAlgs learner;
    private ArrayList<OfferContext> offerContexts;
    private CustomerContext customerContext;
    private BuyBehavior buyBehavior;

    public void simulate(int T /** round */ , boolean showLog) {
        if (showLog) {
            // do something
        }

        ArrayList<Integer> rewards = new ArrayList<>();

        for (int i = 0; i < T; i++) {
            /** Get customer context */
            customerContext.sample();
            /** Get action */
            int action = learner.getAction(customerContext);
            /** Set reward */
            int reward = buyBehavior.sample(customerContext, offerContexts.get(action));
            learner.setReward(action, reward);
            rewards.add(reward);

            if (showLog) {
                // do something
            }
        }
    }

    private Simulation(SimulationBuilder builder) {
        this.offerContexts = builder.offerContexts;
        this.customerContext = builder.customerContext;
        this.buyBehavior = builder.buyBehavior;
        this.learner = builder.learner;
    }

    public static class SimulationBuilder {
        private ArrayList<OfferContext> offerContexts;
        private CustomerContext customerContext;
        private BuyBehavior buyBehavior;
        private AbstractAlgs learner;

        public SimulationBuilder setOfferContexts(ArrayList<OfferContext> offerContexts) {
            this.offerContexts = offerContexts;
            return this;
        }

        public SimulationBuilder setCustomerContext(CustomerContext customerContext) {
            this.customerContext = customerContext;
            return this;
        }

        public SimulationBuilder setBuyBehavior(BuyBehavior buyBehavior) {
            this.buyBehavior = buyBehavior;
            return this;
        }

        public SimulationBuilder setLearner(AbstractAlgs learner) {
            this.learner = learner;
            return this;
        }

        public Simulation build() {
            return new Simulation(this);
        }
    }
}
