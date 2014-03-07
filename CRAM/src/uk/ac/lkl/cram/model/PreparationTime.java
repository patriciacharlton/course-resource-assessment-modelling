package uk.ac.lkl.cram.model;

/**
 * Class to represent the amount of time taken to prepare for a teaching-learning
 * activity for a particular module presentation ('run').
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class PreparationTime extends AbstractModuleTime {

    PreparationTime() {
	this(0f, 0f, 100); //Default for prep is that senior rate is 100%
    }
    
    PreparationTime(float weekly, float non_weekly, int seniorRate) {
	super(weekly, non_weekly, seniorRate);
    }

    /**
     * Return the total number of hours required to prepare for the teaching-learning
     * activity described by the line item as part of the module.
     * @param m the module containing the line item
     * @param li the line item describing the activity
     * @return the total preparation hours for the line item
     */
    public float getTotalHours(Module m, LineItem li) {
	return li.getWeekCount(m) * weekly + non_weekly;
    }

    @Override
    public float getTotalCost(Module m, ModulePresentation modulePresentation, LineItem li) {
	float senior = seniorRate / 100f;
	return (((senior * getTotalHours(m, li) * modulePresentation.getSeniorCost()) + ((1 - senior) * getTotalHours(m, li) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }
}
