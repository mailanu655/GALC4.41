package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PostLoad;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class CounterByModelGroupId implements Serializable {
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	private String shift;

	private int period;

	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="MODEL_YEAR_CODE")
	private String modelYearCode;

	private static final long serialVersionUID = 1L;

	public CounterByModelGroupId() {
		super();
	}
	
	public CounterByModelGroupId(String processPointId, Date productionDate,
			String shift, int period, String modelCode, String modelYearCode) {
		super();
		this.processPointId = processPointId;
		this.productionDate = productionDate;
		this.shift = shift;
		this.period = period;
		this.modelCode = modelCode;
		this.modelYearCode = modelYearCode;
	}


	public String getProcessPointId() {
		return this.processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getShift() {
		return this.shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getModelCode() {
		return this.modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelYearCode() {
		return this.modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result
				+ ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + period;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
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
		CounterByModelGroupId other = (CounterByModelGroupId) obj;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (period != other.period)
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		return true;
	}

	@PostLoad
	public void postLoad() {
		this.processPointId = StringUtils.trim(this.processPointId);
		this.shift = StringUtils.trim(this.shift);
		this.modelCode = StringUtils.trim(this.modelCode);
		this.modelYearCode = StringUtils.trim(this.modelYearCode);
		
	}

}
