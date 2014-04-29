/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.lkl.cram.model;

import com.bluelotussoftware.jaxb.adapter.XmlGenericMapAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This class describes the way in which a teaching-learning activity is used
 * by a module. It takes the shopping trolley pattern: the TLA is the product,
 * whereas this class represents quantity of product.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@XmlType(propOrder = {"weeklyLearnerHourCount", "weekCount", "nonWeeklyLearnerHourCount", "activity", "preparationMap", "supportMap"})
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class TLALineItem implements LineItem {

    /**
     * Property to indicate change in underlying activity.
     * @see TLALineItem#setActivity(TLActivity) 
     */
    public static final String PROP_ACTIVITY = "activity";
    /**
     * Property to indicate change in the number of non-weekly hours spent
     * by the student
     * @see TLALineItem#setNonWeeklyLearnerHourCount(float) 
     */
    public static final String PROP_NON_WEEKLY = "nonWeekly";
    /**
     * Property to indicate change in the number of weeks this activity runs
     * @see TLALineItem#setWeekCount(int) 
     */
    public static final String PROP_WEEKCOUNT = "weekCount";
    /**
     * Property to indicate change in the number of weekly hours spent by the 
     * student
     * @see TLALineItem#setWeeklyLearnerHourCount(float) 
     */
    public static final String PROP_WEEKLY = "weekly";
    //The number of hours per week that the student is expected to spend learning
    private float weeklyLearnerHourCount;
    //The number of weeks that this activity runs
    @XmlAttribute
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
    
    private final transient PropertyChangeSupport propertySupport;

    /**
     * Default Constructor. 
     */
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

    /**
     * Return the maximum tutor group size for a module presentation ('run'). 
     * This is dependent on the kind of learning experience provided by the
     * teaching-learning activity. If the activity is personalised, the tutor
     * group size is 1; if the activity is social, then the tutor group size is
     * the maximum group size for the activity; otherwise the tutor group size 
     * is the cohort size for the module presentation
     * @param presentation the module presentation ('run') that this teaching-learning activity is running in
     * @return the maximum tutor group size
     */
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

    /**
     * Return the number of hours per week that the student will undertake the activity
     * @return the number of hours per week that the student will undertake the activity
     */
    public float getWeeklyLearnerHourCount() {
        return weeklyLearnerHourCount;
    }

    /**
     * Set the number of hours per week that the student will undertake the activity
     * @param f the number of hours per week that the student will undertake the activity
     * @see TLALineItem#PROP_WEEKLY
     */
    @XmlAttribute
    public void setWeeklyLearnerHourCount(float f) {
        float oldValue = weeklyLearnerHourCount;
        weeklyLearnerHourCount = f;
        propertySupport.firePropertyChange(PROP_WEEKLY, oldValue, weeklyLearnerHourCount);
    }

    /**
     * Return the number of weeks that the student will undertake the activity
     * @return the number of weeks that the student will undertake the activity         *
     * @param m the module that contains this teaching-learning activity
     */
    @Override
    public int getWeekCount(Module m) {
	return weekCount;
    }

    /**
     * Set the  number of weeks that the student will undertake the activity
     * @param weekCount the number of weeks that the student will undertake the activity
     * @see TLALineItem#PROP_WEEKCOUNT
     */
    public void setWeekCount(int weekCount) {
	int oldWeekCount = this.weekCount;
	this.weekCount = weekCount;
	propertySupport.firePropertyChange(PROP_WEEKCOUNT, oldWeekCount, weekCount);
    }
    
    /**
     * Return the number of non-weekly hours that the student will undertake the activity
     * @return the number of non-weekly hours that the student will undertake the activity
     */
    public float getNonWeeklyLearnerHourCount() {
        return nonWeeklyLearnerHourCount;
    }

    /**
     * Set the number of non-weekly hours that the student will undertake the activity 
     * @param f the number of non-weekly hours that the student will undertake the activity
     * @see TLALineItem#PROP_NON_WEEKLY
     */
    @XmlAttribute
    public void setNonWeeklyLearnerHourCount(float f) {
        float oldValue = nonWeeklyLearnerHourCount;
        nonWeeklyLearnerHourCount = f;
        propertySupport.firePropertyChange(PROP_NON_WEEKLY, oldValue, nonWeeklyLearnerHourCount);
    }

    /**
     * Return the total number of hours that the student spends on the activity.
     * That is the number of weeks multiplied by the number of hours per week,
     * plus the number of non-weekly hours
     * @param module the module of which the activity is a part
     * @return the total number of hours spent by the student on the activity
     */
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

    /**
     * Return the activity described by this line item
     * @return the activity for this line item
     */
    public TLActivity getActivity() {
        return activity;
    }

    @Override
    public String getName() {
        return activity.getName();
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

    /**
     * Set the teaching-learning activity described by this line item
     * @param theTLA the teaching-learning activity described by this line item
     * @see TLALineItem#PROP_ACTIVITY
     */
    public void setActivity(TLActivity theTLA) {
        TLActivity oldValue = activity;
        this.activity = theTLA;
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

    @Override
    public int removeFrom(Module module) {
        return module.removeTLALineItem(this);
    }
    
    @Override
    public void insertLineItemAt(Module module, int index) {
        module.insertTLALineItem(this, index);
    }
}
