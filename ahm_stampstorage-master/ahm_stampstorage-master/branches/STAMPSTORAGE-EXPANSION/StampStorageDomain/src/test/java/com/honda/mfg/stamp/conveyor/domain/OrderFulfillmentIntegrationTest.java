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
public class OrderFulfillmentIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private OrderFulfillmentDataOnDemand dod;

	// @Test
	public void testCountOrderFulfillments() {
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly",
				dod.getRandomOrderFulfillment());
		long count = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.countOrderFulfillments();
		org.junit.Assert.assertTrue("Counter for 'OrderFulfillment' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testFindOrderFulfillment() {
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillment obj = dod.getRandomOrderFulfillment();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly", obj);
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.findOrderFulfillment(id);
		org.junit.Assert.assertNotNull("Find method for 'OrderFulfillment' illegally returned null for id '" + id + "'",
				obj);
		org.junit.Assert.assertEquals("Find method for 'OrderFulfillment' returned the incorrect identifier", id,
				obj.getId());
	}

	// @Test
	public void testFindAllOrderFulfillments() {
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly",
				dod.getRandomOrderFulfillment());
		long count = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.countOrderFulfillments();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'OrderFulfillment', as there are "
				+ count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<com.honda.mfg.stamp.conveyor.domain.OrderFulfillment> result = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment
				.findAllOrderFulfillments();
		org.junit.Assert.assertNotNull("Find all method for 'OrderFulfillment' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'OrderFulfillment' failed to return any data",
				result.size() > 0);
	}

	// @Test
	public void testFindOrderFulfillmentEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly",
				dod.getRandomOrderFulfillment());
		long count = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.countOrderFulfillments();
		if (count > 20)
			count = 20;
		java.util.List<com.honda.mfg.stamp.conveyor.domain.OrderFulfillment> result = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment
				.findOrderFulfillmentEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'OrderFulfillment' illegally returned null", result);
		org.junit.Assert.assertEquals(
				"Find entries method for 'OrderFulfillment' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillment obj = dod.getRandomOrderFulfillment();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly", obj);
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.findOrderFulfillment(id);
		org.junit.Assert.assertNotNull("Find method for 'OrderFulfillment' illegally returned null for id '" + id + "'",
				obj);
		boolean modified = dod.modifyOrderFulfillment(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'OrderFulfillment' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMerge() {
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillment obj = dod.getRandomOrderFulfillment();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly", obj);
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.findOrderFulfillment(id);
		boolean modified = dod.modifyOrderFulfillment(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillment merged = (com.honda.mfg.stamp.conveyor.domain.OrderFulfillment) obj
				.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'OrderFulfillment' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly",
				dod.getRandomOrderFulfillment());
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillment obj = dod
				.getNewTransientOrderFulfillment(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to provide a new transient entity",
				obj);
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'OrderFulfillment' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillment obj = dod.getRandomOrderFulfillment();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to initialize correctly", obj);
		com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'OrderFulfillment' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.findOrderFulfillment(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'OrderFulfillment' with identifier '" + id + "'",
				com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.findOrderFulfillment(id));
	}
}
