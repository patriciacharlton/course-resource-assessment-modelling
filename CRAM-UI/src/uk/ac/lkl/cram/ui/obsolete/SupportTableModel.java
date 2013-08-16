/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.lkl.cram.ui.obsolete;

import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.SupportTime;

/**
 *
 * @author bernard
 */
public class SupportTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Presentation", "Weekly", "Non-Weekly", "% Senior"};
    private final Module module;
    private final TLALineItem lineItem;
    
    public SupportTableModel(Module module, TLALineItem lineItem) {
        super();
        this.lineItem = lineItem;
        this.module = module;
    }

    @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
    
    @Override
    public String getColumnName(int i) {
        return COLUMN_NAMES[i];
    }

    @Override
    public Object getValueAt(int row, int column) {
        ModulePresentation mp = module.getModulePresentations().get(row);
        SupportTime pt = lineItem.getSupportTime(mp);
        String presentationName = "";
        switch (row) {
            case 0:
                presentationName = "1st Presentation";
                break;
            case 1:
                presentationName = "2nd Presentation";
                break;
            case 2:
                presentationName = "Stable State";
                break;
            default:
                throw new RuntimeException("Invalid Row: " + row);
        }
	//presentationName = mp.toString();
        switch (column) {
            case 0:
                return presentationName;
            case 1: {
                if (pt == null) {
                    return new Float(0);
                } else {
                    return pt.getWeekly();
                }
            }
            case 2:
                if (pt == null) {
                    return new Float(0);
                } else {
                    return pt.getNonWeekly();
                }
            case 3:
                if (pt == null) {
                    return new Float(0);
                } else {
                    return pt.getSeniorRate();
                }
            default:
                throw new RuntimeException("Invalid column: " + column);
                

        }
    }
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */

    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        //Every column but the first one is editable
        return col != 0;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int column) {
        System.out.println("setValueAt row: " + row);
        ModulePresentation mp = module.getModulePresentations().get(row);
        SupportTime st = lineItem.getSupportTime(mp);
        switch (column) {
            case 1:
                st.setWeekly((Float) value);
                break;
            case 2:
                st.setNonWeekly((Float) value);
                break;
            case 3:
                st.setSeniorRate((Float) value);

        }
        fireTableCellUpdated(row, column);
    }
}
