package uk.ac.lkl.cram.ui;

import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 * $Date$
 * @author bernard
 */
public class PresentationTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"\nRun", "Number\nof Students", "Fee", "Junior Cost\nper Day", "Senior Cost\nper Day"};
    private final Module module;
    
    PresentationTableModel(Module module) {
        super();
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
        if (mp == null) {
            mp = new ModulePresentation();
            switch (row) {
                case 0:
                    module.setPresentationOne(mp);
                    break;
                case 1: 
                    module.setPresentationTwo(mp);
                    break;
                case 2:
                    module.setPresentationThree(mp);
                    break;
                default:
                    throw new RuntimeException("invalid row: " + row);
            }
            
        }
        String presentationName = "";
        switch (row) {
            case 0:
                presentationName = "1st Run";
                break;
            case 1:
                presentationName = "2nd Run";
                break;
            case 2:
                presentationName = "Stable State";
                break;
            default:
                throw new RuntimeException("Invalid Row: " + row);
        }
        
        switch (column) {
            case 0:
                return presentationName;
            case 1: 
                return mp.getStudentCount();
            case 2:
                return mp.getFee();
            case 3:
                return mp.getJuniorCost();
            case 4: 
                return mp.getSeniorCost();
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
        switch (column) {
            case 1:
                mp.setStudentCount((Integer) value);
                break;
            case 2:
                mp.setFee((Integer) value);
                break;
            case 3:
                mp.setJuniorCost((Integer) value);
                break;
            case 4:
                mp.setSeniorCost((Integer) value);
                break;
            default:
                throw new RuntimeException("Invalid column: " + column);


        }
        fireTableCellUpdated(row, column);
    }
}
