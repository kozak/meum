package com.meum.classifier;

public enum Target {
    BUY,SELL;

    public Target getOpposite() {
        if (this == BUY) {
            return SELL;
        }
        return BUY;
    }
}
