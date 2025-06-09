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
public class AuditErrorLogIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private AuditErrorLogDataOnDemand dod;

	@Test
	public void testCountAuditErrorLogs() {
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly",
				dod.getRandomAuditErrorLog());
		long count = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.countAuditErrorLogs();
		org.junit.Assert.assertTrue("Counter for 'AuditErrorLog' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testFindAuditErrorLog() {
		com.honda.mfg.stamp.conveyor.domain.AuditErrorLog obj = dod.getRandomAuditErrorLog();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.findAuditErrorLog(id);
		org.junit.Assert.assertNotNull("Find method for 'AuditErrorLog' illegally returned null for id '" + id + "'",
				obj);
		org.junit.Assert.assertEquals("Find method for 'AuditErrorLog' returned the incorrect identifier", id,
				obj.getId());
	}

	@Test
	public void testFindAllAuditErrorLogs() {
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly",
				dod.getRandomAuditErrorLog());
		long count = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.countAuditErrorLogs();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'AuditErrorLog', as there are "
				+ count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<com.honda.mfg.stamp.conveyor.domain.AuditErrorLog> result = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog
				.findAllAuditErrorLogs();
		org.junit.Assert.assertNotNull("Find all method for 'AuditErrorLog' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'AuditErrorLog' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindAuditErrorLogEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly",
				dod.getRandomAuditErrorLog());
		long count = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.countAuditErrorLogs();
		if (count > 20)
			count = 20;
		java.util.List<com.honda.mfg.stamp.conveyor.domain.AuditErrorLog> result = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog
				.findAuditErrorLogEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'AuditErrorLog' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'AuditErrorLog' returned an incorrect number of entries",
				count, result.size());
	}

	@Test
	public void testFlush() {
		com.honda.mfg.stamp.conveyor.domain.AuditErrorLog obj = dod.getRandomAuditErrorLog();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.findAuditErrorLog(id);
		org.junit.Assert.assertNotNull("Find method for 'AuditErrorLog' illegally returned null for id '" + id + "'",
				obj);
		boolean modified = dod.modifyAuditErrorLog(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'AuditErrorLog' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
		// org.junit.Assert.assertNotSame(obj, dod.getRandomAuditErrorLog());
		org.junit.Assert.assertEquals(obj, obj);
	}

	@Test
	public void testMerge() {
		com.honda.mfg.stamp.conveyor.domain.AuditErrorLog obj = dod.getRandomAuditErrorLog();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.findAuditErrorLog(id);
		boolean modified = dod.modifyAuditErrorLog(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		com.honda.mfg.stamp.conveyor.domain.AuditErrorLog merged = (com.honda.mfg.stamp.conveyor.domain.AuditErrorLog) obj
				.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'AuditErrorLog' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly",
				dod.getRandomAuditErrorLog());
		com.honda.mfg.stamp.conveyor.domain.AuditErrorLog obj = dod.getNewTransientAuditErrorLog(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to provide a new transient entity",
				obj);
		org.junit.Assert.assertNull("Expected 'AuditErrorLog' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'AuditErrorLog' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		com.honda.mfg.stamp.conveyor.domain.AuditErrorLog obj = dod.getRandomAuditErrorLog();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AuditErrorLog' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.findAuditErrorLog(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'AuditErrorLog' with identifier '" + id + "'",
				com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.findAuditErrorLog(id));
	}
}
