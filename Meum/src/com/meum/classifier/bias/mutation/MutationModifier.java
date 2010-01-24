package com.meum.classifier.bias.mutation;

import com.meum.classifier.TreeNode;
import com.meum.classifier.bias.BiasModifier;

import java.util.Random;

public abstract class MutationModifier extends BiasModifier {

    abstract public int mutateAt(TreeNode selectedCandidate, Random rng);
}
