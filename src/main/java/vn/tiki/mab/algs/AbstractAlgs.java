package vn.tiki.mab.algs;

import vn.tiki.mab.distribution.CustomerContext;

public abstract class AbstractAlgs {
    abstract public int getAction(CustomerContext customerContext);
    abstract public void setReward(int action, int reward);
    abstract public void showLog();
}
