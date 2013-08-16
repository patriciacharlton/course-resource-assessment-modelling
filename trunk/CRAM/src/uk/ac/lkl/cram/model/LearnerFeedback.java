package uk.ac.lkl.cram.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 *
 * @author Bernard Horan
 */
@XmlEnum(String.class)
public enum LearnerFeedback {
   
    TUTOR, PEER_ONLY, TEL, NONE;
    
}
