package com.honda.galc.vios.dto;

import com.honda.galc.enumtype.StructureCompareStatus;

public class StructureCompareDto {

	private String productSpecCode;

	private long revision;

	private String processPointId;

	private String operationName;

	private int operationRevision;

	private int pddaPlatformId;

	private String partId;

	private int partRevision;
	
	private String divisionId;
	
	private StructureCompareStatus processPtCompareSts;
	
	private StructureCompareStatus opNameCompareSts;
	
	private StructureCompareStatus opRevCompareSts;
	
	private StructureCompareStatus partIdCompareSts;
	
	private StructureCompareStatus partRevCompareSts;
	
	private StructureCompareStatus divIdCompareSts;

	public StructureCompareDto(String productSpecCode, long revision, String processPointId, String operationName, int operationRevision, String partId, int partRevision, String divisionId) {
		this.setProductSpecCode(productSpecCode);
		this.setRevision(revision);
		this.setProcessPointId(processPointId);
		this.setOperationName(operationName);
		this.setOperationRevision(operationRevision);
		this.setPartId(partId);
		this.setPartRevision(partRevision);
		this.setDivisionId(divisionId);
	}
	
	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
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

	public int getPddaPlatformId() {
		return pddaPlatformId;
	}

	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
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

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	public StructureCompareStatus getProcessPtCompareSts() {
		return processPtCompareSts;
	}

	public void setProcessPtCompareSts(StructureCompareStatus processPtCompareSts) {
		this.processPtCompareSts = processPtCompareSts;
	}

	public StructureCompareStatus getOpNameCompareSts() {
		return opNameCompareSts;
	}

	public void setOpNameCompareSts(StructureCompareStatus opNameCompareSts) {
		this.opNameCompareSts = opNameCompareSts;
	}

	public StructureCompareStatus getOpRevCompareSts() {
		return opRevCompareSts;
	}

	public void setOpRevCompareSts(StructureCompareStatus opRevCompareSts) {
		this.opRevCompareSts = opRevCompareSts;
	}

	public StructureCompareStatus getPartIdCompareSts() {
		return partIdCompareSts;
	}

	public void setPartIdCompareSts(StructureCompareStatus partIdCompareSts) {
		this.partIdCompareSts = partIdCompareSts;
	}

	public StructureCompareStatus getPartRevCompareSts() {
		return partRevCompareSts;
	}

	public void setPartRevCompareSts(StructureCompareStatus partRevCompareSts) {
		this.partRevCompareSts = partRevCompareSts;
	}

	public StructureCompareStatus getDivIdCompareSts() {
		return divIdCompareSts;
	}

	public void setDivIdCompareSts(StructureCompareStatus divIdCompareSts) {
		this.divIdCompareSts = divIdCompareSts;
	}
	
}
