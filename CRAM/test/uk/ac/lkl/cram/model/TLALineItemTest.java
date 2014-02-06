package uk.ac.lkl.cram.model;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import uk.ac.lkl.cram.model.ModulePresentation.Run;

/**
 * $Date$
 * $Revision$
 * @author bernard horan
 */
@SuppressWarnings("ClassWithoutLogger")
public class TLALineItemTest extends CRAMTest {
    
    public TLALineItemTest() {
    }

    /**
     * Test of getMaximumGroupSizeForPresentation method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetMaximumGroupSizeForPresentation() {
        System.out.println("getMaximumGroupSizeForPresentation");
	ModulePresentation createdPresentation = createdModule.getPresentationOne();
	ModulePresentation importedPresentation = importedModule.getPresentationOne();
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getMaximumGroupSizeForPresentation(createdPresentation), importedLineItem.getMaximumGroupSizeForPresentation(importedPresentation));
        }
    }

    /**
     * Test of getWeeklyLearnerHourCount method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetWeeklyLearnerHourCount() {
        System.out.println("getWeeklyLearnerHourCount");
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getWeeklyLearnerHourCount(), importedLineItem.getWeeklyLearnerHourCount(),0.1);
        }
    }

    /**
     * Test of setWeeklyLearnerHourCount method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testSetWeeklyLearnerHourCount() {
        System.out.println("setWeeklyLearnerHourCount");
        float f = 3.0F;
        TLALineItem instance = new TLALineItem();
        instance.setWeeklyLearnerHourCount(f);
        assertEquals(f, instance.getWeeklyLearnerHourCount(), 0.0);
    }

    /**
     * Test of getWeekCount method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetWeekCount() {
	System.out.println("getWeekCount");
	int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getWeekCount(), importedLineItem.getWeekCount(),0.1);
        }
    }
    
    /**
     * Test of setWeekCount method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testSetWeekCount() {
	System.out.println("setWeekCount");
	int weekCount = 0;
	TLALineItem instance = new TLALineItem();
	instance.setWeekCount(weekCount);
	assertEquals(weekCount, instance.getWeeklyLearnerHourCount(), 0.0);
    }
    
    /**
     * Test of getNonWeeklyLearnerHourCount method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetNonWeeklyLearnerHourCount() {
        System.out.println("getNonWeeklyLearnerHourCount");
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getNonWeeklyLearnerHourCount(), importedLineItem.getNonWeeklyLearnerHourCount(), 0.1);
        }
    }

    /**
     * Test of setNonWeeklyLearnerHourCount method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testSetNonWeeklyLearnerHourCount() {
        System.out.println("setNonWeeklyLearnerHourCount");
        float f = 3.0F;
        TLALineItem instance = new TLALineItem();
        instance.setNonWeeklyLearnerHourCount(f);
        assertEquals(f, instance.getNonWeeklyLearnerHourCount(), 0.0);
    }

    /**
     * Test of getTotalLearnerHourCount method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetTotalLearnerHourCount() {
        System.out.println("getTotalLearnerHourCount");
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getTotalLearnerHourCount(createdModule), importedLineItem.getTotalLearnerHourCount(importedModule), 0.1);
        }
    }

    /**
     * Test of setPreparationTime method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testSetPreparationTime() {
        System.out.println("setPreparationTime");
        ModulePresentation mp = new ModulePresentation(Run.FIRST);
        PreparationTime pt = new PreparationTime();
        TLALineItem instance = new TLALineItem();
        instance.setPreparationTime(mp, pt);
        assertEquals(instance.getPreparationTime(mp), pt);
    }

    /**
     * Test of getPreparationTime method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetPreparationTime() {
        System.out.println("getPreparationTime");
        ModulePresentation createdPresentation = createdModule.getPresentationOne();
        ModulePresentation importedPresentation = importedModule.getPresentationOne();
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getPreparationTime(createdPresentation), importedLineItem.getPreparationTime(importedPresentation));
        }
    }

    /**
     * Test of setSupportTime method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testSetSupportTime() {
        System.out.println("setSupportTime");
        ModulePresentation mp = new ModulePresentation(Run.FIRST);
        SupportTime st = new SupportTime();
        TLALineItem instance = new TLALineItem();
        instance.setSupportTime(mp, st);
        assertEquals(instance.getSupportTime(mp), st);
    }

    /**
     * Test of getSupportTime method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetSupportTime() {
        System.out.println("getSupportTime");
        ModulePresentation createdPresentation = createdModule.getPresentationOne();
        ModulePresentation importedPresentation = importedModule.getPresentationOne();
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getSupportTime(createdPresentation), importedLineItem.getSupportTime(importedPresentation));
        }
    }

    /**
     * Test of getActivity method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetActivity() {
        System.out.println("getActivity");
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getActivity(), importedLineItem.getActivity());
        }
    }

    /**
     * Test of getName method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetName() {
        System.out.println("getName");
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getName(), importedLineItem.getName());
        }
    }

    /**
     * Test of getNumberOfIndividuals_Groups method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetNumberOfIndividuals_Groups() {
        //public float getNumberOfIndividuals_Groups(ModulePresentation modulePresentation, Module module);
        System.out.println("getNumberOfIndividuals_Groups");
        ModulePresentation createdPresentation = createdModule.getPresentationOne();
        ModulePresentation importedPresentation = importedModule.getPresentationOne();
        SupportTime st = new SupportTime();
        int index = 0;
        for (TLALineItem createdLineItem : getCreatedLineItems()) {
            TLALineItem importedLineItem = getImportedLineItems().get(index);
            index++;
            assertEquals(createdLineItem.getNumberOfIndividuals_Groups(createdPresentation, createdModule), importedLineItem.getNumberOfIndividuals_Groups(importedPresentation, importedModule), 0.1);
        }
    }
    
    /**
     * Test of setActivity method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testSetActivity() {
        System.out.println("setActivity");
        TLActivity selectedTLA = new TLActivity();
        TLALineItem instance = new TLALineItem();
        instance.setActivity(selectedTLA);
        assertEquals(selectedTLA, instance.getActivity());
    }

    /**
     * Test of equals method, of class TLALineItem.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testEquals() {
	System.out.println("equals");
	Object obj = null;
	TLALineItem instance = new TLALineItem();
	boolean expResult = false;
	boolean result = instance.equals(obj);
	assertEquals(expResult, result);
    }
    
    private List<TLALineItem> getImportedLineItems() {
        return importedModule.getTLALineItems();
    }

    private List<TLALineItem> getCreatedLineItems() {
        return createdModule.getTLALineItems();
    }
}