package com.honda.mfg.stamp.conveyor.manager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;

/**
 * User: Jeffrey M Lutz Date: 3/4/11
 */
public class RowMatchersTest {

	@Test
	public void successfullyFindLaneWithMaxCapacity() {
		List<StorageRow> laneImpls = LaneOM.lanes;
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasMaxCapacityOf(LaneOM.MAX_CAPACITY), laneImpls);

		assertEquals(LaneOM.firstLaneImpl, actualLaneImpl);
	}

	@Test
	public void findsNothingAttemptingToFindAllLanesWithWrongMaxCapacity() {
		List<StorageRow> laneImpls = LaneOM.lanes;
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasMaxCapacityOf(LaneOM.MAX_CAPACITY + 1), laneImpls);
		assertNull(actualLaneImpl);
	}

	@Test
	public void successfullyFindEmptyLane() {
		List<StorageRow> laneImpls = LaneOM.lanes;
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isCurrentCapacityEmpty(), laneImpls);
		assertEquals(LaneOM.emptyLaneImpl, actualLaneImpl);
	}

	@Test
	public void findsNothingAttemptingToFindEmptyLane() {
		List<StorageRow> laneImpls = new ArrayList<StorageRow>();
		laneImpls.add(LaneOM.fullLaneImpl);
		laneImpls.add(LaneOM.partialPartOneLaneImpl);
		laneImpls.add(LaneOM.mixedWithSamePartCarrierEndLaneImpl);
		laneImpls.add(LaneOM.mixedWithDifferentPartCarrierEndLaneImpl);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isCurrentCapacityEmpty(), laneImpls);
		assertEquals(null, actualLaneImpl);
	}

	@Test
	public void successfullyFindPartialLane() {
		List<StorageRow> laneImpls = LaneOM.lanes;
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isCurrentCapacityPartial(), laneImpls);
		assertEquals(LaneOM.partialPartOneLaneImpl, actualLaneImpl);
	}

	@Test
	public void findsNothingAttemptingToFindPartialLane() {
		List<StorageRow> laneImpls = new ArrayList<StorageRow>();
		laneImpls.add(LaneOM.emptyLaneImpl);
		laneImpls.add(LaneOM.fullLaneImpl);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isCurrentCapacityPartial(), laneImpls);
		assertEquals(null, actualLaneImpl);
	}

	@Test
	public void successfullyFindFullLane() {
		List<StorageRow> laneImpls = LaneOM.lanes;
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isCurrentCapacityFull(), laneImpls);
		assertEquals(LaneOM.fullLaneImpl, actualLaneImpl);
	}

	@Test
	public void findsNothingAttemptingToFindFullLane() {
		List<StorageRow> laneImpls = new ArrayList<StorageRow>();
		laneImpls.add(LaneOM.emptyLaneImpl);
		laneImpls.add(LaneOM.partialPartOneLaneImpl);
		laneImpls.add(LaneOM.mixedWithSamePartCarrierEndLaneImpl);
		laneImpls.add(LaneOM.mixedWithDifferentPartCarrierEndLaneImpl);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isCurrentCapacityFull(), laneImpls);
		assertEquals(null, actualLaneImpl);
	}

	@Test
	public void successfullyFindLaneHavingCarriersWithSamePartType() {
		List<StorageRow> laneImpls = LaneOM.lanes;
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasCarriersWithSameDieNumber(), laneImpls);
		assertEquals(LaneOM.partialPartOneLaneImpl, actualLaneImpl);
	}

	@Test
	public void findsDifferentLaneHavingCarriersWithSamePartTypeOnChangingSequenceOfLanes() {
		List<StorageRow> laneImpls = new ArrayList<StorageRow>();
		laneImpls.add(LaneOM.emptyLaneImpl);
		laneImpls.add(LaneOM.fullLaneImpl);
		laneImpls.add(LaneOM.partialPartOneLaneImpl);
		laneImpls.add(LaneOM.mixedWithSamePartCarrierEndLaneImpl);
		laneImpls.add(LaneOM.mixedWithDifferentPartCarrierEndLaneImpl);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasCarriersWithSameDieNumber(), laneImpls);
		assertEquals(LaneOM.fullLaneImpl, actualLaneImpl);
	}

	@Test
	public void ignoreLaneHavingCarriersWithManyPartTypesAndFindSecondLaneHavingOnlySinglePartType() {
		StorageRow ignoredLaneImpl = LaneOM.mixedWithSamePartCarrierEndLaneImpl;
		StorageRow expectedLaneImpl = LaneOM.partialPartOneLaneImpl;
		List<StorageRow> laneImpls = LaneOM.getLanes(ignoredLaneImpl, expectedLaneImpl);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isLaneWithOnlySingleDieNumberOf(new Long(LaneOM.PART_A)),
				laneImpls);
		assertNotSame("Unexpectedly found first StorageRow that should have been ignored.", ignoredLaneImpl,
				actualLaneImpl);
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

	@Test
	public void ignoreLaneHavingCarriersWithSinglePartTypeAndFindSecondLaneHavingManyPartTypes() {
		StorageRow ignoredLaneImpl = LaneOM.partialPartOneLaneImpl;
		StorageRow expectedLaneImpl = LaneOM.mixedWithSamePartCarrierEndLaneImpl;
		List<StorageRow> laneImpls = LaneOM.getLanes(ignoredLaneImpl, expectedLaneImpl);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasMixedDieNumbersIncluding(new Long(LaneOM.PART_A)),
				laneImpls);
		assertNotSame("Unexpectedly found first StorageRow that should have been ignored.", ignoredLaneImpl,
				actualLaneImpl);
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

	@Test
	public void successfullyFindLaneHavingCarriersWithPartTypeAtTheEnd() {
		List<StorageRow> laneImpls = new ArrayList<StorageRow>();
		laneImpls.add(LaneOM.emptyLaneImpl);
		laneImpls.add(LaneOM.fullLaneImpl);
		laneImpls.add(LaneOM.partialPartOneLaneImpl);
		laneImpls.add(LaneOM.mixedWithSamePartCarrierEndLaneImpl);
		laneImpls.add(LaneOM.mixedWithDifferentPartCarrierEndLaneImpl);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasCarrierAtTheLaneInWithDieNumber(new Long(LaneOM.PART_B)),
				laneImpls);
		assertEquals(LaneOM.mixedWithDifferentPartCarrierEndLaneImpl, actualLaneImpl);
	}

	@Test
	public void successfullyFindLaneHavingCarriersWithPartTypeAtTheStart() {
		List<StorageRow> laneImpls = new ArrayList<StorageRow>();
		laneImpls.add(LaneOM.emptyLaneImpl);
		laneImpls.add(LaneOM.fullLaneImpl);
		laneImpls.add(LaneOM.partialPartOneLaneImpl);
		laneImpls.add(LaneOM.mixedWithSamePartCarrierEndLaneImpl);
		laneImpls.add(LaneOM.mixedWithDifferentPartCarrierEndLaneImpl);
		StorageRow actualLaneImpl = queryForRow(
				RowMatchers.hasCarrierAtTheLaneOutWithDieNumber(new Long(LaneOM.PART_B)), laneImpls);
		assertEquals(LaneOM.mixedWithSamePartCarrierEndLaneImpl, actualLaneImpl);
	}

	@Test
	public void findsNothingInListOfEmptyLanesAttemptingToFindLaneHavingCarriersWithPartTypeAtEnd() {
		List<StorageRow> laneImpls = LaneOM.getEmptyLanes();
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasCarrierAtTheLaneInWithDieNumber(new Long(LaneOM.PART_B)),
				laneImpls);
		assertEquals(null, actualLaneImpl);
	}

	@Test
	public void findsNothingInListOfEmptyLanesAttemptingToFindLaneHavingCarriersWithPartTypeAtStart() {
		List<StorageRow> laneImpls = LaneOM.getEmptyLanes();
		StorageRow actualLaneImpl = queryForRow(
				RowMatchers.hasCarrierAtTheLaneOutWithDieNumber(new Long(LaneOM.PART_B)), laneImpls);
		assertEquals(null, actualLaneImpl);
	}

	@Test
	public void successfullyFindEmptyCarrierLane() {
		List<StorageRow> laneImpls = LaneOM.lanes;
		StorageRow actualLaneImpl = queryForRow(RowMatchers.hasEmptyCarriers(), laneImpls);
		// assertEquals(LaneOM.emptyCarrierLaneImpl, actualLaneImpl);
	}

	private StorageRow queryForRow(Matcher<StorageRow> matcher, List<StorageRow> lanesImpls) {
		List<StorageRow> lanes = queryForRows(matcher, lanesImpls);
		return lanes == null || lanes.size() == 0 ? null : lanes.get(0);
	}

	private List<StorageRow> queryForRows(Matcher<StorageRow> matcher, List<StorageRow> lanesImpls) {

		Iterable<StorageRow> filteredLanes = matcher == null ? lanesImpls
				: Iterables.filter(lanesImpls, new PredicateWrapper<StorageRow>(matcher));
		List<StorageRow> lanes = new ArrayList<StorageRow>();
		for (StorageRow StorageRow : filteredLanes) {
			lanes.add(StorageRow);
		}
		return lanes;
	}

	// @Test
	public void successfullyFindLowCapacityLaneOverFillThresholdPercentageForGivenPartType() {
//        List<StorageRow> laneImpls = LaneOM.lanes;
//        RulesConfig config = OM.config();
//
//        List<StorageRow> resultLanes = queryForRows(
//                allOf(
//                        RowMatchers.hasCarrierAtTheLaneOutWithDieNumber(new Long(new Long(new Long(LaneOM.PART_A)))),
//                        RowMatchers.hasMaxCapacityOf(config.getLowVolumeStorage()),
//                        RowMatchers.isLaneAvailable()
//                ), laneImpls
//        );
//
//        assertEquals(3, resultLanes.size());
	}

	@Test
	public void successfullyFilterLanesToCheckCarriersHaveArrivedInLanes() {
		List<StorageRow> laneImpls = new ArrayList<StorageRow>();
		Calendar c1 = Calendar.getInstance();
		Timestamp timestamp1 = new Timestamp(c1.getTimeInMillis());
		Integer productionRunNo = 101;
		Die die = new Die(1L, PartProductionVolume.HIGH_VOLUME);
		Stop stop1 = new Stop();
		stop1.setId(1201L);

		Stop stop2 = new Stop();
		stop2.setId(513L);

		Stop stop3 = new Stop();
		stop3.setId(1202L);

		StorageRow partialLane1 = new StorageRow(0, "lane0", 12, 1);
		partialLane1.setStop(stop1);
		Carrier partOneCarrier = new Carrier(1, die, 1, timestamp1, productionRunNo);
		partOneCarrier.setCurrentLocation(stop1);
		partOneCarrier.setDestination(stop1);
		partialLane1.store(partOneCarrier);

		StorageRow partialLane2 = new StorageRow(0, "lane0", 12, 1);
		partialLane2.setStop(stop3);
		Carrier partOneCarrier2 = new Carrier(2, die, 1, timestamp1, productionRunNo);
		partOneCarrier2.setCurrentLocation(stop2);
		partOneCarrier2.setDestination(stop3);
		partialLane2.store(partOneCarrier2);

		laneImpls.add(partialLane1);
		laneImpls.add(partialLane2);
		StorageRow actualLaneImpl = queryForRow(RowMatchers.isCurrentCapacityPartial(), laneImpls);

		assertEquals(partialLane1, actualLaneImpl);
	}

}
