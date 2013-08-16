package uk.ac.lkl.cram.ui;

import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.SupportTime;

/**
 *
 * @author bernard
 */
public class SupportHoursTableModel extends HoursTableModel {

    
    SupportHoursTableModel(Module module, ModulePresentation mp) {
        super(module, mp);
    }

    @Override
    public int getRowCount() {
	return module.getLineItems().size();
    }

    
    @Override
    public Object getValueAt(int row, int column) {
	LineItem li = module.getLineItems().get(row);
	SupportTime st = li.getSupportTime(mp);
	switch (column) {
	    case 0:
		return li.getName();
	    case 1:
		return st.getWeekly();
	    case 2:
		return st.getNonWeekly();
	    case 3: 
		return li.getTotalHours(st, module, mp);		
	    case 4:
		return st.getSeniorRate();
	    case 5:		
		return li.getCost(st, module, mp);
	}
	return null;
    }
    
}
