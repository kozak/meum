package com.meum.classifier.bias.mutation;

import com.meum.classifier.TreeNode;
import org.uncommons.watchmaker.framework.PopulationData;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Random;

public class RangeBiasModifier extends MutationModifier {
    private Range[] ranges;

    public RangeBiasModifier(Range ...ranges) {
        this.ranges = ranges;
    }

    @Override
    public int mutateAt(TreeNode selectedCandidate, Random rng) {
        if (populationData == null) {
            return 0;
        }
        final int numNodes = selectedCandidate.countNodes();
        for (Range range : ranges) {
            if (range.inRange(populationData)) {
                final int increment = range.getHeightIncrement();
                return Math.min(increment, numNodes - 1);
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(ranges);
    }

    public static class Range {
        private int min, max;
        private int heightIncrement;

        public Range(int min, int max, int heightIncrement) {
            this.min = min;
            this.max = max;
            if (heightIncrement < 0) {
                throw new IllegalStateException("Height exception should be >= 0");
            }
            this.heightIncrement = heightIncrement;
        }

        public boolean inRange(PopulationData<? extends TreeNode> populationData) {
            return populationData.getGenerationNumber() >= min || populationData.getGenerationNumber() < max;
        }

        public int getHeightIncrement() {
            return heightIncrement;
        }

        @Override
        public String toString() {
            return MessageFormat.format("{0}<=gen<{1}; +{2}", min, max, heightIncrement);
        }
    }
}
