package com.meum.classifier;

import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;

import java.util.List;
import java.util.Map;

public class Fitness implements FitnessEvaluator<TreeNode>, EvolutionObserver<TreeNode> {
    private final Map<double[], Target> data;
    private PopulationData<? extends TreeNode> populationData;
    private final int numGenerations;
    private final double depthWeight;

    public Fitness(final Map<double[], Target> data,
                   final int numGenerations,
                   final double depthWeight) {
        this.data = data;
        this.numGenerations = numGenerations;
        this.depthWeight = depthWeight;
    }

    public double getFitness(final TreeNode candidate, final List<? extends TreeNode> population) {
        double error = 0;
        for (Map.Entry<double[], Target> entry : data.entrySet()) {
            Target targetClass = candidate.evaluate(entry.getKey());
            error += (targetClass == entry.getValue() ? 0 : 1);
        }
        if (populationData != null) {
            final int number = populationData.getGenerationNumber();
            if (number > (numGenerations / 2)) {
                error += candidate.getDepth() * depthWeight ;
            }
        }
        return error;
    }

    public boolean isNatural() {
        return false;
    }

    public void populationUpdate(PopulationData<? extends TreeNode> populationData) {
        this.populationData = populationData;
    }

    @Override
    public String toString() {
        return "Fitness {" +
                "\n The number of incorrectly classified instances."
                +"\n}";
    }
}
