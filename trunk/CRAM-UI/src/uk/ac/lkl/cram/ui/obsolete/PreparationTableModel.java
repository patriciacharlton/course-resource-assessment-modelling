package uk.ac.lkl.cram.ui.obsolete;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.PreparationTime;

/**
 *
 * @author bernard
 */
public class PreparationTableModel extends AbstractTableModel {
    private static final Logger LOGGER = Logger.getLogger(PreparationTableModel.class.getName());


    private static final String[] COLUMN_NAMES = {"Presentation", "Weekly", "Non-Weekly", "% Senior"};
    private static final Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Integer.class
            };
    private final Module module;
    private final TLALineItem lineItem;
    
    public PreparationTableModel(Module module, TLALineItem lineItem) {
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
        return types[columnIndex];
    }
    
    @Override
    public String getColumnName(int i) {
        return COLUMN_NAMES[i];
    }

    @Override
    public Object getValueAt(int row, int column) {
        ModulePresentation mp = module.getModulePresentations().get(row);
        PreparationTime pt = lineItem.getPreparationTime(mp);
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
        LOGGER.log(Level.INFO, "row: {0} column: {1} with value: {2}", new Object[]{row, column, value});
        ModulePresentation mp = module.getModulePresentations().get(row);
        PreparationTime pt = lineItem.getPreparationTime(mp);
        switch (column) {
            case 1:
                pt.setWeekly((Float) value);
                break;
            case 2:
                pt.setNonWeekly((Float) value);
                break;
            case 3:
                pt.setSeniorRate((Integer) value);

        }
        fireTableCellUpdated(row, column);
    }
}
