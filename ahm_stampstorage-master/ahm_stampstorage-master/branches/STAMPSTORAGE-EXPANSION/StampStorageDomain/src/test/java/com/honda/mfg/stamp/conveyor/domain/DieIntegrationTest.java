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
public class DieIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private DieDataOnDemand dod;

	@Test
	public void testCountDies() {
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", dod.getRandomDie());
		long count = Die.countDies();
		org.junit.Assert.assertTrue("Counter for 'Die' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindDie() {
		Die obj = dod.getRandomDie();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to provide an identifier", id);
		obj = Die.findDie(id);
		org.junit.Assert.assertNotNull("Find method for 'Die' illegally returned null for id '" + id + "'", obj);
		org.junit.Assert.assertEquals("Find method for 'Die' returned the incorrect identifier", id, obj.getId());
	}

	// @Test
//    public void DieIntegrationTest.testFindDieByNumber() {
//        Die obj = dod.getRandomDie();
//        org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", obj);
//        Integer dieNumber = obj.getDieNumber();
//        org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to provide an identifier", dieNumber);
//        obj = Die.findDieByNumber(dieNumber);
//        org.junit.Assert.assertNotNull("Find method for 'Die' illegally returned null for dieNumber '" + dieNumber + "'", obj);
//        org.junit.Assert.assertEquals("Find method for 'Die' returned the incorrect identifier", dieNumber, obj.getDieNumber());
//    }

	@Test
	public void testFindAllDies() {
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", dod.getRandomDie());
		long count = Die.countDies();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Die', as there are " + count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<Die> result = Die.findAllDies();
		org.junit.Assert.assertNotNull("Find all method for 'Die' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'Die' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindDieEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", dod.getRandomDie());
		long count = Die.countDies();
		if (count > 20)
			count = 20;
		java.util.List<Die> result = Die.findDieEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'Die' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'Die' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		Die obj = dod.getRandomDie();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to provide an identifier", id);
		obj = Die.findDie(id);
		org.junit.Assert.assertNotNull("Find method for 'Die' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyDie(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'Die' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMerge() {
		Die obj = dod.getRandomDie();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to provide an identifier", id);
		obj = Die.findDie(id);
		boolean modified = dod.modifyDie(obj);
		Integer currentVersion = obj.getVersion();
		Die merged = (Die) obj.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'Die' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", dod.getRandomDie());
		Die obj = dod.getNewTransientDie(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to provide a new transient entity", obj);
		// org.junit.Assert.assertNull("Expected 'Die' identifier to be null",
		// obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'Die' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		Die obj = dod.getRandomDie();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Die' failed to provide an identifier", id);
		obj = Die.findDie(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'Die' with identifier '" + id + "'", Die.findDie(id));
	}
}
