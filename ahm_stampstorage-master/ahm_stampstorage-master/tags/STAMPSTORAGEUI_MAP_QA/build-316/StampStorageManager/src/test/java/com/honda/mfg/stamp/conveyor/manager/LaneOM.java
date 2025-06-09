package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * User: Jeffrey M Lutz
 * Date: 3/2/11
 */
public class LaneOM {
    private static int index = 0;
    private static final int LANE_COUNT_FOR_EACH_TYPE = 5;
    static final int MAX_CAPACITY = 10;
    static StorageRow emptyLaneImpl;
    static StorageRow partialPartOneLaneImpl;
    static StorageRow fullLaneImpl;
    static StorageRow emptyCarrierLaneImpl;
    static StorageRow mixedWithSamePartCarrierEndLaneImpl;
    static StorageRow mixedWithDifferentPartCarrierEndLaneImpl;
    static Carrier partOneCarrier;
    static Carrier partTwoCarrier;
    static List<StorageRow> laneImpls;
    static List<StorageRow> lanes;
    static StorageState storageState;
    static StorageRow firstLaneImpl;


    static final Long PART_A = 1L;
    static final Long PART_B = 2L;
    static final Long PART_C = 3L;

    static {
        int index = 0;
        Calendar c1 = Calendar.getInstance();
        Timestamp timestamp1 = new Timestamp(c1.getTimeInMillis());
        c1.add(Calendar.DATE, -1);
        Timestamp timestamp2 = new Timestamp(c1.getTimeInMillis());

        Integer productionRunNo = 101;

        partOneCarrier = new Carrier(1, new Die(PART_A, PartProductionVolume.HIGH_VOLUME), 1, timestamp1, productionRunNo);
        partTwoCarrier = new Carrier(2, new Die(PART_B, PartProductionVolume.HIGH_VOLUME), 1, timestamp2, productionRunNo);

        emptyLaneImpl = new StorageRow(index++, "empty StorageRow", MAX_CAPACITY,1);
        firstLaneImpl = emptyLaneImpl;

        partialPartOneLaneImpl = new StorageRow(index++, "partial StorageRow", MAX_CAPACITY,1);
        partialPartOneLaneImpl.store(partOneCarrier);

        mixedWithSamePartCarrierEndLaneImpl = new StorageRow(index++, "mixed with same part carrier at end of StorageRow", MAX_CAPACITY,1);
        mixedWithSamePartCarrierEndLaneImpl.store(partTwoCarrier);
        mixedWithSamePartCarrierEndLaneImpl.store(partOneCarrier);

        mixedWithDifferentPartCarrierEndLaneImpl = new StorageRow(index++, "mixed with different part carrier at end of StorageRow", MAX_CAPACITY,1);
        mixedWithDifferentPartCarrierEndLaneImpl.store(partOneCarrier);
        mixedWithDifferentPartCarrierEndLaneImpl.store(partTwoCarrier);

        fullLaneImpl = new StorageRow(index++, "full StorageRow", MAX_CAPACITY,1);
        Timestamp timestamp = null;
        for (int i = 0; i < MAX_CAPACITY; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            fullLaneImpl.store(new Carrier(i, new Die(PART_A, PartProductionVolume.HIGH_VOLUME), 1, timestamp, productionRunNo));
        }
        emptyCarrierLaneImpl = new StorageRow(index++, "empty carrier StorageRow", MAX_CAPACITY,1);
        for (int i = 0; i < MAX_CAPACITY; i++) {
            emptyCarrierLaneImpl.store(new Carrier(i));
        }
        lanes = new ArrayList<StorageRow>();
        laneImpls = new ArrayList<StorageRow>();
        laneImpls.add(emptyLaneImpl);
        laneImpls.add(partialPartOneLaneImpl);
        laneImpls.add(mixedWithSamePartCarrierEndLaneImpl);
        laneImpls.add(mixedWithDifferentPartCarrierEndLaneImpl);
        laneImpls.add(fullLaneImpl);
        laneImpls.add(emptyCarrierLaneImpl);
        lanes.addAll(laneImpls);
        storageState = new StorageStateImpl(laneImpls);
    }


    static List<StorageRow> getLanes(StorageRow expectedLaneImpl) {
        List<StorageRow> laneImpls = new ArrayList<StorageRow>();
        laneImpls.add(expectedLaneImpl);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < LANE_COUNT_FOR_EACH_TYPE; j++) {
                laneImpls.add(new StorageRow(i,"Lane_" + i + "-" + j, MAX_CAPACITY,1));
            }
        }
        return laneImpls;
    }

    static List<StorageRow> getEmptyLanes() {
        List<StorageRow> laneImpls = new ArrayList<StorageRow>();
        for (int i = 0; i < 10; i++) {
            laneImpls.add(new StorageRow(i,"MyFirstLane_" + i, MAX_CAPACITY,1));
        }
        return laneImpls;
    }

    static List<StorageRow> getLanes(StorageRow lane1, StorageRow lane2) {
        List<StorageRow> laneImpls = new ArrayList<StorageRow>();
        laneImpls.add(lane1);
        laneImpls.add(lane2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < LANE_COUNT_FOR_EACH_TYPE; j++) {
                laneImpls.add(new StorageRow(j,"Lane_" + i + "-" + j, MAX_CAPACITY,1));
            }
        }
        return laneImpls;
    }
}
