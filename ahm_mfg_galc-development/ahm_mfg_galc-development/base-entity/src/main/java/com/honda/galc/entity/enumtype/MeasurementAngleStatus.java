package com.honda.galc.entity.enumtype;
import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum; 

public enum MeasurementAngleStatus implements IdEnum<MeasurementAngleStatus>{
	NG_LOW_ANGLE(0), OK(1), NG_HIGH_ANGLE(2);

    private int id;

    private MeasurementAngleStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MeasurementAngleStatus getType(int id) {
        return EnumUtil.getType(MeasurementAngleStatus.class, id);
    }
}
