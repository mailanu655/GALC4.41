package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;
      

public enum InstalledPartStatus implements IdEnum<InstalledPartStatus> {

    REMOVED(-9), 
    NC(-2), 
    BLANK(-1), 
    NG(0), 
    OK(1), 
    ACCEPT(2), 
    REJECT(3), 
    MISSING(4), 
    PENDING(5), 
    REPAIRED(9),
    NM(11) /* NOT MARRIED - a client side status for invalid product association */;

    private int id;

    private InstalledPartStatus(int id) {
        this.id = id;
    }

    public int getId() {
        // TODO Auto-generated method stub
        return id;
    }

    public static InstalledPartStatus getType(int id) {
        return EnumUtil.getType(InstalledPartStatus.class, id);
    }


}
