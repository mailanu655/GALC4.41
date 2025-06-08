package com.honda.galc.dto;

public class MCViosMasterOperationMeasurementDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	@DtoTag(name="UNIT_NO")
	private String unitNo;
	
	@DtoTag(name="MIN_LIMIT") 
	private double minLimit;
	
	@DtoTag(name="MAX_LIMIT")
	private double maxLimit;
	
	@DtoTag(name="DEVICE_MSG")
	private String deviceMsg;
	
	@DtoTag(name="DEVICE_ID")
	private String deviceId;
	
	@DtoTag(name="OP_MEAS_SEQ_NUM")
	private int noMeansSeq;
	
	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(int minLimit) {
		this.minLimit = minLimit;
	}

	public double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(int maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getDeviceMsg() {
		return deviceMsg;
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	

	public int getNoMeansSeq() {
		return noMeansSeq;
	}

	public void setNoMeansSeq(int noMeansSeq) {
		this.noMeansSeq = noMeansSeq;
	}

	
	
	
}
