package com.meum.classifier;

import com.meum.classifier.decision.Decision;
import org.uncommons.maths.random.Probability;

import java.util.Map;
import java.util.Random;

public class LeafNode implements TreeNode {
    private final Target target;
    private double baseFitness;

    public LeafNode(final Target target) {
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

    public void setBaseFitness(double fitness) {
        this.baseFitness = fitness;
    }

    public double getBaseFitness() {
        return baseFitness;
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

    public TreeNode simplify(Map<Decision, Integer> params) {
        return this;
    }
}
