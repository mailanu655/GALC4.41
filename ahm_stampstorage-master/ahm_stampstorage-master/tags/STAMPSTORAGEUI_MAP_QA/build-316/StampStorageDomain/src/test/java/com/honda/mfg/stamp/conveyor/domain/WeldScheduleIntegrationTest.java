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
public class WeldScheduleIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private WeldScheduleDataOnDemand dod;

	@Test
    public void testCountWeldSchedules() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", dod.getRandomWeldSchedule());
        long count = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.countWeldSchedules();
        org.junit.Assert.assertTrue("Counter for 'WeldSchedule' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindWeldSchedule() {
        com.honda.mfg.stamp.conveyor.domain.WeldSchedule obj = dod.getRandomWeldSchedule();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findWeldSchedule(id);
        org.junit.Assert.assertNotNull("Find method for 'WeldSchedule' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'WeldSchedule' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllWeldSchedules() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", dod.getRandomWeldSchedule());
        long count = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.countWeldSchedules();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'WeldSchedule', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.honda.mfg.stamp.conveyor.domain.WeldSchedule> result = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findAllWeldSchedules();
        org.junit.Assert.assertNotNull("Find all method for 'WeldSchedule' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'WeldSchedule' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindWeldScheduleEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", dod.getRandomWeldSchedule());
        long count = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.countWeldSchedules();
        if (count > 20) count = 20;
        java.util.List<com.honda.mfg.stamp.conveyor.domain.WeldSchedule> result = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findWeldScheduleEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'WeldSchedule' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'WeldSchedule' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        com.honda.mfg.stamp.conveyor.domain.WeldSchedule obj = dod.getRandomWeldSchedule();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findWeldSchedule(id);
        org.junit.Assert.assertNotNull("Find method for 'WeldSchedule' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyWeldSchedule(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'WeldSchedule' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        com.honda.mfg.stamp.conveyor.domain.WeldSchedule obj = dod.getRandomWeldSchedule();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findWeldSchedule(id);
        boolean modified =  dod.modifyWeldSchedule(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.honda.mfg.stamp.conveyor.domain.WeldSchedule merged = (com.honda.mfg.stamp.conveyor.domain.WeldSchedule) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'WeldSchedule' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", dod.getRandomWeldSchedule());
        com.honda.mfg.stamp.conveyor.domain.WeldSchedule obj = dod.getNewTransientWeldSchedule(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'WeldSchedule' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'WeldSchedule' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        com.honda.mfg.stamp.conveyor.domain.WeldSchedule obj = dod.getRandomWeldSchedule();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldSchedule' failed to provide an identifier", id);
        obj = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findWeldSchedule(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'WeldSchedule' with identifier '" + id + "'", com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findWeldSchedule(id));
    }
}
