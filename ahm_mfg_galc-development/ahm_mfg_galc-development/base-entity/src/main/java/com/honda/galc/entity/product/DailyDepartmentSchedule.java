package com.honda.galc.entity.product;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>DailyDepartmentSchedule Class description</h3>
 * <p> DailyDepartmentSchedule description </p>
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

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL226TBX")
public class DailyDepartmentSchedule extends AuditEntry {
	@EmbeddedId
	private DailyDepartmentScheduleId id;

	@Column(name="DAY_OF_WEEK")
	private String dayOfWeek;

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

	@Column(name="PLAN_CODE")
	private String planCode;

	@Column(name="WEEK_NO")
	private String weekNo;

	private String iswork;

	@Column(name="START_TIMESTAMP")
	private Timestamp startTimestamp;

	@Column(name="END_TIMESTAMP")
	private Timestamp endTimestamp;

	private static final long serialVersionUID = 1L;

	public DailyDepartmentSchedule() {
		super();
	}

	public DailyDepartmentScheduleId getId() {
		return this.id;
	}

	public void setId(DailyDepartmentScheduleId id) {
		this.id = id;
	}

	public String getDayOfWeek() {
		return StringUtils.trim(this.dayOfWeek);
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getPeriodLabel() {
		return StringUtils.trim(this.periodLabel);
	}

	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}

	public String getType() {
		return StringUtils.trim(this.type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlan() {
		return StringUtils.trim(this.plan);
	}
	
	public boolean isPlan() {
		return "Y".equalsIgnoreCase(getPlan());
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
	
	public long getDuration() {
		long duration = endTime.getTime() - startTime.getTime();
		return (duration <0) ? 24*60*60*1000 + duration : duration;
	}
	
	public long getDuration(Timestamp currentTime){
		return Math.abs(currentTime.getTime()-startTimestamp.getTime());
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
	
	public boolean isNextDay() {
		return this.nextDay == 1;
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

	public String getPlanCode() {
		return StringUtils.trim(this.planCode);
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getWeekNo() {
		return StringUtils.trim(this.weekNo);
	}

	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}

	public String getIswork() {
		return StringUtils.trim(this.iswork);
	}

	public void setIswork(String iswork) {
		this.iswork = iswork;
	}

	public Timestamp getStartTimestamp() {
		return this.startTimestamp;
	}

	public void setStartTimestamp(Timestamp startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Timestamp getEndTimestamp() {
		return this.endTimestamp;
	}

	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	
    private Timestamp getCurrentTimestamp(){
        
        return new Timestamp(System.currentTimeMillis());
        
    }
    
    /**
     * is current time inbetween this period
     * @return
     */
    public boolean isInBetween(){
        
        return isInBetween(getCurrentTimestamp());
        
    }
    
    /**
     * is this timestamp in between this period
     * @param currentTimestamp
     * @return
     */
    public boolean isInBetween(Timestamp currentTimestamp){
        
        return ( currentTimestamp.after(getStartTimestamp()) || currentTimestamp.equals(getStartTimestamp()))&&
                    (currentTimestamp.equals(getEndTimestamp())|| currentTimestamp.before(getEndTimestamp()));
            
    }

    
    public boolean isAfter() {
        
        return isAfter(getCurrentTimestamp());
        
    }
    
    
    /**
     * test if currentTimestamp is after this peroid
     * @param currentTimestamp
     * @return
     */
    
    public boolean isAfter(Timestamp currentTimestamp){
        
        return currentTimestamp.after(this.getEndTimestamp());
    }
    
    public boolean isBefore() {
        
        return isBefore(getCurrentTimestamp());
        
    }
    
    /**
     * test if currentTimestamp is before this period
     * @param currentTimestamp
     * @return
     */
    
    public boolean isBefore(Timestamp currentTimestamp){
        
        return currentTimestamp.before(this.getStartTimestamp());
        
    }
    
    public String toString(){
    	
    	return toString(getId().getProcessLocation(),
    			        getId().getProductionDate(),
    			        getId().getShift(),
    			        getId().getPeriod());
    }

	public boolean isPeriodPast(Timestamp testTimestamp) {
		return getEndTimestamp().before(testTimestamp);
	}

	public boolean isPeriodCurrent(Timestamp testTimestamp) {
		return getStartTimestamp().before(testTimestamp) && getEndTimestamp().after(testTimestamp);
	}

}
