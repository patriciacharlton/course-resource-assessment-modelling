
package uk.ac.lkl.cram.ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import uk.ac.lkl.cram.model.LearningType;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * $Date$
 * @author Bernard Horan
 */
public class TLALearningTypeChartFactory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("TLA Learning Type Test");
	ChartPanel chartPanel = createChartPanel(new TLActivity("Dummy TLA"));
	frame.setContentPane(chartPanel);
	frame.setVisible(true);
    }
    
    public static ChartPanel createChartPanel(TLActivity tla) {
	JFreeChart chart = createChart(tla);
	ChartPanel chartPanel = new ChartPanel(chart);
	return chartPanel;
    }
    
    public static BufferedImage createChartImage(TLActivity tla, Dimension d) {
	JFreeChart chart = createChart(tla);
        return chart.createBufferedImage(d.width, d.height);
    }

    private static PieDataset createDataSet(TLActivity tla) {
        final LearningType lt = tla.getLearningType();
        
	final DefaultPieDataset dataset = new DefaultPieDataset();
	populateDataset(dataset, lt);
        lt.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                populateDataset(dataset, lt);
            }
        });
	return dataset;
    }

    private static JFreeChart createChart(PieDataset dataset) {
	JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
	PiePlot plot = (PiePlot) chart.getPlot();
	plot.setLabelGenerator(null);
	LegendTitle legend = chart.getLegend();
	legend.setPosition(RectangleEdge.RIGHT);
	return chart;
    }

    private static void populateDataset(DefaultPieDataset dataset, LearningType lt) {
        dataset.setValue("Acquisition", lt.getAcquisition());
        dataset.setValue("Inquiry", lt.getInquiry());
        dataset.setValue("Discusssion", lt.getDiscussion());
        dataset.setValue("Practice", lt.getPractice());
        dataset.setValue("Production", lt.getProduction());
	dataset.setValue("Collaboration", lt.getCollaboration());
    }

    public static JFreeChart createChart(TLActivity tla) {
	PieDataset dataset = createDataSet(tla);
	JFreeChart chart = createChart(dataset);
	return chart;
    }
    
    
}
