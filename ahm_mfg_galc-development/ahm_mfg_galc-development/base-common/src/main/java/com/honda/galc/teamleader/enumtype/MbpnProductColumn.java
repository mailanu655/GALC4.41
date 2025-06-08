package com.honda.galc.teamleader.enumtype;

public enum MbpnProductColumn implements Column{
	
	PRODUCT_ID 						("Product ID", "getProductId"),
	CURRENT_PRODUCT_SPEC_CODE		("Current Spec Code", "getCurrentProductSpecCode"),
	CURRENT_ORDER_NO 				("Current Order No", "getCurrentOrderNo"),
	TRACKING_SEQ 					("Tracking Seq", "getTrackingSeq"),
	HOLD_STATUS_ID 					("Hold Status", "getHoldStatusId"),
	PRODUCT_STATUS_ID 				("Product Status", "getProductStatusId"),
	CONTAINER_ID 					("Container ID", "getContainerId"),
	CONTAINER_POS 					("Container POS", "getContainerPos"),
	DEFECT_STATUS 					("Defect Status", "getDefectStatusValue"),
	EXTERNAL_BUILD 					("External Build", "getExternalBuild"),
	LAST_PASSING_PROCESS_POINT_ID 	("Last Process", "getLastPassingProcessPointId"),
	TRACKING_STATUS 				("Tracking Status", "getTrackingStatus");
	
	private final String columnName;
	private final String columnGetter;
	
	private MbpnProductColumn(String columnName, String columnGetter) {
		this.columnName = columnName;
		this.columnGetter = columnGetter;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
	
	public String getColumnGetter() {
		return this.columnGetter;
	}
}