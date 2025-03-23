package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import java.util.Random;

/**
 * Utility class for performing cross-validation.
 */
public class CrossValidator {

	// Perform k-fold cross-validation on the given Linear Regression model and dataset.
	public static Evaluation performCrossValidation(LinearRegression model, Instances data, int folds, long seed)
			throws Exception {
		if (data == null || data.numInstances() == 0) {
			throw new IllegalArgumentException("Dataset is empty or null.");
		}
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(model, data, folds, new Random(seed));
		return eval;
	}
}
