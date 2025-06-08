package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class CounterId implements Serializable {
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	private String shift;

	private int period;

	private static final long serialVersionUID = 1L;

	public CounterId() {
		super();
	}

	public CounterId(String processPointId, Date productionDate, String shift,
			int period) {
		super();
		this.processPointId = processPointId;
		this.productionDate = productionDate;
		this.shift = shift;
		this.period = period;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
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
		return StringUtils.trim(this.shift);
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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof CounterId)) {
			return false;
		}
		CounterId other = (CounterId) o;
		return this.processPointId.equals(other.processPointId)
			&& this.productionDate.equals(other.productionDate)
			&& this.shift.equals(other.shift)
			&& (this.period == other.period);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.processPointId.hashCode();
		hash = hash * prime + this.productionDate.hashCode();
		hash = hash * prime + this.shift.hashCode();
		hash = hash * prime + this.period;
		return hash;
	}

}
