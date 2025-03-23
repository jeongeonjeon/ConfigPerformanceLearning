package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading datasets from CSV files.
 */
public class DataLoader {

	// Load all CSV files from the specified directory.
	// Each CSV file is converted into an Instances object.
	public static List<Instances> loadAllDatasets(String directoryPath, boolean replaceMissing) throws Exception {
		List<Instances> datasets = new ArrayList<>();
		File dir = new File(directoryPath);
		if (!dir.exists() || !dir.isDirectory()) {
			throw new Exception("Directory does not exist: " + directoryPath);
		}

		// List all files with ".csv" extension
		File[] csvFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
		if (csvFiles != null) {
			for (File csvFile : csvFiles) {
				try {
					CSVLoader loader = new CSVLoader();
					loader.setSource(csvFile);
					Instances data = loader.getDataSet();

					// If class attribute is not set, set the last attribute as the class attribute
					if (data.classIndex() == -1) {
						data.setClassIndex(data.numAttributes() - 1);
					}

					// Replace missing values if required
					if (replaceMissing) {
						ReplaceMissingValues rmv = new ReplaceMissingValues();
						rmv.setInputFormat(data);
						data = Filter.useFilter(data, rmv);
					}

					datasets.add(data);
					//System.out.println("Loaded dataset: " + csvFile.getName() + " with " + data.numInstances() + " instances.");
				} catch (Exception ex) {
					System.out.println("Failed to load dataset from file: " + csvFile.getName());
					ex.printStackTrace();
				}
			}
		}
		return datasets;
	}

	// Load all CSV datasets from multiple subdirectories.
	public static List<Instances> loadAllDatasetsFromFolders(String baseDirectory, String[] folderNames,
			boolean replaceMissing) throws Exception {
		List<Instances> allDatasets = new ArrayList<>();
		for (String folderName : folderNames) {
			String folderPath = baseDirectory + File.separator + folderName;
			try {
				List<Instances> datasets = loadAllDatasets(folderPath, replaceMissing);
				allDatasets.addAll(datasets);
			} catch (Exception ex) {
				System.out.println("Error loading datasets from folder: " + folderName);
				ex.printStackTrace();
			}
		}
		return allDatasets;
	}
}
