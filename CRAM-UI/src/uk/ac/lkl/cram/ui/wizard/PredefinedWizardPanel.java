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
import javax.swing.JList;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.chart.JFreeChart;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.ONE_SIZE_FOR_ALL;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.PERSONALISED;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.SOCIAL;
import static uk.ac.lkl.cram.model.LearnerFeedback.PEER_ONLY;
import static uk.ac.lkl.cram.model.LearnerFeedback.TEL;
import static uk.ac.lkl.cram.model.LearnerFeedback.TUTOR;
import uk.ac.lkl.cram.model.StudentTeacherInteraction;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.chart.TLALearningTypeChartFactory;

/**
 * This class implements the non-visual aspects of the step in the TLA creator
 * wizard that is responsible for enabling the user to select an existing TLA.
 * The TLAs are those that are provided by the tool and also those that the user
 * has created before.
 * @see uk.ac.lkl.cram.model.TLALibrary
 * @see uk.ac.lkl.cram.model.UserTLALibrary
 * @see PredefinedVisualPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class PredefinedWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(PredefinedWizardPanel.class.getName());
    //Is the step valid? I.e. can the user go to the next step
    private boolean isValid = false;
    //For managing properties
    private ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private PredefinedVisualPanel component;
    //The selectedTLA from one of the lists in the visual panel
    private TLActivity selectedTLA;
    //The line item that is being edited
    private final TLALineItem lineItem;
    //The description of the wizard
    private final WizardDescriptor wizardDesc;

    /**
     * Create a new instance of this step in the wizard, for the line item provided
     * and using the description of the wizard
     * @param lineItem the line item to be edited
     * @param wizardDesc the description of the wizard
     */
    PredefinedWizardPanel(TLALineItem lineItem, WizardDescriptor wizardDesc) {
	this.lineItem = lineItem;
	this.wizardDesc = wizardDesc;
    }
 
    /**
     * Get the visual component for the panel. In this template, the component
     * is kept separate. This can be more efficient: if the wizard is created
     * but never displayed, or not all panels are displayed, it is better to
     * create only those which really need to be visible.
     * @return the panel that renders this step of the wizard
     */
    @Override
    public PredefinedVisualPanel getComponent() {
	if (component == null) {
	    component = new PredefinedVisualPanel();
	    //When the selection changes in one of the lists, update the
	    //selected TLA
	    ActivityListSelectionListener alsl = new ActivityListSelectionListener();
	    component.getPredefinedActivityList().addListSelectionListener(alsl);
	    component.getUserActivityList().addListSelectionListener(alsl);
	    //When the data changes in one of the lists, null the selected TLA
	    ActivityListDataListener aldl = new ActivityListDataListener();
	    component.getPredefinedActivityList().getModel().addListDataListener(aldl);
	    component.getUserActivityList().getModel().addListDataListener(aldl);
	}
	return component;
    }
    
    /**
     * The validity of this step of the wizard has changed, let everyone one
     * @param b the validity of this step of the wizard
     */
    private void setValid(boolean b) {
	if (b != isValid) {
	    isValid = b;
	    changeSupport.fireChange();
	}
    }
    
    /**
     * Set the selected TLA for the wizard
     * @param selectedValue a TLA that is now the activity of the line item
     */
    private void setSelectedTLA(TLActivity selectedValue) {
	//set the value of the variable
	selectedTLA = selectedValue;
	//set the activity of the line item
	lineItem.setActivity(selectedTLA);
	//Set the validity of this step of the wizard to be true if the TLA is not null
	setValid(selectedTLA != null);
	//Create an learning type image that describes the activity
	BufferedImage image = TLACreatorWizardIterator.EMPTY_IMAGE;
	if (selectedTLA != null) {
	    JFreeChart chart = TLALearningTypeChartFactory.createChart(selectedTLA);
	    image = chart.createBufferedImage(TLACreatorWizardIterator.LEFT_WIDTH, TLACreatorWizardIterator.LEFT_WIDTH, 300, 300, null);
	}
	wizardDesc.putProperty(WizardDescriptor.PROP_IMAGE, image);
	//Set the contents of the description of the TLA in the visual panel
	if (selectedTLA == null) {
	    getComponent().setTLAInfo("");
	} else {
	    getComponent().setTLAInfo(getInfoMessage(selectedTLA));
	}
    }

    /**
     * AFAIK, not used in this implementation
     * @return the help context for this step of the wizard
     */
    @Override
    public HelpCtx getHelp() {
	// Show no Help button for this panel:
	return HelpCtx.DEFAULT_HELP;
	// If you have context help:
	// return new HelpCtx("help.key.here");
    }

    /**
     * Is it OK to press next or finish
     * @return true if it is OK to press next or finish
     */
    @Override
    public boolean isValid() {
	return isValid;
	// If it depends on some condition (form filled out...) and
	// this condition changes (last form field filled in...) then
	// use ChangeSupport to implement add/removeChangeListener below.
	// WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    /**
     * Listen for changes in state
     * @param l the listener that is listening for changes in state, such as validity
     */
    @Override
    public void addChangeListener(ChangeListener l) {
	changeSupport.addChangeListener(l);
    }

    /**
     * Stop listening for changes in state
     * @param l the listener that was listening for changes in state
     */
    @Override
    public void removeChangeListener(ChangeListener l) {
	changeSupport.removeChangeListener(l);
    }

    /**
     * Not used in this implementation
     * @param wiz
     */
    @Override
    public void readSettings(WizardDescriptor wiz) {
	// use wiz.getProperty to retrieve previous panel state
    }

    /**
     * Not used in this implementation
     * @param wiz
     */
    @Override
    public void storeSettings(WizardDescriptor wiz) {
	// use wiz.putProperty to remember current panel state
        //Record the activity for use by a later step
        wiz.putProperty(TLACreatorWizardIterator.PROP_ACTIVITY, selectedTLA);
    }
    
    /**
     * Return the message that describes the TLA
     * @param tla the TLA to be described
     * @return a string describing the TLA
     */
    private String getInfoMessage(TLActivity tla) {
        @SuppressWarnings("StringBufferWithoutInitialCapacity")
        StringBuilder builder = new StringBuilder();
        //Learning experience
        switch (tla.getLearningExperience()) {
            case ONE_SIZE_FOR_ALL:
                builder.append("Same for All");
                break;
            case PERSONALISED:
                builder.append("Personalised");
                break;
            case SOCIAL:
                builder.append("Social (size: ");
		builder.append(tla.getMaximumGroupSize());
		builder.append(")");
                break;
        }
        
        //Student interaction
        StudentTeacherInteraction sti = tla.getStudentTeacherInteraction();
        if (sti.isOnline()) {
            builder.append(", ");
            builder.append("Online");
        }
        if (sti.isLocationSpecific()) {
            builder.append(", ");
            builder.append("Location-specific");
        }
        if (sti.isTimeSpecific()) {
            builder.append(", ");
            builder.append("Time-specific");
        }
        if (sti.isTutorSupported()) {
            builder.append(", ");
            builder.append("Tutor-present");
        }
        //Student Feedback      
        switch (tla.getLearnerFeedback()) {
            case PEER_ONLY:
                builder.append(", ");
                builder.append("Peer feedback");
                break;
            case TEL:
                builder.append(", ");
                builder.append("Computer-based feedback");
                break;
            case TUTOR:
                builder.append(", ");
                builder.append("Tutor feedback");
                break;
        }
        return builder.toString();
    }
    
    /**
     * Class to listen for changes to a list, which then sets the selected TLA
     */
    private class ActivityListSelectionListener implements ListSelectionListener {

	@Override
	public void valueChanged(ListSelectionEvent lse) {
	    if (!lse.getValueIsAdjusting()) {
		@SuppressWarnings("unchecked")
		JList<TLActivity> list = (JList<TLActivity>) lse.getSource();
		//Only set the selected TLA if the source of the event has focus
		if (list.hasFocus()) {
		    setSelectedTLA(list.getSelectedValue());
		}
	    }
	}
    }
    
    private class ActivityListDataListener implements ListDataListener {

	@Override
	public void intervalAdded(ListDataEvent e) {
	    //no op
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
	    //no-op
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
	    setSelectedTLA(null);
	}
	
    }
}
