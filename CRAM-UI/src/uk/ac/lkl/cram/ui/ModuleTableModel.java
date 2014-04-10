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

package uk.ac.lkl.cram.ui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;

/**
 * This class represents the model for the table in the LineItemPanel and 
 * the table in the ListOfTLAWizardPanel. It has a read-only API. It listens to 
 * the underlying CRAM model for changes (in particular to changes in the module
 * and its lineitems). This model provides view of the module in terms of the 
 * number of hours that students are expected to spend per activity, and the number
 * of weeks they are expected to undertake activities.
 * @see LineItemPanel
 * @see ListOfTLAWizardPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class ModuleTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(ModuleTableModel.class.getName());

    private static final String[] COLUMN_NAMES = {"<html>Activity</html>", "<html>Number of<br>Weeks</html>", "<html>Weekly<br>Learner Hours</html>", "<html>Non-Weekly<br>Learner Hours</html>", "<html>Total<br>Learner Hours</html>"};
    private final Module module;
    private final boolean includeSelfRegulated;
    
    /**
     * Create a new table model from the CRAM module
     * @param module the CRAM module
     * @param includeSelfRegulated if true, include a fake row that describes the learners' unregulated activities
     */
    public ModuleTableModel(Module module, boolean includeSelfRegulated) {
        super();
        this.module = module;
        this.includeSelfRegulated = includeSelfRegulated;
	addListeners();
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
                //self regulated row
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
	    if (pce.getOldValue() != null) {
		//This has been removed
		LineItem lineItem = (LineItem) pce.getOldValue();
		//Remove listener from it
		lineItem.removePropertyChangeListener(this);
	    }
	    if (pce.getNewValue() != null) {
		//This has been added
		LineItem lineItem = (LineItem) pce.getNewValue();
		//So add a listener to it 
		lineItem.addPropertyChangeListener(this);
	    }
	} else {
	    //LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
	    //TODO, catch all for any property
	    fireTableDataChanged();
	}
    }

    private void addListeners() {
	module.addPropertyChangeListener(this);
	for (LineItem lineItem : module.getLineItems()) {
	    lineItem.addPropertyChangeListener(this);
	}
    }
}
