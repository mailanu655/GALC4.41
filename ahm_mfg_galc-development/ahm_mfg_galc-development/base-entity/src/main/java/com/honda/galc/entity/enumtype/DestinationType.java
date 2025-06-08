package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum DestinationType implements IdEnum<DestinationType> {
    Equipment(0), 
    Printer(1), 
    Terminal(2), 
    Application(3),
    External(4),
    Notification(5),
    MQ(6),
    DEVICE_WISE(7),
    JASPER_MQ(8),
    SERVER_TASK(9),
    FTP(10),
    MQMANIFEST(11),
    HTTP_PARAM(12);

    private final int id;

    private DestinationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DestinationType getType(int id) {
        return EnumUtil.getType(DestinationType.class, id);
    }
}
