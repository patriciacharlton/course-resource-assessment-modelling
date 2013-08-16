package uk.ac.lkl.cram.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Bernard Horan
 */
public class StudentTeacherInteraction implements Serializable {
    private boolean isTutorSupported;
    
    private boolean isOnline;
    
    private boolean isLocationSpecific;
    
    private boolean isTimeSpecific;
    
    public StudentTeacherInteraction() {
	//Defaults
	isTutorSupported = false;
	isOnline = false;
	isLocationSpecific = false;
	isTimeSpecific = false;
    }

    public boolean isTutorSupported() {
	return isTutorSupported;
    }

    @XmlAttribute
    public void setTutorSupported(boolean isTutorSupported) {
	this.isTutorSupported = isTutorSupported;
    }

    public boolean isOnline() {
	return isOnline;
    }

    @XmlAttribute
    public void setOnline(boolean isOnline) {
	this.isOnline = isOnline;
    }

    public boolean isLocationSpecific() {
	return isLocationSpecific;
    }

    @XmlAttribute
    public void setLocationSpecific(boolean isLocationSpecific) {
	this.isLocationSpecific = isLocationSpecific;
    }

    public boolean isTimeSpecific() {
	return isTimeSpecific;
    }

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
