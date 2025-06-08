package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@MappedSuperclass()
public abstract class LetProgramResultValue extends AuditEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 589635507410766221L;

	@EmbeddedId
	private LetProgramResultValueId id;

	@Column(name="INSPECTION_PARAM_VALUE")
	private String inspectionParamValue;

	@Column(name="INSPECTION_PARAM_UNIT")
	private String inspectionParamUnit;
	
	@Column(name="LOW_LIMIT")
	private String lowLimit;

	@Column(name="HIGH_LIMIT")
	private String highLimit;

	public LetProgramResultValueId getId() {
		return id;
	}

	public void setId(final LetProgramResultValueId id) {
		this.id = id;
	}

	public String getInspectionParamValue() {
		return StringUtils.trim(inspectionParamValue);
	}

	public void setInspectionParamValue(final String inspectionParamValue) {
		this.inspectionParamValue = inspectionParamValue;
	}

	public String getInspectionParamUnit() {
		return StringUtils.trim(inspectionParamUnit);
	}

	public void setInspectionParamUnit(final String inspectionParamUnit) {
		this.inspectionParamUnit = inspectionParamUnit;
	}

	public String getLowLimit() {
		return StringUtils.trim(lowLimit);
	}

	public void setLowLimit(final String lowLimit) {
		this.lowLimit = lowLimit;
	}

	public String getHighLimit() {
		return StringUtils.trim(highLimit);
	}

	public void setHighLimit(final String highLimit) {
		this.highLimit = highLimit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((inspectionParamUnit == null) ? 0 : inspectionParamUnit
						.hashCode());
		result = prime
				* result
				+ ((inspectionParamValue == null) ? 0 : inspectionParamValue
						.hashCode());
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
		LetProgramResultValue other = (LetProgramResultValue) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inspectionParamUnit == null) {
			if (other.inspectionParamUnit != null)
				return false;
		} else if (!inspectionParamUnit.equals(other.inspectionParamUnit))
			return false;
		if (inspectionParamValue == null) {
			if (other.inspectionParamValue != null)
				return false;
		} else if (!inspectionParamValue.equals(other.inspectionParamValue))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getEndTimestamp(), getId().getInspectionParamId(), getId().getInspectionParamType(),
				getId().getInspectionPgmId(), getId().getProductId(), getId().getTestSeq(), getInspectionParamValue(), 
				getInspectionParamUnit(), getLowLimit(), getHighLimit());
	}
}
