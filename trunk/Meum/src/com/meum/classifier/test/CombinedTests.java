package com.meum.classifier.test;

import com.meum.classifier.*;
import com.meum.classifier.bias.fitness.FitnessModifier;
import com.meum.classifier.bias.fitness.FunctionModifier;
import com.meum.classifier.bias.fitness.NoOpModifier;
import com.meum.classifier.bias.mutation.ConstModifier;
import com.meum.classifier.bias.mutation.MutationModifier;
import com.meum.classifier.bias.mutation.RangeModifier;
import com.meum.classifier.utils.SummaryWriter;
import com.meum.classifier.utils.TestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Test(suiteName = "Combined tests")
public class CombinedTests extends EvolutionTest {
    private static final int NUM_PREDICTION_STEPS = 30;
    private static final int MAX_HOLD_TIME = 10;
    private static final double SPREAD = 0.002;

    @AfterClass()
    private void afterClass() throws Exception {
        SummaryWriter.write(this.getClass().getSimpleName(), testResults);
    }


    @DataProvider(name = "contradictingModifiers")
    public Object[][] marketDataProvider1() {
        final RangeModifier mutationModifier = new RangeModifier(
                new RangeModifier.Range(200, 400, 1),
                new RangeModifier.Range(400, 750, 2),
                new RangeModifier.Range(750, 1000, 3)
        );
        final FunctionModifier functionFitnessModifier = new FunctionModifier(new FunctionModifier.Function() {
            public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                final int number = populationData.getGenerationNumber();
                if (number <= 20) {
                    return 0;
                }
                return candidate.getDepth() * 10 / number;
            }

            @Override
            public String toString() {
                return "10 * depth/generation : generation > 20";
            }
        });

        final FunctionModifier promoteMoreArguments = new FunctionModifier(new FunctionModifier.Function() {
            public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                final int numGen = populationData.getGenerationNumber();
                if (numGen > 0) {
                    return -candidate.getDepth() * 10/ numGen;
                }
                return 0;
            }

            @Override
            public String toString() {
                return "-10 * depth/generation : generation > 20";
            }
        });

        return new Object[][]{
                new Object[]{"chfeur_long", "chfeur", mutationModifier, functionFitnessModifier},
                new Object[]{"audeur_long", "audeur", mutationModifier, functionFitnessModifier},
                new Object[]{"gbpeur_long", "gbpeur", mutationModifier, functionFitnessModifier},
                new Object[]{"cadeur_long", "cadeur", mutationModifier, functionFitnessModifier},

                new Object[]{"chfeur_long", "chfeur", new ConstModifier(), new NoOpModifier()},
                new Object[]{"audeur_long", "audeur", new ConstModifier(), new NoOpModifier()},
                new Object[]{"gbpeur_long", "gbpeur", new ConstModifier(), new NoOpModifier()},
                new Object[]{"cadeur_long", "cadeur", new ConstModifier(), new NoOpModifier()},

                new Object[]{"chfeur_long", "chfeur", new ConstModifier(), functionFitnessModifier},
                new Object[]{"audeur_long", "audeur", new ConstModifier(), functionFitnessModifier},
                new Object[]{"gbpeur_long", "gbpeur", new ConstModifier(), functionFitnessModifier},
                new Object[]{"cadeur_long", "cadeur", new ConstModifier(), functionFitnessModifier},

                new Object[]{"chfeur_long", "chfeur", mutationModifier, new NoOpModifier()},
                new Object[]{"audeur_long", "audeur", mutationModifier, new NoOpModifier()},
                new Object[]{"gbpeur_long", "gbpeur", mutationModifier, new NoOpModifier()},
                new Object[]{"cadeur_long", "cadeur", mutationModifier, new NoOpModifier()},

                new Object[]{"chfeur_long", "chfeur", mutationModifier, promoteMoreArguments},
                new Object[]{"audeur_long", "audeur", mutationModifier, promoteMoreArguments},
                new Object[]{"gbpeur_long", "gbpeur", mutationModifier, promoteMoreArguments},
                new Object[]{"cadeur_long", "cadeur", mutationModifier, promoteMoreArguments}
        };
    }


    @Test(dataProvider = "contradictingModifiers")
    public void testNormal(final String marketName,
                           final String testDataName,
                           final MutationModifier mutationModifier,
                           final FitnessModifier fitnessModifier1) throws IOException {

        final Map<double[], Target> trainingData = getData(marketName, NUM_PREDICTION_STEPS, SPREAD, MAX_HOLD_TIME);

        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(2);

        operators.add(new Mutation(new Probability(0.6), factory, mutationModifier));
        operators.add(new Crossover());
        operators.add(new Simplification());

        final Fitness fitness = new Fitness(trainingData, fitnessModifier1);

        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(mutationModifier);
        observers.add(fitnessModifier1);


        final TerminationCondition[] conditions = {new GenerationCount(1000)};
        final TestConfig config = new TestConfig(getName(marketName + "ContradictingModifiers"),
                2, 1000, 20, conditions,
                fitness,
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        TreeNode result = evolve(factory, config, null);

        addResult(marketName, testDataName, mutationModifier, fitnessModifier1, trainingData, config, result);


    }


/*    @Test(dataProvider = "contradictingModifiers")
    public void testEnsemble(final String marketName,
                             final String testDataName,
                             final MutationModifier mutationModifier,
                             final FitnessModifier fitnessModifier1) throws IOException {
        final Map<double[], Target> trainingData = getData(marketName, NUM_PREDICTION_STEPS, SPREAD, MAX_HOLD_TIME);
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(2);

        operators.add(new Mutation(new Probability(0.6), factory, mutationModifier));
        operators.add(new Crossover());
        operators.add(new Simplification());

        final EnsembleModifier ensembleModifier = new EnsembleModifier(1.03, 0.9, 0.8);
        final Fitness fitness = new Fitness(trainingData, ensembleModifier, fitnessModifier1);


        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(ensembleModifier);
        observers.add(mutationModifier);
        observers.add(fitnessModifier1);

        final TerminationCondition[] conditions = {new GenerationCount(1000)};
        final TestConfig config = new TestConfig(getName(marketName + "EnsembleContradictingModifiers"),
                2, 100, 20, conditions,
                fitness,
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        TreeNode result = evolve(factory, config, null);

        addResult(marketName, testDataName, mutationModifier, fitnessModifier1, trainingData, config, result);

    }*/

    private void addResult(String marketName, String testDataName, MutationModifier mutationModifier, FitnessModifier fitnessModifier1, Map<double[], Target> trainingData, TestConfig config, TreeNode result) throws IOException {
        final Map<double[], Target> testData = getData(testDataName, NUM_PREDICTION_STEPS, SPREAD, MAX_HOLD_TIME);
        final double testFitness = Fitness.getBaseFitness(testData, result) / testData.size();
        final double trainingFitness = Fitness.getBaseFitness(trainingData, result) / trainingData.size();
        log("Base fitness: " + trainingFitness);
        log("Test set base fitness: " + testFitness);
        testResults.add(new TestResult(marketName, config.getTestName() + ".jpg",
                trainingFitness, testFitness, result.getDepth(), mutationModifier, fitnessModifier1));
    }


    /*@Test(dataProvider = "contradictingModifiers")
    public void testPopulation(final String marketName,
                               final MutationModifier mutationModifier,
                               final FitnessModifier fitnessModifier1) throws IOException {
        final Map<double[], Target> trainingData = getData(marketName, NUM_PREDICTION_STEPS, SPREAD, MAX_HOLD_TIME);
        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, 2);

        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(3);

        operators.add(new Mutation(new Probability(0.6), factory, mutationModifier));
        operators.add(new Simplification());


        final Fitness fitness = new Fitness(trainingData, fitnessModifier1);

        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(fitnessModifier1);
        observers.add(mutationModifier);

        final TerminationCondition[] conditions = {new GenerationCount(1000)};
        final TestConfig config = new TestConfig(getName(marketName + "PopulationEvolutionContradictingModifiers"),
                2, 100, 20, conditions,
                fitness,
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());

        final List<EvaluatedCandidate<TreeNode>> population = evolvePopulation(factory, config, null);
        final Map<double[], Target> testData = getData(marketName, NUM_PREDICTION_STEPS, SPREAD, MAX_HOLD_TIME);
        final double testFitness = Fitness.getEnsembleFitness(testData, population) / testData.size();
        final double trainingFitness = Fitness.getEnsembleFitness(trainingData, population) / trainingData.size();
        log("Base fitness: " + trainingFitness);
        log("Test set base fitness: " + testFitness);
        testResults.add(new TestResult(marketName, "",
                trainingFitness, testFitness, 0, mutationModifier, fitnessModifier1));
    }*/
}
