package uk.ac.bham.configPerformance;

/**
 * Utility class for calculating error metrics.
 */
public class ErrorMetrics {

	// Calculate the Mean Absolute Error (MAE).
	public static double calculateMAE(double[] actual, double[] predicted) {
		if (actual == null || predicted == null || actual.length != predicted.length) {
			throw new IllegalArgumentException("Arrays must be non-null and of the same length.");
		}
		double sum = 0.0;
		for (int i = 0; i < actual.length; i++) {
			sum += Math.abs(actual[i] - predicted[i]);
		}
		return sum / actual.length;
	}

	// Calculate the Root Mean Squared Error (RMSE).
	public static double calculateRMSE(double[] actual, double[] predicted) {
		if (actual == null || predicted == null || actual.length != predicted.length) {
			throw new IllegalArgumentException("Arrays must be non-null and of the same length.");
		}
		double sum = 0.0;
		for (int i = 0; i < actual.length; i++) {
			sum += Math.pow(actual[i] - predicted[i], 2);
		}
		return Math.sqrt(sum / actual.length);
	}
    
    // Calculate Mean Absolute Percentage Error (MAPE)
    public static double calculateMAPE(double[] actual, double[] predicted) {
        if (actual == null || predicted == null || actual.length != predicted.length) {
            throw new IllegalArgumentException("Arrays must be non-null and of the same length.");
        }
        double sum = 0.0;
        int count = 0;
        for (int i = 0; i < actual.length; i++) {
            // 실제 값이 0이면 제외하여 분모 0 오류 방지
            if (actual[i] != 0) {
                sum += Math.abs((actual[i] - predicted[i]) / actual[i]);
                count++;
            }
        }
        return count > 0 ? sum / count : 0;
    }
}
