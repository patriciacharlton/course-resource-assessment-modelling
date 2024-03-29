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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.undo.CompoundEdit;
import uk.ac.lkl.cram.model.LearnerFeedback;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.TextFieldAdapter;
import uk.ac.lkl.cram.ui.undo.PluggableUndoableEdit;

/**
 * This class represents the visual rendering of a step in the TLA creator wizard--
 * the one in which the user enters the student interaction and feedback for the
 * TLA.
 * @see TLAPropertiesWizardPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class TLAPropertiesVisualPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(TLAPropertiesVisualPanel.class.getName());
    //The compound edit that keeps track of the user's changes to the model
    private final CompoundEdit compoundEdit;
    //The line item to be edited
    private final TLALineItem lineItem;

    TLAPropertiesVisualPanel(TLALineItem lineItem) {
        this(lineItem, new CompoundEdit());
    }
    
    /**
     * Creates new form TLAPropertiesVisualPanel
     * @param tlaLineItem 
     * @param cEdit compound edit that keeps track of the user's changes to the model
     */
    public TLAPropertiesVisualPanel(TLALineItem tlaLineItem, CompoundEdit cEdit) {
	this.lineItem = tlaLineItem;
	initComponents();
        this.compoundEdit = cEdit;
        addActivityListeners();
        //If the line item's activity changes, then add listeners to it
        lineItem.addPropertyChangeListener(TLALineItem.PROP_ACTIVITY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                addActivityListeners();
            }
        });
		
    }
    
    /**
     * Add listeners to the line item's activity
     */
    private void addActivityListeners() {
        //Listener for change in name of activity
	lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_NAME, new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		tlActivityNameChanged();
	    }
	});
	//Add a document listener to the name field so that when the user
        //changes its contents the value in the activity is updated
        //And the validity of the wizard step is validated
        new TextFieldAdapter(tlaNameField) {

	    @Override
	    public void updateText(String text) {
		//Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity(), "name", text);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'name' of " + lineItem.getActivity(), ex);
		}
                //Set the value in the model
                lineItem.getActivity().setName(text);
	    }
	};
	//Get the name from the activity and put into the text field
	tlActivityNameChanged();
	
	//Radio Buttons
	//Action listener for radio buttons
	ActionListener feedbackListener = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		LearnerFeedback lf = LearnerFeedback.valueOf(actionCommand);
		//Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity(), "learnerFeedback", lf);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'learnerFeedback' of " + lineItem.getActivity(), ex);
		}
                //Set the value in the model
                lineItem.getActivity().setLearnerFeedback(lf);
	    }
	};
	tutorFeedbackRB.addActionListener(feedbackListener);
	tutorFeedbackRB.setActionCommand(LearnerFeedback.TUTOR.name());
	peerFeedbackRB.addActionListener(feedbackListener);
	peerFeedbackRB.setActionCommand(LearnerFeedback.PEER_ONLY.name());
	telFeedbackRB.addActionListener(feedbackListener);
	telFeedbackRB.setActionCommand(LearnerFeedback.TEL.name());
	noFeedbackRB.addActionListener(feedbackListener);
	noFeedbackRB.setActionCommand(LearnerFeedback.NONE.name());
	//Set the state of the RBs from the activity
	LearnerFeedback lf = lineItem.getActivity().getLearnerFeedback();
	switch (lf) {
	    case TUTOR: {
		tutorFeedbackRB.setSelected(true);
		break;
	    }
	    case PEER_ONLY: {
		peerFeedbackRB.setSelected(true);
		break;
	    }
	    case TEL: {
		telFeedbackRB.setSelected(true);
		break;
	    }
	    case NONE: {
		noFeedbackRB.setSelected(true);
		break;
	    }
	}
	
	//CheckBoxes	
	tutorPresentCB.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent ie) {
		boolean selected = ie.getStateChange() == ItemEvent.SELECTED;
		//Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity().getStudentTeacherInteraction(), "tutorSupported", selected);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'tutorSupported' of " + lineItem.getActivity().getStudentTeacherInteraction(), ex);
		}
                //Set the value in the model
                lineItem.getActivity().getStudentTeacherInteraction().setTutorSupported(selected);
	    }
	});
	tutorPresentCB.setSelected(lineItem.getActivity().getStudentTeacherInteraction().isTutorSupported());
	onlineCB.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent ie) {
		boolean selected = ie.getStateChange() == ItemEvent.SELECTED;
		//Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity().getStudentTeacherInteraction(), "online", selected);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'online' of " + lineItem.getActivity().getStudentTeacherInteraction(), ex);
		}
                //Set the value in the model
                lineItem.getActivity().getStudentTeacherInteraction().setOnline(selected);
	    }
	});
	onlineCB.setSelected(lineItem.getActivity().getStudentTeacherInteraction().isOnline());
	locationSpecificCB.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent ie) {
		boolean selected = ie.getStateChange() == ItemEvent.SELECTED;
		//Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity().getStudentTeacherInteraction(), "locationSpecific", selected);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'locationSpecific' of " + lineItem.getActivity().getStudentTeacherInteraction(), ex);
		}
                //Set the value in the model
                lineItem.getActivity().getStudentTeacherInteraction().setLocationSpecific(selected);
	    }
	});
	locationSpecificCB.setSelected(lineItem.getActivity().getStudentTeacherInteraction().isLocationSpecific());
	timeSpecificCB.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent ie) {
		boolean selected = ie.getStateChange() == ItemEvent.SELECTED;
		//Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity().getStudentTeacherInteraction(), "timeSpecific", selected);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'timeSpecific' of " + lineItem.getActivity().getStudentTeacherInteraction(), ex);
		}
                //Set the value in the model
                lineItem.getActivity().getStudentTeacherInteraction().setTimeSpecific(selected);
	    }
	});
	timeSpecificCB.setSelected(lineItem.getActivity().getStudentTeacherInteraction().isTimeSpecific());
    }

    @Override
    public String getName() {
	return "Interaction Details";
    }
    
    /**
     * The name of the activity has been changed, so update the 
     * text field if necessary
     */
    private void tlActivityNameChanged() {
	//Do not update the field if it has focus, as it is the source of the change
	if (tlaNameField.hasFocus()) {
            return;
	}
	if (!tlaNameField.getText().equalsIgnoreCase(lineItem.getName())) {
            tlaNameField.setText(lineItem.getName());
	} 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        feedbackBG = new javax.swing.ButtonGroup();
        tlaNamePanel = new javax.swing.JPanel();
        tlaNameField = new javax.swing.JTextField();
        feedbackPanel = new javax.swing.JPanel();
        tutorFeedbackRB = new javax.swing.JRadioButton();
        peerFeedbackRB = new javax.swing.JRadioButton();
        telFeedbackRB = new javax.swing.JRadioButton();
        noFeedbackRB = new javax.swing.JRadioButton();
        studentInteractionPanel = new javax.swing.JPanel();
        tutorPresentCB = new javax.swing.JCheckBox();
        locationSpecificCB = new javax.swing.JCheckBox();
        onlineCB = new javax.swing.JCheckBox();
        timeSpecificCB = new javax.swing.JCheckBox();

        tlaNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.tlaNamePanel.border.title"))); // NOI18N

        javax.swing.GroupLayout tlaNamePanelLayout = new javax.swing.GroupLayout(tlaNamePanel);
        tlaNamePanel.setLayout(tlaNamePanelLayout);
        tlaNamePanelLayout.setHorizontalGroup(
            tlaNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tlaNameField, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        tlaNamePanelLayout.setVerticalGroup(
            tlaNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tlaNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        feedbackPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.feedbackPanel.border.title"))); // NOI18N

        feedbackBG.add(tutorFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(tutorFeedbackRB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.tutorFeedbackRB.text")); // NOI18N

        feedbackBG.add(peerFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(peerFeedbackRB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.peerFeedbackRB.text")); // NOI18N

        feedbackBG.add(telFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(telFeedbackRB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.telFeedbackRB.text")); // NOI18N

        feedbackBG.add(noFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(noFeedbackRB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.noFeedbackRB.text")); // NOI18N

        javax.swing.GroupLayout feedbackPanelLayout = new javax.swing.GroupLayout(feedbackPanel);
        feedbackPanel.setLayout(feedbackPanelLayout);
        feedbackPanelLayout.setHorizontalGroup(
            feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tutorFeedbackRB)
                    .addComponent(peerFeedbackRB)
                    .addComponent(telFeedbackRB)
                    .addComponent(noFeedbackRB))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        feedbackPanelLayout.setVerticalGroup(
            feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tutorFeedbackRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(peerFeedbackRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(telFeedbackRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noFeedbackRB))
        );

        studentInteractionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.studentInteractionPanel.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(tutorPresentCB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.tutorPresentCB.text")); // NOI18N
        tutorPresentCB.setToolTipText(org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.tutorPresentCB.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(locationSpecificCB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.locationSpecificCB.text")); // NOI18N
        locationSpecificCB.setToolTipText(org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.locationSpecificCB.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(onlineCB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.onlineCB.text")); // NOI18N
        onlineCB.setToolTipText(org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.onlineCB.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(timeSpecificCB, org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.timeSpecificCB.text")); // NOI18N
        timeSpecificCB.setToolTipText(org.openide.util.NbBundle.getMessage(TLAPropertiesVisualPanel.class, "TLAPropertiesVisualPanel.timeSpecificCB.toolTipText")); // NOI18N

        javax.swing.GroupLayout studentInteractionPanelLayout = new javax.swing.GroupLayout(studentInteractionPanel);
        studentInteractionPanel.setLayout(studentInteractionPanelLayout);
        studentInteractionPanelLayout.setHorizontalGroup(
            studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentInteractionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tutorPresentCB)
                    .addComponent(locationSpecificCB)
                    .addComponent(onlineCB)
                    .addComponent(timeSpecificCB))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        studentInteractionPanelLayout.setVerticalGroup(
            studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentInteractionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tutorPresentCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(onlineCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationSpecificCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeSpecificCB))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tlaNamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(studentInteractionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(feedbackPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tlaNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(studentInteractionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(feedbackPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup feedbackBG;
    private javax.swing.JPanel feedbackPanel;
    private javax.swing.JCheckBox locationSpecificCB;
    private javax.swing.JRadioButton noFeedbackRB;
    private javax.swing.JCheckBox onlineCB;
    private javax.swing.JRadioButton peerFeedbackRB;
    private javax.swing.JPanel studentInteractionPanel;
    private javax.swing.JRadioButton telFeedbackRB;
    private javax.swing.JCheckBox timeSpecificCB;
    private javax.swing.JTextField tlaNameField;
    private javax.swing.JPanel tlaNamePanel;
    private javax.swing.JRadioButton tutorFeedbackRB;
    private javax.swing.JCheckBox tutorPresentCB;
    // End of variables declaration//GEN-END:variables
    }
