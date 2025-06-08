package com.honda.galc.dto;

import java.util.Date;

import org.apache.commons.lang.StringUtils;


public class PartDetailsDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag(name = "PART_ID")
	private String partId;
	
	@DtoTag(name = "PART_NO")
	private String partNo;
	
	@DtoTag(name="PART_ITEM_NO")
	private String partItemNo;
	
	@DtoTag(name="PART_SECTION_CODE")
	private String partSectionCode;
	
	@DtoTag(name = "PART_TYPE")
	private String partType;
	
	@DtoTag(name="MAX_LIMIT")
	private double maxLimit;
	
	@DtoTag(name="MIN_LIMIT")
	private double minLimit;
	
	@DtoTag(name="OP_MEAS_SEQ_NUM")
	private int measurementSeqNum;
	
	@DtoTag(name="REV_ID")
	private long revisionId;
	
	@DtoTag(name="OP_REV")
	private int operationRevision;

	@DtoTag(name="APPROVED")
	private Date approved;
	
	@DtoTag(name="DEPRECATED")
	private Date deprecated;
	
	private String status;
	
	public String getPartId() {
		return StringUtils.trimToEmpty(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getPartNo() {
		return StringUtils.trimToEmpty(this.partNo);
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPartItemNo() {
		return StringUtils.trimToEmpty(this.partItemNo);
	}

	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}

	public String getPartSectionCode() {
		return StringUtils.trimToEmpty(this.partSectionCode);
	}

	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}

	public String getPartType() {
		return StringUtils.trimToEmpty(this.partType);
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(double maxLimit) {
		this.maxLimit = maxLimit;
	}

	public double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(double minLimit) {
		this.minLimit = minLimit;
	}

	public int getMeasurementSeqNum() {
		return measurementSeqNum;
	}

	public void setMeasurementSeqNum(int measurementSeqNum) {
		this.measurementSeqNum = measurementSeqNum;
	}

	public Date getApproved() {
		return approved;
	}

	public void setApproved(Date approved) {
		this.approved = approved;
	}

	public Date getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Date deprecated) {
		this.deprecated = deprecated;
	}
	
	public long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}

	public int getOperationRevision() {
		return operationRevision;
	}

	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}

	public String getStatus() {
		if(approved != null && deprecated == null)
			status = "ACTIVE";
		else
			status = "INACTIVE";
		return status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + measurementSeqNum;
		result = prime * result
				+ ((partItemNo == null) ? 0 : partItemNo.hashCode());
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		result = prime * result
				+ ((partSectionCode == null) ? 0 : partSectionCode.hashCode());
		result = prime * result
				+ ((partType == null) ? 0 : partType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartDetailsDto other = (PartDetailsDto) obj;
		if (measurementSeqNum != other.measurementSeqNum)
			return false;
		if (partItemNo == null) {
			if (other.partItemNo != null)
				return false;
		} else if (!partItemNo.equals(other.partItemNo))
			return false;
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		if (partSectionCode == null) {
			if (other.partSectionCode != null)
				return false;
		} else if (!partSectionCode.equals(other.partSectionCode))
			return false;
		if (partType == null) {
			if (other.partType != null)
				return false;
		} else if (!partType.equals(other.partType))
			return false;
		return true;
	}
	
}
