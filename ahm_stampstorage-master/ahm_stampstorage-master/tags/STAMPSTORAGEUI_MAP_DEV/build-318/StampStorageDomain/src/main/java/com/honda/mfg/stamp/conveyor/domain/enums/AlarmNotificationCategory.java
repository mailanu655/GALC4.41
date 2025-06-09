package com.honda.mfg.stamp.conveyor.domain.enums;


public enum AlarmNotificationCategory {

    MECHANICAL(0), TRACKING(1), INVENTORY(2), ATTENTION(3), INFORMATION(4) ;


     private int type;

    AlarmNotificationCategory(int type) {
        this.type = type;
    }

    public int type() {
        return this.type;
    }

    public static AlarmNotificationCategory findByType(int type) {
        AlarmNotificationCategory[] items = AlarmNotificationCategory.values();
        for (int i = 0; i < items.length; i++) {
            AlarmNotificationCategory s = items[i];
            if (type == s.type()) {
                return s;
            }
        }
        return INFORMATION;
    }
}
