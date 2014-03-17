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
 * This class represents the kind of student feedback provided by a 
 * teaching-learning activity.
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
@XmlEnum(String.class)
public enum LearnerFeedback {
   
    /**
     * The student received feedback from a tutor
     */
    TUTOR,
    /**
     * The student receives feedback from one or more peers
     */
    PEER_ONLY,
    /**
     * The student receives computer-based feedback
     */
    TEL,
    /**
     * The student receives no feedback
     */
    NONE;
    
}
