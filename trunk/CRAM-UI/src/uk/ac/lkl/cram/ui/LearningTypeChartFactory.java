
package uk.ac.lkl.cram.ui;

import java.awt.Color;
import java.awt.Paint;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
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
 * $Date$
 * @author Bernard Horan
 */
public class LearningTypeChartFactory {
    private static final Color ACQUISITION_COLOR = new Color(101, 220, 241);  
    private static final Color DISCUSSION_COLOR = new Color(121, 173, 236);  
    private static final Color INQUIRY_COLOR = new Color(250, 128, 128);  
    private static final Color PRACTICE_COLOR = new Color(190, 152, 221);  
    private static final Color PRODUCTION_COLOR = new Color(188, 234, 117);  


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
	dataset.setValue("Collaboration", getTotalCollaboration(m));
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
    
    public static float getTotalCollaboration(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
	    total += lineItem.getTotalLearnerHourCount(m) * activity.getLearningType().getCollaboration();
	}
	return total;
    }

    private static JFreeChart createChart(PieDataset dataset) {
	Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
	JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
	chart.setBackgroundPaint(backgroundPaint);
	PiePlot plot = (PiePlot) chart.getPlot();
	plot.setBackgroundPaint(backgroundPaint);
	plot.setOutlineVisible(false);
	plot.setLabelGenerator(null);
	plot.setSectionPaint("Acquisition", ACQUISITION_COLOR);
	plot.setSectionPaint("Inquiry", INQUIRY_COLOR);
	plot.setSectionPaint("Discusssion", DISCUSSION_COLOR);
	plot.setSectionPaint("Practice", PRACTICE_COLOR);
	plot.setSectionPaint("Production", PRODUCTION_COLOR);
	LegendTitle legend = chart.getLegend();
	legend.setItemFont(UIManager.getFont("Label.font"));
	legend.setBackgroundPaint(backgroundPaint);
	legend.setFrame(BlockBorder.NONE);
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }
}
