package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 *Defines the owning area for a stop.
 * User: VCC30690
 * Date: 9/22/11
 */
public enum StopArea {

    UNDEFINED(0),
    STORE_IN_ROUTE(1),
    ROW(2),
    STORE_OUT_ROUTE(3),
    WELD_LINE_1(4),
    WELD_LINE_2(5),
    KD_LINE(6),
    OLD_WELD_LINE(7),
    EMPTY_AREA(8),
    B_PRESS(9),
    C_PRESS(10),
    B_AREA(11),
    Q_WELD_LINE_1(12),
    Q_WELD_LINE_2(13);

    private int type;

       StopArea(int type) {
           this.type = type;
       }

       public int type() {
           return this.type;
       }

       public static StopArea findByType(int type) {
           StopArea[] items = StopArea.values();
           for (int i = 0; i < items.length; i++) {
               StopArea s = items[i];
               if (type == s.type()) {
                   return s;
               }
           }
           return UNDEFINED;
       }

}
