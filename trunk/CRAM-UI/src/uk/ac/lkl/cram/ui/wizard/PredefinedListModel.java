package uk.ac.lkl.cram.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractListModel;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class PredefinedListModel extends AbstractListModel implements PropertyChangeListener {
    private FilteredList filteredList;
    
    public PredefinedListModel(FilteredList fList) {
	super();
	filteredList = fList;
	filteredList.addPropertyChangeListener(this);
    }

    @Override
    public int getSize() {
	return filteredList.size();
    }

    @Override
    public Object getElementAt(int i) {
	return filteredList.get(i);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	fireContentsChanged(this, 0, getSize() -1);
    }

}
