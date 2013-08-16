

package uk.ac.lkl.cram.model.calculations;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernard Horan
 */
public abstract class Evaluator extends Observable implements PropertyChangeListener{
    private static final Logger LOGGER = Logger.getLogger(Calculation.class.getName());
    static {
	LOGGER.setLevel(Level.WARNING);
    }

    private Number value = null;
    
    public Evaluator(Object receiver) {
	if (receiver instanceof Calculable) {
	    Calculable c = (Calculable) receiver;
	    LOGGER.info("adding property change listener to receiver:"+ c);
	    c.addPropertyChangeListener(this);
	} else {
	    LOGGER.warning("Receiver not calculable: " + receiver);
	}
	evaluate();
    }
    
    public Evaluator(Object receiver, Object... arguments) {
	if (receiver instanceof Calculable) {
	    Calculable c = (Calculable) receiver;
	    LOGGER.info("adding property change listener to receiver:" + c);
	    c.addPropertyChangeListener(this);
	}
	for (Object object : arguments) {
	    if (object instanceof Calculable) {
		Calculable c = (Calculable) object;
		LOGGER.info("adding property change listener to argument:" + c);
		c.addPropertyChangeListener(this);
	    } else {
		LOGGER.warning("Argument not calculable: " + object);
	    }
	}
	evaluate();
    }

    protected abstract void evaluate() ;
    
    public Number getValue() {
	if (value == null) {
	    evaluate();
	}
	return value;
    }
    
    protected final void setValue(Number value) {
	this.value = value;
    }
    
    @Override
    public final void propertyChange(PropertyChangeEvent pce) {
	value = null;
	notifyObservers();
    }
    
    

}
