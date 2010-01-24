package com.meum.classifier.bias.fitness;

import com.meum.classifier.Fitness;
import com.meum.classifier.Target;
import com.meum.classifier.TreeNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnsembleModifier extends FitnessModifier {

    @Override
    public double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population) {
        final Fitness.EnsembleFitness ensembleFitness = getFitness().getEnsembleFitness(candidate, population);
        if (ensembleFitness.getEnsembleExcluded() > ensembleFitness.getEnsemble()) {
            return fitness * 1.2;
        } else {
            return fitness * 0.8;
        }
    }


}
