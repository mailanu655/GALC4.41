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
public class CarrierIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private CarrierDataOnDemand dod;

//	@Test
//    public void testCountCarriers() {
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", dod.getRandomCarrier());
//        long count = com.honda.mfg.stamp.conveyor.domain.Carrier.countCarriers();
//        org.junit.Assert.assertTrue("Counter for 'Carrier' incorrectly reported there were no entries", count > 0);
//    }

//	@Test
//    public void testFindCarrier() {
//        com.honda.mfg.stamp.conveyor.domain.Carrier obj = dod.getRandomCarrier();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
//        Long id = obj.getId();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
//        obj = com.honda.mfg.stamp.conveyor.domain.Carrier.findCarrier(id);
//        org.junit.Assert.assertNotNull("Find method for 'Carrier' illegally returned null for id '" + id + "'", obj);
//        org.junit.Assert.assertEquals("Find method for 'Carrier' returned the incorrect identifier", id, obj.getId());
//    }

//	@Test
//    public void testFindAllCarriers() {
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", dod.getRandomCarrier());
//        long count = com.honda.mfg.stamp.conveyor.domain.Carrier.countCarriers();
//        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Carrier', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
//        java.util.List<com.honda.mfg.stamp.conveyor.domain.Carrier> result = com.honda.mfg.stamp.conveyor.domain.Carrier.findAllCarriers();
//        org.junit.Assert.assertNotNull("Find all method for 'Carrier' illegally returned null", result);
//        org.junit.Assert.assertTrue("Find all method for 'Carrier' failed to return any data", result.size() > 0);
//    }

//	@Test
//    public void testFindCarrierEntries() {
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", dod.getRandomCarrier());
//        long count = com.honda.mfg.stamp.conveyor.domain.Carrier.countCarriers();
//        if (count > 20) count = 20;
//        java.util.List<com.honda.mfg.stamp.conveyor.domain.Carrier> result = com.honda.mfg.stamp.conveyor.domain.Carrier.findCarrierEntries(0, (int) count);
//        org.junit.Assert.assertNotNull("Find entries method for 'Carrier' illegally returned null", result);
//        org.junit.Assert.assertEquals("Find entries method for 'Carrier' returned an incorrect number of entries", count, result.size());
//    }
//
//	@Test
//    public void testFlush() {
//        com.honda.mfg.stamp.conveyor.domain.Carrier obj = dod.getRandomCarrier();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
//        Long id = obj.getId();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
//        obj = com.honda.mfg.stamp.conveyor.domain.Carrier.findCarrier(id);
//        org.junit.Assert.assertNotNull("Find method for 'Carrier' illegally returned null for id '" + id + "'", obj);
//        boolean modified = dod.modifyCarrier(obj);
//        Integer currentVersion = obj.getVersion();
//        obj.flush();
//        org.junit.Assert.assertTrue("Version for 'Carrier' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
//    }

//	@Test
//    public void testMerge() {
//        com.honda.mfg.stamp.conveyor.domain.Carrier obj = dod.getRandomCarrier();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
//        Long id = obj.getId();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
//        obj = com.honda.mfg.stamp.conveyor.domain.Carrier.findCarrier(id);
//        boolean modified = dod.modifyCarrier(obj);
//        Integer currentVersion = obj.getVersion();
//        com.honda.mfg.stamp.conveyor.domain.Carrier merged = (com.honda.mfg.stamp.conveyor.domain.Carrier) obj.merge();
//        obj.flush();
//        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
//        org.junit.Assert.assertTrue("Version for 'Carrier' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
//    }

//	@Test
//    public void testPersist() {
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", dod.getRandomCarrier());
//        com.honda.mfg.stamp.conveyor.domain.Carrier obj = dod.getNewTransientCarrier(Integer.MAX_VALUE);
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide a new transient entity", obj);
//        org.junit.Assert.assertNull("Expected 'Carrier' identifier to be null", obj.getId());
//        obj.persist();
//        obj.flush();
//        org.junit.Assert.assertNotNull("Expected 'Carrier' identifier to no longer be null", obj.getId());
//    }

//	@Test
//    public void testRemove() {
//        com.honda.mfg.stamp.conveyor.domain.Carrier obj = dod.getRandomCarrier();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
//        Long id = obj.getId();
//        org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
//        obj = com.honda.mfg.stamp.conveyor.domain.Carrier.findCarrier(id);
//        obj.remove();
//        obj.flush();
//        org.junit.Assert.assertNull("Failed to remove 'Carrier' with identifier '" + id + "'", com.honda.mfg.stamp.conveyor.domain.Carrier.findCarrier(id));
//    }
}
