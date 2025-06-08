package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.VinBomApprovalStatus;


/**
 * The persistent class for the VIN_PART_APPROVAL database table.
 * 
 */
@Entity
@Table(name="VIN_PART_APPROVAL", schema="LCVINBOM")
public class VinPartApproval extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="VIN_PART_APPROVAL_ID", unique=true, nullable=false)
	private Long vinPartApprovalId;

	@Column(name="APPROVE_ASSOCIATE_NUMBER", length=11)
	private String approveAssociateNumber;

	@Column(name="APPROVE_STATUS", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VinBomApprovalStatus approveStatus;

	@Column(name="APPROVE_TIMESTAMP")
	private Timestamp approveTimestamp;

	@Column(name="CURRENT_DC_PART_NUMBER", nullable=false, length=18)
	private String currentDcPartNumber;

	@Column(name="CURRENT_SHIP_STATUS", nullable=false, columnDefinition="INT(1)")
	private boolean currentShipStatus;

	@Column(name="LET_SYSTEM_NAME", nullable=false, length=255)
	private String letSystemName;

	@Column(name="NEW_DC_PART_NUMBER", nullable=false, length=18)
	private String newDcPartNumber;

	@Column(name="NEW_SHIP_STATUS", nullable=false, columnDefinition="INT(1)")
	private boolean newShipStatus;

	@Column(name="PRODUCT_ID", nullable=false, length=255)
	private String productId;

	@Column(name="REQUEST_ASSOCIATE_NUMBER", nullable=false, length=11)
	private String requestAssociateNumber;

	@Column(name="REQUEST_TIMESTAMP", nullable=false)
	private Timestamp requestTimestamp;
	
	@Column(name="INTERCHANGEABLE", nullable=false, columnDefinition="INT(1)")
	private boolean interchangeable;
	
	@Transient 
	private String requestAssociateName;
	
	@Transient 
	private String productionLot;

	@Transient 
	private String productSpecCode;

	public VinPartApproval() {
	}

	public Long getVinPartApprovalId() {
		return this.vinPartApprovalId;
	}

	public void setVinPartApprovalId(Long vinPartApprovalId) {
		this.vinPartApprovalId = vinPartApprovalId;
	}

	public String getApproveAssociateNumber() {
		return StringUtils.trim(this.approveAssociateNumber);
	}

	public void setApproveAssociateNumber(String approveAssociateNumber) {
		this.approveAssociateNumber = approveAssociateNumber;
	}

	public VinBomApprovalStatus getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(VinBomApprovalStatus approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Timestamp getApproveTimestamp() {
		return this.approveTimestamp;
	}

	public void setApproveTimestamp(Timestamp approveTimestamp) {
		this.approveTimestamp = approveTimestamp;
	}

	public String getCurrentDcPartNumber() {
		return StringUtils.trim(this.currentDcPartNumber);
	}

	public void setCurrentDcPartNumber(String currentDcPartNumber) {
		this.currentDcPartNumber = currentDcPartNumber;
	}

	public boolean getCurrentShipStatus() {
		return this.currentShipStatus;
	}

	public void setCurrentShipStatus(boolean currentShipStatus) {
		this.currentShipStatus = currentShipStatus;
	}

	public String getLetSystemName() {
		return StringUtils.trim(this.letSystemName);
	}

	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}

	public String getNewDcPartNumber() {
		return StringUtils.trim(this.newDcPartNumber);
	}

	public void setNewDcPartNumber(String newDcPartNumber) {
		this.newDcPartNumber = newDcPartNumber;
	}

	public boolean getNewShipStatus() {
		return this.newShipStatus;
	}

	public void setNewShipStatus(boolean newShipStatus) {
		this.newShipStatus = newShipStatus;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRequestAssociateNumber() {
		return StringUtils.trim(this.requestAssociateNumber);
	}

	public void setRequestAssociateNumber(String requestAssociateNumber) {
		this.requestAssociateNumber = requestAssociateNumber;
	}

	public Timestamp getRequestTimestamp() {
		return this.requestTimestamp;
	}

	public void setRequestTimestamp(Timestamp requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	
	public boolean getInterchangeable() {
		return interchangeable;
	}

	public void setInterchangeable(boolean interchangeable) {
		this.interchangeable = interchangeable;
	}

	public String getRequestAssociateName() {
		return requestAssociateName;
	}

	public void setRequestAssociateName(String requestAssociateName) {
		this.requestAssociateName = requestAssociateName;
	}
	
	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}


	@Override
	public Object getId() {
		return this.vinPartApprovalId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approveAssociateNumber == null) ? 0 : approveAssociateNumber.hashCode());
		result = prime * result + ((approveStatus == null) ? 0 : approveStatus.hashCode());
		result = prime * result + ((approveTimestamp == null) ? 0 : approveTimestamp.hashCode());
		result = prime * result + ((currentDcPartNumber == null) ? 0 : currentDcPartNumber.hashCode());
		result = prime * result + (currentShipStatus ? 1231 : 1237);
		result = prime * result + (interchangeable ? 1231 : 1237);
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((newDcPartNumber == null) ? 0 : newDcPartNumber.hashCode());
		result = prime * result + (newShipStatus ? 1231 : 1237);
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((requestAssociateNumber == null) ? 0 : requestAssociateNumber.hashCode());
		result = prime * result + ((requestTimestamp == null) ? 0 : requestTimestamp.hashCode());
		result = prime * result + ((vinPartApprovalId == null) ? 0 : vinPartApprovalId.hashCode());
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
		VinPartApproval other = (VinPartApproval) obj;
		if (approveAssociateNumber == null) {
			if (other.approveAssociateNumber != null)
				return false;
		} else if (!approveAssociateNumber.equals(other.approveAssociateNumber))
			return false;
		if (approveStatus != other.approveStatus)
			return false;
		if (approveTimestamp == null) {
			if (other.approveTimestamp != null)
				return false;
		} else if (!approveTimestamp.equals(other.approveTimestamp))
			return false;
		if (currentDcPartNumber == null) {
			if (other.currentDcPartNumber != null)
				return false;
		} else if (!currentDcPartNumber.equals(other.currentDcPartNumber))
			return false;
		if (currentShipStatus != other.currentShipStatus)
			return false;
		if (interchangeable != other.interchangeable)
			return false;
		if (letSystemName == null) {
			if (other.letSystemName != null)
				return false;
		} else if (!letSystemName.equals(other.letSystemName))
			return false;
		if (newDcPartNumber == null) {
			if (other.newDcPartNumber != null)
				return false;
		} else if (!newDcPartNumber.equals(other.newDcPartNumber))
			return false;
		if (newShipStatus != other.newShipStatus)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (requestAssociateNumber == null) {
			if (other.requestAssociateNumber != null)
				return false;
		} else if (!requestAssociateNumber.equals(other.requestAssociateNumber))
			return false;
		if (requestTimestamp == null) {
			if (other.requestTimestamp != null)
				return false;
		} else if (!requestTimestamp.equals(other.requestTimestamp))
			return false;
		if (vinPartApprovalId == null) {
			if (other.vinPartApprovalId != null)
				return false;
		} else if (!vinPartApprovalId.equals(other.vinPartApprovalId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId(), getApproveAssociateNumber(), getApproveStatus(),
				getApproveTimestamp(), getCurrentDcPartNumber(), getCurrentShipStatus(),
				getLetSystemName(), getNewDcPartNumber(), getNewShipStatus(),
				getProductId(), getRequestAssociateNumber(), getRequestTimestamp(),
				getInterchangeable());
	}
}