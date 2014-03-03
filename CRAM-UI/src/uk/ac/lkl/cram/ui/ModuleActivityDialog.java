package uk.ac.lkl.cram.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.KeyStroke;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.AbstractModuleTime;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModuleLineItem;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("serial")
public class ModuleActivityDialog extends javax.swing.JDialog {
    private static final Logger LOGGER = Logger.getLogger(ModuleActivityDialog.class.getName());

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private final Module module;
    private final ModuleLineItem moduleLineItem;

    /**
     * Creates new form ModuleActivityDialog
     * @param parent
     * @param modal
     * @param m
     * @param li  
     */
    public ModuleActivityDialog(java.awt.Frame parent, boolean modal, Module m, ModuleLineItem li) {
        super(parent, modal);
        this.module = m;
        initComponents();
        this.moduleLineItem = li;
        SelectAllAdapter saa = new SelectAllAdapter();
	
	moduleActivityNameField.setText(moduleLineItem.getName());
	moduleActivityNameField.addFocusListener(saa);
	new TextFieldAdapter(moduleActivityNameField) {

	    @Override
	    public void updateText(String value) {		
		moduleLineItem.setName(value);
	    }
	};	


        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
        
        JFormattedTextField.AbstractFormatterFactory aff = new JFormattedTextField.AbstractFormatterFactory() {

            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField jftf) {
                return new PercentFormatter();
            }
        };
        
        InputVerifier verifier = new PercentVerifier();
        
        //Support Fields
	final JFormattedTextField[] weeklySupportFields = new JFormattedTextField[3];
	weeklySupportFields[0] = presentation1WeeklySupport;
	weeklySupportFields[1] = presentation2WeeklySupport;
	weeklySupportFields[2] = presentation3WeeklySupport;
	final JFormattedTextField[] nonWeeklySupportFields = new JFormattedTextField[3];
	nonWeeklySupportFields[0] = presentation1NonWeeklySupport;
	nonWeeklySupportFields[1] = presentation2NonWeeklySupport;
	nonWeeklySupportFields[2] = presentation3NonWeeklySupport;
	JFormattedTextField[] higherCostSupportFields = new JFormattedTextField[3];
	higherCostSupportFields[0] = presentation1HigherCostSupport;
	higherCostSupportFields[1] = presentation2HigherCostSupport;
	higherCostSupportFields[2] = presentation3HigherCostSupport;
	JFormattedTextField[] lowerCostSupportFields = new JFormattedTextField[3];
	lowerCostSupportFields[0] = presentation1LowerCostSupport;
	lowerCostSupportFields[1] = presentation2LowerCostSupport;
	lowerCostSupportFields[2] = presentation3LowerCostSupport;
	int supportIndex = 0;
	for (final ModulePresentation modulePresentation : module.getModulePresentations()) {
	    weeklySupportFields[supportIndex].setValue(li.getSupportTime(modulePresentation).getWeekly());
	    weeklySupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(weeklySupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		    moduleLineItem.getSupportTime(modulePresentation).setWeekly((Float) value);
		}
	    };
	    
	    nonWeeklySupportFields[supportIndex].setValue(li.getSupportTime(modulePresentation).getNonWeekly());
	    nonWeeklySupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(nonWeeklySupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		    moduleLineItem.getSupportTime(modulePresentation).setNonWeekly((Float) value);
		}
	    };
	    
	    higherCostSupportFields[supportIndex].setValue(li.getSupportTime(modulePresentation).getSeniorRate());
	    higherCostSupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(higherCostSupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		   moduleLineItem.getSupportTime(modulePresentation).setSeniorRate((Integer) value);
		}
	    };
	    higherCostSupportFields[supportIndex].setFormatterFactory(aff);
            higherCostSupportFields[supportIndex].setInputVerifier(verifier);
	    li.getSupportTime(modulePresentation).addPropertyChangeListener(AbstractModuleTime.PROP_SENIOR_RATE, new HigherCostPropertyListener(higherCostSupportFields[supportIndex]));
	    
	    lowerCostSupportFields[supportIndex].setValue(li.getSupportTime(modulePresentation).getJuniorRate());
	    lowerCostSupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(lowerCostSupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		    moduleLineItem.getSupportTime(modulePresentation).setJuniorRate((Integer) value);
		}
	    };
	    lowerCostSupportFields[supportIndex].setFormatterFactory(aff);
	    lowerCostSupportFields[supportIndex].setInputVerifier(verifier);
	    li.getSupportTime(modulePresentation).addPropertyChangeListener(AbstractModuleTime.PROP_SENIOR_RATE, new LowerCostPropertyListener(lowerCostSupportFields[supportIndex]));
            supportIndex++;
	}
        
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        moduleActivityNamePanel = new javax.swing.JPanel();
        moduleActivityNameField = new javax.swing.JTextField();
        supportPanel = new javax.swing.JPanel();
        blankSupportLabel = new javax.swing.JLabel();
        hoursPerWeekSupportLabel = new org.jdesktop.swingx.JXLabel();
        nonWeeklyHoursSupportLabel = new org.jdesktop.swingx.JXLabel();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        presentation1Label1 = new javax.swing.JLabel();
        presentation1WeeklySupport = new javax.swing.JFormattedTextField();
        presentation1NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation1HigherCostSupport = new javax.swing.JFormattedTextField();
        presentation1LowerCostSupport = new javax.swing.JFormattedTextField();
        presentation2Label1 = new javax.swing.JLabel();
        presentation2WeeklySupport = new javax.swing.JFormattedTextField();
        presentation2NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation2HigherCostSupport = new javax.swing.JFormattedTextField();
        presentation2LowerCostSupport = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        presentation3WeeklySupport = new javax.swing.JFormattedTextField();
        presentation3NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation3HigherCostSupport = new javax.swing.JFormattedTextField();
        presentation3LowerCostSupport = new javax.swing.JFormattedTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        moduleActivityNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.moduleActivityNamePanel.border.title"))); // NOI18N

        org.jdesktop.layout.GroupLayout moduleActivityNamePanelLayout = new org.jdesktop.layout.GroupLayout(moduleActivityNamePanel);
        moduleActivityNamePanel.setLayout(moduleActivityNamePanelLayout);
        moduleActivityNamePanelLayout.setHorizontalGroup(
            moduleActivityNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(moduleActivityNameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );
        moduleActivityNamePanelLayout.setVerticalGroup(
            moduleActivityNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(moduleActivityNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        supportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.supportPanel.border.title"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/Bundle"); // NOI18N
        supportPanel.setToolTipText(bundle.getString("ModuleActivityDialog.supportPanel.toolTipText")); // NOI18N
        supportPanel.setLayout(new java.awt.GridLayout(4, 5));
        supportPanel.add(blankSupportLabel);

        org.openide.awt.Mnemonics.setLocalizedText(hoursPerWeekSupportLabel, bundle.getString("ModuleActivityDialog.hoursPerWeekSupportLabel.text")); // NOI18N
        hoursPerWeekSupportLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        hoursPerWeekSupportLabel.setLineWrap(true);
        supportPanel.add(hoursPerWeekSupportLabel);

        org.openide.awt.Mnemonics.setLocalizedText(nonWeeklyHoursSupportLabel, bundle.getString("ModuleActivityDialog.nonWeeklyHoursSupportLabel.text")); // NOI18N
        nonWeeklyHoursSupportLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        nonWeeklyHoursSupportLabel.setLineWrap(true);
        supportPanel.add(nonWeeklyHoursSupportLabel);

        org.openide.awt.Mnemonics.setLocalizedText(jXLabel1, org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.jXLabel1.text")); // NOI18N
        jXLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.jXLabel1.toolTipText")); // NOI18N
        jXLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel1.setLineWrap(true);
        supportPanel.add(jXLabel1);

        org.openide.awt.Mnemonics.setLocalizedText(jXLabel2, org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.jXLabel2.text")); // NOI18N
        jXLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(ModuleActivityDialog.class, "ModuleActivityDialog.jXLabel2.toolTipText")); // NOI18N
        jXLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel2.setLineWrap(true);
        supportPanel.add(jXLabel2);

        presentation1Label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(presentation1Label1, bundle.getString("ModuleActivityDialog.presentation1Label1.text")); // NOI18N
        supportPanel.add(presentation1Label1);

        presentation1WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1WeeklySupport);

        presentation1NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1NonWeeklySupport);

        presentation1HigherCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1HigherCostSupport);

        presentation1LowerCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1LowerCostSupport);

        presentation2Label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(presentation2Label1, bundle.getString("ModuleActivityDialog.presentation2Label1.text")); // NOI18N
        supportPanel.add(presentation2Label1);

        presentation2WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2WeeklySupport);

        presentation2NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2NonWeeklySupport);

        presentation2HigherCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2HigherCostSupport);

        presentation2LowerCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2LowerCostSupport);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, bundle.getString("ModuleActivityDialog.jLabel8.text")); // NOI18N
        supportPanel.add(jLabel8);

        presentation3WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3WeeklySupport);

        presentation3NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3NonWeeklySupport);

        presentation3HigherCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3HigherCostSupport);

        presentation3LowerCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3LowerCostSupport);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 247, Short.MAX_VALUE)
                        .add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton))
                    .add(moduleActivityNamePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(supportPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelButton, okButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(moduleActivityNamePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(supportPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Module m = AELMTest.populateModule();
                ModuleLineItem li = m.getModuleItems().get(0);
                ModuleActivityDialog dialog = new ModuleActivityDialog(new javax.swing.JFrame(), true, m, li);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel blankSupportLabel;
    private javax.swing.JButton cancelButton;
    private org.jdesktop.swingx.JXLabel hoursPerWeekSupportLabel;
    private javax.swing.JLabel jLabel8;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private javax.swing.JTextField moduleActivityNameField;
    private javax.swing.JPanel moduleActivityNamePanel;
    private org.jdesktop.swingx.JXLabel nonWeeklyHoursSupportLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JFormattedTextField presentation1HigherCostSupport;
    private javax.swing.JLabel presentation1Label1;
    private javax.swing.JFormattedTextField presentation1LowerCostSupport;
    private javax.swing.JFormattedTextField presentation1NonWeeklySupport;
    private javax.swing.JFormattedTextField presentation1WeeklySupport;
    private javax.swing.JFormattedTextField presentation2HigherCostSupport;
    private javax.swing.JLabel presentation2Label1;
    private javax.swing.JFormattedTextField presentation2LowerCostSupport;
    private javax.swing.JFormattedTextField presentation2NonWeeklySupport;
    private javax.swing.JFormattedTextField presentation2WeeklySupport;
    private javax.swing.JFormattedTextField presentation3HigherCostSupport;
    private javax.swing.JFormattedTextField presentation3LowerCostSupport;
    private javax.swing.JFormattedTextField presentation3NonWeeklySupport;
    private javax.swing.JFormattedTextField presentation3WeeklySupport;
    private javax.swing.JPanel supportPanel;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
    
    private abstract class CostPropertyListener implements PropertyChangeListener {
	protected JFormattedTextField field;
	
	CostPropertyListener(JFormattedTextField field) {
	    this.field = field;
	}
    }
    
    private class HigherCostPropertyListener extends CostPropertyListener {
	
	HigherCostPropertyListener(JFormattedTextField field) {
	    super(field);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    //LOGGER.info("HCPL: change from " + evt.getSource() + "to new value: " + evt.getNewValue());
	    //LOGGER.info("HCPL: existing value of field: " + field.getValue());
	    Integer currentValue = (Integer) field.getValue();
	    Integer newValue = (Integer) evt.getNewValue();
	    //LOGGER.info("HCPL: values are equal: " + currentValue.equals(newValue));
	    if(!currentValue.equals(newValue)) {
		field.setValue((Integer) evt.getNewValue());
	    }
	}
	
    }
    
    private class LowerCostPropertyListener extends CostPropertyListener {
	
	LowerCostPropertyListener(JFormattedTextField field) {
	    super(field);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    //LOGGER.info("LCPL: change from " + evt.getSource() + "to new value: " + evt.getNewValue());
	    //LOGGER.info("HCPL: existing value of field: " + field.getValue());
	    Integer newValue = (Integer) evt.getNewValue();
	    int i = 100 - newValue;
	    Integer currentValue = (Integer) field.getValue();
	    if(!currentValue.equals(i)) {
		field.setValue(i);
	    }
	}
	
    }//Percent Formatter
    private class PercentFormatter extends JFormattedTextField.AbstractFormatter {

	@Override
	public Object stringToValue(String string) {
	    //LOGGER.info("String: " + string);
	    String s = string.replace("%", ""); //To fix issue 16
	    try {
		Integer rate = Integer.valueOf(s);
		//LOGGER.info("PercentFormatter  rate: " + rate);
                setEditValid(true);
                if (rate > 100) {
                    setEditValid(false);
                    rate = 100;
                }
                if (rate < 0) {
                    setEditValid(false);
                    rate = 0;
                }
		return rate;
	    } catch (NumberFormatException pe) {
		LOGGER.log(Level.SEVERE, s, pe);
		return 0;
	    }
	}

	@Override
	public String valueToString(Object f) {
	    //LOGGER.info("value: " + f + " class: " + f.getClass());
	    int rate = (Integer) f;
	    //LOGGER.info("rate: " + rate);
	    return String.valueOf(rate) + '%';
	}
    }
    
    //Percent verifier
    private class PercentVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            JFormattedTextField tf = (JFormattedTextField) input;
            return tf.isEditValid();
        }
        
    }
}
