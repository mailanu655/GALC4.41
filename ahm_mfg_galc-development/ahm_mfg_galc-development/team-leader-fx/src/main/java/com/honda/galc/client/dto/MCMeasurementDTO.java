package com.honda.galc.client.dto;

import java.util.Comparator;

public class MCMeasurementDTO implements Cloneable, Comparable<MCMeasurementDTO> {

	private String operationName;
	private String partRev;
	private String view;
	private String processor;
	private String measurementType;
	private String seqNumber;
	private String deviceId;
	private String deviceMsg;
	private String minLimit;
	private String maxLimit;
	private String maxAttempts;
	private String partId;

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

	public String getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(String seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMsg() {
		return deviceMsg;
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public String getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(String minLimit) {
		this.minLimit = minLimit;
	}

	public String getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(String maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(String maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getPartRev() {
		return partRev;
	}

	public void setPartRev(String partRev) {
		this.partRev = partRev;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	public int compareTo(MCMeasurementDTO meas) {

		return Integer.parseInt(this.seqNumber)
				- Integer.parseInt(meas.seqNumber);

	}

}
