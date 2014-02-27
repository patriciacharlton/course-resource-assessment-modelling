package uk.ac.lkl.cram.model;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class PreparationTime extends AbstractModuleTime {

    PreparationTime() {
	this(0f, 0f, 100); //Default for prep is that senior rate is 100%
    }
    
    PreparationTime(float weekly, float non_weekly, int seniorRate) {
	super(weekly, non_weekly, seniorRate);
    }

    public float getTotalHours(Module m, LineItem li) {
	return li.getWeekCount(m) * weekly + non_weekly;
    }

    public float getTotalCost(Module m, ModulePresentation modulePresentation, LineItem li) {
	float senior = seniorRate / 100f;
	return (((senior * getTotalHours(m, li) * modulePresentation.getSeniorCost()) + ((1 - senior) * getTotalHours(m, li) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }
}
