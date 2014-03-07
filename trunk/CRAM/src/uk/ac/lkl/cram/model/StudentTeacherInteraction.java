package uk.ac.lkl.cram.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Class to represent the interaction between a student and teacher. This class 
 * can be considered to be immutable, once initialised.
 * @see TLActivity#setStudentTeacherInteraction(StudentTeacherInteraction) 
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class StudentTeacherInteraction implements Serializable {
    private boolean isTutorSupported;
    
    private boolean isOnline;
    
    private boolean isLocationSpecific;
    
    private boolean isTimeSpecific;
    
    /**
     * Create a new instance of Student Teacher interaction with all
     * characteristics set to false.
     */
    public StudentTeacherInteraction() {
	//Defaults
	isTutorSupported = false;
	isOnline = false;
	isLocationSpecific = false;
	isTimeSpecific = false;
    }

    /**
     * Is this student teacher interaction one that is supported by the tutor?
     * @return true if the interaction is supported by the tutor
     */
    public boolean isTutorSupported() {
	return isTutorSupported;
    }

    /**
     * Indicate whether this student teacher interaction is supported by a tutor
     * @param isTutorSupported 
     */
    @XmlAttribute
    public void setTutorSupported(boolean isTutorSupported) {
	this.isTutorSupported = isTutorSupported;
    }

    /**
     * Is this student teacher interaction one that is undertaken online?
     * @return true if the interaction is online
     */
    public boolean isOnline() {
	return isOnline;
    }

    /**
     * Indicate whether this student teacher interaction is undertaken online
     * @param isOnline
     */
    @XmlAttribute
    public void setOnline(boolean isOnline) {
	this.isOnline = isOnline;
    }

    /**
     * Is this student teacher interaction one that takes place at a specific location?
     * @return true if this student teacher interaction is one that takes place at a specific location
     */
    public boolean isLocationSpecific() {
	return isLocationSpecific;
    }

    /**
     * Indicate whether this student teacher interaction takes place at a specific location
     * @param isLocationSpecific
     */
    @XmlAttribute
    public void setLocationSpecific(boolean isLocationSpecific) {
	this.isLocationSpecific = isLocationSpecific;
    }

    /**
     * Is this student teacher interaction one that takes place at a specific time?
     * @return true if this student teacher interaction is one that takes place at a specific time
     */
    public boolean isTimeSpecific() {
	return isTimeSpecific;
    }

    /**
     * Indicate whether this student teacher interaction takes place at a specific time
     * @param isTimeSpecific
     */
    @XmlAttribute
    public void setTimeSpecific(boolean isTimeSpecific) {
	this.isTimeSpecific = isTimeSpecific;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 23 * hash + (this.isTutorSupported ? 1 : 0);
	hash = 23 * hash + (this.isOnline ? 1 : 0);
	hash = 23 * hash + (this.isLocationSpecific ? 1 : 0);
	hash = 23 * hash + (this.isTimeSpecific ? 1 : 0);
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
	final StudentTeacherInteraction other = (StudentTeacherInteraction) obj;
	if (this.isTutorSupported != other.isTutorSupported) {
	    return false;
	}
	if (this.isOnline != other.isOnline) {
	    return false;
	}
	if (this.isLocationSpecific != other.isLocationSpecific) {
	    return false;
	}
	if (this.isTimeSpecific != other.isTimeSpecific) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "StudentTeacherInteraction{" + "isTutorSupported=" + isTutorSupported + ", isOnline=" + isOnline + ", isLocationSpecific=" + isLocationSpecific + ", isTimeSpecific=" + isTimeSpecific + '}';
    }
    
    
}
