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
public class DefectIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private DefectDataOnDemand dod;

	@Test
	public void testCountDefects() {
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly",
				dod.getRandomDefect());
		long count = com.honda.mfg.stamp.conveyor.domain.Defect.countDefects();
		org.junit.Assert.assertTrue("Counter for 'Defect' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindDefect() {
		com.honda.mfg.stamp.conveyor.domain.Defect obj = dod.getRandomDefect();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.Defect.findDefect(id);
		org.junit.Assert.assertNotNull("Find method for 'Defect' illegally returned null for id '" + id + "'", obj);
		org.junit.Assert.assertEquals("Find method for 'Defect' returned the incorrect identifier", id, obj.getId());
	}

	@Test
	public void testFindAllDefects() {
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly",
				dod.getRandomDefect());
		long count = com.honda.mfg.stamp.conveyor.domain.Defect.countDefects();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Defect', as there are " + count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<com.honda.mfg.stamp.conveyor.domain.Defect> result = com.honda.mfg.stamp.conveyor.domain.Defect
				.findAllDefects();
		org.junit.Assert.assertNotNull("Find all method for 'Defect' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'Defect' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindDefectEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly",
				dod.getRandomDefect());
		long count = com.honda.mfg.stamp.conveyor.domain.Defect.countDefects();
		if (count > 20)
			count = 20;
		java.util.List<com.honda.mfg.stamp.conveyor.domain.Defect> result = com.honda.mfg.stamp.conveyor.domain.Defect
				.findDefectEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'Defect' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'Defect' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		com.honda.mfg.stamp.conveyor.domain.Defect obj = dod.getRandomDefect();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.Defect.findDefect(id);
		org.junit.Assert.assertNotNull("Find method for 'Defect' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyDefect(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'Defect' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
		// org.junit.Assert.assertNotSame(obj, dod.getRandomDefect());
		org.junit.Assert.assertEquals(obj, obj);
	}

	@Test
	public void testMerge() {
		com.honda.mfg.stamp.conveyor.domain.Defect obj = dod.getRandomDefect();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.Defect.findDefect(id);
		boolean modified = dod.modifyDefect(obj);
		Integer currentVersion = obj.getVersion();
		com.honda.mfg.stamp.conveyor.domain.Defect merged = (com.honda.mfg.stamp.conveyor.domain.Defect) obj.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'Defect' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly",
				dod.getRandomDefect());
		com.honda.mfg.stamp.conveyor.domain.Defect obj = dod.getNewTransientDefect(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to provide a new transient entity", obj);
		org.junit.Assert.assertNull("Expected 'Defect' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'Defect' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		com.honda.mfg.stamp.conveyor.domain.Defect obj = dod.getRandomDefect();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Defect' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.Defect.findDefect(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'Defect' with identifier '" + id + "'",
				com.honda.mfg.stamp.conveyor.domain.Defect.findDefect(id));
	}
}
