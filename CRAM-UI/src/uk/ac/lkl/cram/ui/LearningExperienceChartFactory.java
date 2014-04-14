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
import java.awt.Paint;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;
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
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class is a factory that produces an instance of class ChartPanel. The ChartPanel 
 * presents a display of the learning experiences for a module, rendered in the form of 
 * a stacked bar chart. The factory is responsible for setting up all the parameters of
 * the chart, including the underlying dataset, legend, colours, and so on.
 * @see org.jfree.chart.ChartPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class LearningExperienceChartFactory {   
    private static final Logger LOGGER = Logger.getLogger(LearningExperienceChartFactory.class.getName());
    //The number formatter for the tooltips
    private static final NumberFormat FORMATTER = NumberFormat.getPercentInstance();
    
    //The colours for the bars
    private static final Color ONE_SIZE_FITS_ALL_COLOR = new Color(166, 166, 166);
    private static final Color PERSONALISED_COLOR = new Color(230, 185, 184);
    private static final Color SOCIAL_COLOR = new Color(252, 213,181);

    /**
     * For testing purposes only
     * @param args the command line arguments (ignored)
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Learning Experience Test");
	Module m = AELMTest.populateModule();
	ChartPanel chartPanel = createChartPanel(m);
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    /**
     * Create a new instance of ChartPanel from the module provided. 
     * @param m the module containing teaching-learning activities
     * @return a chart panel containing a stacked bar chart on the learning experiences of the module's activities
     */
    public static ChartPanel createChartPanel(Module m) {
        //Create the dataset
	CategoryDataset dataset = createDataSet(m);
        //Create the chart from the dataset
	JFreeChart chart = createChart(dataset);
        //Create the chartpanel to render the chart
	ChartPanel chartPanel = new ChartPanel(chart);
	return chartPanel;
    }

    /**
     * Create a dataset from the module
     * @param module the module containing the teaching-learning activities
     * @return a category dataset that is used to produce a stacked bar chart
     */
    private static CategoryDataset createDataSet(final Module module) {
	//Create the dataset to hold the data
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	//Populate the dataset with the data
        populateDataset(dataset, module);
        //Create a listener, which repopulates the dataset when anything changes
        final PropertyChangeListener learningExperienceListener = new PropertyChangeListener() {

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
	    lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_LEARNING_EXPERIENCE, learningExperienceListener);
	    lineItem.addPropertyChangeListener(learningExperienceListener);
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
			lineItem.getActivity().removePropertyChangeListener(TLActivity.PROP_LEARNING_EXPERIENCE, learningExperienceListener);
                        lineItem.removePropertyChangeListener(learningExperienceListener);
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			TLALineItem lineItem = (TLALineItem) pce.getNewValue();
			//So add a listener to it and its activity
			//LOGGER.info("adding listeners to: " + lineItem);
			lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_LEARNING_EXPERIENCE, learningExperienceListener);
                        lineItem.addPropertyChangeListener(learningExperienceListener);
		    }
		}
		//Assume the dataset is now out of date, so repopulate it
		populateDataset(dataset, module);
	    }
	});
	return dataset;
    }

    /**
     * Create a chart from the provide category dataset
     * @param dataset a category data set populated with the learning experiences for the module
     * @return a Chart that can be rendered in a ChartPanel
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        //Create a horizontal stacked bar chart from the chart factory, with no title, no axis labels, a legend, tooltips but no URLs
        JFreeChart chart = ChartFactory.createStackedBarChart(null, null, null, dataset, PlotOrientation.HORIZONTAL, true, true, false);
        //Set the background colour of the chart
	Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
	chart.setBackgroundPaint(backgroundPaint);
	//Get the plot from the chart
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
	//Set the background colour of the plot to be the same as the chart
	plot.setBackgroundPaint(backgroundPaint);
	//No outline around the bars
        plot.setOutlineVisible(false);
        //Remove offsets from the plot
	plot.setInsets(RectangleInsets.ZERO_INSETS); 
	plot.setAxisOffset(RectangleInsets.ZERO_INSETS); 
        //Hide the range lines
	plot.setRangeGridlinesVisible(false);
        //Get the renderer for the plot
	StackedBarRenderer sbRenderer = (StackedBarRenderer) plot.getRenderer();
	//Set the painter for the renderer (nothing fancy)
        sbRenderer.setBarPainter(new StandardBarPainter());
	//sbRenderer.setItemMargin(0.5); //Makes no difference
	//reduces width of bar as proportion of overall width
        sbRenderer.setMaximumBarWidth(0.5); 
        //Render the bars as percentages
	sbRenderer.setRenderAsPercentages(true);
        //Set the colours for the bars
	sbRenderer.setSeriesPaint(0, PERSONALISED_COLOR);
	sbRenderer.setSeriesPaint(1, SOCIAL_COLOR);
	sbRenderer.setSeriesPaint(2, ONE_SIZE_FITS_ALL_COLOR);
        //Set the tooltips to render percentages
        sbRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator() {

            @Override
            public String generateToolTip(CategoryDataset cd, int row, int column) {
                //Only interested in row, as there's only one column
                //TODO--really inefficient
                @SuppressWarnings("unchecked")
                List<Comparable> rows = cd.getRowKeys();
                Comparable columnKey = cd.getColumnKey(column);
                //Sum running total
                int total = 0;
                for (Comparable comparable : rows) {
                    total += cd.getValue(comparable, columnKey).intValue();
                }
                //Get the value for the row (in our case the learning type)
                Comparable rowKey = cd.getRowKey(row);
                float value = cd.getValue(rowKey, columnKey).floatValue();
                //The tooltip is the value of the learning type divided by the total, expressed as a percentage
                return cd.getRowKey(row) + " (" + FORMATTER.format(value / total) + ")";
            }
        });
        //Hide both axes
	CategoryAxis categoryAxis = plot.getDomainAxis();
	//categoryAxis.setCategoryMargin(0.5D);//Makes no difference
	categoryAxis.setVisible(false);
	NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
	numberAxis.setVisible(false);
        //Get the legend
	LegendTitle legend = chart.getLegend();
        //Set the font of the legend to be the same as the platform
	legend.setItemFont(UIManager.getFont("Label.font"));
        //Set the background colour of the legend to be the same as the platform
	legend.setBackgroundPaint(backgroundPaint);
        //No frame around the legend
	legend.setFrame(BlockBorder.NONE);
        //Locate the legend to the right of the chart
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }

    /**
     * Populate the dataset with the values from the module.
     * @param dataset the dataset to be populated
     * @param m the module containing the learning types 
     */
    private static void populateDataset(DefaultCategoryDataset dataset, Module m) {
	//Create running totals
        float personalised = 0, social = 0, oneSizeForAll = 0;
        //Enumerate all the tlaLineItems
	for (TLALineItem lineItem : m.getTLALineItems()) {
            //get the activity for each line item
	    TLActivity tla = lineItem.getActivity();
            //Get the number of hours spent on this activity
	    float total = lineItem.getTotalLearnerHourCount(m);
            //Dependding on the learning type, update the corresponding running total
	    switch (tla.getLearningExperience()) {
		case PERSONALISED: {
		    personalised += (total * 100);
		    break;
		}
		case SOCIAL: {
		    social += (total * 100);
		    break;
		}
		case ONE_SIZE_FOR_ALL: {
		    oneSizeForAll += (total * 100);
		    break;
		}
	    }
	}
        //Set the values in the dataset
	dataset.setValue(personalised, "Personalised", "Learning Experience");
	dataset.setValue(social, "Social", "Learning Experience");
	dataset.setValue(oneSizeForAll, "Same for All", "Learning Experience");
    }
}
