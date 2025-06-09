package com.honda.mfg.stamp.conveyor.rules.store_in;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.manager.OM;

/**
 * User:Michael Grecol Date: 8/5/14
 */

public class StoreCarrierIntoPartialLaneWithDifferentPartsForZonePriorityFourRuleTest {

	@Test
	public void successfullyStoreCarrierWithHighVolumeParts() {

		StorageRow expectedLaneImpl = OM.lane_C_HIGH_Empty();
		expectedLaneImpl.store(OM.carrier_HighVolume_PartB());

		List<StorageRow> lanes = new ArrayList<StorageRow>();
		StorageRow ignoredLane;
		ignoredLane = OM.lane_C_LOW_Empty();
		ignoredLane.store(OM.carrier_HighVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_A_AREA_Empty();
		ignoredLane.store(OM.carrier_HighVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_S_AREA_Empty();
		ignoredLane.store(OM.carrier_HighVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_B_AREA_Empty();
		ignoredLane.store(OM.carrier_HighVolume_PartA());
		lanes.add(ignoredLane);

		lanes.add(expectedLaneImpl);

		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
				OM.storageState(lanes, new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME)), null,
				LaneCondition.PARTIAL_DIFFERENT_DIE, StoragePriority.Priority.FOUR);
		Carrier carrier = OM.carrier_HighVolume_PartA();
		StorageRow actualLaneImpl = storeInRule.processRule(carrier);
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

	@Test
	public void successfullyStoreCarrierWithMediumVolumeParts() {

		StorageRow expectedLaneImpl = OM.lane_A_AREA_Empty();
		expectedLaneImpl.store(OM.carrier_MediumVolume_PartB());

		List<StorageRow> lanes = new ArrayList<StorageRow>();
		StorageRow ignoredLane;
		ignoredLane = OM.lane_B_AREA_Empty();
		ignoredLane.store(OM.carrier_LowVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_C_HIGH_Empty();
		ignoredLane.store(OM.carrier_LowVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_S_AREA_Empty();
		ignoredLane.store(OM.carrier_MediumVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_C_LOW_Empty();
		ignoredLane.store(OM.carrier_MediumVolume_PartA());
		lanes.add(ignoredLane);
		lanes.add(expectedLaneImpl);

		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
				OM.storageState(lanes, new Die(OM.PART_A, PartProductionVolume.MEDIUM_VOLUME)), null,
				LaneCondition.PARTIAL_DIFFERENT_DIE, StoragePriority.Priority.FOUR);
		Carrier carrier = OM.carrier_MediumVolume_PartA();
		StorageRow actualLaneImpl = storeInRule.processRule(carrier);
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

	@Test
	public void successfullyStoreCarrierWithLowVolumeParts() {

		StorageRow expectedLaneImpl = OM.lane_A_AREA_Empty();
		expectedLaneImpl.store(OM.carrier_LowVolume_PartB());

		List<StorageRow> lanes = new ArrayList<StorageRow>();
		StorageRow ignoredLane;
		ignoredLane = OM.lane_B_AREA_Empty();
		ignoredLane.store(OM.carrier_LowVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_C_HIGH_Empty();
		ignoredLane.store(OM.carrier_LowVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_S_AREA_Empty();
		ignoredLane.store(OM.carrier_LowVolume_PartA());
		lanes.add(ignoredLane);
		ignoredLane = OM.lane_C_LOW_Empty();
		ignoredLane.store(OM.carrier_LowVolume_PartA());
		lanes.add(ignoredLane);
		lanes.add(expectedLaneImpl);

		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
				OM.storageState(lanes, new Die(OM.PART_A, PartProductionVolume.LOW_VOLUME)), null,
				LaneCondition.PARTIAL_DIFFERENT_DIE, StoragePriority.Priority.FOUR);
		Carrier carrier = OM.carrier_LowVolume_PartA();
		StorageRow actualLaneImpl = storeInRule.processRule(carrier);
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

	@Test
	public void successfullyStoreEmptyCarrier() {

		List<StorageRow> lanes = new ArrayList<StorageRow>();

		StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
		StorageRow ignored2LaneImpl = OM.lane_B_AREA_Empty();
		StorageRow ignored3LaneImpl = OM.lane_A_AREA_Empty();
		StorageRow expectedLaneImpl = OM.lane_C_HIGH_Empty();

		expectedLaneImpl.store(OM.carrier_HighVolume_PartA()); // first in StorageRow
		expectedLaneImpl.store(OM.carrier_HighVolume_PartA()); // last in StorageRow

		lanes.add(ignoredLaneImpl);
		lanes.add(ignored2LaneImpl);
		lanes.add(ignored3LaneImpl);
		lanes.add(expectedLaneImpl);

		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(OM.storageState(lanes, null),
				null, LaneCondition.PARTIAL_DIFFERENT_DIE, StoragePriority.Priority.FOUR);
		StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_Empty());
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

}
