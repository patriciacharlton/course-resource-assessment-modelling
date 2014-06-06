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

import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import javax.xml.bind.JAXBException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.jdesktop.swingx.JXTaskPane;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Utilities;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModuleLineItem;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.UserTLALibrary;
import uk.ac.lkl.cram.model.io.ModuleMarshaller;
import uk.ac.lkl.cram.ui.chart.FeedbackChartMaker;
import uk.ac.lkl.cram.ui.chart.HoursChartMaker;
import uk.ac.lkl.cram.ui.chart.LearningExperienceChartMaker;
import uk.ac.lkl.cram.ui.chart.LearningTypeChartMaker;
import uk.ac.lkl.cram.ui.report.Report;
import uk.ac.lkl.cram.ui.undo.NamedCompoundEdit;
import uk.ac.lkl.cram.ui.undo.RemoveLineItemEdit;
import uk.ac.lkl.cram.ui.undo.UndoHandler;
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
    
    private final static Cursor HAND = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private final static Cursor WAIT = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        
    //The module rendered by this frame
    private final Module module;
    private File moduleFile;
    //Keeps track of the selection in the various tables in the frame
    private final LineItemSelectionModel sharedSelectionModel = new LineItemSelectionModel();
    //Listens for double clicks on the various tables in the frame
    private final MouseListener doubleClickListener;
    //Manages undos for the module--one undo handler per module frame
    private final UndoHandler undoHandler;
    

    /**
     * Creates new form ModuleFrame
     * @param module the module that this window displays
     * @param file  
     */
    public ModuleFrame(final Module module, File file) {
        this.module = module;
        this.moduleFile = file;
        this.setTitle(module.getModuleName());
        initComponents();
        //Add undo mechanism
        undoHandler = new UndoHandler();
        JMenuItem undoMI = editMenu.add(undoHandler.getUndoAction());
        JMenuItem redoMI = editMenu.add(undoHandler.getRedoAction());
        //Listen to the undo handler for when there is something to undo
        undoHandler.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                //We're assuming there's only one property
                //If the new value is true, then there's something to undo
                //And thus the save menu item should be enabled
                Boolean newValue = (Boolean) evt.getNewValue();
                if (moduleFile != null) {
                    saveMI.setEnabled(newValue);
                }
            }
        });
        editMenu.addSeparator();
        //Add cut, copy & paste menu items
        JMenuItem cutMI = editMenu.add(new JMenuItem(new DefaultEditorKit.CutAction()));
        cutMI.setText("Cut");
        JMenuItem copyMI = editMenu.add(new JMenuItem(new DefaultEditorKit.CopyAction()));
        copyMI.setText("Copy");
        JMenuItem pasteMI = editMenu.add(new JMenuItem(new DefaultEditorKit.PasteAction()));
        pasteMI.setText("Paste");        
        
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
        
        //Set Accelerator keys
        undoMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	redoMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        cutMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	copyMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	pasteMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	openMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	quitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	//Remove quit menu item and separator from file menu on Mac
	if (Utilities.isMac()) {
	    fileMenu.remove(quitSeparator);
	    fileMenu.remove(quitMI);
	}
        
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

        jScrollPane1 = new javax.swing.JScrollPane();
        leftTaskPaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        jScrollPane3 = new javax.swing.JScrollPane();
        rightTaskPaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        windowMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMI = new javax.swing.JMenuItem();
        openMI = new javax.swing.JMenuItem();
        duplicateMI = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exportReportMI = new javax.swing.JMenuItem();
        saveMI = new javax.swing.JMenuItem();
        saveAsMI = new javax.swing.JMenuItem();
        quitSeparator = new javax.swing.JPopupMenu.Separator();
        quitMI = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        moduleMenu = new javax.swing.JMenu();
        addTLALineItemMI = new javax.swing.JMenuItem();
        addModuleLineItemMI = new javax.swing.JMenuItem();
        modifyLineItemMI = new javax.swing.JMenuItem();
        removeLineItemMI = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        openHelpMI = new javax.swing.JMenuItem();

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
        fileMenu.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(exportReportMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.exportReportMI.text")); // NOI18N
        exportReportMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportReportMIActionPerformed(evt);
            }
        });
        fileMenu.add(exportReportMI);

        org.openide.awt.Mnemonics.setLocalizedText(saveMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.saveMI.text")); // NOI18N
        saveMI.setEnabled(false);
        saveMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMIActionPerformed(evt);
            }
        });
        fileMenu.add(saveMI);

        org.openide.awt.Mnemonics.setLocalizedText(saveAsMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.saveAsMI.text")); // NOI18N
        fileMenu.add(saveAsMI);
        fileMenu.add(quitSeparator);

        org.openide.awt.Mnemonics.setLocalizedText(quitMI, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.quitMI.text")); // NOI18N
        fileMenu.add(quitMI);

        windowMenuBar.add(fileMenu);

        org.openide.awt.Mnemonics.setLocalizedText(editMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.editMenu.text")); // NOI18N
        windowMenuBar.add(editMenu);

        org.openide.awt.Mnemonics.setLocalizedText(moduleMenu, org.openide.util.NbBundle.getMessage(ModuleFrame.class, "ModuleFrame.moduleMenu.text")); // NOI18N

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

    private void saveMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMIActionPerformed
        saveModule();
    }//GEN-LAST:event_saveMIActionPerformed

    private void exportReportMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportReportMIActionPerformed
        exportReport();
    }//GEN-LAST:event_exportReportMIActionPerformed

    /**
     * Used for testing purposes only.
     * @param args the command line arguments (ignored)
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
            public void run() {
                new ModuleFrame(AELMTest.populateModule(), null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addModuleLineItemMI;
    private javax.swing.JMenuItem addTLALineItemMI;
    private javax.swing.JMenuItem duplicateMI;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exportReportMI;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
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
    private javax.swing.JPopupMenu.Separator quitSeparator;
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
	courseDataPane.add(new ModulePanel(module, undoHandler));
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
        final LearningTypeChartMaker maker = new LearningTypeChartMaker(module);
	final ChartPanel chartPanel = maker.getChartPanel();
        //Add a mouse listener to the chart
	chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                //Get the mouse event
                MouseEvent trigger = cme.getTrigger();
                //Test if the mouse event is a left-button
                if (trigger.getButton() == MouseEvent.BUTTON1 && trigger.getClickCount() == 2) {
                    //Check that the mouse click is on a segment of the pie
                    if (cme.getEntity() instanceof PieSectionEntity) {
                        //Get the selected segment of the pie
                        PieSectionEntity pieSection = (PieSectionEntity) cme.getEntity();
                        //Get the key that corresponds to that segment--this is a learning type
                        String key = pieSection.getSectionKey().toString();
                        //Get the set of tlalineitems whose activity contains that learning type
                        Set<TLALineItem> relevantTLAs = maker.getLearningTypeMap().get(key);
                        //Create a pop up dialog containing that set of tlalineitems
                        LearningTypePopupDialog popup = new LearningTypePopupDialog((Frame) SwingUtilities.getWindowAncestor(chartPanel), true, relevantTLAs, key);
                        //Set the title of the popup to indicate which learning type was selected
                        popup.setTitle("Activities with \'" + key + "\'");
                        //Centre the popup at the location of the mouse click
                        Point location = trigger.getLocationOnScreen();
                        int w = popup.getWidth();
                        int h = popup.getHeight();
                        popup.setLocation(location.x - w / 2, location.y - h / 2);
                        popup.setVisible(true);
                        int returnStatus = popup.getReturnStatus();
                        if (returnStatus == LearningTypePopupDialog.RET_OK) {
                            modifyTLALineItem(popup.getSelectedTLALineItem(), 0);
                        }
                    }
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //Set the cursor shape according to the location of the cursor
                if (cme.getEntity() instanceof PieSectionEntity) {
                    chartPanel.setCursor(HAND);
                } else {
                    chartPanel.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        chartPanel.setPreferredSize(new Dimension(150, 200));
	typeChartPane.add(chartPanel);
	return typeChartPane;
    }

    private JXTaskPane createLearningExperienceChartPane() {
	JXTaskPane experienceChartPane = new JXTaskPane();
        experienceChartPane.setScrollOnExpand(true);
	experienceChartPane.setTitle("Learning Experiences");
        final LearningExperienceChartMaker maker = new LearningExperienceChartMaker(module);
	final ChartPanel chartPanel = maker.getChartPanel();
	//Add a mouselistener, listening for a double click on a bar of the stacked bar
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                //Get the mouse event
                MouseEvent trigger = cme.getTrigger();
                //Test if the mouse event is a left-button
                if (trigger.getButton() == MouseEvent.BUTTON1 && trigger.getClickCount() == 2) {
                    //Get the selected segment of the pie
                        CategoryItemEntity bar = (CategoryItemEntity) cme.getEntity();
                        //Get the row key that corresponds to that segment--this is a learning experience
                        String key = bar.getRowKey().toString();
                        //Get the set of tlalineitems whose activity contains that learning type
                        Set<TLALineItem> relevantTLAs = maker.getLearningExperienceMap().get(key);
                        //Create a pop up dialog containing that set of tlalineitems
                        LearningExperiencePopupDialog popup = new LearningExperiencePopupDialog((Frame) SwingUtilities.getWindowAncestor(chartPanel), true, relevantTLAs);
                        //Set the title of the popup to indicate which learning type was selected
                        popup.setTitle("Activities with \'" + key + "\'");
                        //Centre the popup at the location of the mouse click
                        Point location = trigger.getLocationOnScreen();
                        int w = popup.getWidth();
                        int h = popup.getHeight();
                        popup.setLocation(location.x - w/2, location.y - h/2);
                        popup.setVisible(true);
                        int returnStatus = popup.getReturnStatus();
                        if (returnStatus == LearningTypePopupDialog.RET_OK) {
                            modifyTLALineItem(popup.getSelectedTLALineItem(), 0);
                        }
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //Set the cursor shape according to the location of the cursor
                if (cme.getEntity() instanceof CategoryItemEntity) {
                    chartPanel.setCursor(HAND);
                } else {
                    chartPanel.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        chartPanel.setPreferredSize(new Dimension(125,75));
	chartPanel.setMinimumDrawHeight(75);
	experienceChartPane.add(chartPanel);
	return experienceChartPane;
    }
    
    private JXTaskPane createLearnerFeedbackChartPane() {
	JXTaskPane feedbackChartPane = new JXTaskPane();
        feedbackChartPane.setScrollOnExpand(true);
	feedbackChartPane.setTitle("Learner Feedback");
        FeedbackChartMaker maker = new FeedbackChartMaker(module);
	final ChartPanel chartPanel = maker.getChartPanel();
        //Add a mouselistener, listening for a double click on a bar of the stacked bar
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                //Get the mouse event
                MouseEvent trigger = cme.getTrigger();
                //Test if the mouse event is a left-button
                if (trigger.getButton() == MouseEvent.BUTTON1 && trigger.getClickCount() == 2) {
                    
                        //Create a pop up dialog containing that the tlalineitems
                        FeedbackPopupDialog popup = new FeedbackPopupDialog((Frame) SwingUtilities.getWindowAncestor(chartPanel), true, module.getTLALineItems());
                        //Set the title of the popup 
                        popup.setTitle("All Activities");
                        //Centre the popup at the location of the mouse click
                        Point location = trigger.getLocationOnScreen();
                        int w = popup.getWidth();
                        int h = popup.getHeight();
                        popup.setLocation(location.x - w/2, location.y - h/2);
                        popup.setVisible(true);
                        int returnStatus = popup.getReturnStatus();
                        if (returnStatus == LearningTypePopupDialog.RET_OK) {
                            modifyTLALineItem(popup.getSelectedTLALineItem(), 1);
                        }
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //Set the cursor shape according to the location of the cursor
                if (cme.getEntity() instanceof CategoryItemEntity) {
                    chartPanel.setCursor(HAND);
                } else {
                    chartPanel.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
	chartPanel.setPreferredSize(new Dimension(150, 200));
	feedbackChartPane.add(chartPanel);
	return feedbackChartPane;
    }

    private JXTaskPane createHoursChartPane() {
	JXTaskPane hoursChartPane = new JXTaskPane();
	hoursChartPane.setTitle("Teacher Time (hours)");
        hoursChartPane.setScrollOnExpand(true);
	ChartPanel chartPanel = (new HoursChartMaker(module)).getChartPanel();
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
            modifyTLALineItem((TLALineItem) selectedLineItem, 2);
	} else if (selectedLineItem instanceof ModuleLineItem) {
            //Create a compound edit that can be used later for undo
            NamedCompoundEdit cEdit = new NamedCompoundEdit("Modify Module Activity");
            ModuleActivityDialog dialog = new ModuleActivityDialog(this, true, module, (ModuleLineItem) selectedLineItem, cEdit);
            dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
            dialog.setTitle("Modify Module Activity for " + module.getModuleName() + " module");
            dialog.setVisible(true);
	    dialog.toFront();
            //If the user clicked OK, create an edit that can be undone
            if (dialog.getReturnStatus() == ModuleActivityDialog.RET_OK) {
                undoHandler.addEdit(cEdit);
            }
        } else {
	    LOGGER.warning("Unable to edit this line item");
	}
    }
    
    private void modifyTLALineItem(TLALineItem lineItem, int index) {
        //Create a compound edit that can be used later for undo
        NamedCompoundEdit cEdit = new NamedCompoundEdit("Modify TLA");
        TLAOkCancelDialog dialog = new TLAOkCancelDialog(ModuleFrame.this, true, module, lineItem, cEdit);
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setTitle("Modify TLA for " + module.getModuleName() + " module");
        dialog.setSelectedIndex(index);
        dialog.setVisible(true);
        //If the user clicked OK, create an edit that can be undone
        if (dialog.getReturnStatus() == TLAOkCancelDialog.RET_OK) {
            undoHandler.addEdit(cEdit);
        }
    }
    
    private void removeSelectedLineItem() {
        LineItem selectedLineItem = sharedSelectionModel.getSelectedLineItem();
	if (selectedLineItem != null) {
	    int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove \'" + selectedLineItem.getName() + "\'?", "Remove TLA", JOptionPane.YES_NO_OPTION);
	    if (reply == JOptionPane.YES_OPTION) {
                int i = selectedLineItem.removeFrom(module);
                UndoableEdit edit = new RemoveLineItemEdit(module, selectedLineItem, i);
                undoHandler.addEdit(edit);
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
	    //No undo support here--assume user will just remove the line item
        }
        //Enable the menu item
        addTLALineItemMI.setEnabled(true);
    }
    
    private void addModuleLineItem() {
        //Disable the menu item
        addModuleLineItemMI.setEnabled(false);
        ModuleLineItem li = new ModuleLineItem();
        //Give the dialog a null parent so that the document modal works properly
        ModuleActivityDialog dialog = new ModuleActivityDialog(null, true, module, li, new CompoundEdit());
        dialog.setSize(dialog.getPreferredSize());
        dialog.setTitle("Add Module Activity for " + module.getModuleName() + " module");
        //Modeless within the document
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setVisible(true);
        dialog.toFront();
        if (dialog.getReturnStatus() == ModuleActivityDialog.RET_OK) {
            module.addModuleItem(li);
	    //No undo support here--assume user will just remove the line item
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

    void discardEdits() {
        undoHandler.discardAllEdits();
        saveMI.setEnabled(false);
    }

    private void saveModule() {
        try {
            //Create a marshaller to marshall the module
            ModuleMarshaller marshaller = new ModuleMarshaller(moduleFile);
            //Marshall the module
            marshaller.marshallModule(module);
            discardEdits();
        } catch (JAXBException ioe) {
            LOGGER.log(Level.SEVERE, "Failed to save file", ioe);
            JOptionPane.showMessageDialog(this, ioe.getLocalizedMessage(), "Unable to save module", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportReport() {
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Export CRAM Module");
        FileFilter filter = new FileNameExtensionFilter("Word Document", "docx");
        jfc.setFileFilter(filter);
        jfc.setSelectedFile(new File(module.getModuleName() + ".docx"));
	//Open the dialog and wait for the user to provide a name for the file
        int returnVal = jfc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
	    //Add the file extension
            if (!jfc.getSelectedFile().getAbsolutePath().endsWith(".docx")) {
                file = new File(jfc.getSelectedFile() + ".docx");
            }
            try {
                this.setCursor(WAIT);
                Report report = new Report(module);
                report.save(file);
            } catch (Docx4JException ex) {
                LOGGER.log(Level.SEVERE, "Failed to export report", ex);
                JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Failed to export report", JOptionPane.ERROR_MESSAGE);
            } finally {
                this.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    void setModuleFile(File file) {
        moduleFile = file;
    }
}
