package uk.ac.lkl.cram.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import uk.ac.lkl.cram.model.Module;

/**
 * Utility class to import a module from a file, into which a module has previously
 * been serialised.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class ModuleImporter {

    private final File moduleFile;

    /**
     * Create a new instance of the module importer using the
     * parameter as the name for the file into which a module
     * has been serialised.
     * @param filename the name of the file containing a serialised module
     * @see #ModuleImporter(File)
     */
    public ModuleImporter(String filename) {
        moduleFile = new File(filename);
    }

    /**
     * Create a new instance of the module importer using the
     * parameter as the file into which a module
     * has been serialised.
     * @param file the name of the file containing a serialised module
     * @see #ModuleImporter(String)
     */
    public ModuleImporter(File file) {
        moduleFile = file;
    }

    /**
     * Read the contents of the file with which this ModuleImporter was 
     * constructed to return an instance of a module. Closes the file, so this
     * is a one shot operation, and this moduleimporter should not be reused.
     * @return the module imported from the file
     * @throws IOException if there are any IO errors
     * @throws ClassNotFoundException if the module cannot be instantiated.
     */
    @SuppressWarnings("ConvertToTryWithResources")
    public Module importModule() throws IOException, ClassNotFoundException {
        FileInputStream inStream = new FileInputStream(moduleFile);
        ObjectInputStream inObject = new ObjectInputStream(inStream);
        Module m = (Module) inObject.readObject();
        inObject.close();
        inStream.close();
        return m;
    }
}
