package uk.ac.lkl.cram.model;

/**
 * $Date$
 * @author Bernard Horan
 */
public class SupportTime extends AbstractModuleTime {

    private static final long serialVersionUID = 1L;

    SupportTime() {
	this(0f, 0f, 0f); //Default for support is that senior rate is 0%
    }
    
    SupportTime(float f, float g, float h) {
	super(f,g,h);
    }

    float getTotalHours(Module module, ModulePresentation modulePresentation, TLALineItem lineItem) {
	float numberOfIndividuals_Groups = modulePresentation.getNumberOfIndividuals_Groups(module, lineItem);
	if (numberOfIndividuals_Groups > 0) {
	    return (module.getWeekCount() * weekly + non_weekly) * numberOfIndividuals_Groups;
	} else {
	    return non_weekly;
	}
    }

    float getTotalHours(Module module, ModulePresentation modulePresentation) {
	float numberOfIndividuals_Groups = modulePresentation.getNumberOfIndividuals_Groups(module);
	if (numberOfIndividuals_Groups > 0) {
	    return (module.getWeekCount() * weekly + non_weekly) * numberOfIndividuals_Groups;
	} else {
	    return non_weekly;
	}
    }

    float getCost(Module module, ModulePresentation modulePresentation, TLALineItem lineItem) {
	return (((seniorRate * getTotalHours(module, modulePresentation, lineItem) * modulePresentation.getSeniorCost()) + ((1 - seniorRate) * getTotalHours(module, modulePresentation, lineItem) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }

    float getCost(Module module, ModulePresentation modulePresentation, ModuleLineItem moduleItem) {
	return (((seniorRate * getTotalHours(module, modulePresentation) * modulePresentation.getSeniorCost()) + ((1 - seniorRate) * getTotalHours(module, modulePresentation) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }

}
