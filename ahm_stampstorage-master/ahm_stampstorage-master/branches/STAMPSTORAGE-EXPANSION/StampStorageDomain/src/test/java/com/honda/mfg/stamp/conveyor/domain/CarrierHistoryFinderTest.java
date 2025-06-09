package com.honda.mfg.stamp.conveyor.domain;

import static junit.framework.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;
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
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

/**
 * Created by IntelliJ IDEA. User: vcc30690 Date: 2/1/12 Time: 11:01 AM To
 * change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierHistoryFinderTest {

	Die die, die1;
	Stop stop, stop1, stop2;

	@Test
	public void successfullyGetCarrierHistoryForCriteria() {
		loadDieTable();
		loadStopTable();
		loadCarrierHistoryTable();

		CarrierHistoryFinderCriteria carrierHistoryFinderCriteria = new CarrierHistoryFinderCriteria();
		carrierHistoryFinderCriteria.setCarrierNumber(111);
		carrierHistoryFinderCriteria.setCarrierStatus(CarrierStatus.SHIPPABLE);
		carrierHistoryFinderCriteria.setCurrentLocation(stop);
		carrierHistoryFinderCriteria.setDestination(stop);
		carrierHistoryFinderCriteria.setDie(die);
		carrierHistoryFinderCriteria.setProductionRunNo(100);
		carrierHistoryFinderCriteria.setPress(Press.REWORK_C_LINE);

		List<CarrierHistory> carrierHistoryList = CarrierHistory
				.findCarrierHistoryByCarrierNumber(carrierHistoryFinderCriteria, 1, 1);

		assertEquals(1, carrierHistoryList.size());
		assertEquals(Press.REWORK_C_LINE, carrierHistoryList.get(0).getPress());
		assertEquals(111, carrierHistoryList.get(0).getCarrierNumber().intValue());
		assertEquals(stop, carrierHistoryList.get(0).getCurrentLocation());
		assertEquals(stop, carrierHistoryList.get(0).getDestination());
		assertEquals(die, carrierHistoryList.get(0).getDieNumber());
		assertEquals(10, carrierHistoryList.get(0).getQuantity().intValue());
		assertEquals(CarrierStatus.SHIPPABLE, carrierHistoryList.get(0).getStatus());
		assertEquals(100, carrierHistoryList.get(0).getProductionRunNumber().intValue());
		assertEquals(405, carrierHistoryList.get(0).getOriginationLocation().intValue());
	}

	private void loadCarrierHistoryTable() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);

		Timestamp timestamp1 = new Timestamp(c.getTimeInMillis());

		CarrierHistory mesCarrier1 = new CarrierHistory();
		mesCarrier1.setCarrierNumber(111);
		mesCarrier1.setCurrentLocation(stop.getId());
		mesCarrier1.setDestination(stop.getId());
		mesCarrier1.setDieNumber(die.getId());
		mesCarrier1.setQuantity(10);
		mesCarrier1.setStatus(CarrierStatus.SHIPPABLE);
		mesCarrier1.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier1.setProductionRunNumber(100);
		mesCarrier1.setOriginationLocation(405);
		mesCarrier1.setUpdateDate(timestamp1);
		mesCarrier1.setBuffer(1);
		mesCarrier1.setCarrierMesArchiveTstp(timestamp1);

		CarrierHistory mesCarrier2 = new CarrierHistory();
		mesCarrier2.setCarrierNumber(111);
		mesCarrier2.setCurrentLocation(stop.getId());
		mesCarrier2.setDestination(stop.getId());
		mesCarrier2.setDieNumber(die1.getId());
		mesCarrier2.setQuantity(10);
		mesCarrier2.setStatus(CarrierStatus.SHIPPABLE);
		mesCarrier2.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier2.setProductionRunNumber(100);
		mesCarrier2.setOriginationLocation(405);
		mesCarrier2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier2.setBuffer(1);
		mesCarrier2.setCarrierMesArchiveTstp(new Timestamp(System.currentTimeMillis()));

		CarrierHistory mesCarrier3 = new CarrierHistory();
		mesCarrier3.setCarrierNumber(113);
		mesCarrier3.setCurrentLocation(stop2.getId());
		mesCarrier3.setDestination(stop1.getId());
		mesCarrier3.setDieNumber(die1.getId());
		mesCarrier3.setQuantity(10);
		mesCarrier3.setStatus(CarrierStatus.SHIPPABLE);
		mesCarrier3.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier3.setProductionRunNumber(100);
		mesCarrier3.setOriginationLocation(405);
		mesCarrier3.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier3.setBuffer(1);
		mesCarrier3.setCarrierMesArchiveTstp(new Timestamp(System.currentTimeMillis()));

		mesCarrier1.persist();
		mesCarrier2.persist();
		mesCarrier3.persist();

	}

	void loadDieTable() {
		die = new Die();
		die.setId(166L);
		die.setDescription("left Die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		die.persist();

		die1 = new Die();
		die1.setId(172L);
		die1.setDescription("right Die");
		die1.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		die1.persist();
	}

	void loadStopTable() {
		stop = new Stop();
		stop.setId(1230L);
		stop.setName("ST12-30");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.findByType(0));

		stop1 = new Stop();
		stop1.setId(1232L);
		stop1.setName("ST12-32");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.findByType(0));

		stop2 = new Stop();
		stop2.setId(513L);
		stop2.setName("ST5-13");
		stop2.setStopType(StopType.STORE_IN_ALL_LANES);
		stop2.setStopArea(StopArea.findByType(0));

		stop.persist();
		stop1.persist();
		stop2.persist();
	}

}
