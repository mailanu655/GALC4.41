package com.honda.galc.dto;

import com.honda.galc.util.ToStringUtil;

public class HoldReasonMappingDto implements IDto {
	private static final long serialVersionUID = 1L;

	@DtoTag()
	public int reasonId;
	
	@DtoTag()
	public int reasonMappingId;
	
	@DtoTag()
	public int qcActionId;
	
	@DtoTag()
	public String divisionId = null;
	
	@DtoTag()
	public String lineId = null;
	
	@DtoTag()
	public String holdReason = null;
	
	@DtoTag()
	public String accosiateId = null;
	
	@DtoTag()
	public String associateName = null;

	public int getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}

	public int getReasonMappingId() {
		return this.reasonMappingId;
	}

	public void setReasonMappingId(int reasonMappingId) {
		this.reasonMappingId = reasonMappingId;
	}

	public int getQcActionId() {
		return this.qcActionId;
	}

	public void setQcActionId(int qcActionId) {
		this.qcActionId = qcActionId;
	}

	public String getDivisionId() {
		return this.divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getLineId() {
		return this.lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getHoldReason() {
		return this.holdReason;
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}

	public String getAccosiateId() {
		return this.accosiateId;
	}

	public void setAccosiateId(String accosiateId) {
		this.accosiateId = accosiateId;
	}

	public String getAssociateName() {
		return this.associateName;
	}

	public void setAssociateName(String associateName) {
		this.associateName = associateName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accosiateId == null) ? 0 : accosiateId.hashCode());
		result = prime * result + ((associateName == null) ? 0 : associateName.hashCode());
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((holdReason == null) ? 0 : holdReason.hashCode());
		result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
		result = prime * result + qcActionId;
		result = prime * result + reasonId;
		result = prime * result + reasonMappingId;
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
		HoldReasonMappingDto other = (HoldReasonMappingDto) obj;
		if (accosiateId == null) {
			if (other.accosiateId != null)
				return false;
		} else if (!accosiateId.equals(other.accosiateId))
			return false;
		if (associateName == null) {
			if (other.associateName != null)
				return false;
		} else if (!associateName.equals(other.associateName))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (holdReason == null) {
			if (other.holdReason != null)
				return false;
		} else if (!holdReason.equals(other.holdReason))
			return false;
		if (lineId == null) {
			if (other.lineId != null)
				return false;
		} else if (!lineId.equals(other.lineId))
			return false;
		if (qcActionId != other.qcActionId)
			return false;
		if (reasonId != other.reasonId)
			return false;
		if (reasonMappingId != other.reasonMappingId)
			return false;
		return true;
	}
	
	public String toString() {
		return ToStringUtil.generateToString(this);
	}
}