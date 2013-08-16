package uk.ac.lkl.cram.model;

/**
 *
 * @author Bernard Horan
 */
public class PreparationTime extends AbstractModuleTime {

    private static final long serialVersionUID = 1L;

    PreparationTime() {
	super();
    }
    
    PreparationTime(float f, float g, float h) {
	super(f, g, h);
    }

    public float getTotalHours(Module module) {
	return module.getWeekCount() * weekly + non_weekly;
    }

    public float getCost(Module module, ModulePresentation modulePresentation) {
	return (((seniorRate * getTotalHours(module) * modulePresentation.getSeniorCost()) + ((1 - seniorRate) * getTotalHours(module) * modulePresentation.getJuniorCost())) / HOURS_PER_DAY);
    }
}
