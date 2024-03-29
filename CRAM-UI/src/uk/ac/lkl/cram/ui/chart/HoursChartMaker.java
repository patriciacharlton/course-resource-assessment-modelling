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
import java.awt.Font;
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
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModuleLineItem;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class produces an instance of class ChartPanel. The ChartPanel 
 * presents a display of the preparation and support hours for a module, rendered in the form of 
 * a stacked bar chart. The factory is responsible for setting up all the parameters of
 * the chart, including the underlying dataset, legend, colours, and so on.
 * @see org.jfree.chart.ChartPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class HoursChartMaker extends AbstractChartMaker {
    private static final Logger LOGGER = Logger.getLogger(HoursChartMaker.class.getName());
    //The colours for the bars
    private static final Color SUPPORT_COLOR = new Color(0xD90505);
    private static final Color PREPARATION_COLOR = new Color(0x3F9CD8);


    /**
     * For testing purposes only
     * @param args the command line arguments (ignored)
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Hours Chart Test");
	Module m = AELMTest.populateModule();
	HoursChartMaker maker = new HoursChartMaker(m);
	frame.setContentPane(maker.getChartPanel());
	frame.setVisible(true);
    }
    
    /**
     * Create an hours chart maker from a module
     * @param m the module from which to create a chart maker
     */
    public HoursChartMaker(Module m) {
        super(m);
    }

    /**
     * Create a dataset from the module
     * @return a category dataset that is used to produce a stacked bar chart
     */
    @Override
    protected CategoryDataset createDataSet() {
	//Create a dataset to hold the data
        final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
	//populate the dataset with the data
        populateDataset(categoryDataset, module);
        //Create a listener, which repopulates the dataset when anything changes
        final PropertyChangeListener presentationListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		//LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
		populateDataset(categoryDataset, module);
	    }
	};
	//Add the listener to each of the module presentations
        for (ModulePresentation modulePresentation : module.getModulePresentations()) {
	    //Listen for when the number of home or overseas students changes
            //This means when the number of home or overseas students changes, the dataset will be repopulated
            modulePresentation.addPropertyChangeListener(ModulePresentation.PROP_HOME_STUDENT_COUNT, presentationListener);
            modulePresentation.addPropertyChangeListener(ModulePresentation.PROP_OVERSEAS_STUDENT_COUNT, presentationListener);
	    
            //Add the listener to the support time and preparation time of 
            //each of the module's tlaLineItems, for each presentation
            //This means that whenever a support time or a preparation time changes,
            //or its activity changes, the listener is triggered
            //Causing the dataset to be repopulated
            //Also add the listener to the activity for the tlaLineItem so that
            //when the tutor group size changes, the chart is updated
            for (TLALineItem lineItem : module.getTLALineItems()) {
		//LOGGER.info("adding listener to : " + lineItem.getName());
		SupportTime st = lineItem.getSupportTime(modulePresentation);
		st.addPropertyChangeListener(presentationListener);
		PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
		pt.addPropertyChangeListener(presentationListener);
                lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_MAX_GROUP_SIZE, presentationListener);
	    }
            //Add the listener to the support time of each of the module's 
            //module line items, for each presentation
            for (ModuleLineItem lineItem : module.getModuleItems()) {
		//LOGGER.info("adding listener to : " + lineItem.getName());
		SupportTime st = lineItem.getSupportTime(modulePresentation);
		st.addPropertyChangeListener(presentationListener);
	    }
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
                        //So remove the listener from the preparation and support time
                        //for each presentation of this line item
			//LOGGER.info("removing listeners from: " + lineItem.getName());
			for (ModulePresentation modulePresentation : module.getModulePresentations()) {
			    SupportTime st = lineItem.getSupportTime(modulePresentation);
			    st.removePropertyChangeListener(presentationListener);
			    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
			    pt.removePropertyChangeListener(presentationListener);
			}
                        //Also remove the listener from the activity
                        lineItem.removePropertyChangeListener(TLActivity.PROP_MAX_GROUP_SIZE, presentationListener);
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			TLALineItem lineItem = (TLALineItem) pce.getNewValue();
                        //So add the listener to the preparation and support time
                        //For each presentation of this line item
			//LOGGER.info("adding listeners to: " + lineItem);
			for (ModulePresentation modulePresentation : module.getModulePresentations()) {
			    SupportTime st = lineItem.getSupportTime(modulePresentation);
			    st.addPropertyChangeListener(presentationListener);
			    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
			    pt.addPropertyChangeListener(presentationListener);
			}
                        //Also add the listener to the activity
                        lineItem.addPropertyChangeListener(TLActivity.PROP_MAX_GROUP_SIZE, presentationListener);
		    }
		}
                //Assume the dataset is now out of date
		populateDataset(categoryDataset, module);
	    }
	});
        //Do the same as above for module line items
        module.addPropertyChangeListener(Module.PROP_MODULE_LINEITEM, new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		if (pce instanceof IndexedPropertyChangeEvent) {
		    //LOGGER.info("indexed change: " + pce);
		    if (pce.getOldValue() != null) {
			//This has been removed
			ModuleLineItem lineItem = (ModuleLineItem) pce.getOldValue();
			//LOGGER.info("removing listeners from: " + lineItem.getName());
			for (ModulePresentation modulePresentation : module.getModulePresentations()) {
			    SupportTime st = lineItem.getSupportTime(modulePresentation);
			    st.removePropertyChangeListener(presentationListener);
			}			
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			ModuleLineItem lineItem = (ModuleLineItem) pce.getNewValue();
			//LOGGER.info("adding listeners to: " + lineItem);
			for (ModulePresentation modulePresentation : module.getModulePresentations()) {
			    SupportTime st = lineItem.getSupportTime(modulePresentation);
			    st.addPropertyChangeListener(presentationListener);
			}
		    }
		}
		populateDataset(categoryDataset, module);
	    }
	});
        //Add the listner to be triggered if the tutor group size of the module changes
        module.addPropertyChangeListener(Module.PROP_GROUP_SIZE, presentationListener);
	return categoryDataset;
    }
    

    /**
     * Create a chart from the provided category dataset
     * @return a Chart that can be rendered in a ChartPanel
     */
    @Override
    protected JFreeChart createChart() {
        //Create a vertical stacked bar chart from the chart factory, with no title, no axis labels, a legend, tooltips but no URLs
        JFreeChart chart = ChartFactory.createStackedBarChart(null, null, null, (CategoryDataset) dataset, PlotOrientation.VERTICAL, true, true, false);
        //Get the font from the platform UI
        Font chartFont = UIManager.getFont("Label.font");
        //Get the plot from the chart
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
        //Get the renderer from the plot
	StackedBarRenderer sbRenderer = (StackedBarRenderer) plot.getRenderer();
	//Set the rendered to use a standard bar painter (nothing fancy)
        sbRenderer.setBarPainter(new StandardBarPainter());
	//Set the colours for the bars
	sbRenderer.setSeriesPaint(0, PREPARATION_COLOR);
	sbRenderer.setSeriesPaint(1, SUPPORT_COLOR);
        //Set the tooltip to be series, category and value
        sbRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{0}, {1} ({2})", NumberFormat.getInstance()));
        //Get the category axis (that's the X-axis in this case)
	CategoryAxis categoryAxis = plot.getDomainAxis();
        //Set the font for rendering the labels on the x-axis to be the platform default
	categoryAxis.setLabelFont(chartFont);
        //Get the number axis (that's the Y-axis in this case)
	NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        //Use the same font as the x-axis
	numberAxis.setLabelFont(chartFont);
        return chart;
    }

    private void populateDataset(DefaultCategoryDataset dataset, Module m) {
	String[] presentationNames = {"Run 1", "Run 2", "Run 3"};
	int i = 0;
	for (ModulePresentation modulePresentation : m.getModulePresentations()) {
	    dataset.setValue(m.getTotalPreparationHours(modulePresentation), "Preparation Hours", presentationNames[i]);
            dataset.setValue(m.getTotalSupportHours(modulePresentation), "Support Hours", presentationNames[i]);
            i++;
	}
    }
}
