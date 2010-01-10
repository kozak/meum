package com.meum.classifier;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.*;

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

    private TreeNode makeNode(final Random rng, final int maxDepth) {
        final Set<double[]> values = trainingData.keySet();
        int whichValue = rng.nextInt(values.size());
        final Iterator<double[]> iterator = values.iterator();
        double[] doubles = iterator.next();
        for (int i = 1; i < whichValue; i++) {
            doubles = iterator.next();
        }
        if (doubles == null) {
            System.out.println("value: " + whichValue);

            throw new IllegalStateException("The randomly chosen training set should exist");
        }
        final int index = rng.nextInt(doubles.length);

        final double value = doubles[index];

        if (maxDepth > 2) {
            int depth = maxDepth - 1;
            return new DecisionNode(makeNode(rng, depth), makeNode(rng, depth), index, value);
        } else {
            final int targetIndex = rng.nextInt(3);
            Target target;
            switch (targetIndex) {
                case 0:
                    target = Target.BUY;
                    break;
                case 1:
                    target = Target.SELL;
                    break;
                case 2:
                    target = Target.CANT_TOUCH_THIS;
                    break;
                default:
                    throw new IllegalStateException("Can't determine the target value for " + targetIndex );

            }            
            return new DecisionNode(new LeafNode(target), new LeafNode(target.getOpposite()), index, value);
        }
    }
}
