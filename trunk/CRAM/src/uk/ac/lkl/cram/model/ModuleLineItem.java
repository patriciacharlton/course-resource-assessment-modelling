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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.lkl.cram.model.xml.XmlGenericMapAdapter;

/**
 * This class represents a module activity. There is no underlying 'product'
 * because a module activity only has a name and no other structure.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@XmlType(propOrder = {"name", "supportMap"})
@SuppressWarnings({"ClassWithoutLogger", "serial"})
public class ModuleLineItem implements LineItem {
    /**
     * Property to indicate that the name of the modulelineitem has changed
     * @see ModuleLineItem#setName(String) 
     */
    public static final String PROP_NAME = "name";
    private String name;
    @XmlJavaTypeAdapter(XmlGenericMapAdapter.class)
    @XmlElement(name = "supportMap")
    private Map<ModulePresentation, SupportTime> supportMap = new HashMap<>();
    
    private final transient PropertyChangeSupport propertySupport;

    /**
     * Default constructor creates a module line item with a null name.
     */
    public ModuleLineItem() {
	propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * Create a  module line item with the given name
     * @param name the name for the new module line item
     */
    public ModuleLineItem(String name) {
        this();
        this.name = name;
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
   
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the module line item.
     * @param name the name of the module line item
     * @see ModuleLineItem#PROP_NAME
     */
    @XmlAttribute
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertySupport.firePropertyChange(PROP_NAME, oldName, name);
    }

    /**
     * Get the number of weeks the activity runs for
     *
     * @param m the module of which the module line item is a part
     * @return the number of weeks the activity runs for
     */
    @Override
    public int getWeekCount(Module m) {
	return m.getWeekCount();
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 13 * hash + Objects.hashCode(this.supportMap);
	hash = 13 * hash + Objects.hashCode(this.name);
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
	return true;
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

    //A module line item has no preparation time, so override to return a
    //preparation time with zero hours and zero cost
    @Override
    public PreparationTime getPreparationTime(ModulePresentation mp) {
	return new PreparationTime() {
	    @Override
	    public float getTotalCost(Module m, ModulePresentation modulePresentation, LineItem li) {
		return 0;
	    }

	    @Override
	    public float getTotalHours(Module m, LineItem li) {
		return 0;
	    }
	};
    }

    @Override
    public float getNumberOfIndividuals_Groups(ModulePresentation modulePresentation, Module module) {
	float i = ((float) modulePresentation.getTotalStudentCount() / (float) module.getTutorGroupSize());
	return (int) (i + 0.99);
    }

    @Override
    public void removeFrom(Module m) {
        m.removeModuleItem(this);
    }
    
}
