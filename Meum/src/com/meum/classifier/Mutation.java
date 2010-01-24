package com.meum.classifier;

import com.meum.classifier.bias.mutation.ConstModifier;
import com.meum.classifier.bias.mutation.MutationModifier;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mutation implements EvolutionaryOperator<TreeNode> {
    private final Probability mutationProbability;
    private final DecisionTreeFactory factory;
    private final MutationModifier biasModifier;

    public Mutation(Probability mutationProbability,
                    DecisionTreeFactory factory,
                    MutationModifier biasModifier) {
        this.mutationProbability = mutationProbability;
        this.factory = factory;
        this.biasModifier = biasModifier;
    }

    public Mutation(Probability mutationProbability, DecisionTreeFactory factory) {
        this.mutationProbability = mutationProbability;
        this.factory = factory;
        this.biasModifier = new ConstModifier();
    }

    public List<TreeNode> apply(List<TreeNode> selectedCandidates, Random rng) {
        List<TreeNode> mutated = new ArrayList<TreeNode>(selectedCandidates.size());
        for (TreeNode selectedCandidate : selectedCandidates) {
            mutated.add(getMutatedCandidate(rng, selectedCandidate));
        }
        return mutated;
    }


    private TreeNode getMutatedCandidate(Random rng, TreeNode selectedCandidate) {

        int index = biasModifier.mutateAt(selectedCandidate, rng);
        return selectedCandidate.replaceNode(index, selectedCandidate.mutate(rng, mutationProbability, factory));

    }

    @Override
    public String toString() {
        return "Mutation(probability: " + mutationProbability.toString() + " bias modifier: " + biasModifier.toString() + "}";
    }


}