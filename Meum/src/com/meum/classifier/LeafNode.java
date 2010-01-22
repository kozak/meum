package com.meum.classifier;

import org.uncommons.maths.random.Probability;

import java.util.Random;

public class LeafNode implements TreeNode {
    private Target target;

    public LeafNode(Target target) {
        this.target = target;
    }

    public Target evaluate(double[] programParameters) {
        return target;
    }

    public String print() {
        return target.toString();
    }

    public String getLabel() {
        return target.toString();
    }

    public Target getTarget() {
        return target;
    }

    public int getArity() {
        return 0;
    }

    public int getDepth() {
        return 1;
    }

    public int getWidth() {
        return 1;
    }

    public int countNodes() {
        return 1;
    }

    public TreeNode getNode(int index) {
        if (index == 0) {
            return this;
        }
        throw new IllegalStateException("Wrong node index");
    }

    public TreeNode getChild(int index) {
        throw new UnsupportedOperationException("Leaf node doesn't have children");
    }

    public TreeNode replaceNode(int index, TreeNode newNode) {
        if (index == 0) {
            return newNode;
        }
        throw new IllegalStateException("Wrong node index");
    }

    public TreeNode mutate(Random rng, Probability mutationProbability, DecisionTreeFactory treeFactory) {
        if (mutationProbability.nextEvent(rng)) {
            return treeFactory.generateRandomCandidate(rng);
        } else {
            // Node is unchanged.
            return this;
        }
    }

    public TreeNode simplify() {
        return this;
    }
}
