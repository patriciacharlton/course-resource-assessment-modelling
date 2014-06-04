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
package uk.ac.lkl.cram.ui.wizard;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.jfree.chart.JFreeChart;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.chart.TLALearningTypeChartFactory;

/**
 * This class represents the non-visual aspects of a step in the TLA creator wizard--
 * the one in which the user enters the student interaction and feedback for the
 * TLA.
 * @see TLAPropertiesVisualPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class TLAPropertiesWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(TLAPropertiesWizardPanel.class.getName());

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private TLAPropertiesVisualPanel component;
    private final TLALineItem lineItem;

    TLAPropertiesWizardPanel(TLALineItem tlaLineItem) {
	this.lineItem = tlaLineItem;
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public TLAPropertiesVisualPanel getComponent() {
	if (component == null) {
	    component = new TLAPropertiesVisualPanel(lineItem);
	}
	return component;
    }

    @Override
    public HelpCtx getHelp() {
	// Show no Help button for this panel:
	return HelpCtx.DEFAULT_HELP;
	// If you have context help:
	// return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
	// If it is always OK to press Next or Finish, then:
	return true;
	// If it depends on some condition (form filled out...) and
	// this condition changes (last form field filled in...) then
	// use ChangeSupport to implement add/removeChangeListener below.
	// WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
	// use wiz.getProperty to retrieve previous panel state
	JFreeChart chart = TLALearningTypeChartFactory.createChart(lineItem.getActivity());
	BufferedImage image = chart.createBufferedImage(TLACreatorWizardIterator.LEFT_WIDTH, TLACreatorWizardIterator.LEFT_WIDTH, 300, 300, null);
	wiz.putProperty(WizardDescriptor.PROP_IMAGE, image);
        wiz.putProperty(WizardDescriptor.PROP_INFO_MESSAGE, getInfoMessage());
        //Read the current activity and update the line item
        TLActivity activity = (TLActivity) wiz.getProperty(TLACreatorWizardIterator.PROP_ACTIVITY);
        if (lineItem.getActivity() != activity) {
            lineItem.setActivity(activity);
        }
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
	// use wiz.putProperty to remember current panel state
    }

    private String getInfoMessage() {
        @SuppressWarnings("StringBufferWithoutInitialCapacity")
        StringBuilder builder = new StringBuilder();
        switch (lineItem.getActivity().getLearningExperience()) {
            case ONE_SIZE_FOR_ALL:
                builder.append("Same for All");
                break;
            case PERSONALISED:
                builder.append("Personalised");
                break;
            case SOCIAL:
                builder.append("Social (size: ");
		builder.append(lineItem.getActivity().getMaximumGroupSize());
		builder.append(")");
                break;
        }
        return builder.toString();
    }
}
