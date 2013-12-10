package uk.ac.lkl.cram.ui;


import java.util.ResourceBundle;
import javax.swing.JButton;

/**
 * $Date$
 * $Revision$
 * @author bernard
 */
public class StartupDialog extends javax.swing.JDialog {
        private static final ResourceBundle bundle = ResourceBundle.getBundle("uk/ac/lkl/cram/ui/Bundle");


    /**
     * Creates new form StartupDialog
     * @param parent
     * @param modal  
     */
    public StartupDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        StringBuilder builder = new StringBuilder();
        builder.append("Build: ");
        builder.append(bundle.getString("build.version"));
        builder.append(" Date: ");
        builder.append(bundle.getString("build.date"));
        buildLabel.setText(builder.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iconLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        newModuleButton = new javax.swing.JButton();
        openModuleButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        buildLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Course Resource Appraisal Model");
        setResizable(false);
        setUndecorated(true);

        iconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/ac/lkl/cram/ui/CRAM.jpg"))); // NOI18N
        getContentPane().add(iconLabel, java.awt.BorderLayout.CENTER);

        newModuleButton.setText("New Module...");
        jPanel1.add(newModuleButton);

        openModuleButton.setText("Open Module...");
        jPanel1.add(openModuleButton);

        quitButton.setText("Quit...");
        jPanel1.add(quitButton);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        buildLabel.setFont(new java.awt.Font("Lucida Grande", 0, 8)); // NOI18N
        buildLabel.setText("Build details");
        getContentPane().add(buildLabel, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(StartupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StartupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StartupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StartupDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartupDialog dialog = new StartupDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel buildLabel;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton newModuleButton;
    private javax.swing.JButton openModuleButton;
    private javax.swing.JButton quitButton;
    // End of variables declaration//GEN-END:variables

    JButton getNewModuleButton() {
        return newModuleButton;
    }

    JButton getOpenModuleButtion() {
        return openModuleButton;
    }
    
    JButton getQuitButton() {
        return quitButton;
    }
}
