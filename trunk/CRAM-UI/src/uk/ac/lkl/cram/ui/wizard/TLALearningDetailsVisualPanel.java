package uk.ac.lkl.cram.ui.wizard;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.ac.lkl.cram.model.EnumeratedLearningExperience;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.TextFieldAdapter;

/**
 * $Date$
 * @author Bernard Horan
 */
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
	UIManager.put("Slider.paintValue", true);
        initComponents();
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
	
	collaborationSlider.setValue(tlActivity.getLearningType().getCollaboration());
	collaborationSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent ce) {
		JSlider source = (JSlider) ce.getSource();
                int collaboration = source.getValue();
                tlActivity.getLearningType().setCollaboration(collaboration);			    
	    }
	});
	
	new TextFieldAdapter(tlaNameField) {

	    @Override
	    public void updateText(String text) {
		tlActivity.setName(text);
		checkValidity();
	    }
	};
	
	ActionListener learningExperienceListener = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		EnumeratedLearningExperience le = EnumeratedLearningExperience.valueOf(actionCommand);
		tlActivity.setLearningExperience(le);
	    }
	};
	
	personalisedRB.addActionListener(learningExperienceListener);
	personalisedRB.setActionCommand(EnumeratedLearningExperience.PERSONALISED.name());
	socialRB.addActionListener(learningExperienceListener);
	socialRB.setActionCommand(EnumeratedLearningExperience.SOCIAL.name());
	osfaRB.addActionListener(learningExperienceListener);
	osfaRB.setActionCommand(EnumeratedLearningExperience.ONE_SIZE_FOR_ALL.name());
	EnumeratedLearningExperience le = tlActivity.getLearningExperience();
	switch (le) {
	    case PERSONALISED: {
		personalisedRB.setSelected(true);
		break;
	    }
	    case SOCIAL: {
		socialRB.setSelected(true);
		break;
	    }
	    case ONE_SIZE_FOR_ALL:
		osfaRB.setSelected(true);
		break;
	}
	
	
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
	return "Enter Learning Details";
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

        learningExperienceBG = new javax.swing.ButtonGroup();
        learningExperiencePanel = new javax.swing.JPanel();
        personalisedRB = new javax.swing.JRadioButton();
        socialRB = new javax.swing.JRadioButton();
        osfaRB = new javax.swing.JRadioButton();
        learningTypePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        acquisitionSlider = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        inquirySlider = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        discussionSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        practiceSlider = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        productionSlider = new javax.swing.JSlider();
        totalLearningTypeField = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        collaborationSlider = new javax.swing.JSlider();
        tlaNamePanel = new javax.swing.JPanel();
        tlaNameField = new javax.swing.JTextField();

        learningExperiencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nature of Learning Experience"));

        learningExperienceBG.add(personalisedRB);
        personalisedRB.setText("Personalised");

        learningExperienceBG.add(socialRB);
        socialRB.setText("Social");

        learningExperienceBG.add(osfaRB);
        osfaRB.setText("One Size fits All");

        org.jdesktop.layout.GroupLayout learningExperiencePanelLayout = new org.jdesktop.layout.GroupLayout(learningExperiencePanel);
        learningExperiencePanel.setLayout(learningExperiencePanelLayout);
        learningExperiencePanelLayout.setHorizontalGroup(
            learningExperiencePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learningExperiencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(learningExperiencePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(personalisedRB)
                    .add(socialRB)
                    .add(osfaRB))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        learningExperiencePanelLayout.setVerticalGroup(
            learningExperiencePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learningExperiencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(personalisedRB)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(socialRB)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(osfaRB)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        learningTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("% each learning type it elicits"));

        jLabel1.setText("Acquisition:");

        acquisitionSlider.setMajorTickSpacing(10);
        acquisitionSlider.setPaintTicks(true);

        jLabel2.setText("Inquiry:");

        inquirySlider.setMajorTickSpacing(10);
        inquirySlider.setPaintTicks(true);

        jLabel3.setText("Discussion:");

        discussionSlider.setMajorTickSpacing(10);
        discussionSlider.setPaintTicks(true);

        jLabel4.setText("Practice:");

        practiceSlider.setMajorTickSpacing(10);
        practiceSlider.setPaintTicks(true);

        jLabel5.setText("Production:");

        productionSlider.setMajorTickSpacing(10);
        productionSlider.setPaintTicks(true);

        totalLearningTypeField.setEditable(false);
        totalLearningTypeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setText("Collaboration:");

        collaborationSlider.setMajorTickSpacing(10);
        collaborationSlider.setPaintTicks(true);

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
                .add(0, 0, Short.MAX_VALUE)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, practiceSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, discussionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, inquirySlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, acquisitionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(productionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(collaborationSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .add(0, 0, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, learningTypePanelLayout.createSequentialGroup()
                .addContainerGap(172, Short.MAX_VALUE)
                .add(totalLearningTypeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        learningTypePanelLayout.setVerticalGroup(
            learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learningTypePanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel1)
                    .add(acquisitionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel2)
                    .add(inquirySlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel3)
                    .add(discussionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel4)
                    .add(practiceSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel5)
                    .add(productionSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(learningTypePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jLabel6)
                    .add(collaborationSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(totalLearningTypeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(learningTypePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(learningExperiencePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0))
            .add(tlaNamePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(tlaNamePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(learningExperiencePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(learningTypePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 284, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider acquisitionSlider;
    private javax.swing.JSlider collaborationSlider;
    private javax.swing.JSlider discussionSlider;
    private javax.swing.JSlider inquirySlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.ButtonGroup learningExperienceBG;
    private javax.swing.JPanel learningExperiencePanel;
    private javax.swing.JPanel learningTypePanel;
    private javax.swing.JRadioButton osfaRB;
    private javax.swing.JRadioButton personalisedRB;
    private javax.swing.JSlider practiceSlider;
    private javax.swing.JSlider productionSlider;
    private javax.swing.JRadioButton socialRB;
    private javax.swing.JTextField tlaNameField;
    private javax.swing.JPanel tlaNamePanel;
    private javax.swing.JFormattedTextField totalLearningTypeField;
    // End of variables declaration//GEN-END:variables

    private void learningTypeSliderChanged() {
        int totalLearningType = acquisitionSlider.getValue();
        totalLearningType += inquirySlider.getValue();
        totalLearningType += discussionSlider.getValue();
        totalLearningType += productionSlider.getValue();
        totalLearningType += practiceSlider.getValue();
	totalLearningType += collaborationSlider.getValue();
        totalLearningTypeField.setValue(totalLearningType);
        Color fieldColour; 
        if (totalLearningType == 100) {
            fieldColour = VALID_COLOUR;
        } else {
            fieldColour = Color.RED;
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
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
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

}
