package com.honda.mfg.stamp.conveyor.domain;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

/**
 * User: VCC30690 Date: 10/3/11
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class DieInventoriesIntegrationTest {

	@Test
	public void testGetDieInventoriesByStopArea() {
		loadDieTable();
		loadStopTable();
		loadCarrierMesTable();
		List<DieInventory> dieInventoryList = CarrierMes.findPartCountsByDiesByStopArea(StopArea.ROW);

		assertEquals(2, dieInventoryList.size());
		assertEquals("left Die", dieInventoryList.get(0).getDieName());
		assertEquals(10l, dieInventoryList.get(0).getQuantity().longValue());
		assertEquals(null, dieInventoryList.get(0).getHoldQuantity());
		assertEquals(null, dieInventoryList.get(0).getInspectionRequiredQuantity());

		assertEquals("Right Die", dieInventoryList.get(1).getDieName());
		assertEquals(20l, dieInventoryList.get(1).getQuantity().longValue());
		assertEquals(10l, dieInventoryList.get(1).getHoldQuantity().longValue());
		assertEquals(null, dieInventoryList.get(1).getInspectionRequiredQuantity());
	}

	private void loadCarrierMesTable() {
		CarrierMes mesCarrier1 = new CarrierMes();
		mesCarrier1.setCarrierNumber(111);
		mesCarrier1.setCurrentLocation(1230L);
		mesCarrier1.setDestination(1230L);
		mesCarrier1.setDieNumber(166);
		mesCarrier1.setQuantity(10);
		mesCarrier1.setStatus(0);
		mesCarrier1.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier1.setProductionRunNumber(100);
		mesCarrier1.setOriginationLocation(0);
		mesCarrier1.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		CarrierMes mesCarrier2 = new CarrierMes();
		mesCarrier2.setCarrierNumber(112);
		mesCarrier2.setCurrentLocation(1230L);
		mesCarrier2.setDestination(1230L);
		mesCarrier2.setDieNumber(172);
		mesCarrier2.setQuantity(10);
		mesCarrier2.setStatus(1);
		mesCarrier2.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier2.setProductionRunNumber(100);
		mesCarrier2.setOriginationLocation(0);
		mesCarrier2.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		CarrierMes mesCarrier3 = new CarrierMes();
		mesCarrier3.setCarrierNumber(113);
		mesCarrier3.setCurrentLocation(513L);
		mesCarrier3.setDestination(1232L);
		mesCarrier3.setDieNumber(172);
		mesCarrier3.setQuantity(10);
		mesCarrier3.setStatus(0);
		mesCarrier3.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier3.setProductionRunNumber(100);
		mesCarrier3.setOriginationLocation(0);
		mesCarrier3.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		mesCarrier1.persist();
		mesCarrier2.persist();
		mesCarrier3.persist();

	}

	void loadDieTable() {
		Die die = new Die();
		die.setId(166L);
		die.setDescription("left Die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Die die1 = new Die();
		die1.setId(172L);
		die1.setDescription("Right Die");
		die1.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		die.persist();
		die1.persist();
	}

	void loadStopTable() {
		Stop stop = new Stop();
		stop.setId(1230L);
		stop.setName("ST12-30");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.ROW);

		Stop stop1 = new Stop();
		stop1.setId(1232L);
		stop1.setName("ST12-32");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.ROW);

		stop.persist();
		stop1.persist();
	}
}
