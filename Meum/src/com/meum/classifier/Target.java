package com.meum.classifier;

public enum Target {
    BUY, SELL, EVIL;

    public Target getOpposite() {
        if (this == BUY) {
            return SELL;
        }
        else if (this == SELL) {
            return BUY;
        }
        return EVIL;
    }
}
