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
