package com.meum.classifier.bias.fitness;

import com.meum.classifier.TreeNode;
import com.meum.classifier.bias.BiasModifier;

import java.util.List;

public abstract class FitnessModifier extends BiasModifier {

    public abstract double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population);
}
