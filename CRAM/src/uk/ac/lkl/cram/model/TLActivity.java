package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@XmlType(propOrder = {"name", "maximumGroupSize", "studentTeacherInteraction", "learnerFeedback", "learningType", "learningExperience"})
@SuppressWarnings("ClassWithoutLogger")
public class TLActivity implements Serializable {   

    private static final long serialVersionUID = 1L;
    public static final String PROP_NAME = "name";
    public static final String PROP_LEARNING_EXPERIENCE = "learning_experience";
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
    
    public TLActivity(String name) {
	this();
	this.name = name;
    }

    @XmlElement
    void setLearningType(LearningType learningType) {
	this.learningType = learningType;
    }

    @XmlAttribute
    public void setLearningExperience(EnumeratedLearningExperience le) {
	EnumeratedLearningExperience oldValue = learningExperience;
	this.learningExperience = le;
	propertySupport.firePropertyChange(PROP_LEARNING_EXPERIENCE, oldValue, learningExperience);
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
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(propertyName, listener);
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

    @XmlElement
    void setStudentTeacherInteraction(StudentTeacherInteraction studentTeacherInteraction) {
	this.studentTeacherInteraction = studentTeacherInteraction;
    }
    
    public int getMaximumGroupSize() {
	return maximumGroupSize;
    }
    
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
