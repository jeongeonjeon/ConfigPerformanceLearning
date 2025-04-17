package uk.ac.bham.configPerformance;

import weka.core.Instance;
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
	
    public static double[] evaluate(ConfPerfModel model, Instances X, Instances y) throws Exception {
        int folds = 10;
        int numInstances = X.numInstances();

        double totalMAE = 0.0;
        double totalRMSE = 0.0;
        double totalMAPE = 0.0;

        // Combine X and y into one dataset
        Instances data = new Instances(X);
        for (int i = 0; i < numInstances; i++) {
            data.instance(i).setClassValue(y.instance(i).classValue());
        }

        data.randomize(new Random(1));
        data.stratify(folds);

        for (int i = 0; i < folds; i++) {
            Instances train = data.trainCV(folds, i);
            Instances test = data.testCV(folds, i);

            model.train(train);

            double foldMAE = 0.0;
            double foldRMSE = 0.0;
            double foldMAPE = 0.0;

            for (int j = 0; j < test.numInstances(); j++) {
                Instance instance = test.instance(j);
                double actual = instance.classValue();
                double predicted = model.predict(instance);

                double error = Math.abs(actual - predicted);
                foldMAE += error;
                foldRMSE += error * error;
                foldMAPE += (actual != 0.0) ? Math.abs(error / actual) : 0.0;
            }

            int n = test.numInstances();
            totalMAE += foldMAE / n;
            totalRMSE += Math.sqrt(foldRMSE / n);
            totalMAPE += foldMAPE / n;
        }

        return new double[] {
            totalMAE / folds,
            totalRMSE / folds,
            totalMAPE / folds
        };
    }
}
