package com.meum.classifier.decision;

import com.meum.classifier.DecisionNode;
import com.meum.classifier.Target;

import java.text.MessageFormat;
import java.util.*;

public class ThresholdDecision implements Decision {
    private static final Map<Map<double[], Target>, Set<Integer>> USED_INDICIES = new HashMap<Map<double[],Target>, Set<Integer>>();

    private final double threshold;
    private final int paramIndex;

    public ThresholdDecision(final Map<double[], Target> trainingData, final Random rng) {
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
    public String toString() {
        return MessageFormat.format("arg{0}>{1}", paramIndex, threshold);
    }
}
