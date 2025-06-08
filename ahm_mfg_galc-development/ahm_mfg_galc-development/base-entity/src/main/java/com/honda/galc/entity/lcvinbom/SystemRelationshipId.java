package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the SYSTEM_RELATIONSHIP database table.
 * 
 */
@Embeddable
public class SystemRelationshipId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="BEAM_SYSTEM_NAME", unique=true, nullable=false, length=255)
	private String beamSystemName;

	@Column(name="LET_SYSTEM_NAME", unique=true, nullable=false, length=255)
	private String letSystemName;

	public SystemRelationshipId() {
	}
	
	public String getLetSystemName() {
		return StringUtils.trim(this.letSystemName);
	}
	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}
	
	public String getBeamSystemName() {
		return beamSystemName;
	}

	public void setBeamSystemName(String beamSystemName) {
		this.beamSystemName = beamSystemName;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SystemRelationshipId)) {
			return false;
		}
		SystemRelationshipId castOther = (SystemRelationshipId)other;
		return 
			this.beamSystemName.equals(castOther.beamSystemName)
			&& this.letSystemName.equals(castOther.letSystemName);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.letSystemName.hashCode();
		hash = hash * prime + this.beamSystemName.hashCode();
		
		return hash;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(),
				 getLetSystemName(), getBeamSystemName());
	}
}