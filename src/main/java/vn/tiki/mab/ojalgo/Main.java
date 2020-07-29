package vn.tiki.mab.ojalgo;

import org.ojalgo.matrix.Primitive64Matrix;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.Primitive64Store;
import org.ojalgo.random.Normal;
import org.ojalgo.random.Uniform;

public class Main {

    private static final int DIM = 3;
    public static final double[][] arrayA = {
            {0.67},
            {0.53},
            {1.0},
            {0.0}
    };

    public static void main(String[] args) {
//        PhysicalStore.Factory<Double, Primitive64Store> storeFactory = Primitive64Store.FACTORY;
//        Primitive64Store mtrxA, mtrxB;
//        Primitive64Store result = storeFactory.make(3, 3);
//
//        mtrxA = Primitive64Store.FACTORY.rows(arrayA);
//        mtrxB = Primitive64Store.FACTORY.rows(arrayB);
//        mtrxA.multiply(mtrxB, result);
//        System.out.println(result.toString());

        double alpha = 2;
        Primitive64Matrix currentContext = Primitive64Matrix.FACTORY.rows(arrayA);
        Primitive64Matrix b = Primitive64Matrix.FACTORY.make(4, 1);
        Primitive64Matrix precisionMatrix = Primitive64Matrix.FACTORY.makeIdentity(4);
        Primitive64Matrix invertedMatrix = precisionMatrix.invert();
        Primitive64Matrix weights = invertedMatrix.multiply(b);
        Primitive64Matrix predVar = currentContext.transpose().multiply(invertedMatrix).multiply(currentContext);
        Primitive64Matrix predMean = weights.transpose().multiply(currentContext);
        double dbPredvar = predVar.get(0, 0);
        double dbPredMean = predMean.get(0, 0);
        double ub = dbPredMean + alpha * Math.sqrt(dbPredvar);
    }
}
