package com.meum.classifier.test;

import com.meum.classifier.bias.fitness.NoOpModifier;
import com.meum.classifier.bias.mutation.ConstModifier;
import com.meum.classifier.bias.mutation.MutationModifier;
import com.meum.classifier.bias.mutation.RangeModifier;
import com.meum.classifier.utils.SummaryWriter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(suiteName = "Mutation modifier tests")
public class MutationModifierTests extends EvolutionTest {

    @AfterClass()
    private void afterClass() throws Exception {
        SummaryWriter.write(this.getClass().getSimpleName(), testResults);
    }

    @DataProvider(name = "ConstModifier")
    public Object[][] marketDataProvider1() {
        return new Object[][]{
                new Object[]{"chfeur", new ConstModifier(), 0.6, 1000, 2, 1000, 20},
                new Object[]{"gbpeur", new ConstModifier(), 0.6, 1000, 2, 1000, 20},
                new Object[]{"cadeur", new ConstModifier(), 0.6, 1000, 2, 1000, 20},
        };
    }

    @DataProvider(name = "RangeModifier")
    public Object[][] marketDataProvider2() {
        return new Object[][]{
                new Object[]{"chfeur", new RangeModifier(new RangeModifier.Range(50, 100, 0),
                        new RangeModifier.Range(100, 200, 1),
                        new RangeModifier.Range(200, 500, 1),
                        new RangeModifier.Range(500, 1000, 2)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"chfeur", new RangeModifier(new RangeModifier.Range(50, 100, 0),
                        new RangeModifier.Range(100, 150, 0),
                        new RangeModifier.Range(150, 200, 1),
                        new RangeModifier.Range(200, 250, 1),
                        new RangeModifier.Range(250, 300, 2),
                        new RangeModifier.Range(350, 400, 2),
                        new RangeModifier.Range(400, 450, 2)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"chfeur", new RangeModifier(new RangeModifier.Range(50, 100, 3),
                        new RangeModifier.Range(100, 150, 0),
                        new RangeModifier.Range(150, 200, 4),
                        new RangeModifier.Range(200, 250, 0),
                        new RangeModifier.Range(250, 300, 5),
                        new RangeModifier.Range(350, 400, 0),
                        new RangeModifier.Range(400, 450, 7)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"chfeur", new RangeModifier(new RangeModifier.Range(50, 100, 7),
                        new RangeModifier.Range(100, 150, 0),
                        new RangeModifier.Range(150, 200, 5),
                        new RangeModifier.Range(200, 250, 0),
                        new RangeModifier.Range(250, 300, 3),
                        new RangeModifier.Range(350, 400, 0),
                        new RangeModifier.Range(400, 450, 1)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"chfeur", new RangeModifier(new RangeModifier.Range(300, 600, 1),
                        new RangeModifier.Range(600, 1000, 3)), 0.6, 1000, 2, 1000, 20},


                new Object[]{"gbpeur", new RangeModifier(new RangeModifier.Range(50, 100, 1),
                        new RangeModifier.Range(100, 200, 2),
                        new RangeModifier.Range(200, 500, 3),
                        new RangeModifier.Range(500, 1000, 4)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"gbpeur", new RangeModifier(new RangeModifier.Range(50, 100, 1),
                        new RangeModifier.Range(100, 150, 2),
                        new RangeModifier.Range(150, 200, 3),
                        new RangeModifier.Range(200, 250, 4),
                        new RangeModifier.Range(250, 300, 5),
                        new RangeModifier.Range(350, 400, 6),
                        new RangeModifier.Range(400, 450, 7)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"gbpeur", new RangeModifier(new RangeModifier.Range(50, 100, 3),
                        new RangeModifier.Range(100, 150, 0),
                        new RangeModifier.Range(150, 200, 4),
                        new RangeModifier.Range(200, 250, 0),
                        new RangeModifier.Range(250, 300, 5),
                        new RangeModifier.Range(350, 400, 0),
                        new RangeModifier.Range(400, 450, 7)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"gbpeur", new RangeModifier(new RangeModifier.Range(50, 100, 7),
                        new RangeModifier.Range(100, 150, 0),
                        new RangeModifier.Range(150, 200, 5),
                        new RangeModifier.Range(200, 250, 0),
                        new RangeModifier.Range(250, 300, 3),
                        new RangeModifier.Range(350, 400, 0),
                        new RangeModifier.Range(400, 450, 1)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"gbpeur", new RangeModifier(new RangeModifier.Range(300, 600, 1),
                        new RangeModifier.Range(600, 1000, 3)), 0.6, 1000, 2, 1000, 20},


                new Object[]{"cadeur", new RangeModifier(new RangeModifier.Range(50, 100, 1),
                        new RangeModifier.Range(100, 200, 2),
                        new RangeModifier.Range(200, 500, 3),
                        new RangeModifier.Range(500, 1000, 4)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"cadeur", new RangeModifier(new RangeModifier.Range(50, 100, 1),
                        new RangeModifier.Range(100, 150, 2),
                        new RangeModifier.Range(150, 200, 3),
                        new RangeModifier.Range(200, 250, 4),
                        new RangeModifier.Range(250, 300, 5),
                        new RangeModifier.Range(350, 400, 6),
                        new RangeModifier.Range(400, 450, 7)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"cadeur", new RangeModifier(new RangeModifier.Range(50, 100, 3),
                        new RangeModifier.Range(100, 150, 0),
                        new RangeModifier.Range(150, 200, 4),
                        new RangeModifier.Range(200, 250, 0),
                        new RangeModifier.Range(250, 300, 5),
                        new RangeModifier.Range(350, 400, 0),
                        new RangeModifier.Range(400, 450, 7)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"cadeur", new RangeModifier(new RangeModifier.Range(50, 100, 7),
                        new RangeModifier.Range(100, 150, 0),
                        new RangeModifier.Range(150, 200, 5),
                        new RangeModifier.Range(200, 250, 0),
                        new RangeModifier.Range(250, 300, 3),
                        new RangeModifier.Range(350, 400, 0),
                        new RangeModifier.Range(400, 450, 1)), 0.6, 1000, 2, 1000, 20},

                new Object[]{"cadeur", new RangeModifier(new RangeModifier.Range(300, 600, 1),
                        new RangeModifier.Range(600, 1000, 3)), 0.6, 1000, 2, 1000, 20}
        };
    }


    @Test(dataProvider = "ConstModifier")
    public void testConstModifier(final String marketName,
                                  final MutationModifier modifier,
                                  final double mutationProbability,
                                  final int numGenerations,
                                  final int subTreeDepth,
                                  final int populationSize,
                                  final int eliteCount) throws IOException {
        test(marketName, "ConstModifier", new NoOpModifier(), modifier,
                mutationProbability, numGenerations, subTreeDepth, populationSize, eliteCount);
    }

    @Test(dataProvider = "RangeModifier")
    public void testRangeModifier(final String marketName,
                                  final MutationModifier modifier,
                                  final double mutationProbability,
                                  final int numGenerations,
                                  final int subTreeDepth,
                                  final int populationSize,
                                  final int eliteCount) throws IOException {
        test(marketName, "RangeModifier", new NoOpModifier(), modifier,
                mutationProbability, numGenerations, subTreeDepth, populationSize, eliteCount);
    }

}
