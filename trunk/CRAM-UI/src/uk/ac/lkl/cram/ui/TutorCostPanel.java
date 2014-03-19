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
import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.ui.table.ColumnGroup;
import uk.ac.lkl.cram.ui.table.GroupableTableHeader;

/**
 * This panel provides the UI for the tutor cost table. It provides little 
 * functionality other than the rendering for the line items in the table. The
 * model of the table does most of the heavy lifting. It coordinates the list
 * selection with the other tables via a shared instance of LineItemSelectionModel.
 * It sets up the table so that it groups the preparation and support columns together.
 * @see LineItemSelectionModel
 * @see TutorCostTableModel
 * @see ColumnGroup
 * @see GroupableTableHeader
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class TutorCostPanel extends javax.swing.JPanel {

    /**
     * Creates new TutorCostPanel from the CRAM module. Creates the table UI
     * and creates a table model based on the module. Uses the sharedSelectionModel
     * to co-ordinate the table row selection with other tables in the UI.
     * @param module the CRAM module to be displayed
     * @param sharedSelectionModel a selection model that is used to co-ordinate selections across multiple tables  
     */
    public TutorCostPanel(final Module module, final LineItemSelectionModel sharedSelectionModel) {
	initComponents();
        //Create the appropriate table model
	tutorCostTable.setModel(new TutorCostTableModel(module));
	tutorCostTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting()) {
                    //The user has selected a row in the table
                    int index = tutorCostTable.getSelectedRow();
                    LineItem selectedLineItem = null;
                    //Check that the user hasn't just deselected a row
                    if (index != -1) {
                        int lineItemCount = module.getLineItems().size();
                        //Last row isn't really a lineItem, it's the totals row
                        if (index < lineItemCount) {
                            selectedLineItem = module.getLineItems().get(index);
                        }
                    }
                    //Tell the shared selection model that the selecion has changed
                    sharedSelectionModel.setSelectedLineItem(selectedLineItem);
                }
            }
        });
        sharedSelectionModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent lse) {
                //The selection in the shared selection model has changed
                //So update the selected row in my table
                LineItem selectedItem = (LineItem) lse.getNewValue();
                int index = module.getLineItems().indexOf(selectedItem);
                //If the user has deselected a row
                if (index == -1) {
                    tutorCostTable.clearSelection();
                } else {
                    tutorCostTable.getSelectionModel().setSelectionInterval(index, index);
                    tutorCostTable.scrollRectToVisible(new Rectangle(tutorCostTable.getCellRect(index, 0, true)));
                }
            }
        });
        
        TableColumnModel columnModel = tutorCostTable.getColumnModel();
        //Set the preferred width of the left most column
        columnModel.getColumn(0).setPreferredWidth(150);
	
	Enumeration<TableColumn> columnEnum = columnModel.getColumns();
        //Create a formatter for the currency values
	final NumberFormat formatter = NumberFormat.getCurrencyInstance();
	formatter.setMaximumFractionDigits(0);
        //Create a renderer to render the currency values
	DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
	    @Override
	    public void setValue(Object value) {
		//  Format the Object before setting its value in the renderer
		try {
		    if (value != null) {
			value = formatter.format(value);
		    }
		} catch (IllegalArgumentException e) {
		}
		super.setValue(value);
	    }
	};
	currencyRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        //Set the renderer of the left most column
	columnEnum.nextElement().setCellRenderer(new LineItemRenderer());
        //Set the renderer of the remaining columns
	while (columnEnum.hasMoreElements()) {
	    columnEnum.nextElement().setCellRenderer(currencyRenderer);
	}
        //Create the preparation group of columns
	ColumnGroup preparationGroup = new ColumnGroup("Preparation");
	preparationGroup.add(columnModel.getColumn(1));
	preparationGroup.add(columnModel.getColumn(2));
	preparationGroup.add(columnModel.getColumn(3));
        //Create the support group of columns
	ColumnGroup supportGroup = new ColumnGroup("Support");
	supportGroup.add(columnModel.getColumn(4));
	supportGroup.add(columnModel.getColumn(5));
	supportGroup.add(columnModel.getColumn(6));
        //Create the table header
	GroupableTableHeader tableHeader = new GroupableTableHeader(columnModel);
	tableHeader.addColumnGroup(preparationGroup);
	tableHeader.addColumnGroup(supportGroup);
	tutorCostTable.setTableHeader(tableHeader);
	tableHeader.revalidate();
        
	setSize(tutorCostTable.getSize());
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
        tutorCostTable = new javax.swing.JTable();

        tutorCostTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Activity", "Run 1", "Run 2", "Run 3", "Run 1", "Run 2", "Run 3"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tutorCostTable.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(tutorCostTable);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 186, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tutorCostTable;
    // End of variables declaration//GEN-END:variables

    JTable getTable() {
        return tutorCostTable;
    }

    private class LineItemRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {

	    super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
		    row, column);
	    //Bold the last row
	    if (row == table.getModel().getRowCount() - 1) {
		setFont(this.getFont().deriveFont(Font.ITALIC));
	    }
	    setToolTipText((String) value);
	    return this;
	}
    }

}
