package com.honda.galc.entity.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "PART_SHIPMENT_TBX")
public class PartShipment extends AuditEntry{

	private static final long serialVersionUID = 1L;

	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 @Id
	 @Column(name = "SHIPMENT_ID")
	 private Integer shipmentId;
	 
	 @Column(name = "RECEIVING_SITE")
	 private String receivingSite;
	 
	 @Column(name = "TRAILER_NUMBER")
	 private String trailerNumber;
	 
	 @Column(name = "ACTUAL_TIMESTAMP")
	 private Date actualTimestamp;
	 
	 @Column(name = "SENT_Timestamp")
	 private Date sentTimestamp;
	 
	 @Column(name = "SEND_STATUS")
	 private Integer sendStatus;

	 @Column(name = "ASSOCIATE_NO")
	 private String associateNo;
	 
	public PartShipment() {
		super();
	}
		
	public Integer getId() {
		return shipmentId;
	}


	public Integer getShipmentId() {
		return shipmentId;
	}


	public String getReceivingSite() {
		return StringUtils.trim(receivingSite);
	}


	public String getTrailerNumber() {
		return StringUtils.trim(trailerNumber);
	}


	public Date getActualTimestamp() {
		return actualTimestamp;
	}


	public Date getSentTimestamp() {
		return sentTimestamp;
	}


	public Integer getSendStatus() {
		return sendStatus;
	}


	public void setShipmentId(Integer shipmentId) {
		this.shipmentId = shipmentId;
	}


	public void setReceivingSite(String receivingSite) {
		this.receivingSite = receivingSite;
	}


	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}


	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}


	public void setSentTimestamp(Date sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}


	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

	
	public String getAssociateNo() {
		return StringUtils.trim(associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result + ((receivingSite == null) ? 0 : receivingSite.hashCode());
		result = prime * result + ((sendStatus == null) ? 0 : sendStatus.hashCode());
		result = prime * result + ((sentTimestamp == null) ? 0 : sentTimestamp.hashCode());
		result = prime * result + ((shipmentId == null) ? 0 : shipmentId.hashCode());
		result = prime * result + ((trailerNumber == null) ? 0 : trailerNumber.hashCode());
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
		PartShipment other = (PartShipment) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (receivingSite == null) {
			if (other.receivingSite != null)
				return false;
		} else if (!receivingSite.equals(other.receivingSite))
			return false;
		if (sendStatus == null) {
			if (other.sendStatus != null)
				return false;
		} else if (!sendStatus.equals(other.sendStatus))
			return false;
		if (sentTimestamp == null) {
			if (other.sentTimestamp != null)
				return false;
		} else if (!sentTimestamp.equals(other.sentTimestamp))
			return false;
		if (shipmentId == null) {
			if (other.shipmentId != null)
				return false;
		} else if (!shipmentId.equals(other.shipmentId))
			return false;
		if (trailerNumber == null) {
			if (other.trailerNumber != null)
				return false;
		} else if (!trailerNumber.equals(other.trailerNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PartShipment [shipmentId=" + shipmentId + ", receivingSite=" + receivingSite + ", trailerNumber="
				+ trailerNumber + ", actualTimestamp=" + actualTimestamp + ", sentTimestamp=" + sentTimestamp
				+ ", sendStatus=" + sendStatus + "]";
	}

	
}
