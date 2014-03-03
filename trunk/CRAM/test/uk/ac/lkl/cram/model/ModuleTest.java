package uk.ac.lkl.cram.model;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * $Date$
 * @author Bernard Horan
 */
public class ModuleTest extends CRAMTestAbstract {

    public ModuleTest() {
    }

    /**
     * Test of getTLALineItems method, of class Module.
     */
    @Test
    public void testGetTLALineItems() {
        System.out.println("getTLALineItems");
        List<TLALineItem> createdResult = createdModule.getTLALineItems();
        List<TLALineItem> importedResult = importedModule.getTLALineItems();
        int index = 0;
        for (TLALineItem createdTLALineItem : createdResult) {
            TLALineItem importedTLALineItem = importedResult.get(index);
            index++;
            assertEquals(createdTLALineItem, importedTLALineItem);
        }      
    }

    /**
     * Test of getModuleName method, of class Module.
     */
    @Test
    public void testGetModuleName() {
        System.out.println("getModuleName");
        String expResult = createdModule.getModuleName();
        String result = importedModule.getModuleName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTotalCreditHourCount method, of class Module.
     */
    @Test
    public void testGetHourCount() {
        System.out.println("getHourCount");
        int expResult = createdModule.getTotalCreditHourCount();
        int result = importedModule.getTotalCreditHourCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWeekCount method, of class Module.
     */
    @Test
    public void testGetweekCount() {
        System.out.println("getweekCount");
        int expResult = createdModule.getWeekCount();
        int result = importedModule.getWeekCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTotalCreditHourCount method, of class Module.
     */
    @Test
    public void testSetTotalCreditHourCount() {
        System.out.println("setTotalCreditHourCount");
        int hourCount = 0;
        Module instance = new Module();
        instance.setTotalCreditHourCount(hourCount);
        assertEquals(hourCount, instance.getTotalCreditHourCount());
    }

    /**
     * Test of setWeekCount method, of class Module.
     */
    @Test
    public void testSetWeekCount() {
        System.out.println("setWeekCount");
        int weekCount = 0;
        Module instance = new Module();
        instance.setWeekCount(weekCount);
        assertEquals(weekCount, instance.getWeekCount());
    }

    /**
     * Test of setTutorGroupSize method, of class Module.
     */
    @Test
    public void testSetTutorGroupSize() {
        System.out.println("setTutorGroupSize");
        int tutorGroupSize = 0;
        Module instance = new Module();
        instance.setTutorGroupSize(tutorGroupSize);
        assertEquals(tutorGroupSize, instance.getTutorGroupSize());
    }

    /**
     * Test of setPresentationOne method, of class Module.
     */
    @Test
    public void testSetPresentationOne() {
        System.out.println("setPresentationOne");
        ModulePresentation modulePresentation = importModule().getPresentationTwo();
        Module instance = new Module();
        instance.setPresentationOne(modulePresentation);
        assertEquals(modulePresentation, instance.getPresentationOne());
    }

    /**
     * Test of setPresentationTwo method, of class Module.
     */
    @Test
    public void testSetPresentationTwo() {
        System.out.println("setPresentationTwo");
        ModulePresentation modulePresentation = importModule().getPresentationThree();
        Module instance = new Module();
        instance.setPresentationTwo(modulePresentation);
        assertEquals(modulePresentation, instance.getPresentationTwo());
    }

    /**
     * Test of setPresentationThree method, of class Module.
     */
    @Test
    public void testSetPresentationThree() {
        System.out.println("setPresentationThree");
        ModulePresentation modulePresentation = importModule().getPresentationOne();
        Module instance = new Module();
        instance.setPresentationThree(modulePresentation);
        assertEquals(modulePresentation, instance.getPresentationThree());
    }

    /**
     * Test of getSelfRegulatedLearningHourCount method, of class Module.
     */
    @Test
    public void testGetSelfRegulatedLearningHourCount() {
        System.out.println("getSelfRegulatedLearningHourCount");
        float expResult = createdModule.getSelfRegulatedLearningHourCount();
        float result = importedModule.getSelfRegulatedLearningHourCount();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getPresentationOne method, of class Module.
     */
    @Test
    public void testGetPresentationOne() {
        System.out.println("getPresentationOne");
        ModulePresentation expResult = createdModule.getPresentationOne();
        ModulePresentation result = importedModule.getPresentationOne();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPresentationTwo method, of class Module.
     */
    @Test
    public void testGetPresentationTwo() {
        System.out.println("getPresentationTwo");
        ModulePresentation expResult = createdModule.getPresentationTwo();
        ModulePresentation result = importedModule.getPresentationTwo();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPresentationThree method, of class Module.
     */
    @Test
    public void testGetPresentationThree() {
        System.out.println("getPresentationThree");
        ModulePresentation expResult = createdModule.getPresentationThree();
        ModulePresentation result = importedModule.getPresentationThree();
        assertEquals(expResult, result);
    }

    /**
     * Test of getModuleItems method, of class Module.
     */
    @Test
    public void testGetModuleItems() {
        System.out.println("getModuleItems");
        List<ModuleLineItem> createdItems = createdModule.getModuleItems();
        List<ModuleLineItem> importedItems = importedModule.getModuleItems();
        int index = 0;
        for (ModuleLineItem createdLineItem : createdItems) {
            ModuleLineItem importedLineItem = importedItems.get(index);
            index++;
            assertEquals(createdLineItem, importedLineItem);
        }
    }

    /**
     * Test of getTutorGroupSize method, of class Module.
     */
    @Test
    public void testGetTutorGroupSize() {
        System.out.println("getTutorGroupSize");
        int expResult = createdModule.getTutorGroupSize();
        int result = importedModule.getTutorGroupSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTotalPreparationHours method, of class Module.
     */
    @Test
    public void testGetTotalPreparationHours() {
        System.out.println("getTotalPreparationHours");
        float expResult = createdModule.getTotalPreparationHours(createdModule.getPresentationOne());
        float result = importedModule.getTotalPreparationHours(importedModule.getPresentationOne());
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getTotalPreparationCost method, of class Module.
     */
    @Test
    public void testGetTotalPreparationCost() {
        System.out.println("getTotalPreparationCost");
        float expResult = createdModule.getTotalPreparationCost(createdModule.getPresentationOne());
        float result = importedModule.getTotalPreparationCost(importedModule.getPresentationOne());
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getTotalSupportHours method, of class Module.
     */
    @Test
    public void testGetTotalSupportHours() {
        System.out.println("getTotalSupportHours");
        float expResult = createdModule.getTotalSupportHours(createdModule.getPresentationOne());
        float result = importedModule.getTotalSupportHours(importedModule.getPresentationOne());
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getTotalSupportCost method, of class Module.
     */
    @Test
    public void testGetTotalSupportCost() {
        System.out.println("getTotalSupportCost");
        float expResult = createdModule.getTotalSupportCost(createdModule.getPresentationOne());
        float result = importedModule.getTotalSupportCost(importedModule.getPresentationOne());
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getModulePresentations method, of class Module.
     */
    @Test
    public void testGetModulePresentations() {
        System.out.println("getModulePresentations");
        List<ModulePresentation> createdPresentations = createdModule.getModulePresentations();
        List<ModulePresentation> importedPresentations = importedModule.getModulePresentations();
        int index = 0;
        for (ModulePresentation importedPresentation : importedPresentations) {
            ModulePresentation createdPresentation = createdPresentations.get(index);
            index++;
            assertEquals(importedPresentation, createdPresentation);
        }
    }

    /**
     * Test of getTotalHours method, of class Module.
     */
    @Test
    public void testGetTotalHours() {
        System.out.println("getTotalHours");
        float expResult = createdModule.getTotalHours(createdModule.getPresentationOne());
        float result = importedModule.getTotalHours(importedModule.getPresentationOne());
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getTotalCost method, of class Module.
     */
    @Test
    public void testGetTotalCost() {
        System.out.println("getTotalCost");
        float expResult = createdModule.getTotalCost(createdModule.getPresentationOne());
        float result = importedModule.getTotalCost(importedModule.getPresentationOne());
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setModuleName method, of class Module.
     */
    @Test
    public void testSetModuleName() {
        System.out.println("setModuleName");
        String text = "test module name";
        Module instance = new Module();
        instance.setModuleName(text);
        assertEquals(text, instance.getModuleName());
    }

    /**
     * Test of setHourCount method, of class Module.
     */
    @Test
    public void testSetHourCount() {
        System.out.println("setHourCount");
        int i = 450;
        Module instance = new Module();
        instance.setTotalCreditHourCount(i);
        assertEquals(i, instance.getTotalCreditHourCount());
    }
}