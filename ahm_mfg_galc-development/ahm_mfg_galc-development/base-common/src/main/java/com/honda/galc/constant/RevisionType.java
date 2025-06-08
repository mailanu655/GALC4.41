package com.honda.galc.constant;

public enum RevisionType {
	PLATFORM_CHG("PLATFORM CHG"),
	PDDA_STD("PDDA STD"),
	PDDA_MASS("PDDA MASS"),
	INVALID("INVALID");
	
	private String revType;
	
	private RevisionType(String revType) {
		this.setRevType(revType);
	}

	public String getRevType() {
		return revType;
	}

	public void setRevType(String revType) {
		this.revType = revType;
	}
}