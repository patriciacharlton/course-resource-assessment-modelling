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
package uk.ac.lkl.cram.ui.chart;

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
import uk.ac.lkl.cram.model.LearningType;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class is a factory that produces an instance of class ChartPanel. The ChartPanel 
 * presents a display of the learning types for a tla, rendered in the form of 
 * a pie chart. The factory is responsible for setting up all the parameters of
 * the chart, including the underlying dataset, legend, colours, and so on.
 * This class is used to provide an image for the predefined TLAs, and thus
 * requires no tooltips.
 * @see org.jfree.chart.ChartPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class TLALearningTypeChartFactory {

    /**
     * For testing purposes only
     * @param args the command line arguments (ignored)
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("TLA Learning Type Test");
	TLActivity tla = AELMTest.populateModule().getTLALineItems().get(0).getActivity();
	ChartPanel chartPanel = createChartPanel(tla);
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    /**
     * Create a new instance of ChartPanel from the tla provided. 
     * @param tla the tla that has learning types
     * @return a chart panel containing a pie chart on the learning types of the activity
     */
    static ChartPanel createChartPanel(TLActivity tla) {
	JFreeChart chart = createChart(tla);
	ChartPanel chartPanel = new ChartPanel(chart);
	return chartPanel;
    }
    
    /**
     * Create a dataset from the tla
     * @param tla the tla containing the learning type
     * @return a pie dataset that is used to produce a pie chart
     */
    private static PieDataset createDataSet(TLActivity tla) {
        final LearningType lt = tla.getLearningType();      
	//Create the dataset to hold the data
	final DefaultPieDataset dataset = new DefaultPieDataset();
	//Populate the dataset with the data
	populateDataset(dataset, lt);
        return dataset;
    }

    private static JFreeChart createChart(PieDataset dataset) {
	//Create a pie chart from the chart factory with no title, a legend and no tooltips
        JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, false, false);
        //Set the background colour of the chart
	Paint backgroundPaint = Color.white;
        chart.setBackgroundPaint(backgroundPaint);
	//Get the plot from the chart
	PiePlot plot = (PiePlot) chart.getPlot();
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
	plot.setSectionPaint(LearningTypeChartMaker.ACQUISITION, LearningTypeChartMaker.ACQUISITION_COLOR);
        plot.setSectionPaint(LearningTypeChartMaker.COLLABORATION, LearningTypeChartMaker.COLLABORATION_COLOR);
	plot.setSectionPaint(LearningTypeChartMaker.DISCUSSION, LearningTypeChartMaker.DISCUSSION_COLOR);
	plot.setSectionPaint(LearningTypeChartMaker.INQUIRY, LearningTypeChartMaker.INQUIRY_COLOR);
	plot.setSectionPaint(LearningTypeChartMaker.PRACTICE, LearningTypeChartMaker.PRACTICE_COLOR);
	plot.setSectionPaint(LearningTypeChartMaker.PRODUCTION, LearningTypeChartMaker.PRODUCTION_COLOR);
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

    private static void populateDataset(DefaultPieDataset dataset, LearningType lt) {
        dataset.setValue(LearningTypeChartMaker.ACQUISITION, lt.getAcquisition());
	dataset.setValue(LearningTypeChartMaker.COLLABORATION, lt.getCollaboration());
        dataset.setValue(LearningTypeChartMaker.DISCUSSION, lt.getDiscussion());
        dataset.setValue(LearningTypeChartMaker.INQUIRY, lt.getInquiry());
        dataset.setValue(LearningTypeChartMaker.PRACTICE, lt.getPractice());
        dataset.setValue(LearningTypeChartMaker.PRODUCTION, lt.getProduction());
    }

    /**
     * Create a new instance of Chart from the tla provided. 
     * @param tla the tla that has learning types
     * @return a chart containing a pie chart on the learning types of the activity
     */
    public static JFreeChart createChart(TLActivity tla) {
	PieDataset dataset = createDataSet(tla);
	JFreeChart chart = createChart(dataset);
	return chart;
    }
    
    
}
