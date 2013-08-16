
package uk.ac.lkl.cram.ui;

import uk.ac.lkl.cram.ui.wizard.TLACreatorWizardIterator;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.ui.ModuleTableModel;
import uk.ac.lkl.cram.ui.obsolete.TableTestForm;

/**
 *
 * @author Bernard Horan
 */
public class ListOfTLAWizardPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(ListOfTLAWizardPanel.class.getName());

    private final Module module;

    /**
     * Creates new form ListOfTLAWizardPanel
     * @param module 
     */
    public ListOfTLAWizardPanel(Module module) {
	initComponents();
	this.module = module;
	activitiesTable.setModel(new ModuleTableModel(module));
	activitiesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
	activitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting() == false) {

		    if (activitiesTable.getSelectedRow() == -1) {
			//No selection, disable delete and edit buttons.
			deleteButton.setEnabled(false);
			editButton.setEnabled(false);

		    } else {
			//Selection, enable the delete and edit button.
			deleteButton.setEnabled(true);
			editButton.setEnabled(true);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        activitiesTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();

        activitiesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Activity", "Weekly Hours", "Non-Weekly Hours", "Total Hours"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(activitiesTable);

        addButton.setText("Add...");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addButton);

        deleteButton.setText("Delete...");
        deleteButton.setEnabled(false);
        jPanel1.add(deleteButton);

        editButton.setText("Edit...");
        editButton.setEnabled(false);
        jPanel1.add(editButton);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .add(0, 0, 0)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
//        TLAOkCancelDialog dialog = new TLAOkCancelDialog(new javax.swing.JFrame(), true, module);
//	dialog.setSize(dialog.getPreferredSize());
//	dialog.setVisible(true);
//	System.out.println(dialog.getReturnStatus());
//	if (dialog.getReturnStatus() == TLAOkCancelDialog.RET_OK) {
//	    addLineItem(dialog.getLineItem());
//	}
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
	LOGGER.info("Cancelled: " + cancelled);
        if (!cancelled) {
            addLineItem(iterator.getLineItem());
	    AELMTest.runReport(module);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable activitiesTable;
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

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
            java.util.logging.Logger.getLogger(TableTestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableTestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableTestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableTestForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        final JFrame frame = new JFrame("TLA Wizard Panel");
        frame.add(new ListOfTLAWizardPanel(AELMTest.populateModule()));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private void addLineItem(TLALineItem lineItem) {
	LOGGER.info("Adding lineItem: " + lineItem);
	module.addTLALineItem(lineItem);
    }

}
