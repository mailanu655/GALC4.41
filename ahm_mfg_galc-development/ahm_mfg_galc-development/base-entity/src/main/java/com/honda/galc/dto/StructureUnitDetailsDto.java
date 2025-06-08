package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;


public class StructureUnitDetailsDto  implements IDto{
	private static final long serialVersionUID = 1L;
	
	@DtoTag(name = "STRUCTURE_REV")
	private long revision;

	@DtoTag(outputName = "PDDA_PLATFORM_ID")
	private int pddaPlatformId;
	
	@DtoTag(outputName = "DIVISION_ID")
	private String divisionId;
	
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(outputName = "OPERATION_NAME")
	private String operationName;
	
	@DtoTag(name = "OP_REV")
	private int operationRevision;
	
	@DtoTag(outputName = "PART_ID")
	private String partId;
	
	@DtoTag(name = "PART_REV")
	private int partRevision;
	
	@DtoTag(outputName = "ASM_PROC_NO")
	private String asmProcNo;
	
	@DtoTag(outputName = "PROCESS_SEQ_NUM")
	private int processSeqNum;
	
	@DtoTag(name = "OPERATION_DESC")
	private String description;
	
	@DtoTag(name = "OPERATION_TYPE")
	private String type;
	
	@DtoTag(outputName = "OPERATION_SEQ_NUM")
	private int operationSeqNum;
	
	@DtoTag(outputName = "PART_TYPE")
	private String partType;
	
	@DtoTag(outputName = "PART_NO")
	private String partNo;
	
	@DtoTag(outputName = "PART_ITEM_NO")
	private String partItemNo;
	
	@DtoTag(outputName = "PART_SECTION_CODE")
	private String partSectionCode;
	
	@DtoTag(outputName = "PART_MASK")
	private String partMask;
	
	@DtoTag(outputName = "PART_DESC")
	private String partDesc;
	
	@DtoTag(outputName = "MIN_LIMIT")
	private double minLimit;
	
	@DtoTag(outputName = "MAX_LIMIT")
	private double maxLimit;
	
	@DtoTag(outputName = "DEVICE_ID")
	private String deviceId;
	
	@DtoTag(outputName = "DEVICE_MSG")
	private String deviceMsg;
	
	@DtoTag(name = "OPERATION_MEAS_SEQ_NUM")
	private int measurementSeqNum;
	
	@DtoTag(outputName = "PROCESS_POINT_NAME")
	private String processPointName;
	
	@DtoTag(outputName = "PROCESS_POINT_DESCRIPTION")
	private String processPointDescription;

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public int getPddaPlatformId() {
		return pddaPlatformId;
	}

	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}

	public String getDivisionId() {
		return StringUtils.trimToEmpty(this.divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getOperationName() {
		return StringUtils.trimToEmpty(this.operationName);
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public int getOperationRevision() {
		return operationRevision;
	}

	public void setOperationRevision(Integer operationRevision) {
		this.operationRevision = operationRevision;
	}

	public String getPartId() {
		return StringUtils.trimToEmpty(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public int getPartRevision() {
		return partRevision;
	}

	public void setPartRevision(int partRevision) {
		this.partRevision = partRevision;
	}

	public String getAsmProcNo() {
		return StringUtils.trimToEmpty(this.asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public int getProcessSeqNum() {
		return processSeqNum;
	}

	public void setProcessSeqNum(int processSeqNum) {
		this.processSeqNum = processSeqNum;
	}

	public String getDescription() {
		return StringUtils.trimToEmpty(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return StringUtils.trimToEmpty(this.type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOperationSeqNum() {
		return operationSeqNum;
	}

	public void setOperationSeqNum(int operationSeqNum) {
		this.operationSeqNum = operationSeqNum;
	}

	public String getPartType() {
		return StringUtils.trimToEmpty(this.partType);
	}

	public void setPartType(String partType) {
		this.partType = partType;
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

	public String getPartMask() {
		return StringUtils.trimToEmpty(this.partMask);
	}

	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	public String getPartDesc() {
		return StringUtils.trimToEmpty(this.partDesc);
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

	public double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(double minLimit) {
		this.minLimit = minLimit;
	}

	public double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(double maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getDeviceId() {
		return StringUtils.trimToEmpty(this.deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMsg() {
		return StringUtils.trimToEmpty(this.deviceMsg);
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public int getMeasurementSeqNum() {
		return measurementSeqNum;
	}

	public void setMeasurementSeqNum(Integer measurementSeqNum) {
		this.measurementSeqNum = measurementSeqNum;
	}

	public String getProcessPointName() {
		return StringUtils.trimToEmpty(this.processPointName);
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public String getProcessPointDescription() {
		return StringUtils.trimToEmpty(this.processPointDescription);
	}

	public void setProcessPointDescription(String processPointDescription) {
		this.processPointDescription = processPointDescription;
	}
}	
