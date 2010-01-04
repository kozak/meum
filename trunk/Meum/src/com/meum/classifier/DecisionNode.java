package com.meum.classifier;

import org.uncommons.maths.random.Probability;

import java.util.Random;

public class DecisionNode implements TreeNode {
    private TreeNode left;
    private TreeNode right;

    private int index;
    private double threshold;

    public DecisionNode(final TreeNode left, final TreeNode right, final int index, final double threshold) {
        this.left = left;
        this.right = right;
        this.index = index;
        this.threshold = threshold;
    }

    public Target evaluate(double[] programParameters) {
        return programParameters[index] > threshold ?
                left.evaluate(programParameters) :
                right.evaluate(programParameters);
    }

    public String print() {
        StringBuilder buffer = new StringBuilder("(");
        buffer.append("arg");
        buffer.append(index);
        buffer.append(" > ");
        buffer.append(threshold);
        buffer.append(" ? (");
        buffer.append(left.print());
        buffer.append(" : ");
        buffer.append(right.print());
        buffer.append(')');
        return buffer.toString();
    }

    public String getLabel() {
        return print();
    }

    public int getArity() {
        return 2;
    }

    public int getDepth() {
        return 1 + Math.max(left.getDepth(), right.getDepth());
    }

    public int getWidth() {
        return left.getWidth() + right.getWidth();
    }


    public int countNodes() {
        return 1 + left.countNodes() + right.countNodes();
    }


    public TreeNode getNode(int index) {
        if (index == 0) {
            return this;
        }
        int leftNodes = left.countNodes();
        if (index <= leftNodes) {
            return left.getNode(index - 1);
        } else {
            return right.getNode(index - leftNodes - 1);
        }
    }


    public TreeNode getChild(int index) {
        switch (index) {
            case 0:
                return left;
            case 1:
                return right;
            default:
                throw new IndexOutOfBoundsException("Invalid child index: " + index);
        }
    }


    public TreeNode replaceNode(int index, TreeNode newNode) {
        if (index == 0) {
            return newNode;
        }
        int leftNodes = left.countNodes();
        if (index <= leftNodes) {
            return new DecisionNode(left.replaceNode(index - 1, newNode), right, index, threshold);
        } else {
            return new DecisionNode(left, right.replaceNode(index - leftNodes - 1, newNode), index, threshold);
        }
    }


    public TreeNode mutate(Random rng, Probability mutationProbability, DecisionTreeFactory treeFactory) {
        if (mutationProbability.nextEvent(rng)) {
            return treeFactory.generateRandomCandidate(rng);
        } else {
            TreeNode newLeft = left.mutate(rng, mutationProbability, treeFactory);
            TreeNode newRight = right.mutate(rng, mutationProbability, treeFactory);
            if (newLeft != left && newRight != right) {
                return new DecisionNode(newLeft, newRight, index, threshold);
            } else {
                // Tree has not changed.
                return this;
            }
        }
    }
}
