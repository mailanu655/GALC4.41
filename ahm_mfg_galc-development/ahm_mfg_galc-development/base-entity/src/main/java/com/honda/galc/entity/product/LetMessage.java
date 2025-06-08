package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Jan 21, 2016
 */
@Entity
@Table(name="LET_MESSAGE_TBX")
public class LetMessage extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 14585947548540L;
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name="MESSAGE_ID", nullable=false)
	private long messageId;
	
	@Column(name="TERMINAL_ID", nullable=false)
	private String terminalId;
	
	@Column(name="IP_ADDRESS")
	private String ipAddress;
	
	@Column(name="PRODUCT_ID")
	private String productId;
	
	@Column(name="BUILD_CODE")
	private String buildCode;
	
	@Column(name="MAC_ADDRESS")
	private String macAddress;
	
	@Column(name="TOTAL_STATUS")
	private String totalStatus;

	@Column(name="MESSAGE_HEADER")
	private String messageHeader;
	
	@Lob
	@Column(name="XML_MESSAGE_BODY")
	private String xmlMessageBody;
	
	@Lob
	@Column(name="EXCEPTION_MESSAGE_BODY")
	private String exceptionMessageBody;
	
	@Column(name="MESSAGE_REPLY")
	private String messageReply;
	
	@Column(name = "ACTUAL_TIMESTAMP", nullable=false)
	private Timestamp actualTimestamp;
	
	@Column(name="SPOOL_ID", nullable=false)
	private Integer spoolId;
	
	@Column(name="MESSAGE_TYPE", nullable=false)
	private String messageType;
	
	@Column(name="DURATION")
	private double duration;
	
	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public String getTerminalId() {
		return StringUtils.trim(terminalId);
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getIpAddress() {
		return StringUtils.trim(ipAddress);
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getBuildCode() {
		return StringUtils.trim(buildCode);
	}

	public void setBuildCode(String buildCode) {
		this.buildCode = buildCode;
	}

	public String getMacAddress() {
		return StringUtils.trim(macAddress);
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getTotalStatus() {
		return StringUtils.trim(totalStatus);
	}

	public void setTotalStatus(String totalStatus) {
		this.totalStatus = totalStatus;
	}

	public String getMessageHeader() {
		return StringUtils.trim(messageHeader);
	}

	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}

	public String getXmlMessageBody() {
		return StringUtils.trim(xmlMessageBody);
	}

	public void setXmlMessageBody(String xmlMessageBody) {
		this.xmlMessageBody = xmlMessageBody;
	}

	public String getExceptionMessageBody() {
		return StringUtils.trim(exceptionMessageBody);
	}

	public void setExceptionMessageBody(String exceptionMessageBody) {
		this.exceptionMessageBody = exceptionMessageBody;
	}

	public String getMessageReply() {
		return StringUtils.trim(messageReply);
	}

	public void setMessageReply(String messageReply) {
		this.messageReply = messageReply;
	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public boolean getIsValid() {
		return (xmlMessageBody == null);
	}

	public Object getId() {
		return getMessageId();
	}
	
	public Integer getSpoolId() {
		return spoolId;
	}

	public void setSpoolId(Integer spoolId) {
		this.spoolId = spoolId;
	}

	
	public String getMessageType() {
		return StringUtils.trim(messageType);
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result
				+ ((buildCode == null) ? 0 : buildCode.hashCode());
		result = prime
				* result
				+ ((exceptionMessageBody == null) ? 0 : exceptionMessageBody
						.hashCode());
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result
				+ ((macAddress == null) ? 0 : macAddress.hashCode());
		result = prime * result
				+ ((messageHeader == null) ? 0 : messageHeader.hashCode());
		result = prime * result + (int) (messageId ^ (messageId >>> 32));
		result = prime * result
				+ ((messageReply == null) ? 0 : messageReply.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((terminalId == null) ? 0 : terminalId.hashCode());
		result = prime * result
				+ ((totalStatus == null) ? 0 : totalStatus.hashCode());
		result = prime * result
				+ ((xmlMessageBody == null) ? 0 : xmlMessageBody.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetMessage other = (LetMessage) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (buildCode == null) {
			if (other.buildCode != null)
				return false;
		} else if (!buildCode.equals(other.buildCode))
			return false;
		if (exceptionMessageBody == null) {
			if (other.exceptionMessageBody != null)
				return false;
		} else if (!exceptionMessageBody.equals(other.exceptionMessageBody))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		if (messageHeader == null) {
			if (other.messageHeader != null)
				return false;
		} else if (!messageHeader.equals(other.messageHeader))
			return false;
		if (messageId != other.messageId)
			return false;
		if (messageReply == null) {
			if (other.messageReply != null)
				return false;
		} else if (!messageReply.equals(other.messageReply))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (terminalId == null) {
			if (other.terminalId != null)
				return false;
		} else if (!terminalId.equals(other.terminalId))
			return false;
		if (totalStatus == null) {
			if (other.totalStatus != null)
				return false;
		} else if (!totalStatus.equals(other.totalStatus))
			return false;
		if (xmlMessageBody == null) {
			if (other.xmlMessageBody != null)
				return false;
		} else if (!xmlMessageBody.equals(other.xmlMessageBody))
			return false;
		return true;
	}

	public String toString() {
		return toString(getMessageId(), getTerminalId(), getIsValid(), 
				getActualTimestamp(), getMessageHeader(), getIpAddress(),getProductId(),getBuildCode(),getMacAddress(),getMessageReply(),getDuration(),getMessageType(),getSpoolId(),getTerminalId(),getTotalStatus());
	}
}