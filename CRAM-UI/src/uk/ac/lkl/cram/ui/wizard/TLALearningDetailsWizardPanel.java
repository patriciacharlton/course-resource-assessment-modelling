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
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class represents a step in the TLA Creator Wizard.
 * It is responsible for managing a visual component, in terms of validity.
 * @see TLALearningDetailsVisualPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class TLALearningDetailsWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(TLALearningDetailsWizardPanel.class.getName());

    /**
     * Indicates if the user can go to the next step
     */
    private boolean isValid = false;
    /**
     * Manages the validity of this step in the wizard
     */
    private ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private TLALearningDetailsVisualPanel component;
    /**
     * The line item to be edited
     */
    private final TLALineItem lineItem;

    TLALearningDetailsWizardPanel(TLALineItem lineItem) {
	this.lineItem = lineItem;
    }

    /**
     * Get the visual component for the panel. In this template, the component
     * is kept separate. This can be more efficient: if the wizard is created
     * but never displayed, or not all panels are displayed, it is better to
     * create only those which really need to be visible.
     * @return the component for the step of the wizard
     */
    @Override
    public TLALearningDetailsVisualPanel getComponent() {
	//Lazy instantiation
        if (component == null) {
	    component = new TLALearningDetailsVisualPanel(lineItem);
            //Add a lisneter to listen if the data entry fields in the 
            //component are valid to determine if this step of the 
            //wizard is valid
	    component.addPropertyChangeListener(new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent pce) {
		   
		    if (pce.getPropertyName().equals(TLALearningDetailsVisualPanel.PROP_VALID)) {
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
    
    /**
     * Get the help context for this step of the wizard.<br/>
     * Not currently used in this implementation.
     * @return the help context describing the currnent step of the wizard
     */
    @Override
    public HelpCtx getHelp() {
	// Show no Help button for this panel:
	return HelpCtx.DEFAULT_HELP;
	// If you have context help:
	// return new HelpCtx("help.key.here");
    }

    /**
     * Return whether the step of this wizard is valid
     * @return true if it is OK to press Next or Finish
     */
    @Override
    public boolean isValid() {
	return isValid;
    }

    /**
     * Add a change listener to this step of the wizard
     * @param l a change listener
     */
    @Override
    public void addChangeListener(ChangeListener l) {
	changeSupport.addChangeListener(l);
    }

    /**
     * Remove a change listener from this step of the wizard
     * @param l a change listener
     */
    @Override
    public void removeChangeListener(ChangeListener l) {
	changeSupport.removeChangeListener(l);
    }

    /**
     * Read the settings from the WizardDescriptor, which acts like a property list
     * @param wiz the wizard descriptor
     */
    @Override
    public void readSettings(WizardDescriptor wiz) {
	// use wiz.getProperty to retrieve previous panel state
	wiz.putProperty(WizardDescriptor.PROP_IMAGE, TLACreatorWizardIterator.EMPTY_IMAGE);
        wiz.putProperty(WizardDescriptor.PROP_INFO_MESSAGE, "");
        //Read the activity from a previous step and update the line item with it
        TLActivity activity = (TLActivity) wiz.getProperty(TLACreatorWizardIterator.PROP_ACTIVITY);
        if (lineItem.getActivity() != activity) {
            lineItem.setActivity(activity);
        }
    }

    /**
     * Store settings on the wizard descriptor, which acts like a property list.
     * @param wiz the wizard descriptor
     */
    @Override
    public void storeSettings(WizardDescriptor wiz) {
	// use wiz.putProperty to remember current panel state
    }
}
