package com.honda.galc.dto;

public class MCRevisionDto  implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "REV_ID")
	private long revId;
	
	@DtoTag(outputName = "ASSOCIATE_NO")
	private String associateNo;
	
	@DtoTag(outputName = "REV_DESC")
	private String revDesc;
	
	@DtoTag(outputName = "REV_STATUS")
	private String revStatus;
	
	@DtoTag(outputName = "REV_TYPE")
	private String revType;
	
	@DtoTag(outputName = "CONTROL_NO")
	private int controlNo;
	
	public long getRevId() {
		return revId;
	}
	public void setRevId(long revId) {
		this.revId = revId;
	}
	public String getAssociateNo() {
		return associateNo;
	}
	
	public int getControlNo() {
		return controlNo;
	}
	public void setControlNo(int controlNo) {
		this.controlNo = controlNo;
	}
	
	
	public String getRevDesc() {
		return revDesc;
	}
	public void setRevDesc(String revDesc) {
		this.revDesc = revDesc;
	}
	public String getRevStatus() {
		return revStatus;
	}
	public void setRevStatus(String revStatus) {
		this.revStatus = revStatus;
	}
	public String getRevType() {
		return revType;
	}
	public void setRevType(String revType) {
		this.revType = revType;
	}
	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}
	

}
