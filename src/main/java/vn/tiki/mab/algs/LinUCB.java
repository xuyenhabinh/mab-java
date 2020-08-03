package vn.tiki.mab.algs;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PlotImpl;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import com.github.sh0nk.matplotlib4j.builder.PlotBuilder;
import org.ojalgo.matrix.Primitive64Matrix;
import org.ojalgo.matrix.store.MatrixStore;
import vn.tiki.mab.distribution.CustomerContext;
import vn.tiki.mab.distribution.OfferContext;
import vn.tiki.mab.utils.number.NumberUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LinUCB extends AbstractAlgs {

    static final int CURRENT_CONTEXT_LEN = 4;

    ArrayList<OfferContext> offerContexts;
    int K;
    /**
     * Number of context: K arms
     */
    int[] rewardSum;
    int[] actionChoice;
    int roundNumber = 0;
    /**
     * Parameters
     */
    Primitive64Matrix[] precisionMatrix;
    Primitive64Matrix[] weights;
    Primitive64Matrix[] b; /** TODO: what does 'b' mean? */
    Primitive64Matrix[] currentContext;
    double[][] currContextArray;
    /**
     * Logging
     */
    List<Double>[] logUpperBound;
    List<Double>[] logPredMean;
    List<Double>[] logPredVar;
    List<Double>[] logRewardSum;

    public LinUCB(ArrayList<OfferContext> offerContexts) {
        this.offerContexts = offerContexts;
        this.K = offerContexts.size();

        rewardSum = new int[K];
        actionChoice = new int[K];

        precisionMatrix = new Primitive64Matrix[K];
        weights = new Primitive64Matrix[K];
        b = new Primitive64Matrix[K];
        currentContext = new Primitive64Matrix[K];
        currContextArray = new double[1][CURRENT_CONTEXT_LEN];

        logUpperBound = new ArrayList[K];
        logPredMean = new ArrayList[K];
        logPredVar = new ArrayList[K];
        logRewardSum = new ArrayList[K];

        /** Init value */
        for (int k = 0; k < K; k++) {
            precisionMatrix[k] = Primitive64Matrix.FACTORY.makeIdentity(CURRENT_CONTEXT_LEN);
            b[k] = Primitive64Matrix.FACTORY.make(CURRENT_CONTEXT_LEN, 1);

            logUpperBound[k] = new ArrayList<>();
            logPredMean[k] = new ArrayList<>();
            logPredVar[k] = new ArrayList<>();
            logRewardSum[k] = new ArrayList<>();
        }
    }

    public int getAction(CustomerContext customerContext) {
        int bestAction = -1;
        double alpha = 1 + 1 / (roundNumber + 1);
        double[] upperBound = new double[K];
        double maxUpperBound = 0.0;
        ArrayList<Integer> candidates = new ArrayList<>();

        Primitive64Matrix[] predVar = new Primitive64Matrix[K];
        Primitive64Matrix[] predMean = new Primitive64Matrix[K];

        /** Loop over K arms */
        for (int k = 0; k < K; k++) {
            int tempLen = customerContext.toArray().length;
            System.arraycopy(customerContext.toArray(), 0, currContextArray[0],
                    0, tempLen);
            System.arraycopy(offerContexts.get(k).toArray(), 0, currContextArray[0],
                    tempLen, offerContexts.get(k).toArray().length);
            currentContext[k] = Primitive64Matrix.FACTORY.columns(currContextArray);

            Primitive64Matrix invertedMatrix = precisionMatrix[k].invert();
            weights[k] = invertedMatrix.multiply(b[k]);
            predVar[k] = currentContext[k].transpose().multiply(invertedMatrix).multiply(currentContext[k]);
            predMean[k] = weights[k].transpose().multiply(currentContext[k]);
            upperBound[k] = predMean[k].get(0, 0) + alpha * Math.sqrt(predVar[k].get(0, 0));
        }

        /** Select the random maximal action (if more than one) */
        maxUpperBound = Arrays.stream(upperBound).max().getAsDouble();
        for (int i = 0; i < K; i++) {
            if (maxUpperBound <= upperBound[i])
                candidates.add(i);
        }

        int randomIdx = NumberUtils.nextInt(candidates.size());
        bestAction = candidates.get(randomIdx);

        actionChoice[bestAction]++;
        roundNumber++;

        /** Update parameters */

        for (int k = 0; k < K; k++) {
            logUpperBound[k].add(upperBound[k]);
            logPredMean[k].add(predMean[k].get(0, 0));
            logPredVar[k].add(predVar[k].get(0, 0));
            logRewardSum[k].add(Double.valueOf(rewardSum[k]));
        }

        return bestAction;
    }

    public void setReward(int action, int reward) {
        rewardSum[action] += reward;
        b[action] = b[action].add(currentContext[action].multiply(reward));

        precisionMatrix[action] = precisionMatrix[action].add(currentContext[action].multiply(currentContext[action].transpose()));
    }

    /**
     * Visualize LinUCB logs
     * */
    public void showLog() {
        /** Visualize */
        Plot plt = Plot.create();

        /** Predictive mean */
        for (int k = 0; k < K; k++) {
            plt.plot()
                    .add(logPredMean[k])
                    .label(offerContexts.get(k).toString())
                    .linestyle("-");
        }
        plt.xlabel("Iteration");
        plt.ylabel("Predictive mean");
        plt.legend();
        try {
            plt.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PythonExecutionException e) {
            e.printStackTrace();
        }

        /** Predictive variance */
        for (int k = 0; k < K; k++) {
            plt.plot()
                    .add(logPredVar[k])
                    .label(offerContexts.get(k).toString())
                    .linestyle("-");
        }
        plt.xlabel("Iteration");
        plt.ylabel("Predictive variance");
        plt.legend();
        try {
            plt.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PythonExecutionException e) {
            e.printStackTrace();
        }

        /** Cumulative reward */
        for (int k = 0; k < K; k++) {
            plt.plot()
                    .add(logRewardSum[k])
                    .label(offerContexts.get(k).toString())
                    .linestyle("-");
        }
        plt.xlabel("Iteration");
        plt.ylabel("Cumulative sum");
        plt.legend();
        try {
            plt.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PythonExecutionException e) {
            e.printStackTrace();
        }
    }
}
