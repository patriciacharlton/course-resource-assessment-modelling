package uk.ac.lkl.cram.ui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.SupportTime;

/**
 * $Date$
 * $Revision$
 * @author bernard
 */
public class TutorHoursTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(TutorHoursTableModel.class.getName());

    private final Module module;
    
    public TutorHoursTableModel(Module module) {
        super();
        this.module = module;
	module.addPropertyChangeListener(this);
	for (ModulePresentation modulePresentation : module.getModulePresentations()) {
	    modulePresentation.addPropertyChangeListener(this);
	}
    }

    @Override
    public int getRowCount() {
	//Add row for total
        return module.getLineItems().size() + 1;
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
	List<ModulePresentation> modulePresentations = module.getModulePresentations();
	if (row >= module.getLineItems().size()) {
	    //total row"
	    switch (column) {
		case 0:
		    return "Totals";
		//Preparation
		case 1: 
		    return module.getTotalPreparationHours(modulePresentations.get(0));
		case 2: 
		    return module.getTotalPreparationHours(modulePresentations.get(1));
		case 3: 
		    return module.getTotalPreparationHours(modulePresentations.get(2));
		//Support
		case 4:
		    return module.getTotalSupportHours(modulePresentations.get(0));
		case 5:
		    return module.getTotalSupportHours(modulePresentations.get(1));
		case 6:
		    return module.getTotalSupportHours(modulePresentations.get(2));
	    }
	}
	LineItem li = module.getLineItems().get(row);
	
	switch (column) {
	    case 0:
		return li.getName();
	    //Preparation
	    case 1:
		return li.getPreparationTime(modulePresentations.get(0)).getTotalHours(li);
	    case 2:
		return li.getPreparationTime(modulePresentations.get(1)).getTotalHours(li);
	    case 3:
		return li.getPreparationTime(modulePresentations.get(2)).getTotalHours(li);
	    //Support
	    case 4: {
		ModulePresentation mp = modulePresentations.get(0);
		SupportTime st = li.getSupportTime(mp);
		return li.getTotalHours(st, module, mp);
	    }
	    case 5: {
		ModulePresentation mp = modulePresentations.get(1);
		SupportTime st = li.getSupportTime(mp);
		return li.getTotalHours(st, module, mp);
	    }
	    case 6: {
		ModulePresentation mp = modulePresentations.get(2);
		SupportTime st = li.getSupportTime(mp);
		return li.getTotalHours(st, module, mp);
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
	    fireTableRowsInserted(ipce.getIndex(), ipce.getIndex());
	} else {
	    //LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
	    //TODO, catch all for any property
	    fireTableDataChanged();
	}
    }
}
