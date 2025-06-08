package com.honda.galc.entity.product;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;

public class DevicePartDetails  implements IDeviceData, Serializable{

	private static final long serialVersionUID = 1L;

	private String partName;
	private String partSerialNumberMask;
	private String instructionCode;
	private Integer partMaxAttempts;
	private Integer measurementSeqNum;
	private Double minimumLimit;
	private Double maximumLimit;
	private Integer maxAttempts;
	
	
	
	public String getPartName() {
		return partName;
	}



	public void setPartName(String partName) {
		this.partName = partName;
	}



	public String getPartSerialNumberMask() {
		return partSerialNumberMask;
	}



	public void setPartSerialNumberMask(String partSerialNumberMask) {
		this.partSerialNumberMask = partSerialNumberMask;
	}



	public String getInstructionCode() {
		return instructionCode;
	}



	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}



	public Integer getPartMaxAttempts() {
		return partMaxAttempts;
	}



	public void setPartMaxAttempts(Integer partMaxAttempts) {
		this.partMaxAttempts = partMaxAttempts;
	}



	public Integer getMeasurementSeqNum() {
		return measurementSeqNum;
	}



	public void setMeasurementSeqNum(Integer measurementSeqNum) {
		this.measurementSeqNum = measurementSeqNum;
	}



	public Double getMinimumLimit() {
		return minimumLimit;
	}



	public void setMinimumLimit(Double minimumLimit) {
		this.minimumLimit = minimumLimit;
	}



	public Double getMaximumLimit() {
		return maximumLimit;
	}



	public void setMaximumLimit(Double maximumLimit) {
		this.maximumLimit = maximumLimit;
	}



	public Integer getMaxAttempts() {
		return maxAttempts;
	}



	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}



	public DevicePartDetails() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String toString(){
		return "partName :" + partName + "\n partSerialNumberMask :" +partSerialNumberMask + "\n instructionCode :"+ instructionCode
		+ "\n partMaxAttempts :"+ partMaxAttempts + "\n measurementSeqNum :" + measurementSeqNum + "\n minimumLimit :" + minimumLimit
		+ "\n maximumLimit :"+ maximumLimit + "\n maxAttempts :"+ maxAttempts;
	}
}