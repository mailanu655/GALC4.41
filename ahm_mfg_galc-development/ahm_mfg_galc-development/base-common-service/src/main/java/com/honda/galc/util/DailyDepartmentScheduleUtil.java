package com.honda.galc.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;

public class DailyDepartmentScheduleUtil {
	
	private ProcessPoint processPoint;
	
	List<DailyDepartmentSchedule> schedules;
	
	GpcsDivision division;


	public DailyDepartmentScheduleUtil(ProcessPoint processPoint) {
		this.processPoint = processPoint;
		division = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
		loadSchedules(getProductionDate());
	}
	
	public DailyDepartmentScheduleUtil(ProcessPoint processPoint, Date date) {
		  this.processPoint = processPoint;
		  division = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
		  loadSchedules(date);
    }
	
	public String getCurrentShift() {
		
		DailyDepartmentSchedule schedule = getCurrentSchedule();
		return schedule == null ? "" : schedule.getId().getShift();
		
	}
	
	public Date getProductionDate() {
		DailyDepartmentSchedule schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(division.getGpcsLineNo(), division.getGpcsProcessLocation(),division.getGpcsPlantCode(),getTimestampNow() );
		if(schedule!=null) return schedule.getId().getProductionDate();
		Logger.getLogger().warn("Failed to find schedule use system date for production date:" + ((processPoint !=null)? processPoint.getProcessPointId() : ""));
		return new Date(System.currentTimeMillis());
	}
	
	private void loadSchedules(Date date) {
		  
		  schedules = getDao(DailyDepartmentScheduleDao.class).findAllByProcessPoint(processPoint, date);
		  
    }
	
	public DailyDepartmentSchedule getCurrentSchedule() {
		
		Timestamp currentTimestamp = getTimestampNow();
		DailyDepartmentSchedule currentSchedule = null;
        
        for(DailyDepartmentSchedule schedule: schedules){
            
            if(!schedule.isAfter(currentTimestamp)) return schedule;
            else currentSchedule = schedule;
        }
        
        return currentSchedule;
		
	}
	
	public Timestamp getTimestampNow() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	
	public static boolean isToday(java.util.Date date) {
		GregorianCalendar d1= new GregorianCalendar();
		GregorianCalendar d2= new GregorianCalendar();
		d2.setTime(date);
		
		return (d1.get(Calendar.ERA)  == d2.get(Calendar.ERA) &&
				d1.get(Calendar.YEAR)  == d2.get(Calendar.YEAR) && 
				d1.get(Calendar.DAY_OF_YEAR)  == d2.get(Calendar.DAY_OF_YEAR)); 
	}

	
	public DailyDepartmentSchedule getScheduleFollowing(DailyDepartmentSchedule currentSchedule) {
	   boolean isSortAcending = true;
	   Collections.sort(schedules, new CompareDailyScheduleByPeriod(isSortAcending));
	   int pos = Collections.binarySearch(schedules,currentSchedule, new CompareDailyScheduleByPeriod(isSortAcending));
	   
	   // if current doesn't exist or there isnt a next schedule
	   if (pos <= 0 || (pos+1)  >= schedules.size()) {
		   return null;
	   } else {
		   return  schedules.get(pos+1);
	   }
	}
	
	
	public DailyDepartmentSchedule getFirstWorkPeriodStartTime(String shift) {
		return getWorkPeriodStartTime(shift,true);
	}
	
	public DailyDepartmentSchedule getLastWorkPeriodStartTime(String shift) {
		return getWorkPeriodStartTime(shift,false);
	}
	
	public DailyDepartmentSchedule getWorkPeriodStartTime(String shift,boolean isFirst) {
		
		List<DailyDepartmentSchedule> modifyableSchedule =  new ArrayList<DailyDepartmentSchedule>(schedules);
	
		Collections.sort(modifyableSchedule, new CompareDailyScheduleByPeriod(isFirst));
		for (DailyDepartmentSchedule schedule : modifyableSchedule) {
			if (schedule.getPlan().equals("Y") && schedule.getId().getShift().equals(shift)) {
				return schedule;
			}
		}
		return null;
	}
	
	public List<DailyDepartmentSchedule> getSchedules() {
		  return schedules;
    }
 
	public DailyDepartmentSchedule getShiftFirstLastSchedule(String shift, boolean first) {
		if (getSchedules() == null || getSchedules().isEmpty()) {
			return null;
		}
		List<DailyDepartmentSchedule> list = new ArrayList<DailyDepartmentSchedule>(getSchedules());
		Collections.sort(list, new CompareDailyScheduleByPeriod(first));
		for (DailyDepartmentSchedule schedule : list) {
			if (schedule.getId().getShift().equals(shift)) {
				return schedule;
			}
		}
		return null;
	}

	public Timestamp getShiftStartTimestamp(String shift) {
		DailyDepartmentSchedule schedule = getShiftFirstLastSchedule(shift, true);
		if (schedule == null) {
			return null;
		}
		return schedule.getStartTimestamp();
	}
	
	public Timestamp getShiftEndTimestamp(String shift) {
		DailyDepartmentSchedule schedule = getShiftFirstLastSchedule(shift, false);
		if (schedule == null) {
			return null;
		}
		return schedule.getEndTimestamp();
	}
	
	class CompareDailyScheduleByPeriod implements Comparator<DailyDepartmentSchedule> {
		
		private boolean isAscending;
		
		public CompareDailyScheduleByPeriod(boolean isAscending) {
			this.isAscending = isAscending;
		}
		
		public int compare(DailyDepartmentSchedule schedule1, DailyDepartmentSchedule schedule2) {
			if (isAscending) {
			   return schedule1.getId().getPeriod() - schedule2.getId().getPeriod();
			} else {
			   return schedule2.getId().getPeriod() - schedule1.getId().getPeriod();
			}
		}
	}
	
}
