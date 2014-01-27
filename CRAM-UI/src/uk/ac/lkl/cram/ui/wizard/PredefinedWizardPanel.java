package uk.ac.lkl.cram.ui.wizard;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.chart.JFreeChart;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.ONE_SIZE_FOR_ALL;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.PERSONALISED;
import static uk.ac.lkl.cram.model.EnumeratedLearningExperience.SOCIAL;
import static uk.ac.lkl.cram.model.LearnerFeedback.PEER_ONLY;
import static uk.ac.lkl.cram.model.LearnerFeedback.TEL;
import static uk.ac.lkl.cram.model.LearnerFeedback.TUTOR;
import uk.ac.lkl.cram.model.StudentTeacherInteraction;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.TLALearningTypeChartFactory;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class PredefinedWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(PredefinedWizardPanel.class.getName());
    private boolean isValid = false;
    private ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private PredefinedVisualPanel component;
    private TLActivity selectedTLA;
    private final TLALineItem lineItem;
    private final WizardDescriptor wizardDesc;

    PredefinedWizardPanel(TLALineItem lineItem, WizardDescriptor wizardDesc) {
	this.lineItem = lineItem;
	this.wizardDesc = wizardDesc;
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public PredefinedVisualPanel getComponent() {
	if (component == null) {
	    component = new PredefinedVisualPanel();
	    component.getActivityList().addListSelectionListener(new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent lse) {
		    if (!lse.getValueIsAdjusting()) {
			JList list = (JList) lse.getSource();
			setSelectedTLA((TLActivity) list.getSelectedValue());
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
    
    private void setSelectedTLA(TLActivity selectedValue) {
	selectedTLA = selectedValue;
	lineItem.setActivity(selectedTLA);
	setValid(selectedTLA != null);
	BufferedImage image = TLACreatorWizardIterator.EMPTY_IMAGE;
	if (selectedTLA != null) {
	    JFreeChart chart = TLALearningTypeChartFactory.createChart(selectedTLA);
	    image = chart.createBufferedImage(TLACreatorWizardIterator.LEFT_WIDTH, TLACreatorWizardIterator.LEFT_WIDTH, 300, 300, null);
	}
	wizardDesc.putProperty(WizardDescriptor.PROP_IMAGE, image);
	if (selectedTLA == null) {
	    getComponent().setTLAInfo("");
	} else {
	    getComponent().setTLAInfo(getInfoMessage(selectedTLA));
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
    
    private String getInfoMessage(TLActivity tla) {
        StringBuilder builder = new StringBuilder();
        //Learning experience
        switch (tla.getLearningExperience()) {
            case ONE_SIZE_FOR_ALL:
                builder.append("Same for All");
                break;
            case PERSONALISED:
                builder.append("Personalised");
                break;
            case SOCIAL:
                builder.append("Social (size: ");
		builder.append(tla.getMaximumGroupSize());
		builder.append(")");
                break;
        }
        
        //Student interaction
        StudentTeacherInteraction sti = tla.getStudentTeacherInteraction();
        if (sti.isOnline()) {
            builder.append(", ");
            builder.append("Online");
        }
        if (sti.isLocationSpecific()) {
            builder.append(", ");
            builder.append("Location-specific");
        }
        if (sti.isTimeSpecific()) {
            builder.append(", ");
            builder.append("Time-specific");
        }
        if (sti.isTutorSupported()) {
            builder.append(", ");
            builder.append("Tutor-present");
        }
        //Student Feedback      
        switch (tla.getLearnerFeedback()) {
            case PEER_ONLY:
                builder.append(", ");
                builder.append("Peer feedback");
                break;
            case TEL:
                builder.append(", ");
                builder.append("Computer-based feedback");
                break;
            case TUTOR:
                builder.append(", ");
                builder.append("Tutor feedback");
                break;
        }
        return builder.toString();
    }
}
