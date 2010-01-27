package com.meum.classifier.utils;

import com.meum.classifier.bias.fitness.FitnessModifier;
import com.meum.classifier.bias.mutation.MutationModifier;

public class TestResult {
    private String marketName;
    private String imageName;
    private MutationModifier mutationModifier;
    private FitnessModifier fitnessModifier;
    private double baseFitness;
    private double fitness;
    private int treeHeight;

    public TestResult(String marketName,
                      String imageName,
                      double baseFitness, double fitness, int treeHeight,
                      MutationModifier mutationModifier,
                      FitnessModifier fitnessModifier) {
        this.marketName = marketName;
        this.mutationModifier = mutationModifier;
        this.fitnessModifier = fitnessModifier;
        this.baseFitness = baseFitness;
        this.fitness = fitness;
        this.treeHeight = treeHeight;
        this.imageName = imageName;
    }

    public double getBaseFitness() {
        return baseFitness;
    }

    public double getFitness() {
        return fitness;
    }

    public int getTreeHeight() {
        return treeHeight;
    }

    public String getMarketName() {
        return marketName;
    }

    public MutationModifier getMutationModifier() {
        return mutationModifier;
    }

    public FitnessModifier getFitnessModifier() {
        return fitnessModifier;
    }

    public String getImageName() {
        return imageName;
    }
}
