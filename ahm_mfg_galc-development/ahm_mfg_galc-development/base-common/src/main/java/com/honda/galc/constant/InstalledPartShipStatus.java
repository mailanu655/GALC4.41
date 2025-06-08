package com.honda.galc.constant;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;
      
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 03, 2016
 */
public enum InstalledPartShipStatus implements IdEnum<InstalledPartShipStatus> {

    NG(0), OK(1);

    private int id;

    private InstalledPartShipStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static InstalledPartShipStatus getType(int id) {
        return EnumUtil.getType(InstalledPartShipStatus.class, id);
    }


}
