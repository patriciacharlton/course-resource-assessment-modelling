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
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;

/**
 * $Date$
 * @author bernard
 */
public class TutorCostTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(TutorCostTableModel.class.getName());

    private final Module module;
    
    public TutorCostTableModel(Module module) {
        super();
        this.module = module;
	module.addPropertyChangeListener(this);
    }

    @Override
    public int getRowCount() {
        return module.getLineItems().size();
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
	LineItem li = module.getLineItems().get(row);
	List<ModulePresentation> modulePresentations = module.getModulePresentations();
	switch (column) {
	    case 0:
		return li.getName();
	    //Preparation
	    case 1: {
		ModulePresentation mp = modulePresentations.get(0);
		PreparationTime pt = li.getPreparationTime(mp);
		return pt.getCost(module, mp);
	    }
	    case 2: {
		ModulePresentation mp = modulePresentations.get(1);
		PreparationTime pt = li.getPreparationTime(mp);
		return pt.getCost(module, mp);
	    }
	    case 3: {
		ModulePresentation mp = modulePresentations.get(2);
		PreparationTime pt = li.getPreparationTime(mp);
		return pt.getCost(module, mp);
	    }	    
	    //Support
	    case 4: {
		ModulePresentation mp = modulePresentations.get(0);
		SupportTime st = li.getSupportTime(mp);
		return li.getCost(st, module, mp);
	    }
	    case 5: {
		ModulePresentation mp = modulePresentations.get(1);
		SupportTime st = li.getSupportTime(mp);
		return li.getCost(st, module, mp);
	    }
	    case 6: {
		ModulePresentation mp = modulePresentations.get(2);
		SupportTime st = li.getSupportTime(mp);
		return li.getCost(st, module, mp);
	    }
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
