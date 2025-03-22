package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    /**
     * Loads all CSV files from the specified directory.
     * Each CSV file is loaded into an Instances object.
     * Optionally replaces missing values using Weka's ReplaceMissingValues filter.
     *
     * @param directoryPath the path to the directory containing CSV files.
     * @param replaceMissing if true, missing values are replaced.
     * @return a list of Instances, each corresponding to one CSV file.
     * @throws Exception if the directory doesn't exist or file loading fails.
     */
    public static List<Instances> loadAllDatasets(String directoryPath, boolean replaceMissing) throws Exception {
        List<Instances> datasets = new ArrayList<>();
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new Exception("Directory does not exist: " + directoryPath);
        }
        
        // List all files ending with ".csv"
        File[] csvFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
        if (csvFiles != null) {
            for (File csvFile : csvFiles) {
                CSVLoader loader = new CSVLoader();
                loader.setSource(csvFile);
                Instances data = loader.getDataSet();
                
                // Set the last attribute as the class attribute if not set
                if (data.classIndex() == -1) {
                    data.setClassIndex(data.numAttributes() - 1);
                }
                
                // Optionally, replace missing values
                if (replaceMissing) {
                    ReplaceMissingValues rmv = new ReplaceMissingValues();
                    rmv.setInputFormat(data);
                    data = Filter.useFilter(data, rmv);
                }
                
                datasets.add(data);
            }
        }
        return datasets;
    }
    
    /**
     * Loads all CSV datasets from multiple subdirectories.
     * It utilizes the loadAllDatasets method for each folder.
     *
     * @param baseDirectory the base directory containing subdirectories with CSV files.
     * @param folderNames an array of folder names.
     * @param replaceMissing if true, missing values are replaced.
     * @return a list of Instances from all specified folders.
     * @throws Exception if loading fails.
     */
    public static List<Instances> loadAllDatasetsFromFolders(String baseDirectory, String[] folderNames, boolean replaceMissing) throws Exception {
        List<Instances> allDatasets = new ArrayList<>();
        for (String folderName : folderNames) {
            String folderPath = baseDirectory + File.separator + folderName;
            List<Instances> datasets = loadAllDatasets(folderPath, replaceMissing);
            allDatasets.addAll(datasets);
        }
        return allDatasets;
    }
}
