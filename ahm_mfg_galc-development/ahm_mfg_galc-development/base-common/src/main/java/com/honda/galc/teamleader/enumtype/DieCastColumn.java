package com.honda.galc.teamleader.enumtype;

public enum DieCastColumn implements Column {
	PRODUCT_ID						("Product ID","getProductId"),
	DC_SERIAL_NUMBER				("DC SN","getDcSerialNumber"),
	MC_SERIAL_NUMBER				("MC SN","getMcSerialNumber"),
	ENGINE_SERIAL_NUMBER			("Engine SN","getEngineSerialNumber"),
	DUNNAGE							("Dunnage","getDunnage"),
	HOLD_STATUS						("Hold Status","getHoldStatus"),
	DEFECT_STATUS					("Defect Status","getDefectStatusValue"),
	MODEL							("Model","getModelCode"),
	LAST_PASSING_PROCESS_POINT_ID 	("Last Process", "getLastPassingProcessPointId"),
	TRACKING_STATUS 				("Tracking Status", "getTrackingStatus");
	
	private final String columnName;
	private final String columnGetter;
	
	private DieCastColumn(String columnName, String columnGetter) {
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