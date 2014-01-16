package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAttribute;
import uk.ac.lkl.cram.model.calculations.Calculable;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class ModulePresentation implements Serializable, Calculable {
    private static final Logger LOGGER = Logger.getLogger(ModulePresentation.class.getName());

    private static final long serialVersionUID = 1L;
    public static final String PROP_JUNIOR_COST = "junior_cost";
    public static final String PROP_SENIOR_COST = "senior_cost";
    public static final String PROP_FEE = "fee";
    public static final String PROP_STUDENT_COUNT = "student_count";
    
    private int studentCount;
    private int fee;
    private int juniorCostPerDay;
    private int seniorCostPerDay;
    @XmlAttribute
    private Run run;
    private PropertyChangeSupport propertySupport;

    public ModulePresentation(Run aRun, int studentCount, int fee, int juniorCostPerDay, int seniorCostPerDay) {
	this(aRun);
	this.studentCount = studentCount;
	this.fee = fee;
	this.juniorCostPerDay = juniorCostPerDay;
	this.seniorCostPerDay = seniorCostPerDay;
    }

    public ModulePresentation(Run aRun) {
	this();
        run = aRun;
    }
    
    public ModulePresentation() {
        super();
        run = Run.FIRST;
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

    int getNumberOfIndividuals_Groups(TLALineItem lineItem) {
	float i = ((float) studentCount / (float) lineItem.getMaximumGroupSizeForPresentation(this));
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
        int hash = 5;
        hash = 89 * hash + (this.run != null ? this.run.hashCode() : 0);
        return hash;
    }

    @Override
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModulePresentation other = (ModulePresentation) obj;
        if (this.run != other.run) {
            return false;
        }
        return true;
    }

    
    
    
    @SuppressWarnings("PublicInnerClass")
    public enum Run {FIRST, SECOND, THIRD};

    
}
