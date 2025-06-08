package com.honda.galc.dto;


public class MfgCtrlMadeFrom implements IDto {

	/**
	 * This bean class created for MC_MADE_FROM_SERVICE
	 */
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName ="ORDER_NO")
	private String orderNo;
	
	@DtoTag(outputName ="PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@DtoTag(outputName ="STRUCTURE_REV")
	private long structureRevision;
	
	@DtoTag(outputName ="PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(outputName ="OPERATION_NAME")
	private String operationName;
	
	@DtoTag(outputName ="OP_REV")
	private int operationRevision;
	
	@DtoTag(outputName ="OP_TYPE")
	private String operationType;
	
	@DtoTag(outputName ="OP_SEQ_NUM")
	private int operationSequenceNum;
	
	@DtoTag(outputName ="PART_ID")
	private String partId;
	
	@DtoTag(outputName ="PART_REV")
	private int partRevision;
	
	@DtoTag(outputName ="PART_NO")
	private String partNo;
	
	@DtoTag(outputName ="PART_SECTION_CODE")
	private String partSectionCode;
	
	@DtoTag(outputName ="PART_ITEM_NO")
	private String partItemNo;
	
	@DtoTag(outputName ="PART_MASK")
	private String partMask;
	
	@DtoTag(outputName ="PART_MARK")
	private String partMark;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public long getStructureRevision() {
		return structureRevision;
	}

	public void setStructureRevision(long structureRevision) {
		this.structureRevision = structureRevision;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
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

	public int getOperationSequenceNum() {
		return operationSequenceNum;
	}

	public void setOperationSequenceNum(int operationSequenceNum) {
		this.operationSequenceNum = operationSequenceNum;
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

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPartSectionCode() {
		return partSectionCode;
	}

	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}

	public String getPartItemNo() {
		return partItemNo;
	}

	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}

	public String getPartMask() {
		return partMask;
	}

	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	public String getPartMark() {
		return partMark;
	}

	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}
	
}
