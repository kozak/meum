package com.meum.classifier;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mutation implements EvolutionaryOperator<TreeNode> {
    private final Probability mutationProbability;
    private final DecisionTreeFactory factory;

    public Mutation(Probability mutationProbability,
                    DecisionTreeFactory factory) {
        this.mutationProbability = mutationProbability;
        this.factory = factory;
    }

    public List<TreeNode> apply(List<TreeNode> selectedCandidates, Random rng) {
        List<TreeNode> mutated = new ArrayList<TreeNode>(selectedCandidates.size());
        for (TreeNode selectedCandidate : selectedCandidates) {
            mutated.add(selectedCandidate.mutate(rng, mutationProbability, factory));
        }
        return mutated;
    }
}
