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

import java.awt.Paint;
import javax.swing.UIManager;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.Dataset;
import org.jfree.ui.RectangleEdge;
import uk.ac.lkl.cram.model.Module;

/**
 * An abstract class to create charts.
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
public abstract class AbstractChartMaker {
    /**
     * The chart panel created by the chart maker
     */
    protected final ChartPanel chartPanel;
    protected final Dataset dataset;
    protected final Module module;
    
    /**
     * Create a chart maker from the module
     * @param module the module from which to create the chart maker
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AbstractChartMaker(Module module) {
        this.module = module;
        dataset = createDataSet();
        chartPanel = createChartPanel();
    }
    
    /**
     * Create the chart panel from a module
     * @param m the module from which to create the chart
     * @return the chart panel created from the module
     */
    private ChartPanel createChartPanel() {
        //Create the chart from the dataset
        JFreeChart chart = createChart();
        setChartDefaults(chart);
        //Create the chartpanel to render the chart
        return new ChartPanel(chart);
    }

    /**
     * Create a dataset from the module
     * @return the dataset created from the module
     */
    protected abstract Dataset createDataSet();

    /**
     * Create a chart from a dataset 
     * @return a chart created from the dataset
     */
    protected abstract JFreeChart createChart();
    
    /**
     * Apply some defaults to a chart, including background paint
     * and font.
     * @param chart the chart to which defaults should be applied
     */
    private void setChartDefaults(JFreeChart chart) {
        Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
        //Set the background colour of the chart
        chart.setBackgroundPaint(backgroundPaint);
        //Set the background colour of the plot to be the same as the chart
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(backgroundPaint);
        //No outline around the bars
        plot.setOutlineVisible(false);
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
    }
    
    /**
     * Access the chart panel created by the maker
     * @return the chart panel created by the maker
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }
    
    /**
     * Return the dataset that is used to create the chart
     * @return the dataset underlying the chart
     */
    public Dataset getDataset() {
        return dataset;
    }
}
