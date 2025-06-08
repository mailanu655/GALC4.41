package com.honda.galc.client.teamleader.model;

import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.PartSpec;

public class LotControlPartSpecRule {

	private PartSpec partSpec;

	private int sequenceNumber;

	private int expectedInstallTime;

	private boolean verificationFlag;

	private int serialNumberScanFlag;
	    
	private String subId;

	private String instructionCode;
	    
	private boolean serialNumberUniqueFlag;
		
	private String strategy;
		
	private String deviceId;
	
	private boolean select;
	
	private boolean partConfirmFlag;
	
	private boolean qiDefectFlag;
	
	public PartSpec getPartSpec() {
		return partSpec;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public int getExpectedInstallTime() {
		return expectedInstallTime;
	}

	public boolean getVerificationFlag() {
		return verificationFlag;
	}

	public int getSerialNumberScanFlag() {
		return serialNumberScanFlag;
	}

	public String getSubId() {
		return subId;
	}

	public String getInstructionCode() {
		return instructionCode;
	}

	public boolean getSerialNumberUniqueFlag() {
		return serialNumberUniqueFlag;
	}

	public String getStrategy() {
		return strategy;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setPartSpec(PartSpec partSpec) {
		this.partSpec = partSpec;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public void setExpectedInstallTime(int expectedInstallTime) {
		this.expectedInstallTime = expectedInstallTime;
	}

	public void setVerificationFlag(boolean verificationFlag) {
		this.verificationFlag = verificationFlag;
	}

	public void setSerialNumberScanFlag(int serialNumberScanFlag) {
		this.serialNumberScanFlag = serialNumberScanFlag;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public void setSerialNumberUniqueFlag(boolean serialNumberUniqueFlag) {
		this.serialNumberUniqueFlag = serialNumberUniqueFlag;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public PartSerialNumberScanType getSerialNumberScanType() {
		return PartSerialNumberScanType.getType(serialNumberScanFlag);
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public boolean isPartConfirmFlag() {
		return partConfirmFlag;
	}

	public void setPartConfirmFlag(boolean partConfirmFlag) {
		this.partConfirmFlag = partConfirmFlag;
	}
	
	public boolean isQiDefectFlag() {
		return qiDefectFlag;
	}

	public void setQiDefectFlag(boolean qiDefectFlag) {
		this.qiDefectFlag = qiDefectFlag;
	}
	
}
