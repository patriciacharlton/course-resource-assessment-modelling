package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.lkl.cram.model.xml.XmlGenericMapAdapter;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@XmlType(propOrder = {"weeklyLearnerHourCount", "weekCount", "nonWeeklyLearnerHourCount", "activity", "preparationMap", "supportMap"})
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class TLALineItem implements LineItem {

    public static final String PROP_ACTIVITY = "activity";
    public static final String PROP_NON_WEEKLY = "nonWeekly";
    public static final String PROP_WEEKCOUNT = "weekCount";
    public static final String PROP_WEEKLY = "weekly";
    //The number of hours per week that the student is expected to spend learning
    private float weeklyLearnerHourCount;
    //The number of weeks that this activity runs
    private int weekCount;   
    //The number of hours of non-regular learning hours (e.g. for an assessment)
    private float nonWeeklyLearnerHourCount;
    
    @XmlJavaTypeAdapter(XmlGenericMapAdapter.class)
    @XmlElement(name = "preparationMap")
    private Map<ModulePresentation, PreparationTime> preparationMap = new HashMap<>();
    
    @XmlJavaTypeAdapter(XmlGenericMapAdapter.class)
    @XmlElement(name = "supportMap")
    private Map<ModulePresentation, SupportTime> supportMap = new HashMap<>();
    
    private TLActivity activity;
    
    private PropertyChangeSupport propertySupport;

    public TLALineItem() {
        propertySupport = new PropertyChangeSupport(this);
        activity = new TLActivity();
	this.weeklyLearnerHourCount = 0f;
	this.weekCount = 0;
	this.nonWeeklyLearnerHourCount = 0f;
    }

    TLALineItem(TLActivity activity, float weeklyLearnerHourCount, int weekCount, float nonWeeklyLearnerHourCount) {
        this();
        this.activity = activity;
        this.weeklyLearnerHourCount = weeklyLearnerHourCount;
	this.weekCount = weekCount;
        this.nonWeeklyLearnerHourCount = nonWeeklyLearnerHourCount;
    }

    public int getMaximumGroupSizeForPresentation(ModulePresentation presentation) {
        switch (activity.getLearningExperience()) {
	    case PERSONALISED: {
		return 1;
	    }
	    case SOCIAL: {
		return activity.getMaximumGroupSize();
	    }
	    case ONE_SIZE_FOR_ALL: {
		return presentation.getTotalStudentCount();
	    }
	    default: {
		throw new RuntimeException("Invalid learning experience");
	    }
	}
    }

    public float getWeeklyLearnerHourCount() {
        return weeklyLearnerHourCount;
    }

    @XmlAttribute
    public void setWeeklyLearnerHourCount(float f) {
        float oldValue = weeklyLearnerHourCount;
        weeklyLearnerHourCount = f;
        propertySupport.firePropertyChange(PROP_WEEKLY, oldValue, weeklyLearnerHourCount);
    }

    /**
     * Get the value of weekCount
     *
     * @return the value of weekCount
     */
    @Override
    public int getWeekCount() {
	return weekCount;
    }

    /**
     * Set the value of weekCount
     *
     * @param weekCount new value of weekCount
     */
    @XmlAttribute
    public void setWeekCount(int weekCount) {
	int oldWeekCount = this.weekCount;
	this.weekCount = weekCount;
	propertySupport.firePropertyChange(PROP_WEEKCOUNT, oldWeekCount, weekCount);
    }
    
    public float getNonWeeklyLearnerHourCount() {
        return nonWeeklyLearnerHourCount;
    }

    @XmlAttribute
    public void setNonWeeklyLearnerHourCount(float f) {
        float oldValue = nonWeeklyLearnerHourCount;
        nonWeeklyLearnerHourCount = f;
        propertySupport.firePropertyChange(PROP_NON_WEEKLY, oldValue, nonWeeklyLearnerHourCount);
    }

    public float getTotalLearnerHourCount(Module module) {
        return weekCount * weeklyLearnerHourCount + nonWeeklyLearnerHourCount;
    }

    void setPreparationTime(ModulePresentation mp, PreparationTime pt) {
        preparationMap.put(mp, pt);
    }

    @Override
    public PreparationTime getPreparationTime(ModulePresentation mp) {
        PreparationTime pt = preparationMap.get(mp);
        if (pt == null) {
            pt = new PreparationTime();
            preparationMap.put(mp, pt);
        }
        return pt;
    }

    @Override
    public void setSupportTime(ModulePresentation mp, SupportTime st) {
        supportMap.put(mp, st);
    }

    @Override
    public SupportTime getSupportTime(ModulePresentation mp) {
        SupportTime st = supportMap.get(mp);
        if (st == null) {
            st = new SupportTime();
            supportMap.put(mp, st);
        }
        return st;
    }

    public TLActivity getActivity() {
        return activity;
    }

    @Override
    public String getName() {
        return activity.getName();
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

    public void setActivity(TLActivity selectedTLA) {
        TLActivity oldValue = activity;
        this.activity = selectedTLA;
        propertySupport.firePropertyChange(PROP_ACTIVITY, oldValue, activity);
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 53 * hash + Float.floatToIntBits(this.weeklyLearnerHourCount);
	hash = 53 * hash + this.weekCount;
	hash = 53 * hash + Float.floatToIntBits(this.nonWeeklyLearnerHourCount);
	hash = 53 * hash + Objects.hashCode(this.preparationMap);
	hash = 53 * hash + Objects.hashCode(this.supportMap);
	hash = 53 * hash + Objects.hashCode(this.activity);
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
	final TLALineItem other = (TLALineItem) obj;
	if (Float.floatToIntBits(this.weeklyLearnerHourCount) != Float.floatToIntBits(other.weeklyLearnerHourCount)) {
	    return false;
	}
	if (this.weekCount != other.weekCount) {
	    return false;
	}
	if (Float.floatToIntBits(this.nonWeeklyLearnerHourCount) != Float.floatToIntBits(other.nonWeeklyLearnerHourCount)) {
	    return false;
	}
	if (!Objects.equals(this.preparationMap, other.preparationMap)) {
	    return false;
	}
	if (!Objects.equals(this.supportMap, other.supportMap)) {
	    return false;
	}
	if (!Objects.equals(this.activity, other.activity)) {
	    return false;
	}
	return true;
    }

    @Override
    public float getNumberOfIndividuals_Groups(ModulePresentation modulePresentation, Module module) {
	float i = ((float) modulePresentation.getTotalStudentCount() / (float) getMaximumGroupSizeForPresentation(modulePresentation));
	return (int) (i + 0.99);
    }
    
   
}
