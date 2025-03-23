package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.classifiers.Evaluation;
import java.util.Random;

/**
 * This class performs random search hyperparameter tuning for the ridge parameter
 * in the Ridge Regression (regularized linear regression) model.
 */
public class RandomSearchTuner {

    /**
     * Performs random search to find the best ridge parameter.
     *
     * @param data       the dataset used for tuning
     * @param iterations the number of random samples to evaluate
     * @param folds      the number of folds for cross-validation
     * @param seed       the random seed for reproducibility
     * @param low        the lower bound for candidate ridge values
     * @param high       the upper bound for candidate ridge values
     * @return the best ridge parameter found
     * @throws Exception if evaluation fails
     */
    public static double tuneRidgeParameter(Instances data, int iterations, int folds, long seed, double low, double high) throws Exception {
        double bestRidge = low;
        double bestError = Double.MAX_VALUE;
        Random rand = new Random(seed);

        for (int i = 0; i < iterations; i++) {
            // Generate a random candidate ridge value between low and high.
            double candidate = low + (high - low) * rand.nextDouble();

            // Build a model using the candidate ridge parameter.
            ConfPerfModel model = new ConfPerfModel(candidate);
            model.train(data);

            // Evaluate the model using cross-validation.
            Evaluation eval = CrossValidator.performCrossValidation(model.getModel(), data, folds, seed);
            double rmse = eval.rootMeanSquaredError();

            // Print candidate details.
            // System.out.println("Iteration " + (i + 1) + ": Candidate ridge = " + candidate + ", RMSE = " + rmse);

            // Update best candidate if current candidate achieves a lower RMSE.
            if (rmse < bestError) {
                bestError = rmse;
                bestRidge = candidate;
            }
        }

        System.out.println("Best ridge parameter found: " + bestRidge + " with RMSE = " + bestError + "\n");
        return bestRidge;
    }
}
