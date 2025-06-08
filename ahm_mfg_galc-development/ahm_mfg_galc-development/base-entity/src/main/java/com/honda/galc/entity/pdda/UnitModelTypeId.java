package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Embeddable
public class UnitModelTypeId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="MTC_MODEL", nullable=false, length=3)
	private String mtcModel;

	@Column(name="MTC_TYPE", nullable=false, length=3)
	private String mtcType;

	@Column(name="MTC_OPTION", nullable=false, length=3)
	private String mtcOption;

    public UnitModelTypeId() {}
    
	public int getMaintenanceId() {
		return this.maintenanceId;
	}
	
	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	public BigDecimal getModelYearDate() {
		return this.modelYearDate;
	}
	
	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}
	
	public String getMtcModel() {
		return StringUtils.trim(this.mtcModel);
	}
	
	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}
	
	public String getMtcType() {
		return StringUtils.stripEnd(this.mtcType, null);
	}
	
	public void setMtcType(String mtcType) {
		this.mtcType = mtcType;
	}
	
	public String getMtcOption() {
		return StringUtils.trim(this.mtcOption);
	}
	
	public void setMtcOption(String mtcOption) {
		this.mtcOption = mtcOption;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result
				+ ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result
				+ ((mtcOption == null) ? 0 : mtcOption.hashCode());
		result = prime * result + ((mtcType == null) ? 0 : mtcType.hashCode());
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
		UnitModelTypeId other = (UnitModelTypeId) obj;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (modelYearDate == null) {
			if (other.modelYearDate != null)
				return false;
		} else if (!modelYearDate.equals(other.modelYearDate))
			return false;
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		if (mtcOption == null) {
			if (other.mtcOption != null)
				return false;
		} else if (!mtcOption.equals(other.mtcOption))
			return false;
		if (mtcType == null) {
			if (other.mtcType != null)
				return false;
		} else if (!mtcType.equals(other.mtcType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getMaintenanceId(), getModelYearDate(), getMtcModel(), getMtcType(), getMtcOption());
	}
}