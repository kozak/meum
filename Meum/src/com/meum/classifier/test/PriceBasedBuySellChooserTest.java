package com.meum.classifier.test;

import com.meum.classifier.Target;
import com.meum.classifier.analysis.PriceBasedBuySellChooser;
import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public class PriceBasedBuySellChooserTest {
    @Test
    public void testGeneratedTrainingDataBuy() {
        double[] testData = new double[]{1,2,3,4,5,6,7,8,9,10,11,12};
        final PriceBasedBuySellChooser chooser = new PriceBasedBuySellChooser(testData);
        final Map<double[],Target> trainingData = chooser.getTrainingData(2, 0.5, 2);
        for (double[] doubles : trainingData.keySet()) {
            assertTrue(doubles.length == 2);
            if (doubles[0] != 10 && doubles[1] != 11) {
                assertTrue(trainingData.get(doubles) == Target.BUY,
                        MessageFormat.format("{0} target: {1}", Arrays.toString(doubles), trainingData.get(doubles)));
            }
            else {
                assertTrue(trainingData.get(doubles) == Target.CANT_TOUCH_THIS,
                        MessageFormat.format("{0} target: {1}", Arrays.toString(doubles), trainingData.get(doubles)));
            }
        }
    }

    @Test
    public void testGeneratedTrainingDataSell() {
        double[] testData = new double[]{12,11,10,9,8,7,6,5,4,3,2,1};
        final PriceBasedBuySellChooser chooser = new PriceBasedBuySellChooser(testData);
        final Map<double[],Target> trainingData = chooser.getTrainingData(2, 0.5, 2);
        for (double[] doubles : trainingData.keySet()) {
            assertTrue(doubles.length == 2);
            if (doubles[0] != 3 && doubles[1] != 2) {
                assertTrue(trainingData.get(doubles) == Target.SELL,
                        MessageFormat.format("{0} target: {1}", Arrays.toString(doubles), trainingData.get(doubles)));
            }
            else {
                assertTrue(trainingData.get(doubles) == Target.CANT_TOUCH_THIS,
                        MessageFormat.format("{0} target: {1}", Arrays.toString(doubles), trainingData.get(doubles)));
            }
        }
    }

    @Test
    public void testGeneratedTrainingDataSellWithSpread() {
        double[] testData = new double[]{12,11,10,9,8,7,6,5,4,3,2,1};
        final PriceBasedBuySellChooser chooser = new PriceBasedBuySellChooser(testData);
        final Map<double[],Target> trainingData = chooser.getTrainingData(2, 1, 2);
        for (double[] doubles : trainingData.keySet()) {
            assertTrue(doubles.length == 2);
            if (doubles[0] > 4 && doubles[1] > 3) {
                assertTrue(trainingData.get(doubles) == Target.SELL,
                        MessageFormat.format("{0} target: {1}", Arrays.toString(doubles), trainingData.get(doubles)));
            }
            else {
                assertTrue(trainingData.get(doubles) == Target.CANT_TOUCH_THIS,
                        MessageFormat.format("{0} target: {1}", Arrays.toString(doubles), trainingData.get(doubles)));
            }
        }
    }
}
