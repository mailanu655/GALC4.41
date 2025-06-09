package com.honda.mfg.stamp.conveyor.comparators;

import com.google.common.collect.Ordering;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.manager.*;
import org.junit.Test;
import org.mockito.internal.matchers.Not;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * User: vcc30690
 * Date: 3/14/11
 */
public class RowComparatorsTest {


    @Test
    public void successfullySortListOfMixedBlockedLanesForPartType() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();

        StorageRow lane1 = OM.lane_A_AREA_Empty();
        StorageRow lane2 = OM.lane_C_LOW_Empty();
        StorageRow lane3 = OM.lane_C_LOW_Empty();

        lane1.store(OM.carrier_HighVolume_PartB());
        lane1.store(OM.carrier_HighVolume_PartB());
        lane1.store(OM.carrier_HighVolume_PartB());
        lane1.store(OM.carrier_HighVolume_PartA());

        lane2.store(OM.carrier_HighVolume_PartB());
        lane2.store(OM.carrier_HighVolume_PartB());
        lane2.store(OM.carrier_HighVolume_PartA());

        lane3.store(OM.carrier_HighVolume_PartB());
        lane3.store(OM.carrier_HighVolume_PartA());

        lanes.add(lane1);
        lanes.add(lane2);
        lanes.add(lane3);
        StorageState storageState = new StorageStateImpl(lanes);

        Die part = new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME);

        List<StorageRow> mixedBlockedLanes = storageState.queryForRows(
                allOf(RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.hasBlockedCarrierWithDieNumberAtTheLaneOut(part.getId())
                )
        );

        List<StorageRow> sortedLanes = Ordering.from(RowComparators.getComparatorByDieNumberBlockingCarriersCountAtLaneOut(part.getId()))
                .sortedCopy(mixedBlockedLanes);

        assertEquals(2, sortedLanes.get(0).getCurrentCarrierCount());

    }


    @Test
    public void successfullySortListOfMixedLanesForPartType() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();

        StorageRow lane1 = OM.lane_A_AREA_Empty();
        StorageRow lane2 = OM.lane_C_LOW_Empty();
        StorageRow lane3 = OM.lane_C_LOW_Empty();

        lane1.store(OM.carrier_HighVolume_PartA());
        lane1.store(OM.carrier_HighVolume_PartA());
        lane1.store(OM.carrier_HighVolume_PartA());
        lane1.store(OM.carrier_HighVolume_PartA());
        lane1.store(OM.carrier_HighVolume_PartB());
        lane1.store(OM.carrier_HighVolume_PartB());
        lane1.store(OM.carrier_HighVolume_PartB());

        lane2.store(OM.carrier_HighVolume_PartA());
        lane2.store(OM.carrier_HighVolume_PartA());
        lane2.store(OM.carrier_HighVolume_PartA());
        lane2.store(OM.carrier_HighVolume_PartB());
        lane2.store(OM.carrier_HighVolume_PartB());

        lane3.store(OM.carrier_HighVolume_PartA());
        lane3.store(OM.carrier_HighVolume_PartA());
        lane3.store(OM.carrier_HighVolume_PartB());

        lanes.add(lane1);
        lanes.add(lane2);
        lanes.add(lane3);
        StorageState storageState = new StorageStateImpl(lanes);

        Die part = new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME);
        Die part2 = new Die(OM.PART_B, PartProductionVolume.HIGH_VOLUME);

        List<StorageRow> partialLanes = storageState.queryForRows(
                allOf(RowMatchers.isCurrentCapacityMixed(),
                        new Not(RowMatchers.hasEmptyCarriers())
                )
        );

        List<StorageRow> sortedLanes = Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(part.getId()))
                .sortedCopy(partialLanes);

        assertEquals(3, sortedLanes.get(0).getCurrentCarrierCount());

         List<StorageRow> sortedLanes2 = Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneIn(part2.getId()))
                .sortedCopy(partialLanes);

        assertEquals(7, sortedLanes2.get(2).getCurrentCarrierCount());
    }

    @Test
    public void successfullySortListOfLanesByCurrentCarrierCount() {

        List<StorageRow> lanes = new ArrayList<StorageRow>();

        StorageRow ignoredLaneImpl = OM.lane_A_AREA_Empty();
        StorageRow ignored2LaneImpl = OM.lane_C_LOW_Empty();
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();

        ignoredLaneImpl.store(OM.carrier_HighVolume_PartA());   //first in lane
        ignoredLaneImpl.store(OM.carrier_HighVolume_PartB());   //middle in lane
        ignoredLaneImpl.store(OM.carrier_HighVolume_PartB());   //last in lane

        ignored2LaneImpl.store(OM.carrier_HighVolume_PartB());  // first in lane
        ignored2LaneImpl.store(OM.carrier_HighVolume_PartB());  // middle in lane
        ignored2LaneImpl.store(OM.carrier_HighVolume_PartA());  // last in lane

        expectedLaneImpl.store(OM.carrier_HighVolume_PartB());  // first in lane
        expectedLaneImpl.store(OM.carrier_HighVolume_PartA());  // last in lane

        lanes.add(ignoredLaneImpl);
        lanes.add(ignored2LaneImpl);
        lanes.add(expectedLaneImpl);

        List<StorageRow> sortedLanes = Ordering.from(RowComparators.getComparatorByCurrentCarrierCount())
                .sortedCopy(lanes);

        assertEquals(expectedLaneImpl, sortedLanes.get(0));

    }
}
