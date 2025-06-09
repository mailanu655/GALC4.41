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
public class OrderMgrIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private OrderMgrDataOnDemand dod;

	@Test
    public void testCountOrderMgrs() {
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", dod.getRandomOrderMgr());
        long count = OrderMgr.countOrderMgrs();
        org.junit.Assert.assertTrue("Counter for 'OrderMgr' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindOrderMgr() {
        OrderMgr obj = dod.getRandomOrderMgr();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to provide an identifier", id);
        obj = OrderMgr.findOrderMgr(id);
        org.junit.Assert.assertNotNull("Find method for 'OrderMgr' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'OrderMgr' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllOrderMgrs() {
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", dod.getRandomOrderMgr());
        long count = OrderMgr.countOrderMgrs();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'OrderMgr', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<OrderMgr> result = OrderMgr.findAllOrderMgrs();
        org.junit.Assert.assertNotNull("Find all method for 'OrderMgr' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'OrderMgr' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindOrderMgrEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", dod.getRandomOrderMgr());
        long count = OrderMgr.countOrderMgrs();
        if (count > 20) count = 20;
        java.util.List<OrderMgr> result = OrderMgr.findOrderMgrEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'OrderMgr' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'OrderMgr' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        OrderMgr obj = dod.getRandomOrderMgr();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to provide an identifier", id);
        obj = OrderMgr.findOrderMgr(id);
        org.junit.Assert.assertNotNull("Find method for 'OrderMgr' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyOrderMgr(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'OrderMgr' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        OrderMgr obj = dod.getRandomOrderMgr();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to provide an identifier", id);
        obj = OrderMgr.findOrderMgr(id);
        boolean modified = dod.modifyOrderMgr(obj);
        Integer currentVersion = obj.getVersion();
        OrderMgr merged = (OrderMgr) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'OrderMgr' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", dod.getRandomOrderMgr());
        OrderMgr obj = dod.getNewTransientOrderMgr(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'OrderMgr' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'OrderMgr' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        OrderMgr obj = dod.getRandomOrderMgr();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to initialize correctly", obj);
        Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'OrderMgr' failed to provide an identifier", id);
        obj = OrderMgr.findOrderMgr(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'OrderMgr' with identifier '" + id + "'", OrderMgr.findOrderMgr(id));
    }
}
