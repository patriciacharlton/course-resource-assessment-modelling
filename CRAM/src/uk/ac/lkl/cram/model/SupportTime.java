package uk.ac.lkl.cram.model;

/**
 * Class to represent the amount of time taken to prepare for a teaching-learning
 * activity or module activity for a particular module presentation ('run').
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class SupportTime extends AbstractModuleTime {

    SupportTime() {
	this(0f, 0f, 0); //Default for support is that senior rate is 0%
    }

    SupportTime(float weekly, float non_weekly, int seniorRate) {
	super(weekly, non_weekly, seniorRate);
    }

    /**
     * Class to represent the amount of time taken to support a teaching-learning
     * activity or module activity for a particular module presentation ('run').
     * @param module the module containing the line item
     * @param modulePresentation the module presentation for which the line item is run
     * @param li the line item describing the activity
     * @return the total support hours for the line item
     */
    public float getTotalHours(Module module, ModulePresentation modulePresentation, LineItem li) {
	float numberOfIndividuals_Groups = li.getNumberOfIndividuals_Groups(modulePresentation, module);
	if (numberOfIndividuals_Groups > 0) {
	    return (li.getWeekCount(module) * weekly + non_weekly) * numberOfIndividuals_Groups;
	} else {
	    return non_weekly;
	}
    }

    @Override
    public float getTotalCost(Module module, ModulePresentation modulePresentation, LineItem lineItem) {
	float senior = seniorRate/100f;
	return (((senior * getTotalHours(module, modulePresentation, lineItem) * modulePresentation.getSeniorCost()) + ((1 - senior) * getTotalHours(module, modulePresentation, lineItem) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }
}
