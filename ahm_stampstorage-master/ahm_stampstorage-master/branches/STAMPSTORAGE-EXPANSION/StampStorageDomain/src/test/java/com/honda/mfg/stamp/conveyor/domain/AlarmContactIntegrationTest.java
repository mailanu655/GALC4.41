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
public class AlarmContactIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private AlarmContactDataOnDemand dod;

	@Test
	public void testCountAlarmContacts() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly",
				dod.getRandomAlarmContact());
		long count = com.honda.mfg.stamp.conveyor.domain.AlarmContact.countAlarmContacts();
		org.junit.Assert.assertTrue("Counter for 'AlarmContact' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindAlarmContact() {
		com.honda.mfg.stamp.conveyor.domain.AlarmContact obj = dod.getRandomAlarmContact();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AlarmContact.findAlarmContact(id);
		org.junit.Assert.assertNotNull("Find method for 'AlarmContact' illegally returned null for id '" + id + "'",
				obj);
		org.junit.Assert.assertEquals("Find method for 'AlarmContact' returned the incorrect identifier", id,
				obj.getId());
	}

	@Test
	public void testFindAllAlarmContacts() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly",
				dod.getRandomAlarmContact());
		long count = com.honda.mfg.stamp.conveyor.domain.AlarmContact.countAlarmContacts();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'AlarmContact', as there are " + count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<com.honda.mfg.stamp.conveyor.domain.AlarmContact> result = com.honda.mfg.stamp.conveyor.domain.AlarmContact
				.findAllAlarmContacts();
		org.junit.Assert.assertNotNull("Find all method for 'AlarmContact' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'AlarmContact' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindAlarmContactEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly",
				dod.getRandomAlarmContact());
		long count = com.honda.mfg.stamp.conveyor.domain.AlarmContact.countAlarmContacts();
		if (count > 20)
			count = 20;
		java.util.List<com.honda.mfg.stamp.conveyor.domain.AlarmContact> result = com.honda.mfg.stamp.conveyor.domain.AlarmContact
				.findAlarmContactEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'AlarmContact' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'AlarmContact' returned an incorrect number of entries",
				count, result.size());
	}

	@Test
	public void testFlush() {
		com.honda.mfg.stamp.conveyor.domain.AlarmContact obj = dod.getRandomAlarmContact();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AlarmContact.findAlarmContact(id);
		org.junit.Assert.assertNotNull("Find method for 'AlarmContact' illegally returned null for id '" + id + "'",
				obj);
		boolean modified = dod.modifyAlarmContact(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'AlarmContact' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMerge() {
		com.honda.mfg.stamp.conveyor.domain.AlarmContact obj = dod.getRandomAlarmContact();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AlarmContact.findAlarmContact(id);
		boolean modified = dod.modifyAlarmContact(obj);
		java.lang.Integer currentVersion = obj.getVersion();
		com.honda.mfg.stamp.conveyor.domain.AlarmContact merged = (com.honda.mfg.stamp.conveyor.domain.AlarmContact) obj
				.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'AlarmContact' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly",
				dod.getRandomAlarmContact());
		com.honda.mfg.stamp.conveyor.domain.AlarmContact obj = dod.getNewTransientAlarmContact(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to provide a new transient entity",
				obj);
		org.junit.Assert.assertNull("Expected 'AlarmContact' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'AlarmContact' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		com.honda.mfg.stamp.conveyor.domain.AlarmContact obj = dod.getRandomAlarmContact();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to initialize correctly", obj);
		java.lang.Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'AlarmContact' failed to provide an identifier", id);
		obj = com.honda.mfg.stamp.conveyor.domain.AlarmContact.findAlarmContact(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'AlarmContact' with identifier '" + id + "'",
				com.honda.mfg.stamp.conveyor.domain.AlarmContact.findAlarmContact(id));
	}
}
