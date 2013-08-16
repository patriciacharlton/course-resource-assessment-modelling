
package uk.ac.lkl.cram.ui;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import uk.ac.lkl.cram.model.AELMTest;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.ONE_SIZE_FOR_ALL;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.PERSONALISED;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.SOCIAL;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLActivity;

/**
 *
 * @author Bernard Horan
 */
public class LearningExperienceChartFactory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Learning Experience Test");
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
	float personalised = 0, social = 0, oneSizeForAll = 0;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity tla = lineItem.getActivity();
	    float total = lineItem.getTotalLearnerHourCount(m);
	    switch (tla.getLearningExperience()) {
		case PERSONALISED: {
		    personalised = personalised + (total * 100);
		    break;
		}
		case SOCIAL: {
		    social = social + (total * 100);
		    break;
		}
		case ONE_SIZE_FOR_ALL: {
		    oneSizeForAll = oneSizeForAll + (total * 100);
		    break;
		}
	    }
	}
	dataset.addValue(personalised, "Personalised", "Learning Experience");
	dataset.addValue(social, "Social", "Learning Experience");
	dataset.addValue(oneSizeForAll, "One Size For All", "Learning Experience");
	return dataset;
    }

    private static JFreeChart createChart(CategoryDataset dataset) {
	JFreeChart chart = ChartFactory.createStackedBarChart(null, null, null, dataset, PlotOrientation.HORIZONTAL, true, true, false);
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
	//plot.setInsets(new RectangleInsets(0.0D, 0.0D, 0.0D, 0.0D));
	plot.setAxisOffset(RectangleInsets.ZERO_INSETS);
	plot.setRangeGridlinesVisible(false);
	StackedBarRenderer sbRenderer = (StackedBarRenderer) plot.getRenderer();
	sbRenderer.setBarPainter(new StandardBarPainter());
	sbRenderer.setMaximumBarWidth(0.5);
	sbRenderer.setShadowVisible(true);
	sbRenderer.setRenderAsPercentages(true);
	CategoryAxis categoryAxis = plot.getDomainAxis();
	categoryAxis.setLowerMargin(0.20000000000000001D);
	categoryAxis.setUpperMargin(0.20000000000000001D);
	categoryAxis.setVisible(false);
	NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
	numberAxis.setVisible(false);
	LegendTitle legend = chart.getLegend();
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }
}
