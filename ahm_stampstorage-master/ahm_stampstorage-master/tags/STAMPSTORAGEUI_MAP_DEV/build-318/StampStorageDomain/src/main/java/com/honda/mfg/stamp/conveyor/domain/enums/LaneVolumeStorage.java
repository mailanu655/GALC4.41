package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * User: Jeffrey M Lutz
 * Date: 2/14/11
 */
public enum LaneVolumeStorage {
    HIGH_VOLUME_STORAGE(0),
    MEDIUM_VOLUME_STORAGE(1),
    LOW_VOLUME_STORAGE(2),
    ROW_35_HIGH_VOLUME_STORAGE(3);

    private int laneCapacityType;

    LaneVolumeStorage(int laneCapacityType) {
        this.laneCapacityType = laneCapacityType;
    }

    public int type() {
        return laneCapacityType;
    }
}
