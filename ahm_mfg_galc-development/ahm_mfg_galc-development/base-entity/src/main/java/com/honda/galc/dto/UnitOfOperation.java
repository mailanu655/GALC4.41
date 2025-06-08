package com.honda.galc.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class UnitOfOperation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String processPointId = null;

	private String prodSpecCode = null;
	
	private String pddaMaintenanceId = null;
	
	private String unitOperationDesc = null;
	
	private Integer unitSeqNo = null;
	
	private String unitNo = null;
	
	private String unitBasePartNo = null;
	
	private String workPtDescText = null;
	
	private Timestamp unitCreateDate = null;
	
	private String unitCreateDateStr = null;

	private String partFlg = "";

	private String asmProcessNo = "";

	private String asmProcessName = "";

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProdSpecCode() {
		return prodSpecCode;
	}

	public void setProdSpecCode(String prodSpecCode) {
		this.prodSpecCode = prodSpecCode;
	}

	public String getPddaMaintenanceId() {
		return pddaMaintenanceId;
	}

	public void setPddaMaintenanceId(String pddaMaintenanceId) {
		this.pddaMaintenanceId = pddaMaintenanceId;
	}

	public String getUnitOperationDesc() {
		return unitOperationDesc;
	}

	public void setUnitOperationDesc(String unitOperationDesc) {
		this.unitOperationDesc = unitOperationDesc;
	}

	public String getWorkPtDescText() {
		return workPtDescText;
	}

	public void setWorkPtDescText(String workPtDescText) {
		this.workPtDescText = workPtDescText;
	}

	public Integer getUnitSeqNo() {
		return unitSeqNo;
	}

	public void setUnitSeqNo(Integer unitSeqNo) {
		this.unitSeqNo = unitSeqNo;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUnitBasePartNo() {
		return unitBasePartNo;
	}

	public void setUnitBasePartNo(String unitBasePartNo) {
		this.unitBasePartNo = unitBasePartNo;
	}

	public Timestamp getUnitCreateDate() {
		return unitCreateDate;
	}

	public void setUnitCreateDate(Timestamp unitCreateDate) {
		this.unitCreateDate = unitCreateDate;
	}

	public String getUnitCreateDateStr() {
		return unitCreateDateStr;
	}

	public void setUnitCreateDateStr(String unitCreateDateStr) {
		this.unitCreateDateStr = unitCreateDateStr;
	}
	
	public String getPartFlg() {
		return partFlg;
	}

	public void setPartFlg(String partFlg) {
		this.partFlg = partFlg;
	}
	
	public String getAsmProcessNo() {
		return asmProcessNo;
	}

	public void setAsmProcessNo(String asmProcessNo) {
		this.asmProcessNo = asmProcessNo;
	}

	public String getAsmProcessName() {
		return asmProcessName;
	}

	public void setAsmProcessName(String asmProcessName) {
		this.asmProcessName = asmProcessName;
	}
}
