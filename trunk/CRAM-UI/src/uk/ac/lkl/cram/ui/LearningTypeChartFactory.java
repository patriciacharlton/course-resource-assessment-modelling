
package uk.ac.lkl.cram.ui;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLActivity;

/**
 *
 * @author Bernard Horan
 */
public class LearningTypeChartFactory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Learning Type Test");
	Module m = AELMTest.populateModule();
	ChartPanel chartPanel = createChartPanel(m);
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    public static ChartPanel createChartPanel(Module m) {
	PieDataset dataset = createDataSet(m);
	JFreeChart chart = createChart(dataset);
	ChartPanel chartPanel = new ChartPanel(chart);
	return chartPanel;
    }

    private static PieDataset createDataSet(Module m) {
	DefaultPieDataset dataset = new DefaultPieDataset();
	dataset.setValue("Acquisition", getTotalAcquisition(m));
	dataset.setValue("Inquiry", getTotalInquiry(m));
	dataset.setValue("Discusssion", getTotalDiscussion(m));
	dataset.setValue("Practice", getTotalPractice(m));
	dataset.setValue("Production", getTotalProduction(m));
	return dataset;
    }
    
    public static float getTotalAcquisition(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
	    total += lineItem.getTotalLearnerHourCount(m) * activity.getLearningType().getAcquisition();
	}
	return total;
    }
    
    public static float getTotalInquiry(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
	    total += lineItem.getTotalLearnerHourCount(m) * activity.getLearningType().getInquiry();
	}
	return total;
    }
    
    public static float getTotalDiscussion(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
	    total += lineItem.getTotalLearnerHourCount(m) * activity.getLearningType().getDiscussion();
	}
	return total;
    }
    
    public static float getTotalPractice(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
	    total += lineItem.getTotalLearnerHourCount(m) * activity.getLearningType().getPractice();
	}
	return total;
    }
    
    public static float getTotalProduction(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
	    total += lineItem.getTotalLearnerHourCount(m) * activity.getLearningType().getProduction();
	}
	return total;
    }

    private static JFreeChart createChart(PieDataset dataset) {
	JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
	PiePlot plot = (PiePlot) chart.getPlot();
	plot.setLabelGenerator(null);
	LegendTitle legend = chart.getLegend();
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }
}
