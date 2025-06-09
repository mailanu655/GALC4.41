package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * Stop numbers for Presses and rework lines
 *
 */
public enum Press {
	/**405*/
	REWORK_C_LINE(405),
	/**2018*/
	REWORK_B_LINE_1(2018),
	/**2019*/
	REWORK_B_LINE_2(2019),
	/**502*/
	PRESS_C_ROBOT_1(502),
	/**506*/
	PRESS_C_ROBOT_2(506),
	/**2011*/
	PRESS_B_ROBOT_1(2011),
	/**2007*/
	PRESS_B_ROBOT_2(2007),
    /**3700*/ 
	WELD_LINE1_ROBOT_1(3700),
	/**1800*/
	WELD_LINE1_ROBOT_2(1800),
	/**10900*/
	WELD_LINE2_ROBOT_1(10900),
	/**11900*/
	WELD_LINE2_ROBOT_2(11900),
	/**800*/
	KD_AREA(800);

    private int press;

    public int type() {
        return this.press;
    }

    Press(int type) {
        this.press = type;
    }

    public static Press findByType(int type) {
        Press[] items = Press.values();
        for (int i = 0; i < items.length; i++) {
            Press s = items[i];
            if (type == s.type()) {
                return s;
            }
        }
        return REWORK_C_LINE;
    }
}
