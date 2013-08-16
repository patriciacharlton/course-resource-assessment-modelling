package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.lkl.cram.model.xml.XmlGenericMapAdapter;

@XmlType(propOrder = {"name", "supportMap"})
public class ModuleLineItem implements LineItem {

    private static final long serialVersionUID = 1L;
    
    @XmlJavaTypeAdapter(XmlGenericMapAdapter.class)
    @XmlElement(name = "supportMap")
    private Map<ModulePresentation, SupportTime> supportMap = new IdentityHashMap<ModulePresentation, SupportTime>();
    
    @XmlElement
    private String name;
    
    private PropertyChangeSupport propertySupport;

    public ModuleLineItem() {
	propertySupport = new PropertyChangeSupport(this);
    }

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
        return supportMap.get(mp);
    }

    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((supportMap == null) ? 0 : supportMap.hashCode());
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
        if (!(obj instanceof ModuleLineItem)) {
            return false;
        }
        ModuleLineItem other = (ModuleLineItem) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (supportMap == null) {
            if (other.supportMap != null) {
                return false;
            }
        } else {
            if (!supportMap.equals(other.supportMap)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public float getCost(SupportTime st, Module module, ModulePresentation mp) {
        return st.getCost(module, mp, this);
    }

    @Override
    public float getTotalHours(SupportTime st, Module module, ModulePresentation mp) {
	return st.getTotalHours(module, mp);
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
