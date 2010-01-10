package com.meum.classifier;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

public class MarketData {
    private static final int MARKET_PRICE_POSITION = 1;
    private static final String DATA_FOLDER = "data";

    private Map<double[], Target> data;
    private final double requiredChange;

    public MarketData(final String toClassify,
                      final int numParameters,
                      final double requiredChange,
                      final String... relevantMarketNames) {

        if (requiredChange == 0) {
            throw new IllegalStateException("Required change must be bigger then 0");
        }
        this.requiredChange = Math.abs(requiredChange);

        try {
            final List<double[]> mainMarketData = getPrices(toClassify, numParameters);
            data = new HashMap<double[], Target>(mainMarketData.size());
            final List<List<double[]>> relevantMarketData = new ArrayList<List<double[]>>(relevantMarketNames.length);
            for (String marketName : relevantMarketNames) {
                final List<double[]> prices = getPrices(marketName, numParameters);
                relevantMarketData.add(prices);
            }

            for (int currentPriceSet = 0; currentPriceSet < mainMarketData.size(); currentPriceSet++) {
                final double[] prices = mainMarketData.get(currentPriceSet);
                final Target target = getTarget(prices);

                final double[] finalParameters = new double[prices.length + prices.length * relevantMarketData.size() - 1];
                System.arraycopy(prices, 0, finalParameters, 0, prices.length - 1);

                for (int i = 0; i < relevantMarketData.size(); i++) {
                    final List<double[]> marketPrices = relevantMarketData.get(i);
                    if (mainMarketData.size() != marketPrices.size()) {
                        throw new IllegalStateException(
                                MessageFormat.format("The auto regressive and regressive data sizes can''t be different {0} vs {1}",
                                        mainMarketData.size(), marketPrices.size()));

                    }
                    final double[] relevantPrices = marketPrices.get(currentPriceSet);
                    final int destPos = prices.length + i * prices.length - 1;
                    System.arraycopy(relevantPrices, 0,
                            finalParameters, destPos, relevantPrices.length);
                }
                data.put(finalParameters, target);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    private Target getTarget(final double[] prices) {
        if (prices.length < 2) {
            throw new IllegalStateException("Not enough arguments to determine the classification target");
        }
        final double change = prices[prices.length - 1] - prices[prices.length - 2];
        if (Math.abs(change) > requiredChange) {
            if (change > 0) {
                return Target.BUY;
            } else {
                return Target.SELL;
            }
        }
        return Target.CANT_TOUCH_THIS;
    }

    public Map<double[], Target> getData() {
        return data;
    }

    private static List<double[]> getPrices(final String name,
                                            final int numParameters) throws IOException {


        final CSVReader reader = new CSVReader(new FileReader(MessageFormat.format("{0}/{1}.csv", DATA_FOLDER, name)));
        final List<String[]> strings = reader.readAll();
        if (numParameters > strings.size()) {
            throw new IllegalStateException("Not enough data for " + numParameters + " parameters.");
        }
        int dataSize = strings.size() / (numParameters);
        int dataIterator = 0;
        final List<double[]> data = new ArrayList<double[]>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            final double[] prices = new double[numParameters];
            for (int j = 0; j < prices.length; j++) {
                prices[j] = Double.parseDouble(strings.get(dataIterator)[MARKET_PRICE_POSITION]);
                dataIterator++;
            }
            data.add(prices);
        }
        return data;
    }

}
