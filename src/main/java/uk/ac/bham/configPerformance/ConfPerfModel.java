package uk.ac.bham.configPerformance;

import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;

/**
 * Class for training and predicting configuration performance using a Linear Regression model
 * Change to a Ridge Regression model
 */
public class ConfPerfModel {

	private LinearRegression model;
	private double ridgeParameter;

	// Default constructor: uses a small default ridge parameter
//	public ConfPerfModel() {
//		//model = new LinearRegression();
//		this(100);
//	}
	
	// Constructor that allows setting a custom ridge parameter
    public ConfPerfModel(double ridgeParameter) {
        this.ridgeParameter = ridgeParameter;
        model = new LinearRegression();
        // Set the ridge parameter to enable L2 regularization
        model.setRidge(ridgeParameter);
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
	
	public double predict(Instance instance) throws Exception {
        return model.classifyInstance(instance);
    }

	// Returns the trained Linear Regression model.
	public LinearRegression getModel() {    	
		return model;
	}
}
