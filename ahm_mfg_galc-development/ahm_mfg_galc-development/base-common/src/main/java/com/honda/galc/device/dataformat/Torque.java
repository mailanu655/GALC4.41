package com.honda.galc.device.dataformat;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

public class Torque extends MeasurementValue {
	
	private static final long serialVersionUID = 6897595012336664852L;
	
	private String processName;
	private int tighteningStatus;
	private double angle;
	private int angleStatus;
	private int torqueStatus;
	private int sequence;

	public Torque() {}
	
	public String getProcessName() {
		return processName;
	}
	
	public void setProcessName(String processName) {
		this.processName = StringUtils.trim(processName);
	}
	
	public int getTighteningStatus() {
		return tighteningStatus;
	}
	
	public void setTighteningStatus(int tighteningStatus) {
		this.tighteningStatus = tighteningStatus;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(Double angle) {
		this.angle = angle;
	}
	
	public int getAngleStatus() {
		return angleStatus;
	}
	
	public void setAngleStatus(int angleStatus) {
		this.angleStatus = angleStatus;
	}
	
	public double getTorqueValue() {
		return getMeasurementValue();
	}
	
	public void setTorqueValue(double torque) {
		super.measurementValue = torque;
	}
		
	public int getTorqueStatus() {
		return torqueStatus;
	}
	
	public void setTorqueStatus(int torqueStatus) {
		this.torqueStatus = torqueStatus;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getProcessName(), getAngle(),
				getAngleStatus(), getTorqueValue(), getTorqueStatus(), getSequence());
	}
}
