package com.honda.ahm.lc.model;

public class ProcessPoint extends AuditEntry {
    private static final long serialVersionUID = 1L;

   
    private String processPointId;

   
    private String processPointName;

   
    private String processPointDescription;

    
    private int processPointTypeId;

    
    private String siteName;

    
    private String plantName;

    
    private String divisionId;

    
    private String divisionName;

   
    private String lineId;

    
    private String lineName;

    
    private String backFillProcessPointId;

   
    private int sequenceNumber;

    
    private short trackingPointFlag;

    
    private short recoveryPointFlag;

    
    private short passingCountFlag;

	
    private String currentProductionLot;
    
    
    private String featureType;
    
   
    private String featureId;
    
   
    private String currentKdLot;


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


	public int getProcessPointTypeId() {
		return processPointTypeId;
	}


	public void setProcessPointTypeId(int processPointTypeId) {
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


	public int getSequenceNumber() {
		return sequenceNumber;
	}


	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}


	public short getTrackingPointFlag() {
		return trackingPointFlag;
	}


	public void setTrackingPointFlag(short trackingPointFlag) {
		this.trackingPointFlag = trackingPointFlag;
	}


	public short getRecoveryPointFlag() {
		return recoveryPointFlag;
	}


	public void setRecoveryPointFlag(short recoveryPointFlag) {
		this.recoveryPointFlag = recoveryPointFlag;
	}


	public short getPassingCountFlag() {
		return passingCountFlag;
	}


	public void setPassingCountFlag(short passingCountFlag) {
		this.passingCountFlag = passingCountFlag;
	}


	public String getCurrentProductionLot() {
		return currentProductionLot;
	}


	public void setCurrentProductionLot(String currentProductionLot) {
		this.currentProductionLot = currentProductionLot;
	}


	public String getFeatureType() {
		return featureType;
	}


	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}


	public String getFeatureId() {
		return featureId;
	}


	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}


	public String getCurrentKdLot() {
		return currentKdLot;
	}


	public void setCurrentKdLot(String currentKdLot) {
		this.currentKdLot = currentKdLot;
	}
    
    
    
}
