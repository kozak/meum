package com.meum.classifier.decision;

import com.meum.classifier.Target;
import org.uncommons.maths.statistics.DataSet;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MeanValue extends StatisticBased {
    private double meanDeviation;
    private double mean;

    public MeanValue(final Map<double[], Target> trainingData, final Random rng) {
        super(trainingData);
        mean = meanDeviation = 0;
        final int which = rng.nextInt(trainingData.keySet().size());
        int i = 0;
        final Iterator<double[]> iterator = trainingData.keySet().iterator();
        double[] data = null;
        do {
            data = iterator.next();
            i++;
        }
        while (iterator.hasNext() && i < which);
        DataSet set = new DataSet(data);
        meanDeviation = set.getMeanDeviation();
        mean = set.getArithmeticMean();

    }


    public int getSubTreeIndex(double[] programParameters) {
        double m = 0;
        for (double programParameter : programParameters) {
            m += programParameter;
        }
        m /= programParameters.length;
        return Math.abs(mean - m) > meanDeviation ? 0 : 1;
    }

    @Override
    public String toString() {
        return "mean(" + FORMAT.format(mean) + "+/-" + FORMAT.format(meanDeviation) + ")";
    }
}
