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

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;

/**
 * Utility class to export a module as a marshalled object into an XML file.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class ModuleMarshaller {
    private final File moduleFile;
    
    /**
     * Create a new instance of the module marshaller using the
     * parameter as the name for the file into which the module
     * will be marshalled.
     * @param filename the name of the file into which to serialise a module
     * @see #ModuleMarshaller(File)
     */
    public ModuleMarshaller(String filename) {
        moduleFile = new File(filename);
    }
    
    /**
     *Create a new instance of the module marshaller using the
     * parameter as the file into which the module
     * will be marshalled.
     * @param file the name of the file into which to serialise a module
     * @see #ModuleMarshaller(File)
     */
    public ModuleMarshaller(File file) {
        moduleFile = file;
    }
    
    /**
     * Marshall a module into the file that has already been opened
     * by this marshaller. This also closes the file, so cannot be used more than
     * once per marshaller.
     * @param m the module to be exported
     * @throws JAXBException errors marshalling the module to the file
     */
    public void marshallModule(Module m) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Module.class, PreparationTime.class, SupportTime.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //Write to file
        marshaller.marshal(m, moduleFile);
    }
    
}
