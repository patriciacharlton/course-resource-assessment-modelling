package uk.ac.lkl.cram.model.calculations;

import java.beans.PropertyChangeListener;

/**
 *
 * @author Bernard Horan
 */
public interface Calculable {
    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
