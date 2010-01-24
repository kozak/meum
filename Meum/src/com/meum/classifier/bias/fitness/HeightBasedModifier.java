package com.meum.classifier.bias.fitness;

import com.meum.classifier.TreeNode;

import java.util.List;

public class HeightBasedModifier extends FitnessModifier {

    @Override
    public double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population) {
        if (populationData != null) {
            return fitness + Math.abs(Math.sin(populationData.getGenerationNumber())) ;
        }
        return fitness;
    }
}
