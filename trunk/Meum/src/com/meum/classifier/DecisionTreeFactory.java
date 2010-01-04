package com.meum.classifier;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DecisionTreeFactory extends AbstractCandidateFactory<TreeNode> {

    private Map<double[], Target> trainingData;
    private int maxDepth;

    public DecisionTreeFactory(final Map<double[], Target> trainingData, int maxDepth) {
        if (trainingData == null || trainingData.isEmpty()) {
            throw new IllegalStateException("Training data can't be empty");
        }
        this.maxDepth = maxDepth;
        this.trainingData = trainingData;
    }


    public TreeNode generateRandomCandidate(Random rng) {
        return makeNode(rng, maxDepth);
    }

    private TreeNode makeNode(Random rng, int maxDepth) {
        final Set<double[]> values = trainingData.keySet();
        final double[] doubles = values.iterator().next();

        int attributeSize = doubles.length;
        int index = rng.nextInt(attributeSize);
        int whichValue = rng.nextInt(values.size());
        final Iterator<double[]> iterator = values.iterator();
        for (int i = 0; i < whichValue; i++) {
            iterator.next();
        }
        final double value = iterator.next()[index];

        if (maxDepth > 2) {
            int depth = maxDepth - 1;
            return new DecisionNode(makeNode(rng, depth), makeNode(rng, depth), index, value);
        } else {
            final Target target = rng.nextBoolean() ? Target.BUY : Target.SELL;
            return new DecisionNode(new LeafNode(target), new LeafNode(target.getOpposite()), index, value);
        }
    }
}
