package com.meum.classifier.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.meum.classifier.Target;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

public final class CsvReader {
    private static final String DATA_FOLDER = "data";
    private Map<String, double[]> cache;
    private static final int MARKET_PRICE_POSITION = 1;

    private CsvReader() {
        cache = new HashMap<String, double[]>();
    }

    private static CsvReader instance;

    public static synchronized CsvReader getInstance() {
        if (instance == null) {
            instance = new CsvReader();
        }
        return instance;
    }


    public double[] read(final String name) throws IOException {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }
        final CSVReader reader = new CSVReader(new FileReader(MessageFormat.format("{0}/{1}.csv", DATA_FOLDER, name)));
        final List<String[]> strings = reader.readAll();
        final double[] data = new double[strings.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = Double.parseDouble(strings.get(i)[MARKET_PRICE_POSITION]);
        }
        return data;
    }

}
