package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

/**
 * @author vfc01862
 *
 */
public class CopyCheckerDetailsDto implements IDto {

private static final long serialVersionUID = 1L;
	
	@DtoTag(name="OPERATION_NAME")
	private String operationName;
	
	@DtoTag(name="OP_REV")
	private int operationRevision;
	
	@DtoTag(name="PART_ID")
	private String partId;
	
	@DtoTag(name="PART_NO")
	private String partNo;
	
	@DtoTag(name="PART_ITEM_NO")
	private String partItemNo;
	
	@DtoTag(name="PART_SECTION_CODE")
	private String partSectionCode;
	
	@DtoTag(name="PART_TYPE")
    private String partType;
	
	@DtoTag(name="MAX_LIMIT")
	private double maxLimit;
	
	@DtoTag(name="MIN_LIMIT")
	private double minLimit;
	
	@DtoTag(name="OP_MEAS_SEQ_NUM")
	private int measurementSeqNum;
	
	public String getOperationName() {
		return StringUtils.trimToEmpty(this.operationName);
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getPartId() {
		return StringUtils.trimToEmpty(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public int getOperationRevision() {
		return operationRevision;
	}

	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
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
}
