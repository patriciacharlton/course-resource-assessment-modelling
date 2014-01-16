package uk.ac.lkl.cram.ui.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import uk.ac.lkl.cram.model.EnumeratedLearningExperience;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.FormattedTextFieldAdapter;
import uk.ac.lkl.cram.ui.TextFieldAdapter;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("serial")
public class TLALearningDetailsVisualPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(TLALearningDetailsVisualPanel.class.getName());
    private static final Color VALID_COLOUR = new Color(0, 153, 51);
    public static final String PROP_VALID = "valid";

    private TLActivity tlActivity;
    
    /**
     * Creates new form TLALearningDetailsVisualPanel
     * @param tla 
     */
    public TLALearningDetailsVisualPanel(TLActivity tla) {
	tlActivity = tla;
        initComponents();
	tlActivityNameChanged();
	tlActivity.getLearningType().addPropertyChangeListener(new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		learningTypeSliderChanged();
	    }
	});	
	tlActivity.addPropertyChangeListener(new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		String property = pce.getPropertyName();
		if (property.equals(TLActivity.PROP_NAME)) {
		    tlActivityNameChanged();
		}
	    }
	});
	
	acquisitionSlider.setValue(tlActivity.getLearningType().getAcquisition());
        acquisitionSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int aquisition = source.getValue();
                tlActivity.getLearningType().setAcquisition(aquisition);			    
	    }
	});
	
	discussionSlider.setValue(tlActivity.getLearningType().getDiscussion());
        discussionSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int discussion = source.getValue();
                tlActivity.getLearningType().setDiscussion(discussion);	
	    }
	});
	
        collaborationSlider.setValue(tlActivity.getLearningType().getCollaboration());
	collaborationSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int collaboration = source.getValue();
                tlActivity.getLearningType().setCollaboration(collaboration);			    
	    }
	});
	
        inquirySlider.setValue(tlActivity.getLearningType().getInquiry());
	inquirySlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int inquiry = source.getValue();
                tlActivity.getLearningType().setInquiry(inquiry);			    
	    }
	});
	
        practiceSlider.setValue(tlActivity.getLearningType().getPractice());
	practiceSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int practice = source.getValue();
                tlActivity.getLearningType().setPractice(practice);			    
	    }
	});
	
	productionSlider.setValue(tlActivity.getLearningType().getProduction());
	productionSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int production = source.getValue();
                tlActivity.getLearningType().setProduction(production);			    
	    }
	});
	
	new TextFieldAdapter(tlaNameField) {

	    @Override
	    public void updateText(String text) {
		tlActivity.setName(text);
		checkValidity();
	    }
	};
	
	EnumeratedLearningExperience[] learningExperiences = {EnumeratedLearningExperience.PERSONALISED, EnumeratedLearningExperience.SOCIAL, EnumeratedLearningExperience.ONE_SIZE_FOR_ALL};
	ComboBoxModel<EnumeratedLearningExperience> cbModel = new DefaultComboBoxModel<>(learningExperiences);
	learningExperienceComboBox.setModel(cbModel);
	learningExperienceComboBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		EnumeratedLearningExperience ele = (EnumeratedLearningExperience) learningExperienceComboBox.getSelectedItem();
		tlActivity.setLearningExperience(ele);
		boolean enableMaxGroupSize = ele == EnumeratedLearningExperience.SOCIAL;
		maxGroupSizeTF.setEnabled(enableMaxGroupSize);
		maxGroupSizeLabel.setEnabled(enableMaxGroupSize);
		if (enableMaxGroupSize) {
		    maxGroupSizeTF.setValue(tlActivity.getMaximumGroupSize());
		} else {
		    maxGroupSizeTF.setValue(0);
		}
	    }
	});
	
	cbModel.setSelectedItem(tlActivity.getLearningExperience());
	learningExperienceComboBox.setRenderer(new LearningExperienceRenderer());
	maxGroupSizeTF.setValue(tlActivity.getMaximumGroupSize());
	maxGroupSizeTF.addFocusListener(new FocusAdapter() {
	    @Override
	    public void focusLost(FocusEvent e) {
		try {
		    maxGroupSizeTF.commitEdit();
		} catch (ParseException ex) {
		    LOGGER.log(Level.SEVERE, "Failed to parse", ex);
		}
	    }
	
	});
	new FormattedTextFieldAdapter(maxGroupSizeTF) {
	    @Override
	    public void updateValue(Object value) {
		Integer maxGroupSize = (int) value;
		tlActivity.setMaximumGroupSize(maxGroupSize);
	    }
	};
	
	
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
        learningTypeSliderChanged();
        
    }
    
    @Override
    public String getName() {
	return "Learning Details";
    }
    
    private void tlActivityNameChanged() {
	if (!tlaNameField.getText().equalsIgnoreCase(tlActivity.getName())) {
	    tlaNameField.setText(tlActivity.getName());
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
        learningExperienceComboBox = new javax.swing.JComboBox();
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

        learningExperienceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Personalised", "Social", "Same for All" }));

        maxGroupSizeLabel.setText("Max. Group Size:");
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

        jLabel1.setText("Acquisition:");

        acquisitionSlider.setMajorTickSpacing(10);
        acquisitionSlider.setPaintTicks(true);

        jLabel2.setText("Collaboration:");

        collaborationSlider.setMajorTickSpacing(10);
        collaborationSlider.setPaintTicks(true);

        jLabel3.setText("Discussion:");

        discussionSlider.setMajorTickSpacing(10);
        discussionSlider.setPaintTicks(true);

        jLabel4.setText("Inquiry:");

        inquirySlider.setMajorTickSpacing(10);
        inquirySlider.setPaintTicks(true);

        jLabel5.setText("Practice:");

        practiceSlider.setMajorTickSpacing(10);
        practiceSlider.setPaintTicks(true);

        totalLearningTypeField.setEditable(false);
        totalLearningTypeField.setColumns(4);
        totalLearningTypeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setText("Production:");

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

        normaliseButton.setText("Normalise");
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
                .addContainerGap(24, Short.MAX_VALUE))
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
                        .add(0, 0, Short.MAX_VALUE))))
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
    private javax.swing.JComboBox learningExperienceComboBox;
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
	if (totalLearningType != 100) {
	    //LOGGER.info("totalLearningType: " + totalLearningType);
	    firePropertyChange(PROP_VALID, true, false);
	    return;
	}
	if (tlaNameField.getText().isEmpty()) {
	    //LOGGER.info("tlaNameField: " + tlaNameField.getText());
	    firePropertyChange(PROP_VALID, true, false);
	    return;
	}
	firePropertyChange(PROP_VALID, false, true);
    }
    
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        final JFrame frame = new JFrame("Enter Learning Details");
        frame.add(new TLALearningDetailsVisualPanel(new TLActivity("Dummy TLA")));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

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

    private class LearningExperienceRenderer extends JLabel implements ListCellRenderer {
	private Map<EnumeratedLearningExperience, String> renditionMap = new HashMap<>();
	public LearningExperienceRenderer() {
	    setOpaque(true);
	    renditionMap.put(EnumeratedLearningExperience.PERSONALISED, "Personalised");
	    renditionMap.put(EnumeratedLearningExperience.SOCIAL, "Social");
	    renditionMap.put(EnumeratedLearningExperience.ONE_SIZE_FOR_ALL, "Same for All");
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    //The value should be an EnumeratedLearningExperience
	    EnumeratedLearningExperience ele = (EnumeratedLearningExperience) value;
	    setText(renditionMap.get(ele));
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
