package com.honda.galc.entity.product;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>DailyDepartmentScheduleId Class description</h3>
 * <p> DailyDepartmentScheduleId description </p>
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
 * Jan 8, 2010
 *
 *
 */

@Embeddable
public class DailyDepartmentScheduleId implements Serializable {
	@Column(name="LINE_NO")
	private String lineNo;

	@Column(name="PROCESS_LOCATION")
	private String processLocation;

	@Column(name="PLANT_CODE")
	private String plantCode;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	private String shift;

	private int period;

	private static final long serialVersionUID = 1L;

	public DailyDepartmentScheduleId() {
		super();
	}

	public String getLineNo() {
		return StringUtils.trim(this.lineNo);
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProcessLocation() {
		return StringUtils.trim(this.processLocation);
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getPlantCode() {
		return StringUtils.trim(this.plantCode);
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
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
	
	public boolean isSameShift(DailyDepartmentScheduleId other) {
		return this.lineNo.equals(other.lineNo)
		&& this.processLocation.equals(other.processLocation)
		&& this.plantCode.equals(other.plantCode)
		&& this.productionDate.equals(other.productionDate)
		&& this.shift.equals(other.shift);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof DailyDepartmentScheduleId)) {
			return false;
		}
		DailyDepartmentScheduleId other = (DailyDepartmentScheduleId) o;
		return this.lineNo.equals(other.lineNo)
			&& this.processLocation.equals(other.processLocation)
			&& this.plantCode.equals(other.plantCode)
			&& this.productionDate.equals(other.productionDate)
			&& this.shift.equals(other.shift)
			&& (this.period == other.period);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.lineNo.hashCode();
		hash = hash * prime + this.processLocation.hashCode();
		hash = hash * prime + this.plantCode.hashCode();
		hash = hash * prime + this.productionDate.hashCode();
		hash = hash * prime + this.shift.hashCode();
		hash = hash * prime + this.period;
		return hash;
	}

}
