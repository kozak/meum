package com.meum.classifier.bias.fitness;

import com.meum.classifier.Fitness;
import com.meum.classifier.Target;
import com.meum.classifier.TreeNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnsembleModifier extends FitnessModifier {
    private double negativeContribution;
    private double noChange;
    private double positiveContribution;

    public EnsembleModifier(double negativeContribution, double noChange, double positiveContribution) {
        this.negativeContribution = negativeContribution;
        this.noChange = noChange;
        this.positiveContribution = positiveContribution;
    }

    @Override
    public double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population) {
        final Fitness.EnsembleFitness ensembleFitness = getFitness().getEnsembleFitness(candidate, population);
        final double excluded = ensembleFitness.getEnsembleExcluded();
        final double ensemble = ensembleFitness.getEnsemble();

        if (excluded > ensemble) {
            return fitness * negativeContribution;
        } else if (excluded == ensemble) {
            return fitness * noChange;
        } else {
            return fitness * positiveContribution;
        }
    }

    @Override
    public String toString() {
        return "EnsembleModifier {" +
                "negativeContribution=" + negativeContribution +
                ", noChange=" + noChange +
                ", positiveContribution=" + positiveContribution +
                '}';
    }
}
