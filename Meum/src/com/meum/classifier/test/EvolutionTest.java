package com.meum.classifier.test;

import com.meum.classifier.*;
import com.meum.classifier.utils.ImageWriter;
import com.meum.classifier.utils.TreeRenderer;
import org.testng.Reporter;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

public abstract class EvolutionTest {

    protected String getName(String name)  {
        final int currentInvocationCount = Reporter.getCurrentTestResult().getMethod().getCurrentInvocationCount();
        return name + " #" + currentInvocationCount;
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
        log("Base fitness: " + treeNode.getBaseFitness());
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
        log("Base fitness: " + fitness.getEnsembleFitness(population));
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
                    "\n termination conditions: " + Arrays.toString(terminationConditions) +
                    "\n fitness: " + fitness.toString() +
                    "\n operators: " + operators.toString() +
                    "\n observers: " + observers.toString() +
                    "\n random number generator: " + random.getClass().getSimpleName() +
                    "\n selection strategy: " + selectionStrategy.toString() +
                    "\n};";
        }


    }

}
