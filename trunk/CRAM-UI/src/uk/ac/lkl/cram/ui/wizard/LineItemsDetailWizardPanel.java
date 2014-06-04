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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.StudentTeacherInteraction;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class represents the non-visual aspects of the step in the TLA creator wizard
 * in which the user specifies the characteristics of a TLA line item.
 * @see uk.ac.lkl.cram.model.TLALineItem
 * @see LineItemsDetailVisualPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class LineItemsDetailWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(LineItemsDetailWizardPanel.class.getName());

    private boolean isValid = false; 
    private ChangeSupport changeSupport = new ChangeSupport(this);
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private LineItemsDetailVisualPanel component;
    private TLALineItem lineItem;
    private Module module;

    LineItemsDetailWizardPanel(Module module, TLALineItem lineItem) {
	this.module = module;
	this.lineItem = lineItem;
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public LineItemsDetailVisualPanel getComponent() {
	if (component == null) {
	    component = new LineItemsDetailVisualPanel(module, lineItem);
	    component.addPropertyChangeListener(new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent pce) {
		    if (pce.getPropertyName().equals(LineItemsDetailVisualPanel.PROP_VALID)) {
			setValid((Boolean)pce.getNewValue());
		    }
		}
	    });
	}
	return component;
    }

    private void setValid(boolean b) {
	if (b != isValid) {
	    isValid = b;
	    changeSupport.fireChange();
	}
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
	return isValid;
	// If it depends on some condition (form filled out...) and
	// this condition changes (last form field filled in...) then
	// use ChangeSupport to implement add/removeChangeListener below.
	// WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
	changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
	changeSupport.removeChangeListener(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
	// use wiz.getProperty to retrieve previous panel state
	wiz.putProperty(WizardDescriptor.PROP_INFO_MESSAGE, getInfoMessage());
        //Use the activity from the previous step
        TLActivity activity = (TLActivity) wiz.getProperty(TLACreatorWizardIterator.PROP_ACTIVITY);
        if (lineItem.getActivity() != activity) {
            //LOGGER.info("setting activity: " + System.identityHashCode(activity));
            lineItem.setActivity(activity);
        }
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
	// use wiz.putProperty to remember current panel state
    }
    
    private String getInfoMessage() {
        StringBuilder builder = new StringBuilder();
        TLActivity tla = lineItem.getActivity();
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
}
