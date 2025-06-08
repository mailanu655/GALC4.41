package com.honda.galc.service.printing;

import com.honda.galc.enumtype.IdEnum;
import com.honda.galc.enumtype.EnumUtil;


public enum JasperExternalPrintAttributes implements IdEnum<JasperExternalPrintAttributes>{
	CLIENT_ID(0),
	JASPER_DUPLEX_FLAG(1), 
	TRAY_VALUE(2),
	VIN(3),
	PRODUCT_ID(4),
	DEFECT_LIST(5),
	PRINT_QUANTITY(6),
	PRINT_FORMAT(7),
	JASPER_REPORT_PAGE_TYPE(8),
	JASPER_REPORT_PAGE_ORIENTATION(9),
	JASPER_REPORT_DUPLEX_TUMBLE(10);
	
	private final int id;
	
	private JasperExternalPrintAttributes(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static JasperExternalPrintAttributes getType(int id) {
		return EnumUtil.getType(JasperExternalPrintAttributes.class, id);
	}
	
	public void getEnumConstant(){
		JasperExternalPrintAttributes.class.getEnumConstants();
	}
}
