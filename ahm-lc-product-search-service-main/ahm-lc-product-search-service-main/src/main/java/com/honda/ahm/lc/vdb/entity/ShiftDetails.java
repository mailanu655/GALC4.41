package com.honda.ahm.lc.vdb.entity;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ShiftDetails</code> is view class for Shift.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */

@Entity
@Immutable
@Subselect(value="select distinct " + 
		"SHIFT||'__'||PERIOD_LABEL as id, END_TIME as end_time, PERIOD as period, PERIOD_LABEL as period_label, " + 
		"PRODUCTION_DATE as production_date, SHIFT as shift, START_TIME as start_time " + 
		"from GAL226TBX " + 
		"order by SHIFT, PRODUCTION_DATE, START_TIME")
public class ShiftDetails extends VdbEntity<String> {
    
	private static final long serialVersionUID = 1L;
	
	@Column(name="end_time")
	private Time endTime;
	
	@Column(name="period")
	private Integer period;
	
	@Column(name="period_label")
	private String periodLabel;
	
	@Column(name="production_date")
	private Date productionDate;
	
	@Column(name="shift")
	private String shift;
	
	@Column(name="start_time")
	private Time startTime;
	
    public ShiftDetails() {
		super();
	}

	@Override
	public String toString() {
		String str = getClass().getSimpleName() + "{";
		str = str + "id: " + getId();
		str = str + "endTime: " + getEndTime();
		str = str + "period: " + getPeriod();
		str = str + "periodLabel: " + getPeriodLabel();
		str = str + "productionDate: " + getProductionDate();
		str = str + "shift: " + getShift();
		str = str + "startTime: " + getStartTime();
        str = str + "}";
        return str;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getPeriodLabel() {
		return StringUtils.trimToEmpty(periodLabel);
	}

	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getShift() {
		return StringUtils.trimToEmpty(shift);
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

}