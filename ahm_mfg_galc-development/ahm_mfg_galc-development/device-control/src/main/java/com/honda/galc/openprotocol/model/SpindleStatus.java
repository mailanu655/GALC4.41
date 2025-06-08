package com.honda.galc.openprotocol.model;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

//@XStreamAlias("OPMessage")
public class SpindleStatus implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamAlias("SPINDLE_NUMBER")
	private int spindleNumber;
	
	@XStreamAlias("CHANNEL_ID")
	private String channelId;
	
	@XStreamAlias("OVERALL_STATUS")
	private int overallStatus;
	
	@XStreamAlias("TORQUE_STATUS")
	private int torqueStatus;
	
	@XStreamAlias("TORQUE_RESULT")
	private double torqueResult;
	
	@XStreamAlias("ANGLE_STATUS")
	private int angleStatus;
	
	@XStreamAlias("ANGLE_RESULT")
	private double angleResult;

	
	//Getters & Setters
	public int getSpindleNumber() {
		return spindleNumber;
	}

	public void setSpindleNumber(int spindleNumber) {
		this.spindleNumber = spindleNumber;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public int getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(int overallStatus) {
		this.overallStatus = overallStatus;
	}

	public int getTorqueStatus() {
		return torqueStatus;
	}

	public void setTorqueStatus(int torqueStatus) {
		this.torqueStatus = torqueStatus;
	}

	public double getTorqueResult() {
		return torqueResult;
	}

	public void setTorqueResult(double torqueResult) {
		this.torqueResult = torqueResult;
	}

	public int getAngleStatus() {
		return angleStatus;
	}

	public void setAngleStatus(int angleStatus) {
		this.angleStatus = angleStatus;
	}

	public double getAngleResult() {
		return angleResult;
	}

	public void setAngleResult(double angleResult) {
		this.angleResult = angleResult;
	}
	
	
	
}
