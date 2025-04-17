package uk.ac.bham.configPerformance;

import org.apache.commons.math3.stat.inference.TTest;
import java.io.*;
import java.util.*;

public class TTestHelper {
    public static void runTTestFromCSV(String csvFilePath) {
        Map<String, Double> baselineMape = new HashMap<>();
        Map<String, Double> tunedMape = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5 || parts[0].equals("Dataset")) continue;

                String dataset = parts[0].trim();
                double ridge = Double.parseDouble(parts[1]);
                double mape = Double.parseDouble(parts[4]);

                if (isBaselineRidge(ridge)) {
                    baselineMape.put(dataset,
                        baselineMape.containsKey(dataset) ?
                        Math.min(baselineMape.get(dataset), mape) : mape);
                } else {
                    tunedMape.put(dataset,
                        tunedMape.containsKey(dataset) ?
                        Math.min(tunedMape.get(dataset), mape) : mape);
                }
            }

            System.out.println("Baseline datasets: " + baselineMape.size());
            System.out.println("Tuned datasets: " + tunedMape.size());

            List<Double> baseList = new ArrayList<>();
            List<Double> tunedList = new ArrayList<>();

            for (String dataset : baselineMape.keySet()) {
                if (tunedMape.containsKey(dataset)) {
                    baseList.add(baselineMape.get(dataset));
                    tunedList.add(tunedMape.get(dataset));
                }
            }

            System.out.println("Comparable dataset pairs: " + baseList.size());

            if (baseList.size() < 2) {
                System.out.println("Not enough comparable datasets for statistical test.");
                return;
            }

            double[] baseArr = baseList.stream().mapToDouble(Double::doubleValue).toArray();
            double[] tunedArr = tunedList.stream().mapToDouble(Double::doubleValue).toArray();

            TTest tTest = new TTest();
            double pValue = tTest.pairedTTest(baseArr, tunedArr);

            System.out.printf("Paired t-test on MAPE (n = %d)\n", baseArr.length);
            System.out.printf("p-value: %.4f\n", pValue);

            if (pValue < 0.05) {
                System.out.println("Statistically significant improvement (p < 0.05).\n");
            } else {
                System.out.println("No statistically significant difference (p ≥ 0.05).\n");
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }

    private static boolean isBaselineRidge(double ridge) {
        double[] baselines = {1e-8, 1e-4, 1.0, 10.0};
        for (double b : baselines) {
            if (Double.compare(b, ridge) == 0) return true;
        }
        return false;
    }
}