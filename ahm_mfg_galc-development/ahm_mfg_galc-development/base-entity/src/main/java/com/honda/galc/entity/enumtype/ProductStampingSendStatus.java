package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * Nov 4, 2011
 */
public enum ProductStampingSendStatus implements IdEnum<ProductStampingSendStatus>{
	WAITING(0),
	SENT(1), 
	STAMPED(2), 
	SHIPPED(3),
	LAYOUT_BODY(10);

	int id;
	
	private ProductStampingSendStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ProductStampingSendStatus getType(int id) {
		return EnumUtil.getType(ProductStampingSendStatus.class, id);
	}
}
