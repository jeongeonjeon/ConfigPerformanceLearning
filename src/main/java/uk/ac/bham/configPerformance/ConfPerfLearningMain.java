package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.classifiers.Evaluation;
import java.util.List;

public class ConfPerfLearningMain {

    private static final String BASE_DIR = "src/datasets";
    private static final String[] DATA_FOLDERS = { "batlik", "dconvert", "h2", "jump3r", "kanzi", "lrzip", "x264", "xz", "z3" };

    public static void main(String[] args) {
        try {
            // 1. Load all datasets from the specified folders
            List<Instances> allDatasets = DataLoader.loadAllDatasetsFromFolders(BASE_DIR, DATA_FOLDERS, true);
            System.out.println("Total number of datasets loaded: " + allDatasets.size());

            // 2. Exit if no dataset is loaded
            if (allDatasets.isEmpty()) {
                System.out.println("No datasets loaded.");
                return;
            }
            
            // 3. Select the first dataset for demonstration
            Instances data = allDatasets.get(0);
            System.out.println("Using dataset from folder: " + data.relationName());

            // 4. Train the performance model using the selected dataset
            ConfPerfModel confPerfModel = new ConfPerfModel();
            confPerfModel.train(data);
            System.out.println("Trained Linear Regression Model:");
            System.out.println(confPerfModel.getModel());

            // 5. Evaluate the model using 10-fold cross-validation
            Evaluation eval = CrossValidator.performCrossValidation(confPerfModel.getModel(), data, 10, 1);
            System.out.println("Cross-Validation Evaluation Summary:");
            System.out.println(eval.toSummaryString());
            System.out.println("Correlation Coefficient: " + eval.correlationCoefficient());
            System.out.println("Mean Absolute Error (MAE): " + eval.meanAbsoluteError());
            System.out.println("Root Mean Squared Error (RMSE): " + eval.rootMeanSquaredError());

            // 6. Perform predictions on the dataset and compute error metrics
            double[] predictions = confPerfModel.predict(data);
            double[] actual = data.attributeToDoubleArray(data.classIndex());
            double mae = ErrorMetrics.calculateMAE(actual, predictions);
            double rmse = ErrorMetrics.calculateRMSE(actual, predictions);
            System.out.println("Calculated MAE: " + mae);
            System.out.println("Calculated RMSE: " + rmse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
