package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAttribute;
import uk.ac.lkl.cram.model.calculations.Calculable;

/**
 * This class represents a run of a module, and represents the 
 * number of home and overseas students, the day rates for higher and
 * lower paid members of staff.
 * There should only ever be three instantiations of this class for a module, 
 * constrained by the enumeration Run.
 * @see Run
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class ModulePresentation implements Serializable, Calculable {
    private static final Logger LOGGER = Logger.getLogger(ModulePresentation.class.getName());

    /**
     * Property to indicate when the day rate for lower cost members
     * of staff has been updated.
     * @see ModulePresentation#setJuniorCost(int) 
     */
    public static final String PROP_JUNIOR_COST = "junior_cost";
    /**
     * Property to indicate when the day rate for higher cost members
     * of staff has been updated.
     * @see ModulePresentation#setSeniorCost(int) 
     */
    public static final String PROP_SENIOR_COST = "senior_cost";
    /**
     * Property to indicate when the fee for home students
     * has been updated.
     * @see ModulePresentation#setHomeFee(int)  
     */
    public static final String PROP_HOME_FEE = "home_fee";
    /**
     * Property to indicate when the number of home students
     * has been updated
     * @see ModulePresentation#setHomeStudentCount(int) 
     */
    public static final String PROP_HOME_STUDENT_COUNT = "home_student_count";
    /**
     * Property to indicate when the fee for overseas students
     * has been updated.OverseasHomeFee(int)  
     */
    public static final String PROP_OVERSEAS_FEE = "overseas_fee";
    /**
     * Property to indicate when the number of overseas students
     * has been updated
     * @see ModulePresentation#setOverseasStudentCount(int) 
     */
    public static final String PROP_OVERSEAS_STUDENT_COUNT = "overseas_student_count";
    
    private int homeStudentCount;
    private int overseasFee;
    private int homeFee;
    private int overseasStudentCount;   
    private int juniorCostPerDay;
    private int seniorCostPerDay;
    
    @XmlAttribute
    private Run run;
    private final transient PropertyChangeSupport propertySupport;

    /**
     * Create a new ModulePresentation from the supplied parameters
     * @param aRun the Run for this ModulePresentation (either First, Second or Third)
     * @param homeStudentCount the number of home students
     * @param homeFee the teaching income for home students
     * @param overseasStudentCount the number of overseas students
     * @param overseasFee the teaching income for overseas students
     * @param juniorCostPerDay the day rate for a lower paid member of staff
     * @param seniorCostPerDay the day rate for a highe rpaid memeber of staff
     */
    public ModulePresentation(Run aRun, int homeStudentCount, int homeFee, int overseasStudentCount, int overseasFee, int juniorCostPerDay, int seniorCostPerDay) {
	this(aRun);
	this.homeStudentCount = homeStudentCount;
        this.overseasStudentCount = overseasStudentCount;
	this.homeFee = homeFee;
        this.overseasFee = overseasFee;
	this.juniorCostPerDay = juniorCostPerDay;
	this.seniorCostPerDay = seniorCostPerDay;
    }

    /**
     * Create a new ModulePresentation from just the supplied Run
     * @param aRun the Run for this ModulePresentation (either First, Second or Third)
     */
    public ModulePresentation(Run aRun) {
	this();
        run = aRun;
    }
    
    /**
     * Create a new module presentation for the first run
     */
    public ModulePresentation() {
        super();
        run = Run.FIRST;
	propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * return the day rate for a higher paid member of staff for this run
     * @return the cost of a higher rate member of staff 
     */
    public int getSeniorCost() {
	return seniorCostPerDay;
    }

    /**
     * return the day rate for a lower paid member of staff for this run
     * @return the cost of a lower rate member of staff 
     */
    public int getJuniorCost() {
	return juniorCostPerDay;
    }

    /**
     * Return the number of home students for this run
     * @return the number of home students
     */
    public int getHomeStudentCount() {
	return homeStudentCount;
    }

    /**
     * return the number of overseas students for this run
     * @return the number of overseas students
     */
    public int getOverseasStudentCount() {
        return overseasStudentCount;
    }
    
    /**
     * Return the total number of students for this run. (The sum of home students
     * and overseas students)
     * @return the total number of students for this run
     */
    public int getTotalStudentCount() {
        return homeStudentCount + overseasStudentCount;
    }

    /**
     * Return the total student income for this run.
     * (The sum of (the product of the number of home students * the home income) 
     * plus (the product of the number of overseas students * the overseas income)
     * @return the income for this run
     */
    public int getIncome() {
	return (homeFee * homeStudentCount) + (overseasFee * overseasStudentCount);
    }

    /**
     * Set the number of home students
     * @param i the j number of home students
     * @see ModulePresentation#PROP_HOME_STUDENT_COUNT
     */
    @XmlAttribute
    public void setHomeStudentCount(int i) {
	int oldValue = homeStudentCount;
	homeStudentCount = i;
	propertySupport.firePropertyChange(PROP_HOME_STUDENT_COUNT, oldValue, homeStudentCount);
    }

    /**
     * Set the number of overseas students
     * @param overseasStudentCount the number of overseas students
     * @see ModulePresentation#PROP_OVERSEAS_STUDENT_COUNT
     */
    @XmlAttribute
    public void setOverseasStudentCount(int overseasStudentCount) {
        int oldOverseasStudentCount = this.overseasStudentCount;
        this.overseasStudentCount = overseasStudentCount;
        propertySupport.firePropertyChange(PROP_OVERSEAS_STUDENT_COUNT, oldOverseasStudentCount, overseasStudentCount);
    }
    
    /**
     * Set the income for a home student
     * @param i the income for a home student
     * @see ModulePresentation#PROP_HOME_FEE
     */
    @XmlAttribute
    public void setHomeFee(int i) {
	int oldValue = homeFee;
	homeFee = i;
	propertySupport.firePropertyChange(PROP_HOME_FEE, oldValue, homeFee);
    }

    /**
     * Return the income for a home student
     * @return the income for a home student
     */
    public int getHomeFee() {
	return homeFee;
    }
    
    /**
     * Set the income for an overseas student
     * @param overseasFee the income from an overseas student
     * @see ModulePresentation#PROP_OVERSEAS_FEE
     */
    @XmlAttribute
    public void setOverseasFee(int overseasFee) {
        int oldOverseasFee = this.overseasFee;
        this.overseasFee = overseasFee;
        propertySupport.firePropertyChange(PROP_OVERSEAS_FEE, oldOverseasFee, overseasFee);
    }
    
    /**
     * Return the income for an overseas student
     * @return the income for an overseas student
     */
    public int getOverseasFee() {
        return overseasFee;
    }

    /**
     * Set the day rate for a lower paid member of staff
     * @param i day rate for a lower paid member of staff
     * @see ModulePresentation#PROP_JUNIOR_COST
     */
    @XmlAttribute
    public void setJuniorCost(int i) {
	int oldValue = juniorCostPerDay;
	juniorCostPerDay = i;
	propertySupport.firePropertyChange(PROP_JUNIOR_COST, oldValue, juniorCostPerDay);
	
    }

    /**
     * Set the day rate for a higher paid member of staff
     * @param i day rate for a lower paid member of staff
     * @see ModulePresentation#PROP_SENIOR_COST
     */
    @XmlAttribute
    public void setSeniorCost(int i) {
	int oldValue = seniorCostPerDay;
	seniorCostPerDay = i;
	propertySupport.firePropertyChange(PROP_SENIOR_COST, oldValue, seniorCostPerDay);
    }

    /**
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener) 
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener) 
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }
    
    /*
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
    
    @Override
    public String toString() {
	return "ModulePresentation[" + run.name() + "]";
    }
   
    /**
     * This enumeration represents the three runs of the activities in the module.
     */
    @SuppressWarnings("PublicInnerClass")
    public enum Run {
	/**
	 * The First run of a module presentation
	 */
	FIRST,
	/**
	 * The Second run of a module presentation  
	 */
	SECOND,
	/**
	 * The  Third run of a module presentation
	 */
	THIRD};

    
}
