package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class CounterByProductionLotId implements Serializable {
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	private String shift;

	private int period;

	@Column(name="PRODUCTION_LOT")
	private String productionLot;

	private static final long serialVersionUID = 1L;

	public CounterByProductionLotId() {
		super();
	}
	
	public CounterByProductionLotId(String processPointId, Date productionDate,
			String shift, int period, String productionLot) {
		super();
		this.processPointId = processPointId;
		this.productionDate = productionDate;
		this.shift = shift;
		this.period = period;
		this.productionLot = productionLot;
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

	public String getProductionLot() {
		return StringUtils.trim(this.productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + period;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result
				+ ((productionLot == null) ? 0 : productionLot.hashCode());
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
		CounterByProductionLotId other = (CounterByProductionLotId) obj;
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
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		return true;
	}

}
