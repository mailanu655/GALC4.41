package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * Various types of stops.
 * User: vcc30690
 * Date: 5/9/11
 */
public enum StopType {

	/**0-Store-in stop for any row*/ 
	STORE_IN_ALL_LANES(0),
	/**1-Not Used*/
    STORE_IN_C_LOW_LANES(1),
    /**2-Not Used*/
    STORE_IN_C_HIGH_LANES(2),
    /**3-Recirculation stop*/
    RECIRC_TO_ALL_ROWS(3),
    /**4-Reader Stop.*/ 
    NO_ACTION(4),
    /**5-Not used*/ 
    DELIVERY_INSPECTION(5) ,
    /**6-Not Used*/ 
    PRESS_INSPECTION(6),
    /**7-Area in Stamping for QC failed product */
    REWORK(7),
    /**8-Entry point into weld.*/ 
    FULL_CARRIER_DELIVERY(8),
    /**9-Carrier becomes empty at this point*/ 
    FULL_CARRIER_CONSUMPTION(9),
    /**10-1st reader after carrier releases from the row*/ 
    RELEASE_CHECK(10),
    /**11-Final Stop for Lefts*/ 
    LEFT_CONSUMED_CARRIER_EXIT(11) ,
    /**12-Final Stop for Rights*/ 
    RIGHT_CONSUMED_CARRIER_EXIT(12),
    MAINTENANCE(13),
    /**14-Stop where empty carriers are stored*/
    EMPTY_CARRIER_DELIVERY(14) ;




    private int type;

    StopType(int type) {
        this.type = type;
    }

    public int type() {
        return this.type;
    }

    public static StopType findByType(int type) {
        StopType[] items = StopType.values();
        for (int i = 0; i < items.length; i++) {
            StopType s = items[i];
            if (type == s.type()) {
                return s;
            }
        }
        return NO_ACTION;
    }

}
