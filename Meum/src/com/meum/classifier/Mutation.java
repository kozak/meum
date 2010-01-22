package com.meum.classifier;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.PopulationData;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mutation implements EvolutionaryOperator<TreeNode>, EvolutionObserver<TreeNode> {
    private final Probability mutationProbability;
    private final DecisionTreeFactory factory;
    private PopulationData<? extends TreeNode> populationData;
    private Range[] ranges;

    public Mutation(Probability mutationProbability,
                    DecisionTreeFactory factory,
                    Range ... ranges) {
        this.mutationProbability = mutationProbability;
        this.factory = factory;
        this.ranges = ranges;
    }

    public List<TreeNode> apply(List<TreeNode> selectedCandidates, Random rng) {
        List<TreeNode> mutated = new ArrayList<TreeNode>(selectedCandidates.size());
        for (TreeNode selectedCandidate : selectedCandidates) {
            mutated.add(getMutatedCandidate(rng, selectedCandidate));
        }
        return mutated;
    }

    public void populationUpdate(PopulationData<? extends TreeNode> populationData) {
        this.populationData = populationData;
    }


    private TreeNode getMutatedCandidate(Random rng, TreeNode selectedCandidate) {
        if (populationData != null) {
            final int numNodes = selectedCandidate.countNodes();
            for (Range range : ranges) {
                if (range.inRange(populationData)) {
                    final int increment = range.getHeightIncrement();
                    int index  = Math.min(increment, numNodes - 1);
                    return selectedCandidate.replaceNode(index, selectedCandidate.mutate(rng, mutationProbability, factory));
                }
            }
        }
        return selectedCandidate.mutate(rng, mutationProbability, factory);
    }

    @Override
    public String toString() {
        return "Mutation(probability: " + mutationProbability.toString() + " ranges: " + Arrays.toString(ranges) + "}";
    }

    public static class Range {
        private int min, max;
        private int heightIncrement;

        public Range(int min, int max, int heightIncrement) {
            this.min = min;
            this.max = max;
            this.heightIncrement = heightIncrement;
        }

        public boolean inRange(PopulationData<? extends TreeNode> populationData) {
            return populationData.getGenerationNumber() >= min || populationData.getGenerationNumber() < max;
        }

        public int getHeightIncrement() {
            return heightIncrement;
        }

        @Override
        public String toString() {
            return MessageFormat.format("{0}<=gen<{1}; +{2}", min, min, heightIncrement);
        }
    }

}