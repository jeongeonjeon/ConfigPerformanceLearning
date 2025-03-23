package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;

/**
 * Class for training and predicting configuration performance using a Linear Regression model.
 */
public class ConfPerfModel {

	private LinearRegression model;

	public ConfPerfModel() {
		model = new LinearRegression();
	}

	// Train the model with the given dataset.
	public void train(Instances data) throws Exception {
		if (data == null || data.numInstances() == 0) {
			throw new IllegalArgumentException("Dataset is empty or null.");
		}
		model.buildClassifier(data);
	}

	// Predict performance values for the given dataset.
	public double[] predict(Instances data) throws Exception {
		if (data == null || data.numInstances() == 0) {
			throw new IllegalArgumentException("Dataset is empty or null.");
		}
		double[] predictions = new double[data.numInstances()];
		for (int i = 0; i < data.numInstances(); i++) {
			predictions[i] = model.classifyInstance(data.instance(i));
		}
		return predictions;
	}

	// Returns the trained Linear Regression model.
	public LinearRegression getModel() {
		return model;
	}
}
