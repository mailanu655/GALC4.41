package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import java.util.List;
import org.junit.Assert;
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
public class StopIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private StopDataOnDemand dod;

	@Test
    public void testCountStops() {
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", dod.getRandomStop());
        long count = Stop.countStops();
        org.junit.Assert.assertTrue("Counter for 'Stop' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindStop() {
        Stop obj = dod.getRandomStop();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to provide an identifier", id);
        obj = Stop.findStop(id);
        org.junit.Assert.assertNotNull("Find method for 'Stop' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Stop' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllStops() {
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", dod.getRandomStop());
        long count = Stop.countStops();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Stop', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<Stop> result = Stop.findAllStops();
        org.junit.Assert.assertNotNull("Find all method for 'Stop' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Stop' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindStopEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", dod.getRandomStop());
        long count = Stop.countStops();
        if (count > 20) count = 20;
        java.util.List<Stop> result = Stop.findStopEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'Stop' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Stop' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Stop obj = dod.getRandomStop();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to provide an identifier", id);
        obj = Stop.findStop(id);
        org.junit.Assert.assertNotNull("Find method for 'Stop' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyStop(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Stop' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        Stop obj = dod.getRandomStop();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to provide an identifier", id);
        obj = Stop.findStop(id);
        boolean modified = dod.modifyStop(obj);
        Integer currentVersion = obj.getVersion();
        Stop merged = (Stop) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'Stop' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", dod.getRandomStop());
        Stop obj = dod.getNewTransientStop(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNotNull("Expected 'Stop' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        //org.junit.Assert.assertNotNull("Expected 'Stop' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Stop obj = dod.getRandomStop();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Stop' failed to provide an identifier", id);
        obj = Stop.findStop(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Stop' with identifier '" + id + "'", Stop.findStop(id));
    }

	@Test
    public void testFindAllStopsByStopType() {
        List<Stop> stops = Stop.findAllStopsByType(StopType.STORE_IN_ALL_LANES);
        if(stops != null && stops.size()>0){
           System.out.println(stops.get(0).getStopType());
        }
        Assert.assertTrue(stops != null);
    }
}
