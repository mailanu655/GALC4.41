package com.honda.galc.client.dto;

public class ChangeFormUnitDTO {
	private String changeFormId;
	private String unitId;
	private String asmProcNo;
	private String description;
	private int apprProcMaintId;
	private int apprUnitMaintId;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public int getApprProcMaintId() {
		return apprProcMaintId;
	}
	public void setApprProcMaintId(int apprProcMaintId) {
		this.apprProcMaintId = apprProcMaintId;
	}
	public int getApprUnitMaintId() {
		return apprUnitMaintId;
	}
	public void setApprUnitMaintId(int apprUnitMaintId) {
		this.apprUnitMaintId = apprUnitMaintId;
	}
	public String getChangeFormId() {
		return changeFormId;
	}
	public void setChangeFormId(String changeFormId) {
		this.changeFormId = changeFormId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getAsmProcNo() {
		return asmProcNo;
	}
	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

}
