package com.meum.classifier.bias.fitness;

import com.meum.classifier.DecisionNode;
import com.meum.classifier.TreeNode;
import com.meum.classifier.decision.Decision;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArgumentDuplicationModifier extends FitnessModifier {
    private Set<Decision> params = new HashSet<Decision>();
    private int duplicateCount;
    private double duplicationPenalty;

    public ArgumentDuplicationModifier(double duplicationPenalty) {
        this.duplicationPenalty = duplicationPenalty;
    }

    @Override
    public double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population) {
        duplicateCount=0;
        params.clear();
        checkNode(candidate);
        return fitness + duplicateCount*duplicationPenalty;
    }

    protected void checkNode(TreeNode node) {
        if (node instanceof DecisionNode) {
            DecisionNode n = (DecisionNode) node;
            if (!params.add(n.getDecision())) {
                duplicateCount++;
            }
            checkNode(n.getChild(0));
            checkNode(n.getChild(1));
        }
    }

    @Override
    public String toString() {
        return "ArgumentDuplicationModifier{" +
                "duplicationPenalty=" + duplicationPenalty +
                '}';
    }
}
