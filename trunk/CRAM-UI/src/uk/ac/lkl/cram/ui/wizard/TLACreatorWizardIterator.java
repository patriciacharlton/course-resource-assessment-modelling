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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Iterator;
import org.openide.WizardDescriptor.Panel;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;

/**
 * This class is responsible for managing the wizard to create a new TLA. It
 * manages the steps in the wizard, and also some data that is common to all steps
 * in the wizard. The wizard provides two 'sequences': a predefined sequence (in which the
 * user selects an existing TLA), or a 'vanilla' sequence (in which the user creates a new
 * TLA).
 * @version $Revision$
 * @author Bernard Horan 
 */
//$Date$
public class TLACreatorWizardIterator implements Iterator<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(TLACreatorWizardIterator.class.getName());
    static final int LEFT_WIDTH = 200;
    static final BufferedImage EMPTY_IMAGE = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
    static final String PROP_VANILLA = "vanilla";

    //The current step in the wizard
    private int index;
    //The descriptor of the wizard--maintains common data
    private WizardDescriptor wizardDesc;
    
    //All the panels used in the wizard
    private Panel[] allPanels;
    //the panels used in the current sequence
    private Panel[] currentPanels;
    //the panels that make up the predefined sequence
    private Panel[] predefinedSequence;
    //the panels used in the vanilla sequence
    private Panel[] vanillaSequence;

    //The indices used to create the predefined sequence
    private String[] predefinedIndices;
    //The indices used to create the vanilla sequence
    private String[] vanillaIndices;
    //The CRAM module
    private final Module module;
    //The line item that is being created
    private final TLALineItem lineItem;

    /**
     * Create a new wizard iterator on the module
     * @param module the CRAM module 
     */
    public TLACreatorWizardIterator(Module module) {
	this.module = module;
	this.lineItem = new TLALineItem();
    }
    
    /**
     * Initialize the iterator with the descriptor. 
     * @param wizardDescriptor holds common data for all steps
     */
    public void initialize(WizardDescriptor wizardDescriptor) {
        wizardDesc = wizardDescriptor;
        wizardDesc.putProperty(WizardDescriptor.PROP_IMAGE, EMPTY_IMAGE);
	//wizardDesc.putProperty(PROP_MODULE, module);
    }
    
    /**
     * Set up the panels that are used in the steps of the wizard
     */
    private void initializePanels() {
        int maxWidth =0, maxHeight =0;
        if (allPanels == null) {
            allPanels = new WizardDescriptor.Panel[]{
                new StartWizardPanel(),
                new PredefinedWizardPanel(lineItem, wizardDesc),
                new TLALearningDetailsWizardPanel(lineItem.getActivity()),
		new TLAPropertiesWizardPanel(lineItem.getActivity()),
		new LineItemsDetailWizardPanel(module, lineItem)
            };
            String[] steps = new String[allPanels.length];
            for (int i = 0; i < allPanels.length; i++) {
                Component c = allPanels[i].getComponent();
                // Default step name to component name of panel.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
		    // Width of left pane
		    jc.putClientProperty(WizardDescriptor.PROP_LEFT_DIMENSION, LEFT_WIDTH);
                    maxWidth = Math.max(maxWidth, jc.getPreferredSize().width);
                    maxHeight = Math.max(maxHeight, jc.getPreferredSize().height);
                }
            }
            Dimension preferredSize = new Dimension(maxWidth, maxHeight);
                
            for (int i = 0; i < allPanels.length; i++) {
                Component c = allPanels[i].getComponent();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.setPreferredSize(preferredSize);
                }
            }
            
            predefinedIndices = new String[]{steps[0], steps[1], steps[4]};
            predefinedSequence = new Panel[]{allPanels[0], allPanels[1], allPanels[4]};
            
            vanillaIndices = new String[]{steps[0], steps[2], steps[3], steps[4]};
            vanillaSequence = new Panel[]{allPanels[0], allPanels[2], allPanels[3], allPanels[4]};
            //Default to predefined sequence
            currentPanels = predefinedSequence;
        
        }
        
    }

    /**
     * set up the steps in the wizard, depending on the kind of sequence
     * @param vanilla if true, a vanilla sequence of steps
     */
    private void setVanilla(boolean vanilla) {
        String[] contentData;
        if (vanilla) {
            currentPanels = vanillaSequence;
            contentData = vanillaIndices;
        } else {
            currentPanels = predefinedSequence;
            contentData = predefinedIndices;
        }
        wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_DATA, contentData);
    }

    /**
     *
     * @return the current panel
     */
    @Override
    @SuppressWarnings("unchecked")
    public WizardDescriptor.Panel<WizardDescriptor> current() {
	initializePanels();
        return currentPanels[index];
    }

    /**
     * Return the title for the wizard
     * @return the title of the wizard
     */
    @Override
    public String name() {
	if (index == 0) {
            return index + 1 + " of ...";
        }
        return index + 1 + " of " + currentPanels.length;
    }

    /**
     * Is there a further step in the sequence
     * @return true if there is a further step in the sequence
     */
    @Override
    public boolean hasNext() {
	initializePanels();
        return index < currentPanels.length - 1;
    }

    /**
     * Is there a previous step in the  sequence
     * @return true if there is a previous step in the sequence
     */
    @Override
    public boolean hasPrevious() {
	return index > 0;
    }

    /**
     * go to the next panel
     */
    @Override
    public void nextPanel() {
	//If there's no next panel, throw an exception
	if (!hasNext()) {
	    throw new NoSuchElementException();
	}
	//If we are at the first step, determine if we are about to embark
	//on a vanilla or predefined sequence
	if (index == 0) {
	    Boolean vanilla = (Boolean) wizardDesc.getProperty(PROP_VANILLA);
	    setVanilla(vanilla);
	}
	//Otherwise increment the step number
	index++;
	wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
    }

    /**
     *
     */
    @Override
    public void previousPanel() {
	//If there's no previous panel, throw an exception
	if (!hasPrevious()) {
	    throw new NoSuchElementException();
	}
	//Otherwise decrement the step number
	index--;
	wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
    }

    /**
     *
     * @param l
     */
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    /**
     *
     * @param l
     */
    @Override
    public void removeChangeListener(ChangeListener l) {
    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    /**
     * Return the line item that this wizard is editing
     * @return the wizard's line item
     */
    public TLALineItem getLineItem() {
	return lineItem;
    }

    /**
     * Is this wizard on a vanilla sequence
     * @return true if the wizard is creating a TLA from scratch
     */
    public boolean isVanilla() {
	return (Boolean) wizardDesc.getProperty(PROP_VANILLA);
    }
}
