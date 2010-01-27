package com.meum.classifier.bias.fitness;

import com.meum.classifier.TreeNode;
import org.uncommons.watchmaker.framework.PopulationData;

import java.util.List;

public class FunctionModifier extends FitnessModifier {
    private final Function function;

    public FunctionModifier() {
        function = new Function() {
            public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                return Math.abs(Math.sin(populationData.getGenerationNumber()));
            }

            @Override
            public String toString() {
                return "abs(sin(generationNumber))";
            }
        };
    }

    public FunctionModifier(final Function function) {
        this.function = function;
    }

    @Override
    public double adjustFitness(double fitness, TreeNode candidate, List<? extends TreeNode> population) {
        if (populationData != null) {
            return fitness + function.evaluate(candidate, populationData);
        }
        return fitness;
    }

    public static interface Function {
        double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData);
    }

    @Override
    public String toString() {
        return "FunctionModifier {" +
                "function=" + function.toString() +
                '}';
    }
}
