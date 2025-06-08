package com.honda.galc.qics.mobile.shared.entity;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class Frame extends AuditEntry {

	private String productId;
	private String engineSerialNo;
	private Double straightShipPercentage;
	private String keyNo;
	private String shortVin;
	private Long engineStatus;
	private Long afOnSequenceNumber;
	private String missionSerialNo;
	private String actualMissionType;
	private String productionLot;
	private String productSpecCode;
	private String planOffDate;
	private String kdLotNumber;
	private String productionDate;
	private String productStartDate;
	private String actualOffDate;
	private Long autoHoldStatus;
	private String lastPassingProcessPointId;
	private String trackingStatus;
	private String prodLot;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getEngineSerialNo() {
		return engineSerialNo;
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}

	public Double getStraightShipPercentage() {
		return straightShipPercentage;
	}

	public void setStraightShipPercentage(Double straightShipPercentage) {
		this.straightShipPercentage = straightShipPercentage;
	}

	public String getKeyNo() {
		return keyNo;
	}

	public void setKeyNo(String keyNo) {
		this.keyNo = keyNo;
	}

	public String getShortVin() {
		return shortVin;
	}

	public void setShortVin(String shortVin) {
		this.shortVin = shortVin;
	}

	public Long getEngineStatus() {
		return engineStatus;
	}

	public void setEngineStatus(Long engineStatus) {
		this.engineStatus = engineStatus;
	}

	public Long getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}

	public void setAfOnSequenceNumber(Long afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}

	public String getMissionSerialNo() {
		return missionSerialNo;
	}

	public void setMissionSerialNo(String missionSerialNo) {
		this.missionSerialNo = missionSerialNo;
	}

	public String getActualMissionType() {
		return actualMissionType;
	}

	public void setActualMissionType(String actualMissionType) {
		this.actualMissionType = actualMissionType;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
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

	public Long getAutoHoldStatus() {
		return autoHoldStatus;
	}

	public void setAutoHoldStatus(Long autoHoldStatus) {
		this.autoHoldStatus = autoHoldStatus;
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

	public String getProdLot() {
		return prodLot;
	}

	public void setProdLot(String prodLot) {
		this.prodLot = prodLot;
	}


	public String getMto() {
		String mto = null;
		if ( this.productSpecCode != null && this.productSpecCode.length() >= 7 ) {
			mto = this.productSpecCode.substring(0,7);
		}
		return mto;
	}
}