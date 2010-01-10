package com.meum.classifier;

public enum Target {
    BUY,SELL, CANT_TOUCH_THIS;

    public Target getOpposite() {
        if (this == BUY) {
            return SELL;
        }
        else if (this == SELL) {
            return BUY;
        }
        return CANT_TOUCH_THIS;
    }
}
