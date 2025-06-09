package com.honda.mfg.stamp.conveyor.domain;

import java.util.Date;

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
public class CarrierMesIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	private CarrierMesDataOnDemand dod;

	@Test
	public void testUpdateDateIssue() {
		long beforeMS = System.currentTimeMillis();
		long afterMS = beforeMS + 1;
		Date beforeDate = new Date(beforeMS);
		Date afterDate = new Date(afterMS);

		CarrierMes beforeCarrier = new CarrierMes(1000000L);
		CarrierMes afterCarrier = new CarrierMes(1000001L);

		beforeCarrier.setUpdateDate(beforeDate);
		afterCarrier.setUpdateDate(afterDate);

		beforeCarrier.persist();
		afterCarrier.persist();

		CarrierMes fndBefore = CarrierMes.findCarrier(1000000L);
		CarrierMes fndAfter = CarrierMes.findCarrier(1000001L);

		// TODO Set test assertions
//        org.junit.Assert.assertEquals(beforeMS, fndBefore.getUpdateDate().getTime());
//        org.junit.Assert.assertEquals(afterMS, fndAfter.getUpdateDate().getTime());
	}

	@Test
	public void testCountCarriers() {
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly",
				dod.getRandomCarrier());
		long count = CarrierMes.countCarriers();
		org.junit.Assert.assertTrue("Counter for 'Carrier' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindCarrier() {
		CarrierMes obj = dod.getRandomCarrier();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
		obj = CarrierMes.findCarrier(id);
		org.junit.Assert.assertNotNull("Find method for 'Carrier' illegally returned null for id '" + id + "'", obj);
		org.junit.Assert.assertEquals("Find method for 'Carrier' returned the incorrect identifier", id, obj.getId());
	}

	@Test
	public void testFindAllCarriers() {
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly",
				dod.getRandomCarrier());
		long count = CarrierMes.countCarriers();
		org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Carrier', as there are " + count
				+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 250);
		java.util.List<CarrierMes> result = CarrierMes.findAllCarriers();
		org.junit.Assert.assertNotNull("Find all method for 'Carrier' illegally returned null", result);
		org.junit.Assert.assertTrue("Find all method for 'Carrier' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindCarrierEntries() {
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly",
				dod.getRandomCarrier());
		long count = CarrierMes.countCarriers();
		if (count > 20)
			count = 20;
		java.util.List<CarrierMes> result = CarrierMes.findCarrierEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'Carrier' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'Carrier' returned an incorrect number of entries",
				count, result.size());
	}

	@Test
	public void testFindCarrierByCarrierNumber() {
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly",
				dod.getRandomCarrier());
		long count = CarrierMes.countCarriers();
		if (count > 20)
			count = 20;
		java.util.List<CarrierMes> result = CarrierMes.findCarrierEntries(0, (int) count);
		org.junit.Assert.assertNotNull("Find entries method for 'Carrier' illegally returned null", result);
		org.junit.Assert.assertEquals("Find entries method for 'Carrier' returned an incorrect number of entries",
				count, result.size());
		CarrierMes expectedCarrierMes = result.get(0);
		Integer carrierNumber;
		carrierNumber = expectedCarrierMes.getCarrierNumber();
		CarrierMes actualCarrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
		org.junit.Assert.assertEquals(expectedCarrierMes, actualCarrierMes);
	}

	@Test
	public void testFlush() {
		CarrierMes obj = dod.getRandomCarrier();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
		obj = CarrierMes.findCarrier(id);
		org.junit.Assert.assertNotNull("Find method for 'Carrier' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyCarrier(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		org.junit.Assert.assertTrue("Version for 'Carrier' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
		// org.junit.Assert.assertNotSame(obj, dod.getRandomCarrier());
		org.junit.Assert.assertEquals(obj, obj);
	}

	@Test
	public void testMerge() {
		CarrierMes obj = dod.getRandomCarrier();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
		obj = CarrierMes.findCarrier(id);
		boolean modified = dod.modifyCarrier(obj);
		Integer currentVersion = obj.getVersion();
		CarrierMes merged = (CarrierMes) obj.merge();
		obj.flush();
		org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		org.junit.Assert.assertTrue("Version for 'Carrier' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly",
				dod.getRandomCarrier());
		CarrierMes obj = dod.getNewTransientCarrierMes(Integer.MAX_VALUE);
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide a new transient entity", obj);
		org.junit.Assert.assertNull("Expected 'Carrier' identifier to be null", obj.getId());
		obj.persist();
		obj.flush();
		org.junit.Assert.assertNotNull("Expected 'Carrier' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		CarrierMes obj = dod.getRandomCarrier();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to initialize correctly", obj);
		Long id = obj.getId();
		org.junit.Assert.assertNotNull("Data on demand for 'Carrier' failed to provide an identifier", id);
		obj = CarrierMes.findCarrier(id);
		obj.remove();
		obj.flush();
		org.junit.Assert.assertNull("Failed to remove 'Carrier' with identifier '" + id + "'",
				CarrierMes.findCarrier(id));
	}
}
