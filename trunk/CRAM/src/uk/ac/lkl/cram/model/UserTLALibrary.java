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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the user's library of TLAs that are stored in the 
 * user's preferences via the Java preferences mechanism. The class relies on 
 * a singleton to manage the library. 
 * @see TLActivity
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
@XmlRootElement(name = "library")
public class UserTLALibrary {

    private static final Logger LOGGER = Logger.getLogger(TLALibrary.class.getName());
    /**
     * Max piece length is 3/4 max string length (see Preferences
     * documentation).
     */
    static private final int PIECE_LENGTH = (3 * Preferences.MAX_VALUE_LENGTH) / 4;
    /**
     * Key to be used in preferences
     */
    static private final String LIBRARY_KEY = "userTLALibrary";
    /**
     * Singleton
     */
    static private UserTLALibrary DEFAULT_LIBRARY = null;
    /**
     * Preferences use by all instances of this class
     */
    private final static Preferences PREFS = Preferences.userNodeForPackage(UserTLALibrary.class);

    /**
     * Main method to report contents of the library, for testing purposes only
     * @param args the command line arguments, ignored
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args) {
        UserTLALibrary library = getDefaultLibrary();
	Set<TLActivity> activities = library.getActivities();
	System.out.println("TLAs in User Preferences: " + activities.size());
	for (TLActivity tLActivity : activities) {
	    System.out.println(tLActivity.getName());
	}
    }    
    
    /**
     * Return the default library of TLAs that has been read from the user's preferences.     *
     * @return the instance of the library
     */
    public static UserTLALibrary getDefaultLibrary() {
	if (DEFAULT_LIBRARY == null) {
	    try {
		DEFAULT_LIBRARY = load();
	    } catch (BackingStoreException | JAXBException ex) {
		LOGGER.log(Level.WARNING, "Failed to load default library, returning empty library", ex);
		DEFAULT_LIBRARY = new UserTLALibrary();
	    }
	}
	return DEFAULT_LIBRARY;
    }

    static void clearDefaultLibrary() {
	DEFAULT_LIBRARY = null;
    }
    
    static void clearPreferences() throws BackingStoreException {
	Preferences node = PREFS.node(LIBRARY_KEY);
	node.clear();
	node.flush();
    }
    
    private static UserTLALibrary toObject(String xmlString) throws JAXBException {
	JAXBContext context = JAXBContext.newInstance(UserTLALibrary.class);
	Unmarshaller unmarshaller = context.createUnmarshaller();
	StringReader sr = new StringReader(xmlString);
	return (UserTLALibrary) unmarshaller.unmarshal(sr);
    }

    private static UserTLALibrary load() throws BackingStoreException, JAXBException {
	String[] pieces = readPieces();
	String raw = combinePieces(pieces);
	return toObject(raw);
    }

    private static String[] readPieces() throws BackingStoreException {
	Preferences node = PREFS.node(LIBRARY_KEY);
	String keys[] = node.keys();
	String[] pieces = new String[keys.length];
	for (int i = 0; i < keys.length; i++) {
	    String key = keys[i];
	    pieces[i] = node.get(key, "");
	}
	return pieces;
    }

    private static String combinePieces(String[] pieces) {
        //Guesstimate of initial size of builder
	StringBuilder sb = new StringBuilder(pieces.length * PIECE_LENGTH);
	for (String string : pieces) {
	    sb.append(string);
	}
	return sb.toString();
    }
    
    @XmlElementWrapper(name = "activities")
    @XmlElement(name = "activity")
    private Set<TLActivity> activities = new HashSet<>();

    /**
     * Default constructor
     */
    public UserTLALibrary() {
    }

    /**
     * Return the set of teaching-learning activities in this library
     *
     * @return the set of teaching-learning activities
     */
    public Set<TLActivity> getActivities() {
	return Collections.unmodifiableSet(activities);
    }

    /**
     * Add an activity to the library, this also commits it to the user's
     * preferences.
     *
     * @param activity the activity to be added to the library
     */
    public void addActivity(TLActivity activity) {
	activities.add(activity);
	commit();
    }
    
    /**
     * Commit the activities to the user preferences.
     */
    public void commit() {
	try {
	    String raw = toXML();
	    String[] pieces = breakIntoPieces(raw);
	    writePieces(pieces);
	} catch (JAXBException | BackingStoreException ex) {
	    LOGGER.log(Level.SEVERE, "Failed to store preferences", ex);
	}
    }

    private String toXML() throws JAXBException {
	JAXBContext context = JAXBContext.newInstance(UserTLALibrary.class);
	Marshaller marshaller = context.createMarshaller();
	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	StringWriter sw = new StringWriter();
	marshaller.marshal(this, sw);
	return sw.toString();
    }

    private String[] breakIntoPieces(String s) {
	int numPieces = (s.length() + PIECE_LENGTH - 1) / PIECE_LENGTH;
	String[] pieces = new String[numPieces];
	for (int i = 0; i < numPieces; ++i) {
	    int startChar = i * PIECE_LENGTH;
	    int endChar = startChar + PIECE_LENGTH;
	    if (endChar > s.length()) {
		endChar = s.length();
	    }
	    pieces[i] = s.substring(startChar, endChar);
	}
	return pieces;
    }

    private void writePieces(String[] pieces) throws BackingStoreException {
	Preferences node = PREFS.node(LIBRARY_KEY);
	node.clear();
	for (int i = 0; i < pieces.length; ++i) {
	    node.put(String.valueOf(i), pieces[i]);
	}
	node.flush();
    }
}
