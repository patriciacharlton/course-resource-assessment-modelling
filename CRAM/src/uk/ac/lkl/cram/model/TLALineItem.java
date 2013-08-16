package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.lkl.cram.model.xml.XmlGenericMapAdapter;

@XmlType(propOrder = {"weeklyLearnerHourCount", "nonWeeklyLearnerHourCount", "activity", "preparationMap", "supportMap"})
public class TLALineItem implements LineItem {

    private static final long serialVersionUID = 1L;
    public static final String PROP_ACTIVITY = "activity";
    public static final String PROP_NAME = "name";
    public static final String PROP_MAX_GROUP_SIZE = "maxGroupSize";
    public static final String PROP_NON_WEEKLY = "nonWeekly";
    public static final String PROP_WEEKLY = "weekly";
    private float weeklyLearnerHourCount;
    private float nonWeeklyLearnerHourCount;
    
    @XmlJavaTypeAdapter(XmlGenericMapAdapter.class)
    @XmlElement(name = "preparationMap")
    private Map<ModulePresentation, PreparationTime> preparationMap = new IdentityHashMap<ModulePresentation, PreparationTime>();
    
    @XmlJavaTypeAdapter(XmlGenericMapAdapter.class)
    @XmlElement(name = "supportMap")
    private Map<ModulePresentation, SupportTime> supportMap = new IdentityHashMap<ModulePresentation, SupportTime>();
    
    private TLActivity activity;
    
    private PropertyChangeSupport propertySupport;

    public TLALineItem() {
        propertySupport = new PropertyChangeSupport(this);
        activity = new TLActivity();
	this.weeklyLearnerHourCount = 0f;
	this.nonWeeklyLearnerHourCount = 0f;
    }

    TLALineItem(TLActivity activity, float weeklyLearnerHourCount, float nonWeeklyLearnerHourCount) {
        this();
        this.activity = activity;
        this.weeklyLearnerHourCount = weeklyLearnerHourCount;
        this.nonWeeklyLearnerHourCount = nonWeeklyLearnerHourCount;
    }

    public int getMaximumGroupSize(Module m, ModulePresentation presentation) {
        switch (activity.getLearningExperience()) {
	    case PERSONALISED: {
		return 1;
	    }
	    case SOCIAL: {
		return m.getTutorGroupSize();
	    }
	    case ONE_SIZE_FOR_ALL: {
		return presentation.getStudentCount();
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
        return module.getWeekCount() * weeklyLearnerHourCount + nonWeeklyLearnerHourCount;
    }

    void setPreparationTime(ModulePresentation mp, PreparationTime pt) {
        preparationMap.put(mp, pt);
    }

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
    public float getTotalHours(SupportTime st, Module module, ModulePresentation mp) {
        return st.getTotalHours(module, mp, this);
    }

    @Override
    public float getCost(SupportTime st, Module module, ModulePresentation mp) {
        return st.getCost(module, mp, this);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void setActivity(TLActivity selectedTLA) {
        this.activity = selectedTLA;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 19 * hash + Float.floatToIntBits(this.weeklyLearnerHourCount);
	hash = 19 * hash + Float.floatToIntBits(this.nonWeeklyLearnerHourCount);
	hash = 19 * hash + (this.preparationMap != null ? this.preparationMap.hashCode() : 0);
	hash = 19 * hash + (this.supportMap != null ? this.supportMap.hashCode() : 0);
	hash = 19 * hash + (this.activity != null ? this.activity.hashCode() : 0);
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
	final TLALineItem other = (TLALineItem) obj;
	if (Float.floatToIntBits(this.weeklyLearnerHourCount) != Float.floatToIntBits(other.weeklyLearnerHourCount)) {
	    return false;
	}
	if (Float.floatToIntBits(this.nonWeeklyLearnerHourCount) != Float.floatToIntBits(other.nonWeeklyLearnerHourCount)) {
	    return false;
	}
	if (this.preparationMap != other.preparationMap && (this.preparationMap == null || !this.preparationMap.equals(other.preparationMap))) {
	    return false;
	}
	if (this.supportMap != other.supportMap && (this.supportMap == null || !this.supportMap.equals(other.supportMap))) {
	    return false;
	}
	if (this.activity != other.activity && (this.activity == null || !this.activity.equals(other.activity))) {
	    return false;
	}
	return true;
    }
    
    
}
