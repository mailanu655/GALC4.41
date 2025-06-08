package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum ProductionLotStatus implements IdEnum<ProductionLotStatus> {
	PROCESS_DEFAULT(0),PROCESS_IN(1), PROCESS_OUT(2);
	
	int id;
	
	private ProductionLotStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ProductionLotStatus getType(int id) {
		return EnumUtil.getType(ProductionLotStatus.class, id);
	}
}
