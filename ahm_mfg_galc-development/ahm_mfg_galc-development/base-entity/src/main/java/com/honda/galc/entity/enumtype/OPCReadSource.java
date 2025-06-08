package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum OPCReadSource implements IdEnum<OPCReadSource> {
    Device(0), Cache(1);

    private final int id;

    private OPCReadSource(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static OPCReadSource getType(int id) {
        return EnumUtil.getType(OPCReadSource.class, id);
    }

}
