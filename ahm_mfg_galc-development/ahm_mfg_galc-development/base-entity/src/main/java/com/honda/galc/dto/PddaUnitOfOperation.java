package com.honda.galc.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PddaUnitOfOperation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId = null;
	
	private String prodSpecCode = null;
	
	private String operationName = null;
	
	private Integer maintenanceId = null;
	
	private Integer unitSeqNo = null;
	
	private String basePartNo = null;
	
	private Timestamp createDate = null;
	
	private String createDateStr = null;
	
	private String operationDescTxt = null;
	
	private String workPtDescText = null;
	
	private String processPointId = null;
	
	private float  unitTotalTime =0.0f;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProdSpecCode() {
		return prodSpecCode;
	}

	public void setProdSpecCode(String prodSpecCode) {
		this.prodSpecCode = prodSpecCode;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Integer getMaintenanceId() {
		return maintenanceId;
	}

	public void setMaintenanceId(Integer maintenanceId) {
		this.maintenanceId = maintenanceId;
	}

	public Integer getUnitSeqNo() {
		return unitSeqNo;
	}

	public void setUnitSeqNo(Integer unitSeqNo) {
		this.unitSeqNo = unitSeqNo;
	}

	public String getBasePartNo() {
		return basePartNo;
	}

	public void setBasePartNo(String basePartNo) {
		this.basePartNo = basePartNo;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getOperationDescTxt() {
		return operationDescTxt;
	}

	public void setOperationDescTxt(String operationDescTxt) {
		this.operationDescTxt = operationDescTxt;
	}

	public String getWorkPtDescText() {
		return workPtDescText;
	}

	public void setWorkPtDescText(String workPtDescText) {
		this.workPtDescText = workPtDescText;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public float getUnitTotalTime() {
		return unitTotalTime;
	}

	public void setUnitTotalTime(float unitTotalTime) {
		this.unitTotalTime = unitTotalTime;
	}

	
}
