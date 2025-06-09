package com.honda.mfg.stamp.conveyor.domain.enums;


public enum REWORK_METHOD {
    DA(0),
    FILE_AND_DA(1),
    STRAIGHTEN_WITH_PLIERS(2),
    CANNOT_REPAIR_SCRAP(3),
    BRAIZE(4),
    MIG_WELD(5),
    SCOTCH_BRIGHT(6),
    UNDETERMINED(7),
    DEBURR(8);


     private int method;


    REWORK_METHOD(int method) {
        this.method = method;
    }

    public int method() {
        return this.method;
    }

    public static REWORK_METHOD findByMethod(int method) {
        REWORK_METHOD[] items = REWORK_METHOD.values();
        for (int i = 0; i < items.length; i++) {
            REWORK_METHOD s = items[i];
            if (method == s.method()) {
                return s;
            }
        }
        return DA;
    }
}
