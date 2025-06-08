package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.VinBomApprovalStatus;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the MODEL_PART_APPROVAL database table.
 * 
 */
@Entity
@Table(name="MODEL_PART_APPROVAL", schema="LCVINBOM")
public class ModelPartApproval extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MODEL_PART_APPROVAL_ID", unique=true, nullable=false)
	private Long modelPartApprovalId;

	@Column(name="APPROVE_ASSOCIATE_NUMBER", length=11)
	private String approveAssociateNumber;

	@Column(name="APPROVE_STATUS", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VinBomApprovalStatus approveStatus;

	@Column(name="APPROVE_TIMESTAMP")
	private Timestamp approveTimestamp;

	@Column(name="CURRENT_INTERCHANGABLE", nullable=false, columnDefinition="INT(1)")
	private boolean currentInterchangable;

	@Column(name="CURRENT_REFLASH", nullable=false, columnDefinition="INT(1)")
	private boolean currentReflash;

	@Column(name="CURRENT_SCRAP_PARTS", nullable=false, columnDefinition="INT(1)")
	private boolean currentScrapParts;

	@Column(name="CURRENT_STARTING_PRODUCTION_LOT", nullable=false, length=20)
	private String currentStartingProductionLot;

	@Column(name="MODEL_PART_ID", nullable=false)
	private long modelPartId;

	@Column(name="NEW_INTERCHANGABLE", nullable=false, columnDefinition="INT(1)")
	private boolean newInterchangable;

	@Column(name="NEW_REFLASH", nullable=false, columnDefinition="INT(1)")
	private boolean newReflash;

	@Column(name="NEW_SCRAP_PARTS", nullable=false, columnDefinition="INT(1)")
	private boolean newScrapParts;

	@Column(name="NEW_STARTING_PRODUCTION_LOT", nullable=false, length=20)
	private String newStartingProductionLot;

	@Column(name="REQUEST_ASSOCIATE_NUMBER", nullable=false, length=11)
	private String requestAssociateNumber;

	@Column(name="REQUEST_TIMESTAMP", nullable=false)
	private Timestamp requestTimestamp;
	
	@Column(name="RETURN_TO_ACTIVE")
	private int returnToActive;

	@Transient 
	private String requestAssociateName;
	
	@Transient 
	private Date currentStartingProductionDate;
	
	@Transient 
	private Date newStartingProductionDate;
	
	@Transient
	private ModelPart modelPart;
	
	@Transient
	private ModelLot modelLot;
	
	@Transient 
	private String model;
	
	@Transient 
	private String modelType;

	public ModelPartApproval() {
	}

	public Long getModelPartApprovalId() {
		return this.modelPartApprovalId;
	}

	public void setModelPartApprovalId(Long modelPartApprovalId) {
		this.modelPartApprovalId = modelPartApprovalId;
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

	public boolean getCurrentInterchangable() {
		return this.currentInterchangable;
	}

	public void setCurrentInterchangable(boolean currentInterchangable) {
		this.currentInterchangable = currentInterchangable;
	}

	public boolean getCurrentReflash() {
		return this.currentReflash;
	}

	public void setCurrentReflash(boolean currentReflash) {
		this.currentReflash = currentReflash;
	}

	public boolean getCurrentScrapParts() {
		return this.currentScrapParts;
	}

	public void setCurrentScrapParts(boolean currentScrapParts) {
		this.currentScrapParts = currentScrapParts;
	}

	public String getCurrentStartingProductionLot() {
		return StringUtils.trim(this.currentStartingProductionLot);
	}

	public void setCurrentStartingProductionLot(String currentStartingProductionLot) {
		this.currentStartingProductionLot = currentStartingProductionLot;
	}

	public long getModelPartId() {
		return this.modelPartId;
	}

	public void setModelPartId(long modelPartId) {
		this.modelPartId = modelPartId;
	}

	public boolean getNewInterchangable() {
		return this.newInterchangable;
	}

	public void setNewInterchangable(boolean newInterchangable) {
		this.newInterchangable = newInterchangable;
	}

	public boolean getNewReflash() {
		return this.newReflash;
	}

	public void setNewReflash(boolean newReflash) {
		this.newReflash = newReflash;
	}

	public boolean getNewScrapParts() {
		return this.newScrapParts;
	}

	public void setNewScrapParts(boolean newScrapParts) {
		this.newScrapParts = newScrapParts;
	}

	public String getNewStartingProductionLot() {
		return StringUtils.trim(this.newStartingProductionLot);
	}

	public void setNewStartingProductionLot(String newStartingProductionLot) {
		this.newStartingProductionLot = newStartingProductionLot;
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

	public int getReturnToActive() {
		return returnToActive;
	}

	public void setReturnToActive(int returnToActive) {
		this.returnToActive = returnToActive;
	}
	
	public String getRequestAssociateName() {
		return requestAssociateName;
	}

	public void setRequestAssociateName(String requestAssociateName) {
		this.requestAssociateName = requestAssociateName;
	}

	public Date getCurrentStartingProductionDate() {
		return currentStartingProductionDate;
	}

	public void setCurrentStartingProductionDate(Date currentStartingProductionDate) {
		this.currentStartingProductionDate = currentStartingProductionDate;
	}

	public Date getNewStartingProductionDate() {
		return newStartingProductionDate;
	}

	public void setNewStartingProductionDate(Date newStartingProductionDate) {
		this.newStartingProductionDate = newStartingProductionDate;
	}

	public ModelPart getModelPart() {
		return modelPart;
	}

	public void setModelPart(ModelPart modelPart) {
		this.modelPart = modelPart;
	}

	public ModelLot getModelLot() {
		return modelLot;
	}

	public void setModelLot(ModelLot modelLot) {
		this.modelLot = modelLot;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	@Override
	public Object getId() {
		return this.modelPartApprovalId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approveAssociateNumber == null) ? 0 : approveAssociateNumber.hashCode());
		result = prime * result + ((approveStatus == null) ? 0 : approveStatus.hashCode());
		result = prime * result + ((approveTimestamp == null) ? 0 : approveTimestamp.hashCode());
		result = prime * result + (currentInterchangable ? 1231 : 1237);
		result = prime * result + (currentReflash ? 1231 : 1237);
		result = prime * result + (currentScrapParts ? 1231 : 1237);
		result = prime * result
				+ ((currentStartingProductionLot == null) ? 0 : currentStartingProductionLot.hashCode());
		result = prime * result + ((modelPartApprovalId == null) ? 0 : modelPartApprovalId.hashCode());
		result = prime * result + (int) (modelPartId ^ (modelPartId >>> 32));
		result = prime * result + (newInterchangable ? 1231 : 1237);
		result = prime * result + (newReflash ? 1231 : 1237);
		result = prime * result + (newScrapParts ? 1231 : 1237);
		result = prime * result + ((newStartingProductionLot == null) ? 0 : newStartingProductionLot.hashCode());
		result = prime * result + ((requestAssociateNumber == null) ? 0 : requestAssociateNumber.hashCode());
		result = prime * result + ((requestTimestamp == null) ? 0 : requestTimestamp.hashCode());
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
		ModelPartApproval other = (ModelPartApproval) obj;
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
		if (currentInterchangable != other.currentInterchangable)
			return false;
		if (currentReflash != other.currentReflash)
			return false;
		if (currentScrapParts != other.currentScrapParts)
			return false;
		if (currentStartingProductionLot == null) {
			if (other.currentStartingProductionLot != null)
				return false;
		} else if (!currentStartingProductionLot.equals(other.currentStartingProductionLot))
			return false;
		if (modelPartApprovalId == null) {
			if (other.modelPartApprovalId != null)
				return false;
		} else if (!modelPartApprovalId.equals(other.modelPartApprovalId))
			return false;
		if (modelPartId != other.modelPartId)
			return false;
		if (newInterchangable != other.newInterchangable)
			return false;
		if (newReflash != other.newReflash)
			return false;
		if (newScrapParts != other.newScrapParts)
			return false;
		if (newStartingProductionLot == null) {
			if (other.newStartingProductionLot != null)
				return false;
		} else if (!newStartingProductionLot.equals(other.newStartingProductionLot))
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
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId(), getApproveAssociateNumber(), getApproveStatus(), getApproveTimestamp(), 
				getCurrentInterchangable(), getCurrentReflash(), getCurrentScrapParts(), getCurrentStartingProductionLot(), 
				getModelPartId(), getNewInterchangable(), getNewReflash(), getNewScrapParts(), getNewStartingProductionLot(),
				getRequestAssociateNumber(), getRequestTimestamp(), getReturnToActive());


	}

}