package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

import uk.ac.lkl.cram.model.calculations.Calculable;

public class ModulePresentation implements Serializable, Calculable {

    private static final long serialVersionUID = 1L;
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
	studentCount = i;
    }

    @XmlAttribute
    public void setFee(int i) {
	fee = i;
    }

    public int getFee() {
	return fee;
    }

    @XmlAttribute
    public void setJuniorCost(int i) {
	juniorCostPerDay = i;
    }

    @XmlAttribute
    public void setSeniorCost(int i) {
	seniorCostPerDay = i;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
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
