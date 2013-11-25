
package uk.ac.lkl.cram.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.PieSectionEntity;
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
    
    private final static String ACQUISITION = "Acquisition";
    private final static String COLLABORATION = "Collaboration";
    private final static String DISCUSSION = "Discussion";
    private final static String INQUIRY = "Inquiry";
    private final static String PRACTICE = "Practice";
    private final static String PRODUCTION = "Production";
    

    static final Color ACQUISITION_COLOR = new Color(101, 220, 241);  
    static final Color DISCUSSION_COLOR = new Color(121, 173, 236);  
    static final Color INQUIRY_COLOR = new Color(250, 128, 128);  
    static final Color PRACTICE_COLOR = new Color(190, 152, 221);  
    static final Color PRODUCTION_COLOR = new Color(188, 234, 117); 
    static final Color COLLABORATION_COLOR = new Color(0xFFCD00);
    
    private static Map<String, Set<TLALineItem>> learningTypeMap = new WeakHashMap<String, Set<TLALineItem>>();


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
    
    public static ChartPanel createChartPanel(final Module m) {
        final Cursor crossHair = new Cursor(Cursor.CROSSHAIR_CURSOR);
        final Cursor normal = new Cursor(Cursor.DEFAULT_CURSOR);
	PieDataset dataset = createDataSet(m);
	JFreeChart chart = createChart(dataset);
	final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                MouseEvent trigger = cme.getTrigger();
                if (trigger.getButton() == MouseEvent.BUTTON1) {
                    if (cme.getEntity() instanceof PieSectionEntity) {
                        PieSectionEntity pieSection = (PieSectionEntity) cme.getEntity();
                        Set<TLALineItem> relevantTLAs = learningTypeMap.get(pieSection.getSectionKey().toString());
                        ChartPopupDialog popup = new ChartPopupDialog((Frame) SwingUtilities.getWindowAncestor(chartPanel), true);
                        popup.setTLAs(relevantTLAs);
                        popup.setTitle("Activities with " + pieSection.getSectionKey().toString());
                        popup.setVisible(true);                       
                    }
                }
                    
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                if (cme.getEntity() instanceof PieSectionEntity) {
                    chartPanel.setCursor(crossHair);
                } else {
                    chartPanel.setCursor(normal);
    }
            }
        });
	return chartPanel;
    }

    private static PieDataset createDataSet(final Module module) {
	final DefaultPieDataset dataset = new DefaultPieDataset();
	populateDataset(dataset, module);
	final PropertyChangeListener learningTypeListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		//LOGGER.info("property change: " + pce);
		populateDataset(dataset, module);
	    }
	};
	
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    //LOGGER.info("adding listeners to : " + lineItem.getName());
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
            int acquisition = activity.getLearningType().getAcquisition();
            if (acquisition > 0) {
                total += lineItem.getTotalLearnerHourCount(m) * acquisition;
                learningTypeMap.get(ACQUISITION).add(lineItem);              
            }
	}
	return total;
    }
    
    public static float getTotalInquiry(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
            int inquiry = activity.getLearningType().getInquiry();
            if (inquiry > 0) {
                total += lineItem.getTotalLearnerHourCount(m) * inquiry;
                learningTypeMap.get(INQUIRY).add(lineItem);
            }
	}
	return total;
    }
    
    public static float getTotalDiscussion(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
            int discussion = activity.getLearningType().getDiscussion();
            if (discussion > 0) {
                total += lineItem.getTotalLearnerHourCount(m) * discussion;
                learningTypeMap.get(DISCUSSION).add(lineItem);
            }
	}
	return total;
    }
    
    public static float getTotalPractice(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
            int practice = activity.getLearningType().getPractice();
            if (practice > 0) {
                total += lineItem.getTotalLearnerHourCount(m) * practice;
                learningTypeMap.get(PRACTICE).add(lineItem);
            }
	}
	return total;
    }
    
    public static float getTotalProduction(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
            int production = activity.getLearningType().getProduction();
            if (production > 0) {
                total += lineItem.getTotalLearnerHourCount(m) * production;
                learningTypeMap.get(PRODUCTION).add(lineItem);
            }
	}
	return total;
    }
    
    public static float getTotalCollaboration(Module m) {
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity activity = lineItem.getActivity();
            int collaboration = activity.getLearningType().getCollaboration();
            if (collaboration > 0) {
                total += lineItem.getTotalLearnerHourCount(m) * collaboration;
                learningTypeMap.get(COLLABORATION).add(lineItem);
            }
	}
	return total;
    }

    private static JFreeChart createChart(PieDataset dataset) {
	Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
	JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
	chart.setBackgroundPaint(backgroundPaint);
	PiePlot plot = (PiePlot) chart.getPlot();
	plot.setBackgroundPaint(backgroundPaint);
	plot.setShadowXOffset(0);
	plot.setShadowYOffset(0);
	plot.setOutlineVisible(false);
	plot.setLabelGenerator(null);
	plot.setSectionPaint(ACQUISITION, ACQUISITION_COLOR);
        plot.setSectionPaint(COLLABORATION, COLLABORATION_COLOR);
	plot.setSectionPaint(DISCUSSION, DISCUSSION_COLOR);
	plot.setSectionPaint(INQUIRY, INQUIRY_COLOR);
	plot.setSectionPaint(PRACTICE, PRACTICE_COLOR);
	plot.setSectionPaint(PRODUCTION, PRODUCTION_COLOR);
	LegendTitle legend = chart.getLegend();
	legend.setItemFont(UIManager.getFont("Label.font"));
	legend.setBackgroundPaint(backgroundPaint);
	legend.setFrame(BlockBorder.NONE);
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }

    private static void populateDataset(DefaultPieDataset dataset, Module m) {
        initializeMap();
	dataset.setValue(ACQUISITION, getTotalAcquisition(m));
	dataset.setValue(COLLABORATION, getTotalCollaboration(m));
	dataset.setValue(DISCUSSION, getTotalDiscussion(m));
	dataset.setValue(INQUIRY, getTotalInquiry(m));
	dataset.setValue(PRACTICE, getTotalPractice(m));
	dataset.setValue(PRODUCTION, getTotalProduction(m));
    }

    private static void initializeMap() {
        Comparator<TLALineItem> tlaLineItemComparator = new Comparator<TLALineItem>() {

            @Override
            public int compare(TLALineItem a, TLALineItem b) {
                return a.getName().compareTo(b.getName());
            }
        };
        learningTypeMap.put(ACQUISITION, new TreeSet<TLALineItem>(tlaLineItemComparator));
	learningTypeMap.put(COLLABORATION, new TreeSet<TLALineItem>(tlaLineItemComparator));
	learningTypeMap.put(DISCUSSION, new TreeSet<TLALineItem>(tlaLineItemComparator));
	learningTypeMap.put(INQUIRY, new TreeSet<TLALineItem>(tlaLineItemComparator));
	learningTypeMap.put(PRACTICE, new TreeSet<TLALineItem>(tlaLineItemComparator));
	learningTypeMap.put(PRODUCTION, new TreeSet<TLALineItem>(tlaLineItemComparator));
    }
}
