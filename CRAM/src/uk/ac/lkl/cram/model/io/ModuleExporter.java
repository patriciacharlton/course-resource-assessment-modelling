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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import uk.ac.lkl.cram.model.Module;

/**
 * Utility class to export a module as a serialised object.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class ModuleExporter {
    private final File moduleFile;
    
    /**
     * Create a new instance of the module exporter using the
     * parameter as the name for the file into which the module
     * will be serialised.
     * @param filename the name of the file into which to serialise a module
     * @see #ModuleExporter(File)
     */
    public ModuleExporter(String filename) {
        moduleFile = new File(filename);
    }
    
    /**
     * Create a new instance of the module exporter using the
     * parameter as the file into which the module
     * will be serialised.
     * @param file the file into which to serialise a module
     * @see #ModuleExporter(String)
     */
    public ModuleExporter(File file) {
        moduleFile = file;
    }
    
    /**
     * Serialise a module into the file that has already been opened
     * by this exporter. This also closes the file, so cannot be used more than
     * once per exporter.
     * @param m the module to be exported
     * @throws IOException thrown if there are any IO errors
     */
    @SuppressWarnings("ConvertToTryWithResources")
    public void exportModule(Module m) throws IOException {
	FileOutputStream outStream = new FileOutputStream(moduleFile);
        ObjectOutputStream outObject = new ObjectOutputStream(outStream);
        outObject.writeObject(m);
        outObject.close();
        outStream.close();       
    }
    
}
