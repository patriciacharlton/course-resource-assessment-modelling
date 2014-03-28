/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.lkl.cram.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.Point;
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
import org.jfree.chart.labels.StandardPieToolTipGenerator;
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
 * This class is a factory that produces an instance of class ChartPanel. The ChartPanel 
 * presents a display of the learning types for a module, rendered in the form of 
 * a pie chart. The factory is responsible for setting up all the parameters of
 * the chart, including the underlying dataset, legend, colours, and so on.
 * @see org.jfree.chart.ChartPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class LearningTypeChartFactory {
    private static final Logger LOGGER = Logger.getLogger(LearningTypeChartFactory.class.getName());
    
    //Labels for the segments of the pie chart
    private final static String ACQUISITION = "Acquisition";
    private final static String COLLABORATION = "Collaboration";
    private final static String DISCUSSION = "Discussion";
    private final static String INQUIRY = "Inquiry";
    private final static String PRACTICE = "Practice";
    private final static String PRODUCTION = "Production";
    
    //Colours for the pie chart
    static final Color ACQUISITION_COLOR = new Color(101, 220, 241);  
    static final Color DISCUSSION_COLOR = new Color(121, 173, 236);  
    static final Color INQUIRY_COLOR = new Color(250, 128, 128);  
    static final Color PRACTICE_COLOR = new Color(190, 152, 221);  
    static final Color PRODUCTION_COLOR = new Color(188, 234, 117); 
    static final Color COLLABORATION_COLOR = new Color(0xFFCD00);
    
    //map between learning type and TLALineItems with an activity that includes that learning type
    private static final Map<String, Set<TLALineItem>> learningTypeMap = new WeakHashMap<>();


    /**
     * For testing purposes only
     * @param args the command line arguments (ignored)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Learning Type Test");
	Module m = AELMTest.populateModule();
	ChartPanel chartPanel = createChartPanel(m);
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    /**
     * Create a new instance of ChartPanel from the module provided. 
     * @param m the module containing teaching-learning activities
     * @return a chart panel containing a pie chart on the learning types of the module's activities
     */
    public static ChartPanel createChartPanel(final Module m) {
        //Create cursors
        final Cursor crossHair = new Cursor(Cursor.CROSSHAIR_CURSOR);
        final Cursor normal = new Cursor(Cursor.DEFAULT_CURSOR);
        //Create the dataset from the module
	PieDataset dataset = createDataSet(m);
        //Create the chart from the dataset
	JFreeChart chart = createChart(dataset);
        //Create a chart panel to render the chart
	final ChartPanel chartPanel = new ChartPanel(chart);
        //Add a mouselistener, listening for a single click on a segment of the pie
        chartPanel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                //Get the mouse event
                MouseEvent trigger = cme.getTrigger();
                //Test if the mouse event is a left-button
                if (trigger.getButton() == MouseEvent.BUTTON1) {
                    //Check that the mouse click is on a segment of the pie
                    if (cme.getEntity() instanceof PieSectionEntity) {
                        //Get the selected segment of the pie
                        PieSectionEntity pieSection = (PieSectionEntity) cme.getEntity();
                        //Get the key that corresponds to that segment--this is a learning type
                        String key = pieSection.getSectionKey().toString();
                        //Get the set of tlalineitems whose activity contains that learning type
                        Set<TLALineItem> relevantTLAs = learningTypeMap.get(key);
                        //Create a pop up dialog containing that set of tlalineitems
                        LearningTypePopupDialog popup = new LearningTypePopupDialog((Frame) SwingUtilities.getWindowAncestor(chartPanel), true, relevantTLAs, key);
                        //Set the title of the popup to indicate which learning type was selected
                        popup.setTitle("Activities with " + key);
                        //Centre the popup at the location of the mouse click
                        Point location = trigger.getLocationOnScreen();
                        int w = popup.getWidth();
                        int h = popup.getHeight();
                        popup.setLocation(location.x - w/2, location.y - h/2);
                        popup.setVisible(true);                       
                    }
                }                   
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //Set the cursor shape according to the location of the cursor
                if (cme.getEntity() instanceof PieSectionEntity) {
                    chartPanel.setCursor(crossHair);
                } else {
                    chartPanel.setCursor(normal);
                }
            }
        });
	return chartPanel;
    }

    /**
     * Create a dataset from the module
     * @param module the module containing the teaching-learning activities
     * @return a pie dataset that is used to produce a pie chart
     */
    private static PieDataset createDataSet(final Module module) {
        //Create the dataset to hold the data
	final DefaultPieDataset dataset = new DefaultPieDataset();
        //Populate the dataset with the data
	populateDataset(dataset, module);
        //Create a listener, which repopulates the dataset when anything changes
	final PropertyChangeListener learningTypeListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		//LOGGER.info("property change: " + pce);
		populateDataset(dataset, module);
	    }
	};
	
	//Add the listener to each of the module's tlaLineItems, as well as to each
        //tlaLineItem's activity
        //This means that whenever a tlaLineItem changes, or its activity changes, the listener is triggered
        //Causing the dataset to be repopulated
        for (TLALineItem lineItem : module.getTLALineItems()) {
	    //LOGGER.info("adding listeners to : " + lineItem.getName());
	    lineItem.getActivity().getLearningType().addPropertyChangeListener(learningTypeListener);
	    lineItem.addPropertyChangeListener(learningTypeListener);
	}
        //Add a listener to the module, listening for changes where a tlaLineItem is added or removed
	module.addPropertyChangeListener(Module.PROP_TLA_LINEITEM, new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
                //A tlaLineItem has been added or removed
		if (pce instanceof IndexedPropertyChangeEvent) {
		    //LOGGER.info("indexed change: " + pce);
		    if (pce.getOldValue() != null) {
			//This has been removed
			TLALineItem lineItem = (TLALineItem) pce.getOldValue();
                        //So remove the listener from it and its activity
			//LOGGER.info("removing listeners from: " + lineItem.getName());
			lineItem.getActivity().getLearningType().removePropertyChangeListener(learningTypeListener);
                        lineItem.removePropertyChangeListener(learningTypeListener);
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			TLALineItem lineItem = (TLALineItem) pce.getNewValue();
                        //So add a listener to it and its activity
			//LOGGER.info("adding listeners to: " + lineItem);
			lineItem.getActivity().getLearningType().addPropertyChangeListener(learningTypeListener);
                        lineItem.addPropertyChangeListener(learningTypeListener);
		    }
		}
                //Assume the dataset is now out of date, so repopulate it
		populateDataset(dataset, module);
	    }
	});
	
	return dataset;
    }
    
    /**
     * Return the total 'amount' of acquisition for all the 
     * activities in the module
     * @param m the module containing the activities
     * @return a float representing all the amount of acquisition learning type
     * for all the activities in the module
     */
    private static float getTotalAcquisition(Module m) {
        //Running total
	float total = 0f;
	for (TLALineItem lineItem : m.getTLALineItems()) {
            //Get the activity for each line item
	    TLActivity activity = lineItem.getActivity();
            //Get the amount of acquisition for the activity (a percentage 0 <= x <= 100)
            int acquisition = activity.getLearningType().getAcquisition();
            //If the acquisition is non-zero
            if (acquisition > 0) {
                //multiply the acquisition by the number of student hours and add to the running total
                total += lineItem.getTotalLearnerHourCount(m) * acquisition;
                //Add the line item to the set of line items whose actitivity includes acquisition
                learningTypeMap.get(ACQUISITION).add(lineItem);              
            }
	}
	return total;
    }
    
    /**
     * @see LearningTypeChartFactory#getTotalAcquisition(Module) 
     */
    private static float getTotalInquiry(Module m) {
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
    
    /**
     * @see LearningTypeChartFactory#getTotalAcquisition(Module) 
     */
    private static float getTotalDiscussion(Module m) {
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
    
    /**
     * @see LearningTypeChartFactory#getTotalAcquisition(Module) 
     */
    private static float getTotalPractice(Module m) {
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
    
    /**
     * @see LearningTypeChartFactory#getTotalAcquisition(Module) 
     */
    private static float getTotalProduction(Module m) {
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
    
    /**
     * @see LearningTypeChartFactory#getTotalAcquisition(Module) 
     */
    private static float getTotalCollaboration(Module m) {
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

    /**
     * Create a chart from the provide pie dataset
     * @param dataset a pie data set populated with the learning types for the module
     * @return a Chart that can be rendered in a ChartPanel
     */
    private static JFreeChart createChart(PieDataset dataset) {
        //Create a pie chart from the chart factory with no title, a legend and tooltips
        JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
        //Set the background colour of the chart
	Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
	chart.setBackgroundPaint(backgroundPaint);
        //Get the plot from the chart
	PiePlot plot = (PiePlot) chart.getPlot();
        //Set the tooltip generator, to be "Practice (12%)"
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0} ({2})"));
        //Set the background colour of the plot to be the same as the chart
	plot.setBackgroundPaint(backgroundPaint);
        //Remove shadows from the plot
	plot.setShadowXOffset(0);
	plot.setShadowYOffset(0);
        //Remove the outline from the plot
	plot.setOutlineVisible(false);
        //Remove the labels from the plot
	plot.setLabelGenerator(null);
        //Set the colours for the segments
	plot.setSectionPaint(ACQUISITION, ACQUISITION_COLOR);
        plot.setSectionPaint(COLLABORATION, COLLABORATION_COLOR);
	plot.setSectionPaint(DISCUSSION, DISCUSSION_COLOR);
	plot.setSectionPaint(INQUIRY, INQUIRY_COLOR);
	plot.setSectionPaint(PRACTICE, PRACTICE_COLOR);
	plot.setSectionPaint(PRODUCTION, PRODUCTION_COLOR);
        //Get the legend from the chart
	LegendTitle legend = chart.getLegend();
        //Set the font of the legend to be the same as the platform UI
	legend.setItemFont(UIManager.getFont("Label.font"));
        //Set the background colour of the legend to be the same as the chart
	legend.setBackgroundPaint(backgroundPaint);
        //Remove the border from the legend
	legend.setFrame(BlockBorder.NONE);
        //Locate the legend to the right of the plot
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
        //Create a comparator that will sort by name of tlaLineItem
        Comparator<TLALineItem> tlaLineItemComparator = new Comparator<TLALineItem>() {

            @Override
            public int compare(TLALineItem a, TLALineItem b) {
                return a.getName().compareTo(b.getName());
            }
        };
        //set the keys and values of the map
        learningTypeMap.put(ACQUISITION, new TreeSet<>(tlaLineItemComparator));
	learningTypeMap.put(COLLABORATION, new TreeSet<>(tlaLineItemComparator));
	learningTypeMap.put(DISCUSSION, new TreeSet<>(tlaLineItemComparator));
	learningTypeMap.put(INQUIRY, new TreeSet<>(tlaLineItemComparator));
	learningTypeMap.put(PRACTICE, new TreeSet<>(tlaLineItemComparator));
	learningTypeMap.put(PRODUCTION, new TreeSet<>(tlaLineItemComparator));
    }
}
