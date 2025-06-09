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
public class ModelIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private ModelDataOnDemand dod;

	@Test
	public void testCountModels() {
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly",
				dod.getRandomModel());
		long count = Model.countModels();
		org.junit.Assert.assertTrue("Counter for 'Model' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindModel() {
		Model obj = dod.getRandomModel();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to provide an identifier", id);
		obj = Model.findModel(id);
		org.junit.Assert.assertNotNull("Find method for 'Model' illegally returned null for id '" + id + "'", obj);
		org.junit.Assert.assertEquals("Find method for 'Model' returned the incorrect identifier", id, obj.getId());
	}

	@Test
	public void testFindAllModels() {
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly",
				dod.getRandomModel());
		long count = Model.countModels();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Model', as there are " + count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<Model> result = Model.findAllModels();
		org.junit.Assert.assertNotNull("Find all method for 'Model' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'Model' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindModelEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly",
				dod.getRandomModel());
		long count = Model.countModels();
		if (count > 20)
			count = 20;
		java.util.List<Model> result = Model.findModelEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'Model' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'Model' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		Model obj = dod.getRandomModel();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to provide an identifier", id);
		obj = Model.findModel(id);
		org.junit.Assert.assertNotNull("Find method for 'Model' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyModel(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'Model' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMerge() {
		Model obj = dod.getRandomModel();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to provide an identifier", id);
		obj = Model.findModel(id);
		boolean modified = dod.modifyModel(obj);
		Integer currentVersion = obj.getVersion();
		Model merged = (Model) obj.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'Model' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly",
				dod.getRandomModel());
		Model obj = dod.getNewTransientModel(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to provide a new transient entity", obj);
		org.junit.Assert.assertNull("Expected 'Model' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'Model' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		Model obj = dod.getRandomModel();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Model' failed to provide an identifier", id);
		obj = Model.findModel(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'Model' with identifier '" + id + "'", Model.findModel(id));
	}
}
