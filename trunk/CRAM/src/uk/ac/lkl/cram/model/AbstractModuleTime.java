package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * An abstract class to represent the time spent on a TLA or a module item. 
 * This includes the amount of learner time per week, as well as teacher
 * time per week, the amount of
 * non-weekly time, and the percentage of that time spent by a higher paid
 * member of staff.
 * @see SupportTime
 * @see PreparationTime
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public abstract class AbstractModuleTime implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(AbstractModuleTime.class.getName());

    /**
     * Property to indicate when the amount of non-weekly time is updated.
     * @see AbstractModuleTime#setNonWeekly(float) 
     */
    public static final String PROP_NON_WEEKLY = "non_weekly";
    /**
     * Property to indicate when there is a change to the percentage of 
     * time spent by by a higher paid member of staff
     */
    public static final String PROP_SENIOR_RATE = "senior_rate";
    /**
     *
     */
    public static final String PROP_WEEKLY = "weekly";
    
    protected static final float HOURS_PER_DAY = 7.5F;

    /**
     * The number of non weekly hours
     * @see AbstractModuleTime#setNonWeekly(float) 
     * @see AbstractModuleTime#getNonWeekly()
     * @see AbstractModuleTime#PROP_NON_WEEKLY
     */
    protected float non_weekly;
    /**
     * The percentage of time spent by a higher paid member of staff. This 
     * should be a value where 0 <= x <= 100
     * @see AbstractModuleTime#setSeniorRate(int) 
     * @see AbstractModuleTime#getSeniorRate() 
     * @see AbstractModuleTime#PROP_SENIOR_RATE
     */
    protected int seniorRate; //Between 0 and 100
    /**
     * The number of hours per week
     * @see AbstractModuleTime#getWeekly() 
     * @see AbstractModuleTime#setWeekly(float) 
     * @see AbstractModuleTime#PROP_WEEKLY
     */
    protected float weekly;
    private final transient PropertyChangeSupport propertySupport;

    /**
     * Create a new instance, with all values set to zero
     */
    public AbstractModuleTime() {
	this(0f, 0f, 0);
    }

    /**
     * Create a new instance using the parameters provided
     * @param weekly the number of hours per week
     * @param non_weekly the number of non-weekly hours
     * @param seniorRate the percentage of time spent by a higher paid member of staff
     */
    public AbstractModuleTime(float weekly, float non_weekly, int seniorRate) {
	propertySupport = new PropertyChangeSupport(this);
	this.weekly = weekly;
	this.non_weekly = non_weekly;
	this.seniorRate = seniorRate;
    }

    /**
     * Return the number of non-weekly hours
     * @return the number of non-weekly hours
     */
    public float getNonWeekly() {
	return non_weekly;
    }

    /**
     * Return the percentage of time spent by a higher paid member of staff
     * @return the percentage of time spent by a higher paid member of staff
     */
    public int getSeniorRate() {
	return seniorRate;
    }
    
    /**
     * Return percentage of time spent by a lower paid member of staff
     * @return percentage of time spent by a lower paid member of staff
     */
    public int getJuniorRate() {
	return 100 - seniorRate;
    }

    /**
     * Return the number of weekly hours
     * @return the number of weekly hours
     */
    public float getWeekly() {
	return weekly;
    }

    /**
     * Set the number of non-weekly hours
     * @param i the number of non-weekly hours
     * @see AbstractModuleTime#PROP_NON_WEEKLY
     */
    @XmlAttribute
    public void setNonWeekly(float i) {
	float oldValue = non_weekly;
	non_weekly = i;
	propertySupport.firePropertyChange(PROP_NON_WEEKLY, oldValue, non_weekly);
    }

    /**
     * Set the percentage of time spent by a higher paid member of staff.
     * The value must be between 0 and 100. If not, this method will throw an IllegalArgumentException
     * @param i the percentage of time spent by a higher paid member of staff
     * @exception IllegalArgumentException if the value is out of range
     */
    @XmlAttribute
    public void setSeniorRate(int i) {
	if (i > 100) {
	    throw new IllegalArgumentException("Senior Rate should be between 0 and 100, not " + i);
	}
	float oldValue = seniorRate;
	seniorRate = i;
	propertySupport.firePropertyChange(PROP_SENIOR_RATE, oldValue, seniorRate);
    }
    
    /**
     * Set the percentage of time spent by a lower paid member of staff.
     * The value must be between 0 and 100. If not, this method will throw a RuntimeException
     * @param i the percentage of time spent by a lower paid member of staff
     */
    @XmlTransient
    public void setJuniorRate(int i) {
	setSeniorRate(100 - i);
    }

    /**
     * Set the number of hours per week
     * @param i the number of hours per week
     * @see AbstractModuleTime#PROP_WEEKLY
     */
    @XmlAttribute
    public void setWeekly(float i) {
	float oldValue = weekly;
	weekly = i;
	propertySupport.firePropertyChange(PROP_WEEKLY, oldValue, weekly);
    }
    
    /**
     * Return the total cost to Prepare for, or Support (depending on subclass)
     * the line item for the presentation.
     * @param m the module that describes the course
     * @param modulePresentation the presentation (run) of the teaching-learning activity or module activity in the module 
     * @param li the teaching-learning or module activity
     * @return the cost for presentation of the line item 
     */
    public abstract float getTotalCost(Module m, ModulePresentation modulePresentation, LineItem li);
    
    /**
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener) 
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener) 
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }
    
    /**
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(propertyName, listener);
    }
    
    /**
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Float.floatToIntBits(non_weekly);
	result = prime * result + seniorRate;
	result = prime * result + Float.floatToIntBits(weekly);
	return result;
    }

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
	if (this.seniorRate != other.seniorRate) {
	    return false;
	}
	if (Float.floatToIntBits(weekly) != Float.floatToIntBits(other.weekly)) {
	    return false;
	}
	return true;
    }

    
}
