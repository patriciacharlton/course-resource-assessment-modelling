/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.lkl.cram.ui.report;

import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;

/**
 * This class represents a model for use by the Report. It has a read-only API.
 * This model provides view of the module in terms of the 
 * number of hours that students are expected to spend per activity, and the number
 * of weeks they are expected to undertake activities.
 * @see Report
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class ModuleReportModel extends AbstractTableModel {
    private static final Logger LOGGER = Logger.getLogger(ModuleReportModel.class.getName());

    private static final String[] COLUMN_NAMES = {"Activity", "Group Size", "Number of Weeks", "Weekly Learner Hours", "Non-Weekly Learner Hours", "Total Learner Hours"};
    private final Module module;
    
    /**
     * Create a new table model from the CRAM module
     * @param module the CRAM module
     */
    public ModuleReportModel(Module module) {
        super();
        this.module = module;
    }

    @Override
    public int getRowCount() {
        int rowCount = module.getTLALineItems().size();
        //Also need to include a 'self-regulated learning row and a totals row
	return rowCount + 2;       
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
        if (row == module.getTLALineItems().size()) {
	    //self regulated row
	    switch (column) {
		case 0:
		    return "Self-regulated Learning";
		case 1:
		    return 1;
		case 2:
		case 3:
		case 4:
		    return 0;
		default:
		    return module.getSelfRegulatedLearningHourCount();
	    }
	}
	
	if (row == module.getTLALineItems().size() + 1) {
	    //Totals row
	    switch (column) {
		case 0:
		    return "Totals";
		case 1:
		case 2:
		case 3:
		case 4:
		    return 0;
		default:
		    return module.getTotalCreditHourCount();
	    }
	}
        
        TLALineItem li = module.getTLALineItems().get(row);
        switch (column) {
            case 0:
                return li.getActivity().getName();
	    case 1:
		return li.getMaximumGroupSizeForPresentation(module.getModulePresentations().get(0));
            case 2:
                return li.getWeekCount(module);
	    case 3:
		return li.getWeeklyLearnerHourCount();
            case 4:
                return li.getNonWeeklyLearnerHourCount();
            case 5:
                return li.getTotalLearnerHourCount(module);

        }
        return null;
    }
    
}
