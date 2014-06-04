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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.CompoundEdit;
import uk.ac.lkl.cram.model.EnumeratedLearningExperience;
import uk.ac.lkl.cram.model.LearningType;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.FormattedTextFieldAdapter;
import uk.ac.lkl.cram.ui.SelectAllAdapter;
import uk.ac.lkl.cram.ui.TextFieldAdapter;
import uk.ac.lkl.cram.ui.undo.PluggableUndoableEdit;

/**
 * This class provides the rendering for the wizard step where the user enters
 * the basic details about the teaching and learning activity, including its
 * name, learning types, and learning experience. 
 * @see TLALearningDetailsWizardPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class TLALearningDetailsVisualPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(TLALearningDetailsVisualPanel.class.getName());
    /**
     * The colour to render a valid distribution of learning types
     */
    private static final Color VALID_COLOUR = new Color(0, 153, 51);
    /**
     * The property used to manage the validity of the data that the user has entered.
     * See the checkValidity() method.
     */
    public static final String PROP_VALID = "valid";
    /**
     * The compound edit that keeps track of the user's changes to the model
     */
    private final CompoundEdit compoundEdit;
    /**
     * The line item that is being edited
     */
    private final TLALineItem lineItem;
    
    TLALearningDetailsVisualPanel(TLALineItem lineItem) {
        this(lineItem, new CompoundEdit());
    }
    
    /**
     * Creates new form TLALearningDetailsVisualPanel
     * @param tlaLineItem the line item to be edited
     * @param cEdit compound edit that keeps track of the user's changes to the model
     */
    public TLALearningDetailsVisualPanel(TLALineItem tlaLineItem, CompoundEdit cEdit) {
	this.lineItem = tlaLineItem;
        initComponents();
        this.compoundEdit = cEdit;
        addActivityListeners();
        //When the line item's activity changes, add listeners to the new activity
        lineItem.addPropertyChangeListener(TLALineItem.PROP_ACTIVITY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                addActivityListeners();
            }
        });
        //Set the renderer for the combo box
        learningExperienceComboBox.setRenderer(new LearningExperienceRenderer());
        //Enable max group size if the learning experience is social
        boolean enableMaxGroupSize = learningExperienceComboBox.getSelectedItem() == EnumeratedLearningExperience.SOCIAL;
        maxGroupSizeTF.setEnabled(enableMaxGroupSize);
	//Add a focus listener to the textfield to ensure edits are committed
        //when the focus is lost
        maxGroupSizeTF.addFocusListener(new SelectAllAdapter() {
	    @Override
	    public void focusLost(FocusEvent e) {
		try {
		    maxGroupSizeTF.commitEdit();
		} catch (ParseException ex) {
		    LOGGER.log(Level.SEVERE, "Failed to parse", ex);
		}
	    }
	
	});
        	
	//A factory to return the formatter for the total learning experience field
        final JFormattedTextField.AbstractFormatter learningFieldFormatter = new JFormattedTextField.AbstractFormatter() {

            @Override
            public Object stringToValue(String string) throws ParseException {
                return string;
            }

            @Override
            public String valueToString(Object o) throws ParseException {
                return String.valueOf(o) + '%';
            }
        };
        AbstractFormatterFactory aff = new JFormattedTextField.AbstractFormatterFactory() {

            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField jftf) {
                return learningFieldFormatter;
            }
        };
        totalLearningTypeField.setFormatterFactory(aff);
        //Update the activity with the current values of the sliders
        learningTypeSliderChanged();      
    }
    
    /**
     * Add listeners to the activity
     */
    private void addActivityListeners() {
        //Add a listener for when the learning type changes
	lineItem.getActivity().getLearningType().addPropertyChangeListener(new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		learningTypeSliderChanged();
	    }
	});
        //Add a listener for when the name of the activity changes
	lineItem.getActivity().addPropertyChangeListener(new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		String property = pce.getPropertyName();
		if (property.equals(TLActivity.PROP_NAME)) {
		    tlActivityNameChanged();
		}
	    }
	});
	
        //Set the value of the slider from the activity
	acquisitionSlider.setValue(lineItem.getActivity().getLearningType().getAcquisition());
        //Add a listener to the slider so the value in the activity is changed
        acquisitionSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int acquisition = source.getValue();
                LearningType lt = lineItem.getActivity().getLearningType();
                //Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lt, "acquisition", acquisition);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'acquisition' of " + lt, ex);
		}
                //Set the value in the model
                lt.setAcquisition(acquisition);			    
	    }
	});
	//Repeat
	discussionSlider.setValue(lineItem.getActivity().getLearningType().getDiscussion());
        discussionSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int discussion = source.getValue();
                LearningType lt = lineItem.getActivity().getLearningType();
                //Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lt, "discussion", discussion);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'discussion' of " + lt, ex);
		}
                //Set the value in the model
                lt.setDiscussion(discussion);	
	    }
	});
	
        collaborationSlider.setValue(lineItem.getActivity().getLearningType().getCollaboration());
	collaborationSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int collaboration = source.getValue();
                LearningType lt = lineItem.getActivity().getLearningType();
                //Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lt, "collaboration", collaboration);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'collaboration' of " + lt, ex);
		}
                //Set the value in the model
                lt.setCollaboration(collaboration);			    
	    }
	});
	
        inquirySlider.setValue(lineItem.getActivity().getLearningType().getInquiry());
	inquirySlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int inquiry = source.getValue();
                LearningType lt = lineItem.getActivity().getLearningType();
                //Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lt, "inquiry", inquiry);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'inquiry' of " + lt, ex);
		}
                //Set the value in the model
                lt.setInquiry(inquiry);			    
	    }
	});
	
        practiceSlider.setValue(lineItem.getActivity().getLearningType().getPractice());
	practiceSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int practice = source.getValue();
                LearningType lt = lineItem.getActivity().getLearningType();
                //Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lt, "practice", practice);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'practice' of " + lt, ex);
		}
                //Set the value in the model
                lt.setPractice(practice);			    
	    }
	});
	
	productionSlider.setValue(lineItem.getActivity().getLearningType().getProduction());
	productionSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int production = source.getValue();
                LearningType lt = lineItem.getActivity().getLearningType();
                //Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lt, "production", production);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'production' of " + lt, ex);
		}
                //Set the value in the model
                lt.setProduction(production);			    
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
                if (!lineItem.getName().equals(text)) {
                    lineItem.getActivity().setName(text);
                    checkValidity();
                }
            }
        };

        //Add a listener to the learning experience combo box
	learningExperienceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get the selected learning experience from the combo box
                EnumeratedLearningExperience ele = (EnumeratedLearningExperience) learningExperienceComboBox.getSelectedItem();
                //Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity(), "learningExperience", ele);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'learningExperience' of " + lineItem.getActivity(), ex);
		}
                //Set the property of the TLA
                lineItem.getActivity().setLearningExperience(ele);
                //Enable max group size if the learning experience is social
                boolean enableMaxGroupSize = ele == EnumeratedLearningExperience.SOCIAL;
                maxGroupSizeTF.setEnabled(enableMaxGroupSize);
                maxGroupSizeLabel.setEnabled(enableMaxGroupSize);
                //Set the value in the text field
                if (enableMaxGroupSize) {
                    maxGroupSizeTF.setValue(lineItem.getActivity().getMaximumGroupSize());
                } else {
                    switch (ele) {
                        case PERSONALISED:
                            maxGroupSizeTF.setValue(1);
                            break;
                        case ONE_SIZE_FOR_ALL:
                            maxGroupSizeTF.setValue(0);
                            break;
                    }
                }
                checkValidity();
            }
        });
        //Set the value of the text field from the activity
        maxGroupSizeTF.setValue(lineItem.getActivity().getMaximumGroupSize());
        //Add a document listener to the text field so that the value is updated
        //in the activity
	new FormattedTextFieldAdapter(maxGroupSizeTF) {
	    @Override
	    public void updateValue(Object value) {
		Integer maxGroupSize = (int) value;
		//Create an undoable edit for the change, and add it to the compound edit
		try {
                    PluggableUndoableEdit edit = new PluggableUndoableEdit(lineItem.getActivity(), "maximumGroupSize", maxGroupSize);
                    compoundEdit.addEdit(edit);		    
		} catch (IntrospectionException ex) {
		    LOGGER.log(Level.WARNING, "Unable to create undo for property 'maximumGroupSize' of " + lineItem.getActivity(), ex);
		}
                lineItem.getActivity().setMaximumGroupSize(maxGroupSize);
                checkValidity();
	    }
	};
        //Get the name from the activity and put it into the text field
	tlActivityNameChanged();
    }
    
    @Override
    public String getName() {
	return java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle").getString("LEARNING DETAILS");
    }
    
    /**
     * The combox box model that contains the enumerated learning experiences, 
     * with the selected item set to be the current value from the lineitem's activity
     * @return a combobox model for use by the combobox menu
     */
    private ComboBoxModel<EnumeratedLearningExperience> getComboBoxModel() {
	EnumeratedLearningExperience[] learningExperiences = {EnumeratedLearningExperience.PERSONALISED, EnumeratedLearningExperience.SOCIAL, EnumeratedLearningExperience.ONE_SIZE_FOR_ALL};
	final ComboBoxModel<EnumeratedLearningExperience> cbModel = new DefaultComboBoxModel<>(learningExperiences);
	cbModel.setSelectedItem(lineItem.getActivity().getLearningExperience());
        lineItem.addPropertyChangeListener(TLALineItem.PROP_ACTIVITY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                cbModel.setSelectedItem(lineItem.getActivity().getLearningExperience());
            }
        });
	return cbModel;
    }
    
    /**
     * If the contents of the text field have changed, update the value of the activity
     */
    private void tlActivityNameChanged() {
	//If the field has focus, do not update, as it's the source of the change
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        learningExperiencePanel = new javax.swing.JPanel();
        learningExperienceComboBox = new javax.swing.JComboBox<EnumeratedLearningExperience>();
        maxGroupSizeLabel = new javax.swing.JLabel();
        maxGroupSizeTF = new javax.swing.JFormattedTextField();
        learningTypePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        acquisitionSlider = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        collaborationSlider = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        discussionSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        inquirySlider = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        practiceSlider = new javax.swing.JSlider();
        totalLearningTypeField = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        productionSlider = new javax.swing.JSlider();
        acquisitionTF = new javax.swing.JTextField();
        collaborationTF = new javax.swing.JTextField();
        discussionTF = new javax.swing.JTextField();
        inquiryTF = new javax.swing.JTextField();
        practiceTF = new javax.swing.JTextField();
        productionTF = new javax.swing.JTextField();
        normaliseButton = new javax.swing.JButton();
        tlaNamePanel = new javax.swing.JPanel();
        tlaNameField = new javax.swing.JTextField();

        learningExperiencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nature of Learning Experience"));

        learningExperienceComboBox.setModel(getComboBoxModel());
        learningExperienceComboBox.setToolTipText("<html>A <em>Personalised</em> activity is one in which each student engages directly with a tutor.<br/>\nFor a <em>Same for All</em> activity all students interact in the same way with a tutor or the materials.<br/>\nFor a <em>Social</em> activity students are divided into tutor groups.\n</html>");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle"); // NOI18N
        maxGroupSizeLabel.setText(bundle.getString("MAX. GROUP SIZE:")); // NOI18N
        maxGroupSizeLabel.setEnabled(false);

        maxGroupSizeTF.setColumns(4);
        maxGroupSizeTF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        maxGroupSizeTF.setEnabled(false);

        org.jdesktop.layout.GroupLayout learningExperiencePanelLayout = new org.jdesktop.layout.GroupLayout(learningExperiencePanel);
        learningExperiencePanel.setLayout(learningExperiencePanelLayout);
        learningExperiencePanelLayout.setHorizontalGroup(
            learningExperiencePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learningExperiencePanelLayout.createSequentialGroup()
                .add(learningExperienceComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
            .add(learningExperiencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(maxGroupSizeLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(maxGroupSizeTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        learningExperiencePanelLayout.setVerticalGroup(
            learningExperiencePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learningExperiencePanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(learningExperienceComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(learningExperiencePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(maxGroupSizeLabel)
                    .add(maxGroupSizeTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        learningTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("% each learning type it elicits"));

        jLabel1.setText(bundle.getString("ACQUISITION:")); // NOI18N
        jLabel1.setToolTipText("<html>Learning through <em>acquisition</em> is what learners<br/>\nare doing when they are listening to a lecture<br/>\nor podcast, reading from books or websites,<br/>\nand watching demos or videos.\n</html>");

        acquisitionSlider.setMajorTickSpacing(10);
        acquisitionSlider.setPaintTicks(true);

        jLabel2.setText(bundle.getString("COLLABORATION:")); // NOI18N
        jLabel2.setToolTipText("<html>Learning through <em>collaboration</em> embraces mainly<br/>\ndiscussion, practice, and production. Building on<br/>\ninvestigations and acquisition it is about taking<br/>\npart in the process of knowledge building itself.</html>");

        collaborationSlider.setMajorTickSpacing(10);
        collaborationSlider.setPaintTicks(true);

        jLabel3.setText(bundle.getString("DISCUSSION:")); // NOI18N
        jLabel3.setToolTipText("<html>Learning through <em>discussion</em> requires the learner<br/>\nto articulate their ideas and questions, and to<br/>\nchallenge and respond to the ideas and questions<br/>\nfrom the teacher, and/or from their peers.</html>");

        discussionSlider.setMajorTickSpacing(10);
        discussionSlider.setPaintTicks(true);

        jLabel4.setText(bundle.getString("INQUIRY:")); // NOI18N
        jLabel4.setToolTipText("<html>Learning through <em>inquiry</em> guides the learner to<br/>\nexplore, compare and critique the texts, documents<br/>\nand resources that reflect the concepts and<br/>\nideas being taught.</html>");

        inquirySlider.setMajorTickSpacing(10);
        inquirySlider.setPaintTicks(true);

        jLabel5.setText(bundle.getString("PRACTICE:")); // NOI18N
        jLabel5.setToolTipText("<html>Learning through <em>practice</em> enables the learner to<br/>\nadapt their actions to the task goal, and use the<br/>\nfeedback to improve their next action. Feedback<br/>\nmay come from self-reflection, from peers, from<br/>\nthe teacher, or from the activity itself, if it shows<br/>\nthem how to improve the result of their action in<br/>\nrelation to the goal.</html>");

        practiceSlider.setMajorTickSpacing(10);
        practiceSlider.setPaintTicks(true);

        totalLearningTypeField.setEditable(false);
        totalLearningTypeField.setColumns(4);
        totalLearningTypeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalLearningTypeField.setText("0");

        jLabel6.setText(bundle.getString("PRODUCTION:")); // NOI18N
        jLabel6.setToolTipText("<html>Learning through <em>production</em> is the way the<br/>\nteacher motivates the learner to consolidate what<br/>\nthey have learned by articulating their current<br/>\nconceptual understanding and how they used it<br/>\nin practice.</html>");

        productionSlider.setMajorTickSpacing(10);
        productionSlider.setPaintTicks(true);

        acquisitionTF.setEditable(false);
        acquisitionTF.setColumns(4);
        acquisitionTF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, acquisitionSlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), acquisitionTF, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        collaborationTF.setEditable(false);
        collaborationTF.setColumns(4);
        collaborationTF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, collaborationSlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), collaborationTF, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        discussionTF.setEditable(false);
        discussionTF.setColumns(4);
        discussionTF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, discussionSlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), discussionTF, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        inquiryTF.setEditable(false);
        inquiryTF.setColumns(4);
        inquiryTF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, inquirySlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), inquiryTF, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        practiceTF.setEditable(false);
        practiceTF.setColumns(4);
        practiceTF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, practiceSlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), practiceTF, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        productionTF.setEditable(false);
        productionTF.setColumns(4);
        productionTF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, productionSlider, org.jdesktop.beansbinding.ELProperty.create("${value}"), productionTF, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        normaliseButton.setText(bundle.getString("NORMALISE")); // NOI18N
        normaliseButton.setEnabled(false);
        normaliseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normaliseButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout learningTypePanelLayout = new org.jdesktop.layout.GroupLayout(learningTypePanel);
        learningTypePanel.setLayout(learningTypePanelLayout);
        learningTypePanelLayout.setHorizontalGroup(
            learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learningTypePanelLayout.createSequentialGroup()
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel4)
                    .add(jLabel5)
                    .add(jLabel6))
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, inquirySlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, discussionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, collaborationSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, acquisitionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(practiceSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(productionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(acquisitionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(collaborationTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discussionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(inquiryTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(practiceTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(productionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, learningTypePanelLayout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(normaliseButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(totalLearningTypeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        learningTypePanelLayout.linkSize(new java.awt.Component[] {productionTF, totalLearningTypeField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        learningTypePanelLayout.setVerticalGroup(
            learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learningTypePanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel1)
                    .add(acquisitionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(acquisitionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel2)
                    .add(collaborationSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(collaborationTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel3)
                    .add(discussionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discussionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel4)
                    .add(inquirySlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(inquiryTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(practiceSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5)
                    .add(practiceTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(productionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6)
                    .add(productionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(totalLearningTypeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(normaliseButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tlaNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Teaching & Learning Activity Name"));

        org.jdesktop.layout.GroupLayout tlaNamePanelLayout = new org.jdesktop.layout.GroupLayout(tlaNamePanel);
        tlaNamePanel.setLayout(tlaNamePanelLayout);
        tlaNamePanelLayout.setHorizontalGroup(
            tlaNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tlaNamePanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(tlaNameField)
                .add(0, 0, 0))
        );
        tlaNamePanelLayout.setVerticalGroup(
            tlaNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tlaNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tlaNamePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(learningTypePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(learningExperiencePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(tlaNamePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(learningTypePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(learningExperiencePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 214, Short.MAX_VALUE))))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void normaliseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normaliseButtonActionPerformed
        normaliseLearningTypes();
    }//GEN-LAST:event_normaliseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider acquisitionSlider;
    private javax.swing.JTextField acquisitionTF;
    private javax.swing.JSlider collaborationSlider;
    private javax.swing.JTextField collaborationTF;
    private javax.swing.JSlider discussionSlider;
    private javax.swing.JTextField discussionTF;
    private javax.swing.JSlider inquirySlider;
    private javax.swing.JTextField inquiryTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JComboBox<EnumeratedLearningExperience> learningExperienceComboBox;
    private javax.swing.JPanel learningExperiencePanel;
    private javax.swing.JPanel learningTypePanel;
    private javax.swing.JLabel maxGroupSizeLabel;
    private javax.swing.JFormattedTextField maxGroupSizeTF;
    private javax.swing.JButton normaliseButton;
    private javax.swing.JSlider practiceSlider;
    private javax.swing.JTextField practiceTF;
    private javax.swing.JSlider productionSlider;
    private javax.swing.JTextField productionTF;
    private javax.swing.JTextField tlaNameField;
    private javax.swing.JPanel tlaNamePanel;
    private javax.swing.JFormattedTextField totalLearningTypeField;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    /**
     * The values of the learning experience type sliders have changed
     * Sum the values for the total field, and change its colour
     * accordingly
     * If necessary, enable the normalise button
     */
    private void learningTypeSliderChanged() {
        int totalLearningType = acquisitionSlider.getValue();
        totalLearningType += collaborationSlider.getValue();
        totalLearningType += discussionSlider.getValue();
        totalLearningType += practiceSlider.getValue();
        totalLearningType += inquirySlider.getValue();
	totalLearningType += productionSlider.getValue();
        totalLearningTypeField.setValue(totalLearningType);
        Color fieldColour; 
        if (totalLearningType == 100) {
            fieldColour = VALID_COLOUR;
            normaliseButton.setEnabled(false);
        } else {
            fieldColour = Color.RED;
            if (totalLearningType == 0) {
                normaliseButton.setEnabled(false);
            } else {
                normaliseButton.setEnabled(true);
            }
        }
        totalLearningTypeField.setForeground(fieldColour);
	checkValidity();
    }

    private void checkValidity() {
	Integer totalLearningType = (Integer) totalLearningTypeField.getValue();
	//If the total learning type isn't 100 then we're not valid
        if (totalLearningType != 100) {
	    firePropertyChange(PROP_VALID, true, false);
	    return;
	}
	//If the name field is empty, then we're not valid
        if (tlaNameField.getText().isEmpty()) {
	    firePropertyChange(PROP_VALID, true, false);
	    return;
	}
        EnumeratedLearningExperience ele = (EnumeratedLearningExperience) learningExperienceComboBox.getSelectedItem();
        //If it's a social learning experience, we have to have a group size of more than 1 to be valid
        if (ele == EnumeratedLearningExperience.SOCIAL) {
          Integer maxGroupSize = (Integer) maxGroupSizeTF.getValue();
          if (maxGroupSize <= 1) {
              firePropertyChange(PROP_VALID, true, false);
              return;
          }
        }
	firePropertyChange(PROP_VALID, false, true);
    }
    
    /**
     * For testing purposes only
     * @param args (ignored)
     */
    public static void main(String args[]) {

        final JFrame frame = new JFrame(java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle").getString("ENTER LEARNING DETAILS"));
        TLActivity activity = new TLActivity("Dummy TLA");
        TLALineItem lineItem = new TLALineItem();
        lineItem.setActivity(activity);
        frame.add(new TLALearningDetailsVisualPanel(lineItem, new CompoundEdit()));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    /**
     * Take the current values from the sliders and force them to sum to 100
     */
    private void normaliseLearningTypes() {
        Map<JSlider, Integer> sliderTable = new HashMap<>();
        sliderTable.put(acquisitionSlider, acquisitionSlider.getValue());
        sliderTable.put(collaborationSlider, collaborationSlider.getValue());
        sliderTable.put(discussionSlider, discussionSlider.getValue());
        sliderTable.put(inquirySlider, inquirySlider.getValue());
        sliderTable.put(practiceSlider, practiceSlider.getValue());
        sliderTable.put(productionSlider, productionSlider.getValue());
        float sum = 0;
        for (Integer sliderValue : sliderTable.values()) {
            sum += sliderValue;
        }
        int normalisedSum = 0;
        for (JSlider jSlider : sliderTable.keySet()) {
            float normalised = jSlider.getValue()/sum;
            jSlider.setValue((int) (normalised * 100));
            normalisedSum += jSlider.getValue();
        }
        //Catch rounding down
        if (normalisedSum < 100) {
            int delta = 100 - normalisedSum;
            for (JSlider jSlider : sliderTable.keySet()) {
                if (jSlider.getValue() != 0) {
                    jSlider.setValue(jSlider.getValue()  + delta);
                    delta = 0;
                }
            }
        }
    }

    /**
     * Renderer for the combo box that uses the correct labels for the 
     * enumerated learning experiences
     */
    private class LearningExperienceRenderer extends JLabel implements ListCellRenderer<EnumeratedLearningExperience> {
	private Map<EnumeratedLearningExperience, String> renditionMap = new HashMap<>();
	LearningExperienceRenderer() {
	    setOpaque(true);
	    renditionMap.put(EnumeratedLearningExperience.PERSONALISED, java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle").getString("PERSONALISED"));
	    renditionMap.put(EnumeratedLearningExperience.SOCIAL, java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle").getString("SOCIAL"));
	    renditionMap.put(EnumeratedLearningExperience.ONE_SIZE_FOR_ALL, java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle").getString("SAME FOR ALL"));
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends EnumeratedLearningExperience> list, EnumeratedLearningExperience value, int index, boolean isSelected, boolean cellHasFocus) {
	    setText(renditionMap.get(value));
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    } else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }
	    return this;
	}
    }
}
