package uk.ac.lkl.cram.model;

import java.io.Serializable;
import uk.ac.lkl.cram.model.calculations.Calculable;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public interface LineItem extends Serializable, Calculable {

    public SupportTime getSupportTime(ModulePresentation mp);

    public PreparationTime getPreparationTime(ModulePresentation mp);

    public void setSupportTime(ModulePresentation mp, SupportTime st);

    public String getName();

    public int getWeekCount(Module m);

    public float getNumberOfIndividuals_Groups(ModulePresentation modulePresentation, Module module);
}
