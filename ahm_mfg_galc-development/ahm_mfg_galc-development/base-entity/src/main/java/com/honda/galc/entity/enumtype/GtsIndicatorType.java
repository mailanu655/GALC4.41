package com.honda.galc.entity.enumtype;

public enum GtsIndicatorType {
	
	MOVE_IN_PROGRESS("MP","receiveMoveInProgress"),
    CARRIER_PRESENT("CP","receiveCarrierPresent"),
    READER("RDR","receiveReader"),
    BODY_COUNT("BC","receiveBodyCountChange"),
    BODY_POSITION("UBP","receiveBodyPositionChange"),
    MOVE_STATUS("MV","receiveMoveStatus"),
    ACTIVE_LANE("AL","receiveActiveLane"),
    LINE_FULL("LF","receiveLaneFull"),
    CPLF("CPLF","receiveCarrierPresentLaneFull"),
    HEART_BEAT("HB","receiveHeartbeat"),
    CONVEYOR_STATUS("CS","receiveConveyorStatus"),
    CONTROL_BOX("CB","receiveControlBox"),
    GATA_STATUS("GS","receiveGateStatus"),
    PHOTO_EYE_STATUS("PE","receivePhotoEyeStatus"),
	MOVE_CONTROL_REQUEST("MCR", "receiveMoveControlCheckRequest"),
	INSPECTION_STATUS("IS","receiveInspectionStatus"),
    TOGGLE_LABEL("TL","receiveToggleLabel");
	
    private String type;
	private String methodName;
	
	private GtsIndicatorType(String type, String methodName) {
		this.type = type;
		this.methodName = methodName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public static String getMethodName(String type) {
		GtsIndicatorType indicatorType = getIndicatorType(type);
		return indicatorType == null ? null : indicatorType.getMethodName();
	}
	
	public static GtsIndicatorType getIndicatorType(String type) {
		for(GtsIndicatorType indicatorType : values()) {
			if(indicatorType.getType().equalsIgnoreCase(type)) return indicatorType;
		}
		return null;
	}
}
