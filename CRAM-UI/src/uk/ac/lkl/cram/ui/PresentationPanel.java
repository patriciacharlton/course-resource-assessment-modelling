
package uk.ac.lkl.cram.ui;

import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 * $Date$
 * @author Bernard Horan
 */
public class PresentationPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(PresentationPanel.class.getName());
    
    /**
     * Creates new form PresentationPanel
     */
    public PresentationPanel() {
	super();
	initComponents();
    }
    
    
    
    public void initializeModule(Module module) {	
	
	SelectAllAdapter saa = new SelectAllAdapter();
	
	JFormattedTextField[] studentCountFields = new JFormattedTextField[3];
	studentCountFields[0] = presentation1StudentCountField;
	studentCountFields[1] = presentation2StudentCountField;
	studentCountFields[2] = presentation3StudentCountField;
	JFormattedTextField[] studentFeeFields = new JFormattedTextField[3];
	studentFeeFields[0] = presentation1StudentFeeField;
	studentFeeFields[1] = presentation2StudentFeeField;
	studentFeeFields[2] = presentation3StudentFeeField;
	JFormattedTextField[] juniorCostFields = new JFormattedTextField[3];
	juniorCostFields[0] = presentation1JuniorField;
	juniorCostFields[1] = presentation2JuniorField;
	juniorCostFields[2] = presentation3JuniorField;
	JFormattedTextField[] seniorCostFields = new JFormattedTextField[3];
	seniorCostFields[0] = presentation1SeniorField;
	seniorCostFields[1] = presentation2SeniorField;
	seniorCostFields[2] = presentation3SeniorField;
	int index = 0;
	for (final ModulePresentation modulePresentation : module.getModulePresentations()) {
	    //LOGGER.info("presentation: " + modulePresentation);
	    studentCountFields[index].setValue(new Long(modulePresentation.getStudentCount()));
	    studentCountFields[index].addFocusListener(saa);
	    new FormattedTextFieldAdapter(studentCountFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long studentCount = (Long) value;
		    modulePresentation.setStudentCount((int) studentCount);
		}
	    };
	    
	    studentFeeFields[index].setValue(new Long(modulePresentation.getFee()));
	    studentFeeFields[index].addFocusListener(saa);
	    new FormattedTextFieldAdapter(studentFeeFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long fee = (Long) value;
		    modulePresentation.setFee((int) fee);
		}
	    };
	    
	    juniorCostFields[index].setValue(new Long(modulePresentation.getJuniorCost()));
	    juniorCostFields[index].addFocusListener(saa);
	    new FormattedTextFieldAdapter(juniorCostFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long cost = (Long) value;
		    modulePresentation.setJuniorCost((int) cost);
		}
	    };
	    
	    seniorCostFields[index].setValue(new Long(modulePresentation.getSeniorCost()));
	    seniorCostFields[index].addFocusListener(saa);
	    new FormattedTextFieldAdapter(seniorCostFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long cost = (Long) value;
		    modulePresentation.setSeniorCost((int) cost);
		}
	    };
	    
	    index++;
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

        presentationsTitle = new org.jdesktop.swingx.JXLabel();
        studentCountTitle = new org.jdesktop.swingx.JXLabel();
        studentFeeTitle = new org.jdesktop.swingx.JXLabel();
        juniorCostTitle = new org.jdesktop.swingx.JXLabel();
        seniorCostTitle = new org.jdesktop.swingx.JXLabel();
        presentation1Label = new javax.swing.JLabel();
        presentation1StudentCountField = new javax.swing.JFormattedTextField();
        presentation1StudentFeeField = new javax.swing.JFormattedTextField();
        presentation1JuniorField = new javax.swing.JFormattedTextField();
        presentation1SeniorField = new javax.swing.JFormattedTextField();
        presentation2Label = new javax.swing.JLabel();
        presentation2StudentCountField = new javax.swing.JFormattedTextField();
        presentation2StudentFeeField = new javax.swing.JFormattedTextField();
        presentation2JuniorField = new javax.swing.JFormattedTextField();
        presentation2SeniorField = new javax.swing.JFormattedTextField();
        presentation3Label = new javax.swing.JLabel();
        presentation3StudentCountField = new javax.swing.JFormattedTextField();
        presentation3StudentFeeField = new javax.swing.JFormattedTextField();
        presentation3JuniorField = new javax.swing.JFormattedTextField();
        presentation3SeniorField = new javax.swing.JFormattedTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.border.title"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(428, 156));
        setLayout(new java.awt.GridLayout(4, 5));
        add(presentationsTitle);

        org.openide.awt.Mnemonics.setLocalizedText(studentCountTitle, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.studentCountTitle.text")); // NOI18N
        studentCountTitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        studentCountTitle.setLineWrap(true);
        add(studentCountTitle);

        org.openide.awt.Mnemonics.setLocalizedText(studentFeeTitle, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.studentFeeTitle.text")); // NOI18N
        studentFeeTitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        studentFeeTitle.setLineWrap(true);
        add(studentFeeTitle);

        org.openide.awt.Mnemonics.setLocalizedText(juniorCostTitle, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.juniorCostTitle.text")); // NOI18N
        juniorCostTitle.setLineWrap(true);
        add(juniorCostTitle);

        org.openide.awt.Mnemonics.setLocalizedText(seniorCostTitle, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.seniorCostTitle.text")); // NOI18N
        seniorCostTitle.setLineWrap(true);
        add(seniorCostTitle);

        presentation1Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(presentation1Label, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.presentation1Label.text")); // NOI18N
        add(presentation1Label);

        presentation1StudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1StudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation1StudentCountField);

        presentation1StudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1StudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation1StudentFeeField);

        presentation1JuniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1JuniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation1JuniorField);

        presentation1SeniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1SeniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation1SeniorField);

        presentation2Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(presentation2Label, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.presentation2Label.text")); // NOI18N
        add(presentation2Label);

        presentation2StudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2StudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation2StudentCountField);

        presentation2StudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2StudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation2StudentFeeField);

        presentation2JuniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2JuniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation2JuniorField);

        presentation2SeniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2SeniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation2SeniorField);

        presentation3Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(presentation3Label, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.presentation3Label.text")); // NOI18N
        add(presentation3Label);

        presentation3StudentCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3StudentCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation3StudentCountField);

        presentation3StudentFeeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3StudentFeeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation3StudentFeeField);

        presentation3JuniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3JuniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation3JuniorField);

        presentation3SeniorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3SeniorField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(presentation3SeniorField);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXLabel juniorCostTitle;
    private javax.swing.JFormattedTextField presentation1JuniorField;
    private javax.swing.JLabel presentation1Label;
    private javax.swing.JFormattedTextField presentation1SeniorField;
    private javax.swing.JFormattedTextField presentation1StudentCountField;
    private javax.swing.JFormattedTextField presentation1StudentFeeField;
    private javax.swing.JFormattedTextField presentation2JuniorField;
    private javax.swing.JLabel presentation2Label;
    private javax.swing.JFormattedTextField presentation2SeniorField;
    private javax.swing.JFormattedTextField presentation2StudentCountField;
    private javax.swing.JFormattedTextField presentation2StudentFeeField;
    private javax.swing.JFormattedTextField presentation3JuniorField;
    private javax.swing.JLabel presentation3Label;
    private javax.swing.JFormattedTextField presentation3SeniorField;
    private javax.swing.JFormattedTextField presentation3StudentCountField;
    private javax.swing.JFormattedTextField presentation3StudentFeeField;
    private org.jdesktop.swingx.JXLabel presentationsTitle;
    private org.jdesktop.swingx.JXLabel seniorCostTitle;
    private org.jdesktop.swingx.JXLabel studentCountTitle;
    private org.jdesktop.swingx.JXLabel studentFeeTitle;
    // End of variables declaration//GEN-END:variables
}