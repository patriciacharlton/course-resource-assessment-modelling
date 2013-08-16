package uk.ac.lkl.cram.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import uk.ac.lkl.cram.model.Module;

/**
 *
 * @author bernard
 */
public class ModuleImporter {

    private final File moduleFile;

    public ModuleImporter(String filename) {
        moduleFile = new File(filename);
    }

    public ModuleImporter(File file) {
        moduleFile = file;
    }

    public Module importModule() throws IOException, ClassNotFoundException {
        Module m = null;
        FileInputStream inStream = new FileInputStream(moduleFile);
        ObjectInputStream inObject = new ObjectInputStream(inStream);
        m = (Module) inObject.readObject();
        inObject.close();
        inStream.close();
        return m;
    }
}
