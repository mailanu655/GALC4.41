package com.honda.galc.client.teamleader.model;



public class PartIdSelection {
	private static final long serialVersionUID = 1L;
	
	private String partId;
	private String description;
	private String partMask;
	private String partMark;
	private String partNumber;
	private boolean apply;
	
	
	public String getPartId() {
		return partId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getPartMask() {
		return partMask;
	}
	
	public String getPartMark() {
		return partMark;
	}
	
	public String getPartNumber() {
		return partNumber;
	}
	
	public boolean isApply() {
		return apply;
	}
	
	public void setPartId(String partId) {
		this.partId = partId;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}
	
	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}
	
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public void setApply(boolean apply) {
		this.apply = apply;
	}
}
