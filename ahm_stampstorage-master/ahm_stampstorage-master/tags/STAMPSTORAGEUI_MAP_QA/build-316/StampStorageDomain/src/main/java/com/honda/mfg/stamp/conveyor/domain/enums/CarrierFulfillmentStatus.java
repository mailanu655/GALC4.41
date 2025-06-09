package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/16/11
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum CarrierFulfillmentStatus {
	/**0-Order Mgr Selected Carrier*/
    SELECTED(0),
    /**1-Given dest to Queue*/
    RETRIEVED(1), 
    /**2-After Release Check stop*/
    RELEASED(2),
    /**3-Carrier at st13*/
    DELIVERED(3),
    /**4-At consuption stop*/
    CONSUMED(4), 
    /**5-In the queue*/
    QUEUED(5), 
    /**6-Marked for delivery*/
    SELECTED_TO_DELIVER(6),
    /**7-Has destination st13*/
    READY_TO_DELIVER(7);


    private int status;

    CarrierFulfillmentStatus(int status) {
        this.status = status;
    }

     public int status() {
        return this.status;
    }

    public static CarrierFulfillmentStatus findByType(int status) {
        CarrierFulfillmentStatus[] items = CarrierFulfillmentStatus.values();
        for (int i = 0; i < items.length; i++) {
            CarrierFulfillmentStatus s = items[i];
            if (status == s.status()) {
                return s;
            }
        }
        return SELECTED;
    }
}
