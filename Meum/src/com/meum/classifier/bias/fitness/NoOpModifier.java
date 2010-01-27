package com.meum.classifier.bias.fitness;

import com.meum.classifier.TreeNode;

import java.util.List;

public class NoOpModifier extends FitnessModifier {
    @Override
    public double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population) {
        return fitness;
    }

    @Override
    public String toString() {
        return "NoOpModifier";
    }
}
