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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uk.ac.lkl.cram.model.io.ModuleExporter;
import uk.ac.lkl.cram.model.io.ModuleMarshaller;

/**
 * Example class with main() method to produce example Module document.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class ExampleTest {

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
	Module module = new Module("Example");
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
	return module;
    }

    @SuppressWarnings("deprecation")
    protected static void addTutoredDiscussionOnline(Module module) {
	TLActivity tla = new TLActivity("Tutored discussion online");
	tla.setLearningType(new LearningType(10, 20, 70, 0, 0));
	tla.setLearningExperience(EnumeratedLearningExperience.SOCIAL);
	tla.setMaximumGroupSize(5);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(true);
	sti.setLocationSpecific(false);
	sti.setTimeSpecific(true);
	sti.setTutorSupported(true);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.TUTOR);
	TLALineItem li = new TLALineItem(tla, 3.3f, 10, 0f);
	li.setPreparationTime(module.getPresentationOne(), new PreparationTime(20f, 30f, 100));
	li.setPreparationTime(module.getPresentationTwo(), new PreparationTime(5f, 2f, 100));
	li.setPreparationTime(module.getPresentationThree(), new PreparationTime(0.5f, 0, 100));
	li.setSupportTime(module.getPresentationOne(), new SupportTime(2.5f, 3f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(2.5f, 2f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(2.5f, 1f, 0));
	module.addTLALineItem(li);
    }

    @SuppressWarnings("deprecation")
    protected static void addReadingOnlineAndOffline(Module module) {
	TLActivity tla = new TLActivity("Reading online and offline");
	tla.setLearningType(new LearningType(100, 0, 0, 0, 0));
	tla.setLearningExperience(EnumeratedLearningExperience.ONE_SIZE_FOR_ALL);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(true);
	sti.setLocationSpecific(false);
	sti.setTimeSpecific(false);
	sti.setTutorSupported(false);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.NONE);
	TLALineItem li = new TLALineItem(tla, 6.7f, 12, 0f);
	li.setPreparationTime(module.getPresentationOne(), new PreparationTime(20f, 10f, 100));
	li.setPreparationTime(module.getPresentationTwo(), new PreparationTime(3f, 2f, 100));
	li.setPreparationTime(module.getPresentationThree(), new PreparationTime(0.5f, 0, 100));
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 27f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 10f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 5f, 0));
	module.addTLALineItem(li);
    }

    @SuppressWarnings("deprecation")
    protected static void addFormativePractice(Module module) {
	TLActivity tla = new TLActivity("Formative practice");
	tla.setLearningType(new LearningType(0, 0, 0, 50, 50));
	tla.setLearningExperience(EnumeratedLearningExperience.PERSONALISED);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(false);
	sti.setLocationSpecific(false);
	sti.setTimeSpecific(false);
	sti.setTutorSupported(true);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.TUTOR);
	TLALineItem li = new TLALineItem(tla, 0, 12, 22f);
	li.setPreparationTime(module.getPresentationOne(), new PreparationTime(0f, 8f, 100));
	li.setPreparationTime(module.getPresentationTwo(), new PreparationTime(0f, 2f, 100));
	li.setPreparationTime(module.getPresentationThree(), new PreparationTime(0f, 0.5f, 100));
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 1f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 1f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 1f, 0));
	module.addTLALineItem(li);
    }

    @SuppressWarnings("deprecation")
    protected static void addSummativeAssessment(Module module) {
	TLActivity tla = new TLActivity("Summative Assessment");
	tla.setLearningType(new LearningType(0, 0, 0, 0, 100));
	tla.setLearningExperience(EnumeratedLearningExperience.PERSONALISED);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(false);
	sti.setLocationSpecific(false);
	sti.setTimeSpecific(false);
	sti.setTutorSupported(true);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.TUTOR);
	TLALineItem li = new TLALineItem(tla, 0, 12, 23f);
	li.setPreparationTime(module.getPresentationOne(), new PreparationTime(0f, 12f, 100));
	li.setPreparationTime(module.getPresentationTwo(), new PreparationTime(0f, 2f, 100));
	li.setPreparationTime(module.getPresentationThree(), new PreparationTime(0f, 0.5f, 100));
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 1.5f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 1.5f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 1.5f, 0));
	module.addTLALineItem(li);
    }

    @SuppressWarnings("deprecation")
    protected static void addBuildingUpOwnNotes(Module module) {
	TLActivity tla = new TLActivity("Building up own notes");
	tla.setLearningType(new LearningType(0, 40, 0, 60, 0));
	tla.setLearningExperience(EnumeratedLearningExperience.ONE_SIZE_FOR_ALL);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(false);
	sti.setLocationSpecific(false);
	sti.setTimeSpecific(false);
	sti.setTutorSupported(false);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.NONE);
	TLALineItem li = new TLALineItem(tla, 3.3f, 12, 0);
	li.setPreparationTime(module.getPresentationOne(), new PreparationTime(0f, 1f, 100));
	li.setPreparationTime(module.getPresentationTwo(), new PreparationTime(0f, 0f, 100));
	li.setPreparationTime(module.getPresentationThree(), new PreparationTime(0f, 0f, 100));
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 0f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 0f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 0f, 0));
	module.addTLALineItem(li);
    }

    @SuppressWarnings("deprecation")
    protected static void addExploringResources(Module module) {
	TLActivity tla = new TLActivity("Exploring resources");
	tla.setLearningType(new LearningType(30, 70, 0, 0, 0));
	tla.setLearningExperience(EnumeratedLearningExperience.ONE_SIZE_FOR_ALL);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(true);
	sti.setLocationSpecific(false);
	sti.setTimeSpecific(false);
	sti.setTutorSupported(false);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.NONE);
	TLALineItem li = new TLALineItem(tla, 2.1f, 12, 0);
	li.setPreparationTime(module.getPresentationOne(), new PreparationTime(0f, 1f, 100));
	li.setPreparationTime(module.getPresentationTwo(), new PreparationTime(0f, 0f, 100));
	li.setPreparationTime(module.getPresentationThree(), new PreparationTime(0f, 0f, 100));
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 0f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 0f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 0f, 0));
	module.addTLALineItem(li);
    }

    @SuppressWarnings("deprecation")
    protected static void addApplicationOfConcept(Module module) {
	TLActivity tla = new TLActivity("Application of Concept");
	tla.setLearningType(new LearningType(0, 0, 0, 100, 0));
	tla.setLearningExperience(EnumeratedLearningExperience.ONE_SIZE_FOR_ALL);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(false);
	sti.setLocationSpecific(false);
	sti.setTimeSpecific(false);
	sti.setTutorSupported(false);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.NONE);
	TLALineItem li = new TLALineItem(tla, 5f, 12, 0);
	li.setPreparationTime(module.getPresentationOne(), new PreparationTime(0f, 1f, 100));
	li.setPreparationTime(module.getPresentationTwo(), new PreparationTime(0f, 0f, 100));
	li.setPreparationTime(module.getPresentationThree(), new PreparationTime(0f, 0f, 100));
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 0f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 0f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 0f, 0));
	module.addTLALineItem(li);
    }

    @SuppressWarnings("deprecation")
    protected static void addPersonalTuition(Module module) {
	TLActivity tla = new TLActivity("Personal Tuition");
	tla.setLearningType(new LearningType(0, 0, 100, 0, 0));
	tla.setLearningExperience(EnumeratedLearningExperience.PERSONALISED);
	StudentTeacherInteraction sti = new StudentTeacherInteraction();
	sti.setOnline(false);
	sti.setLocationSpecific(true);
	sti.setTimeSpecific(true);
	sti.setTutorSupported(true);
	tla.setStudentTeacherInteraction(sti);
	tla.setLearnerFeedback(LearnerFeedback.TUTOR);
	TLALineItem li = new TLALineItem(tla, 0f, 12, 1.5f);
	li.setSupportTime(module.getPresentationOne(), new SupportTime(0f, 1.5f, 0));
	li.setSupportTime(module.getPresentationTwo(), new SupportTime(0f, 1.5f, 0));
	li.setSupportTime(module.getPresentationThree(), new SupportTime(0f, 1.5f, 0));
	module.addTLALineItem(li);
    }

    public static void runReport(Module module) {
	reportActivity(module);
	reportPreparations(module);
	reportSupport(module);
	reportLearningTypes(module);
	reportLearningExperience(module);
	reportCosts(module);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void reportActivity(Module module) {
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    TLActivity tla = lineItem.getActivity();
	    System.out.print(tla.getName() + '\t');
	    System.out.print(lineItem.getWeeklyLearnerHourCount() + "; ");
	    System.out.print(lineItem.getNonWeeklyLearnerHourCount() + "; ");
	    System.out.print(lineItem.getWeekCount(module) + "; ");
	    System.out.print(lineItem.getTotalLearnerHourCount(module) + "; ");
	    System.out.println(tla.getLearnerFeedback());
	}
	System.out.println("Self regulated learning:\t" + module.getSelfRegulatedLearningHourCount());
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void reportPreparations(Module module) {
	System.out.println("\n1st run: preparation hours");
	reportPresentationPreparation(module, module.getPresentationOne());
	System.out.println("\n2nd run: preparation hours");
	reportPresentationPreparation(module, module.getPresentationTwo());
	System.out.println("\n3rd run: preparation hours");
	reportPresentationPreparation(module, module.getPresentationThree());
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void reportSupport(Module module) {
	System.out.println("\n1st run: Individual tutor time to support the work of each group or individual");
	reportPresentationSupport(module, module.getPresentationOne());
	System.out.println("\n2nd run: Individual tutor time to support the work of each group or individual");
	reportPresentationSupport(module, module.getPresentationTwo());
	System.out.println("\n3rd run: Individual tutor time to support the work of each group or individual");
	reportPresentationSupport(module, module.getPresentationThree());
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void reportPresentationPreparation(Module module, ModulePresentation modulePresentation) {
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    TLActivity tla = lineItem.getActivity();
	    PreparationTime pt = lineItem.getPreparationTime(modulePresentation);
	    if (pt != null) {
		System.out.print(tla.getName() + '\t');
		System.out.print(pt.getWeekly() + "; ");
		System.out.print(pt.getNonWeekly() + "; ");
		System.out.print(pt.getTotalHours(module, lineItem) + "; ");
		System.out.print(DecimalFormat.getPercentInstance().format(pt.getSeniorRate()) + "; ");
		System.out.println(DecimalFormat.getCurrencyInstance().format(pt.getTotalCost(module, modulePresentation, lineItem)));
	    }
	}
	System.out.print("\t" + module.getTotalPreparationHours(modulePresentation));
	System.out.println("\t" + DecimalFormat.getCurrencyInstance().format(module.getTotalPreparationCost(modulePresentation)));
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void reportPresentationSupport(Module module, ModulePresentation modulePresentation) {
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    TLActivity tla = lineItem.getActivity();
	    SupportTime st = lineItem.getSupportTime(modulePresentation);
	    System.out.print(tla.getName() + '\t');
	    System.out.print(lineItem.getNumberOfIndividuals_Groups(modulePresentation, module) + "; ");
	    System.out.print(st.getWeekly() + "; ");
	    System.out.print(st.getNonWeekly() + "; ");
	    System.out.print(st.getTotalHours(module, modulePresentation, lineItem) + "; ");
	    System.out.print(DecimalFormat.getPercentInstance().format(st.getSeniorRate()) + "; ");
	    System.out.println(DecimalFormat.getCurrencyInstance().format(st.getTotalCost(module, modulePresentation, lineItem)));
	}

	for (ModuleLineItem moduleItem : module.getModuleItems()) {
	    SupportTime st = moduleItem.getSupportTime(modulePresentation);
	    System.out.print(moduleItem.getName() + '\t');
	    System.out.print(moduleItem.getNumberOfIndividuals_Groups(modulePresentation, module) + "; ");
	    System.out.print(st.getWeekly() + "; ");
	    System.out.print(st.getNonWeekly() + "; ");
	    System.out.print(st.getTotalHours(module, modulePresentation, moduleItem) + "; ");
	    System.out.print(DecimalFormat.getPercentInstance().format(st.getSeniorRate()) + "; ");
	    System.out.println(DecimalFormat.getCurrencyInstance().format(st.getTotalCost(module, modulePresentation, moduleItem)));
	}
	System.out.print("\t" + module.getTotalSupportHours(modulePresentation));
	System.out.println("\t" + DecimalFormat.getCurrencyInstance().format(module.getTotalSupportCost(modulePresentation)));
    }

    @SuppressWarnings({"AssignmentReplaceableWithOperatorAssignment", "UseOfSystemOutOrSystemErr"})
    public static void reportLearningTypes(Module module) {
	float acquisition = 0, inquiry = 0, discussion = 0, practice = 0, production = 0;
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    TLActivity tla = lineItem.getActivity();
	    LearningType lt = tla.getLearningType();
	    float total = lineItem.getTotalLearnerHourCount(module);
	    acquisition = acquisition + (total * lt.getAcquisition());
	    inquiry = inquiry + (total * lt.getInquiry());
	    discussion = discussion + (total * lt.getDiscussion());
	    practice = practice + (total * lt.getPractice());
	    production = production + (total * lt.getProduction());
	}
	System.out.println("\nLearning Types:\t\t" + acquisition + "\t" + inquiry + "\t" + discussion + "\t" + practice + "\t" + production);
    }

    @SuppressWarnings({"AssignmentReplaceableWithOperatorAssignment", "UseOfSystemOutOrSystemErr"})
    public static void reportLearningExperience(Module module) {
	float personalised = 0, social = 0, oneSizeForAll = 0;
	for (TLALineItem lineItem : module.getTLALineItems()) {
	    TLActivity tla = lineItem.getActivity();
	    float total = lineItem.getTotalLearnerHourCount(module);
	    switch (tla.getLearningExperience()) {
		case PERSONALISED: {
		    personalised = personalised + (total * 100);
		    break;
		}
		case SOCIAL: {
		    social = social + (total * 100);
		    break;
		}
		case ONE_SIZE_FOR_ALL: {
		    oneSizeForAll = oneSizeForAll + (total * 100);
		    break;
		}
	    }
	}
	System.out.println("\nLearning Experience:\t" + personalised + "\t" + social + "\t" + oneSizeForAll);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void reportCosts(Module module) {
	NumberFormat formatter = DecimalFormat.getCurrencyInstance();
	formatter.setMaximumFractionDigits(0);
	formatter.setRoundingMode(RoundingMode.UP);
	System.out.println("\n\t\t\t1st Run\t\t2nd Run\t\tStable State");
	Collection<ModulePresentation> presentations = module.getModulePresentations();
	System.out.print("Student Nos.");
	for (ModulePresentation mp : presentations) {
	    System.out.print("\t\t" + mp.getTotalStudentCount());
	}
	System.out.print("\nSupport hrs");
	for (ModulePresentation mp : presentations) {
	    System.out.print("\t\t" + module.getTotalSupportHours(mp));
	}
	System.out.print("\nPrep hrs");
	for (ModulePresentation mp : presentations) {
	    System.out.print("\t\t" + module.getTotalPreparationHours(mp));
	}
	System.out.print("\nTotal hrs");
	for (ModulePresentation mp : presentations) {
	    System.out.print("\t\t" + module.getTotalHours(mp));
	}
	System.out.print("\nIncome\t");
	for (ModulePresentation mp : presentations) {
	    System.out.print("\t\t" + formatter.format(mp.getIncome()));
	}
	System.out.print("\nCost\t");
	for (ModulePresentation mp : presentations) {
	    System.out.print("\t\t" + formatter.format(module.getTotalCost(mp)));
	}
	System.out.print("\nProfit\t\t");
	for (ModulePresentation mp : presentations) {
	    System.out.print("\t" + formatter.format(mp.getIncome() - module.getTotalCost(mp)));
	}
    }

    protected static void export(Module m) {
	ModuleExporter exporter = new ModuleExporter("Example.mam");
	try {
	    exporter.exportModule(m);
	} catch (IOException i) {
	    Logger.getLogger(ExampleTest.class.getName()).log(Level.SEVERE, null, i);
	}
    }

    protected static void encode(Module m) {
	ModuleMarshaller encoder = new ModuleMarshaller("Example.mamx");
	try {
	    encoder.marshallModule(m);
	} catch (JAXBException ex) {
	    Logger.getLogger(ExampleTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }
}
