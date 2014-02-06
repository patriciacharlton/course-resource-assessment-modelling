package uk.ac.lkl.cram.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 * $Date$
 * $Revision$
 * @author bernard
 */
@SuppressWarnings("serial")
public class CostTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(CostTableModel.class.getName());

    private static final String[] COLUMN_NAMES = {"", "Run 1", "Run 2", "Run 3"};
    private static final String[] ROW_NAMES = {"Student Nos", "Support Hours", "Prep. Hours", "Total Hours", "Income", "Cost", "Difference"};
    private final Module module;
    
    CostTableModel(Module module) {
        super();
        this.module = module;
	module.addPropertyChangeListener(this);
	for (ModulePresentation modulePresentation : module.getModulePresentations()) {
	    modulePresentation.addPropertyChangeListener(this);
	}
    }

    @Override
    public int getRowCount() {
        return 7;
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
	if (column == 0) {
	    return ROW_NAMES[row];
	} else {
	    ModulePresentation mp = module.getModulePresentations().get(column - 1);
	    switch (row) {
		case 0:
		    return mp.getTotalStudentCount();
		case 1:
		    return (int) module.getTotalSupportHours(mp);
		case 2:
		    return (int) module.getTotalPreparationHours(mp);
		case 3:
		    return (int) module.getTotalHours(mp);
		case 4:
		    return mp.getIncome();
		case 5:
		    return module.getTotalCost(mp);
		case 6:
		    return mp.getIncome() - module.getTotalCost(mp);
	    }
	}
	return null;
    }
    
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	//LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
	//TODO, catch all for any property
	fireTableDataChanged();
    }
}
