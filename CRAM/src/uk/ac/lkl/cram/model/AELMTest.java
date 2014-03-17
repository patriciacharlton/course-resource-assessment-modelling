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
package uk.ac.lkl.cram.model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uk.ac.lkl.cram.model.io.ModuleExporter;
import uk.ac.lkl.cram.model.io.ModuleMarshaller;

/**
 * Example class with main() method to create module document. Based on AELM
 * module description.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class AELMTest extends ExampleTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
	Module m = populateModule();
	runReport(m);
	export(m);
	encode(m);
    }

    public static Module populateModule() {
	Module module = new Module("AELM");
	module.setTotalCreditHourCount(300);
	module.setWeekCount(12);
	module.setTutorGroupSize(15);
	module.setPresentationOne(new ModulePresentation(Run.FIRST, 10, 500, 5, 1000, 182, 450));
	module.setPresentationTwo(new ModulePresentation(Run.SECOND, 10, 500, 10, 1000, 182, 450));
	module.setPresentationThree(new ModulePresentation(Run.THIRD, 10, 500, 10, 1000, 182, 450));
	addTutoredDiscussionOnline(module);
	addReadingOnlineAndOffline(module);
	addFormativePractice(module);
	addSummativeAssessment(module);
	addBuildingUpOwnNotes(module);
	addExploringResources(module);
	addApplicationOfConcept(module);
	addPersonalTuition(module);
	addModuleContributions(module);
	return module;
    }

    private static void addModuleContributions(Module module) {
	ModuleLineItem li = new ModuleLineItem("Module Contributions");
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 21.5f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 21.5f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 21.5f, 0));
	module.addModuleItem(li);
    }

    protected static void export(Module m) {
	ModuleExporter exporter = new ModuleExporter("AELM.mam");
	try {
	    exporter.exportModule(m);
	} catch (IOException i) {
	    Logger.getLogger(AELMTest.class.getName()).log(Level.SEVERE, null, i);
	}
    }

    protected static void encode(Module m) {
	ModuleMarshaller encoder = new ModuleMarshaller("AELM.mamx");
	try {
	    encoder.marshallModule(m);
	} catch (JAXBException ex) {
	    Logger.getLogger(AELMTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }
}
