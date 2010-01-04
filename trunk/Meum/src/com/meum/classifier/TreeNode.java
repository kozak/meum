package com.meum.classifier;

import org.uncommons.maths.random.Probability;

import java.util.Random;

public interface TreeNode {

    Target evaluate(double[] programParameters);

    String print();

    String getLabel();

    int getArity();

    int getDepth();

    int getWidth();

    int countNodes();

    TreeNode getNode(int index);

    TreeNode getChild(int index);

    TreeNode replaceNode(int index, TreeNode newNode);

    TreeNode mutate(Random rng, Probability mutationProbability, DecisionTreeFactory treeFactory);

}
