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
import java.util.List;
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
 * This class represents the model for the tutor cost table. It has a read-only
 * API. It listens to the underlying CRAM model for changes (in particular, the module and the 
 * module presentations and the support and preparation times) and when it receives them fires changes for any listeners
 * to this model. This model provides a view of the module in terms of the cost
 * per activity per presentation. Each row of the table represents 
 * an activity, and each column represents a presentation. Each presentation is
 * provided twice: once in terms of the cost to prepare for
 * an activity, and once in terms of cost to support an 
 * activity.
 * @see TutorCostPanel
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class TutorCostTableModel extends AbstractTableModel implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(TutorCostTableModel.class.getName());

    private final Module module;
    
    /**
     * Create a new table model from the module
     * @param module the CRAM module
     */
    public TutorCostTableModel(Module module) {
        super();
        this.module = module;
        addListeners();
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
	//Columns 1, 4 = Run1
	//Columns 2, 5 = Run2
	//Columns 3, 6 = Run3
	int run = ((i - 1) % 3) + 1;
	return " Run " + run;
    }

    @Override
    public Object getValueAt(int row, int column) {
	List<ModulePresentation> modulePresentations = module.getModulePresentations();
	if (row >= module.getLineItems().size()) {
	    //total row
	    switch (column) {
		case 0:
		    return "Totals";
		//Preparation
		case 1: 
                case 2:
                case 3:
		    return module.getTotalPreparationCost(modulePresentations.get(column - 1));
		//Support
		case 4:
                case 5:
                case 6:
		    return module.getTotalSupportCost(modulePresentations.get(column - 4));
	    }
	}
        //End of total row
        //Get the line item for the row
	LineItem li = module.getLineItems().get(row);
	
	switch (column) {
	    case 0:
		return li.getName();
	    //Preparation columns
	    case 1:
            case 2:
            case 3: {
                //Get the module presentation for this column
		ModulePresentation mp = modulePresentations.get(column - 1);
		//Get the preparation time for this line item
                PreparationTime pt = li.getPreparationTime(mp);
		//Get the total cost of preparation for this line item
                return pt.getTotalCost(module, mp, li);
	    }
	    //Support
	    case 4:
            case 5:
            case 6: {
                //Get the module presentation for this column
		ModulePresentation mp = modulePresentations.get(column - 4);
		//Get the supporr time for this line item
                SupportTime st = li.getSupportTime(mp);
                //Get the total cost of suport for this line item
		return st.getTotalCost(module, mp, li);
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
