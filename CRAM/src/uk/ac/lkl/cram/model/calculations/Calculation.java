package uk.ac.lkl.cram.model.calculations;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernard Horan, bernard@essex.ac.uk
 */
public class Calculation extends Observable implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(Calculation.class.getName());
    static {
	LOGGER.setLevel(Level.WARNING);
    }
    
    private final Object receiver;
    private final Method method;
    private final Object[] arguments;
    
    
    public Calculation(Object receiver, String methodName) throws NoSuchMethodException {
	this.receiver = receiver;
	if (receiver instanceof Calculable) {
	    Calculable c = (Calculable) receiver;
	    LOGGER.info("adding property change listener to receiver:"+ c);
	    c.addPropertyChangeListener(this);
	} else {
	    LOGGER.warning("Receiver not calculable: " + receiver);
	}
	this.arguments = new Object[0];
	Class<?>[] classes = new Class[0];
	method = receiver.getClass().getMethod(methodName, classes);
	LOGGER.info("method: " + method);
    }
    
    public Calculation(Object receiver, String methodName, Object... arguments) throws NoSuchMethodException {
	this.receiver = receiver;
	if (receiver instanceof Calculable) {
	    Calculable c = (Calculable) receiver;
	    LOGGER.info("adding property change listener to receiver:"+ c);
	    c.addPropertyChangeListener(this);
	} else {
	    LOGGER.warning("Receiver not calculable: " + receiver);
	}
	this.arguments = arguments;
	for (Object object : arguments) {
	    if (object instanceof Calculable) {
		Calculable c = (Calculable) object;
		LOGGER.info("adding property change listener to argument:"+ c);
		c.addPropertyChangeListener(this);
	    } else {
		LOGGER.warning("Argument not calculable: " + object);
	    }
	}
	Class<?>[] classes = new Class[arguments.length];
	for (int i = 0; i < arguments.length; i++) {
	    Object object = arguments[i];
	    classes[i] = object.getClass();
	}
	method = receiver.getClass().getMethod(methodName, classes);
	LOGGER.info("method: " + method);
    }

    public Number getValue() {
	LOGGER.info("Attempting to invoke method: " + method + " on receiver: " + receiver + " with arguments: " + arguments);
	try {
	    return (Number) method.invoke(receiver, arguments);
	} catch (IllegalAccessException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	} catch (IllegalArgumentException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	} catch (InvocationTargetException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return 0;
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
	notifyObservers();
    }

}
