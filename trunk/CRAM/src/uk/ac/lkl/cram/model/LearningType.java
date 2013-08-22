package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
/**
 * $Date$
 * @author Bernard Horan
 */

public class LearningType implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PROP_ACQUISITION = "acquisition";
    public static final String PROP_DISCUSSION = "discussion";
    public static final String PROP_INQUIRY = "inquiry";
    public static final String PROP_PRACTICE = "practice";
    public static final String PROP_PRODUCTION = "production";
    public static final String PROP_COLLABORATION = "collaboration"; 
    
    //These should all be in the range 0 <= x <= 100
    private int acquisition = 0;
    private int inquiry = 0;
    private int discussion = 0;
    private int practice = 0;
    private int production = 0;
    private int collaboration = 0;
    private PropertyChangeSupport propertySupport;


    LearningType() {
	propertySupport = new PropertyChangeSupport(this);
    }
    
    /**
     * @param aquisition
     * @param inquiry
     * @param discussion
     * @param practice
     * @param production
     * @param collaboration  
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
	if (acquisition + inquiry + discussion + practice + production + collaboration != 100) {
	    throw new RuntimeException("Checksum failed");
	}

    }

    public int getAcquisition() {
	return acquisition;
    }

    public int getInquiry() {
	return inquiry;
    }

    public int getDiscussion() {
	return discussion;
    }

    public int getPractice() {
	return practice;
    }

    public int getProduction() {
	return production;
    }
    
    public int getCollaboration() {
	return collaboration;
    }

    @XmlAttribute
    public void setAcquisition(int i) {
	int oldValue = acquisition;
	acquisition = i;
	propertySupport.firePropertyChange(PROP_ACQUISITION, oldValue, acquisition);	
    }
    
    @XmlAttribute
    public void setDiscussion(int i) {
	int oldValue = discussion;
	discussion = i;
	propertySupport.firePropertyChange(PROP_DISCUSSION, oldValue, discussion);
    }    

    @XmlAttribute
    public void setInquiry(int i) {
	int oldValue = inquiry;
	inquiry = i;
	propertySupport.firePropertyChange(PROP_INQUIRY, oldValue, inquiry);
    }    

    @XmlAttribute
    public void setPractice(int i) {
	int oldValue = practice;
	practice = i;
	propertySupport.firePropertyChange(PROP_PRACTICE, oldValue, practice);
    }
    
    @XmlAttribute
    public void setProduction(int i) {
	int oldValue = production;
	production = i;
	propertySupport.firePropertyChange(PROP_PRODUCTION, oldValue, production);
    }
    
    @XmlAttribute
    public void setCollaboration(int i) {
	int oldValue = collaboration;
	collaboration = i;
	propertySupport.firePropertyChange(PROP_COLLABORATION, oldValue, collaboration);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acquisition;
		result = prime * result + discussion;
		result = prime * result + inquiry;
		result = prime * result + practice;
		result = prime * result + production;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LearningType other = (LearningType) obj;
		if (acquisition != other.acquisition) {
			return false;
		}
		if (discussion != other.discussion) {
			return false;
		}
		if (inquiry != other.inquiry) {
			return false;
		}
		if (practice != other.practice) {
			return false;
		}
		if (production != other.production) {
			return false;
		}
		return true;
	}

    @Override
    public String toString() {
	return "LearningType{" + "acquisition=" + acquisition + ", inquiry=" + inquiry + ", discussion=" + discussion + ", practice=" + practice + ", production=" + production + '}';
    }

    
}
