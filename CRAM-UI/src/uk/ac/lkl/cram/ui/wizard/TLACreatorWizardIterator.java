package uk.ac.lkl.cram.ui.wizard;

import java.awt.Component;
import java.awt.Dimension;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.Module;

public final class TLACreatorWizardIterator implements WizardDescriptor.Iterator<WizardDescriptor> {
    private static final Logger LOGGER = Logger.getLogger(TLACreatorWizardIterator.class.getName());

    static final String PROP_VANILLA = "vanilla";
    static final String PROP_LINE_ITEM = "lineItem";
    static final String PROP_MODULE = "module";

    private int index;
    private WizardDescriptor wizardDesc;
    
    private WizardDescriptor.Panel[] allPanels;
    private WizardDescriptor.Panel[] currentPanels;
    private WizardDescriptor.Panel[] predefinedSequence;
    private WizardDescriptor.Panel[] vanillaSequence;

    private String[] predefinedIndex;
    private String[] vanillaIndex;
    private final Module module;
    private final TLALineItem lineItem;

    public TLACreatorWizardIterator(Module module) {
	this.module = module;
	this.lineItem = new TLALineItem();
    }
    
    public void initialize(WizardDescriptor wizardDescriptor) {
        wizardDesc = wizardDescriptor;
	//wizardDesc.putProperty(PROP_MODULE, module);
    }
    
    private void initializePanels() {
        int maxWidth =0, maxHeight =0;
        if (allPanels == null) {
            allPanels = new WizardDescriptor.Panel[]{
                new StartWizardPanel(),
                new PredefinedWizardPanel(lineItem),
                new TLALearningDetailsWizardPanel(lineItem.getActivity()),
		new TLAPropertiesWizardPanel(lineItem.getActivity()),
		new LineItemsDetailWizardPanel(module, lineItem)
            };
            String[] steps = new String[allPanels.length];
            for (int i = 0; i < allPanels.length; i++) {
                Component c = allPanels[i].getComponent();
                // Default step name to component name of panel.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
                    maxWidth = Math.max(maxWidth, jc.getPreferredSize().width);
                    maxHeight = Math.max(maxHeight, jc.getPreferredSize().height);
                }
            }
            Dimension preferredSize = new Dimension(maxWidth, maxHeight);
                
            for (int i = 0; i < allPanels.length; i++) {
                Component c = allPanels[i].getComponent();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.setPreferredSize(preferredSize);
                }
            }
            
            predefinedIndex = new String[]{steps[0], steps[1], steps[4]};
            predefinedSequence = new WizardDescriptor.Panel[]{allPanels[0], allPanels[1], allPanels[4]};
            
            vanillaIndex = new String[]{steps[0], steps[2], steps[3], steps[4]};
            vanillaSequence = new WizardDescriptor.Panel[]{allPanels[0], allPanels[2], allPanels[3], allPanels[4]};
            
            currentPanels = predefinedSequence;
        
        }
        
    }

    private void setVanilla(boolean vanilla) {
        String[] contentData;
        if (vanilla) {
            currentPanels = vanillaSequence;
            contentData = vanillaIndex;
        } else {
            currentPanels = predefinedSequence;
            contentData = predefinedIndex;
        }
        wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_DATA, contentData);
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
	initializePanels();
        return currentPanels[index];
    }

    @Override
    public String name() {
	if (index == 0) {
            return index + 1 + " of ...";
        }
        return index + 1 + " of " + currentPanels.length;
    }

    @Override
    public boolean hasNext() {
	initializePanels();
        return index < currentPanels.length - 1;
    }

    @Override
    public boolean hasPrevious() {
	return index > 0;
    }

    @Override
    public void nextPanel() {
	if (!hasNext()) {
	    throw new NoSuchElementException();
	}
	if (index == 0) {
	    Boolean vanilla = (Boolean) wizardDesc.getProperty(PROP_VANILLA);
	    setVanilla(vanilla);
	}
	index++;
	wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
    }

    @Override
    public void previousPanel() {
	if (!hasPrevious()) {
	    throw new NoSuchElementException();
	}
	index--;
	wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    public TLALineItem getLineItem() {
	return lineItem;
    }
}
