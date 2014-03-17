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
package uk.ac.lkl.cram.model.io;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.TLALibrary;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;

/**
 * This is a utility class to manage the library of TLAs
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
public class LibraryManager {
    private static final Logger LOGGER = Logger.getLogger(LibraryManager.class.getName());

    /**
     * Main method to either report contents of the library, 
     * or import the TLAs from the module file specified by the
     * argument.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TLALibrary library = TLALibrary.getDefaultLibrary();
        switch (args.length) {
            case 0:
                reportLibrary(library);
                break;
            case 1:
                importModule(library, args[0]);
                break;
        }
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void reportLibrary(TLALibrary library) {
        System.out.println("Library Contains:");               
        for (TLActivity tLActivity : library.getActivities()) {
            System.out.println(tLActivity.getName());
        }
        System.out.println("Library size: " + library.getActivities().size());
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void importModule(TLALibrary library, String string) {
        System.out.println("Adding Activities to Library");
        Set<TLActivity> existingActivities = library.getActivities();
        try {
            Module m = new ModuleUnmarshaller(string).unmarshallModule();
            for (TLALineItem tLALineItem : m.getTLALineItems()) {
                TLActivity activity = tLALineItem.getActivity();
                if (existingActivities.contains(activity)) {
                    System.out.println("Library already contains " + activity.getName());
                } else {
                    System.out.println("Adding activity: " + activity.getName());
                    library.addActivity(activity);
                }
            }
            System.out.println("Exporting default library");
            library.exportDefaultLibrary();
            System.out.println("Library successfully exported");
            reportLibrary(library);
        } catch (IOException | JAXBException ex) {
            LOGGER.log(Level.SEVERE, "Failed to import module", ex);
        }
    }
}
