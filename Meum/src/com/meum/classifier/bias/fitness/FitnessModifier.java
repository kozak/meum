package com.meum.classifier.bias.fitness;

import com.meum.classifier.Fitness;
import com.meum.classifier.TreeNode;
import com.meum.classifier.bias.BiasModifier;

import java.util.List;

public abstract class FitnessModifier extends BiasModifier {
    private Fitness fitness;

    public Fitness getFitness() {
        return fitness;
    }

    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
    }

    public abstract double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population);
}
