package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class represents a teaching-learning activity.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@XmlType(propOrder = {"name", "maximumGroupSize", "studentTeacherInteraction", "learnerFeedback", "learningType", "learningExperience"})
@SuppressWarnings({"ClassWithoutLogger", "serial"})
public class TLActivity implements Serializable {   

    /**
     * Property to indicate change of name of activity
     * @see TLActivity#setName(String) 
     */
    public static final String PROP_NAME = "name";
    /**
     * Property to indicate change of learning experience
     * @see TLActivity#setLearningExperience(EnumeratedLearningExperience) 
     */
    public static final String PROP_LEARNING_EXPERIENCE = "learning_experience";
    /**
     * Property to indicate change of maximum group size
     * @see TLActivity#setMaximumGroupSize(int) 
     */
    public static final String PROP_MAX_GROUP_SIZE = "maxGroupSize";
    
    private LearningType learningType; //acquisition, practice, etc.
    
    private EnumeratedLearningExperience learningExperience; //personalised, social, standard
    
    private StudentTeacherInteraction studentTeacherInteraction;
    private String name;
    
    //The maximum number of students for this TLA
    //E.g. Ten students for a face to face tutorial
    private int maximumGroupSize;
    
    private LearnerFeedback learnerFeedback;
    
    private final transient PropertyChangeSupport propertySupport;


    TLActivity() {
	propertySupport = new PropertyChangeSupport(this);
	learningType = 	new LearningType();
	this.name = "";
	learningExperience = EnumeratedLearningExperience.ONE_SIZE_FOR_ALL; //Default
	learnerFeedback = LearnerFeedback.NONE; //Default
	studentTeacherInteraction = new StudentTeacherInteraction();
	maximumGroupSize = 0;
    }
    
    /**
     * Create a new teaching-learning activity with the given name
     * @param name the name of the new teaching-learning activity
     */
    public TLActivity(String name) {
	this();
	this.name = name;
    }

    @XmlElement
    void setLearningType(LearningType learningType) {
	this.learningType = learningType;
    }

    /**
     * Set the kind of learning experience for this teaching-learning activity.
     * The learning experience is one taken from an enumerated type
     * @param le the learning experience
     * @see TLActivity#PROP_LEARNING_EXPERIENCE
     */
    @XmlAttribute
    public void setLearningExperience(EnumeratedLearningExperience le) {
	EnumeratedLearningExperience oldValue = learningExperience;
	this.learningExperience = le;
	propertySupport.firePropertyChange(PROP_LEARNING_EXPERIENCE, oldValue, learningExperience);
    }

    /**
     * Return the name of the teaching-learning experience
     * @return the name of the teaching-learning experience
     */
    public String getName() {
	return name;
    }
    
    /**
     * Set the name of the teaching-learning experience
     * @param text the name of the teaching-learning experience
     * @see TLActivity#PROP_NAME
     */
    @XmlAttribute
    public void setName(String text) {
	String oldValue = name;
	name = text;
	propertySupport.firePropertyChange(PROP_NAME, oldValue, name);
    }

    /**
     * Return the learning type for the teaching-learning activity
     * @return the learning type for the learning experience
     */
    public LearningType getLearningType() {
	return learningType;
    }

    /**
     * return the (enumerated) learning experience for the teaching-learning activity
     * @return the learning experience for the teaching-learning activity
     */
    public EnumeratedLearningExperience getLearningExperience() {
	return learningExperience;
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
     * Set the (enumerated) learning feedback for the teaching-learning activity
     * @param learnerFeedback the learning feedback for the teaching-learning activity 
     */
    @XmlAttribute
    public void setLearnerFeedback(LearnerFeedback learnerFeedback) {
	this.learnerFeedback = learnerFeedback;
    }
    
    /**
     * Return the (enumerated) learning feedback for the teaching-learning activity
     * @return the learning feedback for the teaching-learning activity
     */
    public LearnerFeedback getLearnerFeedback() {
	return learnerFeedback;
    }
    
    
    /**
     * Return the student-teaching interaction for the teaching-learning activity
     * @return the student-teaching interaction for the teaching-learning activity
     */
    public StudentTeacherInteraction getStudentTeacherInteraction() {
	return studentTeacherInteraction;
    }

    @XmlElement
    void setStudentTeacherInteraction(StudentTeacherInteraction studentTeacherInteraction) {
	this.studentTeacherInteraction = studentTeacherInteraction;
    }
    
    /**
     * Return the maximum tutor group size for the teaching-learning activity
     * @return the maximum tutor group size for the teaching-learning activity
     */
    public int getMaximumGroupSize() {
	return maximumGroupSize;
    }
    
    /**
     * Set the maximum tutor group size for the teaching-learning activity
     * @param i the maximum tutor group size for the teaching-learning activity
     * @see TLActivity#PROP_MAX_GROUP_SIZE
     */
    @XmlAttribute
    public void setMaximumGroupSize(int i) {
        int oldValue = maximumGroupSize;
        maximumGroupSize = i;
        propertySupport.firePropertyChange(PROP_MAX_GROUP_SIZE, oldValue, maximumGroupSize);
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 97 * hash + Objects.hashCode(this.learningType);
	hash = 97 * hash + (this.learningExperience != null ? this.learningExperience.hashCode() : 0);
	hash = 97 * hash + Objects.hashCode(this.studentTeacherInteraction);
	hash = 97 * hash + Objects.hashCode(this.name);
	hash = 97 * hash + this.maximumGroupSize;
	hash = 97 * hash + (this.learnerFeedback != null ? this.learnerFeedback.hashCode() : 0);
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
	final TLActivity other = (TLActivity) obj;
	if (!Objects.equals(this.learningType, other.learningType)) {
	    return false;
	}
	if (this.learningExperience != other.learningExperience) {
	    return false;
	}
	if (!Objects.equals(this.studentTeacherInteraction, other.studentTeacherInteraction)) {
	    return false;
	}
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	if (this.maximumGroupSize != other.maximumGroupSize) {
	    return false;
	}
	if (this.learnerFeedback != other.learnerFeedback) {
	    return false;
	}
	return true;
    }

    

    
    
}
