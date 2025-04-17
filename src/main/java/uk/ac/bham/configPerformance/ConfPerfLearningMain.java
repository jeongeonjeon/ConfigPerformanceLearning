package uk.ac.bham.configPerformance;

import weka.core.Instances;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ConfPerfLearningMain {

    private static final String BASE_DIR = "src/datasets";
    private static final String RESULT_CSV = "src/result/results.csv";
    private static final String[] DATA_FOLDERS = { "batlik", "dconvert", "h2", "jump3r", "kanzi", "lrzip", "x264", "xz", "z3" };
    private static final double TRAIN_FRACTION = 0.7;
    // private static final int REPEATS = 3;
    private static final long SEED = 1L;

    public static void main(String[] args) {
        
        try {
        	
            File csvFile = new File(RESULT_CSV);
            if (csvFile.exists()) {
                if (csvFile.delete()) {
                    System.out.println("Old results.csv deleted.");
                } else {
                    System.err.println("Failed to delete existing results.csv.");
                }
            }
            
            // Load all datasets from the specified folders
            List<Instances> allDatasets = DataLoader.loadAllDatasetsFromFolders(BASE_DIR, DATA_FOLDERS, true);
            System.out.println("Total number of datasets loaded: " + allDatasets.size());

            File file = new File(RESULT_CSV);
            boolean fileExists = file.exists() && file.length() > 0;
            
            try (FileWriter writer = new FileWriter(file, true)) {
                if (!fileExists) {
                    writer.write("Dataset,Ridge,MAE,RMSE,MAPE\n");
                }

                for (Instances data : allDatasets) {
                	String datasetName = data.relationName().replaceAll("-weka\\.filters\\..*$", "");
                    System.out.println("Evaluating: " + datasetName);

                    runBaselineExperiment(data, datasetName, writer);
                    runTunedExperiment(data, datasetName, writer);
                }
                
                System.out.println("Save CSV");
                
                // Run T-Test after saving
                TTestHelper.runTTestFromCSV(RESULT_CSV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runBaselineExperiment(Instances data, String datasetName, FileWriter writer) throws Exception {
        double[] ridgeValues = {1e-8, 1e-4, 1.0, 10.0};
        for (double ridge : ridgeValues) {
            ConfPerfModel model = new ConfPerfModel(ridge);
            data.randomize(new Random(SEED));
            int trainSize = (int) Math.round(data.numInstances() * TRAIN_FRACTION);
            Instances train = new Instances(data, 0, trainSize);
            Instances test = new Instances(data, trainSize, data.numInstances() - trainSize);

            model.train(train);
            double[] result = evaluateModel(model, test);
            logResult(writer, datasetName, ridge, result);
        }
    }

    private static void runTunedExperiment(Instances data, String datasetName, FileWriter writer) throws Exception {
        TuningConfig config = new TuningConfig();
        double bestRidge = RandomSearchTuner.tuneRidgeParameter(data, config.iterations, config.folds, config.seed, config.low, config.high);
        ConfPerfModel model = new ConfPerfModel(bestRidge);

        data.randomize(new Random(SEED));
        int trainSize = (int) Math.round(data.numInstances() * TRAIN_FRACTION);
        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, data.numInstances() - trainSize);

        model.train(train);
        double[] result = evaluateModel(model, test);
        logResult(writer, datasetName, bestRidge, result);
    }

    private static double[] evaluateModel(ConfPerfModel model, Instances test) throws Exception {
        double[] actual = new double[test.numInstances()];
        double[] predicted = new double[test.numInstances()];
        for (int i = 0; i < test.numInstances(); i++) {
            actual[i] = test.instance(i).classValue();
            predicted[i] = model.predict(test.instance(i));
        }
        return new double[]{
            ErrorMetrics.calculateMAE(actual, predicted),
            ErrorMetrics.calculateRMSE(actual, predicted),
            ErrorMetrics.calculateMAPE(actual, predicted)
        };
    }

    private static void logResult(FileWriter writer, String datasetName, double ridgeValue, double[] metrics) throws IOException {
        writer.write(String.format("%s,%.8f,%.6f,%.6f,%.6f\n", datasetName, ridgeValue, metrics[0], metrics[1], metrics[2]));
    }
}

class TuningConfig {
    public int iterations = 30;
    public int folds = 10;
    public double low = 1.0e-8;
    public double high = 10.0;
    public long seed = 1L;
}