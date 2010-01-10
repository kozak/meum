package com.meum.classifier;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassifierWithRealData {

    public static void main(String[] args) {
        MarketData marketData = new MarketData("audeur", 3, 0.001, "chfeur", "gbpeur");
        TreeNode node = evolve(marketData.getData());
        System.out.println(node.print());
    }

    public static TreeNode evolve(Map<double[], Target> trainingData) {
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(2);
        operators.add(new Mutation(new Probability(0.6), factory));
        operators.add(new Crossover());
        final Fitness fitness = new Fitness(trainingData, 1000, 0.001);
        EvolutionEngine<TreeNode> engine = new GenerationalEvolutionEngine<TreeNode>(factory,
                new EvolutionPipeline<TreeNode>(operators),
                fitness,
                new RouletteWheelSelection(),
                new MersenneTwisterRNG());
        engine.addEvolutionObserver(new EvolutionLogger());
        engine.addEvolutionObserver(fitness);
        return engine.evolve(1000, 20, new TargetFitness(1, fitness.isNatural()), new GenerationCount(1000));
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
