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
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import uk.ac.lkl.cram.model.ExampleTest;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.ui.table.ColumnGroup;
import uk.ac.lkl.cram.ui.table.GroupableTableHeader;

/**
 * This panel provides the UI for the tutor hours table. It provides little 
 * functionality other than the rendering for the line items in the table. The
 * model of the table does most of the heavy lifting. It coordinates the list
 * selection with the other tables via a shared instance of LineItemSelectionModel.
 * It sets up the table so that it groups the preparation and support columns together.
 * @see LineItemSelectionModel
 * @see TutorHoursTableModel
 * @see ColumnGroup
 * @see GroupableTableHeader
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class TutorHoursPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(TutorHoursPanel.class.getName());
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat( "#.0" );
    private static final String[] TOOL_TIP_STRINGS = new String[7];
    
    static {
        for (int i = 0; i < TOOL_TIP_STRINGS.length; i++) {
            TOOL_TIP_STRINGS[i] = "";          
        }
        for (int i = 1; i < 4; i++) {
            TOOL_TIP_STRINGS[i] = "non-weekly hours + (weekly hours * number of weeks)";
        }
        for (int i = 4; i < 7; i++) {
            TOOL_TIP_STRINGS[i] = "(non-weekly hours + (weekly hours * number of weeks)) * number of individuals or groups";
        }
    }
    private final Module module;


    /**
     * Creates new TutorHoursPanel from the CRAM module. Creates the table UI
     * and creates a table model based on the module. Uses the sharedSelectionModel
     * to co-ordinate the table row selection with other tables in the UI.
     * @param m the CRAM module to be displayed
     * @param sharedSelectionModel a selection model that is used to co-ordinate selections across multiple tables  
     */
    public TutorHoursPanel(Module m, final LineItemSelectionModel sharedSelectionModel) {
	initComponents();
        this.module = m;
        //Create the appropriate table model
	tutorHoursTable.setModel(new TutorHoursTableModel(module));
	tutorHoursTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting()) {
                    //The user has selected a row in the table
                    int index = tutorHoursTable.getSelectedRow();
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
                    tutorHoursTable.clearSelection();
                } else {
                    tutorHoursTable.getSelectionModel().setSelectionInterval(index, index);
                    tutorHoursTable.scrollRectToVisible(new Rectangle(tutorHoursTable.getCellRect(index, 0, true)));
                }
            }
        });
        //Set the renderer for the left most column
        tutorHoursTable.setDefaultRenderer(String.class, new LineItemRenderer());

	TableColumnModel columnModel = tutorHoursTable.getColumnModel();
        
        //Set the preferred width of the leftmost column
        columnModel.getColumn(0).setPreferredWidth(150);
        
        //Set up the column groups	
        //The preparation column group
	ColumnGroup preparationGroup = new ColumnGroup("Preparation");
	preparationGroup.add(columnModel.getColumn(1));
	preparationGroup.add(columnModel.getColumn(2));
	preparationGroup.add(columnModel.getColumn(3));
        //The support column group
	ColumnGroup supportGroup = new ColumnGroup("Support");
	supportGroup.add(columnModel.getColumn(4));
	supportGroup.add(columnModel.getColumn(5));
	supportGroup.add(columnModel.getColumn(6));
        //Set up the table header
	GroupableTableHeader tableHeader = new GroupableTableHeader(columnModel);
        tableHeader.setToolTipStrings(TOOL_TIP_STRINGS);
	tableHeader.addColumnGroup(preparationGroup);
	tableHeader.addColumnGroup(supportGroup);
	tutorHoursTable.setTableHeader(tableHeader);
	tableHeader.revalidate();
        //Set renderer for preparation columns
        TableCellRenderer prepRenderer = new PreparationCellRenderer();
        columnModel.getColumn(1).setCellRenderer(prepRenderer);
        columnModel.getColumn(2).setCellRenderer(prepRenderer);
	columnModel.getColumn(3).setCellRenderer(prepRenderer);
        //Set renderer for support columns
        TableCellRenderer supportRenderer = new SupportCellRenderer();
        columnModel.getColumn(4).setCellRenderer(supportRenderer);
        columnModel.getColumn(5).setCellRenderer(supportRenderer);
	columnModel.getColumn(6).setCellRenderer(supportRenderer);
	setSize(tutorHoursTable.getSize());
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
        tutorHoursTable = new javax.swing.JTable();

        tutorHoursTable.setModel(new javax.swing.table.DefaultTableModel(
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
                java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
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
        tutorHoursTable.setRequestFocusEnabled(false);
        tutorHoursTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tutorHoursTable);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tutorHoursTable;
    // End of variables declaration//GEN-END:variables

    JTable getTable() {
        return tutorHoursTable;
    }

    /**
     * This class renders the left-most column. It italicises the last row and
     * adds a tooltip to each.
     */
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
    
    private class PreparationCellRenderer extends DefaultTableCellRenderer {

        @Override
        @SuppressWarnings("AssignmentToMethodParameter")
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            value = DECIMAL_FORMATTER.format((Number) value);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
            int rowCount = module.getTLALineItems().size();
            //We have a totals row at the bottom of the table
            if (row < rowCount) {
                TLALineItem li = module.getTLALineItems().get(row);
                List<ModulePresentation> modulePresentations = module.getModulePresentations();
                //Get the module presentation for this column
                ModulePresentation mp = modulePresentations.get(column - 1);
                //Get the preparation time for this line item
                PreparationTime pt = li.getPreparationTime(mp);
                StringBuilder builder = new StringBuilder(15);
                builder.append(pt.getNonWeekly());
                builder.append(" + (");
                builder.append(pt.getWeekly());
                builder.append(" * ");
                builder.append(li.getWeekCount(module));
                builder.append(")");
                setToolTipText(builder.toString());
            } 
            return this;
        }
    }
    
    private class SupportCellRenderer extends DefaultTableCellRenderer {

        @Override
        @SuppressWarnings("AssignmentToMethodParameter")
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            value = DECIMAL_FORMATTER.format((Number) value);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
            int rowCount = module.getTLALineItems().size();
            //We have a totals row at the bottom of the table
            if (row < rowCount) {
                TLALineItem li = module.getTLALineItems().get(row);
                List<ModulePresentation> modulePresentations = module.getModulePresentations();
                //Get the module presentation for this column
                ModulePresentation mp = modulePresentations.get(column - 4);
                //Get the preparation time for this line item
                SupportTime st = li.getSupportTime(mp);
                StringBuilder builder = new StringBuilder(15);
                builder.append("(");
                builder.append(st.getNonWeekly());
                builder.append(" + (");
                builder.append(st.getWeekly());
                builder.append(" * ");
                builder.append(li.getWeekCount(module));
                builder.append(")) * ");
                builder.append(li.getNumberOfIndividuals_Groups(mp, module));
                setToolTipText(builder.toString());
            } 
            return this;
        }
    }
    
    public static void main(String args[]) {
        final JFrame frame = new JFrame("Tutor Hours Panel Panel");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TutorHoursPanel(ExampleTest.populateModule(), new LineItemSelectionModel()));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }
}
