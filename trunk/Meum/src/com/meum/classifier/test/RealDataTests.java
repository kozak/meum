package com.meum.classifier.test;

import com.meum.classifier.*;
import com.meum.classifier.analysis.PriceBasedBuySellChooser;
import com.meum.classifier.utils.CsvReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RealDataTests extends EvolutionTest {
    private CsvReader reader;

    @BeforeClass
    public void beforeClassSetup() {
        reader = CsvReader.getInstance();
    }

    @Test
    public void testSingleMarket() throws IOException {
        final double[] prices = reader.read("audeur");
        final PriceBasedBuySellChooser chooser = new PriceBasedBuySellChooser(prices);
        final Map<double[],Target> trainingData = chooser.getTrainingData(15, 0.005, 15);
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(2);
        operators.add(new Mutation(new Probability(0.6), factory, new Mutation.Range(200,300, 1),
                new Mutation.Range(300,500, 2),
                new Mutation.Range(500,600, 3),
                new Mutation.Range(600,1000, 4)
        ));
        operators.add(new Crossover());
        final Fitness fitness = new Fitness(trainingData, 1000, 0.001);
        final TerminationCondition[] conditions = {new GenerationCount(1000), new TargetFitness(2, false)};
        final TestConfig config = new TestConfig(2, 1000, 20, conditions, fitness, operators, new MersenneTwisterRNG(), new RouletteWheelSelection());
        evolve(config, trainingData, null);
    }
}
