package com.honda.galc.teamleader.enumtype;

public enum FrameColumn implements Column{
	PRODUCT_ID 					("VIN", "getProductId"),
	ENGINE_SERIAL_NO 			("Engine SN", "getEngineSerialNo"),
	MISSION_SERIAL_NO 			("Mission SN", "getMissionSerialNo"),
	STRAIGHT_SHIP_PERCENTAGE 	("Straight Ship %", "getStraightShipPercentage"),
	KEY_NO 						("Key No", "getKeyNo"),
	SHORT_VIN 					("Short VIN", "getShortVin"),
	ENGINE_STATUS 				("Engine Status", "getEngineStatus"),
	AF_ON_SEQUENCE_NUMBER 		("AF ON Seq", "getAfOnSequenceNumber"),
	ACTUAL_MISSION_TYPE 		("Actual Mission Type", "getActualMissionType"),
	PURCHASE_CONTRACT_NUMBER 	("Purchase Contract No", "getPurchaseContractNumber");
	
	private final String columnName;
	private final String columnGetter;
	
	private FrameColumn(String columnName, String columnGetter) {
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