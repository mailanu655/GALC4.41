package com.honda.galc.client.entity.manager;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;

import static com.honda.galc.service.ServiceFactory.getDao;

public class DailyDepartmentScheduleUtil {
	
	private ProcessPoint processPoint;
	
	List<DailyDepartmentSchedule> schedules;

	public DailyDepartmentScheduleUtil(ProcessPoint processPoint) {
		
		this.processPoint = processPoint;
		loadSchedules(getTimestampNow());
		
	}
	
	public String getCurrentShift() {
		
		DailyDepartmentSchedule schedule = getCurrentSchedule();
		return schedule == null ? "" : schedule.getId().getShift();
		
	}
	
	public DailyDepartmentSchedule getCurrentScheduleByTimestamp() {
		return getScheduleByTimestamp( getTimestampNow());
	}

	private List<DailyDepartmentSchedule> loadSchedules(Timestamp stamp){
		Date date = new Date(stamp.getTime());
		if(schedules != null && !schedules.isEmpty()){
			date = schedules.get(0).getId().getProductionDate();
		    date = new Date(date.getTime() + 24*60*60*1000);
		}
		
		schedules = getDao(DailyDepartmentScheduleDao.class).findAllByProcessPoint(processPoint, date);
		return schedules;
	}
	
	private List<DailyDepartmentSchedule> loadSchedulesByTimestamp(Timestamp timestamp){
		Date date = new Date(timestamp.getTime());
		if(schedules != null && !schedules.isEmpty()){
			date = schedules.get(0).getId().getProductionDate();
		    date = new Date(date.getTime() + 24*60*60*1000);
		}
		
		schedules = getDao(DailyDepartmentScheduleDao.class).findAllByProcessPointAndTimestamp(processPoint, timestamp);
		return schedules;
	}

	public DailyDepartmentSchedule getCurrentSchedule() {
		return getSchedule( getTimestampNow());
	}
	
	public DailyDepartmentSchedule getSchedule(Timestamp timeStamp) {
		DailyDepartmentSchedule currentSchedule = findCurrentSchedule(timeStamp);
		if(currentSchedule != null) return currentSchedule;
		else {
			loadSchedules(timeStamp);
			return findCurrentSchedule(timeStamp);
		}
	}
	
	private DailyDepartmentSchedule findCurrentSchedule(Timestamp currentTimestamp){
		if(schedules == null) return null;
        for(DailyDepartmentSchedule schedule: schedules){
            if(schedule.isInBetween(currentTimestamp)) return schedule;
        }
        
        return null;
	}
	
	public DailyDepartmentSchedule getScheduleByTimestamp(Timestamp timeStamp) {
		DailyDepartmentSchedule currentSchedule = findCurrentSchedule(timeStamp);
		if(currentSchedule != null) return currentSchedule;
		else {
			loadSchedulesByTimestamp(timeStamp);
			return findCurrentSchedule(timeStamp);
		}
	}

	public Timestamp getTimestampNow() {
		return new Timestamp(System.currentTimeMillis());
	}
	
}
