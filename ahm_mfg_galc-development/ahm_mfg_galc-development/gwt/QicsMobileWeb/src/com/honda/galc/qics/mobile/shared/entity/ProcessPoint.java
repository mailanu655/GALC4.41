package com.honda.galc.qics.mobile.shared.entity;

import javax.annotation.Generated;

@Generated("com.googlecode.jsonschema2pojo")
public class ProcessPoint extends AuditEntry {

	private String processPointId;
	private String processPointName;
	private String processPointDescription;
	private Integer processPointTypeId;
	private String siteName;
	private String plantName;
	private String divisionId;
	private String divisionName;
	private String lineId;
	private String lineName;
	private String backFillProcessPointId;
	private Integer sequenceNumber;
	private Integer trackingPointFlag;
	private Integer recoveryPointFlag;
	private Integer passingCountFlag;
	private String currentProductionLot;
	private String applicationTasks;


	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public String getProcessPointDescription() {
		return processPointDescription;
	}

	public void setProcessPointDescription(String processPointDescription) {
		this.processPointDescription = processPointDescription;
	}

	public Integer getProcessPointTypeId() {
		return processPointTypeId;
	}

	public void setProcessPointTypeId(Integer processPointTypeId) {
		this.processPointTypeId = processPointTypeId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getBackFillProcessPointId() {
		return backFillProcessPointId;
	}

	public void setBackFillProcessPointId(String backFillProcessPointId) {
		this.backFillProcessPointId = backFillProcessPointId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getTrackingPointFlag() {
		return trackingPointFlag;
	}

	public void setTrackingPointFlag(Integer trackingPointFlag) {
		this.trackingPointFlag = trackingPointFlag;
	}

	public Integer getRecoveryPointFlag() {
		return recoveryPointFlag;
	}

	public void setRecoveryPointFlag(Integer recoveryPointFlag) {
		this.recoveryPointFlag = recoveryPointFlag;
	}

	public Integer getPassingCountFlag() {
		return passingCountFlag;
	}

	public void setPassingCountFlag(Integer passingCountFlag) {
		this.passingCountFlag = passingCountFlag;
	}

	public String getCurrentProductionLot() {
		return currentProductionLot;
	}

	public void setCurrentProductionLot(String currentProductionLot) {
		this.currentProductionLot = currentProductionLot;
	}

	public String getApplicationTasks() {
		return applicationTasks;
	}

	public void setApplicationTasks(String applicationTasks) {
		this.applicationTasks = applicationTasks;
	}


}