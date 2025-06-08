package com.honda.galc.qics.mobile.shared.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class DefectDescription extends AuditEntry {

	private DefectDescriptionId id;
	private String iqsCategoryName;
	private String iqsItemName;
	private String regressionCode;
	private Long lockMode;
	private String responsibleDept;
	private String responsibleZone;
	private Long twoPartDefectFlag;
	private Long inventoryRepairTime;
	private Long onlineRepairTime;
	private String responsibleLine;
	private Long engineFiringFlag;
	private List<InspectionPartDescription> inspectionPartDescriptions = new ArrayList<InspectionPartDescription>();
	private List<Object> inspectionTwoPartDescriptions = new ArrayList<Object>();

	public DefectDescriptionId getId() {
		return id;
	}

	public void setId(DefectDescriptionId id) {
		this.id = id;
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

	public Long getLockMode() {
		return lockMode;
	}

	public void setLockMode(Long lockMode) {
		this.lockMode = lockMode;
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

	public Long getTwoPartDefectFlag() {
		return twoPartDefectFlag;
	}

	public void setTwoPartDefectFlag(Long twoPartDefectFlag) {
		this.twoPartDefectFlag = twoPartDefectFlag;
	}

	public Long getInventoryRepairTime() {
		return inventoryRepairTime;
	}

	public void setInventoryRepairTime(Long inventoryRepairTime) {
		this.inventoryRepairTime = inventoryRepairTime;
	}

	public Long getOnlineRepairTime() {
		return onlineRepairTime;
	}

	public void setOnlineRepairTime(Long onlineRepairTime) {
		this.onlineRepairTime = onlineRepairTime;
	}

	public String getResponsibleLine() {
		return responsibleLine;
	}

	public void setResponsibleLine(String responsibleLine) {
		this.responsibleLine = responsibleLine;
	}

	public Long getEngineFiringFlag() {
		return engineFiringFlag;
	}

	public void setEngineFiringFlag(Long engineFiringFlag) {
		this.engineFiringFlag = engineFiringFlag;
	}

	public List<InspectionPartDescription> getInspectionPartDescriptions() {
		return inspectionPartDescriptions;
	}

	public void setInspectionPartDescriptions(
			List<InspectionPartDescription> inspectionPartDescriptions) {
		this.inspectionPartDescriptions = inspectionPartDescriptions;
	}

	public List<Object> getInspectionTwoPartDescriptions() {
		return inspectionTwoPartDescriptions;
	}

	public void setInspectionTwoPartDescriptions(
			List<Object> inspectionTwoPartDescriptions) {
		this.inspectionTwoPartDescriptions = inspectionTwoPartDescriptions;
	}


}
