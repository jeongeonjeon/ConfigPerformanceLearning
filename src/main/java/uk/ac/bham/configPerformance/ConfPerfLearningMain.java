package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;
import java.util.List;
import java.util.Random;

public class ConfPerfLearningMain {

	private static final String BASE_DIR = "src/datasets";
	private static final String[] DATA_FOLDERS = { "batlik", "dconvert", "h2", "jump3r", "kanzi", "lrzip", "x264", "xz",
			"z3" };
	private static final double TRAIN_FRACTION = 0.7;
	private static final int REPEATS = 3;
	private static final long SEED = 1L;

	public static void main(String[] args) {
	    
		try {
			// Load all datasets from the specified folders
			List<Instances> allDatasets = DataLoader.loadAllDatasetsFromFolders(BASE_DIR, DATA_FOLDERS, true);
			System.out.println("Total number of datasets loaded: " + allDatasets.size());

			// Exit if no dataset is loaded
			if (allDatasets.isEmpty()) {
				System.out.println("No datasets loaded.");
				return;
			}

			for (int i = 0; i < allDatasets.size(); i++) {
				Instances data = allDatasets.get(i);
				String datasetName = data.relationName();

				double totalMAPE = 0.0;
				double totalMAE = 0.0;
				double totalRMSE = 0.0;
				

				System.out.println((i + 1) + ". System: " + getSystemNameFromDataset(data) + ", Dataset: " + datasetName + ", Training data fraction: " + TRAIN_FRACTION + ", Number of repeats: " + REPEATS);

				// Baseline experiments with fixed ridge values
				double[] baselineRidgeValues = { 1.0E-8, 1.0E-5, 1.0, 10.0 };
				for (double baseRidge : baselineRidgeValues) {
					double sumMAE = 0.0, sumRMSE = 0.0, sumMAPE = 0.0;

					for (int rep = 0; rep < REPEATS; rep++) {
						Instances randData = new Instances(data);
						randData.randomize(new Random(SEED + rep));

						int trainSize = (int) Math.round(randData.numInstances() * TRAIN_FRACTION);
						int testSize = randData.numInstances() - trainSize;

						Instances train = new Instances(randData, 0, trainSize);
						Instances test = new Instances(randData, trainSize, testSize);

						ConfPerfModel baselineModel = new ConfPerfModel(baseRidge);
						try {
							baselineModel.train(train);
						} catch (Exception ex) {
							continue;
						}

						double[] predictions = baselineModel.predict(test);
						double[] actual = test.attributeToDoubleArray(test.classIndex());

						sumMAE += ErrorMetrics.calculateMAE(actual, predictions);
						sumRMSE += ErrorMetrics.calculateRMSE(actual, predictions);
						sumMAPE += ErrorMetrics.calculateMAPE(actual, predictions);
					}

					double avgMAE = sumMAE / REPEATS;
					double avgRMSE = sumRMSE / REPEATS;
					double avgMAPE = sumMAPE / REPEATS;

					System.out.printf("[Baseline] Ridge = %.2g | Avg MAE = %.6f | Avg RMSE = %.6f | Avg MAPE = %.6f\n", baseRidge, avgMAE, avgRMSE, avgMAPE);
				}
				
				// Tune best ridge parameter
				double bestRidge = RandomSearchTuner.tuneRidgeParameter(data, 30, 10, SEED, 1.0E-8, 10.0);

				for (int rep = 0; rep < REPEATS; rep++) {
					Instances randData = new Instances(data);
					randData.randomize(new Random(SEED + rep));

					int trainSize = (int) Math.round(randData.numInstances() * TRAIN_FRACTION);
					int testSize = randData.numInstances() - trainSize;

					Instances train = new Instances(randData, 0, trainSize);
					Instances test = new Instances(randData, trainSize, testSize);

					ConfPerfModel model = new ConfPerfModel(bestRidge);
					model.train(train);

					double[] predictions = model.predict(test);
					double[] actual = test.attributeToDoubleArray(test.classIndex());

					totalMAE += ErrorMetrics.calculateMAE(actual, predictions);
					totalRMSE += ErrorMetrics.calculateRMSE(actual, predictions);
					totalMAPE += ErrorMetrics.calculateMAPE(actual, predictions);
				}

				double avgMAPE = totalMAPE / REPEATS;
				double avgMAE = totalMAE / REPEATS;
				double avgRMSE = totalRMSE / REPEATS;
				
				System.out.printf("[Tuned] Best Ridge = %.6f | Avg MAE = %.6f | Avg RMSE = %.6f | Avg MAPE = %.6f\n", bestRidge, avgMAE, avgRMSE, avgMAPE);
				System.out.println();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 임시 메소드: 데이터셋에서 시스템 이름을 추출하는 방법(필요에 따라 구현)
	private static String getSystemNameFromDataset(Instances data) {
		// 예를 들어, 데이터셋의 relationName()에 시스템명이 포함되어 있다고 가정
		// "batlik_corona" 같은 형식일 경우, "_" 이전의 문자열을 반환
		String name = data.relationName();
		if (name.contains("_")) {
			return name.substring(0, name.indexOf("_"));
		}
		return name;
	}
}
