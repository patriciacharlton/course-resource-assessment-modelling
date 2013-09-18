package uk.ac.lkl.cram.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractListModel;

/**
 * $Date$
 * $Revision$
 * @param <E> 
 * @author Bernard Horan 
 */
@SuppressWarnings("serial")
public class PredefinedListModel<E> extends AbstractListModel implements PropertyChangeListener {
    private FilteredList<E> filteredList;
    
    public PredefinedListModel(FilteredList<E> fList) {
	super();
	filteredList = fList;
	filteredList.addPropertyChangeListener(this);
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

}
