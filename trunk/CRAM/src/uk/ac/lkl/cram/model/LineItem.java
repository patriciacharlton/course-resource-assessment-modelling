package uk.ac.lkl.cram.model;

import uk.ac.lkl.cram.model.calculations.Calculable;
import java.io.Serializable;

/**
 * $Date$
 * @author Bernard Horan
 */
public interface LineItem extends Serializable, Calculable {

    public SupportTime getSupportTime(ModulePresentation mp);
    
    public PreparationTime getPreparationTime(ModulePresentation mp);

    public void setSupportTime(ModulePresentation mp, SupportTime st);

    public String getName();

    public float getCost(SupportTime st, Module module, ModulePresentation mp);

    public float getTotalHours(SupportTime st, Module module, ModulePresentation mp);
    
}
