package com.meum.classifier.decision;

import com.meum.classifier.DecisionNode;
import com.meum.classifier.Target;

import java.text.MessageFormat;
import java.util.*;

public class Threshold implements Decision {
    private final double threshold;
    private final int paramIndex;

    public Threshold(final int paramIndex, final double threshold) {
        this.threshold = threshold;
        this.paramIndex = paramIndex;
    }

    public Threshold(final Map<double[], Target> trainingData, final Random rng) {
        final Set<double[]> values = trainingData.keySet();
        int whichValue = rng.nextInt(values.size());
        final Iterator<double[]> iterator = values.iterator();
        double[] prices = iterator.next();
        for (int i = 1; i < whichValue; i++) {
            prices = iterator.next();
        }
        if (prices == null) {
            throw new IllegalStateException("The randomly chosen training set should exist");
        }
        paramIndex = rng.nextInt(prices.length);
        threshold = prices[paramIndex];
    }

    public int getSubTreeIndex(double[] programParameters) {
        return programParameters[paramIndex] > threshold ? DecisionNode.LEFT : DecisionNode.RIGHT;
    }

    @Override
    public int hashCode() {
        return paramIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Threshold) {
            Threshold t = (Threshold) obj;
            return t.paramIndex == this.paramIndex;
        }
        throw new IllegalStateException(
                MessageFormat.format("Wrong object passed for equality testing, expected: {0} got: {1}",
                        this.getClass().getName(), obj.getClass().getName()));
        
    }

    @Override
    public String toString() {
        return MessageFormat.format("arg{0}>{1}", paramIndex, threshold);
    }
}
