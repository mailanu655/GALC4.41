package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum DeviceType implements IdEnum<DeviceType> {
    EQUIPMENT(0,"Equipment"), 
    PRINTER(1,"Printer"), 
    VENDER_APP(2,"Vendor Application"), 
    OPC(3,"OPC"), 
    MQ_PRINTER(4,"MQ Printer"),
    MQ(5,"MQ"),
    DEVICE_WISE(6,"DeviceWise"),
    JASPER(8,"Jasper MQ Printer"),
    FTP(10,"FTP"),
	HTTP_PARAM(12,"HTTP Param");


    private final int id;
    private final String name;

    private DeviceType(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public static DeviceType getType(int id) {
        return EnumUtil.getType(DeviceType.class, id);
    }
}
