package com.meum.classifier;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuySellClassifier {

    private static final Map<double[], Target> TEST_DATA = new HashMap<double[], Target>();

    static {
        TEST_DATA.put(new double[]{10, 10, 11, 11, 12, 12, 13, 13}, Target.BUY);
        TEST_DATA.put(new double[]{0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.7, 0.7}, Target.BUY);
        TEST_DATA.put(new double[]{1, 2, 3, 4, 5, 6, 7, 8}, Target.BUY);
        TEST_DATA.put(new double[]{8, 7, 6, 5, 4, 3, 2 , 1}, Target.SELL);
        TEST_DATA.put(new double[]{8, 8, 8, 7, 7, 7, 6, 6}, Target.SELL);
    }

    public static void main(String[] args) {
        TreeNode node = evolve(TEST_DATA);
        System.out.println(node.print());
    }

    public static TreeNode evolve(Map<double[], Target> trainingData) {
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(2);
        operators.add(new Mutation(Probability.ONE, factory));
        operators.add(new Crossover());
        Fitness fitness = new Fitness(trainingData);
        EvolutionEngine<TreeNode> engine = new GenerationalEvolutionEngine<TreeNode>(factory,
                new EvolutionPipeline<TreeNode>(operators),
                fitness,
                new RouletteWheelSelection(),
                new MersenneTwisterRNG());
        engine.addEvolutionObserver(new EvolutionLogger());
        return engine.evolve(20, 1, new TargetFitness(0, fitness.isNatural()), new GenerationCount(1000));
    }

    /**
     * Trivial evolution observer for displaying information at the end
     * of each generation.
     */
    private static class EvolutionLogger implements EvolutionObserver<TreeNode> {
        public void populationUpdate(PopulationData<? extends TreeNode> data) {
            System.out.println("Generation " + data.getGenerationNumber() + ": " + data.getBestCandidateFitness());
        }
    }
}
