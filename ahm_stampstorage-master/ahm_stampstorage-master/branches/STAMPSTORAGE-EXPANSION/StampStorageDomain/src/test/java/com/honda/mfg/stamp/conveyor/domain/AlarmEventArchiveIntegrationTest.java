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
public class AlarmEventArchiveIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private AlarmEventArchiveDataOnDemand dod;

	@Test
	public void testCountArchivedAlarms() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to initialize correctly",
				dod.getRandomArchivedAlarm());
		long count = AlarmEventArchive.countArchivedAlarms();
		org.junit.Assert.assertTrue("Counter for 'AlarmEventArchive' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testFindCurrent_Alarm() {
		AlarmEventArchive obj = dod.getRandomArchivedAlarm();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to provide an identifier", id);
		obj = AlarmEventArchive.findArchivedAlarm(id);
		org.junit.Assert
				.assertNotNull("Find method for 'AlarmEventArchive' illegally returned null for id '" + id + "'", obj);
		org.junit.Assert.assertEquals("Find method for 'AlarmEventArchive' returned the incorrect identifier", id,
				obj.getId());
	}

	@Test
	public void testFindAllArchivedAlarms() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to initialize correctly",
				dod.getRandomArchivedAlarm());
		long count = AlarmEventArchive.countArchivedAlarms();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'AlarmEventArchive', as there are "
				+ count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<AlarmEventArchive> result = AlarmEventArchive.findAllArchivedAlarms();
		org.junit.Assert.assertNotNull("Find all method for 'AlarmEventArchive' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'AlarmEventArchive' failed to return any data",
				result.size() > 0);
	}

	@Test
	public void testFindArchivedAlarmEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to initialize correctly",
				dod.getRandomArchivedAlarm());
		long count = AlarmEventArchive.countArchivedAlarms();
		if (count > 20)
			count = 20;
		java.util.List<AlarmEventArchive> result = AlarmEventArchive.findArchivedAlarmEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'AlarmEventArchive' illegally returned null", result);
		org.junit.Assert.assertEquals(
				"Find entries method for 'AlarmEventArchive' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		AlarmEventArchive obj = dod.getRandomArchivedAlarm();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to provide an identifier", id);
		obj = AlarmEventArchive.findArchivedAlarm(id);
		org.junit.Assert
				.assertNotNull("Find method for 'AlarmEventArchive' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyArchivedAlarm(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'AlarmEventArchive' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmEventArchive' failed to initialize correctly",
				dod.getRandomArchivedAlarm());
		AlarmEventArchive obj = dod.getNewTransientArchivedAlarm(Integer.MAX_VALUE);
		org.junit.Assert
				.assertNotNull("Data on demand for 'AlarmEventArchive' failed to provide a new transient entity", obj);
		org.junit.Assert.assertNull("Expected 'AlarmEventArchive' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'AlarmEventArchive' identifier to no longer be null", obj.getId());
	}
}
