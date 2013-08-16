package uk.ac.lkl.cram.ui;

import java.util.List;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.PreparationTime;

/**
 *
 * @author bernard
 */
public class PreparationHoursTableModel extends HoursTableModel {
    
    PreparationHoursTableModel(Module module, ModulePresentation mp) {
        super(module, mp);
    }

    @Override
    public int getRowCount() {
	List<TLALineItem> lineItems = module.getTLALineItems();
        int rowCount = 0;
	for (TLALineItem lineItem : lineItems) {
	    if (lineItem.getPreparationTime(mp) != null) {
		rowCount++;
	    }
	}
	return rowCount;
    }

    @Override
    public Object getValueAt(int row, int column) {
	TLALineItem li = module.getTLALineItems().get(row);
	PreparationTime pt = li.getPreparationTime(mp);
	switch (column) {
	    case 0:
		return li.getActivity().getName();
	    case 1:
		return pt.getWeekly();
	    case 2:
		return pt.getNonWeekly();
	    case 3:
		return pt.getTotalHours(module);
	    case 4:
		return pt.getSeniorRate();
	    case 5:		
		return pt.getCost(module, mp);
	    
	}
	return null;
    }

    
}
