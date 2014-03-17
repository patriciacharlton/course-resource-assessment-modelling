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
