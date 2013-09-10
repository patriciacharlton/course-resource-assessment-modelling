
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
import org.jfree.ui.RectangleInsets;
import uk.ac.lkl.cram.model.AELMTest;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.ONE_SIZE_FOR_ALL;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.PERSONALISED;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.SOCIAL;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class LearningExperienceChartFactory {   
    private static final Logger LOGGER = Logger.getLogger(LearningExperienceChartFactory.class.getName());

    private static final Color ONE_SIZE_FITS_ALL_COLOR = new Color(242,242,242);
    private static final Color PERSONALISED_COLOR = new Color(128,128,128);
    private static final Color SOCIAL_COLOR = new Color(42,42,42);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Learning Experience Test");
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
	final PropertyChangeListener learningExperienceListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		LOGGER.info("property change: " + pce);
		populateDataset(dataset, module);
	    }
	};
	
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    LOGGER.info("adding listener to : " + lineItem.getName());
	    lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_LEARNING_EXPERIENCE, learningExperienceListener);
	    
	}
	module.addPropertyChangeListener(new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		if (pce instanceof IndexedPropertyChangeEvent) {
		    LOGGER.info("indexed change: " + pce);
		    if (pce.getOldValue() != null) {
			//This has been removed
			TLALineItem lineItem = (TLALineItem) pce.getOldValue();
			LOGGER.info("removing listener from: " + lineItem.getName());
			lineItem.getActivity().removePropertyChangeListener(TLActivity.PROP_LEARNING_EXPERIENCE, learningExperienceListener);
		    }
		    if (pce.getNewValue() != null) {
			//This has been added
			TLALineItem lineItem = (TLALineItem) pce.getNewValue();
			LOGGER.info("adding listener to: " + lineItem);
			lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_LEARNING_EXPERIENCE, learningExperienceListener);
		    }
		}
		populateDataset(dataset, module);
	    }
	});
	return dataset;
    }

    private static JFreeChart createChart(CategoryDataset dataset) {
	Paint backgroundPaint = UIManager.getColor("InternalFrame.background");
	JFreeChart chart = ChartFactory.createStackedBarChart(null, null, null, dataset, PlotOrientation.HORIZONTAL, true, true, false);
	chart.setBackgroundPaint(backgroundPaint);
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
	plot.setBackgroundPaint(backgroundPaint);
	plot.setOutlineVisible(false);
	plot.setInsets(RectangleInsets.ZERO_INSETS); //Keep
	plot.setAxisOffset(RectangleInsets.ZERO_INSETS); //Keep
	plot.setRangeGridlinesVisible(false);
	StackedBarRenderer sbRenderer = (StackedBarRenderer) plot.getRenderer();
	sbRenderer.setBarPainter(new StandardBarPainter());
	//sbRenderer.setItemMargin(0.5); //Makes no difference
	//sbRenderer.setMaximumBarWidth(0.25); //reduces width of bar as proportion of overall width
	sbRenderer.setShadowVisible(true);
	sbRenderer.setRenderAsPercentages(true);
	sbRenderer.setSeriesPaint(0, PERSONALISED_COLOR);
	sbRenderer.setSeriesPaint(1, SOCIAL_COLOR);
	sbRenderer.setSeriesPaint(2, ONE_SIZE_FITS_ALL_COLOR);
	CategoryAxis categoryAxis = plot.getDomainAxis();
	categoryAxis.setLowerMargin(0.20000000000000001D);
	categoryAxis.setUpperMargin(0.20000000000000001D);
	//categoryAxis.setCategoryMargin(0.5D);//Makes no difference
	categoryAxis.setVisible(false);
	NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
	numberAxis.setVisible(false);
	LegendTitle legend = chart.getLegend();
	legend.setItemFont(UIManager.getFont("Label.font"));
	legend.setBackgroundPaint(backgroundPaint);
	legend.setFrame(BlockBorder.NONE);
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }

    private static void populateDataset(DefaultCategoryDataset dataset, Module m) {
	float personalised = 0, social = 0, oneSizeForAll = 0;
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    TLActivity tla = lineItem.getActivity();
	    float total = lineItem.getTotalLearnerHourCount(m);
	    switch (tla.getLearningExperience()) {
		case PERSONALISED: {
		    personalised = personalised + (total * 100);
		    break;
		}
		case SOCIAL: {
		    social = social + (total * 100);
		    break;
		}
		case ONE_SIZE_FOR_ALL: {
		    oneSizeForAll = oneSizeForAll + (total * 100);
		    break;
		}
	    }
	}
	dataset.setValue(personalised, "Personalised", "Learning Experience");
	dataset.setValue(social, "Social", "Learning Experience");
	dataset.setValue(oneSizeForAll, "One Size For All", "Learning Experience");
    }
}
