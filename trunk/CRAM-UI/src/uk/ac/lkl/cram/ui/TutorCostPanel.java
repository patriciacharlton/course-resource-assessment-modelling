
package uk.ac.lkl.cram.ui;

import java.text.NumberFormat;
import java.util.Enumeration;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import uk.ac.lkl.cram.ui.table.ColumnGroup;
import uk.ac.lkl.cram.ui.table.GroupableTableHeader;
import javax.swing.table.TableColumnModel;
import uk.ac.lkl.cram.model.Module;

/**
 * $Date$
 * @author Bernard Horan
 */
public class TutorCostPanel extends javax.swing.JPanel {

    /**
     * Creates new form TutorCostPanel
     * @param module 
     */
    public TutorCostPanel(Module module) {
	initComponents();
	tutorCostTable.setModel(new TutorCostTableModel(module));
	tutorCostTable.getColumnModel().getColumn(0).setPreferredWidth(150);
	TableColumnModel tableColumnModel = tutorCostTable.getColumnModel();
	Enumeration<TableColumn> columnEnum = tableColumnModel.getColumns();
	final NumberFormat formatter = NumberFormat.getCurrencyInstance();
	formatter.setMaximumFractionDigits(0);
	DefaultTableCellRenderer tcRenderer = new DefaultTableCellRenderer() {
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
	tcRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
	columnEnum.nextElement(); //Skip first column
	while (columnEnum.hasMoreElements()) {
	    columnEnum.nextElement().setCellRenderer(tcRenderer);
	}
	ColumnGroup g_2nd = new ColumnGroup("Preparation");
	g_2nd.add(tableColumnModel.getColumn(1));
	g_2nd.add(tableColumnModel.getColumn(2));
	g_2nd.add(tableColumnModel.getColumn(3));
	ColumnGroup g_3rd = new ColumnGroup("Support");
	g_3rd.add(tableColumnModel.getColumn(4));
	g_3rd.add(tableColumnModel.getColumn(5));
	g_3rd.add(tableColumnModel.getColumn(6));
	GroupableTableHeader tableHeader = new GroupableTableHeader(tableColumnModel);
	tableHeader.addColumnGroup(g_2nd);
	tableHeader.addColumnGroup(g_3rd);
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
        tutorCostTable.setEnabled(false);
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
}