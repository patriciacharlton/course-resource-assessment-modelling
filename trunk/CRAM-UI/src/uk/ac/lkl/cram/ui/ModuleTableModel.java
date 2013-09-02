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
                return li.getWeeklyLearnerHourCount();
            case 2:
                return li.getNonWeeklyLearnerHourCount();
            case 3:
                return li.getTotalLearnerHourCount(module);

        }
        return null;
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