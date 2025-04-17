package uk.ac.bham.configPerformance;

import weka.core.Instances;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.VisualizePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

/**
 * CustomDataVisualization class visualizes a Weka Instances dataset by allowing the user to specify
 * which attributes to plot on the X and Y axes. This approach uses SwingUtilities.invokeLater to update
 * the GUI in the event dispatch thread, simplifying thread management while offering customizable plots.
 */
public class DataVisualization {

    /**
     * Visualizes a scatter plot using the specified attributes for the X and Y axes.
     *
     * @param data   the dataset to visualize
     * @param xIndex the index of the attribute to be used for the X axis
     * @param yIndex the index of the attribute to be used for the Y axis
     */
    public static void visualizeScatter(final Instances data, final int xIndex, final int yIndex) {
        if (data == null || data.numInstances() == 0) {
            System.out.println("No data available for visualization.");
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Create PlotData2D object with the dataset.
                PlotData2D plotData = new PlotData2D(data);
                // Customize plot name to indicate which attributes are used.
                String plotName = data.relationName() + " [X: " + data.attribute(xIndex).name() 
                        + ", Y: " + data.attribute(yIndex).name() + "]";
                plotData.setPlotName(plotName);

                // Create the VisualizePanel and add the plot data.
                VisualizePanel vp = new VisualizePanel();
                vp.setName("Custom Scatter Plot");
                vp.addPlot(plotData);

                // Create and set up the JFrame.
                JFrame frame = new JFrame("Custom Data Visualization - " + data.relationName());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(800, 600);
                frame.getContentPane().add(vp, BorderLayout.CENTER);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
