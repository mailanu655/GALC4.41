package com.honda.galc.dto;

import java.sql.Date;

import com.honda.galc.util.StringUtil;

public class LotSequenceDto implements IDto {
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String productId;
	
	@DtoTag()
	private double buildSeq;
	
	@DtoTag()
	private double gpcsLotSeq;
	
	@DtoTag()
	private String productionLot;

	@DtoTag()
	private int lotSize;
	
	@DtoTag()
	private String productSpecCode;
	
	@DtoTag()
	private String planCode;

	@DtoTag()
	private String kdLotNumber;
	
	@DtoTag()
	private String remakeFlag;
	
	@DtoTag()
	private String demandType;

	@DtoTag()
	private String notes;
	
	@DtoTag()
	private int holdStatus;
	
	@DtoTag()
	private String comments;
	
	@DtoTag()
	private String processLocation;
	
	@DtoTag()
	private String boundaryMarkRequired;
	
	@DtoTag()
	private Date productionDate; 
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public double getBuildSeq() {
		return buildSeq;
	}

	public void setBuildSeq(double buildSeq) {
		this.buildSeq = buildSeq;
	}

	public double getGpcsLotSeq() {
		return gpcsLotSeq;
	}

	public void setGpcsLotSeq(double gpcsLotSeq) {
		this.gpcsLotSeq = gpcsLotSeq;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getRemakeFlag() {
		return remakeFlag;
	}

	public void setRemakeFlag(String remakeFlag) {
		this.remakeFlag = remakeFlag;
	}

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(int holdStatus) {
		this.holdStatus = holdStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getBoundaryMarkRequired() {
		return boundaryMarkRequired;
	}

	public void setBoundaryMarkRequired(String boundaryMarkRequired) {
		this.boundaryMarkRequired = boundaryMarkRequired;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(buildSeq);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((demandType == null) ? 0 : demandType.hashCode());
		temp = Double.doubleToLongBits(gpcsLotSeq);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + holdStatus;
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + lotSize;
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((remakeFlag == null) ? 0 : remakeFlag.hashCode());
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
		LotSequenceDto other = (LotSequenceDto) obj;
		if (Double.doubleToLongBits(buildSeq) != Double.doubleToLongBits(other.buildSeq))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (demandType == null) {
			if (other.demandType != null)
				return false;
		} else if (!demandType.equals(other.demandType))
			return false;
		if (Double.doubleToLongBits(gpcsLotSeq) != Double.doubleToLongBits(other.gpcsLotSeq))
			return false;
		if (holdStatus != other.holdStatus)
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (lotSize != other.lotSize)
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (remakeFlag == null) {
			if (other.remakeFlag != null)
				return false;
		} else if (!remakeFlag.equals(other.remakeFlag))
			return false;
		return true;
	}

	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(), getGpcsLotSeq(), getBuildSeq(), getComments(), getDemandType(),
				getHoldStatus(), getKdLotNumber(), getLotSize(), getNotes(), getPlanCode(), getProductId(),
				getProductionLot(), getProductSpecCode(), getRemakeFlag());
	}
}

