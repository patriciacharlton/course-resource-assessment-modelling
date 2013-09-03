package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeEvent;
import uk.ac.lkl.cram.model.calculations.Calculable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * $Date$
 * @author Bernard Horan
 */
@XmlRootElement(name = "module")
@XmlType(propOrder = {"moduleName", "totalCreditHourCount", "weekCount", "tutorGroupSize", "tlaLineItems", "moduleLineItems", "presentations"})
public class Module implements Serializable, Calculable {

    private static final Logger LOGGER = Logger.getLogger(Module.class.getName());

    private static final long serialVersionUID = 1L;
    public static final String PROP_TLA_LINEITEM = "tlaLineItem";
    public static final String PROP_NAME = "name";
    public static final String PROP_HOUR_COUNT = "hour_count";
    public static final String PROP_WEEK_COUNT = "week_count";
    public static final String PROP_GROUP_SIZE = "group_size";

    @XmlElementWrapper(name = "tlaLineItems")
    @XmlElement(name = "tlaLineItem")
    private List<TLALineItem> tlaLineItems = new ArrayList<TLALineItem>();
    
    @XmlElementWrapper(name = "moduleLineItems")
    @XmlElement(name = "moduleLineItem")
    private List<ModuleLineItem> moduleLineItems = new ArrayList<ModuleLineItem>();
    
    @XmlAttribute
    private int totalCreditHours;
    
    private String moduleName;
    private int weekCount;
    private int tutorGroupSize;
    
    @XmlElementWrapper(name = "presentations")
    @XmlElement(name = "modulePresentation")
    private ModulePresentation[] presentations = new ModulePresentation[3];
    
    private PropertyChangeSupport propertySupport;

    public Module() {
	propertySupport = new PropertyChangeSupport(this);
	moduleName = "";
	presentations[0] = new ModulePresentation();
	presentations[1] = new ModulePresentation();
	presentations[2] = new ModulePresentation();
		}

    public Module(String name) {
	this();
	moduleName = name;
    }

    public void addTLALineItem(TLALineItem lineItem) {
	tlaLineItems.add(lineItem);
	int index = tlaLineItems.indexOf(lineItem);
	propertySupport.fireIndexedPropertyChange(PROP_TLA_LINEITEM, index, null, lineItem);
    }
    
    public void removeTLALineItem(TLALineItem  li) {
        int i = tlaLineItems.indexOf(li);
	tlaLineItems.remove(i);
        propertySupport.fireIndexedPropertyChange(PROP_TLA_LINEITEM, i, li, null);
    }

    public List<TLALineItem> getTLALineItems() {
	return tlaLineItems;
    }

    public String getModuleName() {
	return moduleName;
    }

    public int getTotalCreditHourCount() {
	return totalCreditHours;
    }

    public int getWeekCount() {
	return weekCount;
    }

    @XmlAttribute
    public void setWeekCount(int i) {
	int oldValue = weekCount;
	this.weekCount = i;
	propertySupport.firePropertyChange(PROP_WEEK_COUNT, oldValue, weekCount);
    }

    @XmlAttribute
    public void setTutorGroupSize(int i) {
	int oldValue = tutorGroupSize;
	this.tutorGroupSize = i;
	propertySupport.firePropertyChange(PROP_GROUP_SIZE, oldValue, tutorGroupSize);
    }

    void setPresentationOne(ModulePresentation modulePresentation) {
	presentations[0] = modulePresentation;
    }

    void setPresentationTwo(ModulePresentation modulePresentation) {
	presentations[1] = modulePresentation;
    }

    void setPresentationThree(ModulePresentation modulePresentation) {
	presentations[2] = modulePresentation;
    }

    public float getSelfRegulatedLearningHourCount() {
	float totalHourCount = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    totalHourCount = totalHourCount + lineItem.getTotalLearnerHourCount(this);
	}
	return totalCreditHours - totalHourCount;
    }

    ModulePresentation getPresentationOne() {
	return presentations[0];
    }

    ModulePresentation getPresentationTwo() {
	return presentations[1];
    }

    ModulePresentation getPresentationThree() {
	return presentations[2];
    }

    public void addModuleItem(ModuleLineItem mi) {
	moduleLineItems.add(mi);
    }

    List<ModuleLineItem> getModuleItems() {
	return moduleLineItems;
    }

    public int getTutorGroupSize() {
	return tutorGroupSize;
    }

    public float getTotalPreparationHours(ModulePresentation modulePresentation) {
	float totalHours = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
	    if (pt != null) {
		totalHours = totalHours + pt.getTotalHours(this);
	    }
	}
	return totalHours;
    }

    public float getTotalPreparationCost(ModulePresentation modulePresentation) {
	float totalHours = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
	    if (pt != null) {
		totalHours = totalHours + pt.getCost(this, modulePresentation);
	    }
	}
	return totalHours;
    }

    public float getTotalSupportHours(ModulePresentation modulePresentation) {
	float totalHours = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    SupportTime st = lineItem.getSupportTime(modulePresentation);
	    totalHours = totalHours + st.getTotalHours(this, modulePresentation, lineItem);
	}
	for (ModuleLineItem moduleItem : moduleLineItems) {
	    SupportTime st = moduleItem.getSupportTime(modulePresentation);
	    totalHours = totalHours + st.getTotalHours(this, modulePresentation);
	}
	return totalHours;
    }

    public float getTotalSupportCost(ModulePresentation modulePresentation) {
	float totalCost = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    SupportTime st = lineItem.getSupportTime(modulePresentation);
	    totalCost = totalCost + st.getCost(this, modulePresentation, lineItem);
	}
	for (ModuleLineItem moduleItem : moduleLineItems) {
	    SupportTime st = moduleItem.getSupportTime(modulePresentation);
	    totalCost = totalCost + st.getCost(this, modulePresentation, moduleItem);
	}
	return totalCost;
    }

    public List<ModulePresentation> getModulePresentations() {
	return Arrays.asList(presentations);
    }

    public float getTotalHours(ModulePresentation mp) {
	return getTotalPreparationHours(mp) + getTotalSupportHours(mp);
    }

    public float getTotalCost(ModulePresentation mp) {
	return getTotalPreparationCost(mp) + getTotalSupportCost(mp);
    }

    @XmlAttribute
    public void setModuleName(String text) {
	String oldValue = moduleName;
	moduleName = text;
	propertySupport.firePropertyChange(PROP_NAME, oldValue, moduleName);
    }

    public void setTotalCreditHourCount(int i) {
	int oldValue = totalCreditHours;
	totalCreditHours = i;
	propertySupport.firePropertyChange(PROP_HOUR_COUNT, oldValue, totalCreditHours);
	
    }

    public List<LineItem> getLineItems() {
	List<LineItem> cramItems = new ArrayList<LineItem>();
	cramItems.addAll(tlaLineItems);
	cramItems.addAll(moduleLineItems);
	return cramItems;
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(listener);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(listener);
    }
    
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	propertySupport.removePropertyChangeListener(propertyName, listener);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((moduleLineItems == null) ? 0 : moduleLineItems.hashCode());
		result = prime * result
				+ ((moduleName == null) ? 0 : moduleName.hashCode());
		result = prime * result + Arrays.hashCode(presentations);
		result = prime * result
				+ ((tlaLineItems == null) ? 0 : tlaLineItems.hashCode());
		result = prime * result + totalCreditHours;
		result = prime * result + tutorGroupSize;
		result = prime * result + weekCount;
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
		if (!(obj instanceof Module)) {
			return false;
		}
		Module other = (Module) obj;
		if (moduleLineItems == null) {
			if (other.moduleLineItems != null) {
				return false;
			}
		} else if (!moduleLineItems.equals(other.moduleLineItems)) {
			return false;
		}
		if (moduleName == null) {
			if (other.moduleName != null) {
				return false;
			}
		} else if (!moduleName.equals(other.moduleName)) {
			return false;
		}
		if (!Arrays.equals(presentations, other.presentations)) {
			return false;
		}
		if (tlaLineItems == null) {
			if (other.tlaLineItems != null) {
				return false;
			}
		} else if (!tlaLineItems.equals(other.tlaLineItems)) {
			return false;
		}
		if (totalCreditHours != other.totalCreditHours) {
			return false;
		}
		if (tutorGroupSize != other.tutorGroupSize) {
			return false;
		}
		if (weekCount != other.weekCount) {
			return false;
		}
		return true;
	}

    

    
}
