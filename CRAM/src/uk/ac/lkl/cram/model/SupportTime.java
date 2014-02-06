package uk.ac.lkl.cram.model;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("serial")
public class SupportTime extends AbstractModuleTime {

    SupportTime() {
	this(0f, 0f, 0f); //Default for support is that senior rate is 0%
    }

    SupportTime(float weekly, float non_weekly, float seniorRate) {
	super(weekly, non_weekly, seniorRate);
    }

    public float getTotalHours(Module module, ModulePresentation modulePresentation, LineItem li) {
	float numberOfIndividuals_Groups = li.getNumberOfIndividuals_Groups(modulePresentation, module);
	if (numberOfIndividuals_Groups > 0) {
	    return (li.getWeekCount() * weekly + non_weekly) * numberOfIndividuals_Groups;
	} else {
	    return non_weekly;
	}
    }

    public float getTotalCost(Module module, ModulePresentation modulePresentation, LineItem lineItem) {
	return (((seniorRate * getTotalHours(module, modulePresentation, lineItem) * modulePresentation.getSeniorCost()) + ((1 - seniorRate) * getTotalHours(module, modulePresentation, lineItem) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }
}
