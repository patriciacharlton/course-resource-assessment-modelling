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
 * $Date$
 * $Revision$
 * This is a utility class to manage the library of TLAs
 * @author bernard
 */
public class LibraryManager {
    private static final Logger LOGGER = Logger.getLogger(LibraryManager.class.getName());

    /**
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
