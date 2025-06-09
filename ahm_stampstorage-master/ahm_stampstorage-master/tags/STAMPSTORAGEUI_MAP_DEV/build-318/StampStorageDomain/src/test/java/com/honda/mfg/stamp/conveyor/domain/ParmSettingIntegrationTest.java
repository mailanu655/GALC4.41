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
public class ParmSettingIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private ParmSettingDataOnDemand dod;

	@Test
    public void testCountParmSettings() {
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", dod.getRandomParmSetting());
        long count = com.honda.mfg.stamp.conveyor.domain.ParmSetting.countParmSettings();
        org.junit.Assert.assertTrue("Counter for 'ParmSetting' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindParmSetting() {
        com.honda.mfg.stamp.conveyor.domain.ParmSetting obj = dod.getRandomParmSetting();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ParmSetting.findParmSetting(id);
        org.junit.Assert.assertNotNull("Find method for 'ParmSetting' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'ParmSetting' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllParmSettings() {
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", dod.getRandomParmSetting());
        long count = com.honda.mfg.stamp.conveyor.domain.ParmSetting.countParmSettings();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'ParmSetting', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.honda.mfg.stamp.conveyor.domain.ParmSetting> result = com.honda.mfg.stamp.conveyor.domain.ParmSetting.findAllParmSettings();
        org.junit.Assert.assertNotNull("Find all method for 'ParmSetting' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'ParmSetting' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindParmSettingEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", dod.getRandomParmSetting());
        long count = com.honda.mfg.stamp.conveyor.domain.ParmSetting.countParmSettings();
        if (count > 20) count = 20;
        java.util.List<com.honda.mfg.stamp.conveyor.domain.ParmSetting> result = com.honda.mfg.stamp.conveyor.domain.ParmSetting.findParmSettingEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'ParmSetting' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'ParmSetting' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        com.honda.mfg.stamp.conveyor.domain.ParmSetting obj = dod.getRandomParmSetting();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ParmSetting.findParmSetting(id);
        org.junit.Assert.assertNotNull("Find method for 'ParmSetting' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyParmSetting(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'ParmSetting' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        com.honda.mfg.stamp.conveyor.domain.ParmSetting obj = dod.getRandomParmSetting();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ParmSetting.findParmSetting(id);
        boolean modified =  dod.modifyParmSetting(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.honda.mfg.stamp.conveyor.domain.ParmSetting merged = (com.honda.mfg.stamp.conveyor.domain.ParmSetting) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'ParmSetting' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", dod.getRandomParmSetting());
        com.honda.mfg.stamp.conveyor.domain.ParmSetting obj = dod.getNewTransientParmSetting(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'ParmSetting' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'ParmSetting' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        com.honda.mfg.stamp.conveyor.domain.ParmSetting obj = dod.getRandomParmSetting();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ParmSetting' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.ParmSetting.findParmSetting(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'ParmSetting' with identifier '" + id + "'", com.honda.mfg.stamp.conveyor.domain.ParmSetting.findParmSetting(id));
    }
}
