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
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class produces an instance of class
 * ChartPanel. The ChartPanel presents a display of the learning types for a
 * module, rendered in the form of a pie chart. The factory is responsible for
 * setting up all the parameters of the chart, including the underlying dataset,
 * legend, colours, and so on.<br>
 *
 * @see org.jfree.chart.ChartPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class LearningTypeChartMaker extends AbstractChartMaker {

    private static final Logger LOGGER = Logger.getLogger(LearningTypeChartMaker.class.getName());
    //Labels for the segments of the pie chart
    final static String ACQUISITION = "Acquisition";
    final static String COLLABORATION = "Collaboration";
    final static String DISCUSSION = "Discussion";
    final static String INQUIRY = "Inquiry";
    final static String PRACTICE = "Practice";
    final static String PRODUCTION = "Production";
    //Colours for the pie chart
    static final Color ACQUISITION_COLOR = new Color(101, 220, 241);
    static final Color DISCUSSION_COLOR = new Color(121, 173, 236);
    static final Color INQUIRY_COLOR = new Color(250, 128, 128);
    static final Color PRACTICE_COLOR = new Color(190, 152, 221);
    static final Color PRODUCTION_COLOR = new Color(188, 234, 117);
    static final Color COLLABORATION_COLOR = new Color(0xFFCD00);

    /**
     * For testing purposes only
     *
     * @param args the command line arguments (ignored)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Learning Type Test");
        Module m = AELMTest.populateModule();
        LearningTypeChartMaker maker = new LearningTypeChartMaker(m);
        frame.setContentPane(maker.getChartPanel());
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
    //map between learning type and TLALineItems with an activity that includes that learning type
    private Map<String, Set<TLALineItem>> learningTypeMap;

    /**
     * Create a learning type chart maker from the module
     * @param m the module from which to create the chart maker
     */
    public LearningTypeChartMaker(Module m) {
        super(m);       
    }

    /**
     * Create a dataset from the module
     *
     * @return a pie dataset that is used to produce a pie chart
     */
    @Override
    protected Dataset createDataSet() {
        //Create the dataset to hold the data
        final DefaultPieDataset pieDataset = new DefaultPieDataset();
        //Populate the dataset with the data
        populateDataset(pieDataset, module);
        //Create a listener, which repopulates the dataset when anything changes
        final PropertyChangeListener learningTypeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                //LOGGER.info("property change: " + pce);
                populateDataset(pieDataset, module);
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
                populateDataset(pieDataset, module);
            }
        });

        return pieDataset;
    }

    /**
     * Return the total 'amount' of acquisition for all the activities in the
     * module
     *
     * @param m the module containing the activities
     * @return a float representing all the amount of acquisition learning type
     * for all the activities in the module
     */
    private float getTotalAcquisition(Module m) {
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
     * @see LearningTypeChartMaker#getTotalAcquisition(Module)
     */
    private float getTotalInquiry(Module m) {
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
     * @see LearningTypeChartMaker#getTotalAcquisition(Module)
     */
    private float getTotalDiscussion(Module m) {
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
     * @see LearningTypeChartMaker#getTotalAcquisition(Module)
     */
    private float getTotalPractice(Module m) {
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
     * @see LearningTypeChartMaker#getTotalAcquisition(Module)
     */
    private float getTotalProduction(Module m) {
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
     * @see LearningTypeChartMaker#getTotalAcquisition(Module)
     */
    private float getTotalCollaboration(Module m) {
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
     *
     * @return a Chart that can be rendered in a ChartPanel
     */
    @Override
    protected JFreeChart createChart() {
        //Create a pie chart from the chart factory with no title, a legend and tooltips
        JFreeChart chart = ChartFactory.createPieChart(null, (PieDataset) dataset, true, true, false);
        //Get the plot from the chart
        PiePlot plot = (PiePlot) chart.getPlot();
        //Set the tooltip generator, to be "Practice (12%)"
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("<html><center>{0} ({2})<br/>Double-click for more</center></html>"));
        //Remove shadows from the plot
        plot.setShadowXOffset(0);
        plot.setShadowYOffset(0);
        //Remove the labels from the plot
        plot.setLabelGenerator(null);
        //Set the colours for the segments
        plot.setSectionPaint(ACQUISITION, ACQUISITION_COLOR);
        plot.setSectionPaint(COLLABORATION, COLLABORATION_COLOR);
        plot.setSectionPaint(DISCUSSION, DISCUSSION_COLOR);
        plot.setSectionPaint(INQUIRY, INQUIRY_COLOR);
        plot.setSectionPaint(PRACTICE, PRACTICE_COLOR);
        plot.setSectionPaint(PRODUCTION, PRODUCTION_COLOR);
        return chart;
    }

    private void populateDataset(DefaultPieDataset dataset, Module m) {
        initializeMap();
        dataset.setValue(ACQUISITION, getTotalAcquisition(m));
        dataset.setValue(COLLABORATION, getTotalCollaboration(m));
        dataset.setValue(DISCUSSION, getTotalDiscussion(m));
        dataset.setValue(INQUIRY, getTotalInquiry(m));
        dataset.setValue(PRACTICE, getTotalPractice(m));
        dataset.setValue(PRODUCTION, getTotalProduction(m));
    }

    private void initializeMap() {
        learningTypeMap = new WeakHashMap<>();
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

    /**
     * Return the learning type map
     * @return the learning type map
     */
    public Map<String, Set<TLALineItem>>  getLearningTypeMap() {
        return Collections.unmodifiableMap(learningTypeMap);
    }
}
