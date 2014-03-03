package uk.ac.lkl.cram.ui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;

/**
 * $Date$
 * $Revision$
 * @author bernard
 */
@SuppressWarnings("serial")
public class ModuleTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(ModuleTableModel.class.getName());

    private static final String[] COLUMN_NAMES = {"<html>Activity</html>", "<html>Number of<br>Weeks</html>", "<html>Weekly<br>Learner Hours</html>", "<html>Non-Weekly<br>Learner Hours</html>", "<html>Total<br>Learner Hours</html>"};
    private final Module module;
    private final boolean includeSelfRegulated;
    
    public ModuleTableModel(Module module, boolean includeSelfRegulated) {
        super();
        this.module = module;
        this.includeSelfRegulated = includeSelfRegulated;
	module.addPropertyChangeListener(this);
    }

    @Override
    public int getRowCount() {
        int rowCount = module.getTLALineItems().size();
        if (includeSelfRegulated) {
            //Also need to include a 'self-regulated learning row
            return rowCount + 1;
        } else {
            return rowCount;
        }
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
        if (includeSelfRegulated) {
            if (row >= module.getTLALineItems().size()) {
                //self regulated row"
                switch (column) {
                    case 0:
                        return "Self-regulated Learning";
                    case 1:
		    case 2:
                        return 0;
                    default:
                        return module.getSelfRegulatedLearningHourCount();
                }
            }
        }
        TLALineItem li = module.getTLALineItems().get(row);
        switch (column) {
            case 0:
                return li.getActivity().getName();
            case 1:
                return li.getWeekCount(module);
	    case 2:
		return li.getWeeklyLearnerHourCount();
            case 3:
                return li.getNonWeeklyLearnerHourCount();
            case 4:
                return li.getTotalLearnerHourCount(module);

        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	if (pce instanceof IndexedPropertyChangeEvent) {
	    IndexedPropertyChangeEvent ipce = (IndexedPropertyChangeEvent) pce;
	    fireTableRowsInserted(ipce.getIndex(), ipce.getIndex());
	} else {
	    //LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
	    //TODO, catch all for any property
	    fireTableDataChanged();
	}
    }
}
