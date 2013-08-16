package uk.ac.lkl.cram.model;

import javax.xml.bind.annotation.XmlEnum;



@XmlEnum(String.class)
public enum EnumeratedLearningExperience {

    ONE_SIZE_FOR_ALL, PERSONALISED, SOCIAL;
}
