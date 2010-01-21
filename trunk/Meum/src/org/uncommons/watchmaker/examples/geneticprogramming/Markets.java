package org.uncommons.watchmaker.examples.geneticprogramming;

import au.com.bytecode.opencsv.CSVReader;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Markets {

    public static Map<double[], Double> read(final String name,
                                             final int numParameters) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(name + ".csv"));
        final List<String[]> strings = reader.readAll();
        if (numParameters > strings.size()) {
            throw new IllegalStateException("Not enough data for " + numParameters + " parameters.");
        }
        int dataSize = strings.size() / (numParameters + 1);
        int dataIterator = 0;
        final Map<double[], Double> data = new HashMap<double[], Double>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            final double[] prices = new double[numParameters];
            for (int j = 0; j < prices.length; j++) {
                prices[j] = Double.parseDouble(strings.get(dataIterator)[1]);
                dataIterator++;
            }
            final Double result = Double.parseDouble(strings.get(dataIterator)[1]);
            data.put(prices, result);
        }
        return data;
    }

    public static void main(String[] args) throws IOException {
        final int numParameters =  30;
        final Map<double[], Double> training = read("data/audeur1", numParameters);
        final Map<double[], Double> test = read("data/audeur", numParameters);
        Node program = evolveProgram(training, numParameters);

        System.out.println(program.print());

        for (double[] doubles : test.keySet()) {
            System.out.println("Target: " + test.get(doubles) + " Result: " + program.evaluate(doubles));
        }
    }


    /**
     * Evolve a function to fit the specified data.
     *
     * @param data         A map from input values to expected output values.
     * @param numParameter number of parameters to take into account
     * @return A program that generates the correct outputs for all specified
     *         sets of input.
     */
    public static Node evolveProgram(final Map<double[], Double> data,
                                     final int numParameter) {
        TreeFactory factory = new TreeFactory(numParameter, // Number of parameters passed into each program.
                numParameter, // Maximum depth of generated trees.
                new Probability(0.5d), // Probability that a node is a function node.
                new Probability(0.7d)); // Probability that other nodes are params rather than constants.
        List<EvolutionaryOperator<Node>> operators = new ArrayList<EvolutionaryOperator<Node>>(3);
        operators.add(new TreeMutation(factory, new Probability(0.6d)));
        operators.add(new TreeCrossover());
        operators.add(new Simplification());
        TreeEvaluator evaluator = new TreeEvaluator(data);
        EvolutionEngine<Node> engine = new GenerationalEvolutionEngine<Node>(factory,
                new EvolutionPipeline<Node>(operators),
                evaluator,
                new RouletteWheelSelection(),
                new MersenneTwisterRNG());
        engine.addEvolutionObserver(new EvolutionLogger());
        return engine.evolve(1000, 20, new TargetFitness(0.000001d, evaluator.isNatural()), new GenerationCount(1000));
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
