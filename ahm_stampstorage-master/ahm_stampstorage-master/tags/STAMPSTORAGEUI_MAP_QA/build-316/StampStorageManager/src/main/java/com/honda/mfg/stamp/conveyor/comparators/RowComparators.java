package com.honda.mfg.stamp.conveyor.comparators;

import com.honda.mfg.stamp.conveyor.domain.StorageRow;

import java.util.Comparator;

/**
 * User: vcc30690
 * Date: 3/14/11
 */
public final class RowComparators {

    private RowComparators(){

    }

    public static Comparator<StorageRow> getComparatorByCarriersCountOfDieNumberAtLaneOut(final Long dieNumber) {

        Comparator<StorageRow> byCarrierCountOfDieNumber = new Comparator<StorageRow>() {
            public int compare(StorageRow lane1, StorageRow lane2) {
                Integer laneCount1 = lane1.getCountOfCarriersAtLaneOutEndForDieNumber(dieNumber);
                Integer laneCount2 = lane2.getCountOfCarriersAtLaneOutEndForDieNumber(dieNumber);
                return laneCount1.compareTo(laneCount2);
            }
        };
        return byCarrierCountOfDieNumber;
    }

     public static Comparator<StorageRow> getComparatorByCarriersCountOfDieNumberAtLaneIn(final Long dieNumber) {

        Comparator<StorageRow> byCarrierCountOfDieNumber = new Comparator<StorageRow>() {
            public int compare(StorageRow lane1, StorageRow lane2) {
                Integer laneCount1 = lane1.getCountOfCarriersAtLaneInEndForDieNumber(dieNumber);
                Integer laneCount2 = lane2.getCountOfCarriersAtLaneInEndForDieNumber(dieNumber);
                return laneCount1.compareTo(laneCount2);
            }
        };
        return byCarrierCountOfDieNumber;
    }

    public static Comparator<StorageRow> getComparatorByDieNumberBlockingCarriersCountAtLaneOut(final Long dieNumber) {

        Comparator<StorageRow> byBlockingCarrierCount = new Comparator<StorageRow>() {
            public int compare(StorageRow lane1, StorageRow lane2) {
                Integer laneCount1 = lane1.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(dieNumber);
                Integer laneCount2 = lane2.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(dieNumber);

                return laneCount1.compareTo(laneCount2);
            }
        };
        return byBlockingCarrierCount;
    }

    public static Comparator<StorageRow> getComparatorByCurrentCarrierCount() {

        Comparator<StorageRow> byCarrierCount = new Comparator<StorageRow>() {
            public int compare(StorageRow lane1, StorageRow lane2) {
                Integer lane1count = lane1.getCurrentCarrierCount();
                Integer lane2count = lane2.getCurrentCarrierCount();
                return lane1count.compareTo(lane2count);
            }
        };

        return byCarrierCount;

    }

     public static Comparator<StorageRow> getComparatorByRowStopId() {

        Comparator<StorageRow> byStopId = new Comparator<StorageRow>() {
            public int compare(StorageRow lane1, StorageRow lane2) {
                Long laneStop1 = lane1.getStop().getId();
                Long laneStop2 = lane2.getStop().getId();
                return laneStop1.compareTo(laneStop2);
            }
        };

        return byStopId;

    }

}
