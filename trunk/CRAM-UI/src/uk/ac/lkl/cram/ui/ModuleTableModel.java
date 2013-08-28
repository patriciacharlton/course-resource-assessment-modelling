package uk.ac.lkl.cram.ui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;

/**
 * $Date$
 * @author bernard
 */
public class ModuleTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(ModuleTableModel.class.getName());

    private static final String[] COLUMN_NAMES = {"<html>Activity</html>", "<html>Weekly<br>Hours</html>", "<html>Non-Weekly<br>Hours</html>", "<html>Total<br>Hours</html>"};
    private final Module module;
    
    public ModuleTableModel(Module module) {
        super();
        this.module = module;
	module.addPropertyChangeListener(this);
    }

    @Override
    public int getRowCount() {
        return module.getTLALineItems().size();
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
        TLALineItem li = module.getTLALineItems().get(row);
        switch (column) {
            case 0:
                return li.getActivity().getName();
            case 1:
                return li.getWeeklyLearnerHourCount();
            case 2:
                return li.getNonWeeklyLearnerHourCount();
            case 3:
                return li.getTotalLearnerHourCount(module);

        }
        return null;
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
        TLALineItem li = module.getTLALineItems().get(row);
        switch (column) {
            case 1:
                li.setWeeklyLearnerHourCount((Integer) value);
                break;
            case 2:
                li.setNonWeeklyLearnerHourCount((Integer) value);

        }
        fireTableCellUpdated(row, column);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	if (pce instanceof IndexedPropertyChangeEvent) {
	    IndexedPropertyChangeEvent ipce = (IndexedPropertyChangeEvent) pce;
	    fireTableRowsInserted(ipce.getIndex() - 1, ipce.getIndex());
	} else {
	    
	}
    }
}
