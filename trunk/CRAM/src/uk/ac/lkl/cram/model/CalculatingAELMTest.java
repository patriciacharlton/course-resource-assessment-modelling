package uk.ac.lkl.cram.model;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.lkl.cram.model.calculations.Calculation;
import uk.ac.lkl.cram.model.calculations.Evaluator;

public class CalculatingAELMTest extends AELMTest{

	/**
	 * @param args
	 * @throws NoSuchMethodException  
	 */
	public static void main(String[] args)  {
	    Module m = populateModule();
	    runReport(m);
	    export(m);
            encode(m);
	}
        
        public static void reportActivity(Module module) {
		for (TLALineItem lineItem : module.getTLALineItems()) {
		    try {
			TLActivity tla = lineItem.getActivity();
			System.out.print(tla.getName() + '\t');
			Calculation getWeeklyLearnerHourCount = new Calculation(lineItem, "getWeeklyLearnerHourCount");
			System.out.print(getWeeklyLearnerHourCount.getValue() + "; ");
			Calculation getNonWeeklyLearnerHourCount = new Calculation(lineItem, "getNonWeeklyLearnerHourCount");
			System.out.print(getNonWeeklyLearnerHourCount.getValue() + "; ");
			Calculation getTotalLearnerHourCount = new Calculation(lineItem, "getTotalLearnerHourCount", module);
			System.out.print(getTotalLearnerHourCount.getValue() + "; ");
			System.out.println(tla.getLearnerFeedback());
		    } catch (NoSuchMethodException ex) {
			Logger.getLogger(CalculatingAELMTest.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
		System.out.println("Self regulated learning:\t" + module.getSelfRegulatedLearningHourCount());
	}

	public static void reportPresentationPreparation(final Module module, final ModulePresentation modulePresentation) {
		for (TLALineItem lineItem : module.getTLALineItems()) {
			TLActivity tla = lineItem.getActivity();
			final PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
			if (pt != null) {
				System.out.print(tla.getName() + '\t');
				System.out.print(pt.getWeekly() + "; ");
				System.out.print(pt.getNonWeekly() + "; ");
				Evaluator getTotalHours = new Evaluator(pt, module) {
				    @Override
				    protected void evaluate() {
					setValue(pt.getTotalHours(module));
				    }
				};
				System.out.print(getTotalHours.getValue() + "; ");
				System.out.print(DecimalFormat.getPercentInstance().format(pt.getSeniorRate()) + "; ");
				Evaluator getCost = new Evaluator(pt, module, modulePresentation) {

				    @Override
				    protected void evaluate() {
					setValue(pt.getCost(module, modulePresentation));
				    }
				};
				System.out.println(DecimalFormat.getCurrencyInstance().format(getCost.getValue()));
			}
		}
		System.out.print("\t" + module.getTotalPreparationHours(modulePresentation));
		System.out.println("\t" + DecimalFormat.getCurrencyInstance().format(module.getTotalPreparationCost(modulePresentation)));
	}

}
