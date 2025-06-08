package com.honda.galc.qics.mobile.shared.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class DefectResult extends AuditEntry {

	private DefectResultId id;
	private String associateNo;
	private String shift;
	private String date;
	private String actualTimestamp;
	private Integer defectStatus;
	private String imageName;
	private String responsibleDept;
	private String responsibleZone;
	private String repairMethodNamePlan;
	private Long repairTimePlan;
	private String repairAssociateNoPlan;
	private String repairAssociateNo;
	private String writeUpDepartment;
	private Long twoPartDefectFlag;
	private String iqsCategoryName;
	private String iqsItemName;
	private String regressionCode;
	private Long pointX;
	private Long pointY;
	private String entryDept;
	private Integer outstandingFlag;
	private String repairTimestamp;
	private String responsibleLine;
	private List<DefectRepairResult> defectRepairResults = new ArrayList<DefectRepairResult>();
	private DefectDescription defectDescription;
	private Boolean isNewDefect;
	private Boolean isChangeAtRepair;
	private Boolean engineFiring;
	private String entryStation;

	public DefectResultId getId() {
		return id;
	}

	public void setId(DefectResultId id) {
		this.id = id;
	}

	public String getAssociateNo() {
		return associateNo;
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(String actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public Integer getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(Integer defectStatus) {
		this.defectStatus = defectStatus;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getResponsibleDept() {
		return responsibleDept;
	}

	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	public String getResponsibleZone() {
		return responsibleZone;
	}

	public void setResponsibleZone(String responsibleZone) {
		this.responsibleZone = responsibleZone;
	}

	public String getRepairMethodNamePlan() {
		return repairMethodNamePlan;
	}

	public void setRepairMethodNamePlan(String repairMethodNamePlan) {
		this.repairMethodNamePlan = repairMethodNamePlan;
	}

	public Long getRepairTimePlan() {
		return repairTimePlan;
	}

	public void setRepairTimePlan(Long repairTimePlan) {
		this.repairTimePlan = repairTimePlan;
	}

	public String getRepairAssociateNoPlan() {
		return repairAssociateNoPlan;
	}

	public void setRepairAssociateNoPlan(String repairAssociateNoPlan) {
		this.repairAssociateNoPlan = repairAssociateNoPlan;
	}

	public String getRepairAssociateNo() {
		return repairAssociateNo;
	}

	public void setRepairAssociateNo(String repairAssociateNo) {
		this.repairAssociateNo = repairAssociateNo;
	}

	public String getWriteUpDepartment() {
		return writeUpDepartment;
	}

	public void setWriteUpDepartment(String writeUpDepartment) {
		this.writeUpDepartment = writeUpDepartment;
	}

	public Long getTwoPartDefectFlag() {
		return twoPartDefectFlag;
	}

	public void setTwoPartDefectFlag(Long twoPartDefectFlag) {
		this.twoPartDefectFlag = twoPartDefectFlag;
	}

	public String getIqsCategoryName() {
		return iqsCategoryName;
	}

	public void setIqsCategoryName(String iqsCategoryName) {
		this.iqsCategoryName = iqsCategoryName;
	}

	public String getIqsItemName() {
		return iqsItemName;
	}

	public void setIqsItemName(String iqsItemName) {
		this.iqsItemName = iqsItemName;
	}

	public String getRegressionCode() {
		return regressionCode;
	}

	public void setRegressionCode(String regressionCode) {
		this.regressionCode = regressionCode;
	}

	public Long getPointX() {
		return pointX;
	}

	public void setPointX(Long pointX) {
		this.pointX = pointX;
	}

	public Long getPointY() {
		return pointY;
	}

	public void setPointY(Long pointY) {
		this.pointY = pointY;
	}

	public String getEntryDept() {
		return entryDept;
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	public Integer getOutstandingFlag() {
		return outstandingFlag;
	}

	public void setOutstandingFlag(Integer outstandingFlag) {
		this.outstandingFlag = outstandingFlag;
	}

	public String getRepairTimestamp() {
		return repairTimestamp;
	}

	public void setRepairTimestamp(String repairTimestamp) {
		this.repairTimestamp = repairTimestamp;
	}

	public String getResponsibleLine() {
		return responsibleLine;
	}

	public void setResponsibleLine(String responsibleLine) {
		this.responsibleLine = responsibleLine;
	}

	public List<DefectRepairResult> getDefectRepairResults() {
		return defectRepairResults;
	}

	public void setDefectRepairResults(
			List<DefectRepairResult> defectRepairResults) {
		this.defectRepairResults = defectRepairResults;
	}

	public DefectDescription getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(DefectDescription defectDescription) {
		this.defectDescription = defectDescription;
	}

	public Boolean getIsNewDefect() {
		return isNewDefect;
	}

	public void setIsNewDefect(Boolean isNewDefect) {
		this.isNewDefect = isNewDefect;
	}

	public Boolean getIsChangeAtRepair() {
		return isChangeAtRepair;
	}

	public void setIsChangeAtRepair(Boolean isChangeAtRepair) {
		this.isChangeAtRepair = isChangeAtRepair;
	}

	public Boolean getEngineFiring() {
		return engineFiring;
	}

	public void setEngineFiring(Boolean engineFiring) {
		this.engineFiring = engineFiring;
	}

	public String getEntryStation() {
		return entryStation;
	}

	public void setEntryStation(String entryStation) {
		this.entryStation = entryStation;
	}



}
