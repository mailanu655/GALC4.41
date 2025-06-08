package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
*/
public class Aep010Dto extends BaseOutboundDto implements IMsipOutboundDto{
	
	private static final long serialVersionUID = 1L;
	private String recordType;
	private String eventDate;
	private String eventTime;
	private String engineId;
	private String productId;
	private String frameModel;
	private String frameType;
	private String extColor;
	private String intColor;
	private String frameOption;
	private String processPointId;
	private String transmissionId;
	private String filler;
	private String errorMsg;
	private Boolean isError;

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

	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public String getVersion() {
		return version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recordType == null) ? 0 : recordType.hashCode());
		result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result + ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result + ((engineId == null) ? 0 : engineId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((frameModel == null) ? 0 : frameModel.hashCode());
		result = prime * result + ((frameType == null) ? 0 : frameType.hashCode());
		result = prime * result + ((extColor == null) ? 0 : extColor.hashCode());
		result = prime * result + ((intColor == null) ? 0 : intColor.hashCode());
		result = prime * result + ((frameOption == null) ? 0 : frameOption.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((transmissionId == null) ? 0 : transmissionId.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aep010Dto other = (Aep010Dto) obj;
		if (recordType == null) {
			if (other.recordType != null)
				return false;
		} else if (!recordType.equals(other.recordType))
			return false;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (eventTime == null) {
			if (other.eventTime != null)
				return false;
		} else if (!eventTime.equals(other.eventTime))
			return false;
		if (engineId == null) {
			if (other.engineId != null)
				return false;
		} else if (!engineId.equals(other.engineId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (frameModel == null) {
			if (other.frameModel != null)
				return false;
		} else if (!frameModel.equals(other.frameModel))
			return false;
		if (frameType == null) {
			if (other.frameType != null)
				return false;
		} else if (!frameType.equals(other.frameType))
			return false;
		if (extColor == null) {
			if (other.extColor != null)
				return false;
		} else if (!extColor.equals(other.extColor))
			return false;
		if (intColor == null) {
			if (other.intColor != null)
				return false;
		} else if (!intColor.equals(other.intColor))
			return false;
		if (frameOption == null) {
			if (other.frameOption != null)
				return false;
		} else if (!frameOption.equals(other.frameOption))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (transmissionId == null) {
			if (other.transmissionId != null)
				return false;
		} else if (!transmissionId.equals(other.transmissionId))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
