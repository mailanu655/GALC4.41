package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>StandardScheduleId</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> StandardScheduleId description </p>
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
 * @author Paul Chou
 * Mar 3, 2011
 *
 */
@Embeddable
public class StandardScheduleId implements Serializable {
	@Column(name="PLANT_CODE")
	private String plantCode;

	@Column(name="LINE_NO")
	private String lineNo;

	@Column(name="PROCESS_LOCATION")
	private String processLocation;

	@Column(name="DAY_OF_WEEK")
	private String dayOfWeek;

	private String shift;

	private int period;

	private static final long serialVersionUID = 1L;

	public StandardScheduleId() {
		super();
	}

	public String getPlantCode() {
		return this.plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getLineNo() {
		return this.lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProcessLocation() {
		return this.processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getDayOfWeek() {
		return this.dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof StandardScheduleId)) {
			return false;
		}
		StandardScheduleId other = (StandardScheduleId) o;
		return this.plantCode.equals(other.plantCode)
			&& this.lineNo.equals(other.lineNo)
			&& this.processLocation.equals(other.processLocation)
			&& this.dayOfWeek.equals(other.dayOfWeek)
			&& this.shift.equals(other.shift)
			&& (this.period == other.period);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.plantCode.hashCode();
		hash = hash * prime + this.lineNo.hashCode();
		hash = hash * prime + this.processLocation.hashCode();
		hash = hash * prime + this.dayOfWeek.hashCode();
		hash = hash * prime + this.shift.hashCode();
		hash = hash * prime + this.period;
		return hash;
	}

}
