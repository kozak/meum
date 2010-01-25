package com.meum.classifier.test;

import com.meum.classifier.*;
import com.meum.classifier.analysis.PriceBasedBuySellChooser;
import com.meum.classifier.bias.fitness.EnsembleModifier;
import com.meum.classifier.bias.fitness.FunctionModifier;
import com.meum.classifier.bias.mutation.ConstModifier;
import com.meum.classifier.bias.mutation.RangeBiasModifier;
import com.meum.classifier.utils.CsvReader;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
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

    @AfterTest
    public void afterTest() {
        System.gc();
    }

    @DataProvider(name = "marketDataProvider")
    public Object[][] marketDataProvider() {
        return new Object[][] {
                new Object[] {"chfeur"},
                new Object[] {"audeur"},
                new Object[] {"gbpeur"},
                new Object[] {"cadeur"}
        };
    }


    @Test(dataProvider = "marketDataProvider", invocationCount = 2)
    public void testSingleMarket(final String marketName) throws IOException {
        final double[] prices = reader.read(marketName);
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

        final FunctionModifier functionFitnessModifier = new FunctionModifier();
        final Fitness fitness = new Fitness(trainingData, functionFitnessModifier);

        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(mutationModifier);
        observers.add(functionFitnessModifier);

        final TerminationCondition[] conditions = {new GenerationCount(1000), new TargetFitness(2, false)};
        final TestConfig config = new TestConfig(getName(marketName + " - Height Based Modifier"),
                2, 1000, 20, conditions,
                fitness, 
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        evolve(factory, config, null);
    }



    @Test(dataProvider = "marketDataProvider", invocationCount = 2)
    public void testEnsembleSingleMarket(final String marketName) throws IOException {
        final double[] prices = reader.read(marketName);
        final PriceBasedBuySellChooser chooser = new PriceBasedBuySellChooser(prices);
        final Map<double[],Target> trainingData = chooser.getTrainingData(15, 0.005, 15);
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(2);

        operators.add(new Mutation(new Probability(0.6), factory, new ConstModifier()));
        operators.add(new Crossover());
        operators.add(new Simplification());

        final EnsembleModifier ensembleModifier = new EnsembleModifier(1.03, 0.9, 0.8);
        final Fitness fitness = new Fitness(trainingData, ensembleModifier);

        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(ensembleModifier);

        final TerminationCondition[] conditions = {new GenerationCount(1000), new TargetFitness(2, false)};
        final TestConfig config = new TestConfig(getName(marketName + " - Ensemble"),
                2, 100, 20, conditions,
                fitness,
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        evolve(factory, config, null);
    }

    @Test(dataProvider = "marketDataProvider", invocationCount = 2)
    public void testPopulationSingleMarket(final String marketName) throws IOException {
        final double[] prices = reader.read(marketName);
        final PriceBasedBuySellChooser chooser = new PriceBasedBuySellChooser(prices);
        final Map<double[],Target> trainingData = chooser.getTrainingData(15, 0.005, 15);
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);

        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(3);
        RangeBiasModifier modifier = new RangeBiasModifier(new RangeBiasModifier.Range(0,100,1),
                                                           new RangeBiasModifier.Range(0,500,2));
        operators.add(new Mutation(new Probability(0.6), factory, modifier));
        operators.add(new Crossover());
        operators.add(new Simplification());

        
        final Fitness fitness = new Fitness(trainingData, new FunctionModifier());
        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(modifier);

        final TerminationCondition[] conditions = {new GenerationCount(1000), new TargetFitness(2, false)};
        final TestConfig config = new TestConfig(getName(marketName + " - population evolution, "),
                2, 200, 20, conditions,
                fitness,
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        evolvePopulation(factory, config, null);
    }
}
