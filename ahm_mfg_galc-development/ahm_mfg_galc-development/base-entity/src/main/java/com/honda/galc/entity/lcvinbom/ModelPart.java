package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;


/**
 * The persistent class for the MODEL_PART database table.
 * 
 */
@Entity
@Table(name="MODEL_PART", schema="LCVINBOM")
public class ModelPart extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MODEL_PART_ID", unique=true, nullable=false)
	private Long modelPartId;
	
	@Column(name="PRODUCT_SPEC_WILDCARD", unique=true, nullable=false, length=30)
	private String productSpecWildcard;

	@Column(name="LET_SYSTEM_NAME", unique=true, nullable=false, length=255)
	private String letSystemName;

	@Column(name="DC_PART_NUMBER", unique=true, nullable=false, length=18)
	private String dcPartNumber;
	
	@Column(name="ACTIVE", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VinBomActiveStatus active;
	
	@Column(name="DC_CLASS", length=1)
	private String dcClass;

	@Temporal(TemporalType.DATE)
	@Column(name="DC_EFF_BEG_DATE")
	private Date dcEffBegDate;

	@Column(name="DC_NUMBER", length=10)
	private String dcNumber;

	@Column(nullable=false, columnDefinition="INT(1)")
	private boolean interchangeable;

	@Column(nullable=false, columnDefinition="INT(1)")
	private boolean reflash;

	@Column(name="SCRAP_PARTS", nullable=false, columnDefinition="INT(1)")
	private boolean scrapParts;

	public ModelPart() {
	}

	public Long getModelPartId() {
		return this.modelPartId;
	}

	@Override
	public Object getId() {
		return this.modelPartId;
	}

	public void setModelPartId(Long modelPartId) {
		this.modelPartId = modelPartId;
	}
	
	public String getProductSpecWildcard() {
		return StringUtils.trim(this.productSpecWildcard);
	}
	
	public void setProductSpecWildcard(String productSpecWildcard) {
		this.productSpecWildcard = productSpecWildcard;
	}
	
	public String getLetSystemName() {
		return StringUtils.trim(this.letSystemName);
	}
	
	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}
	
	public String getDcPartNumber() {
		return StringUtils.trim(this.dcPartNumber);
	}
	
	public void setDcPartNumber(String dcPartNumber) {
		this.dcPartNumber = dcPartNumber;
	}

	public VinBomActiveStatus getActive() {
		return this.active;
	}

	public void setActive(VinBomActiveStatus active) {
		this.active = active;
	}
	
	public String getDcClass() {
		return StringUtils.trim(this.dcClass);
	}

	public void setDcClass(String dcClass) {
		this.dcClass = dcClass;
	}

	public Date getDcEffBegDate() {
		return this.dcEffBegDate;
	}

	public void setDcEffBegDate(Date dcEffBegDate) {
		this.dcEffBegDate = dcEffBegDate;
	}

	public String getDcNumber() {
		return StringUtils.trim(this.dcNumber);
	}
	
	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public boolean getInterchangeable() {
		return this.interchangeable;
	}

	public void setInterchangeable(boolean interchangeable) {
		this.interchangeable = interchangeable;
	}

	public boolean getReflash() {
		return this.reflash;
	}

	public void setReflash(boolean reflash) {
		this.reflash = reflash;
	}

	public boolean getScrapParts() {
		return this.scrapParts;
	}

	public void setScrapParts(boolean scrapParts) {
		this.scrapParts = scrapParts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((dcClass == null) ? 0 : dcClass.hashCode());
		result = prime * result + ((dcEffBegDate == null) ? 0 : dcEffBegDate.hashCode());
		result = prime * result + ((dcNumber == null) ? 0 : dcNumber.hashCode());
		result = prime * result + ((dcPartNumber == null) ? 0 : dcPartNumber.hashCode());
		result = prime * result + (interchangeable ? 1231 : 1237);
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((modelPartId == null) ? 0 : modelPartId.hashCode());
		result = prime * result + ((productSpecWildcard == null) ? 0 : productSpecWildcard.hashCode());
		result = prime * result + (reflash ? 1231 : 1237);
		result = prime * result + (scrapParts ? 1231 : 1237);
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
		ModelPart other = (ModelPart) obj;
		if (active != other.active)
			return false;
		if (dcClass == null) {
			if (other.dcClass != null)
				return false;
		} else if (!dcClass.equals(other.dcClass))
			return false;
		if (dcEffBegDate == null) {
			if (other.dcEffBegDate != null)
				return false;
		} else if (!dcEffBegDate.equals(other.dcEffBegDate))
			return false;
		if (dcNumber == null) {
			if (other.dcNumber != null)
				return false;
		} else if (!dcNumber.equals(other.dcNumber))
			return false;
		if (dcPartNumber == null) {
			if (other.dcPartNumber != null)
				return false;
		} else if (!dcPartNumber.equals(other.dcPartNumber))
			return false;
		if (interchangeable != other.interchangeable)
			return false;
		if (letSystemName == null) {
			if (other.letSystemName != null)
				return false;
		} else if (!letSystemName.equals(other.letSystemName))
			return false;
		if (modelPartId == null) {
			if (other.modelPartId != null)
				return false;
		} else if (!modelPartId.equals(other.modelPartId))
			return false;
		if (productSpecWildcard == null) {
			if (other.productSpecWildcard != null)
				return false;
		} else if (!productSpecWildcard.equals(other.productSpecWildcard))
			return false;
		if (reflash != other.reflash)
			return false;
		if (scrapParts != other.scrapParts)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId(), getProductSpecWildcard(), getLetSystemName(), 
				getDcPartNumber(), getDcClass(), getDcEffBegDate(), getDcNumber(), 
				getActive(), getInterchangeable(), getReflash(), getScrapParts());
	}
}