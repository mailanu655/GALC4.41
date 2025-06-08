package com.honda.galc.enumtype;

/**
 * @author Alex Johnson
 * @date Jul 21, 2015
 */
public enum LotcontrolNotificationType  implements IdEnum<LotcontrolNotificationType> {

    UPDATE(0), 
    REMOVE(1);

    private int id;

    private LotcontrolNotificationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static LotcontrolNotificationType getType(int id) {
        return EnumUtil.getType(LotcontrolNotificationType.class, id);
    }
}
