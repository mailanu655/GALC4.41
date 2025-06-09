package com.honda.mfg.stamp.conveyor.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
@Configurable
public class WeldOrderIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private WeldOrderDataOnDemand dod;

	@Test
    public void testCountWeldOrders() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", dod.getRandomWeldOrder());
        long count = WeldOrder.countWeldOrders();
        org.junit.Assert.assertTrue("Counter for 'WeldOrder' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindWeldOrder() {
        WeldOrder obj = dod.getRandomWeldOrder();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to provide an identifier", id);
        obj = WeldOrder.findWeldOrder(id);
        org.junit.Assert.assertNotNull("Find method for 'WeldOrder' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'WeldOrder' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllWeldOrders() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", dod.getRandomWeldOrder());
        long count = WeldOrder.countWeldOrders();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'WeldOrder', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<WeldOrder> result = WeldOrder.findAllWeldOrders();
        org.junit.Assert.assertNotNull("Find all method for 'WeldOrder' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'WeldOrder' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindWeldOrderEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", dod.getRandomWeldOrder());
        long count = WeldOrder.countWeldOrders();
        if (count > 20) count = 20;
        java.util.List<WeldOrder> result = WeldOrder.findWeldOrderEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'WeldOrder' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'WeldOrder' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        WeldOrder obj = dod.getRandomWeldOrder();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to provide an identifier", id);
        obj = WeldOrder.findWeldOrder(id);
        org.junit.Assert.assertNotNull("Find method for 'WeldOrder' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyWeldOrder(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'WeldOrder' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        WeldOrder obj = dod.getRandomWeldOrder();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to provide an identifier", id);
        obj = WeldOrder.findWeldOrder(id);
        boolean modified = dod.modifyWeldOrder(obj);
        Integer currentVersion = obj.getVersion();
        WeldOrder merged = (WeldOrder) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'WeldOrder' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", dod.getRandomWeldOrder());
        WeldOrder obj = dod.getNewTransientWeldOrder(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'WeldOrder' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'WeldOrder' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        WeldOrder obj = dod.getRandomWeldOrder();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'WeldOrder' failed to provide an identifier", id);
        obj = WeldOrder.findWeldOrder(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'WeldOrder' with identifier '" + id + "'", WeldOrder.findWeldOrder(id));
    }
}
