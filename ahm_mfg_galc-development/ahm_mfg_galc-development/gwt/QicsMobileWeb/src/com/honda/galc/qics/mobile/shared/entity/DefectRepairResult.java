package com.honda.galc.qics.mobile.shared.entity;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class DefectRepairResult extends AuditEntry {

	private DefectRepairResultId id;
	private String repairAssociateNo;
	private String actualTimestamp;
	private String actualProblemName;
	private String repairMethodName;
	private Long repairTime;
	private String repairDept;
	private String repairProcessPointId;
	private String comment;

	public DefectRepairResultId getId() {
		return id;
	}

	public void setId(DefectRepairResultId id) {
		this.id = id;
	}

	public String getRepairAssociateNo() {
		return repairAssociateNo;
	}

	public void setRepairAssociateNo(String repairAssociateNo) {
		this.repairAssociateNo = repairAssociateNo;
	}

	public String getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(String actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getActualProblemName() {
		return actualProblemName;
	}

	public void setActualProblemName(String actualProblemName) {
		this.actualProblemName = actualProblemName;
	}

	public String getRepairMethodName() {
		return repairMethodName;
	}

	public void setRepairMethodName(String repairMethodName) {
		this.repairMethodName = repairMethodName;
	}

	public Long getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Long repairTime) {
		this.repairTime = repairTime;
	}

	public String getRepairDept() {
		return repairDept;
	}

	public void setRepairDept(String repairDept) {
		this.repairDept = repairDept;
	}

	public String getRepairProcessPointId() {
		return repairProcessPointId;
	}

	public void setRepairProcessPointId(String repairProcessPointId) {
		this.repairProcessPointId = repairProcessPointId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


}
