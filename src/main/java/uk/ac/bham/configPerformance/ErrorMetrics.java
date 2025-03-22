package uk.ac.bham.configPerformance;

public class ErrorMetrics {

    public static double calculateMAE(double[] actual, double[] predicted) {
        double sum = 0.0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.abs(actual[i] - predicted[i]);
        }
        return sum / actual.length;
    }

    public static double calculateRMSE(double[] actual, double[] predicted) {
        double sum = 0.0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.pow(actual[i] - predicted[i], 2);
        }
        return Math.sqrt(sum / actual.length);
    }
}
