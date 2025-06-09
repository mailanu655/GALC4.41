package com.honda.mfg.stamp.conveyor.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.sql.Timestamp;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;

/**
 * User: Jeffrey M Lutz Date: 2/14/11
 */
public class CarrierTest {

	@Test
	public void equalityCheckTwoCarriersWithSameIdAndNullStampedPart() {
		Carrier c1 = new Carrier(1);
		Carrier c2 = new Carrier(1);
		assertEquals(c1, c2);
	}

	@Test
	public void equalityCheckTwoCarriersWithSameIdAndOneCarrierHasAValidStampedPart() {
		Carrier c1 = new Carrier(1, new Die(2L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier c2 = new Carrier(1);
		assertEquals(c1, c2);
	}

	@Test
	public void inequalityCheckTwoCarriersWithSameId() {
		Carrier c1 = new Carrier(1);
		Carrier c2 = new Carrier(2);
		assertNotSame(c1, c2);
	}

	@Test
	public void successfullyReturnsSinglePartTypeForOneCarrierInTheLane() {
		Long expectedPartType = 2L;
		Die die = new Die(expectedPartType, PartProductionVolume.HIGH_VOLUME);
		// die.setDieNumber(expectedPartType);
		Carrier c1 = new Carrier(1, die, 3, null, null);
		Long actualPartType;
		actualPartType = c1.getDie().getId();
		assertEquals(expectedPartType, actualPartType);
		assertEquals(3, c1.getQuantity().intValue());
	}

	@Test
	public void successfullyConstructEmptyCarrier() {
		Die part1 = new Die(1L, PartProductionVolume.HIGH_VOLUME);
		Carrier c1 = new Carrier(1);
		assertEquals(0, c1.getQuantity().intValue());
	}

	@Test
	public void successfullyConstructCarrierWithPartAndQuantity() {
		Die die1 = new Die(1L, PartProductionVolume.HIGH_VOLUME);
		Carrier c1 = new Carrier(1, die1, 3, null, null);
		assertEquals(3, c1.getQuantity().intValue());
	}

	@Test
	public void successfullyConstructEmptyCarrierAndLoadWithParts() {
		Die part1 = new Die(1L, PartProductionVolume.HIGH_VOLUME);

		Carrier c1 = new Carrier(1);

		assertEquals(0, c1.getQuantity().intValue());
		c1.setDie(part1);
		c1.setQuantity(3);
		assertEquals(3, c1.getQuantity().intValue());
	}

	@Test
	public void testEquals() {

		long id = 0;
		Carrier carrier;
		Timestamp productionRunTimestamp = new Timestamp(System.currentTimeMillis());
		Integer prodRunNo = 108;
		Stop currentLocation = new Stop("05-13");
		Stop destination = new Stop("05-13");
		CarrierStatus carrierStatus = CarrierStatus.ON_HOLD;
		Integer carrierNumber = 123;

		carrier = new Carrier(id, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus,
				carrierNumber, new Die(1L, PartProductionVolume.HIGH_VOLUME));
		carrier.setQuantity(10);

		Carrier carrier1 = new Carrier(id, productionRunTimestamp, prodRunNo, currentLocation, destination,
				carrierStatus, carrierNumber, new Die(1L, PartProductionVolume.HIGH_VOLUME));
		carrier1.setQuantity(10);

		assertEquals(carrier1, carrier);
	}
}
