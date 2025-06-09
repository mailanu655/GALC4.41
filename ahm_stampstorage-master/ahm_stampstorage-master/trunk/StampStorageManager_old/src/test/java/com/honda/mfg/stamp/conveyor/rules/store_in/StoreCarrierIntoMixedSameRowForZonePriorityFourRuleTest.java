package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.manager.OM;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * User: Greg Austin
 * Date: Sept 12, 2013
 */
public class StoreCarrierIntoMixedSameRowForZonePriorityFourRuleTest {
    @Test
        public void successfullyStoreHighVolumeCarrier() {
            List<StorageRow> lanes = new ArrayList<StorageRow>();

            StorageRow ignoredLaneImpl = OM.lane_A_AREA_Empty();
            StorageRow ignored2LaneImpl = OM.lane_C_LOW_Empty();
            StorageRow ignored3LaneImpl = OM.lane_B_AREA_Empty();
            StorageRow ignored4LaneImpl = OM.lane_S_AREA_Empty();
            StorageRow expectedLaneImpl = OM.lane_C_HIGH_Empty();

            ignoredLaneImpl.store(OM.carrier_HighVolume_PartA());   //first in StorageRow
            ignoredLaneImpl.store(OM.carrier_HighVolume_PartB());   //last in StorageRow

            ignored2LaneImpl.store(OM.carrier_HighVolume_PartB());  // first in StorageRow
            ignored2LaneImpl.store(OM.carrier_HighVolume_PartA());  // last in StorageRow

            ignored3LaneImpl.store(OM.carrier_HighVolume_PartB());  // first in StorageRow
            ignored3LaneImpl.store(OM.carrier_HighVolume_PartA());  // last in StorageRow

            ignored4LaneImpl.store(OM.carrier_HighVolume_PartB());  // first in StorageRow
            ignored4LaneImpl.store(OM.carrier_HighVolume_PartA());  // last in StorageRow

            expectedLaneImpl.store(OM.carrier_HighVolume_PartB());  // first in StorageRow
            expectedLaneImpl.store(OM.carrier_HighVolume_PartA());  // last in StorageRow

            lanes.add(ignoredLaneImpl);
            lanes.add(ignored2LaneImpl);
            lanes.add(ignored3LaneImpl);
            lanes.add(ignored4LaneImpl);
            lanes.add(expectedLaneImpl);

            StoreInRule storeInRule =
                     new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                            OM.storageState(lanes, null),
                             null, LaneCondition.MIXED_BACK, StoragePriority.Priority.FOUR);
            StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_HighVolume_PartA());
            assertEquals(expectedLaneImpl, actualLaneImpl);
        }

        @Test
        public void successfullyStoreMediumVolumeCarrier() {
            List<StorageRow> lanes = new ArrayList<StorageRow>();

            StorageRow ignoredLaneImpl = OM.lane_C_HIGH_Empty();
            StorageRow ignored2LaneImpl = OM.lane_C_LOW_Empty();
            StorageRow ignored3LaneImpl = OM.lane_B_AREA_Empty();
            StorageRow ignored4LaneImpl = OM.lane_S_AREA_Empty();
            StorageRow expectedLaneImpl = OM.lane_A_AREA_Empty();

            ignoredLaneImpl.store(OM.carrier_MediumVolume_PartA());   //first in StorageRow
            ignoredLaneImpl.store(OM.carrier_MediumVolume_PartB());   //last in StorageRow

            ignored2LaneImpl.store(OM.carrier_MediumVolume_PartB());  // first in StorageRow
            ignored2LaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in StorageRow

            ignored3LaneImpl.store(OM.carrier_MediumVolume_PartB());  // first in StorageRow
            ignored3LaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in StorageRow

            ignored4LaneImpl.store(OM.carrier_MediumVolume_PartB());  // first in StorageRow
            ignored4LaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in StorageRow

            expectedLaneImpl.store(OM.carrier_MediumVolume_PartB());  // first in StorageRow
            expectedLaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in StorageRow

            lanes.add(ignoredLaneImpl);
            lanes.add(ignored2LaneImpl);
            lanes.add(ignored3LaneImpl);
            lanes.add(ignored4LaneImpl);
            lanes.add(expectedLaneImpl);

            StoreInRule storeInRule =
                     new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                            OM.storageState(lanes, null),
                             null, LaneCondition.MIXED_BACK, StoragePriority.Priority.FOUR);
            StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_MediumVolume_PartA());
            assertEquals(expectedLaneImpl, actualLaneImpl);
        }

        @Test
        public void successfullyStoreLowVolume() {
            List<StorageRow> lanes = new ArrayList<StorageRow>();

            StorageRow ignoredLaneImpl = OM.lane_B_AREA_Empty();
            StorageRow ignored2LaneImpl = OM.lane_C_HIGH_Empty();
            StorageRow ignored3LaneImpl = OM.lane_S_AREA_Empty();
            StorageRow ignored4LaneImpl = OM.lane_C_LOW_Empty();
            StorageRow expectedLaneImpl = OM.lane_A_AREA_Empty();

            ignoredLaneImpl.store(OM.carrier_LowVolume_PartA());   //first in StorageRow
            ignoredLaneImpl.store(OM.carrier_LowVolume_PartB());   //last in StorageRow

            ignored2LaneImpl.store(OM.carrier_LowVolume_PartB());  // first in StorageRow
            ignored2LaneImpl.store(OM.carrier_LowVolume_PartA());  // last in StorageRow

            ignored3LaneImpl.store(OM.carrier_LowVolume_PartB());  // first in StorageRow
            ignored3LaneImpl.store(OM.carrier_LowVolume_PartA());  // last in StorageRow

            ignored4LaneImpl.store(OM.carrier_LowVolume_PartB());  // first in StorageRow
            ignored4LaneImpl.store(OM.carrier_LowVolume_PartA());  // last in StorageRow

            expectedLaneImpl.store(OM.carrier_LowVolume_PartB());  // first in StorageRow
            expectedLaneImpl.store(OM.carrier_LowVolume_PartA());  // last in StorageRow

            lanes.add(ignoredLaneImpl);
            lanes.add(ignored2LaneImpl);
            lanes.add(ignored3LaneImpl);
            lanes.add(ignored4LaneImpl);
            lanes.add(expectedLaneImpl);

            StoreInRule storeInRule =
                    new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                            OM.storageState(lanes, null),
                            null, LaneCondition.MIXED_BACK, StoragePriority.Priority.FOUR);
            StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_LowVolume_PartA());
            assertEquals(expectedLaneImpl, actualLaneImpl);
        }

        @Test
           public void successfullyStoreEmptyCarrier() {
               List<StorageRow> lanes = new ArrayList<StorageRow>();

               StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
               StorageRow ignored2LaneImpl = OM.lane_B_AREA_Empty();
               StorageRow ignored3LaneImpl = OM.lane_A_AREA_Empty();

               StorageRow expectedLaneImpl = OM.lane_C_HIGH_Empty();

               ignoredLaneImpl.store(OM.carrier_LowVolume_PartA());   //first in StorageRow
               ignoredLaneImpl.store(OM.carrier_LowVolume_PartB());   //last in StorageRow

               ignored3LaneImpl.store(OM.carrier_LowVolume_PartB());  // first in StorageRow
               ignored3LaneImpl.store(OM.carrier_LowVolume_PartA());  // last in StorageRow

               expectedLaneImpl.store(OM.carrier_LowVolume_PartB());  // first in StorageRow
               expectedLaneImpl.store(OM.carrier_Empty());  // last in StorageRow

               lanes.add(ignoredLaneImpl);
               lanes.add(ignored2LaneImpl);
               lanes.add(ignored3LaneImpl);

               lanes.add(expectedLaneImpl);

               StoreInRule storeInRule =
                       new StoreCarrierIntoRowOfZonePriorityAndRowCondition(
                               OM.storageState(lanes, null),
                               null, LaneCondition.MIXED_BACK, StoragePriority.Priority.FOUR);
               StorageRow actualLaneImpl = storeInRule.processRule(OM.carrier_Empty());
               assertEquals(expectedLaneImpl, actualLaneImpl);
           }
}
