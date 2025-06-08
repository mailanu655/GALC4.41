package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum PrintAttributeType implements IdEnum<PrintAttributeType> {
    None(0), 
    Static(1), 
    Tag(2), 
    SQL(3), 
    AttributeByMTOC(4), 
    Date(5), 
    List(6), 
    Class(7),
    AttributeByEngineSpec(8),
    AttributeByFrameSpec(9),
    AttributeByProduct(10),
    AttributeByEngine(11),
    AttributeByFrame(12),
    AttributeByProductionLot(13),
    SQL_COLLECTION(14),
    JPQL(15),
    JPQL_COLLECTION(16),
    PartSpecByMTOC(17),
    AttributeByPartSpec(18),
    AttributeByService(19);

    private final int id;

    private PrintAttributeType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PrintAttributeType getType(int id) {
        return EnumUtil.getType(PrintAttributeType.class, id);
    }

}
