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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtificialDataTests extends EvolutionTest {
    private static final Map<double[], Target> TEST_DATA = new HashMap<double[], Target>();

    static {
        TEST_DATA.put(new double[]{10, 10, 11, 11, 12, 12, 13, 13}, Target.BUY);
        TEST_DATA.put(new double[]{0.5, 0.5, 0.5, 0.6, 0.6, 0.6, 0.7, 0.7}, Target.BUY);
        TEST_DATA.put(new double[]{1, 2, 3, 4, 5, 6, 7, 8}, Target.BUY);
        TEST_DATA.put(new double[]{8, 7, 6, 5, 4, 3, 2, 1}, Target.SELL);
        TEST_DATA.put(new double[]{8, 8, 8, 7, 7, 7, 6, 6}, Target.SELL);
    }


    @Test(description = "Basic test to check everything is up and running")
    public void basicTest() {
        final Map<double[], Target> data = TEST_DATA;
        final DecisionTreeFactory factory = new DecisionTreeFactory(data, 2);
        final List<EvolutionaryOperator<TreeNode>> operators = new ArrayList<EvolutionaryOperator<TreeNode>>();
        operators.add(new Mutation(Probability.EVENS, factory));

        final TestConfig config = new TestConfig(2, 1000, 20, new TerminationCondition[]{
                new TargetFitness(0, false),
                new GenerationCount(1000)},
                new Fitness(data, 500, 0.001),
                operators,
                new MersenneTwisterRNG(),
                new RouletteWheelSelection());

        evolve(config, data, null);
    }


}
