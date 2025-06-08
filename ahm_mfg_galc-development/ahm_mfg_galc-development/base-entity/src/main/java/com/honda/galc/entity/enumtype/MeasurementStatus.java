package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public enum MeasurementStatus implements IdEnum<MeasurementStatus> {
	REMOVED(-9), ERR(-1)/*BLANK*/, NG(0), OK(1), REVERSE(2);

    private int id;

    private MeasurementStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MeasurementStatus getType(int id) {
        return EnumUtil.getType(MeasurementStatus.class, id);
    }
}
