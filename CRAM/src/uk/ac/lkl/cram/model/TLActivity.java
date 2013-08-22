package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "studentTeacherInteraction", "learnerFeedback", "learningType", "learningExperience"})
public class TLActivity implements Serializable {   

    private static final long serialVersionUID = 1L;
    public static final String PROP_NAME = "name";    
    
    private LearningType learningType; //acquisition, practice, etc.
    
    private EnumeratedLearningExperience learningExperience; //personalised, social, standard
    
    private StudentTeacherInteraction studentTeacherInteraction;
    private String name;
    
    private LearnerFeedback learnerFeedback;
    
    private PropertyChangeSupport propertySupport;


    TLActivity() {
	propertySupport = new PropertyChangeSupport(this);
	learningType = 	new LearningType();
	this.name = "";
	learningExperience = EnumeratedLearningExperience.ONE_SIZE_FOR_ALL; //Default
	learnerFeedback = LearnerFeedback.NONE; //Default
	studentTeacherInteraction = new StudentTeacherInteraction();
    }
    
    public TLActivity(String name) {
	this();
	this.name = name;
    }

    @XmlElement
    public void setLearningType(LearningType learningType) {
	this.learningType = learningType;
    }

    @XmlAttribute
    public void setLearningExperience(EnumeratedLearningExperience learningExperience) {
	this.learningExperience = learningExperience;
    }

    public String getName() {
	return name;
    }
    
    @XmlAttribute
    public void setName(String text) {
	String oldValue = name;
	name = text;
	propertySupport.firePropertyChange(PROP_NAME, oldValue, name);
    }

    public LearningType getLearningType() {
	return learningType;
    }

    public EnumeratedLearningExperience getLearningExperience() {
	return learningExperience;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }
  
    @XmlAttribute
    public void setLearnerFeedback(LearnerFeedback learnerFeedback) {
	this.learnerFeedback = learnerFeedback;
    }
    
    public LearnerFeedback getLearnerFeedback() {
	return learnerFeedback;
    }
    
    
    public StudentTeacherInteraction getStudentTeacherInteraction() {
	return studentTeacherInteraction;
    }

    public void setStudentTeacherInteraction(StudentTeacherInteraction studentTeacherInteraction) {
	this.studentTeacherInteraction = studentTeacherInteraction;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 47 * hash + (this.learningType != null ? this.learningType.hashCode() : 0);
	hash = 47 * hash + (this.learningExperience != null ? this.learningExperience.hashCode() : 0);
	hash = 47 * hash + (this.studentTeacherInteraction != null ? this.studentTeacherInteraction.hashCode() : 0);
	hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
	hash = 47 * hash + (this.learnerFeedback != null ? this.learnerFeedback.hashCode() : 0);
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
	final TLActivity other = (TLActivity) obj;
	if (this.learningType != other.learningType && (this.learningType == null || !this.learningType.equals(other.learningType))) {
	    return false;
	}
	if (this.learningExperience != other.learningExperience) {
	    return false;
	}
	if (this.studentTeacherInteraction != other.studentTeacherInteraction && (this.studentTeacherInteraction == null || !this.studentTeacherInteraction.equals(other.studentTeacherInteraction))) {
	    return false;
	}
	if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
	    return false;
	}
	if (this.learnerFeedback != other.learnerFeedback) {
	    return false;
	}
	return true;
    }

    
    
}
