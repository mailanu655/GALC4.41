package com.honda.galc.openprotocol.model;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * Jan 30, 2009
 */

@XStreamAlias("OPMessage")
public class LastTighteningResult extends AbstractOPMessage implements ILastTighteningResult
{	@Tag(name="")
	@XStreamAlias("CELL_ID")
	private String _cellId = "";
	
	@Tag(name="")
	@XStreamAlias("CHANNEL_ID")
	private String _channelId = "";
	
	@Tag(name="")
	@XStreamAlias("CONTROLLER_NAME")
	private String _controllerName = "";
	
	@Tag(name = "PRODUCT_ID")
	@XStreamAlias("VIN_NUMBER") 
	private String _productId = "";
	
	@Tag(name="")
	@XStreamAlias("JOB_NUMBER")
	private int _jobNumber;	
	
	@Tag(name="")
	@XStreamAlias("PSET_NUMBER")
	private int _psetNumber;
	
	@Tag(name="")
	@XStreamAlias("BATCH_SIZE")
	private int _batchSize;
	
	@Tag(name="")
	@XStreamAlias("BATCH_COUNTER")
	private int _batchCounter;
	
	@Tag(name = "TIGHTENING_STATUS")
	@XStreamAlias("TIGHTENING_STATUS")
	private int _tighteningStatus;
	
	@Tag(name = "PEAK_TORQUE_STATUS")
	@XStreamAlias("TORQUE_STATUS")
	private int _torqueStatus;
	
	@Tag(name = "FINAL_ANGLE_STATUS")
	@XStreamAlias("ANGLE_STATUS")
	private int _angleStatus;
	
	@Tag(name="")
	@XStreamAlias("TORQUE_MIN_LIMIT")
	private String _torqueMinLimit = "";
	
	@Tag(name="")
	@XStreamAlias("TORQUE_MAX_LIMIT")
	private String _torqueMaxLimit = "";
	
	@Tag(name="")
	@XStreamAlias("TORQUE_FINAL_TARGET")
	private String _torqueFinalTarget = "";
	
	@Tag(name = "PEAK_TORQUE")
	@XStreamAlias("TORQUE")
	private double _torque;
	
	@Tag(name="")
	@XStreamAlias("ANGLE_MIN")
	private String _angleMin = "";
	
	@Tag(name="")
	@XStreamAlias("ANGLE_MAX")
	private String _angleMax = "";
	
	@Tag(name="")
	@XStreamAlias("FINAL_ANGLE_TARGET")
	private String _finalAngleTarget = "";
	
	@Tag(name = "FINAL_ANGLE")
	@XStreamAlias("ANGLE")
	private double _angle;
	
	@Tag(name="")
	@XStreamAlias("TIME_STAMP")
	private String _timeStamp = "";
	
	@Tag(name="")
	@XStreamAlias("LAST_PSET_CHANGE") 
	private String _lastPSetChanageTime = "";
	
	@Tag(name="")
	@XStreamAlias("BATCH_STATUS")
	private int _batchStatus;
	
	@Tag(name = "TIGHTENING_ID")
	@XStreamAlias("TIGHTENING_ID") 
	private String _tighteningId;
	
	private String deviceId = "";
	
	public double getAngle() {
		return _angle;
	}

	public void setAngle(double angle) {
		_angle = angle;
	}

	public String getAngleMax() {
		return _angleMax;
	}

	public void setAngleMax(String max) {
		_angleMax = max;
	}

	public String getAngleMin() {
		return _angleMin;
	}

	public void setAngleMin(String min) {
		_angleMin = min;
	}

	public int getAngleStatus() {
		return _angleStatus;
	}

	public void setAngleStatus(int status) {
		_angleStatus = status;
	}

	public int getBatchCounter() {
		return _batchCounter;
	}

	public void setBatchCounter(int counter) {
		_batchCounter = counter;
	}

	public int getBatchSize() {
		return _batchSize;
	}

	public void setBatchSize(int size) {
		_batchSize = size;
	}

	public int getBatchStatus() {
		return _batchStatus;
	}

	public void setBatchStatus(int status) {
		_batchStatus = status;
	}

	public String getCellId() {
		return _cellId;
	}

	public void setCellId(String id) {
		_cellId = id;
	}

	public String getChannelId() {
		return _channelId;
	}

	public void setChannelId(String id) {
		_channelId = id;
	}

	public String getControllerName() {
		return _controllerName;
	}

	public void setControllerName(String name) {
		_controllerName = name;
	}

	public String getFinalAngleTarget() {
		return _finalAngleTarget;
	}

	public void setFinalAngleTarget(String angleTarget) {
		_finalAngleTarget = angleTarget;
	}

	public int getJobNumber() {
		return _jobNumber;
	}

	public void setJobNumber(int number) {
		_jobNumber = number;
	}

	public String getLastPSetChanageTime() {
		return _lastPSetChanageTime;
	}

	public void setLastPSetChanageTime(String setChanageTime) {
		_lastPSetChanageTime = setChanageTime;
	}

	public int getPsetNumber() {
		return _psetNumber;
	}

	public void setPsetNumber(int number) {
		_psetNumber = number;
	}

	public String getTighteningId() {
		return StringUtils.trim(_tighteningId);
	}

	public void setTighteningId(String id) {
		_tighteningId = id;
	}

	public int getTighteningStatus() {
		return _tighteningStatus;
	}

	public void setTighteningStatus(int status) {
		_tighteningStatus = status;
	}

	public String getTimeStamp() {
		return _timeStamp;
	}

	public void setTimeStamp(String stamp) {
		_timeStamp = stamp;
	}

	public double getTorque() {
		return _torque;
	}

	public void setTorque(double torque) {
		_torque = torque;
	}

	public String getTorqueFinalTarget() {
		return _torqueFinalTarget;
	}

	public void setTorqueFinalTarget(String finalTarget) {
		_torqueFinalTarget = finalTarget;
	}

	public String getTorqueMaxLimit() {
		return _torqueMaxLimit;
	}

	public void setTorqueMaxLimit(String maxLimit) {
		_torqueMaxLimit = maxLimit;
	}

	public String getTorqueMinLimit() {
		return _torqueMinLimit;
	}

	public void setTorqueMinLimit(String minLimit) {
		_torqueMinLimit = minLimit;
	}

	public int getTorqueStatus() {
		return _torqueStatus;
	}

	public void setTorqueStatus(int status) {
		_torqueStatus = status;
	}

	public String getProductId() {
		return _productId;
	}

	public void setProductId(String productId) {
		_productId = productId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
