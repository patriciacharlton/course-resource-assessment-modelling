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
package uk.ac.lkl.cram.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractListModel;

/**
 * 
 * @version $Revision$
 * @param <E> the generic type of the contents of the filtered list
 * @see FilteredList
 * @author Bernard Horan 
 */
//$Date$
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class PredefinedListModel<E> extends AbstractListModel<E> implements PropertyChangeListener {
    private FilteredList<E> filteredList;
    
    /**
     * Create a new instance of the list model, using the filtered list as a parameter.
     * @param fList the filtered list to use as the list model.
     */
    public PredefinedListModel(FilteredList<E> fList) {
	super();
	filteredList = fList;
	addListener();
    }

    @Override
    public int getSize() {
	return filteredList.size();
    }

    @Override
    public E getElementAt(int i) {
	return filteredList.get(i);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	fireContentsChanged(this, 0, getSize() -1);
    }

    private void addListener() {
        filteredList.addPropertyChangeListener(this);
    }

}
