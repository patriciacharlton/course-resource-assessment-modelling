package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class AbstractModuleTime implements Serializable {
    public static final String PROP_NON_WEEKLY = "non_weekly";
    public static final String PROP_SENIOR_RATE = "senior_rate";
    public static final String PROP_WEEKLY = "weekly";
    
    protected static final float HOURS_PER_DAY = 7.5F;
    private static final long serialVersionUID = 1L;
    protected float non_weekly;
    protected float seniorRate; //Between 0 and 1
    protected float weekly;
    private PropertyChangeSupport propertySupport;

    public AbstractModuleTime() {
	this(0f, 0f, 0f);
    }

    public AbstractModuleTime(float weekly, float non_weekly, float seniorRate) {
	propertySupport = new PropertyChangeSupport(this);
	this.weekly = weekly;
	this.non_weekly = non_weekly;
	this.seniorRate = seniorRate;
    }

    public float getNonWeekly() {
	return non_weekly;
    }

    public float getSeniorRate() {
	return seniorRate;
    }

    public float getWeekly() {
	return weekly;
    }

    @XmlAttribute
    public void setNonWeekly(float i) {
	float oldValue = non_weekly;
	non_weekly = i;
	propertySupport.firePropertyChange(PROP_NON_WEEKLY, oldValue, non_weekly);
    }

    @XmlAttribute
    public void setSeniorRate(float i) {
	if (i > 1) {
	    throw new RuntimeException("Senior Rate should be between 0 and 1, not " + i);
	}
	float oldValue = seniorRate;
	seniorRate = i;
	propertySupport.firePropertyChange(PROP_SENIOR_RATE, oldValue, seniorRate);
    }

    @XmlAttribute
    public void setWeekly(float i) {
	float oldValue = weekly;
	weekly = i;
	propertySupport.firePropertyChange(PROP_WEEKLY, oldValue, weekly);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Float.floatToIntBits(non_weekly);
	result = prime * result + Float.floatToIntBits(seniorRate);
	result = prime * result + Float.floatToIntBits(weekly);
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof AbstractModuleTime)) {
	    return false;
	}
	AbstractModuleTime other = (AbstractModuleTime) obj;
	if (Float.floatToIntBits(non_weekly) != Float
		.floatToIntBits(other.non_weekly)) {
	    return false;
	}
	if (Float.floatToIntBits(seniorRate) != Float
		.floatToIntBits(other.seniorRate)) {
	    return false;
	}
	if (Float.floatToIntBits(weekly) != Float.floatToIntBits(other.weekly)) {
	    return false;
	}
	return true;
    }
}
