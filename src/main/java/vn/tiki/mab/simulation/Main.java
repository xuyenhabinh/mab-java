package vn.tiki.mab.simulation;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import vn.tiki.mab.algs.AbstractAlgs;
import vn.tiki.mab.algs.LinUCB;
import vn.tiki.mab.distribution.BuyBehavior;
import vn.tiki.mab.distribution.CustomerContext;
import vn.tiki.mab.distribution.OfferContext;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static ArrayList<OfferContext> offerContexts = new ArrayList<>();

    private static ArrayList<Integer> simulate(AbstractAlgs _learner, CustomerContext customerContext, BuyBehavior buyBehavior, int T /** round */, boolean showLog) {
        AbstractAlgs learner = _learner;
        ArrayList<Integer> rewards = new ArrayList<>();
        for (int t = 0; t < T; t++) {
            customerContext.sample();
            var action = learner.getAction(customerContext);
            var reward = buyBehavior.sample(customerContext, offerContexts.get(action));
            learner.setReward(action, reward);
            rewards.add(reward);

            if (showLog) {
                System.out.println(String.format("%d %25s %20s %10d",
                        t + 1, customerContext.toString(), offerContexts.get(action).toString(), reward));
            }
        }

        return rewards;
    }

    public static void main(String[] args) {
        /** Offer settings */

        offerContexts.add(new OfferContext(0, 1));
        offerContexts.add(new OfferContext(0, 0));
        offerContexts.add(new OfferContext(1, 0));

        /** Simulation */
        AbstractAlgs linUCB = new LinUCB(offerContexts);
        CustomerContext customerContext = new CustomerContext();
        BuyBehavior buyBehavior = new BuyBehavior();

        /***/
        AbstractList<Integer> rewards = simulate(linUCB, customerContext, buyBehavior, 2000, true);
        linUCB.showLog();

        /** Visualize */
//        AtomicInteger ai = new AtomicInteger();
//        List<Integer> collect = rewards.stream()
//                .map(ai::addAndGet)
//                .collect(Collectors.toList());
//
//        Plot plt = Plot.create();
//        plt.plot()
//                .add(collect)
//                .label("Simulation")
//                .linestyle("-");
//        plt.xlabel("Iteration");
//        plt.ylabel("Cumulative reward");
//        plt.text(0.5, 0.2, "Simulation");
//        plt.title("Simulation");
//        plt.legend();
//        try {
//            plt.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (PythonExecutionException e) {
//            e.printStackTrace();
//        }
    }
}
