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

package uk.ac.lkl.cram.ui.undo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.undo.AbstractUndoableEdit;

/**
 * Pluggable class to represent a change to the property of an object. Relies
 * on introspection to get the old value of the property and to create a setter 
 * that will undo the change. Need to provide
 * the object whose property has been changed, the name of the property and the new value.
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
@SuppressWarnings("serial")
public class PluggableUndoableEdit extends AbstractUndoableEdit {
    private static final Logger LOGGER = Logger.getLogger(PluggableUndoableEdit.class.getName());

    private final Object object;
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;
    private final Method setter;
    
    /**
     * Create a new instance of the undoable edit from the parameters supplied.
     * @param object the object that has changed
     * @param property the name of the property that changed
     * @param newValue the new value of the property
     * @throws IntrospectionException
     */
    public PluggableUndoableEdit(Object object, String property, Object newValue) throws IntrospectionException {
	super();
	this.object = object;
	this.propertyName = property;
	this.newValue = newValue;
	PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
	Method getter = pd.getReadMethod();
	try {
	    this.oldValue = getter.invoke(object);
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
	    LOGGER.log(Level.SEVERE, "Failed to get old value of property named {0} from {1}", new Object[]{propertyName, object});
	    throw new IntrospectionException(propertyName);
	}
	setter = pd.getWriteMethod();
	
    }
    
    @Override
    public String getPresentationName() {
	return propertyName;
    }
    
    @Override
    public void undo() {
	super.undo();
	//Set the old value of the property
	try {
	    setter.invoke(object, oldValue);
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
	    LOGGER.log(Level.SEVERE, "Failed to undo setting " + propertyName + " of " + object + " to " + newValue, ex);
	}
    }
    
    @Override
    public void redo() {
	super.redo();
	try {
	    setter.invoke(object, newValue);
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
	    LOGGER.log(Level.SEVERE, "Failed to redo setting " + propertyName + " of " + object + " to " + newValue, ex);
	}
    }

}
