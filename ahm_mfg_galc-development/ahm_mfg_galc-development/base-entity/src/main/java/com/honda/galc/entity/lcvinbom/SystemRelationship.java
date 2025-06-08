package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;


import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the SYSTEM_RELATIONSHIP database table.
 * 
 */
@Entity
@Table(name="SYSTEM_RELATIONSHIP", schema="LCVINBOM")
public class SystemRelationship extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SystemRelationshipId systemRelationshipId;

	@Column(name="UPDATE_USER", length=11)
	private String updateUser;


	public SystemRelationship() {
	}

	@Override
	public String toString() {
		return toString(getId(), getUpdatedUser());
	}

	@Override
	public SystemRelationshipId getId() {
		return this.systemRelationshipId;
	}

	public void setId(SystemRelationshipId id) {
		this.systemRelationshipId = id;
	}

	public String getUpdatedUser() {
		return StringUtils.trim(this.updateUser);
	}

	public void setUpdatedUser(String updatedUser) {
		this.updateUser = updatedUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((systemRelationshipId == null) ? 0 : systemRelationshipId.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
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
		SystemRelationship other = (SystemRelationship) obj;
		if (systemRelationshipId == null) {
			if (other.systemRelationshipId != null)
				return false;
		} else if (!systemRelationshipId.equals(other.systemRelationshipId))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		return true;
	}
	
}