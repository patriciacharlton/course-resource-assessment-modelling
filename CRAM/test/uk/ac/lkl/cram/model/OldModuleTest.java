package uk.ac.lkl.cram.model;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.lkl.cram.model.io.ModuleExporter;
import uk.ac.lkl.cram.model.io.ModuleImporter;

public class OldModuleTest {

    Module m;

    @Before
    public void setUp() throws Exception {
        m = AELMTest.populateModule();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws IOException {
        File moduleFile = File.createTempFile("CRAMModel", null);
        ModuleExporter exporter = new ModuleExporter(moduleFile);
        try {
            exporter.exportModule(m);
        } catch (IOException i) {
            i.printStackTrace();
        }
        Module testModule = null;
        ModuleImporter importer = new ModuleImporter(moduleFile);
        try {
            testModule = importer.importModule();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
	assertEquals(m.getTotalCreditHourCount(), testModule.getTotalCreditHourCount());
	assertEquals(m.getModuleName(), testModule.getModuleName());
	assertEquals(m.getTutorGroupSize(), testModule.getTutorGroupSize());
        //This will fail, because class moduleItem and TLAItem use an identitymap
	assertEquals(m, testModule);
    }
}
