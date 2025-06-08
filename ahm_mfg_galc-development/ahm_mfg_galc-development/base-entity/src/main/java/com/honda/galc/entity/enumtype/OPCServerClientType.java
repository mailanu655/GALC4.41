package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum OPCServerClientType implements IdEnum<OPCServerClientType> {
    HTTPClient(0, "HTTP Client"), RouterClient(1, "Router Client"),
    EmbeddedClient(2, "Embedded Client");

    private final int id;
    private final String name;

    private OPCServerClientType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public static OPCServerClientType getType(int id) {
        return EnumUtil.getType(OPCServerClientType.class, id);
    }

    public String getName() {
        return name;
    }
}
