package com.meum.classifier.decision;

import com.meum.classifier.Target;

import java.util.*;

public class DecisionFactory {
    private Map<double[], Target> trainingData;

    public DecisionFactory(final Map<double[], Target> trainingData) {
        this.trainingData = trainingData;
    }

    public Decision getDecision(final Random rng) {
        int decisionType = rng.nextInt(1);
        Decision decision;
        switch (decisionType) {
            case 0:
                decision = new Threshold(trainingData, rng);
                break;
            case 1:
                decision = new MeanValue(trainingData, rng);
                break;
            default:
                decision = new Threshold(trainingData, rng);
        }
        return decision;
    }
}
