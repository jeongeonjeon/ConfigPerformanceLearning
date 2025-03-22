package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import java.util.Random;

public class CrossValidator {
    // Perform k-fold cross-validation on the given model and dataset
    public static Evaluation performCrossValidation(LinearRegression model, Instances data, int folds, long seed) throws Exception {
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(model, data, folds, new Random(seed));
        return eval;
    }
}