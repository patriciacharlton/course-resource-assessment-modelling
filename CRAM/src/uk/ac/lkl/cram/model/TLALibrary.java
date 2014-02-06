package uk.ac.lkl.cram.model;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * $Date$
 * $Revision$
 * This class represents the library of TLAs
 * @author Bernard Horan
 * 
 */
@XmlRootElement(name = "library")
public class TLALibrary {
    private static String DEFAULT_LIBRARY_NAME = "TLALibrary.xml";
    private static final Logger LOGGER = Logger.getLogger(TLALibrary.class.getName());
    
    public static TLALibrary getDefaultLibrary() {
        try {
            JAXBContext context = JAXBContext.newInstance(TLALibrary.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //Get library from root of JAR
            InputStream is = TLALibrary.class.getResourceAsStream('/' + DEFAULT_LIBRARY_NAME);
            return (TLALibrary) unmarshaller.unmarshal(is);
        } catch (JAXBException | IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, "Failed to load default library, returning empty library", ex);
            return new TLALibrary();
        }
    
    }

    @XmlElementWrapper(name = "activities")
    @XmlElement(name = "activity")
    private Set<TLActivity> activities = new HashSet<>();
    
    public TLALibrary() {
        
    }
    
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public Set<TLActivity> getActivities() {
        return activities;
    }

    public void addActivity(TLActivity activity) {
        activities.add(activity);
    }

    public void exportDefaultLibrary() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(TLALibrary.class);
        Marshaller marshaller = context.createMarshaller();
        //Dump library at root of project
        File f = new File(DEFAULT_LIBRARY_NAME);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this, f);
    }
    
    
}
