
package uk.ac.lkl.cram.ui;

import java.awt.Font;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.SortOrder;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;
import uk.ac.lkl.cram.model.TLALineItem;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class HoursChartFactory {
    private static final Logger LOGGER = Logger.getLogger(HoursChartFactory.class.getName());


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Hours Chart Test");
	Module m = AELMTest.populateModule();
	ChartPanel chartPanel = createChartPanel(m);
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    public static ChartPanel createChartPanel(Module m) {
	CategoryDataset dataset = createDataSet(m);
	JFreeChart chart = createChart(dataset);
	ChartPanel chartPanel = new ChartPanel(chart);
	return chartPanel;
    }

    private static CategoryDataset createDataSet(final Module module) {
	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	populateDataset(dataset, module);
	final PropertyChangeListener presentationListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		//LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
		populateDataset(dataset, module);
	    }
	};
	for (ModulePresentation modulePresentation : module.getModulePresentations()) {
	    modulePresentation.addPropertyChangeListener(ModulePresentation.PROP_STUDENT_COUNT, presentationListener);
	    for (TLALineItem lineItem : module.getTLALineItems()) {
		//LOGGER.info("adding listener to : " + lineItem.getName());
		SupportTime st = lineItem.getSupportTime(modulePresentation);
		st.addPropertyChangeListener(presentationListener);
		PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
		pt.addPropertyChangeListener(presentationListener);
	    }
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
			for (ModulePresentation modulePresentation : module.getModulePresentations()) {
			    SupportTime st = lineItem.getSupportTime(modulePresentation);
			    st.removePropertyChangeListener(presentationListener);
			    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
			    pt.removePropertyChangeListener(presentationListener);
			}			
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			TLALineItem lineItem = (TLALineItem) pce.getNewValue();
			LOGGER.info("adding listeners to: " + lineItem);
			for (ModulePresentation modulePresentation : module.getModulePresentations()) {
			    SupportTime st = lineItem.getSupportTime(modulePresentation);
			    st.addPropertyChangeListener(presentationListener);
			    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
			    pt.addPropertyChangeListener(presentationListener);
			}
		    }
		}
		populateDataset(dataset, module);
	    }
	});
	return dataset;
    }
    

    private static JFreeChart createChart(CategoryDataset dataset) {
	Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
	Font chartFont = UIManager.getFont("Label.font");
	JFreeChart chart = ChartFactory.createStackedBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
	chart.setBackgroundPaint(backgroundPaint);
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
	plot.setBackgroundPaint(backgroundPaint);
	plot.setOutlineVisible(false);
	StackedBarRenderer sbRenderer = (StackedBarRenderer) plot.getRenderer();
	sbRenderer.setBarPainter(new StandardBarPainter());
	sbRenderer.setShadowVisible(true);
	CategoryAxis categoryAxis = plot.getDomainAxis();
	categoryAxis.setLabelFont(chartFont);
	NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
	numberAxis.setLabelFont(chartFont);
	LegendTitle legend = chart.getLegend();
	legend.setItemFont(chartFont);
	legend.setBackgroundPaint(backgroundPaint);
	legend.setFrame(BlockBorder.NONE);
	legend.setPosition(RectangleEdge.RIGHT);
        legend.setSortOrder(SortOrder.DESCENDING);
	return chart;
    }

    private static void populateDataset(DefaultCategoryDataset dataset, Module m) {
	String[] presentationNames = {"Run 1", "Run 2", "Run 3"};
	int i = 0;
	for (ModulePresentation modulePresentation : m.getModulePresentations()) {
	    dataset.setValue(m.getTotalPreparationHours(modulePresentation), "Preparation Hours", presentationNames[i]);
            dataset.setValue(m.getTotalSupportHours(modulePresentation), "Support Hours", presentationNames[i]);
            i++;
	}
    }
}
