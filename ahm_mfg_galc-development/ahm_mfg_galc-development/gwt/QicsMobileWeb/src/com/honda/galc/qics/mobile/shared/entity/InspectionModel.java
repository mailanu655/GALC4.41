package com.honda.galc.qics.mobile.shared.entity;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class InspectionModel extends AuditEntry {

	private InspectionModelId id;
	private DefectGroup defectGroup;
	private PartGroup partGroup;
	
	public InspectionModelId getId() {
	return id;
	}
	
	public void setId(InspectionModelId id) {
	this.id = id;
	}
	
	public DefectGroup getDefectGroup() {
	return defectGroup;
	}
	
	public void setDefectGroup(DefectGroup defectGroup) {
	this.defectGroup = defectGroup;
	}
	
	public PartGroup getPartGroup() {
	return partGroup;
	}
	
	public void setPartGroup(PartGroup partGroup) {
	this.partGroup = partGroup;
	}
	


}
