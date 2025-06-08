package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class SystemrelationshipDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	private String updateUser;
	private String beamSystemName;
	private String letSystemName;
	
	public SystemrelationshipDto() {
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getBeamSystemName() {
		return beamSystemName;
	}

	public void setBeamSystemName(String beamSystemName) {
		this.beamSystemName = beamSystemName;
	}

	public String getLetSystemName() {
		return letSystemName;
	}

	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}

	public SystemrelationshipDto(String updateUser, String beamSystemName, String letSystemName) {
		super();
		this.updateUser = updateUser;
		this.beamSystemName = beamSystemName;
		this.letSystemName = letSystemName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beamSystemName == null) ? 0 : beamSystemName.hashCode());
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
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
		SystemrelationshipDto other = (SystemrelationshipDto) obj;
		if (beamSystemName == null) {
			if (other.beamSystemName != null)
				return false;
		} else if (!beamSystemName.equals(other.beamSystemName))
			return false;
		if (letSystemName == null) {
			if (other.letSystemName != null)
				return false;
		} else if (!letSystemName.equals(other.letSystemName))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(), 
				getBeamSystemName(), getLetSystemName());
	}
}
