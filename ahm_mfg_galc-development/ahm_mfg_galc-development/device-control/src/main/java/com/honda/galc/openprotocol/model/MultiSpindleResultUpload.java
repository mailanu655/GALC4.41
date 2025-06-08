package com.honda.galc.openprotocol.model;

import java.util.List;

import com.honda.galc.device.IDeviceData;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("OPMessage")
public class MultiSpindleResultUpload extends AbstractOPMessage implements IDeviceData{
	@XStreamAlias("NUMBER_OF_SPINDLES")
	private int numberOfSpindles;

	@XStreamAlias("VIN_NUMBER")  
	private String productId;
	
	@XStreamAlias("JOB_NUMBER")
	private int jobNumber;

	@XStreamAlias("PSET_NUMBER")  
	private int psetNumber;
	
	@XStreamAlias("BATCH_SIZE")
	private int batchSize;
	
	@XStreamAlias("BATCH_COUNTER")
	private int batchCounter;
	
	@XStreamAlias("BATCH_STATUS")
	private int batchStatus;
	
	@XStreamAlias("TORQUE_MIN_LIMIT")   
	private String torqueMinLImit;
	
	@XStreamAlias("TORQUE_MAX_LIMIT")  
	private String torqueMaxLImit;
	
	@XStreamAlias("TORQUE_FINAL_TARGET")
	private String torqueFinalTarget;
	
	@XStreamAlias("ANGLE_MIN")  
	private String angleMin;
	
	@XStreamAlias("ANGLE_MAX")
	private String angleMax;
	
	@XStreamAlias("FINAL_ANGLE_TARGET")
	private String finalAngleTarget;
	
	@XStreamAlias("LAST_CHANGE_IN_PSET_SETTING")
	private String lastChangeInPsetSetting;
	
	@XStreamAlias("TIME")
	private String time;
	
	@XStreamAlias("SYNC_TIGHTENING_ID")
	private String syncTighteningId;
	
	@XStreamAlias("SYNC_OVERALL_STATUS")
	private int syncOverallStatus;
	
	//@XStreamOmitField
    //private List<SpindleStatus> spindleStatusList;
	@XStreamOmitField
	private List<SpindleStatus> spindleStatusList;
	
	
		
	//Getters & Setters
	public int getNumberOfSpindles() {
		return numberOfSpindles;
	}

	public void setNumberOfSpindles(int numberOfSpindles) {
		this.numberOfSpindles = numberOfSpindles;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(int jobNumber) {
		this.jobNumber = jobNumber;
	}

	public int getPsetNumber() {
		return psetNumber;
	}

	public void setPsetNumber(int psetNumber) {
		this.psetNumber = psetNumber;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public int getBatchCounter() {
		return batchCounter;
	}

	public void setBatchCounter(int batchCounter) {
		this.batchCounter = batchCounter;
	}

	public int getBatchStatus() {
		return batchStatus;
	}

	public void setBatchStatus(int batchStatus) {
		this.batchStatus = batchStatus;
	}

	public String getTorqueMinLImit() {
		return torqueMinLImit;
	}

	public void setTorqueMinLImit(String torqueMinLImit) {
		this.torqueMinLImit = torqueMinLImit;
	}

	public String getTorqueMaxLImit() {
		return torqueMaxLImit;
	}

	public void setTorqueMaxLImit(String torqueMaxLImit) {
		this.torqueMaxLImit = torqueMaxLImit;
	}

	public String getTorqueFinalTarget() {
		return torqueFinalTarget;
	}

	public void setTorqueFinalTarget(String torqueFinalTarget) {
		this.torqueFinalTarget = torqueFinalTarget;
	}

	public String getAngleMin() {
		return angleMin;
	}

	public void setAngleMin(String angleMin) {
		this.angleMin = angleMin;
	}

	public String getAngleMax() {
		return angleMax;
	}

	public void setAngleMax(String angleMax) {
		this.angleMax = angleMax;
	}

	public String getFinalAngleTarget() {
		return finalAngleTarget;
	}

	public void setFinalAngleTarget(String finalAngleTarget) {
		this.finalAngleTarget = finalAngleTarget;
	}

	public String getLastChangeInPsetSetting() {
		return lastChangeInPsetSetting;
	}

	public void setLastChangeInPsetSetting(String lastChangeInPsetSetting) {
		this.lastChangeInPsetSetting = lastChangeInPsetSetting;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSyncTighteningId() {
		return syncTighteningId;
	}

	public void setSyncTighteningId(String syncTighteningId) {
		this.syncTighteningId = syncTighteningId;
	}

	public int getSyncOverallStatus() {
		return syncOverallStatus;
	}

	public void setSyncOverallStatus(int syncOverallStatus) {
		this.syncOverallStatus = syncOverallStatus;
	}

	public List<SpindleStatus> getSpindleStatusList() {
		return spindleStatusList;
	}

	public void setSpindleStatusList(List<SpindleStatus> spindleStatusList) {
		this.spindleStatusList = spindleStatusList;
	}

}
