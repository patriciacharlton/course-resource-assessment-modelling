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
package uk.ac.lkl.cram.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.ui.undo.UndoHandler;

/**
 * This panel provides the 'table' of runs that is displayed both in the ModuleFrame
 * (within a ModulePanel) and in the ModuleWizardPanel.
 * @see ModulePanel
 * @see ModuleWizardPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class PresentationPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(PresentationPanel.class.getName());
    //Map of the text fields, used to manage the automatic defaults
    //A 'dirty' field is one that the user has entered data into
    private WeakHashMap<JFormattedTextField, Boolean> dirtyMap = new WeakHashMap<>();
    //Manages the undos for the text fields in the panel
    private final UndoHandler undoHandler;

    /**
     * Creates a new form PresentationPanel with a new UndoHandler
     */
    public PresentationPanel() {
        this(new UndoHandler());
    }
    
    /**
     * Creates new form PresentationPanel with the specified undoHandler
     * @param uh the undoHandler that is added as a listener to the document 
     * of each of the textfields
     */
    public PresentationPanel(UndoHandler uh) {
	super();
        undoHandler = uh;
	initComponents();
    }
    
    
    
    /**
     * Initialise the panel, based on the provided module
     * @param module the module containing the presentations
     */
    public void initializeModule(Module module) {	
	//Utility class to ensure that the text in the field is selected when the
        //field gains focus
	SelectAllAdapter saa = new SelectAllAdapter();
	
	//Create array of fields that represent the number of home students for each run
        final JFormattedTextField[] homeStudentCountFields = new JFormattedTextField[3];
	homeStudentCountFields[0] = presentation1HomeStudentCountField;
	homeStudentCountFields[1] = presentation2HomeStudentCountField;
	homeStudentCountFields[2] = presentation3HomeStudentCountField;
        //Create array of fields that represent the number of overseas students for each run
        final JFormattedTextField[] overseasStudentCountFields = new JFormattedTextField[3];
	overseasStudentCountFields[0] = presentation1OverseasStudentCountField;
	overseasStudentCountFields[1] = presentation2OverseasStudentCountField;
	overseasStudentCountFields[2] = presentation3OverseasStudentCountField;
        //Create array of fields that represent the fee for home students for each run
	final JFormattedTextField[] homeStudentFeeFields = new JFormattedTextField[3];
	homeStudentFeeFields[0] = presentation1HomeStudentFeeField;
	homeStudentFeeFields[1] = presentation2HomeStudentFeeField;
	homeStudentFeeFields[2] = presentation3HomeStudentFeeField;
        //Create array of fields that represent the fee for overseas students for each run
        final JFormattedTextField[] overseasStudentFeeFields = new JFormattedTextField[3];
	overseasStudentFeeFields[0] = presentation1OverseasStudentFeeField;
	overseasStudentFeeFields[1] = presentation2OverseasStudentFeeField;
	overseasStudentFeeFields[2] = presentation3OverseasStudentFeeField;
        //Create array of fields that represent the daily rate for a lower paid member of staff for each run
	final JFormattedTextField[] juniorCostFields = new JFormattedTextField[3];
	juniorCostFields[0] = presentation1JuniorField;
	juniorCostFields[1] = presentation2JuniorField;
	juniorCostFields[2] = presentation3JuniorField;
        //Create array of fields that represent the daily rate for a lower paid member of staff for each run
        final JFormattedTextField[] seniorCostFields = new JFormattedTextField[3];
	seniorCostFields[0] = presentation1SeniorField;
	seniorCostFields[1] = presentation2SeniorField;
	seniorCostFields[2] = presentation3SeniorField;
        //Iterate through the runs
	int index = 0;
	for (final ModulePresentation modulePresentation : module.getModulePresentations()) {
	    //LOGGER.info("presentation: " + modulePresentation);
            //Initialise the fields that represent the number of home students
            //set the value of the field from the run
	    homeStudentCountFields[index].setValue(new Long(modulePresentation.getHomeStudentCount()));
            //Add the select all adapter to the field 
	    homeStudentCountFields[index].addFocusListener(saa);
            //Add an undoListener to the document of the textfield
            homeStudentCountFields[index].getDocument().addUndoableEditListener(undoHandler);
            //Create an adapter on the field so that when the value of the field changes
            //(from user input) the underlying run is updated with the new 
            //value of the field
	    new FormattedTextFieldAdapter(homeStudentCountFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long studentCount = (Long) value;
		    modulePresentation.setHomeStudentCount((int) studentCount);
                    //The field is dirty--i.e. the user has entered a value
                    makeFieldDirty(textField);
		}
	    };
            //Repeat the above pattern for the elements of each array
            
            overseasStudentCountFields[index].setValue(new Long(modulePresentation.getOverseasStudentCount()));
	    overseasStudentCountFields[index].addFocusListener(saa);
            overseasStudentCountFields[index].getDocument().addUndoableEditListener(undoHandler);
	    new FormattedTextFieldAdapter(overseasStudentCountFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long studentCount = (Long) value;
		    modulePresentation.setOverseasStudentCount((int) studentCount);
                    makeFieldDirty(textField);
		}
	    };
	    
	    homeStudentFeeFields[index].setValue(new Long(modulePresentation.getHomeFee()));
	    homeStudentFeeFields[index].addFocusListener(saa);
            homeStudentFeeFields[index].getDocument().addUndoableEditListener(undoHandler);
	    new FormattedTextFieldAdapter(homeStudentFeeFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long fee = (Long) value;
		    modulePresentation.setHomeFee((int) fee);
                    makeFieldDirty(textField);
		}
	    };
            
            overseasStudentFeeFields[index].setValue(new Long(modulePresentation.getOverseasFee()));
	    overseasStudentFeeFields[index].addFocusListener(saa);
            overseasStudentFeeFields[index].getDocument().addUndoableEditListener(undoHandler);
	    new FormattedTextFieldAdapter(overseasStudentFeeFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long fee = (Long) value;
		    modulePresentation.setOverseasFee((int) fee);
                    makeFieldDirty(textField);
		}
	    };
	    
	    juniorCostFields[index].setValue(new Long(modulePresentation.getJuniorCost()));
	    juniorCostFields[index].addFocusListener(saa);
            juniorCostFields[index].getDocument().addUndoableEditListener(undoHandler);
	    new FormattedTextFieldAdapter(juniorCostFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long cost = (Long) value;
		    modulePresentation.setJuniorCost((int) cost);
                    makeFieldDirty(textField);
		}
	    };
	    
	    seniorCostFields[index].setValue(new Long(modulePresentation.getSeniorCost()));
	    seniorCostFields[index].addFocusListener(saa);
            seniorCostFields[index].getDocument().addUndoableEditListener(undoHandler);
	    new FormattedTextFieldAdapter(seniorCostFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long cost = (Long) value;
		    modulePresentation.setSeniorCost((int) cost);
                    makeFieldDirty(textField);
		}
	    };
	    
	    index++;
	}
        
        //Set the the automatic defaults, so that if the user enters data in 
        //a field for run 1, we update the fields for runs 2 and 3
        final ModulePresentation mp1 = module.getModulePresentations().get(0);
        final ModulePresentation mp2 = module.getModulePresentations().get(1);
        mp1.addPropertyChangeListener(ModulePresentation.PROP_HOME_STUDENT_COUNT, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(homeStudentCountFields[1])) {
                    long studentCount = mp1.getHomeStudentCount();
                    if (mp2.getHomeStudentCount() == 0l) {
                        homeStudentCountFields[1].setValue(studentCount);
                        homeStudentCountFields[2].setValue(studentCount);
                    }
                }
            }
        });
        
        mp1.addPropertyChangeListener(ModulePresentation.PROP_OVERSEAS_STUDENT_COUNT, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(overseasStudentCountFields[1])) {
                    long studentCount = mp1.getOverseasStudentCount();
                    if (mp2.getOverseasStudentCount() == 0l) {
                        overseasStudentCountFields[1].setValue(studentCount);
                        overseasStudentCountFields[2].setValue(studentCount);
                    }
                }
            }
        });
        
        mp1.addPropertyChangeListener(ModulePresentation.PROP_HOME_FEE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(homeStudentFeeFields[1])) {
                    long fee = mp1.getHomeFee();
                    if (mp2.getHomeFee() == 0l) {
                        homeStudentFeeFields[1].setValue(fee);
                        homeStudentFeeFields[2].setValue(fee);
                    }
                }
            }
        });
        
        mp1.addPropertyChangeListener(ModulePresentation.PROP_OVERSEAS_FEE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(overseasStudentFeeFields[1])) {
                    long fee = mp1.getOverseasFee();
                    if (mp2.getOverseasFee() == 0l) {
                        overseasStudentFeeFields[1].setValue(fee);
                        overseasStudentFeeFields[2].setValue(fee);
                    }
                }
            }
        });
        
        mp1.addPropertyChangeListener(ModulePresentation.PROP_JUNIOR_COST, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(juniorCostFields[1])) {
                    long juniorCost = mp1.getJuniorCost();
                    if (mp2.getJuniorCost() == 0l) {
                        juniorCostFields[1].setValue(juniorCost);
                        juniorCostFields[2].setValue(juniorCost);
                    }
                }
            }
        });
        
        
        
        mp1.addPropertyChangeListener(ModulePresentation.PROP_SENIOR_COST, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(seniorCostFields[1])) {
                    long seniorCost = mp1.getSeniorCost();
                    if (mp2.getSeniorCost() == 0l) {
                        seniorCostFields[1].setValue(seniorCost);
                        seniorCostFields[2].setValue(seniorCost);
                    }
                }
            }
        });
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        presentation1Label = new javax.swing.JLabel();
        presentation2Label = new javax.swing.JLabel();
        presentation3Label = new javax.swing.JLabel();
        homeStudentCountLabel = new javax.swing.JLabel();
        presentation1HomeStudentCountField = new javax.swing.JFormattedTextField();
        presentation2HomeStudentCountField = new javax.swing.JFormattedTextField();
        presentation3HomeStudentCountField = new javax.swing.JFormattedTextField();
        homeStudentIncomeFeeLabel = new javax.swing.JLabel();
        presentation1HomeStudentFeeField = new javax.swing.JFormattedTextField();
        presentation2HomeStudentFeeField = new javax.swing.JFormattedTextField();
        presentation3HomeStudentFeeField = new javax.swing.JFormattedTextField();
        overseasStudentCountLabel = new javax.swing.JLabel();
        presentation1OverseasStudentCountField = new javax.swing.JFormattedTextField();
        presentation2OverseasStudentCountField = new javax.swing.JFormattedTextField();
        presentation3OverseasStudentCountField = new javax.swing.JFormattedTextField();
        overseasStudentIncomeFeeLabel = new javax.swing.JLabel();
        presentation1OverseasStudentFeeField = new javax.swing.JFormattedTextField();
        presentation2OverseasStudentFeeField = new javax.swing.JFormattedTextField();
        presentation3OverseasStudentFeeField = new javax.swing.JFormattedTextField();
        juniorCostLabel = new javax.swing.JLabel();
        presentation1JuniorField = new javax.swing.JFormattedTextField();
        presentation2JuniorField = new javax.swing.JFormattedTextField();
        presentation3JuniorField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        presentation1SeniorField = new javax.swing.JFormattedTextField();
        presentation2SeniorField = new javax.swing.JFormattedTextField();
        presentation3SeniorField = new javax.swing.JFormattedTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.border.title"))); // NOI18N

        presentation1Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(presentation1Label, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.presentation1Label.text")); // NOI18N

        presentation2Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(presentation2Label, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.presentation2Label.text")); // NOI18N

        presentation3Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(presentation3Label, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.presentation3Label.text")); // NOI18N

        homeStudentCountLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(homeStudentCountLabel, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.homeStudentCountLabel.text")); // NOI18N

        presentation1HomeStudentCountField.setColumns(5);
        presentation1HomeStudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1HomeStudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation2HomeStudentCountField.setColumns(5);
        presentation2HomeStudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2HomeStudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation3HomeStudentCountField.setColumns(5);
        presentation3HomeStudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3HomeStudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        homeStudentIncomeFeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(homeStudentIncomeFeeLabel, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.homeStudentIncomeFeeLabel.text")); // NOI18N
        homeStudentIncomeFeeLabel.setToolTipText(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.homeStudentIncomeFeeLabel.toolTipText")); // NOI18N

        presentation1HomeStudentFeeField.setColumns(5);
        presentation1HomeStudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1HomeStudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation2HomeStudentFeeField.setColumns(5);
        presentation2HomeStudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2HomeStudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation3HomeStudentFeeField.setColumns(5);
        presentation3HomeStudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3HomeStudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        overseasStudentCountLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(overseasStudentCountLabel, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.overseasStudentCountLabel.text")); // NOI18N

        presentation1OverseasStudentCountField.setColumns(5);
        presentation1OverseasStudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1OverseasStudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation2OverseasStudentCountField.setColumns(5);
        presentation2OverseasStudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2OverseasStudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation3OverseasStudentCountField.setColumns(5);
        presentation3OverseasStudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3OverseasStudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        overseasStudentIncomeFeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(overseasStudentIncomeFeeLabel, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.overseasStudentIncomeFeeLabel.text")); // NOI18N
        overseasStudentIncomeFeeLabel.setToolTipText(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.overseasStudentIncomeFeeLabel.toolTipText")); // NOI18N

        presentation1OverseasStudentFeeField.setColumns(5);
        presentation1OverseasStudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1OverseasStudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation2OverseasStudentFeeField.setColumns(5);
        presentation2OverseasStudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2OverseasStudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation3OverseasStudentFeeField.setColumns(5);
        presentation3OverseasStudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3OverseasStudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        juniorCostLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(juniorCostLabel, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.juniorCostLabel.text")); // NOI18N
        juniorCostLabel.setToolTipText(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.juniorCostLabel.toolTipText")); // NOI18N

        presentation1JuniorField.setColumns(5);
        presentation1JuniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1JuniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation2JuniorField.setColumns(5);
        presentation2JuniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2JuniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation3JuniorField.setColumns(5);
        presentation3JuniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3JuniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.jLabel1.toolTipText")); // NOI18N

        presentation1SeniorField.setColumns(5);
        presentation1SeniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1SeniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation2SeniorField.setColumns(5);
        presentation2SeniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2SeniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        presentation3SeniorField.setColumns(5);
        presentation3SeniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3SeniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(1, 1, 1)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(homeStudentCountLabel)
                        .add(0, 0, 0)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                            .add(presentation1HomeStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(presentation1Label))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(presentation2HomeStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, 0)
                                .add(presentation3HomeStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(18, 18, 18)
                                .add(presentation2Label)
                                .add(39, 39, 39)
                                .add(presentation3Label)
                                .add(34, 34, 34))))
                    .add(layout.createSequentialGroup()
                        .add(homeStudentIncomeFeeLabel)
                        .add(0, 0, 0)
                        .add(presentation1HomeStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation2HomeStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation3HomeStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(overseasStudentCountLabel)
                        .add(0, 0, 0)
                        .add(presentation1OverseasStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation2OverseasStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation3OverseasStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(overseasStudentIncomeFeeLabel)
                        .add(0, 0, 0)
                        .add(presentation1OverseasStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation2OverseasStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation3OverseasStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .add(0, 0, 0)
                        .add(presentation1SeniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation2SeniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation3SeniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(juniorCostLabel)
                        .add(0, 0, 0)
                        .add(presentation1JuniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation2JuniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(presentation3JuniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );

        layout.linkSize(new java.awt.Component[] {homeStudentCountLabel, homeStudentIncomeFeeLabel, jLabel1, juniorCostLabel, overseasStudentCountLabel, overseasStudentIncomeFeeLabel}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(presentation1Label)
                    .add(presentation2Label)
                    .add(presentation3Label))
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(homeStudentCountLabel)
                    .add(presentation1HomeStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation2HomeStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation3HomeStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(homeStudentIncomeFeeLabel)
                    .add(presentation1HomeStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation2HomeStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation3HomeStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(overseasStudentCountLabel)
                    .add(presentation1OverseasStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation2OverseasStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation3OverseasStudentCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(overseasStudentIncomeFeeLabel)
                    .add(presentation1OverseasStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation2OverseasStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation3OverseasStudentFeeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(juniorCostLabel)
                    .add(presentation1JuniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation2JuniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation3JuniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel1)
                    .add(presentation1SeniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation2SeniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(presentation3SeniorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel homeStudentCountLabel;
    private javax.swing.JLabel homeStudentIncomeFeeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel juniorCostLabel;
    private javax.swing.JLabel overseasStudentCountLabel;
    private javax.swing.JLabel overseasStudentIncomeFeeLabel;
    private javax.swing.JFormattedTextField presentation1HomeStudentCountField;
    private javax.swing.JFormattedTextField presentation1HomeStudentFeeField;
    private javax.swing.JFormattedTextField presentation1JuniorField;
    private javax.swing.JLabel presentation1Label;
    private javax.swing.JFormattedTextField presentation1OverseasStudentCountField;
    private javax.swing.JFormattedTextField presentation1OverseasStudentFeeField;
    private javax.swing.JFormattedTextField presentation1SeniorField;
    private javax.swing.JFormattedTextField presentation2HomeStudentCountField;
    private javax.swing.JFormattedTextField presentation2HomeStudentFeeField;
    private javax.swing.JFormattedTextField presentation2JuniorField;
    private javax.swing.JLabel presentation2Label;
    private javax.swing.JFormattedTextField presentation2OverseasStudentCountField;
    private javax.swing.JFormattedTextField presentation2OverseasStudentFeeField;
    private javax.swing.JFormattedTextField presentation2SeniorField;
    private javax.swing.JFormattedTextField presentation3HomeStudentCountField;
    private javax.swing.JFormattedTextField presentation3HomeStudentFeeField;
    private javax.swing.JFormattedTextField presentation3JuniorField;
    private javax.swing.JLabel presentation3Label;
    private javax.swing.JFormattedTextField presentation3OverseasStudentCountField;
    private javax.swing.JFormattedTextField presentation3OverseasStudentFeeField;
    private javax.swing.JFormattedTextField presentation3SeniorField;
    // End of variables declaration//GEN-END:variables

    private void makeFieldDirty(JFormattedTextField jFormattedTextField) {
	dirtyMap.put(jFormattedTextField, Boolean.TRUE);
    }
    
    private boolean isFieldDirty(JFormattedTextField jFormattedTextField) {
	Boolean isDirty = dirtyMap.get(jFormattedTextField);
	if (isDirty == null) {
	    //LOGGER.warning("no entry in dirtyMap for : " + jFormattedTextField);
	    isDirty = Boolean.FALSE;
	}
	return isDirty;
    }

}
