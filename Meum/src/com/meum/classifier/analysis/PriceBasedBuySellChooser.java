package com.meum.classifier.analysis;

import com.meum.classifier.Target;

import java.util.*;

public class PriceBasedBuySellChooser {
    private double[] testData;

    public PriceBasedBuySellChooser(double[] testData) {
        this.testData = testData;
    }

    public Map<double[], Target> getTrainingData(final int numPredictionSteps,
                                                 final double spread,
                                                 final int maxHoldTime) {

        final Map<double[], Target> trainingData = new HashMap<double[], Target>();
        int day = 0;
        for (int i = 0; i < testData.length; i++) {
            if (day == numPredictionSteps) {
                final double currentPrice = testData[i];
                final Target target = checkFuture(currentPrice, i, spread, maxHoldTime);
                trainingData.put(Arrays.copyOfRange(testData, i - numPredictionSteps, i), target);
                i -= numPredictionSteps;
                day = 0;

            } else {
                day++;
            }
        }
        return trainingData;
    }

    private Target checkFuture(double currentPrice, final int i, double spread, int maxHoldTime) {
        int day = 0;
        int iterator = i + 1;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        while (iterator < testData.length && day < maxHoldTime) {
            min = Math.min(min, testData[iterator]);
            max = Math.max(max, testData[iterator]);
            iterator++;
            day++;
        }
        if (day > 0) {
            double buyDifference = max - currentPrice - spread;
            double sellDifference = currentPrice - spread - min;
            if (buyDifference > sellDifference && buyDifference > 0) {
                return Target.BUY;
            }
            else if (sellDifference > 0) {
                return Target.SELL;
            }

        }
        return Target.CANT_TOUCH_THIS;
    }

}
