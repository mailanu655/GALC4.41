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
public class StorageRowIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private StorageRowDataOnDemand dod;

	@Test
	public void testCountStorageRows() {
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly",
				dod.getRandomStorageRow());
		long count = com.honda.mfg.stamp.conveyor.domain.StorageRow.countStorageRows();
		org.junit.Assert.assertTrue("Counter for 'StorageRow' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindStorageRow() {
		com.honda.mfg.stamp.conveyor.domain.StorageRow obj = dod.getRandomStorageRow();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.StorageRow.findStorageRow(id);
		org.junit.Assert.assertNotNull("Find method for 'StorageRow' illegally returned null for id '" + id + "'", obj);
		org.junit.Assert.assertEquals("Find method for 'StorageRow' returned the incorrect identifier", id,
				obj.getId());
	}

	@Test
	public void testFindAllStorageRows() {
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly",
				dod.getRandomStorageRow());
		long count = com.honda.mfg.stamp.conveyor.domain.StorageRow.countStorageRows();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'StorageRow', as there are " + count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<com.honda.mfg.stamp.conveyor.domain.StorageRow> result = com.honda.mfg.stamp.conveyor.domain.StorageRow
				.findAllStorageRows();
		org.junit.Assert.assertNotNull("Find all method for 'StorageRow' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'StorageRow' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindStorageRowEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly",
				dod.getRandomStorageRow());
		long count = com.honda.mfg.stamp.conveyor.domain.StorageRow.countStorageRows();
		if (count > 20)
			count = 20;
		java.util.List<com.honda.mfg.stamp.conveyor.domain.StorageRow> result = com.honda.mfg.stamp.conveyor.domain.StorageRow
				.findStorageRowEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'StorageRow' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'StorageRow' returned an incorrect number of entries",
				count, result.size());
	}

	@Test
	public void testFlush() {
		com.honda.mfg.stamp.conveyor.domain.StorageRow obj = dod.getRandomStorageRow();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.StorageRow.findStorageRow(id);
		org.junit.Assert.assertNotNull("Find method for 'StorageRow' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyStorageRow(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'StorageRow' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMerge() {
		com.honda.mfg.stamp.conveyor.domain.StorageRow obj = dod.getRandomStorageRow();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.StorageRow.findStorageRow(id);
		boolean modified = dod.modifyStorageRow(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		com.honda.mfg.stamp.conveyor.domain.StorageRow merged = (com.honda.mfg.stamp.conveyor.domain.StorageRow) obj
				.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'StorageRow' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly",
				dod.getRandomStorageRow());
		com.honda.mfg.stamp.conveyor.domain.StorageRow obj = dod.getNewTransientStorageRow(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to provide a new transient entity", obj);
		org.junit.Assert.assertNull("Expected 'StorageRow' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'StorageRow' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		com.honda.mfg.stamp.conveyor.domain.StorageRow obj = dod.getRandomStorageRow();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'StorageRow' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.StorageRow.findStorageRow(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'StorageRow' with identifier '" + id + "'",
				com.honda.mfg.stamp.conveyor.domain.StorageRow.findStorageRow(id));
	}
}
