package com.honda.galc.teamleader.enumtype;

public enum ProductColumn implements Column {
	PRODUCT_ID						("Product ID","getProductId"),
	PRODUCTION_LOT					("Prod Lot","getProductionLot"),
	PRODUCT_SPEC_CODE				("Product Spec","getProductSpecCode"),
	PLAN_OFF_DATE					("Plan Off Date","getPlanOffDate"),
	KD_LOT_NUMBER					("KD Lot","getKdLotNumber"),
	PRODUCTION_DATE					("Production Date","getFormattedProductionDate"),
	PRODUCT_START_DATE				("Product Start Date","getProductStartDate"),
	ACTUAL_OFF_DATE					("Actual Off Date","getActualOffDate"),
	AUTO_HOLD_STATUS				("Auto Hold Status","getAutoHoldStatus"),
	LAST_PASSING_PROCESS_POINT_ID 	("Last Process", "getLastPassingProcessPointId"),
	TRACKING_STATUS 				("Tracking Status", "getTrackingStatus");
	
	private final String columnName;
	private final String columnGetter;
	
	private ProductColumn(String columnName, String columnGetter) {
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