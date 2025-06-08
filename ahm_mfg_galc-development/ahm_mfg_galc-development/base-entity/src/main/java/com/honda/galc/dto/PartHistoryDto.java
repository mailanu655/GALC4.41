package com.honda.galc.dto;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.enumtype.InstalledPartStatus;

public class PartHistoryDto implements IDto {
private static final long serialVersionUID = 1L;
	
	private InstalledPartStatus status;
	private String productId;
	private String partName;
	private String partSn;
	private String measurements;
	private String processPointId;
    private String associateNo;
	private Timestamp timestamp;

	public InstalledPartStatus getStatus() {
		return this.status;
	}

	public void setStatus(InstalledPartStatus status) {
		this.status = status;
	}
	
	public String getProductId() {
		return StringUtils.trimToEmpty(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getPartName() {
		return StringUtils.trimToEmpty(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public String getPartSn() {
		return StringUtils.trimToEmpty(this.partSn);
	}

	public void setPartSn(String partSn) {
		this.partSn = partSn;
	}
	
	public String getMeasurements() {
		return StringUtils.trimToEmpty(this.measurements);
	}

	public void setMeasurements(String measurements) {
		this.measurements = measurements;
	}
	
	public String getProcessPointId() {
		return StringUtils.trimToEmpty(this.processPointId);
	}
	
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getAssociateNo() {
		return StringUtils.trimToEmpty(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}


	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}