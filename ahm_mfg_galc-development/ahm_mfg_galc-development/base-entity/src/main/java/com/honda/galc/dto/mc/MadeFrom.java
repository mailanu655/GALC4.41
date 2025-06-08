package com.honda.galc.dto.mc;

import java.io.Serializable;

/**
 * @author Subu Kathiresan
 * Apr 17, 2014
 */
public class MadeFrom implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String productId;
	
	private String productSpecCode;
	
	private long structureRevision;
	
	private String processPointId;
	
	private String operationName;
	
	private int operationRevision;
	
	private String operationType;
	
	private int operationSequenceNum;
	
	private String partId;
	
	private int partRevision;
	
	private String partNo;
	
	private String partSectionCode;
	
	private String partItemNo;
	
	private String partMask;
	
	private String partMark;

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
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
