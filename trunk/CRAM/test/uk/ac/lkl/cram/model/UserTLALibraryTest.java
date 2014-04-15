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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Bernard Horan
 */
public class UserTLALibraryTest {
    
    @BeforeClass
    public static void setUpClass() {
	UserTLALibrary.clearDefaultLibrary();
    }
    
    @AfterClass
    public static void tearDownClass() {
	UserTLALibrary.clearDefaultLibrary();
    }
    
    public UserTLALibraryTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() throws BackingStoreException {
	UserTLALibrary.clearPreferences();
    }

    /**
     * Test of getDefaultLibrary method, of class UserTLALibrary.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testGetDefaultLibrary() {
	System.out.println("getDefaultLibrary");
	UserTLALibrary result = UserTLALibrary.getDefaultLibrary();
	assertNotNull(result);
    }

    /**
     * Test of clearDefaultLibrary method, of class UserTLALibrary.
     */
    @Test
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void testClearDefaultLibrary() {
	System.out.println("clearDefaultLibrary");
	UserTLALibrary.clearDefaultLibrary();
	UserTLALibrary result = UserTLALibrary.getDefaultLibrary();
	assertNotNull(result);
    }

    /**
     * Test of addActivity method, of class UserTLALibrary.
     */
    @Test
    @SuppressWarnings({"UnusedAssignment", "UseOfSystemOutOrSystemErr"})
    public void testAddActivity() {
	System.out.println("addActivity");
	Collection<TLALineItem> tlaLineItems = ExampleTest.populateModule().getTLALineItems();
	Set<TLActivity> activities = new HashSet<>();
	UserTLALibrary instance = UserTLALibrary.getDefaultLibrary();
	for (TLALineItem tLALineItem : tlaLineItems) {
	    TLActivity activity = tLALineItem.getActivity();
	    instance.addActivity(activity);
	    activities.add(activity);
	}	
	instance = null;
	UserTLALibrary.clearDefaultLibrary();
	instance = UserTLALibrary.getDefaultLibrary();
	assertEquals(activities, instance.getActivities());
    }
}