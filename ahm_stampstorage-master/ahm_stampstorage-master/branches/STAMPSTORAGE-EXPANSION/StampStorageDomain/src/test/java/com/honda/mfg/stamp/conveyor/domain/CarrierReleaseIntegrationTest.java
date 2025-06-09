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
public class CarrierReleaseIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private CarrierReleaseDataOnDemand dod;

	@Test
	public void testCountCarrierReleases() {
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly",
				dod.getRandomCarrierRelease());
		long count = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.countCarrierReleases();
		org.junit.Assert.assertTrue("Counter for 'CarrierRelease' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testFindCarrierRelease() {
		com.honda.mfg.stamp.conveyor.domain.CarrierRelease obj = dod.getRandomCarrierRelease();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.findCarrierRelease(id);
		org.junit.Assert.assertNotNull("Find method for 'CarrierRelease' illegally returned null for id '" + id + "'",
				obj);
		org.junit.Assert.assertEquals("Find method for 'CarrierRelease' returned the incorrect identifier", id,
				obj.getId());
	}

	@Test
	public void testFindAllCarrierReleases() {
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly",
				dod.getRandomCarrierRelease());
		long count = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.countCarrierReleases();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'CarrierRelease', as there are "
				+ count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<com.honda.mfg.stamp.conveyor.domain.CarrierRelease> result = com.honda.mfg.stamp.conveyor.domain.CarrierRelease
				.findAllCarrierReleases();
		org.junit.Assert.assertNotNull("Find all method for 'CarrierRelease' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'CarrierRelease' failed to return any data",
				result.size() > 0);
	}

	@Test
	public void testFindCarrierReleaseEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly",
				dod.getRandomCarrierRelease());
		long count = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.countCarrierReleases();
		if (count > 20)
			count = 20;
		java.util.List<com.honda.mfg.stamp.conveyor.domain.CarrierRelease> result = com.honda.mfg.stamp.conveyor.domain.CarrierRelease
				.findCarrierReleaseEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'CarrierRelease' illegally returned null", result);
		org.junit.Assert.assertEquals(
				"Find entries method for 'CarrierRelease' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		com.honda.mfg.stamp.conveyor.domain.CarrierRelease obj = dod.getRandomCarrierRelease();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.findCarrierRelease(id);
		org.junit.Assert.assertNotNull("Find method for 'CarrierRelease' illegally returned null for id '" + id + "'",
				obj);
		boolean modified = dod.modifyCarrierRelease(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'CarrierRelease' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMerge() {
		com.honda.mfg.stamp.conveyor.domain.CarrierRelease obj = dod.getRandomCarrierRelease();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.findCarrierRelease(id);
		boolean modified = dod.modifyCarrierRelease(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		com.honda.mfg.stamp.conveyor.domain.CarrierRelease merged = (com.honda.mfg.stamp.conveyor.domain.CarrierRelease) obj
				.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'CarrierRelease' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	// @Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly",
				dod.getRandomCarrierRelease());
		com.honda.mfg.stamp.conveyor.domain.CarrierRelease obj = dod.getNewTransientCarrierRelease(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to provide a new transient entity",
				obj);
		org.junit.Assert.assertNull("Expected 'CarrierRelease' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'CarrierRelease' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		com.honda.mfg.stamp.conveyor.domain.CarrierRelease obj = dod.getRandomCarrierRelease();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'CarrierRelease' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.findCarrierRelease(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'CarrierRelease' with identifier '" + id + "'",
				com.honda.mfg.stamp.conveyor.domain.CarrierRelease.findCarrierRelease(id));
	}
}
