/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.lkl.cram.model;

import java.io.Serializable;

/**
 * An interface that represents a line item in the module. A line item follows
 * the pattern of a shopping trolley, in which the line item describes the amount
 * or quantity of the product purchased, not the product itself.
 * @see TLALineItem
 * @see ModuleLineItem
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public interface LineItem extends Serializable {

    /**
     * Return the amount of time required to support a module presentation (run)
     * @param mp the module presentation for which the return value describes the amount of time
     * @return the amount of time required to support a module presentation (run) 
     */
    public SupportTime getSupportTime(ModulePresentation mp);

    /**
     * Return the amount of time required to prepare a module presentation (run)
     * @param mp the module presentation for which the return value describes the amount of time
     * @return the amount of time required to prepare a module presentation (run) 
     */
    public PreparationTime getPreparationTime(ModulePresentation mp);

    /**
     * Set the support time for a module presentation
     * @param mp the module presentation 
     * @param st the support time that corresponds to the module presentation
     */
    public void setSupportTime(ModulePresentation mp, SupportTime st);

    /**
     * The (humanly-readable) name of the line item
     * @return a string describing the name of the line item
     */
    public String getName();

    /**
     * return the number of weeks that the teaching-learning or module activity runs for
     * @param m the module of which the line item is a part
     * @return the number of weeks that the teaching-learning or module activity runs for
     */
    public int getWeekCount(Module m);

    /**
     * Return the number of individuals or groups that are required by this line
     * item to perform the activity for a particular module presentation ('run')
     * If the line item represents a teaching-learning activity, this will be the number of tutor groups. 
     * If the line item represents a module activity, this will be the number of tutors necessary
     * @param modulePresentation the 'run' that the line item is part of
     * @param module the module that the line item is part of
     * @return the number of groups/individuals necessary to run the activity for the module
     */
    public float getNumberOfIndividuals_Groups(ModulePresentation modulePresentation, Module module);

    /**
     * Remove the line item from a module
     * @param m the module from which the line item should be removed. Double dispatches
     * based on type of implementing class.
     * @see TLALineItem#removeFrom(Module) 
     * @see ModuleLineItem#removeFrom(Module) 
     */
    public void removeFrom(Module m);
}
