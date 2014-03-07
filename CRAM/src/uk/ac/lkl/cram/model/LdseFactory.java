package uk.ac.lkl.cram.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import uk.ac.lkl.cram.ldse.Tla;
import uk.ac.lkl.cram.ldse.Tlas;

/**
 * This class creates CRAM TLAs from LDSE TLA, but not particularly successfully because
 * the LDSE TLAs have different properties, so there is little commonality.
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
public class LdseFactory {
    private static final Logger LOGGER = Logger.getLogger(LdseFactory.class.getName());
    
    public static Collection<TLActivity> createTLActivities() {
	List<Tla> ldse_tlas = new ArrayList<Tla>();
	try {
	    JAXBContext jc = JAXBContext.newInstance("uk.ac.lkl.cram.ldse");
	    Unmarshaller um = jc.createUnmarshaller();
	    Tlas tlas = (Tlas) um.unmarshal(LdseFactory.class.getResourceAsStream("tlas.xml"));
	    ldse_tlas = tlas.getTla();
	} catch (JAXBException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	} 
	for (Tla ldse_tla : ldse_tlas) {
	    System.out.println(ldse_tla.getName());
	}
	Set<TLActivity> cram_tlas = new HashSet<TLActivity>();
	for (Tla ldse_tla : ldse_tlas) {
	    TLActivity cram_tla = convert(ldse_tla);
	    if (cram_tla != null) {
		cram_tlas.add(cram_tla);
	    }
	}
	return cram_tlas;
    }

    private static TLActivity convert(Tla ldse_tla) {
	TLActivity cram_tla = new TLActivity(ldse_tla.getName());
	try {
	    LearningType lt = new LearningType(ldse_tla.getAcquisition().intValue(), ldse_tla.getInquiry().intValue(), ldse_tla.getDiscussion().intValue(), ldse_tla.getPractice().intValue(), ldse_tla.getProduction().intValue());

	    cram_tla.setLearningType(lt);
	    String ldse_learning_experience = ldse_tla.getLearningExperience();
	    EnumeratedLearningExperience ele = EnumeratedLearningExperience.ONE_SIZE_FOR_ALL;
	    if (ldse_learning_experience.equalsIgnoreCase(EnumeratedLearningExperience.PERSONALISED.name())) {
		ele = EnumeratedLearningExperience.PERSONALISED;
	    }
	    if (ldse_learning_experience.equalsIgnoreCase(EnumeratedLearningExperience.SOCIAL.name())) {
		ele = EnumeratedLearningExperience.SOCIAL;
	    }
	    cram_tla.setLearningExperience(ele);
	    StudentTeacherInteraction sti = new StudentTeacherInteraction();
	    sti.setOnline(true);
	    cram_tla.setStudentTeacherInteraction(sti);
	} catch (Exception e) {
	    LOGGER.log(Level.SEVERE, "Failed for " + ldse_tla.getName(), e);
	    return null;
	}
	return cram_tla;
    }
    
    public static void main(String[] args) {
	    Collection<TLActivity> activities = LdseFactory.createTLActivities();
	    for (TLActivity tLActivity : activities) {
	    LOGGER.info(tLActivity.getName());
	}
	}
    
}
