package com.meum.classifier;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crossover extends AbstractCrossover<TreeNode> {
    public Crossover() {
        super(1);
    }


    @Override
    protected List<TreeNode> mate(TreeNode parent1,
                                  TreeNode parent2,
                                  int numberOfCrossoverPoints,
                                  Random rng) {
        List<TreeNode> offspring = new ArrayList<TreeNode>(2);
        TreeNode offspring1 = parent1;
        TreeNode offspring2 = parent2;

        for (int i = 0; i < numberOfCrossoverPoints; i++) {
            int crossoverPoint1 = rng.nextInt(parent1.countNodes());
            TreeNode subTree1 = parent1.getNode(crossoverPoint1);
            int crossoverPoint2 = rng.nextInt(parent2.countNodes());
            TreeNode subTree2 = parent2.getNode(crossoverPoint2);
            offspring1 = parent1.replaceNode(crossoverPoint1, subTree2);
            offspring2 = parent2.replaceNode(crossoverPoint2, subTree1);
        }

        offspring.add(offspring1);
        offspring.add(offspring2);
        return offspring;
    }
}
