package com.meum.classifier;

import com.meum.classifier.bias.fitness.ConstModifier;
import com.meum.classifier.bias.fitness.FitnessModifier;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;

import java.util.List;
import java.util.Map;

public class Fitness implements FitnessEvaluator<TreeNode> {
    private final Map<double[], Target> data;
    private final FitnessModifier fitnessModifier;

    public Fitness(final Map<double[], Target> data,
                   final FitnessModifier fitnessModifier) {
        this.data = data;
        this.fitnessModifier = fitnessModifier;
        this.fitnessModifier.setFitness(this);
    }

    public Fitness(final Map<double[], Target> data) {
        this.data = data;
        this.fitnessModifier = new ConstModifier();
    }

    public Map<double[], Target> getData() {
        return data;
    }


    public double getFitness(final TreeNode candidate, final List<? extends TreeNode> population) {
        double error = getBaseFitness(candidate);
        error = fitnessModifier.adjustFitness(error, candidate, population);
        return error;
    }

    protected double getBaseFitness(TreeNode candidate) {
        double error = 0;
        for (Map.Entry<double[], Target> entry : data.entrySet()) {
            Target targetClass = candidate.evaluate(entry.getKey());
            error += (targetClass == entry.getValue() ? 0 : 1);
        }
        return error;
    }

    public EnsembleFitness getEnsembleFitness(final TreeNode excluded, final List<? extends TreeNode> population) {
        double error = 0;
        double excludedError = 0;

        for (Map.Entry<double[], Target> entry : data.entrySet()) {
            int buy = 0;
            int sell = 0;
            int evil = 0;
            for (TreeNode candidate : population) {
                Target targetClass = candidate.evaluate(entry.getKey());
                switch (targetClass) {
                    case BUY:
                        buy++;
                        break;
                    case SELL:
                        sell++;
                        break;
                    case EVIL:
                        evil++;
                        break;
                }
            }
            Target ensemble = getTarget(buy, sell, evil);
            Target targetClass = excluded.evaluate(entry.getKey());
            switch (targetClass) {
                case BUY:
                    buy--;
                    break;
                case SELL:
                    sell--;
                    break;
                case EVIL:
                    evil--;
                    break;
            }
            Target ensembleExcluded = getTarget(buy, sell, evil);
            error += ensemble.equals(entry.getValue()) ? 0 : 1;
            excludedError += ensembleExcluded.equals(entry.getValue()) ? 0 : 1;
        }
        return new EnsembleFitness(error, excludedError);

    }

    private Target getTarget(int buy, int sell, int evil) {
        Target classifiedAs = Target.EVIL;
        if (buy > sell && buy > evil) {
            classifiedAs = Target.BUY;
        } else if (sell > evil) {
            classifiedAs = Target.SELL;
        }
        return classifiedAs;
    }

    public boolean isNatural() {
        return false;
    }


    @Override
    public String toString() {
        return "The number of incorrectly classified instances.";
    }

    public static class EnsembleFitness {
        private double ensemble;
        private double ensembleExcluded;

        public EnsembleFitness(double ensemble, double ensembleExcluded) {
            this.ensemble = ensemble;
            this.ensembleExcluded = ensembleExcluded;
        }

        public double getEnsemble() {
            return ensemble;
        }

        public double getEnsembleExcluded() {
            return ensembleExcluded;
        }
    }
}
