package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the "PART" database table.
 * 
 */
@Entity
@Table(name="\"PART\"", schema="LCVINBOM")
public class VinBomPart extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VinBomPartId id;

	@Column(name="BASE_PART_NUMBER", nullable=false, length=18)
	private String basePartNumber;


	@Column(length=128)
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_BEGIN_DATE", nullable=false)
	private Date effectiveBeginDate;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_END_DATE", nullable=false)
	private Date effectiveEndDate;
	
	@Transient
	private String effectiveBeginDateString;
	
	@Transient
	private String effectiveEndDateString;

	public VinBomPart() {
	}

	public VinBomPartId getId() {
		return this.id;
	}

	public void setId(VinBomPartId id) {
		this.id = id;
	}

	public String getBasePartNumber() {
		return StringUtils.trim(this.basePartNumber);
	}

	public void setBasePartNumber(String basePartNumber) {
		this.basePartNumber = basePartNumber;
	}

	

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEffectiveBeginDate() {
		return this.effectiveBeginDate;
	}
	
	public String getEffectiveBeginDateString() {
		
		return this.effectiveBeginDateString;
	}

	public void setEffectiveBeginDate(Date effectiveBeginDate) {
		this.effectiveBeginDate = effectiveBeginDate;
	}

	public String getEffectiveEndDateString() {
		return this.effectiveEndDateString;
	}
	public Date getEffectiveEndDate() {
		return this.effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basePartNumber == null) ? 0 : basePartNumber.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((effectiveBeginDate == null) ? 0 : effectiveBeginDate.hashCode());
		result = prime * result + ((effectiveEndDate == null) ? 0 : effectiveEndDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		VinBomPart other = (VinBomPart) obj;
		if (basePartNumber == null) {
			if (other.basePartNumber != null)
				return false;
		} else if (!basePartNumber.equals(other.basePartNumber))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (effectiveBeginDate == null) {
			if (other.effectiveBeginDate != null)
				return false;
		} else if (!effectiveBeginDate.equals(other.effectiveBeginDate))
			return false;
		if (effectiveEndDate == null) {
			if (other.effectiveEndDate != null)
				return false;
		} else if (!effectiveEndDate.equals(other.effectiveEndDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setEffectiveBeginDateString(String effectiveBeginDateString) {
		this.effectiveBeginDateString = effectiveBeginDateString;
	}

	public void setEffectiveEndDateString(String effectiveEndDateString) {
		this.effectiveEndDateString = effectiveEndDateString;
	}

	@Override
	public String toString() {
		return toString(getId().getProductSpecCode(), getId().getLetSystemName(), 
				getBasePartNumber(), getDescription(),
				getEffectiveBeginDate(), getEffectiveEndDate());
	}
}