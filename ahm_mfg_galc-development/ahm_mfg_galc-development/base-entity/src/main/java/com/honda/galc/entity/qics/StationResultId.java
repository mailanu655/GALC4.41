package com.honda.galc.entity.qics;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>StationResultId Class description</h3>
 * <p> StationResultId description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 30, 2011
 *
 *
 */
@Embeddable
public class StationResultId implements Serializable {
	@Column(name="APPLICATION_ID")
	private String applicationId;

	private String shift;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	private static final long serialVersionUID = 1L;

	public StationResultId() {
		super();
	}

	public String getApplicationId() {
		return StringUtils.trim(this.applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getShift() {
		return StringUtils.trim(this.shift);
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof StationResultId)) {
			return false;
		}
		StationResultId other = (StationResultId) o;
		return this.applicationId.equals(other.applicationId)
			&& this.shift.equals(other.shift)
			&& this.productionDate.equals(other.productionDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.applicationId.hashCode();
		hash = hash * prime + this.shift.hashCode();
		hash = hash * prime + this.productionDate.hashCode();
		return hash;
	}
	
	public String toString() {
		return getApplicationId() + "," + getShift();
	}

}
