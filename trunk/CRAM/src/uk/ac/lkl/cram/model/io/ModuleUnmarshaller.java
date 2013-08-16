package uk.ac.lkl.cram.model.io;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;

/**
 *
 * @author bernard
 */
public class ModuleUnmarshaller {
    private final File moduleFile;
    
    public ModuleUnmarshaller(String filename) {
        moduleFile = new File(filename);
    }
    
    public ModuleUnmarshaller(File file) {
        moduleFile = file;
    }
    
    public Module unmarshallModule() throws IOException, JAXBException {
	JAXBContext context = JAXBContext.newInstance(Module.class, PreparationTime.class, SupportTime.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Module) unmarshaller.unmarshal(moduleFile);
    }
}
