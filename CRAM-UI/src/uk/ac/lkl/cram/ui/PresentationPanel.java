
package uk.ac.lkl.cram.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("serial")
public class PresentationPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(PresentationPanel.class.getName());
    private WeakHashMap<JFormattedTextField, Boolean> dirtyMap = new WeakHashMap<JFormattedTextField, Boolean>();

    
    /**
     * Creates new form PresentationPanel
     */
    public PresentationPanel() {
	super();
	initComponents();
    }
    
    
    
    public void initializeModule(Module module) {	
	
	SelectAllAdapter saa = new SelectAllAdapter();
	
	final JFormattedTextField[] studentCountFields = new JFormattedTextField[3];
	studentCountFields[0] = presentation1StudentCountField;
	studentCountFields[1] = presentation2StudentCountField;
	studentCountFields[2] = presentation3StudentCountField;
	final JFormattedTextField[] studentFeeFields = new JFormattedTextField[3];
	studentFeeFields[0] = presentation1StudentFeeField;
	studentFeeFields[1] = presentation2StudentFeeField;
	studentFeeFields[2] = presentation3StudentFeeField;
	final JFormattedTextField[] juniorCostFields = new JFormattedTextField[3];
	juniorCostFields[0] = presentation1JuniorField;
	juniorCostFields[1] = presentation2JuniorField;
	juniorCostFields[2] = presentation3JuniorField;
	final JFormattedTextField[] seniorCostFields = new JFormattedTextField[3];
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
                    makeFieldDirty(textField);
		}
	    };
	    
	    studentFeeFields[index].setValue(new Long(modulePresentation.getFee()));
	    studentFeeFields[index].addFocusListener(saa);
	    new FormattedTextFieldAdapter(studentFeeFields[index]) {
		@Override
		public void updateValue(Object value) {
		    long fee = (Long) value;
		    modulePresentation.setFee((int) fee);
                    makeFieldDirty(textField);
		}
	    };
	    
	    juniorCostFields[index].setValue(new Long(modulePresentation.getJuniorCost()));
	    juniorCostFields[index].addFocusListener(saa);
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
        
        final ModulePresentation mp1 = module.getModulePresentations().get(0);
        final ModulePresentation mp2 = module.getModulePresentations().get(1);
        mp1.addPropertyChangeListener(ModulePresentation.PROP_STUDENT_COUNT, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(studentCountFields[1])) {
                    long studentCount = mp1.getStudentCount();
                    if (mp2.getStudentCount() == 0l) {
                        studentCountFields[1].setValue(studentCount);
                        studentCountFields[2].setValue(studentCount);
                    }
                }
            }
        });
        
        mp1.addPropertyChangeListener(ModulePresentation.PROP_FEE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (!isFieldDirty(studentFeeFields[1])) {
                    long fee = mp1.getFee();
                    if (mp2.getFee() == 0l) {
                        studentFeeFields[1].setValue(fee);
                        studentFeeFields[2].setValue(fee);
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
        studentFeeTitle.setToolTipText(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.studentFeeTitle.toolTipText")); // NOI18N
        studentFeeTitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        studentFeeTitle.setLineWrap(true);
        add(studentFeeTitle);

        org.openide.awt.Mnemonics.setLocalizedText(juniorCostTitle, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.juniorCostTitle.text")); // NOI18N
        juniorCostTitle.setToolTipText(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.juniorCostTitle.toolTipText")); // NOI18N
        juniorCostTitle.setLineWrap(true);
        add(juniorCostTitle);

        org.openide.awt.Mnemonics.setLocalizedText(seniorCostTitle, org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.seniorCostTitle.text")); // NOI18N
        seniorCostTitle.setToolTipText(org.openide.util.NbBundle.getMessage(PresentationPanel.class, "PresentationPanel.seniorCostTitle.toolTipText")); // NOI18N
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
