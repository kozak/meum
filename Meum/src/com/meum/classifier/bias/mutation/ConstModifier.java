package com.meum.classifier.bias.mutation;

import com.meum.classifier.TreeNode;

import java.util.Random;

public class ConstModifier extends MutationModifier {
    private int index;

    public ConstModifier() {
        index = 0;
    }

    public ConstModifier(int index) {
        this.index = index;
    }

    @Override
    public int mutateAt(TreeNode selectedCandidate, Random rng) {
        return index;
    }

    @Override
    public String toString() {
        return "Mutate at: " + index;
    }
}
