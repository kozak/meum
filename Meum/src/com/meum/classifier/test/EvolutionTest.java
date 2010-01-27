package com.meum.classifier.test;

import com.meum.classifier.*;
import com.meum.classifier.analysis.PriceBasedBuySellChooser;
import com.meum.classifier.bias.fitness.FitnessModifier;
import com.meum.classifier.bias.mutation.MutationModifier;
import com.meum.classifier.utils.CsvReader;
import com.meum.classifier.utils.ImageWriter;
import com.meum.classifier.utils.TestResult;
import com.meum.classifier.utils.TreeRenderer;
import org.testng.Reporter;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class EvolutionTest {
    private static final int NUM_PREDICTION_STEPS = 15;
    private static final int MAX_HOLD_TIME = 15;
    private static final double SPREAD = 0.002;
    protected final List<TestResult> testResults = new ArrayList<TestResult>(64);

    protected String getName(String name) {
        final int currentInvocationCount = Reporter.getCurrentTestResult().getMethod().getCurrentInvocationCount();
        return name + currentInvocationCount;
    }


    protected void test(final String marketName,
                        final String testSuffix,
                        final FitnessModifier modifier,
                        final MutationModifier mutationModifier,
                        final double mutationProbability,
                        final int numGenerations,
                        final int subTreeDepth,
                        final int populationSize,
                        final int eliteCount) throws IOException {

        final Map<double[], Target> trainingData = getData(marketName, NUM_PREDICTION_STEPS, SPREAD, MAX_HOLD_TIME);

        DecisionTreeFactory factory = new DecisionTreeFactory(trainingData, subTreeDepth);
        List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>(3);
        operators.add(new Mutation(new Probability(mutationProbability),
                factory, mutationModifier));
        operators.add(new Crossover());
        operators.add(new Simplification());

        final Fitness fitness = new Fitness(trainingData, modifier);

        List<EvolutionObserver<TreeNode>> observers = new ArrayList<EvolutionObserver<TreeNode>>(1);
        observers.add(modifier);
        observers.add(mutationModifier);

        final TerminationCondition[] conditions = {new GenerationCount(numGenerations), new TargetFitness(0, false)};
        final TestConfig config = new TestConfig(getName(marketName + " " + testSuffix),
                subTreeDepth, populationSize, eliteCount, conditions,
                fitness,
                operators,
                observers,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());
        TreeNode result = evolve(factory, config, null);

        final Map<double[], Target> testData = getData(marketName+"_test", NUM_PREDICTION_STEPS, SPREAD, MAX_HOLD_TIME);
        final double testFitness = Fitness.getBaseFitness(testData, result);

        final double baseFitness = result.getBaseFitness() / fitness.getData().size();
        final double testSetFitness = testFitness / testData.size();
        testResults.add(new TestResult(marketName,
                config.getTestName()+".jpg",
                baseFitness,
                testSetFitness, result.getDepth(),
                mutationModifier, modifier));
        log("Base fitness: " + baseFitness);
        log("Test set base fitness: " + testSetFitness);
    }

    protected Map<double[], Target> getData(final String name,
                                            final int numPredictionSteps,
                                            final double spread,
                                            final int maxHoldTime
    ) throws IOException {
        final double[] testPrices = CsvReader.getInstance().read(name);
        final PriceBasedBuySellChooser testChooser = new PriceBasedBuySellChooser(testPrices);
        return testChooser.getTrainingData(numPredictionSteps, spread, maxHoldTime);

    }

    @SuppressWarnings("unchecked")
    public static TreeNode evolve(final DecisionTreeFactory factory,
                                  final TestConfig config,
                                  final EngineInitializer initializer) throws IOException {
        log(config);
        List<EvolutionaryOperator<TreeNode>> operators = config.getOperators();
        Fitness fitness = config.getFitness();
        EvolutionEngine<TreeNode> engine = new GenerationalEvolutionEngine<TreeNode>(factory,
                new EvolutionPipeline<TreeNode>(operators),
                fitness,
                config.getSelectionStrategy(),
                config.getRandom());
        final List<EvolutionObserver<TreeNode>> evolutionObserverList = config.getObservers();
        for (EvolutionObserver<TreeNode> treeNodeEvolutionObserver : evolutionObserverList) {
            engine.addEvolutionObserver(treeNodeEvolutionObserver);
        }
        //engine.addEvolutionObserver(new EvolutionLogger());

        if (initializer != null) {
            initializer.initialise(engine);
        }
        final TreeNode treeNode = engine.evolve(config.getPopulationSize(),
                config.getEliteCount(),
                config.getTerminationConditions());

        log(treeNode.print());
        ImageWriter.save(config.getTestName(), new TreeRenderer().render(treeNode));
        return treeNode;
    }

    @SuppressWarnings("unchecked")
    public static List<EvaluatedCandidate<TreeNode>> evolvePopulation(final DecisionTreeFactory factory,
                                                                      final TestConfig config,
                                                                      final EngineInitializer initializer) throws IOException {
        log(config);
        List<EvolutionaryOperator<TreeNode>> operators = config.getOperators();
        Fitness fitness = config.getFitness();
        EvolutionEngine<TreeNode> engine = new GenerationalEvolutionEngine<TreeNode>(factory,
                new EvolutionPipeline<TreeNode>(operators),
                fitness,
                config.getSelectionStrategy(),
                config.getRandom());
        final List<EvolutionObserver<TreeNode>> evolutionObserverList = config.getObservers();
        for (EvolutionObserver<TreeNode> treeNodeEvolutionObserver : evolutionObserverList) {
            engine.addEvolutionObserver(treeNodeEvolutionObserver);
        }
        //engine.addEvolutionObserver(new EvolutionLogger());

        if (initializer != null) {
            initializer.initialise(engine);
        }

        final List<EvaluatedCandidate<TreeNode>> population = engine.evolvePopulation(config.getPopulationSize(),
                config.getEliteCount(),
                config.getTerminationConditions());
        log("Base fitness: " + Fitness.getEnsembleFitness(fitness.getData(), population) / fitness.getData().size());
        return population;
    }

    public static TreeNode evolve(final DecisionTreeFactory factory,
                                  final TestConfig config) throws IOException {
        return evolve(factory, config, null);
    }

    public static List<EvaluatedCandidate<TreeNode>> evolvePopulation(final DecisionTreeFactory factory,
                                                                      final TestConfig config) throws IOException {
        return evolvePopulation(factory, config, null);
    }

    public static void log(final Object toLog) {
        Reporter.log(toLog.toString(), true);
    }

    private static class EvolutionLogger implements EvolutionObserver<TreeNode> {
        public void populationUpdate(PopulationData<? extends TreeNode> data) {
            final double baseFitness = data.getBestCandidate().getBaseFitness();
            log(MessageFormat.format("Generation {0}; Base fitness: {1} Modified fitness: {2}",
                    data.getGenerationNumber(), baseFitness, data.getBestCandidateFitness()));
        }
    }

    public static interface EngineInitializer {
        void initialise(EvolutionEngine engine);
    }

    public static class TestConfig {
        int subTreeMaxDepth;
        int populationSize;
        int eliteCount;
        TerminationCondition[] terminationConditions;
        Fitness fitness;
        List<EvolutionaryOperator<TreeNode>> operators;
        List<EvolutionObserver<TreeNode>> observers;
        Random random;
        SelectionStrategy selectionStrategy;
        private String testName;

        public TestConfig(String testName,
                          int subTreeMaxDepth,
                          int populationSize,
                          int eliteCount,
                          TerminationCondition[] terminationConditions,
                          Fitness fitness, List<EvolutionaryOperator<TreeNode>> operators,
                          List<EvolutionObserver<TreeNode>> observers,
                          Random random,
                          SelectionStrategy selectionStrategy) {
            this.testName = testName;
            this.subTreeMaxDepth = subTreeMaxDepth;
            this.populationSize = populationSize;
            this.eliteCount = eliteCount;
            this.terminationConditions = terminationConditions;
            this.fitness = fitness;
            this.operators = operators;
            this.random = random;
            this.selectionStrategy = selectionStrategy;
            this.observers = observers;
        }

        public int getSubTreeMaxDepth() {
            return subTreeMaxDepth;
        }

        public int getPopulationSize() {
            return populationSize;
        }

        public int getEliteCount() {
            return eliteCount;
        }

        public TerminationCondition[] getTerminationConditions() {
            return terminationConditions;
        }

        public Fitness getFitness() {
            return fitness;
        }

        public List<EvolutionaryOperator<TreeNode>> getOperators() {
            return operators;
        }

        public Random getRandom() {
            return random;
        }

        public SelectionStrategy getSelectionStrategy() {
            return selectionStrategy;
        }

        public String getTestName() {
            return testName;
        }

        public List<EvolutionObserver<TreeNode>> getObservers() {
            return observers;
        }

        @Override
        public String toString() {
            return "TestConfig { " +
                    "\n " + testName +
                    "\n sub tree max depth: " + subTreeMaxDepth +
                    "\n population size: " + populationSize +
                    "\n elite count: " + eliteCount +
                    "\n fitness: " + fitness.toString() +
                    "\n operators: " + operators.toString() +
                    "\n observers: " + observers.toString() +
                    "\n random number generator: " + random.getClass().getSimpleName() +
                    "\n selection strategy: " + selectionStrategy.toString() +
                    "\n};";
        }


    }

}
