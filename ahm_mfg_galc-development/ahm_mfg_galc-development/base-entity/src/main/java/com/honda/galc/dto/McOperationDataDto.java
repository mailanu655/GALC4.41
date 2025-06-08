package com.honda.galc.dto;


public class McOperationDataDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	@DtoTag(name="OPERATION_NAME")
	private String operationName;
	
	@DtoTag(name="PART_ID")
	private String partId;

	@DtoTag(name="PART_REV") 
	private int partRevision;
	
	@DtoTag(name="OP_REV")
	private int operationRevision;
	
	@DtoTag(name="OP_TYPE")
	private String operationType;
	
	@DtoTag(name="PDDA_PLATFORM_ID")
	private int pddaPlatformId;
	
	@DtoTag(name="SPEC_CODE_TYPE")
	private String specCodeType;

	@DtoTag(name="SPEC_CODE_MASK")
	private String specCodeMask;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getPartId() {
		return partId;
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

	public int getOperationRevision() {
		return operationRevision;
	}

	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public int getPddaPlatformId() {
		return pddaPlatformId;
	}

	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}

	public String getSpecCodeType() {
		return specCodeType;
	}

	public void setSpecCodeType(String specCodeType) {
		this.specCodeType = specCodeType;
	}

	public String getSpecCodeMask() {
		return specCodeMask;
	}

	public void setSpecCodeMask(String specCodeMask) {
		this.specCodeMask = specCodeMask;
	}
	
	
}
