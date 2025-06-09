package com.honda.ahm.lc.model;

public class FrameShipConfirmation extends AuditEntry{

	private static final long serialVersionUID = 1L;

	
	private FrameShipConfirmationId id;
	
	private String eventDate;
	
	private String eventTime;
	
	
	private String frameModel;
	
	
	private String frameType;
	
	
	private String extColor;
	
	
	private String intColor;
	
	
	private String frameOption;
	
	
	private String sentFlag;
	
	
	private String recordType;


	public FrameShipConfirmationId getId() {
		return id;
	}


	public void setId(FrameShipConfirmationId id) {
		this.id = id;
	}


	public String getEventDate() {
		return eventDate;
	}


	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}


	public String getEventTime() {
		return eventTime;
	}


	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}


	public String getFrameModel() {
		return frameModel;
	}


	public void setFrameModel(String frameModel) {
		this.frameModel = frameModel;
	}


	public String getFrameType() {
		return frameType;
	}


	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}


	public String getExtColor() {
		return extColor;
	}


	public void setExtColor(String extColor) {
		this.extColor = extColor;
	}


	public String getIntColor() {
		return intColor;
	}


	public void setIntColor(String intColor) {
		this.intColor = intColor;
	}


	public String getFrameOption() {
		return frameOption;
	}


	public void setFrameOption(String frameOption) {
		this.frameOption = frameOption;
	}


	public String getSentFlag() {
		return sentFlag;
	}


	public void setSentFlag(String sentFlag) {
		this.sentFlag = sentFlag;
	}


	public String getRecordType() {
		return recordType;
	}


	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	
}
