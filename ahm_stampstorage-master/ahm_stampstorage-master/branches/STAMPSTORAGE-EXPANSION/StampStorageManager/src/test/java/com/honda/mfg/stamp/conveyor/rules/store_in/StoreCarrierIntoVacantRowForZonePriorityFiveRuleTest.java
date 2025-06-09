package com.honda.mfg.stamp.conveyor.rules.store_in;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.OM;

/**
 * User: Greg Austin Date: Sept 13, 2013
 */
public class StoreCarrierIntoVacantRowForZonePriorityFiveRuleTest {

	@Test
	public void successfullyStoreHighVolumeCarrier() {
		List<StorageRow> lanes = new ArrayList<StorageRow>();
		Carrier emptyCarrier1 = new Carrier(101);
		Carrier emptyCarrier2 = new Carrier(102);

		StorageRow ignoredLaneImpl = OM.lane_C_HIGH_Empty();
		StorageRow ignored2LaneImpl = OM.lane_C_LOW_Empty();
		StorageRow expectedLaneImpl = OM.lane_A_AREA_Empty();
		StorageRow ignored3LaneImpl = OM.lane_B_AREA_Empty();
		StorageRow expectedLaneImpl2 = OM.lane_A_AREA_Empty();
		StorageRow ignored4LaneImpl = OM.lane_S_AREA_Empty();
		StorageRow expectedLaneImpl3 = OM.lane_A_AREA_Empty();
		ignoredLaneImpl.store(emptyCarrier1); // first in StorageRow
		ignoredLaneImpl.store(emptyCarrier2); // last in StorageRow

		ignored2LaneImpl.store(OM.carrier_HighVolume_PartB()); // first in StorageRow
		ignored2LaneImpl.store(OM.carrier_HighVolume_PartA()); // last in StorageRow
		ignored3LaneImpl.store(OM.carrier_Empty());
		ignored4LaneImpl.store(OM.carrier_Empty());

		lanes.add(ignoredLaneImpl);
		lanes.add(ignored2LaneImpl);
		lanes.add(ignored3LaneImpl);
		lanes.add(expectedLaneImpl3);
		lanes.add(ignored4LaneImpl);
		lanes.add(expectedLaneImpl);
		lanes.add(expectedLaneImpl2);

		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(OM.storageState(lanes, null),
				null, LaneCondition.VACANT, StoragePriority.Priority.FIVE);
		StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_HighVolume_PartA());
		assertEquals(expectedLaneImpl3, actualLaneImpl);
	}

	@Test
	public void successfullyStoreMediumVolumeCarrier() {
		List<StorageRow> lanes = new ArrayList<StorageRow>();
		Carrier emptyCarrier1 = new Carrier(101);
		Carrier emptyCarrier2 = new Carrier(102);

		StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
		StorageRow ignored2LaneImpl = OM.lane_A_AREA_Empty();
		StorageRow expectedLaneImpl = OM.lane_S_AREA_Empty();
		StorageRow ignored3LaneImpl = OM.lane_B_AREA_Empty();
		StorageRow ignored4LaneImpl = OM.lane_C_HIGH_Empty();

		ignoredLaneImpl.store(emptyCarrier1); // first in StorageRow
		ignoredLaneImpl.store(emptyCarrier2); // last in StorageRow

		ignored2LaneImpl.store(OM.carrier_HighVolume_PartB()); // first in StorageRow
		ignored2LaneImpl.store(OM.carrier_HighVolume_PartA()); // last in StorageRow
		ignored3LaneImpl.store(OM.carrier_Empty());
		ignored4LaneImpl.store(OM.carrier_Empty());

		lanes.add(ignoredLaneImpl);
		lanes.add(ignored2LaneImpl);
		lanes.add(ignored3LaneImpl);
		lanes.add(ignored4LaneImpl);
		lanes.add(expectedLaneImpl);

		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(OM.storageState(lanes, null),
				null, LaneCondition.VACANT, StoragePriority.Priority.FIVE);
		StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_MediumVolume_PartA());
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

	@Test
	public void successfullyStoreLowVolumeCarrier() {

		List<StorageRow> lanes = new ArrayList<StorageRow>();
		Carrier emptyCarrier1 = new Carrier(101);
		Carrier emptyCarrier2 = new Carrier(102);

		StorageRow ignoredLaneImpl = OM.lane_C_HIGH_Empty();
		StorageRow ignored2LaneImpl = OM.lane_A_AREA_Empty();
		StorageRow expectedLaneImpl = OM.lane_S_AREA_Empty();
		StorageRow ignored3LaneImpl = OM.lane_C_LOW_Empty();
		StorageRow ignored4LaneImpl = OM.lane_B_AREA_Empty();

		ignoredLaneImpl.store(emptyCarrier1); // first in StorageRow
		ignoredLaneImpl.store(emptyCarrier2); // last in StorageRow

		ignored2LaneImpl.store(OM.carrier_HighVolume_PartB()); // first in StorageRow
		ignored2LaneImpl.store(OM.carrier_HighVolume_PartA()); // last in StorageRow
		ignored3LaneImpl.store(OM.carrier_Empty());
		ignored4LaneImpl.store(OM.carrier_Empty());

		lanes.add(ignoredLaneImpl);
		lanes.add(ignored2LaneImpl);
		lanes.add(ignored3LaneImpl);
		lanes.add(ignored4LaneImpl);
		lanes.add(expectedLaneImpl);
		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(OM.storageState(lanes, null),
				null, LaneCondition.VACANT, StoragePriority.Priority.FIVE);
		StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_LowVolume_PartA());
		assertEquals(expectedLaneImpl, actualLaneImpl);
	}

	@Test(expected = NoApplicableRuleFoundException.class)
	public void successfullyStoreEmptyCarrier() {
		List<StorageRow> lanes = new ArrayList<StorageRow>();
		Carrier emptyCarrier1 = new Carrier(101);
		Carrier emptyCarrier2 = new Carrier(102);

		StorageRow ignoredLaneImpl = OM.lane_C_HIGH_Empty();
		StorageRow ignored2LaneImpl = OM.lane_A_AREA_Empty();
		StorageRow ignored3LaneImpl = OM.lane_C_LOW_Empty();

		ignoredLaneImpl.store(emptyCarrier1); // first in StorageRow
		ignoredLaneImpl.store(emptyCarrier2); // last in StorageRow

		ignored2LaneImpl.store(OM.carrier_LowVolume_PartB()); // first in StorageRow
		ignored2LaneImpl.store(OM.carrier_LowVolume_PartA()); // last in StorageRow

		lanes.add(ignoredLaneImpl);
		lanes.add(ignored2LaneImpl);
		lanes.add(ignored3LaneImpl);

		StoreInRule storeInRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(OM.storageState(lanes, null),
				null, LaneCondition.VACANT, StoragePriority.Priority.FIVE);
		StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_Empty());
		assertNull(actualLaneImpl);
	}

}
