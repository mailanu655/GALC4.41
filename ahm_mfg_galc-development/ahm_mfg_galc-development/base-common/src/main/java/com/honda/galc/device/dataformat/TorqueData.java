package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class TorqueData extends InputData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	String productId;
	double peakTorque;
	boolean peakTorqueStatus;
	double finalAngle;
	boolean finalAngleStatus;
	String tighteningId;
    boolean tighteningStatus;
    
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = StringUtils.trim(productId);
	}
	
	public double getPeakTorque() {
		return peakTorque;
	}
	
	public void setPeakTorque(double peakTorque) {
		this.peakTorque = peakTorque;
	}
	
	public boolean isPeakTorqueStatus() {
		return peakTorqueStatus;
	}
	
	public void setPeakTorqueStatus(boolean peakTorqueStatus) {
		this.peakTorqueStatus = peakTorqueStatus;
	}
	
	public double getFinalAngle() {
		return finalAngle;
	}
	
	public void setFinalAngle(double finalAngle) {
		this.finalAngle = finalAngle;
	}
	
	public boolean isFinalAngleStatus() {
		return finalAngleStatus;
	}
	
	public void setFinalAngleStatus(boolean finalAngleStatus) {
		this.finalAngleStatus = finalAngleStatus;
	}
	
	public String getTighteningId() {
		return tighteningId;
	}
	
	public void setTighteningId(String tighteningId) {
		this.tighteningId = StringUtils.trim(tighteningId);
	}
	
	public boolean isTighteningStatus() {
		return tighteningStatus;
	}
	
	public void setTighteningStatus(boolean tighteningStatus) {
		this.tighteningStatus = tighteningStatus;
	}

	@Override
	public String toString() {
		return this.tighteningId + ":" + this.peakTorque;
	}
	
	
    
}
