package com.honda.galc.dto;

import java.sql.Timestamp;

/**
 * @author Subu Kathiresan
 * @date Mar 21, 2023
 */
public class ExtProductStatusDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag()
    private ExtProductStatusIdDto id;
	
	@DtoTag()
    private int unitOnHold;
	
	@DtoTag()
    private int holdType;
	
	@DtoTag()
    private int defectStatus;

	@DtoTag()
    private int buildResultStatus;
	
	@DtoTag()
    private int pmqaStatus;
	
	@DtoTag()
    private Timestamp createTimestamp;
    
	@DtoTag()
    private Timestamp updateTimestamp;

	public ExtProductStatusDto() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + buildResultStatus;
		result = prime * result + ((createTimestamp == null) ? 0 : createTimestamp.hashCode());
		result = prime * result + defectStatus;
		result = prime * result + holdType;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + pmqaStatus;
		result = prime * result + unitOnHold;
		result = prime * result + ((updateTimestamp == null) ? 0 : updateTimestamp.hashCode());
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
		ExtProductStatusDto other = (ExtProductStatusDto) obj;
		if (buildResultStatus != other.buildResultStatus)
			return false;
		if (createTimestamp == null) {
			if (other.createTimestamp != null)
				return false;
		} else if (!createTimestamp.equals(other.createTimestamp))
			return false;
		if (defectStatus != other.defectStatus)
			return false;
		if (holdType != other.holdType)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pmqaStatus != other.pmqaStatus)
			return false;
		if (unitOnHold != other.unitOnHold)
			return false;
		if (updateTimestamp == null) {
			if (other.updateTimestamp != null)
				return false;
		} else if (!updateTimestamp.equals(other.updateTimestamp))
			return false;
		return true;
	}

	public ExtProductStatusIdDto getId() {
		return id;
	}

	public void setId(ExtProductStatusIdDto id) {
		this.id = id;
	}

	public boolean isUnitOnHold() {
		return (unitOnHold == 1);
	}

	public void setUnitOnHold(int unitOnHold) {
		this.unitOnHold = unitOnHold;
	}

	public int getHoldType() {
		return holdType;
	}

	public void setHoldType(int holdType) {
		this.holdType = holdType;
	}

	public int getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(int defectStatus) {
		this.defectStatus = defectStatus;
	}

	public int getBuildResultStatus() {
		return buildResultStatus;
	}

	public void setBuildResultStatus(int buildResultStatus) {
		this.buildResultStatus = buildResultStatus;
	}

	public int getPmqaStatus() {
		return pmqaStatus;
	}

	public void setPmqaStatus(int pmqaStatus) {
		this.pmqaStatus = pmqaStatus;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}
