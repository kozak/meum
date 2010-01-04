package com.meum.classifier;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.Map;

public class Fitness implements FitnessEvaluator<TreeNode> {
    private final Map<double[], Target> data;

    public Fitness(Map<double[], Target> data) {
        this.data = data;
    }

    public double getFitness(TreeNode candidate, List<? extends TreeNode> population) {
        double error = 0;
        for (Map.Entry<double[], Target> entry : data.entrySet()) {
            Target targetClass = candidate.evaluate(entry.getKey());
            error += (targetClass == entry.getValue() ? 0 : 1);
        }
        return error;
    }

    public boolean isNatural() {
        return false;
    }
}
