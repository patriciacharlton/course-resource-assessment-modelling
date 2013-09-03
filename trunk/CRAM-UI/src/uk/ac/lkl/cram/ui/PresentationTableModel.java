package uk.ac.lkl.cram.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 * $Date$
 * @author bernard
 */
public class PresentationTableModel extends AbstractTableModel implements PropertyChangeListener{
    private static final Logger LOGGER = Logger.getLogger(PresentationTableModel.class.getName());

    private static final String[] COLUMN_NAMES = {"<html>Run</html>", "<html>Number<br>of Students</html>", "<html>Student<br>Fee</html>", "<html>Junior Cost<br>per Day</html>", "<html>Senior Cost<br>per Day</html>"};
    private final Module module;
    
    PresentationTableModel(Module module) {
        super();
        this.module = module;
	module.addPropertyChangeListener(this);
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
	//TODO delete if no longer required
//        if (mp == null) {
//            mp = new ModulePresentation();
//            switch (row) {
//                case 0:
//                    module.setPresentationOne(mp);
//                    break;
//                case 1: 
//                    module.setPresentationTwo(mp);
//                    break;
//                case 2:
//                    module.setPresentationThree(mp);
//                    break;
//                default:
//                    throw new RuntimeException("invalid row: " + row);
//            }
//            
//        }
        String presentationName = "";
        switch (row) {
            case 0:
                presentationName = "Run 1";
                break;
            case 1:
                presentationName = "Run 2";
                break;
            case 2:
                presentationName = "Run 3";
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
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	//LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
	//TODO -- this is a catch all
	fireTableDataChanged();
    }
    
}
