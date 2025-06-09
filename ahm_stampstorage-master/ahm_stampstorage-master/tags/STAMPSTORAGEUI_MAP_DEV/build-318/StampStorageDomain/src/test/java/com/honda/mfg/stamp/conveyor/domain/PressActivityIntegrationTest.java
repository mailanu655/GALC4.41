package com.honda.mfg.stamp.conveyor.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class PressActivityIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private PressActivityDataOnDemand dod;

	@Test
    public void testCountPressActivitys() {
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", dod.getRandomPressActivity());
        long count = com.honda.mfg.stamp.conveyor.domain.PressActivity.countPressActivitys();
        org.junit.Assert.assertTrue("Counter for 'PressActivity' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindPressActivity() {
        com.honda.mfg.stamp.conveyor.domain.PressActivity obj = dod.getRandomPressActivity();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.PressActivity.findPressActivity(id);
        org.junit.Assert.assertNotNull("Find method for 'PressActivity' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'PressActivity' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllPressActivitys() {
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", dod.getRandomPressActivity());
        long count = com.honda.mfg.stamp.conveyor.domain.PressActivity.countPressActivitys();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'PressActivity', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.honda.mfg.stamp.conveyor.domain.PressActivity> result = com.honda.mfg.stamp.conveyor.domain.PressActivity.findAllPressActivitys();
        org.junit.Assert.assertNotNull("Find all method for 'PressActivity' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'PressActivity' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindPressActivityEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", dod.getRandomPressActivity());
        long count = com.honda.mfg.stamp.conveyor.domain.PressActivity.countPressActivitys();
        if (count > 20) count = 20;
        java.util.List<com.honda.mfg.stamp.conveyor.domain.PressActivity> result = com.honda.mfg.stamp.conveyor.domain.PressActivity.findPressActivityEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'PressActivity' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'PressActivity' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        com.honda.mfg.stamp.conveyor.domain.PressActivity obj = dod.getRandomPressActivity();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.PressActivity.findPressActivity(id);
        org.junit.Assert.assertNotNull("Find method for 'PressActivity' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPressActivity(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'PressActivity' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        com.honda.mfg.stamp.conveyor.domain.PressActivity obj = dod.getRandomPressActivity();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.PressActivity.findPressActivity(id);
        boolean modified =  dod.modifyPressActivity(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.honda.mfg.stamp.conveyor.domain.PressActivity merged = (com.honda.mfg.stamp.conveyor.domain.PressActivity) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'PressActivity' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", dod.getRandomPressActivity());
        com.honda.mfg.stamp.conveyor.domain.PressActivity obj = dod.getNewTransientPressActivity(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'PressActivity' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'PressActivity' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        com.honda.mfg.stamp.conveyor.domain.PressActivity obj = dod.getRandomPressActivity();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'PressActivity' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.PressActivity.findPressActivity(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'PressActivity' with identifier '" + id + "'", com.honda.mfg.stamp.conveyor.domain.PressActivity.findPressActivity(id));
    }
}
