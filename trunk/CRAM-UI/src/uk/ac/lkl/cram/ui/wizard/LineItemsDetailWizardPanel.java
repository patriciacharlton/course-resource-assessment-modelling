package uk.ac.lkl.cram.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;

public class LineItemsDetailWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(LineItemsDetailWizardPanel.class.getName());

    private boolean isValid = false;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private LineItemsDetailVisualPanel component;
    private TLALineItem lineItem;
    private Module module;

    LineItemsDetailWizardPanel(Module module, TLALineItem lineItem) {
	this.module = module;
	this.lineItem = lineItem;
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public LineItemsDetailVisualPanel getComponent() {
	if (component == null) {
	    component = new LineItemsDetailVisualPanel(module, lineItem);
	    component.addPropertyChangeListener(new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent pce) {
		    if (pce.getPropertyName().equals(LineItemsDetailVisualPanel.PROP_VALID)) {
			setValid((Boolean)pce.getNewValue());
		    }
		}
	    });
	}
	return component;
    }

    private void setValid(boolean b) {
	if (b != isValid) {
	    isValid = b;
	    changeSupport.fireChange();
	}
    }
    
    @Override
    public HelpCtx getHelp() {
	// Show no Help button for this panel:
	return HelpCtx.DEFAULT_HELP;
	// If you have context help:
	// return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
	// If it is always OK to press Next or Finish, then:
	return isValid;
	// If it depends on some condition (form filled out...) and
	// this condition changes (last form field filled in...) then
	// use ChangeSupport to implement add/removeChangeListener below.
	// WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
	changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
	changeSupport.removeChangeListener(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
	// use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
	// use wiz.putProperty to remember current panel state
    }
}
