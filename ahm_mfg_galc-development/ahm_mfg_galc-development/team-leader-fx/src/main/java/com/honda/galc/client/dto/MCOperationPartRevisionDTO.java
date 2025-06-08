package com.honda.galc.client.dto;

public class MCOperationPartRevisionDTO implements Cloneable {

	private String operationName;
	private String partRev;
	private String revId;
	private String view;
	private String partId;
	private String processor;
	private String partNo;
	private String sectionCode;
	private String description;
	private String partMask;
	private String partCheck;
	private String deviceMsg;
	private String partItemNo;
	private String partType;
	private String partMaskTextboxVal;
	private boolean partMaskCheckbox;

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public String getPartItemNo() {
		return partItemNo;
	}

	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}

	public String getDeviceMsg() {
		return deviceMsg;
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
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

	public String getRevId() {
		return revId;
	}

	public void setRevId(String revId) {
		this.revId = revId;
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

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPartMask() {
		return partMask;
	}

	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	public String getPartCheck() {
		return partCheck;
	}

	public void setPartCheck(String partCheck) {
		this.partCheck = partCheck;
	}

	public String getPartMaskTextboxVal() {
		return partMaskTextboxVal;
	}

	public void setPartMaskTextboxVal(String partMaskTextboxVal) {
		this.partMaskTextboxVal = partMaskTextboxVal;
	}

	public boolean getPartMaskCheckbox() {
		return partMaskCheckbox;
	}

	public void setPartMaskCheckbox(boolean partMaskCheckbox) {
		this.partMaskCheckbox = partMaskCheckbox;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}

}
