package com.meum.classifier;

import org.uncommons.watchmaker.examples.geneticprogramming.Node;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simplification implements EvolutionaryOperator<TreeNode> {

    public List<TreeNode> apply(List<TreeNode> selectedCandidates, Random rng) {
        List<TreeNode> evolved = new ArrayList<TreeNode>(selectedCandidates.size());
        for (TreeNode node : selectedCandidates)
        {
            evolved.add(node.simplify());
        }
        return evolved;
    }

    @Override
    public String toString() {
        return "Simplification(replaces decision nodes where both children targets are the same with a single leaf)";
    }
}
