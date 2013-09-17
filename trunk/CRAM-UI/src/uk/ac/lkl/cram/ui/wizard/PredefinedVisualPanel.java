/*
 *  +Spaces Project, http://www.positivespaces.eu/
 *  
 *  Copyright (c) 2013, University of Essex, UK, 2013, All Rights Reserved.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package uk.ac.lkl.cram.ui.wizard;

import java.awt.Component;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLActivity;

public final class PredefinedVisualPanel extends JPanel {

    /**
     * Creates new form PredefinedVisualPanel
     */
    public PredefinedVisualPanel() {
	initComponents();
	DefaultListModel listModel = new DefaultListModel();
	jList1.setModel(listModel);
	Module m = AELMTest.populateModule();
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    listModel.addElement(lineItem.getActivity());
	}
	jList1.setCellRenderer(new TLActivityRenderer());
    }

    @Override
    public String getName() {
	return "Select Predefined Activity";
    }
    
    JList getList() {
	return jList1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jSeparator2 = new javax.swing.JSeparator();
        jRadioButton8 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton1, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton1.text")); // NOI18N
        jRadioButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton2, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton2.text")); // NOI18N
        jRadioButton2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton3, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton3.text")); // NOI18N
        jRadioButton3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton4, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton5, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton6, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton6.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton7, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton7.text")); // NOI18N

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton8, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jRadioButton8.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jRadioButton3)
                                    .addComponent(jRadioButton2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton4)
                                    .addComponent(jRadioButton5)
                                    .addComponent(jRadioButton6))
                                .addGap(1, 1, 1)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton8)
                                    .addComponent(jRadioButton7)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton3))
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton4)
                            .addComponent(jRadioButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jRadioButton6))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButton8)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jSeparator2))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

    private class TLActivityRenderer extends JLabel implements ListCellRenderer {

	public TLActivityRenderer() {
	    setOpaque(true);
	    setHorizontalAlignment(LEFT);
	    setVerticalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    } else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }
	    TLActivity tla = (TLActivity) value;
	    setText(tla.getName());
	    return this;
	}
    }
}
