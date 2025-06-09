package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.manager.OM;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: 3/6/11
 */
public class StoreCarrierIntoVacantRowForZonePriorityOneRuleTest {

    @Test
    public void successfullyStoreCarrierWithHighVolumeParts() {

        List<StorageRow> lanes = new ArrayList<StorageRow>();
        Carrier emptyCarrier1 = new Carrier(101);
        Carrier emptyCarrier2 = new Carrier(102);

        StorageRow ignoredLaneImpl = OM.lane_A_AREA_Empty();
        StorageRow ignored2LaneImpl = OM.lane_C_HIGH_Empty();
        StorageRow ignored3LaneImpl = OM.lane_C_LOW_Empty();
        StorageRow ignored4LaneImpl = OM.lane_B_AREA_Empty();
        StorageRow expectedLaneImpl = OM.lane_S_AREA_Empty();

        ignoredLaneImpl.store(emptyCarrier1);   //first in lane
        ignoredLaneImpl.store(emptyCarrier2);   //last in lane

        ignored2LaneImpl.store(OM.carrier_HighVolume_PartB());  // first in lane
        ignored2LaneImpl.store(OM.carrier_HighVolume_PartA());  // last in lane

        ignored3LaneImpl.store(OM.carrier_Empty());
        ignored4LaneImpl.store(OM.carrier_Empty());

        lanes.add(ignoredLaneImpl);
        lanes.add(ignored2LaneImpl);
        lanes.add(ignored3LaneImpl);
        lanes.add(ignored4LaneImpl);
        lanes.add(expectedLaneImpl);

        StoreInRule storeInRule =
                new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                        OM.storageState(lanes, null),
                        null, LaneCondition.VACANT, StoragePriority.Priority.ONE);
        StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_HighVolume_PartA());
        assertEquals(expectedLaneImpl, actualLaneImpl);
    }

    @Test
    public void successfullyStoreCarrierWithMediumVolumeParts() {
         List<StorageRow> lanes = new ArrayList<StorageRow>();
        Carrier emptyCarrier1 = new Carrier(101);
        Carrier emptyCarrier2 = new Carrier(102);

        StorageRow ignoredLaneImpl = OM.lane_A_AREA_Empty();
        StorageRow ignored2LaneImpl = OM.lane_C_HIGH_Empty();
        StorageRow ignored3LaneImpl = OM.lane_S_AREA_Empty();
        StorageRow ignored4LaneImpl = OM.lane_B_AREA_Empty();
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();

        ignoredLaneImpl.store(emptyCarrier1);   //first in lane
        ignoredLaneImpl.store(emptyCarrier2);   //last in lane

        ignored2LaneImpl.store(OM.carrier_MediumVolume_PartB());  // first in lane
        ignored2LaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in lane

        ignored3LaneImpl.store(OM.carrier_Empty());
        ignored4LaneImpl.store(OM.carrier_Empty());


        lanes.add(ignoredLaneImpl);
        lanes.add(ignored2LaneImpl);
        lanes.add(ignored3LaneImpl);
        lanes.add(ignored4LaneImpl);
        lanes.add(expectedLaneImpl);

        StoreInRule storeInRule =
                new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                        OM.storageState(lanes, null),
                        null, LaneCondition.VACANT, StoragePriority.Priority.ONE);
        StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_MediumVolume_PartA());
        assertEquals(expectedLaneImpl, actualLaneImpl);

    }

    @Test
    public void successfullyStoreCarrierWithLowVolumeParts() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        Carrier emptyCarrier1 = new Carrier(101);
        Carrier emptyCarrier2 = new Carrier(102);

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
        StorageRow ignored2LaneImpl = OM.lane_A_AREA_Empty();
         StorageRow ignored3LaneImpl = OM.lane_S_AREA_Empty();
        StorageRow ignored4LaneImpl = OM.lane_B_AREA_Empty();
        StorageRow expectedLaneImpl = OM.lane_C_HIGH_Empty();

        ignoredLaneImpl.store(emptyCarrier1);   //first in lane
        ignoredLaneImpl.store(emptyCarrier2);   //last in lane

        ignored2LaneImpl.store(OM.carrier_LowVolume_PartB());  // first in lane
        ignored2LaneImpl.store(OM.carrier_LowVolume_PartA());  // last in lane

        ignored3LaneImpl.store(OM.carrier_Empty());
        ignored4LaneImpl.store(OM.carrier_Empty());


        lanes.add(ignoredLaneImpl);
        lanes.add(ignored2LaneImpl);
        lanes.add(ignored3LaneImpl);
        lanes.add(ignored4LaneImpl);
        lanes.add(expectedLaneImpl);

        StoreInRule storeInRule =
                 new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                        OM.storageState(lanes, null),
                         null, LaneCondition.VACANT, StoragePriority.Priority.ONE);
        StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_LowVolume_PartA());
        assertEquals(expectedLaneImpl, actualLaneImpl);

    }

    @Test
    public void successfullyStoreEmptyCarrier() {
       List<StorageRow> lanes = new ArrayList<StorageRow>();

        StorageRow ignoredLaneImpl = OM.lane_B_AREA_Empty();
        StorageRow ignored2LaneImpl = OM.lane_C_HIGH_Empty();
        StorageRow ignored3LaneImpl = OM.lane_C_LOW_Empty();
        StorageRow expectedLaneImpl = OM.lane_A_AREA_Empty();


        lanes.add(ignoredLaneImpl);
        lanes.add(ignored2LaneImpl);
        lanes.add(ignored3LaneImpl);
        lanes.add(expectedLaneImpl);

        StoreInRule storeInRule =
               new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                        OM.storageState(lanes, null),
                       null, LaneCondition.VACANT, StoragePriority.Priority.ONE);
        StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_Empty());
        assertEquals(expectedLaneImpl, actualLaneImpl);

    }
}
