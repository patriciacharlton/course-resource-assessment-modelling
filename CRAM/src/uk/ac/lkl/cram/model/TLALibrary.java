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
 * This class represents the library of TLAs. It is used to manage the 
 * pre-defined TLAs. This class does not implement a singleton, so each time
 * it is asked for the default library, it is read from disk. This avoids
 * having to clone the instances of TLActivity.<br>
 * NB. There is no interface to remove activities from the library.
 * @see TLActivity
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
@XmlRootElement(name = "library")
public class TLALibrary {
    private static String DEFAULT_LIBRARY_NAME = "TLALibrary.xml";
    private static final Logger LOGGER = Logger.getLogger(TLALibrary.class.getName());
    
    /**
     * Return the default library of TLAs that has been read from the xml file.
     * No singleton is used in this implementation, so the library is read from
     * disk each time this method is called.
     * @return the library read from disk
     */
    public static TLALibrary getDefaultLibrary() {
        try {
            JAXBContext context = JAXBContext.newInstance(TLALibrary.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //Get library from root of JAR
            InputStream is = TLALibrary.class.getResourceAsStream('/' + DEFAULT_LIBRARY_NAME);
            return (TLALibrary) unmarshaller.unmarshal(is);
        } catch (JAXBException | IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, "Failed to load default library, returning new empty library", ex);
            return new TLALibrary();
        }
    
    }

    @XmlElementWrapper(name = "activities")
    @XmlElement(name = "activity")
    private Set<TLActivity> activities = new HashSet<>();
    
    /**
     * Default constructor
     */
    public TLALibrary() {
        
    }
    
    /**
     * Return the set of teaching-learning activities in this library
     * @return the set of teaching-learning activities
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public Set<TLActivity> getActivities() {
        return activities;
    }

    /**
     * Add an activity to the library
     * @param activity the activity to be added to the library
     */
    public void addActivity(TLActivity activity) {
        activities.add(activity);
    }

    /**
     * Export the library to the XML file.
     * @throws JAXBException
     */
    public void exportDefaultLibrary() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(TLALibrary.class);
        Marshaller marshaller = context.createMarshaller();
        //Dump library at root of project
        File f = new File(DEFAULT_LIBRARY_NAME);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this, f);
    }
    
    
}
