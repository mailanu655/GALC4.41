package com.honda.ahm.lc.model;

public class Product extends AuditEntry {

	private String productSpecCode;

	protected String lastPassingProcessPointId;

	protected String trackingStatus;

	private String productionLot;

	private String planOffDate;

	private String kdLotNumber;

	private String productionDate;

	private String productStartDate;

	private String actualOffDate;

	private short autoHoldStatus;

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getLastPassingProcessPointId() {
		return lastPassingProcessPointId;
	}

	public void setLastPassingProcessPointId(String lastPassingProcessPointId) {
		this.lastPassingProcessPointId = lastPassingProcessPointId;
	}

	public String getTrackingStatus() {
		return trackingStatus;
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getPlanOffDate() {
		return planOffDate;
	}

	public void setPlanOffDate(String planOffDate) {
		this.planOffDate = planOffDate;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getProductStartDate() {
		return productStartDate;
	}

	public void setProductStartDate(String productStartDate) {
		this.productStartDate = productStartDate;
	}

	public String getActualOffDate() {
		return actualOffDate;
	}

	public void setActualOffDate(String actualOffDate) {
		this.actualOffDate = actualOffDate;
	}

	public short getAutoHoldStatus() {
		return autoHoldStatus;
	}

	public void setAutoHoldStatus(short autoHoldStatus) {
		this.autoHoldStatus = autoHoldStatus;
	}
	
	
}
