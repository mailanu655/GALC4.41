package com.honda.galc.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnitOfOperationDetails implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer maintenanceId = null;
	
	private BigDecimal minTrqValQty = null;
	
	private BigDecimal maxTrqValQty = null;
	
	private String trqCharVal = null;
	
	private String tool = null;
	
	private String workPtDescText = null;
	
	private String workPtDetail = null;
	
	private String safetyPtDescText = null;
	
	private String AuxMatDescText = null;
	
	private String reactionPlan = null;
	
	private String controlMethod = null;
	
	private String specialControl = null;

	private String impactPoint = null;
	
	private String qualityPoint = null;
	
	private String unitOperationDesc = null;
	
	private String unitNo = null;
	
	private String unitRank = null;
	
	private String processNo = null;
	
	private String auxMsdsNo = null;
	
	private Date maintDate = null;
	
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	public Integer getMaintenanceId() {
		return maintenanceId;
	}

	public void setMaintenanceId(Integer maintenanceId) {
		this.maintenanceId = maintenanceId;
	}

	public BigDecimal getMinTrqValQty() {
		return minTrqValQty;
	}

	public void setMinTrqValQty(BigDecimal minTrqValQty) {
		this.minTrqValQty = minTrqValQty;
	}

	public BigDecimal getMaxTrqValQty() {
		return maxTrqValQty;
	}

	public void setMaxTrqValQty(BigDecimal maxTrqValQty) {
		this.maxTrqValQty = maxTrqValQty;
	}

	public String getTrqCharVal() {
		return trqCharVal;
	}

	public void setTrqCharVal(String trqCharVal) {
		this.trqCharVal = trqCharVal;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	public String getWorkPtDescText() {
		return workPtDescText;
	}

	public void setWorkPtDescText(String workPtDescText) {
		this.workPtDescText = workPtDescText;
	}

	public String getWorkPtDetail() {
		return workPtDetail;
	}

	public void setWorkPtDetail(String workPtDetail) {
		this.workPtDetail = workPtDetail;
	}

	public String getSafetyPtDescText() {
		return safetyPtDescText;
	}

	public void setSafetyPtDescText(String safetyPtDescText) {
		this.safetyPtDescText = safetyPtDescText;
	}

	public String getAuxMatDescText() {
		return AuxMatDescText;
	}

	public void setAuxMatDescText(String auxMatDescText) {
		AuxMatDescText = auxMatDescText;
	}

	public String getReactionPlan() {
		return reactionPlan;
	}

	public void setReactionPlan(String reactionPlan) {
		this.reactionPlan = reactionPlan;
	}

	public String getControlMethod() {
		return controlMethod;
	}

	public void setControlMethod(String controlMethod) {
		this.controlMethod = controlMethod;
	}
	
	
	public String getSpecialControl() {
		return specialControl;
	}

	public void setSpecialControl(String specialControl) {
		this.specialControl = specialControl;
	}
	
	
	public String getImpactPoint() {
		return impactPoint;
	}

	public void setImpactPoint(String impactPoint) {
		this.impactPoint = impactPoint;
	}
	
	public String getQualityPoint() {
		return qualityPoint;
	}

	public void setQualityPoint(String qualityPoint) {
		this.qualityPoint = qualityPoint;
	}
	public String getUnitOperationDesc() {
		return unitOperationDesc;
	}

	public void setUnitOperationDesc(String unitOperationDesc) {
		this.unitOperationDesc = unitOperationDesc;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUnitRank() {
		return unitRank;
	}

	public void setUnitRank(String unitRank) {
		this.unitRank = unitRank;
	}

	public String getProcessNo() {
		return processNo;
	}

	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}

	public String getAuxMsdsNo() {
		return auxMsdsNo;
	}

	public void setAuxMsdsNo(String auxMsdsNo) {
		this.auxMsdsNo = auxMsdsNo;
	}

	public Date getMaintDate() {
		return maintDate;
	}

	public void setMaintDate(Date maintDate) {
		this.maintDate = maintDate;
	}
	
	public String getMaintDateVal() {
		return (getMaintDate()!=null)?dateFormat.format(getMaintDate()):"";
	}
	
}
