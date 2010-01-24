package com.meum.classifier.test;

import com.meum.classifier.*;
import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.IOException;
import java.util.*;

public class ArtificialDataTests extends EvolutionTest {
    private static final Map<double[], Target> TEST_DATA = new HashMap<double[], Target>();

    static {
        TEST_DATA.put(new double[]{10, 10, 11, 11, 12, 12, 13, 13}, Target.BUY);
        TEST_DATA.put(new double[]{0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.7, 0.7}, Target.BUY);
        TEST_DATA.put(new double[]{1, 2, 3, 4, 5, 6, 7, 8}, Target.BUY);
        TEST_DATA.put(new double[]{8, 7, 6, 5, 4, 3, 2, 1}, Target.SELL);
        TEST_DATA.put(new double[]{8, 8, 8, 7, 7, 7, 6, 6}, Target.SELL);
    }


    @Test
    public void basicTest() throws IOException {
        final Map<double[], Target> data = TEST_DATA;
        final DecisionTreeFactory factory = new DecisionTreeFactory(data, 2);
        final List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>();
        operators.add(new Mutation(Probability.EVENS, factory));
        operators.add(new Simplification());
        final TestConfig config = new TestConfig("Basic test to check everything is up and runnning",
                2, 1000, 20, new TerminationCondition[]{
                        new TargetFitness(0, false),
                        new GenerationCount(1000)},
                new Fitness(data),
                operators,
                Collections.<EvolutionObserver<TreeNode>>emptyList(),
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());

        evolve(factory, config, data, null);
    }


}
