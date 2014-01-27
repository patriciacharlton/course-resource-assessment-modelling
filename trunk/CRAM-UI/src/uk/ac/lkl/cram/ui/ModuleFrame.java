package uk.ac.lkl.cram.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jfree.chart.ChartPanel;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.ui.wizard.TLACreatorWizardIterator;

/**
 * $Date$
 * $Revision$
 * @author bernard
 */
@SuppressWarnings("serial")
public class ModuleFrame extends javax.swing.JFrame {
    private static final Logger LOGGER = Logger.getLogger(ModuleFrame.class.getName());
    private final Module module;
    private LineItem selectedLineItem;

    /**
     * Creates new form ModuleFrame
     * @param module 
     */
    public ModuleFrame(Module module) {
        this.module = module;
        initComponents();
	
//	newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//	openMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//	saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//	quitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

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
        saveAsMI = new javax.swing.JMenuItem();
        quitMI = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        moduleMenu = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        modifyLineItemMI = new javax.swing.JMenuItem();
        addLineItemMI = new javax.swing.JMenuItem();
        removeLineItemMI = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(730, 600));
        setPreferredSize(new java.awt.Dimension(860, 930));
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
        gridBagConstraints.ipadx = 490;
        gridBagConstraints.ipady = 600;
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
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.ipady = 600;
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
        saveMI.setEnabled(false);
        fileMenu.add(saveMI);

        org.openide.awt.Mnemonics.setLocalizedText(saveAsMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.saveAsMI.text")); // NOI18N
        fileMenu.add(saveAsMI);

        org.openide.awt.Mnemonics.setLocalizedText(quitMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.quitMI.text")); // NOI18N
        fileMenu.add(quitMI);

        windowMenuBar.add(fileMenu);

        org.openide.awt.Mnemonics.setLocalizedText(editMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.editMenu.text")); // NOI18N
        windowMenuBar.add(editMenu);

        org.openide.awt.Mnemonics.setLocalizedText(moduleMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.moduleMenu.text")); // NOI18N
        moduleMenu.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(modifyLineItemMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.modifyLineItemMI.text")); // NOI18N
        modifyLineItemMI.setEnabled(false);
        modifyLineItemMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyLineItemMIActionPerformed(evt);
            }
        });
        moduleMenu.add(modifyLineItemMI);

        org.openide.awt.Mnemonics.setLocalizedText(addLineItemMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.addLineItemMI.text")); // NOI18N
        addLineItemMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLineItemMIActionPerformed(evt);
            }
        });
        moduleMenu.add(addLineItemMI);

        org.openide.awt.Mnemonics.setLocalizedText(removeLineItemMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.removeLineItemMI.text")); // NOI18N
        removeLineItemMI.setEnabled(false);
        removeLineItemMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLineItemMIActionPerformed(evt);
            }
        });
        moduleMenu.add(removeLineItemMI);

        windowMenuBar.add(moduleMenu);

        org.openide.awt.Mnemonics.setLocalizedText(windowMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.windowMenu.text")); // NOI18N
        windowMenuBar.add(windowMenu);

        setJMenuBar(windowMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modifyLineItemMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyLineItemMIActionPerformed
        modifySelectedLineItem();
    }//GEN-LAST:event_modifyLineItemMIActionPerformed

    private void removeLineItemMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLineItemMIActionPerformed
	removeSelectedLineItem();
    }//GEN-LAST:event_removeLineItemMIActionPerformed

    private void addLineItemMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLineItemMIActionPerformed
        addLineItem();
    }//GEN-LAST:event_addLineItemMIActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
            public void run() {
                new ModuleFrame(AELMTest.populateModule()).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addLineItemMI;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private org.jdesktop.swingx.JXTaskPaneContainer leftTaskPaneContainer;
    private javax.swing.JMenuItem modifyLineItemMI;
    private javax.swing.JMenu moduleMenu;
    private javax.swing.JMenuItem newMI;
    private javax.swing.JMenuItem openMI;
    private javax.swing.JMenuItem quitMI;
    private javax.swing.JMenuItem removeLineItemMI;
    private org.jdesktop.swingx.JXTaskPaneContainer rightTaskPaneContainer;
    private javax.swing.JMenuItem saveAsMI;
    private javax.swing.JMenuItem saveMI;
    private javax.swing.JMenu windowMenu;
    private javax.swing.JMenuBar windowMenuBar;
    // End of variables declaration//GEN-END:variables


    private JXTaskPane createCourseDataPane() {
	JXTaskPane courseDataPane = new JXTaskPane();
	courseDataPane.setTitle("Module Data");
        courseDataPane.setScrollOnExpand(true);
	courseDataPane.add(new ModulePanel(module));
	return courseDataPane;
    }

    private JXTaskPane createLineItemPane() {
	JXTaskPane lineItemPane = new JXTaskPane();
	lineItemPane.setTitle("Student Hours");
        lineItemPane.setScrollOnExpand(true);
	final LineItemPanel lineItemPanel = new LineItemPanel(module);
	lineItemPanel.addPropertyChangeListener(LineItemPanel.PROP_SELECTED_LINEITEM, new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		selectedLineItem = (LineItem) pce.getNewValue();
		//LOGGER.info("selected line item: " + selectedLineItem);
		modifyLineItemMI.setEnabled(selectedLineItem != null);
		removeLineItemMI.setEnabled(selectedLineItem != null);
	    }
	});

	JTable table = lineItemPanel.getTable();
	table.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
		    modifySelectedLineItem();
		}
	    }
	});

	lineItemPane.add(lineItemPanel);
	return lineItemPane;
    }
    
    private JXTaskPane createTutorHoursPane() {
	JXTaskPane tutorHoursPane = new JXTaskPane();
        tutorHoursPane.setScrollOnExpand(true);
	tutorHoursPane.setTitle("Tutor Hours");
	tutorHoursPane.add(new TutorHoursPanel(module));
	tutorHoursPane.setCollapsed(true);
	return tutorHoursPane;
    }
    
    private JXTaskPane createTutorCostPane() {
	JXTaskPane tutorCostPane = new JXTaskPane();
        tutorCostPane.setScrollOnExpand(true);
	tutorCostPane.setTitle("Tutor Costs");
	tutorCostPane.add(new TutorCostPanel(module));
	tutorCostPane.setCollapsed(true);
	return tutorCostPane;
    }

    private JXTaskPane createLearningTypeChartPane() {
	JXTaskPane typeChartPane = new JXTaskPane();
	typeChartPane.setTitle("Learning Types");
        typeChartPane.setScrollOnExpand(true);
	ChartPanel chartPanel = LearningTypeChartFactory.createChartPanel(module);
	chartPanel.setPreferredSize(new Dimension(150, 200));
	typeChartPane.add(chartPanel);
	return typeChartPane;
    }

    private JXTaskPane createLearningExperienceChartPane() {
	JXTaskPane experienceChartPane = new JXTaskPane();
        experienceChartPane.setScrollOnExpand(true);
	experienceChartPane.setTitle("Learning Experiences");
	ChartPanel chartPanel = LearningExperienceChartFactory.createChartPanel(module);
	chartPanel.setPreferredSize(new Dimension(125,75));
	chartPanel.setMinimumDrawHeight(75);
	experienceChartPane.add(chartPanel);
	return experienceChartPane;
    }

    private JXTaskPane createHoursChartPane() {
	JXTaskPane hoursChartPane = new JXTaskPane();
	hoursChartPane.setTitle("Hours");
        hoursChartPane.setScrollOnExpand(true);
	ChartPanel chartPanel = HoursChartFactory.createChartPanel(module);
	chartPanel.setPreferredSize(new Dimension(200, 200));
	hoursChartPane.add(chartPanel);
	return hoursChartPane;
    }

    private JXTaskPane createTotalCostsPane() {
	JXTaskPane costPane = new JXTaskPane();
        costPane.setScrollOnExpand(true);
	costPane.setTitle("Summary");
	costPane.add(new CostPanel(module));
	costPane.setCollapsed(true);
	return costPane;
    }
    
    private void modifySelectedLineItem() {
	if (selectedLineItem instanceof TLALineItem) {
	    TLAOkCancelDialog dialog = new TLAOkCancelDialog(this, true, module, (TLALineItem) selectedLineItem);
	    dialog.setTitle("Modify TLA");
	    dialog.setSelectedIndex(2);
	    dialog.setVisible(true);
	    //LOGGER.info("Dialog returnStatus: " + dialog.getReturnStatus());
	    //TODO--undo
	} else {
	    LOGGER.warning("Unable to edit this line item");
	}
    }
    
    private void removeSelectedLineItem() {
        //TODO -- undo
	if (selectedLineItem instanceof TLALineItem) {
	    int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete \'" + selectedLineItem.getName() + "\'?", "Delete TLA", JOptionPane.YES_NO_OPTION);
	    if (reply == JOptionPane.YES_OPTION) {
		module.removeTLALineItem((TLALineItem) selectedLineItem);
	    } else {
		LOGGER.info("Delete cancelled");
	    }
	} else {
	    LOGGER.warning("Unable to remove this line item");
	}
    }
    
    private void addLineItem() {
        //TODO -- undo
        TLACreatorWizardIterator iterator = new TLACreatorWizardIterator(module);
	WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
	iterator.initialize(wizardDescriptor);
	// {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
	// {1} will be replaced by WizardDescriptor.Iterator.name()
	wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
	wizardDescriptor.setTitle("TLA Creator Wizard");
	Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
	//LOGGER.info("Cancelled: " + cancelled);
        if (!cancelled) {
            module.addTLALineItem(iterator.getLineItem());
        }
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

    JMenuItem getQuitMenuItem() {
	return quitMI;
    }
    
    JMenuItem getSaveAsMenuItem() {
        return saveAsMI;
    }
    
    Module getModule() {
        return module;
    }
}
