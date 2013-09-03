package uk.ac.lkl.cram.ui;

import java.util.logging.Logger;
import javax.swing.JFrame;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;

/**
 * $Date$
 * @author Bernard Horan
 */
public class ModuleWizardPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(ModuleWizardPanel.class.getName());


    /**
     * Creates new form ModuleWizardPanel
     * @param module
     */
    public ModuleWizardPanel(final Module module) {
	initComponents();

	SelectAllAdapter saa = new SelectAllAdapter();
	
	moduleNameField.setText(module.getModuleName());
	moduleNameField.addFocusListener(saa);
	new TextFieldAdapter(moduleNameField) {

	    @Override
	    public void updateText(String value) {		
		module.setModuleName(value);
	    }
	};	
	
	hourCountField.setValue(new Long(module.getTotalCreditHourCount()));
	hourCountField.addFocusListener(saa);
	new FormattedTextFieldAdapter(hourCountField) {

	    @Override
	    public void updateValue(Object value) {
		long hourCount = (Long) value;
		module.setTotalCreditHourCount((int) hourCount);
	    }
	};
		
	weekCountField.setValue(new Long(module.getWeekCount()));
	weekCountField.addFocusListener(saa);
	new FormattedTextFieldAdapter(weekCountField) {

	    @Override
	    public void updateValue(Object value) {
		long weekCount = (Long) value;
		module.setWeekCount((int) weekCount);
	    }
	};
	
	tutorGroupSizeField.setValue(new Long(module.getTutorGroupSize()));
	tutorGroupSizeField.addFocusListener(saa);
	new FormattedTextFieldAdapter(tutorGroupSizeField) {

	    @Override
	    public void updateValue(Object value) {
		long tutorGroupSize = (Long) value;
		module.setTutorGroupSize((int) tutorGroupSize);
	    }
	};
		
	presentationPanel.initializeModule(module);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        moduleDetailsPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tutorGroupSizeField = new javax.swing.JFormattedTextField();
        hourCountField = new javax.swing.JFormattedTextField();
        weekCountField = new javax.swing.JFormattedTextField();
        moduleNamePanel = new javax.swing.JPanel();
        moduleNameField = new javax.swing.JTextField();
        presentationPanel = new uk.ac.lkl.cram.ui.PresentationPanel();

        moduleDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Details..."));

        jLabel3.setText("Number of Hours:");

        jLabel4.setText("Number of Weeks:");

        jLabel2.setText("Tutor Group Size:");

        tutorGroupSizeField.setColumns(4);
        tutorGroupSizeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        tutorGroupSizeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        hourCountField.setColumns(4);
        hourCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        hourCountField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        weekCountField.setColumns(4);
        weekCountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        weekCountField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        org.jdesktop.layout.GroupLayout moduleDetailsPanelLayout = new org.jdesktop.layout.GroupLayout(moduleDetailsPanel);
        moduleDetailsPanel.setLayout(moduleDetailsPanelLayout);
        moduleDetailsPanelLayout.setHorizontalGroup(
            moduleDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(moduleDetailsPanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(moduleDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(moduleDetailsPanelLayout.createSequentialGroup()
                        .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(hourCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(moduleDetailsPanelLayout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(weekCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(moduleDetailsPanelLayout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(tutorGroupSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        moduleDetailsPanelLayout.linkSize(new java.awt.Component[] {hourCountField, tutorGroupSizeField, weekCountField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        moduleDetailsPanelLayout.setVerticalGroup(
            moduleDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(moduleDetailsPanelLayout.createSequentialGroup()
                .add(moduleDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tutorGroupSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .add(moduleDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(hourCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(moduleDetailsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(weekCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        moduleNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Module Name"));

        org.jdesktop.layout.GroupLayout moduleNamePanelLayout = new org.jdesktop.layout.GroupLayout(moduleNamePanel);
        moduleNamePanel.setLayout(moduleNamePanelLayout);
        moduleNamePanelLayout.setHorizontalGroup(
            moduleNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(moduleNamePanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(moduleNameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .add(0, 0, 0))
        );
        moduleNamePanelLayout.setVerticalGroup(
            moduleNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(moduleNamePanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(moduleNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(moduleNamePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(0, 0, 0)
                .add(moduleDetailsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, presentationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(moduleDetailsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(moduleNamePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(presentationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField hourCountField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel moduleDetailsPanel;
    private javax.swing.JTextField moduleNameField;
    private javax.swing.JPanel moduleNamePanel;
    private uk.ac.lkl.cram.ui.PresentationPanel presentationPanel;
    private javax.swing.JFormattedTextField tutorGroupSizeField;
    private javax.swing.JFormattedTextField weekCountField;
    // End of variables declaration//GEN-END:variables
    
    
    public static void main(String args[]) {
        final JFrame frame = new JFrame("Module Wizard Panel");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ModuleWizardPanel(AELMTest.populateModule()));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }
}
