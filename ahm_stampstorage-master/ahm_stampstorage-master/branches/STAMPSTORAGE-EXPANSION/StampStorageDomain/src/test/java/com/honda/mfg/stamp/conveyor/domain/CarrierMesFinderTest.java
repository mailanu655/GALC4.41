package com.honda.mfg.stamp.conveyor.domain;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

/**
 * Created by IntelliJ IDEA. User: vcc30690 Date: 2/1/12 Time: 11:01 AM To
 * change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierMesFinderTest {

	// @Test
	public void successfullyGetCarriersForHoldCriteria() {
		loadCarrierMesTable();
		StorageRow row = new StorageRow(1, "Row30", 10, 0);
		row.setStop(new Stop(1230L));
		GroupHoldFinderCriteria criteria = new GroupHoldFinderCriteria();
		criteria.setRow(row);
		criteria.setProductionRunNumber(100);
		criteria.setProductionRunDate(new Timestamp(System.currentTimeMillis() - 1000));
		criteria.setNumberAfterRunDate(4);
		criteria.setRowAndProdRun(true);
		criteria.setStatus(CarrierStatus.ON_HOLD);
		criteria.setRobot(Press.REWORK_C_LINE);
		criteria.setNumberBeforeRunDate(4);

		List<CarrierMes> carriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(criteria, 1,
				10);

		assertNotNull(carriers);
		assertEquals(2, carriers.size());
		assertEquals(CarrierStatus.ON_HOLD, criteria.getStatus());
	}

	@Test
	public void successfullyGetCarriersForCarrierCriteria() {
		loadCarrierMesTable();
		CarrierFinderCriteria criteria = new CarrierFinderCriteria();
		criteria.setCurrentLocation(new Stop(1230l));
		criteria.setProductionRunNo(100);
		criteria.setDestination(new Stop(1230l));
		criteria.setCarrierNumber(111);
		criteria.setDie(new Die(166l, PartProductionVolume.HIGH_VOLUME));
		criteria.setCarrierStatus(CarrierStatus.SHIPPABLE);
		criteria.setPress(Press.REWORK_C_LINE);

		List<CarrierMes> carriers = CarrierMes
				.findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(criteria,
						1, 10);

		assertNotNull(carriers);
		assertEquals(1, carriers.size());
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
		mesCarrier1.setOriginationLocation(405);
		mesCarrier1.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		CarrierMes mesCarrier2 = new CarrierMes();
		mesCarrier2.setCarrierNumber(112);
		mesCarrier2.setCurrentLocation(1230L);
		mesCarrier2.setDestination(1230L);
		mesCarrier2.setDieNumber(172);
		mesCarrier2.setQuantity(10);
		mesCarrier2.setStatus(0);
		mesCarrier2.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier2.setProductionRunNumber(100);
		mesCarrier2.setOriginationLocation(405);
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
		mesCarrier3.setOriginationLocation(405);
		mesCarrier3.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		mesCarrier1.persist();
		mesCarrier2.persist();
		mesCarrier3.persist();

	}

}
