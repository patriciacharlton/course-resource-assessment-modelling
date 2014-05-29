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
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 * This class represents a model for the summary table used by the Report. It has a read-only
 * API. This model provides a view of the module in terms of the
 * total cost of the module.
 * @see Report
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class SummaryReportModel extends AbstractTableModel {
    private static final Logger LOGGER = Logger.getLogger(SummaryReportModel.class.getName());

    private static final String[] COLUMN_NAMES = {"", "Run 1", "Run 2", "Run 3"};
    private static final String[] ROW_NAMES = {"Student Nos", "Prep. Hours", "Support Hours", "Support Hours per Student", "Total Hours", "Income", "Cost", "Support Cost per Student","Difference"};
    private final Module module;
    
    /**
     * Create a new SummaryReportModel from the module
     * @param module the module from which to create the model
     */
    public SummaryReportModel(Module module) {
        super();
        this.module = module;
    }

    @Override
    public int getRowCount() {
        return ROW_NAMES.length;
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
            //First column has labels for the rows
	    return ROW_NAMES[row];
	} else {
	    ModulePresentation mp = module.getModulePresentations().get(column - 1);
	    switch (row) {
		case 0:
		    return mp.getTotalStudentCount();
		case 1:
		    return (int) module.getTotalPreparationHours(mp);
		case 2:
		    return (int) module.getTotalSupportHours(mp);
		case 3:
		    float totalSupportHours = module.getTotalSupportHours(mp);
		    float hoursPerStudent = totalSupportHours / mp.getTotalStudentCount();
		    return (int) hoursPerStudent;
		case 4:
		    return (int) module.getTotalHours(mp);
		case 5:
		    return mp.getIncome();
		case 6:
		    return module.getTotalCost(mp);
		case 7:
		    float totalSupportCost = module.getTotalSupportCost(mp);
		    float costPerStudent = totalSupportCost / mp.getTotalStudentCount();
		    return (int) costPerStudent;
		case 8:
		    return mp.getIncome() - module.getTotalCost(mp);
	    }
	}
	return null;
    }
       
}
