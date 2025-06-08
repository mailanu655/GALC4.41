package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum OPCServerClientMode implements IdEnum<OPCServerClientMode> {
    StatelessTransmit(0, "Stateless Transmit"), StatelessAsyncTransmit(1, "Statless Async Transmit"),
    Transmit(2, "Transmit"), AsyncTransmit(3, "Transmit Async");

    private final int id;
    private final String name;

    private OPCServerClientMode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public static OPCServerClientMode getType(int id) {
        return EnumUtil.getType(OPCServerClientMode.class, id);
    }

    public String getName() {
        return name;
    }
}
