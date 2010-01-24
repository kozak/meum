package com.meum.classifier.decision;

import com.meum.classifier.Target;
import org.uncommons.maths.statistics.DataSet;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Map;

public abstract class StatisticBased implements Decision {
    protected static final NumberFormat FORMAT = new DecimalFormat("#0.0000");
    protected Map<double[], Target> trainingData;

    public StatisticBased(Map<double[], Target> trainingData) {
        this.trainingData = trainingData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatisticBased) {
            return obj.getClass().equals(this.getClass());
        }
        throw new IllegalStateException(
                MessageFormat.format("Wrong object passed for equality testing, expected: {0} got: {1}",
                        StatisticBased.class.getName(), obj.getClass().getName()));
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
