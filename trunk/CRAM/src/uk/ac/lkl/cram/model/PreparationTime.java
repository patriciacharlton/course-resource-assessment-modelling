package uk.ac.lkl.cram.model;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public class PreparationTime extends AbstractModuleTime {

    PreparationTime() {
	this(0f, 0f, 1f); //Default for prep is that senior rate is 100%
    }
    
    PreparationTime(float weekly, float non_weekly, float seniorRate) {
	super(weekly, non_weekly, seniorRate);
    }

    public float getTotalHours(LineItem li) {
	return li.getWeekCount() * weekly + non_weekly;
    }

    public float getTotalCost(LineItem li, ModulePresentation modulePresentation) {
	return (((seniorRate * getTotalHours(li) * modulePresentation.getSeniorCost()) + ((1 - seniorRate) * getTotalHours(li) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }
}
