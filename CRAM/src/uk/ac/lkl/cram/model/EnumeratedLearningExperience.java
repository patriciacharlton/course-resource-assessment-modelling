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

import javax.xml.bind.annotation.XmlEnum;



/**
 * This represents the kind of learning experience provided by a teaching-learning
 * activity.
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
@XmlEnum(String.class)
public enum EnumeratedLearningExperience {

    /**
     * All students experience the same learning experience
     */
    ONE_SIZE_FOR_ALL,
    /**
     * Students have a personalised learning experience
     */
    PERSONALISED,
    /**
     * Student have a social learning experience (e.g. as part of a tutor group)
     */
    SOCIAL;
}
