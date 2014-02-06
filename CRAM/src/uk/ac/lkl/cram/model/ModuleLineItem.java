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
@XmlType(propOrder = {"name", "weekCount", "supportMap"})
@SuppressWarnings({"ClassWithoutLogger", "serial"})
public class ModuleLineItem implements LineItem {

    @XmlJavaTypeAdapter(XmlGenericMapAdapter.class)
    @XmlElement(name = "supportMap")
    private Map<ModulePresentation, SupportTime> supportMap = new HashMap<>();
    
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private int weekCount;
    
    private PropertyChangeSupport propertySupport;

    public ModuleLineItem() {
	propertySupport = new PropertyChangeSupport(this);
    }

    public ModuleLineItem(String name, int weekCount) {
        this();
        this.name = name;
	this.weekCount = weekCount;
    }

    @Override
    public void setSupportTime(ModulePresentation mp, SupportTime st) {
        supportMap.put(mp, st);
    }

    @Override
    public SupportTime getSupportTime(ModulePresentation mp) {
        return supportMap.get(mp);
    }

    @Override
    public String getName() {
        return name;
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

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 13 * hash + Objects.hashCode(this.supportMap);
	hash = 13 * hash + Objects.hashCode(this.name);
	hash = 13 * hash + this.weekCount;
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
	final ModuleLineItem other = (ModuleLineItem) obj;
	if (!Objects.equals(this.supportMap, other.supportMap)) {
	    return false;
	}
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	if (this.weekCount != other.weekCount) {
	    return false;
	}
	return true;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    @Override
    public PreparationTime getPreparationTime(ModulePresentation mp) {
	return new PreparationTime() {
	    @Override
	    public float getTotalCost(LineItem li, ModulePresentation modulePresentation) {
		return 0;
	    }

	    @Override
	    public float getTotalHours(LineItem li) {
		return 0;
	    }
	};
    }

    @Override
    public float getNumberOfIndividuals_Groups(ModulePresentation modulePresentation, Module module) {
	float i = ((float) modulePresentation.getTotalStudentCount() / (float) module.getTutorGroupSize());
	return (int) (i + 0.99);
    }
    
}
