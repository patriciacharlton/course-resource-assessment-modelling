package uk.ac.lkl.cram.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Bernard Horan
 */
@SuppressWarnings("ClassWithoutLogger")
public class TLActivityTest extends CRAMTestAbstract {
    
    public TLActivityTest() {
    }

    /**
     * Test of getName method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetName() {
        System.out.println("getName");
        int index = 0;
        for (TLActivity createdActivity : getCreatedActivities()) {
            TLActivity importedActivity = getImportedActivities().get(index);
            index++;
            assertEquals(createdActivity.getName(), importedActivity.getName());
        }
    }

    /**
     * Test of getLearningType method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetLearningType() {
        System.out.println("getLearningType");
        int index = 0;
        for (TLActivity createdActivity : getCreatedActivities()) {
            TLActivity importedActivity = getImportedActivities().get(index);
            index++;
            assertEquals(createdActivity.getLearningType(), importedActivity.getLearningType());
        }
    }

    /**
     * Test of getLearningExperience method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetLearningExperience() {
        System.out.println("getLearningExperience");
        int index = 0;
        for (TLActivity createdActivity : getCreatedActivities()) {
            TLActivity importedActivity = getImportedActivities().get(index);
            index++;
            assertEquals(createdActivity.getLearningExperience(), importedActivity.getLearningExperience());
        }
    }

    /**
     * Test of getLearnerFeedback method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetLearnerFeedback() {
        System.out.println("getLearnerFeedback");
        int index = 0;
        for (TLActivity createdActivity : getCreatedActivities()) {
            TLActivity importedActivity = getImportedActivities().get(index);
            index++;
            assertEquals(createdActivity.getLearnerFeedback(), importedActivity.getLearnerFeedback());
        }
    }

    /**
     * Test of getStudentTeacherInteraction method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetStudentTeacherInteraction() {
        System.out.println("getStudentTeacherInteraction");
        int index = 0;
        for (TLActivity createdActivity : getCreatedActivities()) {
            TLActivity importedActivity = getImportedActivities().get(index);
            index++;
            assertEquals(createdActivity.getStudentTeacherInteraction(), importedActivity.getStudentTeacherInteraction());
        }
    }

    /**
     * Test of getMaximumGroupSize method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetMaximumGroupSize() {
        System.out.println("getMaximumGroupSize");
        int index = 0;
        for (TLActivity createdActivity : getCreatedActivities()) {
            TLActivity importedActivity = getImportedActivities().get(index);
            index++;
            assertEquals(createdActivity.getMaximumGroupSize(), importedActivity.getMaximumGroupSize());
        }
    }

    /**
     * Test of equals method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testEquals() {
        System.out.println("equals");
        assertEquals(new TLActivity(), new TLActivity());
    }

    /**
     * Test of isImmutable method, of class TLActivity.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testIsImmutable() {
        System.out.println("isImmutable");
        int index = 0;
        for (TLActivity createdActivity : getCreatedActivities()) {
            TLActivity importedActivity = getImportedActivities().get(index);
            index++;
            assertEquals(createdActivity.isImmutable(), importedActivity.isImmutable());
        }
    }
    
    /**
     * Test of isImmutable method, of class TLActivity.
     */
    @Test
    public void testCopyConstructor() {
        System.out.println("copy constructor");
        TLActivity activity = new TLActivity("Test");
        assertEquals(activity, new TLActivity(activity));
        assertFalse(activity == new TLActivity(activity));
    }
    
    private List<TLActivity> getImportedActivities() {
        List<TLALineItem> lineItems = importedModule.getTLALineItems();
        List<TLActivity> importedActivities = new ArrayList<>();
        for (TLALineItem lineItem : lineItems) {
            importedActivities.add(lineItem.getActivity());
        }
        return importedActivities;
    }

    private List<TLActivity> getCreatedActivities() {
        List<TLALineItem> lineItems = createdModule.getTLALineItems();
        List<TLActivity> createdActivities = new ArrayList<>();
        for (TLALineItem lineItem : lineItems) {
            createdActivities.add(lineItem.getActivity());
        }
        return createdActivities;
    }
}