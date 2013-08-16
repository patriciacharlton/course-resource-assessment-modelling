package uk.ac.lkl.cram.ui.wizard;

import java.awt.Dialog;
import java.text.MessageFormat;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALineItem;

/**
 *
 * @author Bernard Horan
 */
public class TLACreator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Module module = new Module();
	TLACreatorWizardIterator iterator = new TLACreatorWizardIterator(module);
	WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
	iterator.initialize(wizardDescriptor);
	// {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
	// {1} will be replaced by WizardDescriptor.Iterator.name()
	wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
	wizardDescriptor.setTitle("TLA Creator Wizard");
	Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
	    TLALineItem lineItem = iterator.getLineItem();
	    module.addTLALineItem(lineItem);
	    System.out.println("Learning Types:\t\t" + lineItem.getActivity().getLearningType());
	    System.out.println("Learning Experience: " + lineItem.getActivity().getLearningExperience());
	    System.out.println("Student Feedback: " + lineItem.getActivity().getLearnerFeedback());
	    System.out.println("Student Teacher Interaction: " + lineItem.getActivity().getStudentTeacherInteraction());
	    AELMTest.reportActivity(module);
	    AELMTest.reportPreparations(module);
	    AELMTest.reportSupport(module);
        }
	
    }
}
