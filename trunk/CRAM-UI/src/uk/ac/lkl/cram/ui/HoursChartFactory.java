
package uk.ac.lkl.cram.ui;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 *
 * @author Bernard Horan
 */
public class HoursChartFactory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Hours Chart Test");
	Module m = AELMTest.populateModule();
	ChartPanel chartPanel = createChartPanel(m);
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    public static ChartPanel createChartPanel(Module m) {
	CategoryDataset dataset = createDataSet(m);
	JFreeChart chart = createChart(dataset);
	ChartPanel chartPanel = new ChartPanel(chart);
	return chartPanel;
    }

    private static CategoryDataset createDataSet(Module m) {
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	String[] presentationNames = {"1st Run", "2nd Run", "Stable State"};
	int i = 0;
	for (ModulePresentation modulePresentation : m.getModulePresentations()) {
	    dataset.addValue(m.getTotalSupportHours(modulePresentation), "Support Hours", presentationNames[i]);
	    dataset.addValue(m.getTotalPreparationHours(modulePresentation), "Preparation Hours", presentationNames[i]);
	    i++;
	}
	return dataset;
    }
    

    private static JFreeChart createChart(CategoryDataset dataset) {
	JFreeChart chart = ChartFactory.createStackedBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
	StackedBarRenderer sbRenderer = (StackedBarRenderer) plot.getRenderer();
	sbRenderer.setBarPainter(new StandardBarPainter());
	sbRenderer.setShadowVisible(true);
	LegendTitle legend = chart.getLegend();
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }
}
