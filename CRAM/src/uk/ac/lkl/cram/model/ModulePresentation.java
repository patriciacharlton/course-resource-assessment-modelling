package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

import uk.ac.lkl.cram.model.calculations.Calculable;

/**
 * $Date$
 * @author Bernard Horan
 */
public class ModulePresentation implements Serializable, Calculable {

    private static final long serialVersionUID = 1L;
    public static final String PROP_JUNIOR_COST = "junior_cost";
    public static final String PROP_SENIOR_COST = "senior_cost";
    public static final String PROP_FEE = "fee";
    public static final String PROP_STUDENT_COUNT = "student_count";
    
    private int studentCount;
    private int fee;
    private int juniorCostPerDay;
    private int seniorCostPerDay;
    private PropertyChangeSupport propertySupport;

    public ModulePresentation(int studentCount, int fee, int juniorCostPerDay, int seniorCostPerDay) {
	this();
	this.studentCount = studentCount;
	this.fee = fee;
	this.juniorCostPerDay = juniorCostPerDay;
	this.seniorCostPerDay = seniorCostPerDay;
    }

    public ModulePresentation() {
	super();
	propertySupport = new PropertyChangeSupport(this);
    }

    public int getSeniorCost() {
	return seniorCostPerDay;
    }

    public int getJuniorCost() {
	return juniorCostPerDay;
    }

    public int getStudentCount() {
	return studentCount;
    }

    int getNumberOfIndividuals_Groups(Module m, TLALineItem lineItem) {
	float i = ((float) studentCount / (float) lineItem.getMaximumGroupSize(m, this));
	return (int) (i + 0.99);
    }

    int getNumberOfIndividuals_Groups(Module module) {
	float i = ((float) studentCount / (float) module.getTutorGroupSize());
	return (int) (i + 0.99);
    }

    public int getIncome() {
	return fee * studentCount;
    }

    @XmlAttribute
    public void setStudentCount(int i) {
	int oldValue = studentCount;
	studentCount = i;
	propertySupport.firePropertyChange(PROP_STUDENT_COUNT, oldValue, studentCount);
    }

    @XmlAttribute
    public void setFee(int i) {
	int oldValue = fee;
	fee = i;
	propertySupport.firePropertyChange(PROP_FEE, oldValue, fee);
    }

    public int getFee() {
	return fee;
    }

    @XmlAttribute
    public void setJuniorCost(int i) {
	int oldValue = juniorCostPerDay;
	juniorCostPerDay = i;
	propertySupport.firePropertyChange(PROP_JUNIOR_COST, oldValue, juniorCostPerDay);
	
    }

    @XmlAttribute
    public void setSeniorCost(int i) {
	int oldValue = seniorCostPerDay;
	seniorCostPerDay = i;
	propertySupport.firePropertyChange(PROP_SENIOR_COST, oldValue, seniorCostPerDay);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 37 * hash + this.studentCount;
	hash = 37 * hash + this.fee;
	hash = 37 * hash + this.juniorCostPerDay;
	hash = 37 * hash + this.seniorCostPerDay;
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final ModulePresentation other = (ModulePresentation) obj;
	if (this.studentCount != other.studentCount) {
	    return false;
	}
	if (this.fee != other.fee) {
	    return false;
	}
	if (this.juniorCostPerDay != other.juniorCostPerDay) {
	    return false;
	}
	if (this.seniorCostPerDay != other.seniorCostPerDay) {
	    return false;
	}
	return true;
    }

    
}
