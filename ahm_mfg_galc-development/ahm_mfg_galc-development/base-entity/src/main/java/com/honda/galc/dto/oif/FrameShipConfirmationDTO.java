package com.honda.galc.dto.oif;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;


public class FrameShipConfirmationDTO implements IDto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DtoTag(outputName = "ENGINE_MOUNT_RECORD")
	private String recordType;
	
	@DtoTag(outputName = "EVENT_DATE")
	private String eventDate;
	
	@DtoTag(outputName = "EVENT_TIME")
	private String eventTime;
			
	@DtoTag(outputName = "ENGINE_SERIAL_NUMBER")
	private String engineId;
	
	@DtoTag(outputName = "FRAME_SERIAL_NUMBER")
	private String productId;

	@DtoTag(outputName = "FRAME_MODEL")
	private String frameModel;
	
	@DtoTag(outputName = "FRAME_TYPE")
	private String frameType;
	
	@DtoTag(outputName = "FRAME_COLOR")
	private String extColor;
	
	@DtoTag(outputName = "FRAME_INTERIOR_COLOR")
	private String intColor;
	
	@DtoTag(outputName = "FRAME_OPTION")
	private String frameOption;
	
	@DtoTag()//,outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(outputName = "TRANSMISSION_NUMBER")
	private String transmissionId;
	
	@DtoTag(outputName = "FILLER")
	private String filler;

	/**
	 * @return the recordType
	 */
	public String getRecordType() {
		return recordType;
	}

	/**
	 * @param recordType the recordType to set
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	/**
	 * @return the eventDate
	 */
	public String getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the eventTime
	 */
	public String getEventTime() {
		return eventTime;
	}

	/**
	 * @param eventTime the eventTime to set
	 */
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	/**
	 * @return the engineId
	 */
	public String getEngineId() {
		return engineId;
	}

	/**
	 * @param engineId the engineId to set
	 */
	public void setEngineId(String engineId) {
		this.engineId = engineId;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the frameModel
	 */
	public String getFrameModel() {
		return frameModel;
	}

	/**
	 * @param frameModel the frameModel to set
	 */
	public void setFrameModel(String frameModel) {
		this.frameModel = frameModel;
	}

	/**
	 * @return the frameType
	 */
	public String getFrameType() {
		return frameType;
	}

	/**
	 * @param frameType the frameType to set
	 */
	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}

	/**
	 * @return the extColor
	 */
	public String getExtColor() {
		return extColor;
	}

	/**
	 * @param extColor the extColor to set
	 */
	public void setExtColor(String extColor) {
		this.extColor = extColor;
	}

	/**
	 * @return the intColor
	 */
	public String getIntColor() {
		return intColor;
	}

	/**
	 * @param intColor the intColor to set
	 */
	public void setIntColor(String intColor) {
		this.intColor = intColor;
	}

	/**
	 * @return the frameOption
	 */
	public String getFrameOption() {
		return frameOption;
	}

	/**
	 * @param frameOption the frameOption to set
	 */
	public void setFrameOption(String frameOption) {
		this.frameOption = frameOption;
	}

	/**
	 * @return the processPointId
	 */
	public String getProcessPointId() {
		return processPointId;
	}

	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	/**
	 * @return the transmissionId
	 */
	public String getTransmissionId() {
		return transmissionId;
	}

	/**
	 * @param transmissionId the transmissionId to set
	 */
	public void setTransmissionId(String transmissionId) {
		this.transmissionId = transmissionId;
	}

	/**
	 * @return the filler
	 */
	public String getFiller() {
		return filler;
	}

	/**
	 * @param filler the filler to set
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}
		
}
