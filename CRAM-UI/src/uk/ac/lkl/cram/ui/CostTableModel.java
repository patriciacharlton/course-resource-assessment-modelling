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
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This class represents the model for the summary cost table. It has a read-only
 * API. It listens to the underlying CRAM model for changes (in particular, the module and the 
 * module presentations and the support and preparation times) and when it receives them fires changes for any listeners
 * to this model. This model provides a view of the module in terms of the
 * total cost of the module.
 * @see CostPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class CostTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(CostTableModel.class.getName());

    private static final String[] COLUMN_NAMES = {"", "Run 1", "Run 2", "Run 3"};
    private static final String[] ROW_NAMES = {"Student Nos", "Prep. Hours", "Support Hours", "Total Hours", "Income", "Cost", "Difference"};
    private final Module module;
    
    /**
     * Create a new CostTableModel from the module
     * @param module the module from which to create the model
     */
    public CostTableModel(Module module) {
        super();
        this.module = module;
        addListeners();
    }

    @Override
    public int getRowCount() {
        return 7;
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
		    return (int) module.getTotalHours(mp);
		case 4:
		    return mp.getIncome();
		case 5:
		    return module.getTotalCost(mp);
		case 6:
		    return mp.getIncome() - module.getTotalCost(mp);
	    }
	}
	return null;
    }
    
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	//If there has been an addition or removal of line items in the module
	if (pce instanceof IndexedPropertyChangeEvent) {
	    IndexedPropertyChangeEvent ipce = (IndexedPropertyChangeEvent) pce;
	    //Let the ui know that the table has been updated
	    fireTableRowsInserted(ipce.getIndex(), ipce.getIndex());
	    if (pce.getOldValue() != null) {
		//This has been removed
		LineItem lineItem = (LineItem) pce.getOldValue();
		//Remove listeners from it
		if (lineItem instanceof TLALineItem) {
                    TLALineItem tlaLineItem = (TLALineItem) lineItem;
                    tlaLineItem.getActivity().removePropertyChangeListener(TLActivity.PROP_MAX_GROUP_SIZE, this);
                }
		for (ModulePresentation modulePresentation : module.getModulePresentations()) {
		    SupportTime st = lineItem.getSupportTime(modulePresentation);
		    st.removePropertyChangeListener(this);
		    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
		    pt.removePropertyChangeListener(this);
		}
	    }
	    if (pce.getNewValue() != null) {
		//This has been added
		LineItem lineItem = (LineItem) pce.getNewValue();
		//So add listeners to it 
                if (lineItem instanceof TLALineItem) {
                    TLALineItem tlaLineItem = (TLALineItem) lineItem;
                    tlaLineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_MAX_GROUP_SIZE, this);
                }
		for (ModulePresentation modulePresentation : module.getModulePresentations()) {
		    SupportTime st = lineItem.getSupportTime(modulePresentation);
		    st.addPropertyChangeListener(this);
		    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
		    pt.addPropertyChangeListener(this);
		}
	    }
	} else {
	    //LOGGER.info("event propertyName: " + pce.getPropertyName() + " newValue: " + pce.getNewValue());
	    //TODO, catch all for any property
	    fireTableDataChanged();
	}
    }

    private void addListeners() {
        //Listen to changes in the module
        module.addPropertyChangeListener(this);
        //Listen to changes in the max group size of the activity
        for (TLALineItem tLALineItem : module.getTLALineItems()) {
            tLALineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_MAX_GROUP_SIZE, this);
        }
        for (ModulePresentation modulePresentation : module.getModulePresentations()) {
            //Listen to changes in each module presentation
	    modulePresentation.addPropertyChangeListener(this);
	    for (LineItem lineItem : module.getLineItems()) {
		//Listen to changes in the support time for each line item, for each run
		SupportTime st = lineItem.getSupportTime(modulePresentation);
		st.addPropertyChangeListener(this);
		//Listen to changes in the preparation time for each line item, for each run
		PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
		pt.addPropertyChangeListener(this);
	    }
        }
    }
}
