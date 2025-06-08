package com.honda.galc.qics.mobile.shared.entity;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class InspectionPartDescriptionId {

	private String inspectionPartName;
	private String inspectionPartLocationName;
	private String partGroupName;

	public String getInspectionPartName() {
		return inspectionPartName;
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getInspectionPartLocationName() {
		return inspectionPartLocationName;
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	public String getPartGroupName() {
		return partGroupName;
	}

	public void setPartGroupName(String partGroupName) {
		this.partGroupName = partGroupName;
	}

}
