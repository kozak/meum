package com.meum.classifier.test;

import com.meum.classifier.*;
import com.meum.classifier.utils.ImageWriter;
import com.meum.classifier.utils.TreeRenderer;
import org.testng.Reporter;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

import java.io.IOException;
import java.util.*;

public abstract class EvolutionTest {

    @SuppressWarnings("unchecked")
    public static TreeNode evolve(final DecisionTreeFactory factory, 
                                  final TestConfig config,
                                  final Map<double[], Target> trainingData,
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
        engine.addEvolutionObserver(new EvolutionLogger());
        
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

    public static TreeNode evolve(final DecisionTreeFactory factory,
                                  final TestConfig config,
                                  final Map<double[], Target> trainingData) throws IOException {
        return evolve(factory, config, trainingData, null);
    }


    public static void log(final Object toLog) {
        Reporter.log(toLog.toString(), true);
    }

    private static class EvolutionLogger implements EvolutionObserver<TreeNode> {
        public void populationUpdate(PopulationData<? extends TreeNode> data) {
            log("Generation " + data.getGenerationNumber() + ": " + data.getBestCandidateFitness());
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
