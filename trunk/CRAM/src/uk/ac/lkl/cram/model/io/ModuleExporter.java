package uk.ac.lkl.cram.model.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import uk.ac.lkl.cram.model.Module;

/**
 *
 * @author bernard
 */
public class ModuleExporter {
    private final File moduleFile;
    
    public ModuleExporter(String filename) {
        moduleFile = new File(filename);
    }
    
    public ModuleExporter(File file) {
        moduleFile = file;
    }
    
    public void exportModule(Module m) throws IOException {
	FileOutputStream outStream = new FileOutputStream(moduleFile);
        ObjectOutputStream outObject = new ObjectOutputStream(outStream);
        outObject.writeObject(m);
        outObject.close();
        outStream.close();       
    }
    
}
