
package uk.ac.lkl.cram.ui;

import java.awt.Color;
import java.awt.Paint;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
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
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class LearningTypeChartFactory {
    private static final Logger LOGGER = Logger.getLogger(LearningTypeChartFactory.class.getName());

    static final Color ACQUISITION_COLOR = new Color(101, 220, 241);  
    static final Color DISCUSSION_COLOR = new Color(121, 173, 236);  
    static final Color INQUIRY_COLOR = new Color(250, 128, 128);  
    static final Color PRACTICE_COLOR = new Color(190, 152, 221);  
    static final Color PRODUCTION_COLOR = new Color(188, 234, 117); 
    static final Color COLLABORATION_COLOR = new Color(0xFFCD00);


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

    private static PieDataset createDataSet(final Module module) {
	final DefaultPieDataset dataset = new DefaultPieDataset();
	populateDataset(dataset, module);
	final PropertyChangeListener learningTypeListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		LOGGER.info("property change: " + pce);
		populateDataset(dataset, module);
	    }
	};
	
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    LOGGER.info("adding listeners to : " + lineItem.getName());
	    lineItem.getActivity().getLearningType().addPropertyChangeListener(learningTypeListener);
	    lineItem.addPropertyChangeListener(learningTypeListener);
	}
	module.addPropertyChangeListener(Module.PROP_TLA_LINEITEM, new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		if (pce instanceof IndexedPropertyChangeEvent) {
		    LOGGER.info("indexed change: " + pce);
		    if (pce.getOldValue() != null) {
			//This has been removed
			TLALineItem lineItem = (TLALineItem) pce.getOldValue();
			LOGGER.info("removing listeners from: " + lineItem.getName());
			lineItem.getActivity().getLearningType().removePropertyChangeListener(learningTypeListener);
                        lineItem.removePropertyChangeListener(learningTypeListener);
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			TLALineItem lineItem = (TLALineItem) pce.getNewValue();
			LOGGER.info("adding listeners to: " + lineItem);
			lineItem.getActivity().getLearningType().addPropertyChangeListener(learningTypeListener);
                        lineItem.addPropertyChangeListener(learningTypeListener);
		    }
		}
		populateDataset(dataset, module);
	    }
	});
	
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
        plot.setSectionPaint("Collaboration", COLLABORATION_COLOR);
	plot.setSectionPaint("Discusssion", DISCUSSION_COLOR);
	plot.setSectionPaint("Inquiry", INQUIRY_COLOR);
	plot.setSectionPaint("Practice", PRACTICE_COLOR);
	plot.setSectionPaint("Production", PRODUCTION_COLOR);
	LegendTitle legend = chart.getLegend();
	legend.setItemFont(UIManager.getFont("Label.font"));
	legend.setBackgroundPaint(backgroundPaint);
	legend.setFrame(BlockBorder.NONE);
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }

    private static void populateDataset(DefaultPieDataset dataset, Module m) {
	dataset.setValue("Acquisition", getTotalAcquisition(m));
	dataset.setValue("Collaboration", getTotalCollaboration(m));
	dataset.setValue("Discusssion", getTotalDiscussion(m));
	dataset.setValue("Inquiry", getTotalInquiry(m));
	dataset.setValue("Practice", getTotalPractice(m));
	dataset.setValue("Production", getTotalProduction(m));
    }
}
