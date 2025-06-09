package com.honda.mfg.stamp.conveyor.domain.enums;


public enum CarrierStatus {

    SHIPPABLE(0), ON_HOLD(1), INSPECTION_REQUIRED(2);//, MAINTENANCE_REQUIRED(3);

    private int carrierStatus;


    CarrierStatus(int carrierStatus) {
        this.carrierStatus = carrierStatus;
    }

    public int type() {
        return this.carrierStatus;
    }

    public static CarrierStatus findByType(int type) {
        CarrierStatus[] items = CarrierStatus.values();
        for (int i = 0; i < items.length; i++) {
            CarrierStatus s = items[i];
            if (type == s.type()) {
                return s;
            }
        }
        return SHIPPABLE;
    }


}
