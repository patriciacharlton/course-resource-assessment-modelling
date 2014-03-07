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
