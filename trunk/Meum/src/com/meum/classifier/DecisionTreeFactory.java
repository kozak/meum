package com.meum.classifier;

import com.meum.classifier.decision.Decision;
import com.meum.classifier.decision.ThresholdDecision;
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
        final int decisionType = 0;
        Decision decision;
        switch (decisionType) {
            default:
                decision = new ThresholdDecision(trainingData, rng);
        }

        if (maxDepth > 2) {
            int depth = maxDepth - 1;
            return new DecisionNode(makeNode(rng, depth), makeNode(rng, depth), decision);
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
                    target = Target.EVIL;
                    break;
                default:
                    throw new IllegalStateException("Can't determine the target value for " + targetIndex );

            }            
            return new DecisionNode(new LeafNode(target), new LeafNode(target.getOpposite()), decision);
        }
    }
}
