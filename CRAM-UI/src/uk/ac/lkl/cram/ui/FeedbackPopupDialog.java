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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import uk.ac.lkl.cram.model.LearnerFeedback;
import uk.ac.lkl.cram.model.TLALineItem;

/**
 * This class provides a popup dialog from the feedback chart. It contains a 
 * list of all the TLAs, and renders the name of the TLA as well as the kind of
 * feedback provided by the TLA.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings({"serial"})
public class FeedbackPopupDialog extends javax.swing.JDialog {
    private static final Logger LOGGER = Logger.getLogger(FeedbackPopupDialog.class.getName());
    /**
     * Map to determine how to render the LearnerFeedback
     */
    private final static Map<LearnerFeedback, String> RENDER_MAP = new HashMap<>();
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    static final int RET_OK = 1;
    
    /**
     * Initialise the render map 
     */
    static {
        RENDER_MAP.put(LearnerFeedback.TEL, "Computer-based");
        RENDER_MAP.put(LearnerFeedback.PEER_ONLY, "Peer");
        RENDER_MAP.put(LearnerFeedback.TUTOR, "Tutor");
        RENDER_MAP.put(LearnerFeedback.NONE, "None");
    }

    /**
     * Creates new form LearningTypePopupDialog
     * @param parent the parent for this dialog, which has an impact on modality
     * @param modal if true, make this dialog modal
     * @param lineItems the line items to display in the dialog
     */
    public FeedbackPopupDialog(java.awt.Frame parent, boolean modal, Collection<TLALineItem> lineItems) {
        super(parent, modal);
        initComponents();
        //Create a list model containing the line items
        DefaultListModel<TLALineItem> listModel = new DefaultListModel<>();
        for (TLALineItem tLALineItem : lineItems) {
            listModel.addElement(tLALineItem);
        }
        lineItemList.setModel(listModel);
        lineItemList.setCellRenderer(new TLALineItemRenderer());
        lineItemList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                //Only interested when the user has stopped making a selection
		if (!lse.getValueIsAdjusting()) {

		    if (lineItemList.getSelectedIndex() == -1) {
			//No selection, disable modify button.
			modifyButton.setEnabled(false);

		    } else {
			//Selection, enable the modify button.
			modifyButton.setEnabled(true);
		    }
		}
            }
        });
        //Add a mouse listener--double-click means modify the selected row
        lineItemList.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
		    modifyButtonActionPerformed(null);
		}
	    }
	});

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modifyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lineItemList = new javax.swing.JList<TLALineItem>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(modifyButton, org.openide.util.NbBundle.getMessage(FeedbackPopupDialog.class, "FeedbackPopupDialog.modifyButton.text")); // NOI18N
        modifyButton.setEnabled(false);
        modifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(FeedbackPopupDialog.class, "FeedbackPopupDialog.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        lineItemList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lineItemList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lineItemList);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .add(modifyButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cancelButton)
                .addContainerGap())
            .add(jScrollPane1)
        );

        layout.linkSize(new java.awt.Component[] {cancelButton, modifyButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(modifyButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(modifyButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_modifyButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * For testing purposes only
     * @param args the command line arguments (ignored)
     */
    public static void main(String args[]) {
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FeedbackPopupDialog dialog = new FeedbackPopupDialog(new javax.swing.JFrame(), true, new ArrayList<TLALineItem>());
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
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<TLALineItem> lineItemList;
    private javax.swing.JButton modifyButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    TLALineItem getSelectedTLALineItem() {
        return lineItemList.getSelectedValue();
    }
    
    /**
     * Renderer for the line items
     */
    private class TLALineItemRenderer extends JLabel implements ListCellRenderer<TLALineItem> {

        TLALineItemRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends TLALineItem> list, TLALineItem lineItem, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            @SuppressWarnings("StringBufferWithoutInitialCapacity")
            StringBuilder builder = new StringBuilder();
            builder.append(lineItem.getName());
            builder.append( " (");
            //Human readable version of the learner feedback
            builder.append(RENDER_MAP.get(lineItem.getActivity().getLearnerFeedback()));
            builder.append(")");
            setText(builder.toString());
            return this;
        }
    }
}
