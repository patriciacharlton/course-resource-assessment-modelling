package uk.ac.lkl.cram.model;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class SupportTime extends AbstractModuleTime {

    SupportTime() {
	this(0f, 0f, 0f); //Default for support is that senior rate is 0%
    }

    SupportTime(float weekly, float non_weekly, float seniorRate) {
	super(weekly, non_weekly, seniorRate);
    }

    public float getTotalHours(ModulePresentation modulePresentation, TLALineItem lineItem) {
	float numberOfIndividuals_Groups = modulePresentation.getNumberOfIndividuals_Groups(lineItem);
	if (numberOfIndividuals_Groups > 0) {
	    return (lineItem.getWeekCount() * weekly + non_weekly) * numberOfIndividuals_Groups;
	} else {
	    return non_weekly;
	}
    }

    float getTotalHours(Module module, ModulePresentation modulePresentation, LineItem li) {
	float numberOfIndividuals_Groups = modulePresentation.getNumberOfIndividuals_Groups(module);
	if (numberOfIndividuals_Groups > 0) {
	    return (li.getWeekCount() * weekly + non_weekly) * numberOfIndividuals_Groups;
	} else {
	    return non_weekly;
	}
    }

    float getTotalCost(ModulePresentation modulePresentation, TLALineItem lineItem) {
	return (((seniorRate * getTotalHours(modulePresentation, lineItem) * modulePresentation.getSeniorCost()) + ((1 - seniorRate) * getTotalHours(modulePresentation, lineItem) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }

    float getTotalCost(Module module, ModulePresentation modulePresentation, ModuleLineItem moduleItem) {
	return (((seniorRate * getTotalHours(module, modulePresentation, moduleItem) * modulePresentation.getSeniorCost()) + ((1 - seniorRate) * getTotalHours(module, modulePresentation, moduleItem) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }
}
