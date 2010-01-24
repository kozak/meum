package com.meum.classifier.test;

import com.meum.classifier.*;
import com.meum.classifier.analysis.PriceBasedBuySellChooser;
import com.meum.classifier.bias.fitness.EnsembleModifier;
import com.meum.classifier.bias.fitness.HeightBasedModifier;
import com.meum.classifier.bias.mutation.RangeBiasModifier;
import com.meum.classifier.utils.CsvReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
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
        final RangeBiasModifier mutationModifier = new RangeBiasModifier(
                new RangeBiasModifier.Range(0, 50, 0),
                new RangeBiasModifier.Range(50, 100, 1),
                new RangeBiasModifier.Range(100, 150, 0),
                new RangeBiasModifier.Range(150, 200, 2)
        );
        operators.add(new Mutation(new Probability(0.6), factory, mutationModifier));
        operators.add(new Crossover());
        operators.add(new Simplification());

        final HeightBasedModifier heightBasedFitnessModifier = new HeightBasedModifier();
        final Fitness fitness = new Fitness(trainingData, heightBasedFitnessModifier);

        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(mutationModifier);
        observers.add(heightBasedFitnessModifier);

        final TerminationCondition[] conditions = {new GenerationCount(200), new TargetFitness(2, false)};
        final TestConfig config = new TestConfig("Single market test",
                2, 1000, 20, conditions,
                fitness, 
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        evolve(factory, config, trainingData, null);
    }

    @Test
    public void testEnsembleSingleMarket() throws IOException {
        final double[] prices = reader.read("audeur");
        final PriceBasedBuySellChooser chooser = new PriceBasedBuySellChooser(prices);
        final Map<double[],Target> trainingData = chooser.getTrainingData(15, 0.005, 15);
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(2);
        final RangeBiasModifier mutationModifier = new RangeBiasModifier(
                new RangeBiasModifier.Range(0, 50, 0),
                new RangeBiasModifier.Range(50, 100, 1),
                new RangeBiasModifier.Range(100, 150, 0),
                new RangeBiasModifier.Range(150, 200, 2)
        );
        operators.add(new Mutation(new Probability(0.6), factory, mutationModifier));
        operators.add(new Crossover());
        operators.add(new Simplification());

        final EnsembleModifier ensembleModifier = new EnsembleModifier();
        final Fitness fitness = new Fitness(trainingData, ensembleModifier);

        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(mutationModifier);
        observers.add(ensembleModifier);

        final TerminationCondition[] conditions = {new GenerationCount(200), new TargetFitness(2, false)};
        final TestConfig config = new TestConfig("Single market test",
                2, 200, 20, conditions,
                fitness,
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        evolve(factory, config, trainingData, null);
    }
}
