package com.honda.mfg.stamp.conveyor.domain.enums;


public enum OrderStatus {

    Queued(0),
    /**Currently Active Order*/
    InProcess(1),
    /**1-Not used*/
    Delivered(2),
    /**3-User forced completion of order */
    ManuallyCompleted(3),
    /**4-Cancel initialized orders before they are processed */
    Cancelled(4),
    /**5-Either fulfilled or delivered Automatically*/
    AutoCompleted(5),
    /**6-Pause an order*/
    OnHold(6),
    /**7-After Carriers are selected and retrieved (assigned queue dest) to fulfill the order*/
    RetrievingCarriers(7),
    /**8-After carriers are selected to deliver and ready to deliver (assigned st13 dest)*/
    DeliveringCarriers(8),
    /**9-Not used*/
    SelectingCarriers(9),
    /**10-Newly Created Weld Order*/
    Initialized(10);


    private int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int status() {
        return this.status;
    }

    public static OrderStatus findByType(int type) {
        OrderStatus[] items = OrderStatus.values();
        for (int i = 0; i < items.length; i++) {
            OrderStatus s = items[i];
            if (type == s.status()) {
                return s;
            }
        }
        return Initialized;
    }

//    public static boolean isActiveStatus(OrderStatus checkStatus)  {
//    	boolean bActiveStatus =
//    			checkStatus == InProcess ||
//    			checkStatus == RetrievingCarriers ||
//    			checkStatus == DeliveringCarriers ||
//    			checkStatus == SelectingCarriers ||
//    			checkStatus == OnHold;
//    	return bActiveStatus;
//    }

}
