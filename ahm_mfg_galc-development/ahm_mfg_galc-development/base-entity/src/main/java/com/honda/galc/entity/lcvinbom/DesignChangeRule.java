package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.VinBomDesignChangeRuleRequired;


/**
 * The persistent class for the DESIGN_CHANGE_RULES database table.
 * 
 */
@Entity
@Table(name="DESIGN_CHANGE_RULES", schema="LCVINBOM")
public class DesignChangeRule extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DC_CLASS", unique=true, nullable=false, length=10)
	private String dcClass;
	
	@Column(name="INTERCHANGABLE", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VinBomDesignChangeRuleRequired interchangable;

	@Column(name="REFLASH", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VinBomDesignChangeRuleRequired reflash;

	@Column(name="SCRAP_PARTS", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VinBomDesignChangeRuleRequired scrapParts;

	public DesignChangeRule() {
	}

	public String getDcClass() {
		return StringUtils.trim(this.dcClass);
	}

	public void setDcClass(String dcClass) {
		this.dcClass = dcClass;
	}

	public VinBomDesignChangeRuleRequired getInterchangable() {
		return this.interchangable;
	}

	public void setInterchangable(VinBomDesignChangeRuleRequired interchangable) {
		this.interchangable = interchangable;
	}

	public VinBomDesignChangeRuleRequired getReflash() {
		return this.reflash;
	}

	public void setReflash(VinBomDesignChangeRuleRequired reflash) {
		this.reflash = reflash;
	}

	public VinBomDesignChangeRuleRequired getScrapParts() {
		return this.scrapParts;
	}

	public void setScrapParts(VinBomDesignChangeRuleRequired scrapParts) {
		this.scrapParts = scrapParts;
	}

	@Override
	public Object getId() {
		return this.dcClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dcClass == null) ? 0 : dcClass.hashCode());
		result = prime * result + ((interchangable == null) ? 0 : interchangable.hashCode());
		result = prime * result + ((reflash == null) ? 0 : reflash.hashCode());
		result = prime * result + ((scrapParts == null) ? 0 : scrapParts.hashCode());
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
		DesignChangeRule other = (DesignChangeRule) obj;
		if (dcClass == null) {
			if (other.dcClass != null)
				return false;
		} else if (!dcClass.equals(other.dcClass))
			return false;
		if (interchangable != other.interchangable)
			return false;
		if (reflash != other.reflash)
			return false;
		if (scrapParts != other.scrapParts)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId(), getReflash(), getInterchangable(), getScrapParts());
	}
}