package com.honda.galc.teamleader.enumtype;

public enum EngineColumn implements Column{
	PRODUCT_ID 			("EIN", "getProductId"),
	VIN					("VIN", "getVin"),
	MISSION_SERIAL_NO 	("Mission SN", "getMissionSerialNo"),
	ENGINE_FIRING_FLAG 	("Engine Firing Flag", "getEngineFiringFlag"),
	MISSION_STATUS 		("Mission Status", "getMissionStatus"),
	FIRING_TYPE 		("Firing Type", "getFiringType"),
	REPAIR_FLAG 		("Repair Flag", "getRepairFlag"),
	ACTUAL_MISSION_TYPE ("Actual Mission Type", "getActualMissionType"),
	DEFECT_STATUS 		("Defect Status", "getDefectStatusValue"),
	PLANT_CODE 			("Plant Code", "getPlantCode");
	
	private final String columnName;
	private final String columnGetter;
	
	private EngineColumn(String columnName, String columnGetter) {
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