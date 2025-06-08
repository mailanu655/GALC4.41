package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 23, 2014
 * added additional ProcessPoint Types used by Legacy GALC.
 */
public enum ProcessPointType implements IdEnum<ProcessPointType> {
    None				(0, "NONE"),
    On					(1, "ON"),
    Off					(2, "OFF"),
    @Deprecated
    QICSInspection		(3, "QICS INSPECTION STATION"),
    @Deprecated
    QICSRepair			(4, "QICS REPAIR STATION"),
    PartsInstallation	(5, "PARTS INSTALLATION"),
    OtherWork			(6, "OTHER WORK"),
    XCoupler			(7, "X COUPLER"),
    @Deprecated
    OffQics				(8, "OFF QICS"),
    @Deprecated
    OnQics				(9, "ON QICS"),
    ExceptionalOut		(10, "EXCEPTIONAL OUT"),
    Scrap				(11, "SCRAP"),
    Shipping			(12, "SHIPPING"),
    ProductOff			(13, "PRODUCT OFF"),
    ProductExit			(14, "PRODUCT EXIT"),
    Repair				(15, "REPAIR");
    
    private final int id;
    private final String name;

    private ProcessPointType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public static ProcessPointType getType(int id) {
        return EnumUtil.getType(ProcessPointType.class, id);
    }
}
