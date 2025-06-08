package com.honda.galc.qics.mobile.shared.entity;


import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class DefectGroup extends AuditEntry {

	private String defectGroupName;
	private String defectGroupDescriptionShort;
	private String defectGroupDescriptionLong;
	private String imageName;
	private String modelCode;

	public String getDefectGroupName() {
		return defectGroupName;
	}

	public void setDefectGroupName(String defectGroupName) {
		this.defectGroupName = defectGroupName;
	}

	public String getDefectGroupDescriptionShort() {
		return defectGroupDescriptionShort;
	}

	public void setDefectGroupDescriptionShort(
			String defectGroupDescriptionShort) {
		this.defectGroupDescriptionShort = defectGroupDescriptionShort;
	}

	public String getDefectGroupDescriptionLong() {
		return defectGroupDescriptionLong;
	}

	public void setDefectGroupDescriptionLong(String defectGroupDescriptionLong) {
		this.defectGroupDescriptionLong = defectGroupDescriptionLong;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}



}