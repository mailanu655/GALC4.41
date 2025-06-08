package com.honda.galc.service.msip.dto.inbound;

import java.sql.Timestamp;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Adc02390ADto implements IAdcDto {

	private static final long serialVersionUID = 1L;
	
	private String messageDate;
	private String messageTime;
	private String sendLocation;
	private String tranType;
	private String vin;
	private String frame;
	private String space;
	private String productId;
	private Timestamp actualTimestamp;
	
	public Adc02390ADto() {}

	public String getSendLocation() {
		return sendLocation;
	}

	public void setSendLocation(String sendLocation) {
		this.sendLocation = sendLocation;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getFrame() {
		return frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getProductId() {
		return productId;
	}

	public String getMessageDate() {
		return messageDate;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageDate == null) ? 0 : messageDate.hashCode());
		result = prime * result + ((messageTime == null) ? 0 : messageTime.hashCode());
		result = prime * result + ((sendLocation == null) ? 0 : sendLocation.hashCode());
		result = prime * result + ((tranType == null) ? 0 : tranType.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		result = prime * result + ((frame == null) ? 0 : frame.hashCode());
		result = prime * result + ((space == null) ? 0 : space.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
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
		Adc02390ADto other = (Adc02390ADto) obj;
		if (messageDate == null) {
			if (other.messageDate != null)
				return false;
		} else if (!messageDate.equals(other.messageDate))
			return false;
		if (messageTime == null) {
			if (other.messageTime != null)
				return false;
		} else if (!messageTime.equals(other.messageTime))
			return false;
		if (sendLocation == null) {
			if (other.sendLocation != null)
				return false;
		} else if (!sendLocation.equals(other.sendLocation))
			return false;
		if (tranType == null) {
			if (other.tranType != null)
				return false;
		} else if (!tranType.equals(other.tranType))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		if (frame == null) {
			if (other.frame != null)
				return false;
		} else if (!frame.equals(other.frame))
			return false;
		if (space == null) {
			if (other.space != null)
				return false;
		} else if (!space.equals(other.space))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;		
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}



