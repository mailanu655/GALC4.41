package com.honda.galc.entity.oif;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "AEP_STAT_OUT_TBX")
public class FrameShipConfirmation extends AuditEntry{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId	
	private FrameShipConfirmationId id;
	
	@Column(name = "EVENT_DATE")
	private String eventDate;
	
	@Column(name = "EVENT_TIME")
	private String eventTime;
	
	@Column(name = "FRAME_MODEL")
	private String frameModel;
	
	@Column(name = "FRAME_TYPE")
	private String frameType;
	
	@Column(name = "EXT_COLOR")
	private String extColor;
	
	@Column(name = "INT_COLOR")
	private String intColor;
	
	@Column(name = "FRAME_OPTION")
	private String frameOption;
	
	@Column(name = "SENT_FLAG")
	private String sentFlag;
	
	@Column(name = "RECORD_TYPE")
	private String recordType;
	
	
	public FrameShipConfirmationId getId() {
		return id;
	}


	/**
	 * @return the eventDate
	 */
	public String getEventDate() {
		return StringUtils.trim( eventDate );
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
		return StringUtils.trim( eventTime );
	}


	/**
	 * @param eventTime the eventTime to set
	 */
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}


	/**
	 * @return the frameModel
	 */
	public String getFrameModel() {
		return StringUtils.trim( frameModel );
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
		return StringUtils.trim( frameType );
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
		return StringUtils.trim( extColor );
	}


	/**
	 * @param extColor the extColor to set
	 */
	public void setExtColor(String extColor) {
		this.extColor = extColor;
	}

	
	/**
	 * @return the frameOption
	 */
	public String getFrameOption() {
		return StringUtils.trim( frameOption );
	}


	/**
	 * @param frameOption the frameOption to set
	 */
	public void setFrameOption(String frameOption) {
		this.frameOption = frameOption;
	}


	/**
	 * @return the sentFlag
	 */
	public String getSentFlag() {
		return StringUtils.trim( sentFlag );
	}


	/**
	 * @param sentFlag the sentFlag to set
	 */
	public void setSentFlag(String sentFlag) {
		this.sentFlag = sentFlag;
	}


	/**
	 * @return the recordType
	 */
	public String getRecordType() {
		return StringUtils.trim( recordType );
	}


	/**
	 * @param recordType the recordType to set
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(FrameShipConfirmationId id) {
		this.id = id;
	}


	/**
	 * @return the intColor
	 */
	public String getIntColor() {
		return StringUtils.trim( intColor );
	}


	/**
	 * @param intColor the intColor to set
	 */
	public void setIntColor(String intColor) {
		this.intColor = intColor;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result
				+ ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result
				+ ((extColor == null) ? 0 : extColor.hashCode());
		result = prime * result
				+ ((frameModel == null) ? 0 : frameModel.hashCode());
		result = prime * result
				+ ((frameOption == null) ? 0 : frameOption.hashCode());
		result = prime * result
				+ ((frameType == null) ? 0 : frameType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((intColor == null) ? 0 : intColor.hashCode());
		result = prime * result
				+ ((recordType == null) ? 0 : recordType.hashCode());
		result = prime * result
				+ ((sentFlag == null) ? 0 : sentFlag.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FrameShipConfirmation other = (FrameShipConfirmation) obj;
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
		if (extColor == null) {
			if (other.extColor != null)
				return false;
		} else if (!extColor.equals(other.extColor))
			return false;
		if (frameModel == null) {
			if (other.frameModel != null)
				return false;
		} else if (!frameModel.equals(other.frameModel))
			return false;
		if (frameOption == null) {
			if (other.frameOption != null)
				return false;
		} else if (!frameOption.equals(other.frameOption))
			return false;
		if (frameType == null) {
			if (other.frameType != null)
				return false;
		} else if (!frameType.equals(other.frameType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (intColor == null) {
			if (other.intColor != null)
				return false;
		} else if (!intColor.equals(other.intColor))
			return false;
		if (recordType == null) {
			if (other.recordType != null)
				return false;
		} else if (!recordType.equals(other.recordType))
			return false;
		if (sentFlag == null) {
			if (other.sentFlag != null)
				return false;
		} else if (!sentFlag.equals(other.sentFlag))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see com.honda.galc.entity.AbstractEntity#toString(java.lang.Object[])
	 */
	@Override
	protected String toString(Object... objects)
	{
		return super.toString( id.getEngineId(),id.getProductId(), id.getProcessPointId(), eventDate, eventTime, recordType );
	}
	
	
}
