package com.honda.galc.data;

public enum LetInspectionParamType {

	TEST_ATTRIB("1"), TEST_PARAM("2"), TEST_FAULT_CODE("3");

	private String type;

	private LetInspectionParamType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}