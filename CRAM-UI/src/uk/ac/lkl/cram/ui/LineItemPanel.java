
package uk.ac.lkl.cram.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.ui.table.ToolTipHeader;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("serial")
public class LineItemPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(LineItemPanel.class.getName());
    private static final String[] toolTipStr = {"","","","","non-weekly hours + (weekly hours * number of weeks)"};
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat( "#.0" );

    public static final String PROP_SELECTED_LINEITEM = " selected_line_item";
    private LineItem selectedLineItem = null;
    private final Module module;

    

    /**
     * Creates new form LineItemPanel
     * @param module 
     */
    public LineItemPanel(Module module) {
	initComponents();
	this.module = module;
	activitiesTable.setModel(new ModuleTableModel(module, true));
	activitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

	    @Override
	    public void valueChanged(ListSelectionEvent lse) {
		if (!lse.getValueIsAdjusting()) {
		    ListSelectionModel lsModel = (ListSelectionModel) lse.getSource();
		    int selectionIndex = lsModel.getMinSelectionIndex();
		    setSelectionIndex(selectionIndex);
		}
	    }
	});
	TableCellRenderer activityRenderer = new ActivityRenderer();
	activitiesTable.getColumnModel().getColumn(0).setCellRenderer(activityRenderer);
	activitiesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
	TableCellRenderer totalRenderer = new TotalRenderer();
	activitiesTable.getColumnModel().getColumn(4).setCellRenderer(totalRenderer);
	ToolTipHeader ttHeader = new ToolTipHeader(activitiesTable.getColumnModel());
	ttHeader.setToolTipStrings(toolTipStr);
	activitiesTable.setTableHeader(ttHeader);
	activitiesTable.getTableHeader().setPreferredSize(new Dimension(activitiesTable.getColumnModel().getTotalColumnWidth(),36));
	setSize(activitiesTable.getSize());
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

        activitiesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Activity", "Number of Weeks", "Weekly Learner Hours", "Non-Weekly Hours", "Total Hours"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        activitiesTable.setRequestFocusEnabled(false);
        activitiesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        activitiesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(activitiesTable);

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
    private javax.swing.JTable activitiesTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
    private void setSelectionIndex(int index) {
	LineItem oldValue = selectedLineItem;
	selectedLineItem = null;
	if (index != -1) {
	    int tlaCount = module.getTLALineItems().size();
	    //Last row isn't really a TLA, it's the self-regulated learning
	    if (index < tlaCount) {
		selectedLineItem = module.getTLALineItems().get(index);
	    }
	} 
	firePropertyChange(PROP_SELECTED_LINEITEM, oldValue, selectedLineItem);
    }

    JTable getTable() {
	return activitiesTable;
    }

    LineItem getSelectedLineItem() {
	return selectedLineItem;
    }
    
    @SuppressWarnings("serial")
    private class ActivityRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {

	    super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
		    row, column);
	    //Italicise the last row
	    if (row == table.getModel().getRowCount() - 1) {
		setFont(this.getFont().deriveFont(Font.ITALIC));
                setToolTipText("Learning hours not included in TLAs");
	    } else {
                setToolTipText((String) value);
            }   

	    return this;
	}
    }
    
    private class TotalRenderer extends DefaultTableCellRenderer {

        @Override
        @SuppressWarnings("AssignmentToMethodParameter")
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            value = DECIMAL_FORMATTER.format((Number) value);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
            int rowCount = module.getTLALineItems().size();
            //We may have a dummy row at the bottom of the table
            if (row < rowCount) {
                TLALineItem li = module.getTLALineItems().get(row);
                @SuppressWarnings("StringBufferWithoutInitialCapacity")
                StringBuilder builder = new StringBuilder();
                builder.append(li.getNonWeeklyLearnerHourCount());
                builder.append(" + (");
                builder.append(li.getWeeklyLearnerHourCount());
                builder.append(" * ");
                builder.append(li.getWeekCount());
                builder.append(")");
                setToolTipText(builder.toString());
            } else {
                setToolTipText("Remainder of Student Hours");
            }
            return this;
        }
    }
}