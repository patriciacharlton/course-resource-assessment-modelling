package uk.ac.lkl.cram.ui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.SupportTime;

/**
 * $Date$
 * @author bernard
 */
public class TutorHoursTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(TutorHoursTableModel.class.getName());

    private final Module module;
    
    public TutorHoursTableModel(Module module) {
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
        return 7;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
	return getValueAt(0, columnIndex).getClass();
    }
    
    @Override
    public String getColumnName(int i) {
        if (i == 0) {
	    return "Activity";
	}
	//1, 4 = Run1
	//2, 5 = Run2
	//3, 6 = Run3
	int run = ((i - 1) % 3) + 1;
	return " Run " + run;
    }

    @Override
    public Object getValueAt(int row, int column) {
        TLALineItem li = module.getTLALineItems().get(row);
	List<ModulePresentation> modulePresentations = module.getModulePresentations();
	switch (column) {
            case 0:
                return li.getActivity().getName();
	    //Preparation
            case 1: 
		return li.getPreparationTime(modulePresentations.get(0)).getTotalHours(module);
            case 2:
                return li.getPreparationTime(modulePresentations.get(1)).getTotalHours(module);
            case 3:
                return li.getPreparationTime(modulePresentations.get(2)).getTotalHours(module);
	    //Support
	    case 4: {
		ModulePresentation mp = modulePresentations.get(0);
		SupportTime st = li.getSupportTime(mp);
		return st.getTotalHours(module, mp, li);
	    }
	    case 5: {
		ModulePresentation mp = modulePresentations.get(1);
		SupportTime st = li.getSupportTime(mp);
		return st.getTotalHours(module, mp, li);
	    }
	    case 6:
		{
		ModulePresentation mp = modulePresentations.get(2);
		SupportTime st = li.getSupportTime(mp);
		return st.getTotalHours(module, mp, li);
	    }
        }
        return null;
    }
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	if (pce instanceof IndexedPropertyChangeEvent) {
	    IndexedPropertyChangeEvent ipce = (IndexedPropertyChangeEvent) pce;
	    fireTableRowsInserted(ipce.getIndex() - 1, ipce.getIndex());
	} else {
	    
	}
    }
}
