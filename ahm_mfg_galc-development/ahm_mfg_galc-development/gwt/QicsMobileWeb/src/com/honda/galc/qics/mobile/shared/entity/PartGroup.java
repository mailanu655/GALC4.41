package com.honda.galc.qics.mobile.shared.entity;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class PartGroup extends AuditEntry {

	private String partGroupName;
	private String partGroupDescriptionShort;
	private String partGroupDescriptionLong;
	private String modelCode;

	public String getPartGroupName() {
		return partGroupName;
	}

	public void setPartGroupName(String partGroupName) {
		this.partGroupName = partGroupName;
	}

	public String getPartGroupDescriptionShort() {
		return partGroupDescriptionShort;
	}

	public void setPartGroupDescriptionShort(String partGroupDescriptionShort) {
		this.partGroupDescriptionShort = partGroupDescriptionShort;
	}

	public String getPartGroupDescriptionLong() {
		return partGroupDescriptionLong;
	}

	public void setPartGroupDescriptionLong(String partGroupDescriptionLong) {
		this.partGroupDescriptionLong = partGroupDescriptionLong;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}


}
