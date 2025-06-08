package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum DeviceTagType implements IdEnum<DeviceTagType> {
    NONE(0), 					// default
    STATIC(1), 					// constant value to write to plc
    TAG(2), 					// read from Kepware tag
    SQL(3), 					// use value returned by sql to write to plc
    DEVICE(4), 					// (HCM)
    ATTR_BY_MTOC(5), 
    DATE(6), 
    LIST(7), 
    CLASS(8),
    ATTR_BY_ENGINE_SPEC(9),
    ATTR_BY_FRAME_SPEC(10),
    ATTR_BY_ENGINE(11),
    ATTR_BY_FRAME(12),
    ATTR_BY_PROD_LOT(13),
    ATTR_BY_TRACK(14),
    OBJECT(15),
    ENTITY(16),
    
    PLC_EQ_DATA_READY(100), 	// equipment data ready
    PLC_READ(101), 				// input fields read from plc
    PLC_READ_ATTRIBUTE(102),	// attribute read from plc
    PLC_READ_MEASUREMENT(103),	// measurement read from plc
    PLC_READ_JUDGEMENT(104),	// judgement read from plc
    PLC_READ_CONSTANT(105),		// constant input values 
    
    PLC_GALC_DATA_READY(200), 	// galc data ready
    PLC_WRITE(201),				// output fields written to plc
    PLC_WRITE_ERRCODE(202),		// error code written to plc
    PLC_WRITE_ERRDESC(203),		// error description written to plc

	DATA_FORMATTER(800);        // helper type not visible to equipment
    private final int id;

    private DeviceTagType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DeviceTagType getType(int id) {
        return EnumUtil.getType(DeviceTagType.class, id);
    }
}
