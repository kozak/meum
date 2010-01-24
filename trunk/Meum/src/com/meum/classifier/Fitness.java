package com.meum.classifier;

import com.meum.classifier.bias.fitness.ConstModifier;
import com.meum.classifier.bias.fitness.FitnessModifier;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;

import java.util.List;
import java.util.Map;

public class Fitness implements FitnessEvaluator<TreeNode> {
    private final Map<double[], Target> data;
    private final FitnessModifier fitnessModifier;

    public Fitness(final Map<double[], Target> data,
                   final FitnessModifier fitnessModifier) {
        this.data = data;
        this.fitnessModifier = fitnessModifier;
    }
    public Fitness(final Map<double[], Target> data) {
        this.data = data;
        this.fitnessModifier = new ConstModifier();
    }

    public double getFitness(final TreeNode candidate, final List<? extends TreeNode> population) {
        double error = 0;
        for (Map.Entry<double[], Target> entry : data.entrySet()) {
            Target targetClass = candidate.evaluate(entry.getKey());
            error += (targetClass == entry.getValue() ? 0 : 1);
        }
        error = fitnessModifier.adjustFitness(error, candidate, population);
        return error;
    }

    public boolean isNatural() {
        return false;
    }


    @Override
    public String toString() {
        return "The number of incorrectly classified instances.";
    }
}
