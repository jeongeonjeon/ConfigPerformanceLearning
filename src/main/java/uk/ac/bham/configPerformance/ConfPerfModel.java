package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;

public class ConfPerfModel {
    private LinearRegression model;

    public ConfPerfModel() {
        model = new LinearRegression();
    }

    // Train the model with the given dataset
    public void train(Instances data) throws Exception {
        model.buildClassifier(data);
    }

    // Predict performance values for the given dataset
    public double[] predict(Instances data) throws Exception {
        double[] predictions = new double[data.numInstances()];
        for (int i = 0; i < data.numInstances(); i++) {
            predictions[i] = model.classifyInstance(data.instance(i));
        }
        return predictions;
    }

    public LinearRegression getModel() {
        return model;
    }
}