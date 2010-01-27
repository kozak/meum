package com.meum.classifier.bias.mutation;

import com.meum.classifier.TreeNode;

import java.util.Random;

public class ConstModifier extends MutationModifier {

    public ConstModifier() {
    }


    @Override
    public int mutateAt(TreeNode selectedCandidate, Random rng) {
        return 0;
    }

    @Override
    public String toString() {
        return "ConstModifier";
    }
}
