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
    public static final String PROP_HOME_FEE = "home_fee";
    public static final String PROP_HOME_STUDENT_COUNT = "home_student_count";
    public static final String PROP_OVERSEAS_FEE = "overseas_fee";
    public static final String PROP_OVERSEAS_STUDENT_COUNT = "overseas_student_count";
    
    private int homeStudentCount;
    private int overseasFee;
    private int homeFee;
    private int overseasStudentCount;   
    private int juniorCostPerDay;
    private int seniorCostPerDay;
    
    @XmlAttribute
    private Run run;
    private PropertyChangeSupport propertySupport;

    public ModulePresentation(Run aRun, int homeStudentCount, int homeFee, int overseasStudentCount, int overseasFee, int juniorCostPerDay, int seniorCostPerDay) {
	this(aRun);
	this.homeStudentCount = homeStudentCount;
        this.overseasStudentCount = overseasStudentCount;
	this.homeFee = homeFee;
        this.overseasFee = overseasFee;
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

    public int getHomeStudentCount() {
	return homeStudentCount;
    }

    public int getOverseasStudentCount() {
        return overseasStudentCount;
    }
    
    public int getTotalStudentCount() {
        return homeStudentCount + overseasStudentCount;
    }

    public int getIncome() {
	return (homeFee * homeStudentCount) + (overseasFee * overseasStudentCount);
    }

    @XmlAttribute
    public void setHomeStudentCount(int i) {
	int oldValue = homeStudentCount;
	homeStudentCount = i;
	propertySupport.firePropertyChange(PROP_HOME_STUDENT_COUNT, oldValue, homeStudentCount);
    }

    @XmlAttribute
    public void setOverseasStudentCount(int overseasStudentCount) {
        int oldOverseasStudentCount = this.overseasStudentCount;
        this.overseasStudentCount = overseasStudentCount;
        propertySupport.firePropertyChange(PROP_OVERSEAS_STUDENT_COUNT, oldOverseasStudentCount, overseasStudentCount);
    }
    
    @XmlAttribute
    public void setHomeFee(int i) {
	int oldValue = homeFee;
	homeFee = i;
	propertySupport.firePropertyChange(PROP_HOME_FEE, oldValue, homeFee);
    }

    public int getHomeFee() {
	return homeFee;
    }
    
    @XmlAttribute
    public void setOverseasFee(int overseasFee) {
        int oldOverseasFee = this.overseasFee;
        this.overseasFee = overseasFee;
        propertySupport.firePropertyChange(PROP_OVERSEAS_FEE, oldOverseasFee, overseasFee);
    }
    
    public int getOverseasFee() {
        return overseasFee;
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
