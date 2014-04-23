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
import java.awt.Font;
import java.awt.Paint;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.SortOrder;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class is a factory that produces an instance of class ChartPanel. The ChartPanel 
 * presents a display of learner feedback for a module, rendered in the form of 
 * a bar chart. The factory is responsible for setting up all the parameters of
 * the chart, including the underlying dataset, legend, colours, and so on.
 * @see org.jfree.chart.ChartPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class FeedbackChartFactory {
    private static final Logger LOGGER = Logger.getLogger(FeedbackChartFactory.class.getName());
    //The colours for the bars
    private static final Color TUTOR_COLOR = new Color(0x5E9300);
    private static final Color PEER_ONLY_COLOR = new Color(0xCC005D);
    private static final Color TEL_COLOR = new Color(0x057797);


    /**
     * For testing purposes only
     * @param args the command line arguments (ignored)
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Feedback Chart Test");
	Module m = AELMTest.populateModule();
	ChartPanel chartPanel = createChartPanel(m);
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    /**
     * Create a new instance of ChartPanel from the module provided.
     * @param m the module containing data about the learner feedback for the module's activities
     * @return a chart panel containing a stacked bar chart on the learner feedback of the module's activities
     */
    public static ChartPanel createChartPanel(Module m) {
        //Create a dataset from the module
        CategoryDataset dataset = createDataSet(m);
        //Create a chart from the dataset
        JFreeChart chart = createChart(dataset);
        //Create a chartpanel to render the chart
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    /**
     * Create a dataset from the module
     * @param module the module containing data about the learner feedback for the module's activities
     * @return a category dataset that is used to produce a bar chart
     */
    private static CategoryDataset createDataSet(final Module module) {
	//Create a dataset to hold the data
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	//populate the dataset with the data
        populateDataset(dataset, module);
        //Create a listener, which repopulates the dataset when anything changes
        final PropertyChangeListener feedbackListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		//LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
		populateDataset(dataset, module);
	    }
	};
	//Add the listener to each of the module's tlaLineItems, as well as to each
        //tlaLineItem's activity
        //This means that whenever a tlaLineItem changes, or its activity changes, the listener is triggered
        //Causing the dataset to be repopulated
        for (TLALineItem lineItem : module.getTLALineItems()) {
	    //LOGGER.info("adding listeners to : " + lineItem.getName());
	    lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_LEARNER_FEEDBACK, feedbackListener);
	    lineItem.addPropertyChangeListener(feedbackListener);
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
			lineItem.getActivity().removePropertyChangeListener(TLActivity.PROP_LEARNER_FEEDBACK, feedbackListener);
                        lineItem.removePropertyChangeListener(feedbackListener);
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			TLALineItem lineItem = (TLALineItem) pce.getNewValue();
			//So add a listener to it and its activity
			//LOGGER.info("adding listeners to: " + lineItem);
			lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_LEARNER_FEEDBACK, feedbackListener);
                        lineItem.addPropertyChangeListener(feedbackListener);
		    }
		}
		//Assume the dataset is now out of date, so repopulate it
		populateDataset(dataset, module);
	    }
	});
	return dataset;
    }
    

    /**
     * Create a chart from the provided category dataset
     * @param dataset a category data set populated with the learner feedback for the module's activities
     * @return a Chart that can be rendered in a ChartPanel
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        //Create a vertical bar chart from the chart factory, with no title, no axis labels, a legend, tooltips but no URLs
	JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
        //Get the background colour from the platform UI
        Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
	//Get the font from the platform UI
        Font chartFont = UIManager.getFont("Label.font");
        //Set the background colour of the chart
	chart.setBackgroundPaint(backgroundPaint);
        //Get the plot from the chart
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
        //Set the background colour of the plot
	plot.setBackgroundPaint(backgroundPaint);
        //Don't render an oultine around the bar chart
	plot.setOutlineVisible(false);
        //Get the renderer from the plot
	BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
	//Set the rendered to use a standard bar painter (nothing fancy)
        barRenderer.setBarPainter(new StandardBarPainter());
	//Set the colours for the bars
	barRenderer.setSeriesPaint(0, PEER_ONLY_COLOR);
	barRenderer.setSeriesPaint(1, TEL_COLOR);
	barRenderer.setSeriesPaint(2, TUTOR_COLOR);
        //Set the tooltip to be series, category and value
        barRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{0} ({2} hours)", NumberFormat.getInstance()));
        //Get the category axis (that's the X-axis in this case)
	CategoryAxis categoryAxis = plot.getDomainAxis();
        //Set the font for rendering the labels on the x-axis to be the platform default
	categoryAxis.setLabelFont(chartFont);
	//Hide the tick marks and labels for the x-axis
	categoryAxis.setTickMarksVisible(false);
	categoryAxis.setTickLabelsVisible(false);
	//Set the label for the x-axis
	categoryAxis.setLabel("Feedback to individuals or group");	
	//Get the number axis (that's the Y-axis in this case)
	NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        //Use the same font as the x-axis
	numberAxis.setLabelFont(chartFont);
	//Set the label for the vertical axis
	numberAxis.setLabel("Hours");
        //Get the legend
	LegendTitle legend = chart.getLegend();
        //Use the same font on the legend as for the chart
	legend.setItemFont(chartFont);
        //Set the legend to use the same background colour as the chart
	legend.setBackgroundPaint(backgroundPaint);
        //No frame around the legend
	legend.setFrame(BlockBorder.NONE);
        //Locate the legend on the right of the chart
	legend.setPosition(RectangleEdge.RIGHT);
        //Sort the items in the legend
        legend.setSortOrder(SortOrder.DESCENDING);
	return chart;
    }

    private static void populateDataset(DefaultCategoryDataset dataset, Module m) {
	//Create running totals
        float peer_only = 0, tel = 0, tutor = 0;
        //Enumerate all the tlaLineItems
	for (TLALineItem lineItem : m.getTLALineItems()) {
            //get the activity for each line item
	    TLActivity tla = lineItem.getActivity();
            //Get the number of hours spent on this activity
	    float total = lineItem.getTotalLearnerHourCount(m);
            //Depending on the learner feedback, update the corresponding running total
	    switch (tla.getLearnerFeedback()) {
		case PEER_ONLY: {
		    peer_only += total;
		    break;
		}
		case TEL: {
		    tel += total;
		    break;
		}
		case TUTOR: {
		    tutor += total;
		    break;
		}
	    }
	}
        //Set the values in the dataset
	dataset.setValue(peer_only, "Peer", "Learner Feedback");
	dataset.setValue(tel, "Computer-based", "Learner Feedback");
	dataset.setValue(tutor, "Tutor", "Learner Feedback");
    }
}
