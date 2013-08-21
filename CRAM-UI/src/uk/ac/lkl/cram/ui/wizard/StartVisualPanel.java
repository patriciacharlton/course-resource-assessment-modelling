package uk.ac.lkl.cram.ui.wizard;

import javax.swing.JPanel;

public final class StartVisualPanel extends JPanel {

    /**
     * Creates new form StartVisualPanel
     */
    public StartVisualPanel() {
	initComponents();
	predefinedRadioButton.setSelected(true);
    }

    @Override
    public String getName() {
	return "Create Teaching & Learning Activity";
    }
    
    public boolean isVanilla() {
	return vanillaRadioButton.isSelected();
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        predefinedRadioButton = new javax.swing.JRadioButton();
        vanillaRadioButton = new javax.swing.JRadioButton();

        buttonGroup1.add(predefinedRadioButton);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(predefinedRadioButton, bundle.getString("USE PRE-DEFINED")); // NOI18N

        buttonGroup1.add(vanillaRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(vanillaRadioButton, bundle.getString("START FROM SCRATCH")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(predefinedRadioButton)
                    .addComponent(vanillaRadioButton)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(predefinedRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vanillaRadioButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton predefinedRadioButton;
    private javax.swing.JRadioButton vanillaRadioButton;
    // End of variables declaration//GEN-END:variables
}
