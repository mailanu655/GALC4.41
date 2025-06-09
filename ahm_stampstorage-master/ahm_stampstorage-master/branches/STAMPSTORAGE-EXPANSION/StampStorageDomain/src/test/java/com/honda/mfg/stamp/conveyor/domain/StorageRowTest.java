package com.honda.mfg.stamp.conveyor.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 5/23/13 Time: 2:25 PM To
 * change this template use File | Settings | File Templates.
 */
public class StorageRowTest {

	@Test
	public void equalityCheckTest() {
		StorageRow lane1 = new StorageRow(1, "test_lane", 10, 1);
		StorageRow lane2 = new StorageRow(1, "Something else", 10, 1);
		// assertEquals(lane1, lane2);
	}

	@Test
	public void inequalityCheckForMaxCapacitiesTest() {
		StorageRow lane1 = new StorageRow(1, "test_lane", 10, 1);
		StorageRow lane2 = new StorageRow(1, "Something else", 11, 1);
		// assertFalse("How can two lanes of different max capacities be the same?",
		// lane1.isLoadedLike(lane2));
	}

	@Test
	public void inequalityCheckForAssociatedStopsTest() {
		Stop oneStop = new Stop("one");
		Stop twoStop = new Stop("two");
		StorageRow lane1 = new StorageRow(1, "test_lane", 10, 1);
		StorageRow lane2 = new StorageRow(1, "Something else", 10, 1);
		lane2.store(new Carrier(1));
		// assertFalse("How can two lanes of different max capacities be the same?",
		// lane1.isLoadedLike(lane2));
	}

	@Test
	public void addCarrierToEmptyLaneAndReleaseSameCarrier() {
		StorageRow l = new StorageRow(1, "test_lane", 1, 1);
		Carrier expectedCarrier = new Carrier(100, new Die(1L, PartProductionVolume.HIGH_VOLUME), 1, null, 101);
		assertEquals(0, l.getCurrentCarrierCount());
		assertTrue(l.isEmpty());
		assertFalse(l.isFull());
		l.store(expectedCarrier);
		assertTrue(l.isFull());
		assertFalse(l.isEmpty());
		assertEquals(1, l.getCurrentCarrierCount());
		Carrier actualCarrier = l.release();
		assertTrue(l.isEmpty());
		assertFalse(l.isFull());
		assertEquals(expectedCarrier, actualCarrier);
		assertEquals(0, l.getCurrentCarrierCount());
	}

	@Test
	public void addTwoCarriersToEmptyLaneAndReleaseSameTwoCarriersInProperSequence() {
		StorageRow l = new StorageRow(1, "test_lane", 10, 1);
		Carrier expectedCarrier_1 = new Carrier(100, new Die(15L, PartProductionVolume.HIGH_VOLUME), 1, null, 101);
		Carrier expectedCarrier_2 = new Carrier(200, new Die(15L, PartProductionVolume.HIGH_VOLUME), 1, null, 101);

		assertEquals(0, l.getCurrentCarrierCount());
		l.store(expectedCarrier_1);
		assertEquals(1, l.getCurrentCarrierCount());
		l.store(expectedCarrier_2);
		assertEquals(2, l.getCurrentCarrierCount());

		assertEquals(expectedCarrier_1, l.release());
		assertEquals(1, l.getCurrentCarrierCount());
		assertEquals(expectedCarrier_2, l.release());
		assertEquals(0, l.getCurrentCarrierCount());
	}

	@Test(expected = IllegalStateException.class)
	public void throwsExceptionWhenAttemptingToStoreCarrierInLaneThatExceedsLaneCapacity() {
		StorageRow l = new StorageRow(1, "test_lane", 1, 1);
		Carrier expectedCarrier_1 = new Carrier(100, new Die(15L, PartProductionVolume.HIGH_VOLUME), 1, null, 101);
		Carrier expectedCarrier_2 = new Carrier(200, new Die(15L, PartProductionVolume.MEDIUM_VOLUME), 1, null, 101);
		l.store(expectedCarrier_1);
		l.store(expectedCarrier_2);
	}

	@Test
	public void successfullyReturnsTheStartAndEndOfTheQueue() {
		StorageRow l = new StorageRow(1, "test_lane", 2, 1);
		Carrier expectedCarrier_1 = new Carrier(100, new Die(15L, PartProductionVolume.HIGH_VOLUME), 1, null, 101);
		Carrier expectedCarrier_2 = new Carrier(200, new Die(15L, PartProductionVolume.MEDIUM_VOLUME), 1, null, 101);
		l.store(expectedCarrier_1);
		l.store(expectedCarrier_2);

		assertEquals(expectedCarrier_2, l.getCarrierAtLaneIn());
		assertEquals(expectedCarrier_1, l.getCarrierAtRowOut());
	}

	@Test
	public void successfullyReturnsPartTypesForAllCarriers() {
		StorageRow l = new StorageRow(1, "test_lane", 3, 1);
		Carrier expectedCarrier_1 = new Carrier(100, new Die(151L, PartProductionVolume.HIGH_VOLUME), 1, null, 101);
		Carrier expectedCarrier_2 = new Carrier(200, new Die(152L, PartProductionVolume.MEDIUM_VOLUME), 1, null, 101);
		Carrier expectedCarrier_3 = new Carrier(300, new Die(null, PartProductionVolume.MEDIUM_VOLUME), 1, null, 101);

		l.store(expectedCarrier_1);
		l.store(expectedCarrier_2);
		l.store(expectedCarrier_3);

		Set<Long> allPartTypes = l.getDieNumbersForAllCarriers();
		assertEquals(3, allPartTypes.size());
		assertTrue(allPartTypes.contains(151L));
	}

	@Test
	public void successfullyVerifiesThatALaneContainsACarrier() {
		StorageRow l = new StorageRow(1, "test_lane", 1, 1);
		Carrier expectedCarrier = new Carrier(100, new Die(15L, PartProductionVolume.HIGH_VOLUME), 1, null, null);

		assertFalse(l.carrierExistsInRow(expectedCarrier));
		l.store(expectedCarrier);
		assertTrue(l.carrierExistsInRow(expectedCarrier));
	}

	@Test
	public void successfullyReturnsIfLaneIsEmpty() {
		StorageRow l = new StorageRow(1, "test_lane", 1, 1);
		assertTrue(l.isEmpty());
		assertFalse(l.isFull());
	}

	@Test
	public void successfullyReturnsIfLaneIsFull() {
		StorageRow l = new StorageRow(1, "test_lane", 1, 1);
		Carrier expectedCarrier = new Carrier(100, new Die(15L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(expectedCarrier);
		assertFalse(l.isEmpty());
		assertTrue(l.isFull());
	}

	@Test
	public void successfullyReturnsIfLaneIsPartial() {
		StorageRow l = new StorageRow(1, "test_lane", 3, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_2 = new Carrier(101, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(carrier_1);
		l.store(carrier_2);
		assertTrue(!l.isEmpty() && !l.isFull());
	}

	@Test
	public void successfullyReturnsIfLaneIsMixed() {
		StorageRow l = new StorageRow(1, "test_lane", 3, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_2 = new Carrier(101, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(carrier_1);
		l.store(carrier_2);
		assertTrue(l.getDieNumbersForAllCarriers().size() > 1);
	}

	@Test
	public void successfullyFiltersLaneThatIsPartialCapacityAndInMixedLaneWithDifferentPartTypeInCarrierAtEnd() {
		StorageRow l = new StorageRow(1, "test_lane", 4, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_2 = new Carrier(101, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_3 = new Carrier(102, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(carrier_1);
		l.store(carrier_2);
		l.store(carrier_3);
		assertTrue(!l.isEmpty() && !l.isFull() && l.getDieNumbersForAllCarriers().size() > 1);
		assertTrue(!l.getCarrierAtRowOut().getDie().equals(l.getCarrierAtLaneIn().getDie()));

	}

	@Test
	public void successfullyReturnsOldestProductionRunCarrierForPartType() {
		// Pre-condition checks
		StorageRow lane = buildLane();
		StorageRow lane1 = buildLaneWithOnHoldCarriers();

		// Perform test
		Carrier carrier = lane.getOldestProductionRunCarrierForDieNumber(101L);
		Carrier carrier2 = lane1.getOldestProductionRunCarrierForDieNumber(101L);

		// Post-condition validation / assertions
		assertEquals(new Integer(2), carrier.getCarrierNumber());
		assertEquals(null, carrier2);
	}

	private StorageRow buildLane() {
		int laneId = 1;
		String laneName = "test_lane";
		int maxCapacity = 4;
		StorageRow lane = new StorageRow(laneId, laneName, maxCapacity, 1);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Long dieNumber = 101L;
		int productionRunNumber = 10;
		Carrier c = buildCarrier(1, dieNumber, now, productionRunNumber);
		c.setCarrierStatus(CarrierStatus.SHIPPABLE);
		Carrier c1 = buildCarrier(2, dieNumber, getPreviousDay(now), productionRunNumber - 1);
		c1.setCarrierStatus(CarrierStatus.SHIPPABLE);
		Carrier c2 = buildCarrier(3, dieNumber + 1, getPreviousDay(getPreviousDay(now)), productionRunNumber - 2);
		c2.setCarrierStatus(CarrierStatus.SHIPPABLE);
		lane.store(c);
		lane.store(c1);
		lane.store(c2);
		return lane;
	}

	private StorageRow buildLaneWithOnHoldCarriers() {
		int laneId = 1;
		String laneName = "test_lane";
		int maxCapacity = 4;
		StorageRow lane = new StorageRow(laneId, laneName, maxCapacity, 1);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Long dieNumber = 101L;
		int productionRunNumber = 10;
		Carrier c = buildCarrier(1, dieNumber, now, productionRunNumber);
		c.setCarrierStatus(CarrierStatus.ON_HOLD);
		Carrier c1 = buildCarrier(2, dieNumber, getPreviousDay(now), productionRunNumber - 1);
		c1.setCarrierStatus(CarrierStatus.ON_HOLD);
		lane.store(c);
		lane.store(c1);
		lane.store(buildCarrier(3, dieNumber, getPreviousDay(getPreviousDay(now)), productionRunNumber - 2));
		return lane;
	}

	private Carrier buildCarrier(long id, Long dieNumber, Timestamp productionRunTimestamp,
			Integer productionRunNumber) {
		Carrier c = new Carrier(id, new Die(dieNumber, PartProductionVolume.HIGH_VOLUME), 1, productionRunTimestamp,
				productionRunNumber);
		c.setCarrierStatus(CarrierStatus.SHIPPABLE);
		return c;
	}

	private Timestamp getPreviousDay(Timestamp now) {
		return new Timestamp(now.getTime() - (1000 * 60 * 60 * 24));
	}

	private Timestamp getThreeMinutesBefore(Timestamp now) {
		return new Timestamp(now.getTime() - (1000 * 60 * 3));
	}

	@Test
	public void successfullyReturnsIfLaneHasCarriersOfProdRunNo() {
		Calendar c1 = Calendar.getInstance();
		Timestamp timestamp1 = new Timestamp(c1.getTimeInMillis());
		c1.add(Calendar.DATE, -1);
		Timestamp timestamp2 = new Timestamp(c1.getTimeInMillis());
		c1.add(Calendar.MINUTE, -3);
		Timestamp timestamp3 = new Timestamp(c1.getTimeInMillis());
		StorageRow l = new StorageRow(1, "test_lane", 3, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, timestamp1,
				new Integer(101));
		Carrier carrier_2 = new Carrier(101, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, timestamp2,
				new Integer(102));
		Carrier carrier_3 = new Carrier(102, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, timestamp3,
				new Integer(101));
		l.store(carrier_1);
		l.store(carrier_2);
		l.store(carrier_3);
		assertEquals(LaneCondition.FULL, l.getLaneConditionByDie(new Die(10L, PartProductionVolume.HIGH_VOLUME)));
		assertTrue(l.hasCarrierOfProdRunNo(new Integer(101)));
	}

	@Test
	public void attemptingToFindIfLaneHasCarriersOfProdRunNoForUnknownProdRunNo() {
		Calendar c1 = Calendar.getInstance();
		Timestamp timestamp1 = new Timestamp(c1.getTimeInMillis());
		c1.add(Calendar.DATE, -1);
		Timestamp timestamp2 = new Timestamp(c1.getTimeInMillis());
		c1.add(Calendar.MINUTE, -3);
		Timestamp timestamp3 = new Timestamp(c1.getTimeInMillis());
		StorageRow l = new StorageRow(1, "test_lane", 4, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, timestamp1,
				new Integer(101));
		Carrier carrier_2 = new Carrier(101, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, timestamp2,
				new Integer(102));
		Carrier carrier_3 = new Carrier(102, new Die(20L, PartProductionVolume.HIGH_VOLUME), 1, timestamp3,
				new Integer(101));
		carrier_1.setCarrierStatus(CarrierStatus.SHIPPABLE);
		carrier_2.setCarrierStatus(CarrierStatus.SHIPPABLE);
		carrier_3.setCarrierStatus(CarrierStatus.SHIPPABLE);
		l.store(carrier_1);
		l.store(carrier_2);
		l.store(carrier_3);

		assertEquals(LaneCondition.MIXED_BLOCK,
				l.getLaneConditionByDie(new Die(20L, PartProductionVolume.HIGH_VOLUME)));
		assertEquals(LaneCondition.MIXED_FRONT,
				l.getLaneConditionByDie(new Die(10L, PartProductionVolume.HIGH_VOLUME)));
		assertFalse(l.hasCarrierOfProdRunNo(null));
	}

	@Test
	public void successfullyReturnsCountOfCarriersAtLaneOutEndForPartType() {
		StorageRow l = new StorageRow(1, "test_lane", 8, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_2 = new Carrier(101, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_3 = new Carrier(102, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_4 = new Carrier(103, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_5 = new Carrier(104, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_6 = new Carrier(105, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_7 = new Carrier(106, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(carrier_1);
		l.store(carrier_2);
		l.store(carrier_3);
		l.store(carrier_4);
		l.store(carrier_5);
		l.store(carrier_6);
		l.store(carrier_7);

		assertEquals(new Integer(3), l.getCountOfCarriersAtLaneOutEndForDieNumber(10L));
	}

	@Test
	public void attemptingToFindCountOfCarriersAtLaneOutEndForPartTypeForUnknownPart() {
		StorageRow l = new StorageRow(1, "test_lane", 8, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_2 = new Carrier(101, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_3 = new Carrier(102, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_4 = new Carrier(103, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_5 = new Carrier(104, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_6 = new Carrier(105, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_7 = new Carrier(106, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(carrier_1);
		l.store(carrier_2);
		l.store(carrier_3);
		l.store(carrier_4);
		l.store(carrier_5);
		l.store(carrier_6);
		l.store(carrier_7);
		assertEquals(0, l.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(3L).intValue());
	}

	@Test
	public void successfullyReturnsCountOfCarriersBlockingPartTypeAtLaneOutEnd() {
		StorageRow l = new StorageRow(1, "test_lane", 8, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_2 = new Carrier(101, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_3 = new Carrier(102, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_4 = new Carrier(103, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_5 = new Carrier(104, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_6 = new Carrier(105, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_7 = new Carrier(106, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(carrier_1);
		l.store(carrier_2);
		l.store(carrier_3);
		l.store(carrier_4);
		l.store(carrier_5);
		l.store(carrier_6);
		l.store(carrier_7);
		assertEquals(new Integer(3), l.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(11L));
	}

	@Test
	public void attemptingToFindCountOfCarriersBlockingPartTypeAtLaneOutEndForUnknownPart() {
		StorageRow l = new StorageRow(1, "test_lane", 8, 1);
		Carrier carrier_1 = new Carrier(100, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_2 = new Carrier(101, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_3 = new Carrier(102, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_4 = new Carrier(103, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_5 = new Carrier(104, new Die(11L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_6 = new Carrier(105, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		Carrier carrier_7 = new Carrier(106, new Die(10L, PartProductionVolume.HIGH_VOLUME), 1, null, null);
		l.store(carrier_1);
		l.store(carrier_2);
		l.store(carrier_3);
		l.store(carrier_4);
		l.store(carrier_5);
		l.store(carrier_6);
		l.store(carrier_7);
		assertEquals(0, l.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(3L).intValue());
	}

	@Test
	public void testEquals() {
		int id = 0;
		Carrier carrier;
		Timestamp productionRunTimestamp = new Timestamp(System.currentTimeMillis());
		Integer prodRunNo = 108;
		Stop currentLocation = new Stop("05-13");
		Stop destination = new Stop("05-13");
		CarrierStatus carrierStatus = CarrierStatus.ON_HOLD;
		Integer carrierNumber = 123;

		carrier = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus,
				carrierNumber, new Die(3L, PartProductionVolume.HIGH_VOLUME));
		carrier.setQuantity(10);

		Carrier carrier1 = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination,
				carrierStatus, carrierNumber, new Die(3L, PartProductionVolume.HIGH_VOLUME));
		carrier1.setQuantity(10);

		StorageRow laneImpl1 = new StorageRow(0, "lane0", 12, 1);

		laneImpl1.store(carrier);
		laneImpl1.store(carrier1);

		StorageRow laneImpl2 = new StorageRow(0, "lane0", 12, 1);

		laneImpl2.store(carrier);
		laneImpl2.store(carrier1);

		// assertEquals(laneImpl1, laneImpl2);
		// assertEquals(true, laneImpl1.isLoadedLike(laneImpl2));
	}

	@Test
	public void tryingToStoreSameCarrierTwice() {
		int id = 0;
		Carrier carrier;
		Timestamp productionRunTimestamp = new Timestamp(System.currentTimeMillis());
		Integer prodRunNo = 108;
		Stop currentLocation = new Stop("05-13");
		Stop destination = new Stop("05-13");
		CarrierStatus carrierStatus = CarrierStatus.ON_HOLD;
		Integer carrierNumber = 123;

		carrier = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus,
				carrierNumber, new Die(3L, PartProductionVolume.HIGH_VOLUME));
		carrier.setQuantity(10);

		StorageRow laneImpl1 = new StorageRow(0, "lane0", 12, 1);

		laneImpl1.store(carrier);
		laneImpl1.store(carrier);

		assertEquals(1, laneImpl1.getCurrentCarrierCount());

	}

}
