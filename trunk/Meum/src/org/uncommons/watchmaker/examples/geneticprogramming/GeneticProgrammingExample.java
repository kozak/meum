// ============================================================================
//   Copyright 2006-2009 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================
package org.uncommons.watchmaker.examples.geneticprogramming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

/**
 * Simple tree-based genetic programming application based on the first example
 * in Chapter 11 of Toby Segaran's Progamming Collective Intelligence.
 *
 * @author Daniel Dyer
 */
public class GeneticProgrammingExample {
    // This data describes the problem.  For each pair of inputs, the generated program
    // should return the associated output.  The goal of this appliction is to generalise
    // the examples into an equation.
    private static final Map<double[], Double> TEST_DATA = new HashMap<double[], Double>();

    static {
        TEST_DATA.put(new double[]{
                1.1361,
                1.1356,
                1.1383,
                1.1428,
                1.1461,
                1.1474,
                1.1474,
                1.1435,
                1.1406,
                1.1381,
                1.1386}, 1.1437d);
        TEST_DATA.put(new double[]{
                1.144,
                1.144,
                1.1387,
                1.1316,
                1.1227,
                1.1203,
                1.1111,
                1.1066,
                1.1065,
                1.1043,
                1.1046,
        }, 1.1087);
    }


    public static void main(String[] args) {
        Node program = evolveProgram(TEST_DATA);
        System.out.println(program.print());
        for (double[] arg : TEST_DATA.keySet()) {
            System.out.printf("Predicted value: %s ; real value = %s%n", program.evaluate(arg), TEST_DATA.get(arg));
        }
    }


    /**
     * Evolve a function to fit the specified data.
     *
     * @param data A map from input values to expected output values.
     * @return A program that generates the correct outputs for all specified
     *         sets of input.
     */
    public static Node evolveProgram(Map<double[], Double> data) {
        TreeFactory factory = new TreeFactory(11, // Number of parameters passed into each program.
                6, // Maximum depth of generated trees.
                Probability.EVENS, // Probability that a node is a function node.
                new Probability(0.7d)); // Probability that other nodes are params rather than constants.
        List<EvolutionaryOperator<Node>> operators = new ArrayList<EvolutionaryOperator<Node>>(3);
        operators.add(new TreeMutation(factory, new Probability(0.4d)));
        operators.add(new TreeCrossover());
        operators.add(new Simplification());
        TreeEvaluator evaluator = new TreeEvaluator(data);
        EvolutionEngine<Node> engine = new GenerationalEvolutionEngine<Node>(factory,
                new EvolutionPipeline<Node>(operators),
                evaluator,
                new RouletteWheelSelection(),
                new MersenneTwisterRNG());
        engine.addEvolutionObserver(new EvolutionLogger());
        return engine.evolve(1000, 5, new TargetFitness(5.5e-8d, evaluator.isNatural()));
    }


    /**
     * Trivial evolution observer for displaying information at the end
     * of each generation.
     */
    private static class EvolutionLogger implements EvolutionObserver<Node> {
        public void populationUpdate(PopulationData<? extends Node> data) {
            System.out.println("Generation " + data.getGenerationNumber() + ": " + data.getBestCandidateFitness());
        }
    }
}
