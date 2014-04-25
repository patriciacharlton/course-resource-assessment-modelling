/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.lkl.cram.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.undo.UndoManager;
import org.jdesktop.swingx.JXTaskPane;
import org.jfree.chart.ChartPanel;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModuleLineItem;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.UserTLALibrary;
import uk.ac.lkl.cram.ui.wizard.TLACreatorWizardIterator;

/**
 * This class represents the window that displays the contents of a CRAM module.
 * It is made up of several internal panes, each of which display some view
 * of the CRAM module. It also holds the menubar and the menu items. The CRAMApplication
 * handles much of the menu item behaviour.
 * @see CRAMApplication
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class ModuleFrame extends javax.swing.JFrame {
    private static final Logger LOGGER = Logger.getLogger(ModuleFrame.class.getName());
    //The module rendered by this frame
    private final Module module;
    //Keeps track of the selection in the various tables in the frame
    private final LineItemSelectionModel sharedSelectionModel = new LineItemSelectionModel();
    //Listens for double clicks on the various tables in the frame
    private final MouseListener doubleClickListener;
    

    /**
     * Creates new form ModuleFrame
     * @param module the module that this window displays
     */
    public ModuleFrame(Module module) {
        this.module = module;
        initComponents();
	//Listen for changes to the shared selection model
        sharedSelectionModel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
		//Update the menu items
                modifyLineItemMI.setEnabled(evt.getNewValue() != null);
                removeLineItemMI.setEnabled(evt.getNewValue() != null);
            }
        });
        
        doubleClickListener = new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		//If the user double clicks, then treat this as a shortcut to modify the selected line item
		if (e.getClickCount() == 2) {
		    modifySelectedLineItem();
		}
	    }
	};
        
	//TODO
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
	rightTaskPaneContainer.add(createLearnerFeedbackChartPane());
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

        jMenuItem1 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        leftTaskPaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        jScrollPane3 = new javax.swing.JScrollPane();
        rightTaskPaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        windowMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMI = new javax.swing.JMenuItem();
        openMI = new javax.swing.JMenuItem();
        duplicateMI = new javax.swing.JMenuItem();
        saveMI = new javax.swing.JMenuItem();
        saveAsMI = new javax.swing.JMenuItem();
        quitMI = new javax.swing.JMenuItem();
        moduleMenu = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        addTLALineItemMI = new javax.swing.JMenuItem();
        addModuleLineItemMI = new javax.swing.JMenuItem();
        modifyLineItemMI = new javax.swing.JMenuItem();
        removeLineItemMI = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        openHelpMI = new javax.swing.JMenuItem();

        org.openide.awt.Mnemonics.setLocalizedText(jMenuItem1, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.jMenuItem1.text")); // NOI18N

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

        org.openide.awt.Mnemonics.setLocalizedText(duplicateMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.duplicateMI.text")); // NOI18N
        fileMenu.add(duplicateMI);

        org.openide.awt.Mnemonics.setLocalizedText(saveMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.saveMI.text")); // NOI18N
        saveMI.setEnabled(false);
        fileMenu.add(saveMI);

        org.openide.awt.Mnemonics.setLocalizedText(saveAsMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.saveAsMI.text")); // NOI18N
        fileMenu.add(saveAsMI);

        org.openide.awt.Mnemonics.setLocalizedText(quitMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.quitMI.text")); // NOI18N
        fileMenu.add(quitMI);

        windowMenuBar.add(fileMenu);

        org.openide.awt.Mnemonics.setLocalizedText(moduleMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.moduleMenu.text")); // NOI18N
        moduleMenu.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(addTLALineItemMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.addTLALineItemMI.text")); // NOI18N
        addTLALineItemMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTLALineItemMIActionPerformed(evt);
            }
        });
        moduleMenu.add(addTLALineItemMI);

        org.openide.awt.Mnemonics.setLocalizedText(addModuleLineItemMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.addModuleLineItemMI.text")); // NOI18N
        addModuleLineItemMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addModuleLineItemMIActionPerformed(evt);
            }
        });
        moduleMenu.add(addModuleLineItemMI);

        org.openide.awt.Mnemonics.setLocalizedText(modifyLineItemMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.modifyLineItemMI.text")); // NOI18N
        modifyLineItemMI.setEnabled(false);
        modifyLineItemMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyLineItemMIActionPerformed(evt);
            }
        });
        moduleMenu.add(modifyLineItemMI);

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

        org.openide.awt.Mnemonics.setLocalizedText(helpMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.helpMenu.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(openHelpMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.openHelpMI.text")); // NOI18N
        helpMenu.add(openHelpMI);

        windowMenuBar.add(helpMenu);

        setJMenuBar(windowMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modifyLineItemMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyLineItemMIActionPerformed
        modifySelectedLineItem();
    }//GEN-LAST:event_modifyLineItemMIActionPerformed

    private void removeLineItemMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLineItemMIActionPerformed
	removeSelectedLineItem();
    }//GEN-LAST:event_removeLineItemMIActionPerformed

    private void addTLALineItemMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTLALineItemMIActionPerformed
        addTLALineItem();
    }//GEN-LAST:event_addTLALineItemMIActionPerformed

    private void addModuleLineItemMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addModuleLineItemMIActionPerformed
        addModuleLineItem();
    }//GEN-LAST:event_addModuleLineItemMIActionPerformed

    /**
     * Used for testing purposes only.
     * @param args the command line arguments (ignored)
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
    private javax.swing.JMenuItem addModuleLineItemMI;
    private javax.swing.JMenuItem addTLALineItemMI;
    private javax.swing.JMenuItem duplicateMI;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private org.jdesktop.swingx.JXTaskPaneContainer leftTaskPaneContainer;
    private javax.swing.JMenuItem modifyLineItemMI;
    private javax.swing.JMenu moduleMenu;
    private javax.swing.JMenuItem newMI;
    private javax.swing.JMenuItem openHelpMI;
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
	LineItemPanel lineItemPanel = new LineItemPanel(module, sharedSelectionModel);
	JTable table = lineItemPanel.getTable();
	table.addMouseListener(doubleClickListener);
	lineItemPane.add(lineItemPanel);
	return lineItemPane;
    }
    
    private JXTaskPane createTutorHoursPane() {
	JXTaskPane tutorHoursPane = new JXTaskPane();
        tutorHoursPane.setScrollOnExpand(true);
        tutorHoursPane.setCollapsed(true);
        tutorHoursPane.setTitle("Tutor Hours");
        TutorHoursPanel tutorHoursPanel = new TutorHoursPanel(module, sharedSelectionModel);
        JTable table = tutorHoursPanel.getTable();
        table.addMouseListener(doubleClickListener);
	tutorHoursPane.add(tutorHoursPanel);
	return tutorHoursPane;
    }
    
    private JXTaskPane createTutorCostPane() {
	JXTaskPane tutorCostPane = new JXTaskPane();
        tutorCostPane.setScrollOnExpand(true);
	tutorCostPane.setCollapsed(true);
	tutorCostPane.setTitle("Tutor Costs");
        TutorCostPanel tutorCostPanel = new TutorCostPanel(module,sharedSelectionModel);
        JTable table = tutorCostPanel.getTable();
        table.addMouseListener(doubleClickListener);
	tutorCostPane.add(tutorCostPanel);
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
    
    private JXTaskPane createLearnerFeedbackChartPane() {
	JXTaskPane feedbackChartPane = new JXTaskPane();
        feedbackChartPane.setScrollOnExpand(true);
	feedbackChartPane.setTitle("Learner Feedback");
	ChartPanel chartPanel = FeedbackChartFactory.createChartPanel(module);
	chartPanel.setPreferredSize(new Dimension(150, 200));
	feedbackChartPane.add(chartPanel);
	return feedbackChartPane;
    }

    private JXTaskPane createHoursChartPane() {
	JXTaskPane hoursChartPane = new JXTaskPane();
	hoursChartPane.setTitle("Teacher Time (hours)");
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
        LineItem selectedLineItem = sharedSelectionModel.getSelectedLineItem();
	if (selectedLineItem instanceof TLALineItem) {
	    TLAOkCancelDialog dialog = new TLAOkCancelDialog(this, true, module, (TLALineItem) selectedLineItem);
            dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
	    dialog.setTitle("Modify TLA for " + module.getModuleName() + " module");
	    dialog.setSelectedIndex(2);
	    dialog.setVisible(true);
	    //LOGGER.info("Dialog returnStatus: " + dialog.getReturnStatus());
	    //TODO--undo
	} else if (selectedLineItem instanceof ModuleLineItem) {
	    UndoManager undoManager = new UndoManager();
            ModuleActivityDialog dialog = new ModuleActivityDialog(this, true, module, (ModuleLineItem) selectedLineItem, undoManager);
            dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
            dialog.setTitle("Modify Module Activity for " + module.getModuleName() + " module");
            dialog.setVisible(true);
	    dialog.toFront();
	    if (dialog.getReturnStatus() == ModuleActivityDialog.RET_CANCEL) {
            //TODO--undo
		while(undoManager.canUndo()) {
		    undoManager.undo();
		}
	    }            
        } else {
	    LOGGER.warning("Unable to edit this line item");
	}
    }
    
    private void removeSelectedLineItem() {
        //TODO -- undo
        LineItem selectedLineItem = sharedSelectionModel.getSelectedLineItem();
	if (selectedLineItem != null) {
	    int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove \'" + selectedLineItem.getName() + "\'?", "Remove TLA", JOptionPane.YES_NO_OPTION);
	    if (reply == JOptionPane.YES_OPTION) {
                selectedLineItem.removeFrom(module);
	    } else {
		LOGGER.info("Remove cancelled");
	    }
	} else {
	    LOGGER.warning("Unable to remove this line item");
	}
    }
    
    private void addTLALineItem() {
        //Disable the menu item
        addTLALineItemMI.setEnabled(false);
        //TODO -- undo
        TLACreatorWizardIterator iterator = new TLACreatorWizardIterator(module);
	WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
	iterator.initialize(wizardDescriptor);
	// {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
	// {1} will be replaced by WizardDescriptor.Iterator.name()
	wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
	wizardDescriptor.setTitle("TLA Creator Wizard for " + module.getModuleName() + " module");
	Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        //Modeless within the document
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        //LOGGER.info("Cancelled: " + cancelled);
        if (!cancelled) {
	    //Get the newly created line item
	    TLALineItem lineItem = iterator.getLineItem();
	    //If the user created a new TLA, add it to their preferences
	    if (iterator.isVanilla()) {
		UserTLALibrary.getDefaultLibrary().addActivity(lineItem.getActivity());
	    }
            module.addTLALineItem(lineItem);
	    //TODO--undo
        }
        //Enable the menu item
        addTLALineItemMI.setEnabled(true);
    }
    
    private void addModuleLineItem() {
        //Disable the menu item
        addModuleLineItemMI.setEnabled(false);
        //TODO--undo
        ModuleLineItem li = new ModuleLineItem();
        //Give the dialog a null parent so that the document modal works properly
        ModuleActivityDialog dialog = new ModuleActivityDialog(null, true, module, li, new UndoManager());
        dialog.setSize(dialog.getPreferredSize());
        dialog.setTitle("Add Module Activity for " + module.getModuleName() + " module");
        //Modeless within the document
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setVisible(true);
        dialog.toFront();
        if (dialog.getReturnStatus() == ModuleActivityDialog.RET_OK) {
            module.addModuleItem(li);
	    //TODO--undo
        }
        //Enable the menu item
        addModuleLineItemMI.setEnabled(true);
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

    JMenuItem getDuplicateMenuItem() {
        return duplicateMI;
    }
    
    Module getModule() {
        return module;
    }

    JMenuItem getOpenHelpMenuItem() {
	return openHelpMI;
    }
}
