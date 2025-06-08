package com.honda.galc.client.enumtype;

import com.honda.galc.enumtype.IdEnum;
import com.honda.galc.enumtype.EnumUtil;

public enum TemplatePrintAttributes implements IdEnum<TemplatePrintAttributes>{
	CLIENT_ID(0),
	JASPER_DUPLEX_FLAG(1), 
	VIN(3),
	PRODUCT_ID(4),
	TRAY_VALUE(2);

	private final int id;
	
	private TemplatePrintAttributes(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TemplatePrintAttributes getType(int id) {
		return EnumUtil.getType(TemplatePrintAttributes.class, id);
	}
	
	public void getEnumConstant(){
		TemplatePrintAttributes.class.getEnumConstants();
	}
	
	
}

