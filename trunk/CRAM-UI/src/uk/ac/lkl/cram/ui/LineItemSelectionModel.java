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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import uk.ac.lkl.cram.model.LineItem;

/**
 * This class provides a means of coordinating the row selection between multiple
 * tables in the module frame.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class LineItemSelectionModel {
    /**
     * The property that represented the selected line item (or row) in a table
     */
    public static final String PROP_SELECTEDLINEITEM = "selectedLineItem";
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private LineItem selectedLineItem;

    /**
     * Return the selected line item in the model
     * @return a line Item that is the shared selection
     */
    public LineItem getSelectedLineItem() {
        return selectedLineItem;
    }

    /**
     * Set the selected line item, and propagate the change
     * @param selectedLineItem the shared selected line item
     */
    public void setSelectedLineItem(LineItem selectedLineItem) {
        LineItem oldSelectedLineItem = this.selectedLineItem;
        this.selectedLineItem = selectedLineItem;
        propertyChangeSupport.firePropertyChange(PROP_SELECTEDLINEITEM, oldSelectedLineItem, selectedLineItem);
    }

    /**
     * Add a listener
     * @param listener the listener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener
     * @param listener the listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
