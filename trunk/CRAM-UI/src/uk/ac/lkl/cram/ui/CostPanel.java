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
import java.text.NumberFormat;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;

/**
 * This class represents the panel of costs in the module frame, and acts mainly as a holder for the 
 * table of costs.
 * @see CostTableModel
 * $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class CostPanel extends javax.swing.JPanel {

    /**
     * Creates new form CostPanel
     * @param module the CRAM module that is being edited
     */
    public CostPanel(Module module) {
	initComponents();
	costTable.setModel(new CostTableModel(module));
	TableColumnModel tableColumnModel = costTable.getColumnModel();
	Enumeration<TableColumn> columnEnum = tableColumnModel.getColumns();
	//set the renderer for the first column
	columnEnum.nextElement().setCellRenderer(new ActivityRenderer());
	//set the renderer for the remaining columns
	DefaultTableCellRenderer tcRenderer = new TCRenderer();
	tcRenderer.setHorizontalAlignment(SwingConstants.RIGHT);	
	while (columnEnum.hasMoreElements()) {
	    columnEnum.nextElement().setCellRenderer(tcRenderer);
	}
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
        costTable = new javax.swing.JTable();

        costTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "", "Run 1", "Run 2", "Run 3"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
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
        costTable.setEnabled(false);
        costTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(costTable);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable costTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * For testing purposes only
     * @param args (ignored)
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("Cost Test");
	Module m = AELMTest.populateModule();
	CostPanel panel = new CostPanel(m);
	frame.setContentPane(panel);
	frame.setVisible(true);
    }

    /**
     * Renderer for the table columns that contain numbers
     */
    private static class TCRenderer extends DefaultTableCellRenderer {

	static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();

	TCRenderer() {
	    currencyFormatter.setMaximumFractionDigits(0);
	}

	@Override
	@SuppressWarnings("AssignmentToMethodParameter")
	public Component getTableCellRendererComponent(JTable table,
		Object value,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int column) {
	    //Only format currency if in row 4 or greater
	    if (row > 3) {		
		if (value != null) {
		    value = currencyFormatter.format(value);
		}		
	    } 
	    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	}
    }
    
    /**
     * Renderer for the activity column, which simply adds a tooltip
     */
    private class ActivityRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {

	    super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
		    row, column);
	    setToolTipText((String) value);

	    return this;
	}
    }

}
