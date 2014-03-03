package uk.ac.lkl.cram.model;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.junit.After;
import org.junit.Before;
import uk.ac.lkl.cram.model.io.ModuleExporter;
import uk.ac.lkl.cram.model.io.ModuleImporter;
import uk.ac.lkl.cram.model.io.ModuleMarshaller;
import uk.ac.lkl.cram.model.io.ModuleUnmarshaller;

/**
 *
 * @author bernard
 */
public abstract class CRAMTestAbstract {
    protected static final boolean USE_XML = true;
    protected Module createdModule;
    protected File moduleFile;
    protected Module importedModule;

    protected void exportModule() {
        if (USE_XML) {
            ModuleMarshaller marshaller = new ModuleMarshaller(moduleFile);
            try {
                marshaller.marshallModule(createdModule);
            } catch (JAXBException ex) {
                Logger.getLogger(ModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ModuleExporter exporter = new ModuleExporter(moduleFile);
            try {
                exporter.exportModule(createdModule);
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    protected Module importModule() {
        Module m = null;
        if (USE_XML) {
            ModuleUnmarshaller unmarshaller = new ModuleUnmarshaller(moduleFile);
            try {
                m = unmarshaller.unmarshallModule();
            } catch (JAXBException ex) {
                Logger.getLogger(ModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ModuleImporter importer = new ModuleImporter(moduleFile);
            try {
                m = importer.importModule();
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return null;
            }
        }
        return m;
    }

    @Before
    public void setUp() throws IOException {
        createdModule = AELMTest.populateModule();
        moduleFile = File.createTempFile("CRAMModel", null);
        exportModule();
        importedModule = importModule();
    }

    @After
    public void tearDown() {
    }
    
}
