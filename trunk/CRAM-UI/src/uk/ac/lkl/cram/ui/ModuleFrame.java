package uk.ac.lkl.cram.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXTaskPane;
import org.jfree.chart.ChartPanel;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;

/**
 * $Date$
 * @author bernard
 */
public class ModuleFrame extends javax.swing.JFrame {
    private static final Logger LOGGER = Logger.getLogger(ModuleFrame.class.getName());
    private final Module module;

    /**
     * Creates new form ModuleFrame
     * @param module 
     */
    public ModuleFrame(Module module) {
        this.module = module;
        initComponents();
	
	newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	openMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        setTitle("Module Name: " + module.getModuleName());
	leftTaskPaneContainer.add(createCourseDataPane());
	leftTaskPaneContainer.add(createLineItemPane());
	leftTaskPaneContainer.add(createTutorHoursPane());
	leftTaskPaneContainer.add(createTutorCostPane());
	rightTaskPaneContainer.add(createLearningTypeChartPane());
	rightTaskPaneContainer.add(createLearningExperienceChartPane());
	rightTaskPaneContainer.add(createHoursChartPane());
	rightTaskPaneContainer.add(createTotalCostsPane());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        leftTaskPaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        jScrollPane3 = new javax.swing.JScrollPane();
        rightTaskPaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        windowMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMI = new javax.swing.JMenuItem();
        openMI = new javax.swing.JMenuItem();
        saveMI = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        moduleMenu = new javax.swing.JMenu();
        editCourseDataMI = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(830, 880));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        leftTaskPaneContainer.setPaintBorderInsets(false);
        org.jdesktop.swingx.VerticalLayout verticalLayout1 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout1.setGap(14);
        leftTaskPaneContainer.setLayout(verticalLayout1);
        jScrollPane1.setViewportView(leftTaskPaneContainer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 277;
        gridBagConstraints.ipady = 421;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        org.jdesktop.swingx.VerticalLayout verticalLayout2 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout2.setGap(14);
        rightTaskPaneContainer.setLayout(verticalLayout2);
        jScrollPane3.setViewportView(rightTaskPaneContainer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 266;
        gridBagConstraints.ipady = 421;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane3, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(fileMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.fileMenu.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(newMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.newMI.text")); // NOI18N
        fileMenu.add(newMI);

        org.openide.awt.Mnemonics.setLocalizedText(openMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.openMI.text")); // NOI18N
        fileMenu.add(openMI);

        org.openide.awt.Mnemonics.setLocalizedText(saveMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.saveMI.text")); // NOI18N
        fileMenu.add(saveMI);

        windowMenuBar.add(fileMenu);

        org.openide.awt.Mnemonics.setLocalizedText(editMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.editMenu.text")); // NOI18N
        windowMenuBar.add(editMenu);

        org.openide.awt.Mnemonics.setLocalizedText(moduleMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.moduleMenu.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(editCourseDataMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.editCourseDataMI.text")); // NOI18N
        editCourseDataMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCourseDataMIActionPerformed(evt);
            }
        });
        moduleMenu.add(editCourseDataMI);

        windowMenuBar.add(moduleMenu);

        org.openide.awt.Mnemonics.setLocalizedText(windowMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.windowMenu.text")); // NOI18N
        windowMenuBar.add(windowMenu);

        setJMenuBar(windowMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editCourseDataMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCourseDataMIActionPerformed
	ModuleOkCancelDialog dialog = new ModuleOkCancelDialog(new javax.swing.JFrame(), true, module);
	dialog.setVisible(true);
    }//GEN-LAST:event_editCourseDataMIActionPerformed

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
            java.util.logging.Logger.getLogger(ModuleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModuleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModuleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModuleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
            public void run() {
                new ModuleFrame(AELMTest.populateModule()).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem editCourseDataMI;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private org.jdesktop.swingx.JXTaskPaneContainer leftTaskPaneContainer;
    private javax.swing.JMenu moduleMenu;
    private javax.swing.JMenuItem newMI;
    private javax.swing.JMenuItem openMI;
    private org.jdesktop.swingx.JXTaskPaneContainer rightTaskPaneContainer;
    private javax.swing.JMenuItem saveMI;
    private javax.swing.JMenu windowMenu;
    private javax.swing.JMenuBar windowMenuBar;
    // End of variables declaration//GEN-END:variables


    private JXTaskPane createCourseDataPane() {
	JXTaskPane courseDataPane = new JXTaskPane();
	courseDataPane.setTitle("Course Data");
	courseDataPane.add(new ModulePanel(module));
	return courseDataPane;
    }

    private JXTaskPane createLineItemPane() {
	JXTaskPane lineItemPane = new JXTaskPane();
	lineItemPane.setTitle("Student Hours");
	lineItemPane.add(new LineItemPanel(module));
	return lineItemPane;
    }
    
    private JXTaskPane createTutorHoursPane() {
	JXTaskPane tutorHoursPane = new JXTaskPane();
	tutorHoursPane.setTitle("Tutor Hours");
	tutorHoursPane.add(new TutorHoursPanel(module));
	tutorHoursPane.setCollapsed(true);
	return tutorHoursPane;
    }
    
    private JXTaskPane createTutorCostPane() {
	JXTaskPane tutorCostPane = new JXTaskPane();
	tutorCostPane.setTitle("Tutor Cost");
	tutorCostPane.add(new TutorCostPanel(module));
	tutorCostPane.setCollapsed(true);
	return tutorCostPane;
    }

    private JXTaskPane createLearningTypeChartPane() {
	JXTaskPane typeChartPane = new JXTaskPane();
	typeChartPane.setTitle("Learning Types");
	ChartPanel chartPanel = LearningTypeChartFactory.createChartPanel(module);
	chartPanel.setPreferredSize(new Dimension(150, 200));
	typeChartPane.add(chartPanel);
	return typeChartPane;
    }

    private JXTaskPane createLearningExperienceChartPane() {
	JXTaskPane experienceChartPane = new JXTaskPane();
	experienceChartPane.setTitle("Learning Experiences");
	ChartPanel chartPanel = LearningExperienceChartFactory.createChartPanel(module);
	chartPanel.setPreferredSize(new Dimension(125,200));
	experienceChartPane.add(chartPanel);
	return experienceChartPane;
    }

    private JXTaskPane createHoursChartPane() {
	JXTaskPane hoursChartPane = new JXTaskPane();
	hoursChartPane.setTitle("Hours");
	ChartPanel chartPanel = HoursChartFactory.createChartPanel(module);
	chartPanel.setPreferredSize(new Dimension(200, 200));
	hoursChartPane.add(chartPanel);
	return hoursChartPane;
    }

    private JXTaskPane createTotalCostsPane() {
	JXTaskPane costPane = new JXTaskPane();
	costPane.setTitle("Profit & Loss");
	costPane.add(new CostPanel(module));
	costPane.setCollapsed(true);
	return costPane;
    }

    JMenu getWindowMenu() {
        return windowMenu;
    }

    JMenuItem getNewMenuItem() {
        return newMI;
    }

    JMenuItem getOpenMenuItem() {
        return openMI;
    }
}
