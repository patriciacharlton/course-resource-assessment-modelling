package uk.ac.lkl.cram.model.io;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;

/**
 *
 * @author bernard
 */
public class ModuleMarshaller {
    private final File moduleFile;
    
    public ModuleMarshaller(String filename) {
        moduleFile = new File(filename);
    }
    
    public ModuleMarshaller(File file) {
        moduleFile = file;
    }
    
    public void marshallModule(Module m) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Module.class, PreparationTime.class, SupportTime.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //Write to file
        marshaller.marshal(m, moduleFile);
    }
    
}
