package com.honda.galc.entity.product;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>StandardSchedule</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> StandardSchedule description </p>
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

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="STANDARD_SCHEDULE_TBX")
public class StandardSchedule extends AuditEntry {
	@EmbeddedId
	private StandardScheduleId id;

	@Column(name="PERIOD_LABEL")
	private String periodLabel;

	private String type;

	private String plan;

	@Column(name="START_TIME")
	private Time startTime;

	@Column(name="END_TIME")
	private Time endTime;

	@Column(name="NEXT_DAY")
	private short nextDay;

	private int capacity;

	@Column(name="CAPACITY_ON")
	private int capacityOn;

	private static final long serialVersionUID = 1L;

	public StandardSchedule() {
		super();
	}

	public StandardScheduleId getId() {
		return this.id;
	}

	public void setId(StandardScheduleId pk) {
		this.id = pk;
	}

	public String getPeriodLabel() {
		return this.periodLabel;
	}

	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlan() {
		return this.plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public Time getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public short getNextDay() {
		return this.nextDay;
	}

	public void setNextDay(short nextDay) {
		this.nextDay = nextDay;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacityOn() {
		return this.capacityOn;
	}

	public void setCapacityOn(int capacityOn) {
		this.capacityOn = capacityOn;
	}

	@Override
	public String toString() {
		return toString(id.getProcessLocation(),id.getShift(),id.getPeriod());
	}

}
