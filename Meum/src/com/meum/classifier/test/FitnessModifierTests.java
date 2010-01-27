package com.meum.classifier.test;

import com.meum.classifier.TreeNode;
import com.meum.classifier.bias.fitness.*;
import com.meum.classifier.bias.mutation.ConstModifier;
import com.meum.classifier.utils.SummaryWriter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.uncommons.watchmaker.framework.PopulationData;

import java.io.IOException;

@Test(suiteName = "Fitness modifier tests")
public class FitnessModifierTests extends EvolutionTest {
    @AfterClass()
    private void afterClass() throws Exception {
        SummaryWriter.write(this.getClass().getSimpleName(), testResults);
    }


    @DataProvider(name = "EnsembleModifier")
    public Object[][] marketDataProvider2() {
        return new Object[][]{
                new Object[]{"chfeur", new EnsembleModifier(1.1, 1, 0.9), 0.6, 1000, 2, 100, 20},
                new Object[]{"chfeur", new EnsembleModifier(1.2, 1, 0.8), 0.6, 1000, 2, 100, 20},
                new Object[]{"chfeur", new EnsembleModifier(1.1, 0.9, 0.8), 0.6, 1000, 2, 100, 20},
                new Object[]{"chfeur", new EnsembleModifier(0.9, 1, 1.1), 0.6, 1000, 2, 100, 20},
                new Object[]{"chfeur", new EnsembleModifier(2, 1, 0.5), 0.6, 1000, 2, 100, 20},

                new Object[]{"gbpeur", new EnsembleModifier(1.1, 1, 0.9), 0.6, 1000, 2, 100, 20},
                new Object[]{"gbpeur", new EnsembleModifier(1.2, 1, 0.8), 0.6, 1000, 2, 100, 20},
                new Object[]{"gbpeur", new EnsembleModifier(1.1, 0.9, 0.8), 0.6, 1000, 2, 100, 20},
                new Object[]{"gbpeur", new EnsembleModifier(0.9, 1, 1.1), 0.6, 1000, 2, 100, 20},
                new Object[]{"gbpeur", new EnsembleModifier(2, 1, 0.5), 0.6, 1000, 2, 100, 20},

                new Object[]{"cadeur", new EnsembleModifier(1.1, 1, 0.9), 0.6, 1000, 2, 100, 20},
                new Object[]{"cadeur", new EnsembleModifier(1.2, 1, 0.8), 0.6, 1000, 2, 100, 20},
                new Object[]{"cadeur", new EnsembleModifier(1.1, 0.9, 0.8), 0.6, 1000, 2, 100, 20},
                new Object[]{"cadeur", new EnsembleModifier(0.9, 1, 1.1), 0.6, 1000, 2, 100, 20},
                new Object[]{"cadeur", new EnsembleModifier(2, 1, 0.5), 0.6, 1000, 2, 100, 20}
        };
    }

    @DataProvider(name = "FunctionModifier")
    public Object[][] marketDataProvider3() {
        return new Object[][]{
                new Object[]{"chfeur", new FunctionModifier(new FunctionModifier.Function() {
                    public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                        return Math.abs(Math.sin(candidate.getDepth() * 0.1));
                    }

                    @Override
                    public String toString() {
                        return "abs(sin(depth*0.1))";
                    }
                }), 0.6, 1000, 2, 1000, 20},

                new Object[]{"chfeur", new FunctionModifier(new FunctionModifier.Function() {
                    public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                        return -candidate.getDepth() * 0.001;
                    }

                    @Override
                    public String toString() {
                        return "-depth*0.001";
                    }
                }), 0.6, 1000, 2, 1000, 20},

                new Object[]{"chfeur", new FunctionModifier(new FunctionModifier.Function() {
                    public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                        return Math.atan(candidate.getDepth() * 0.1);
                    }

                    @Override
                    public String toString() {
                        return "atan(depth*0.1)";
                    }
                }), 0.6, 1000, 2, 1000, 20},
                new Object[]{"gbpeur", new FunctionModifier(new FunctionModifier.Function() {
                    public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                        return Math.abs(Math.sin(candidate.getDepth() * 0.1));
                    }

                    @Override
                    public String toString() {
                        return "abs(sin(depth*0.1))";
                    }
                }), 0.6, 1000, 2, 1000, 20},

                new Object[]{"cadeur", new FunctionModifier(new FunctionModifier.Function() {
                    public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                        return Math.atan(candidate.getDepth() * 0.1);
                    }

                    @Override
                    public String toString() {
                        return "atan(depth*0.1)";
                    }
                }), 0.6, 1000, 2, 1000, 20},

                new Object[]{"cadeur", new FunctionModifier(new FunctionModifier.Function() {
                    public double evaluate(TreeNode candidate, PopulationData<? extends TreeNode> populationData) {
                        if (populationData.getGenerationNumber() > 0) {
                            return candidate.getDepth()/populationData.getGenerationNumber();
                        }
                        return 0;
                    }

                    @Override
                    public String toString() {
                        return "depth/generation : generation > 0";
                    }
                }), 0.6, 1000, 2, 1000, 20}

        };
    }


    @DataProvider(name = "NoOpModifier")
    public Object[][] marketDataProvider4() {
        return new Object[][]{
                new Object[]{"chfeur", new NoOpModifier(), 0.6, 1000, 2, 1000, 20},
                new Object[]{"gbpeur", new NoOpModifier(), 0.6, 1000, 2, 1000, 20},
                new Object[]{"cadeur", new NoOpModifier(), 0.6, 1000, 2, 1000, 20}
        };
    }



    @Test(dataProvider = "EnsembleModifier")
    public void testEnsembleModifier(final String marketName,
                                     final FitnessModifier modifier,
                                     final double mutationProbability,
                                     final int numGenerations,
                                     final int subTreeDepth,
                                     final int populationSize,
                                     final int eliteCount) throws IOException {
        test(marketName, "EnsembleModifier", modifier,
                new ConstModifier(), mutationProbability, numGenerations, subTreeDepth, populationSize, eliteCount);
    }

    @Test(dataProvider = "FunctionModifier")
    public void testFunctionModifier(final String marketName,
                                     final FitnessModifier modifier,
                                     final double mutationProbability,
                                     final int numGenerations,
                                     final int subTreeDepth,
                                     final int populationSize,
                                     final int eliteCount) throws IOException {
        test(marketName, "FunctionModifier",
                modifier, new ConstModifier(), mutationProbability, numGenerations, subTreeDepth, populationSize, eliteCount);
    }

    @Test(dataProvider = "NoOpModifier")
    public void testNoOpModifier(final String marketName,
                                 final FitnessModifier modifier,
                                 final double mutationProbability,
                                 final int numGenerations,
                                 final int subTreeDepth,
                                 final int populationSize,
                                 final int eliteCount) throws IOException {
        test(marketName, "NoOpModifier", modifier,
                new ConstModifier(), mutationProbability, numGenerations, subTreeDepth, populationSize, eliteCount);
    }


}
