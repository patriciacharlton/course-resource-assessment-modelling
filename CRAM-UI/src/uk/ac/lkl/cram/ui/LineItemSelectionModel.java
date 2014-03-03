package uk.ac.lkl.cram.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import uk.ac.lkl.cram.model.LineItem;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("ClassWithoutLogger")
public class LineItemSelectionModel {
    public static final String PROP_SELECTEDLINEITEM = "selectedLineItem";
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private LineItem selectedLineItem;

    public LineItem getSelectedLineItem() {
        return selectedLineItem;
    }

    public void setSelectedLineItem(LineItem selectedLineItem) {
        LineItem oldSelectedLineItem = this.selectedLineItem;
        this.selectedLineItem = selectedLineItem;
        propertyChangeSupport.firePropertyChange(PROP_SELECTEDLINEITEM, oldSelectedLineItem, selectedLineItem);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
