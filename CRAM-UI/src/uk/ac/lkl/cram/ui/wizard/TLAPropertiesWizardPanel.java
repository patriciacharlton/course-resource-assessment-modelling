package uk.ac.lkl.cram.ui.wizard;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.jfree.chart.JFreeChart;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.TLALearningTypeChartFactory;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class TLAPropertiesWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(TLAPropertiesWizardPanel.class.getName());

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private TLAPropertiesVisualPanel component;
    private final TLActivity tla;

    TLAPropertiesWizardPanel(TLActivity activity) {
	this.tla = activity;
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public TLAPropertiesVisualPanel getComponent() {
	if (component == null) {
	    component = new TLAPropertiesVisualPanel(tla);
	}
	return component;
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
	return true;
	// If it depends on some condition (form filled out...) and
	// this condition changes (last form field filled in...) then
	// use ChangeSupport to implement add/removeChangeListener below.
	// WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
	// use wiz.getProperty to retrieve previous panel state
	JFreeChart chart = TLALearningTypeChartFactory.createChart(tla);
	BufferedImage image = chart.createBufferedImage(TLACreatorWizardIterator.LEFT_WIDTH, TLACreatorWizardIterator.LEFT_WIDTH, 300, 300, null);
	wiz.putProperty(WizardDescriptor.PROP_IMAGE, image);
        wiz.putProperty(WizardDescriptor.PROP_INFO_MESSAGE, getInfoMessage());
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
	// use wiz.putProperty to remember current panel state
    }

    private String getInfoMessage() {
        StringBuilder builder = new StringBuilder();
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
        return builder.toString();
    }
}
