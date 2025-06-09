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
public class ValidDestinationIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private ValidDestinationDataOnDemand dod;

	@Test
	public void testCountValidDestinations() {
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly",
				dod.getRandomValidDestination());
		long count = com.honda.mfg.stamp.conveyor.domain.ValidDestination.countValidDestinations();
		org.junit.Assert.assertTrue("Counter for 'ValidDestination' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testFindValidDestination() {
		com.honda.mfg.stamp.conveyor.domain.ValidDestination obj = dod.getRandomValidDestination();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.ValidDestination.findValidDestination(id);
		org.junit.Assert.assertNotNull("Find method for 'ValidDestination' illegally returned null for id '" + id + "'",
				obj);
		org.junit.Assert.assertEquals("Find method for 'ValidDestination' returned the incorrect identifier", id,
				obj.getId());
	}

	@Test
	public void testFindAllValidDestinations() {
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly",
				dod.getRandomValidDestination());
		long count = com.honda.mfg.stamp.conveyor.domain.ValidDestination.countValidDestinations();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'ValidDestination', as there are "
				+ count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<com.honda.mfg.stamp.conveyor.domain.ValidDestination> result = com.honda.mfg.stamp.conveyor.domain.ValidDestination
				.findAllValidDestinations();
		org.junit.Assert.assertNotNull("Find all method for 'ValidDestination' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'ValidDestination' failed to return any data",
				result.size() > 0);
	}

	@Test
	public void testFindValidDestinationEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly",
				dod.getRandomValidDestination());
		long count = com.honda.mfg.stamp.conveyor.domain.ValidDestination.countValidDestinations();
		if (count > 20)
			count = 20;
		java.util.List<com.honda.mfg.stamp.conveyor.domain.ValidDestination> result = com.honda.mfg.stamp.conveyor.domain.ValidDestination
				.findValidDestinationEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'ValidDestination' illegally returned null", result);
		org.junit.Assert.assertEquals(
				"Find entries method for 'ValidDestination' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		com.honda.mfg.stamp.conveyor.domain.ValidDestination obj = dod.getRandomValidDestination();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.ValidDestination.findValidDestination(id);
		org.junit.Assert.assertNotNull("Find method for 'ValidDestination' illegally returned null for id '" + id + "'",
				obj);
		boolean modified = dod.modifyValidDestination(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'ValidDestination' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMerge() {
		com.honda.mfg.stamp.conveyor.domain.ValidDestination obj = dod.getRandomValidDestination();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.ValidDestination.findValidDestination(id);
		boolean modified = dod.modifyValidDestination(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		com.honda.mfg.stamp.conveyor.domain.ValidDestination merged = (com.honda.mfg.stamp.conveyor.domain.ValidDestination) obj
				.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'ValidDestination' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly",
				dod.getRandomValidDestination());
		com.honda.mfg.stamp.conveyor.domain.ValidDestination obj = dod
				.getNewTransientValidDestination(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to provide a new transient entity",
				obj);
		org.junit.Assert.assertNull("Expected 'ValidDestination' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'ValidDestination' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		com.honda.mfg.stamp.conveyor.domain.ValidDestination obj = dod.getRandomValidDestination();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'ValidDestination' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.ValidDestination.findValidDestination(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'ValidDestination' with identifier '" + id + "'",
				com.honda.mfg.stamp.conveyor.domain.ValidDestination.findValidDestination(id));
	}
}
