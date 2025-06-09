package com.honda.mfg.stamp.conveyor.messages;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

/**
 * User: vcc30690 Date: 6/29/11
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierStatusMessageTest {

	@Test
	public void successfullyGetCarrierFromCarrierStatusMessage() {

		loadDieTable();
		loadStopTable();

		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

		carrierMessage.setCarrierNumber("111");
		carrierMessage.setCurrentLocation("1230");
		carrierMessage.setDieNumber("166");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");

		assertNotNull(carrierMessage.getCarrier());

	}

	@Test
	public void successfullyGetCarrierFromCarrierStatusMessage2() {

		loadDieTable();
		loadStopTable();

		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

		carrierMessage.setCarrierNumber("111");
		carrierMessage.setCurrentLocation("1230");
		carrierMessage.setDieNumber("166");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");

		assertNotNull(carrierMessage.getCarrier());
	}

	@Test
	public void successfullyGetCarrierFromCarrierStatusMessage3() {

		loadDieTable();
		loadStopTable();

		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

		carrierMessage.setCarrierNumber("111");
		carrierMessage.setDieNumber("166");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");

		assertNotNull(carrierMessage.getCarrier());
	}

	void loadDieTable() {
		Die die = new Die();
		// die.setDieNumber(166);
		die.setId(166L);
		die.setDescription("left Die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		die.persist();
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
