package uk.ac.lkl.cram.model;

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
 * This class represents a CRAM module, and is the outermost container of
 * the data that describes a module.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@XmlRootElement(name = "module")
@XmlType(propOrder = {"moduleName", "totalCreditHourCount", "weekCount", "tutorGroupSize", "tlaLineItems", "moduleLineItems", "presentations"})
@SuppressWarnings("serial")
public class Module implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(Module.class.getName());

    /**
     * Property to indicate when the contents of the list of
     * TLA Line Items has been updated.
     * @see TLALineItem
     * @see Module#addTLALineItem(TLALineItem) 
     * @see LineItem#removeFrom(Module) 
     */
    public static final String PROP_TLA_LINEITEM = "tlaLineItem";
    /**
     * Property to indicate when the contents of the list of
     * Module Line Items has been updated.
     * @see ModuleLineItem
     * @see Module#addModuleItem(ModuleLineItem)
     * @see LineItem#removeFrom(Module) 
     */
    public static final String PROP_MODULE_LINEITEM = "moduleLineItem";
    /**
     * Property to indicate when the name of the module
     * has been updated.
     * @see Module#setModuleName(java.lang.String) 
     */
    public static final String PROP_NAME = "name";
    /**
     * Property to indicate when the number of credit hours for 
     * the module has been updated.
     * @see Module#setTotalCreditHourCount(int) 
     */
    public static final String PROP_HOUR_COUNT = "hour_count";
    /**
     * Property to indicate when the number of weeks for 
     * the module has been updated.
     * @see Module#setWeekCount(int)
     */
    public static final String PROP_WEEK_COUNT = "week_count";
    /**
     * Property to indicate when the tutor group size for
     * the module has been updated.
     * @see Module#setTutorGroupSize(int) 
     */
    public static final String PROP_GROUP_SIZE = "group_size";

    @XmlElementWrapper(name = "tlaLineItems")
    @XmlElement(name = "tlaLineItem")
    private List<TLALineItem> tlaLineItems = new ArrayList<>();
    
    @XmlElementWrapper(name = "moduleLineItems")
    @XmlElement(name = "moduleLineItem")
    private List<ModuleLineItem> moduleLineItems = new ArrayList<>();
    
    private int totalCreditHourCount;
    
    private String moduleName;
    //This is the number of weeks the module runs
    //Some activities may run less than the number of weeks
    private int weekCount;
    //This is the size of the tutor group for the module
    //This has an impact only on the module contributions line items
    //E.g. if there is a cohort of 40 students, with a tutor group size
    //of 20, then there will be two tutor groups, and thus 
    //two lots of tutor group contributions
    private int tutorGroupSize;
    
    @XmlElementWrapper(name = "presentations")
    @XmlElement(name = "modulePresentation")
    private ModulePresentation[] presentations = new ModulePresentation[3];
    
    private final transient PropertyChangeSupport propertySupport;

    /**
     * Create a new Module with no name.
     */
    public Module() {
	propertySupport = new PropertyChangeSupport(this);
	moduleName = "";
	presentations[0] = new ModulePresentation(Run.FIRST);
	presentations[1] = new ModulePresentation(Run.SECOND);
	presentations[2] = new ModulePresentation(Run.THIRD);
    }

    /**
     * Create a new Module with the specified name.
     * @param name name of the module.
     */
    public Module(String name) {
	this();
	moduleName = name;
    }

    /**
     * Add a TTLALine item to the list of tlaLineItems for this module.
     * @param lineItem the new TLALineItem to be added.
     * @see TLALineItem
     */
    public void addTLALineItem(TLALineItem lineItem) {
	tlaLineItems.add(lineItem);
	int index = tlaLineItems.indexOf(lineItem);
	propertySupport.fireIndexedPropertyChange(PROP_TLA_LINEITEM, index, null, lineItem);
    }
    
    void removeTLALineItem(TLALineItem  li) {
        int i = tlaLineItems.indexOf(li);
	tlaLineItems.remove(i);
        propertySupport.fireIndexedPropertyChange(PROP_TLA_LINEITEM, i, li, null);
    }
    
    void removeModuleItem(ModuleLineItem moduleItem) {
        int i = moduleLineItems.indexOf(moduleItem);
        moduleLineItems.remove(i);
        propertySupport.fireIndexedPropertyChange(PROP_MODULE_LINEITEM, i, moduleItem, null);
    }
    
    /**
     * Remove a lineItem from the module.<p>
     * This double dispatches based on the class of the lineItem
     * @param li the line item to be removed
     * @see LineItem#removeFrom(Module) 
     * 
     */
    public void removeLineItem(LineItem li) {
        //Double dispatch
        li.removeFrom(this);
    }

    /**
     * Return the list of TLALineItems managed by this module
     * @return a List of TLALineItems
     * @see TLALineItem
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<TLALineItem> getTLALineItems() {
	return tlaLineItems;
    }

    /**
     * Return the name of this module
     * @return the module name
     */
    public String getModuleName() {
	return moduleName;
    }

    /**
     * Return the number of credit hours for this module
     * @return the number of credit hours
     */
    public int getTotalCreditHourCount() {
	return totalCreditHourCount;
    }

    /**
     * return the number of weeks for which this module runs
     * @return the number of weeks for which this module runs
     */
    public int getWeekCount() {
	return weekCount;
    }

    /**
     * Set the number of weeks for which this module runs
     * @param i the number of weeks for which this module runs
     * @see Module#PROP_WEEK_COUNT
     */
    @XmlAttribute
    public void setWeekCount(int i) {
	int oldValue = weekCount;
	this.weekCount = i;
	propertySupport.firePropertyChange(PROP_WEEK_COUNT, oldValue, weekCount);
    }

    /**
     * Set the tutor group size for this module
     * @param i the tutor group size for this module
     * @see Module#PROP_GROUP_SIZE
     */
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

    /**
     * Return the number of hours of self-regulated learning<p>
     * Calculated by subtracting the total number of learner hours spent in the 
     * TLAs from the number of credit hours
     * @return the number of hours for self-regulated learning
     */
    @SuppressWarnings("AssignmentReplaceableWithOperatorAssignment")
    public float getSelfRegulatedLearningHourCount() {
	float totalHourCount = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    totalHourCount = totalHourCount + lineItem.getTotalLearnerHourCount(this);
	}
	return totalCreditHourCount - totalHourCount;
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

    /**
     * Add a module item to the list of module items
     * @param mi a module item
     * @see Module#PROP_MODULE_LINEITEM
     */
    public void addModuleItem(ModuleLineItem mi) {
	moduleLineItems.add(mi);
	int index = moduleLineItems.indexOf(mi);
	propertySupport.fireIndexedPropertyChange(PROP_MODULE_LINEITEM, index, null, mi);
    }

    /**
     * Return the list of module items
     * @return the list of module items
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<ModuleLineItem> getModuleItems() {
	return moduleLineItems;
    }

    /**
     * The size of the tutor group
     * @return the size of the tutor group
     */
    public int getTutorGroupSize() {
	return tutorGroupSize;
    }

    /**
     * Return the total number of hours spent in preparation for all the
     * TLAs for the specified module presentation (or 'run')
     * @param modulePresentation the presentation (or 'run') for which the number of presentation hours is required
     * @return the number of presentation hours spent on all the TLAs for the specified run
     */
    @SuppressWarnings("AssignmentReplaceableWithOperatorAssignment")
    public float getTotalPreparationHours(ModulePresentation modulePresentation) {
	float totalHours = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
	    if (pt != null) {
		totalHours = totalHours + pt.getTotalHours(this, lineItem);
	    }
	}
	return totalHours;
    }

    /**
     * Return the total cost spent in preparation for all the 
     * TLAs for the specified module presentation (or 'run')
     * @param modulePresentation the presentation (or 'run') for which the cost is required
     * @return the total cost involved in preparing for the specified run
     */
    @SuppressWarnings("AssignmentReplaceableWithOperatorAssignment")
    public float getTotalPreparationCost(ModulePresentation modulePresentation) {
	float totalCost = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
	    if (pt != null) {
		totalCost = totalCost + pt.getTotalCost(this, modulePresentation, lineItem);
	    }
	}
	return totalCost;
    }

    /**
     * Return the total number of hours spent in support for all the
     * TLAs AND module items for the specified module presentation (or 'run')
     * @param modulePresentation the presentation (or 'run') for which the number of support hours is required
     * @return the number of presentation hours spent on all the activities (TLA and module activities) for the specified run
     */
    @SuppressWarnings("AssignmentReplaceableWithOperatorAssignment")
    public float getTotalSupportHours(ModulePresentation modulePresentation) {
	float totalHours = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    SupportTime st = lineItem.getSupportTime(modulePresentation);
	    totalHours = totalHours + st.getTotalHours(this, modulePresentation, lineItem);
	}
	for (ModuleLineItem moduleItem : moduleLineItems) {
	    SupportTime st = moduleItem.getSupportTime(modulePresentation);
	    totalHours = totalHours + st.getTotalHours(this, modulePresentation, moduleItem);
	}
	return totalHours;
    }

    /**
     * Return the total cost spent in support for all the 
     * activities (TLAs and module activities) for the specified module presentation (or 'run')
     * @param modulePresentation the presentation (or 'run') for which the cost is required
     * @return the total cost involved in supporting the specified run
     */
    @SuppressWarnings("AssignmentReplaceableWithOperatorAssignment")
    public float getTotalSupportCost(ModulePresentation modulePresentation) {
	float totalCost = 0;
	for (TLALineItem lineItem : tlaLineItems) {
	    SupportTime st = lineItem.getSupportTime(modulePresentation);
	    totalCost = totalCost + st.getTotalCost(this, modulePresentation, lineItem);
	}
	for (ModuleLineItem moduleItem : moduleLineItems) {
	    SupportTime st = moduleItem.getSupportTime(modulePresentation);
	    totalCost = totalCost + st.getTotalCost(this, modulePresentation, moduleItem);
	}
	return totalCost;
    }

    /**
     * Return the list of module presentations (or 'runs'), which should contain
     * three elements.
     * @return the list of module presentations
     */
    public List<ModulePresentation> getModulePresentations() {
	return Arrays.asList(presentations);
    }

    /**
     * Return the total hours spent on a module presentation (or 'run'). This is
     * the sum of the total preparation hours and the total support hours
     * @param mp the specified module presentation (or 'run')
     * @return the total number of hours spend in supporting and preparing for this module presentation
     * @see Module#getTotalPreparationHours(ModulePresentation) 
     * @see Module#getTotalSupportHours(ModulePresentation) 
     */
    public float getTotalHours(ModulePresentation mp) {
	return getTotalPreparationHours(mp) + getTotalSupportHours(mp);
    }

    /**
     * Return the total cost for a module presentation (or 'run'). This is 
     * the sum of the total preparation cost and the total support cost.
     * @param mp the specified module presentation (or 'run')
     * @return the total cost of supporting and preparing this module presentation
     * @see Module#getTotalPreparationCost(ModulePresentation) 
     * @see Module#getTotalSupportCost(ModulePresentation) 
     */
    public float getTotalCost(ModulePresentation mp) {
	return getTotalPreparationCost(mp) + getTotalSupportCost(mp);
    }

    /**
     * Set the name of the module
     * @param text the name of the module
     * @see Module#PROP_NAME
     */
    @XmlAttribute
    public void setModuleName(String text) {
	String oldValue = moduleName;
	moduleName = text;
	propertySupport.firePropertyChange(PROP_NAME, oldValue, moduleName);
    }

    /**
     * Set the number of credit hours for the module
     * @param i the number of credit hours for the module
     * @see Module#PROP_HOUR_COUNT
     */
    @XmlAttribute
    public void setTotalCreditHourCount(int i) {
	int oldValue = totalCreditHourCount;
	totalCreditHourCount = i;
	propertySupport.firePropertyChange(PROP_HOUR_COUNT, oldValue, totalCreditHourCount);
	
    }

    /**
     * Return the list of line items (a new collection containing
     * the TLALineItems appended by the moduleItems)
     * @return a list of all the line items managed by this module
     * @see LineItem
     * @see TLALineItem
     * @see ModuleLineItem
     */
    public List<LineItem> getLineItems() {
	List<LineItem> cramItems = new ArrayList<>();
	cramItems.addAll(tlaLineItems);
	cramItems.addAll(moduleLineItems);
	return cramItems;
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

    /* 
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
	result = prime * result + totalCreditHourCount;
	result = prime * result + tutorGroupSize;
	result = prime * result + weekCount;
	return result;
    }

    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
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
	if (totalCreditHourCount != other.totalCreditHourCount) {
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
