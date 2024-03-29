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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
/**
 * Class to represent the learning type for a teaching-learning activity.
 * @see TLActivity#setLearningType(LearningType) 
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings({"ClassWithoutLogger", "serial"})
public class LearningType implements Serializable {

    /**
     * Property to indicate the change in value for the acquisition
     * @see LearningType#setAcquisition(int) 
     */
    public static final String PROP_ACQUISITION = "acquisition";
    /**
     * Property to indicate the change in value for the discussion
     * @see LearningType#setDiscussion(int) 
     */
    public static final String PROP_DISCUSSION = "discussion";
    /**
     * Property to indicate the change in value for the inquiry
     * @see LearningType#setInquiry(int) 
     */
    public static final String PROP_INQUIRY = "inquiry";
    /**
     * Property to indicate the change in value for the practice
     * @see LearningType#setPractice(int) 
     */
    public static final String PROP_PRACTICE = "practice";
    /**
     * Property to indicate the change in value for the production
     * @see LearningType#setProduction(int) 
     */
    public static final String PROP_PRODUCTION = "production";
    /**
     * Property to indicate the change in value for the collaboration
     * @see LearningType#setCollaboration(int) 
     */
    public static final String PROP_COLLABORATION = "collaboration"; 
    
    //These should all be in the range 0 <= x <= 100
    private int acquisition = 0;
    private int inquiry = 0;
    private int discussion = 0;
    private int practice = 0;
    private int production = 0;
    private int collaboration = 0;
    private final transient PropertyChangeSupport propertySupport;


    LearningType() {
	propertySupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Create a new instance of a LearningType with the supplied parameters, each
     * of which should be in the range 0 <= x <= 100
     * @param aquisition the amount of acquisition this learning type represents
     * @param inquiry the amount of inquiry this learning type represents
     * @param discussion the amount of discussion this learning type represents
     * @param practice the amount of practice this learning type represents
     * @param production the amount of production this learning type represents
     * @param collaboration the amount of collaboration this learning type represents
     */
    public LearningType(int aquisition, int inquiry, int discussion,
	    int practice, int production, int collaboration) {
	this();
	this.acquisition = aquisition;
	this.inquiry = inquiry;
	this.discussion = discussion;
	this.practice = practice;
	this.production = production;
	this.collaboration = collaboration;
	checkSum();
    }
    
    /**
     * Backward compatibility
     * @param aquisition
     * @param inquiry
     * @param discussion
     * @param practice
     * @param production
     * @deprecated 
     */
    @Deprecated
    public LearningType(int aquisition, int inquiry, int discussion,
	    int practice, int production) {
	this(aquisition, inquiry, discussion, practice, production, 0);
    }

    private void checkSum() {
	int sum = acquisition + inquiry + discussion + practice + production + collaboration;
	if (sum != 100) {
	    throw new RuntimeException("Checksum failed: " + sum);
	}
    }

    /**
     * Return the amount of acquisition this learning type represents
     * @return the amount of acquisition this learning type represents
     */
    public int getAcquisition() {
	return acquisition;
    }

    /**
     * Return the amount of inquiry this learning type represents
     * @return the amount of inquiry this learning type represents
     */
    public int getInquiry() {
	return inquiry;
    }

    /**
     * Return the amount of discussion this learning type represents
     * @return the amount of discussion this learning type represents
     */
    public int getDiscussion() {
	return discussion;
    }

    /**
     * Return the amount of practice this learning type represents
     * @return the amount of practice this learning type represents
     */
    public int getPractice() {
	return practice;
    }

    /**
     * Return the amount of production this learning type represents
     * @return the amount of production this learning type represents
     */
    public int getProduction() {
	return production;
    }
    
    /**
     * Return the amount of collaboration this learning type represents
     * @return the amount of collaboration this learning type represents
     */
    public int getCollaboration() {
	return collaboration;
    }

    /**
     * Set the amount of acquisition this learning type represents
     * @param i the amount of acquisition this learning type represents
     * @see LearningType#PROP_ACQUISITION
     */
    @XmlAttribute
    public void setAcquisition(int i) {
	int oldValue = acquisition;
	acquisition = i;
	propertySupport.firePropertyChange(PROP_ACQUISITION, oldValue, acquisition);	
    }
    
    /**
     * Set the amount of discussion this learning type represents
     * @param i the amount of discussion this learning type represents
     * @see LearningType#PROP_DISCUSSION
     */
    @XmlAttribute
    public void setDiscussion(int i) {
	int oldValue = discussion;
	discussion = i;
	propertySupport.firePropertyChange(PROP_DISCUSSION, oldValue, discussion);
    }    

    /**
     * Set the amount of inquiry this learning type represents
     * @param i the amount of inquiry this learning type represents
     * @see LearningType#PROP_INQUIRY
     */
    @XmlAttribute
    public void setInquiry(int i) {
	int oldValue = inquiry;
	inquiry = i;
	propertySupport.firePropertyChange(PROP_INQUIRY, oldValue, inquiry);
    }    

    /**
     * Set the amount of practice this learning type represents
     * @param i the amount of practice this learning type represents
     * @see LearningType#PROP_PRACTICE
     */
    @XmlAttribute
    public void setPractice(int i) {
	int oldValue = practice;
	practice = i;
	propertySupport.firePropertyChange(PROP_PRACTICE, oldValue, practice);
    }
    
    /**
     * Set the amount of production this learning type represents
     * @param i the amount of production this learning type represents
     * @see LearningType#PROP_PRODUCTION
     */
    @XmlAttribute
    public void setProduction(int i) {
	int oldValue = production;
	production = i;
	propertySupport.firePropertyChange(PROP_PRODUCTION, oldValue, production);
    }
    
    /**
     * Set the amount of collaboration this learning type represents
     * @param i the amount of collaboration this learning type represents
     * @see LearningType#PROP_COLLABORATION
     */
    @XmlAttribute
    public void setCollaboration(int i) {
	int oldValue = collaboration;
	collaboration = i;
	propertySupport.firePropertyChange(PROP_COLLABORATION, oldValue, collaboration);
    }
    
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.acquisition;
        hash = 67 * hash + this.inquiry;
        hash = 67 * hash + this.discussion;
        hash = 67 * hash + this.practice;
        hash = 67 * hash + this.production;
        hash = 67 * hash + this.collaboration;
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
        final LearningType other = (LearningType) obj;
        if (this.acquisition != other.acquisition) {
            return false;
        }
        if (this.inquiry != other.inquiry) {
            return false;
        }
        if (this.discussion != other.discussion) {
            return false;
        }
        if (this.practice != other.practice) {
            return false;
        }
        if (this.production != other.production) {
            return false;
        }
        if (this.collaboration != other.collaboration) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LearningType{" + "acquisition=" + acquisition + ", inquiry=" + inquiry + ", discussion=" + discussion + ", practice=" + practice + ", production=" + production + ", collaboration=" + collaboration + '}';
    }

	

    
}
