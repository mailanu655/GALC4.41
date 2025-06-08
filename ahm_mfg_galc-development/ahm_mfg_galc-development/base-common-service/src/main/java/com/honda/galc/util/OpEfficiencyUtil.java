package com.honda.galc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.honda.galc.entity.product.DailyDepartmentSchedule;


public class OpEfficiencyUtil {
	public static long calculateNonWorkTime(List<DailyDepartmentSchedule> nonWorkScheduleList, Date unitStartTime, Date unitEndTime) {
		long nonWorkTime = 0;
	    for(DailyDepartmentSchedule nonWorkSchedule: nonWorkScheduleList) {
	    	Date nonWorkStartDate = getDateFromSqlDateAndTime(nonWorkSchedule.getId().getProductionDate(), nonWorkSchedule.getStartTime());
	    	Date nonWorkEndDate = getDateFromSqlDateAndTime(nonWorkSchedule.getId().getProductionDate(), nonWorkSchedule.getEndTime());
	    	
	    	if(unitStartTime.compareTo(nonWorkStartDate)>=0 && unitStartTime.compareTo(nonWorkEndDate)<=0
	    			&& unitEndTime.compareTo(nonWorkEndDate)>0) {
	    		//Unit_Start_Time is in between Non_Work_Start_Time and Non_Work_End_Time, 
	    		//AND Unit_End_Time > Non_Work_End_Time THEN Non_Work_Start_Time  = Unit_Start_Time
	    		nonWorkTime += TimeUnit.MILLISECONDS.toSeconds(nonWorkEndDate.getTime() - unitStartTime.getTime());
	    	}
	    	else if(nonWorkStartDate.compareTo(unitStartTime)>0 && nonWorkStartDate.compareTo(unitEndTime)<0
	    			&& nonWorkEndDate.compareTo(unitStartTime)>0 && nonWorkEndDate.compareTo(unitEndTime)<0) {
	    		//Non_Work_Start_Time is in between Unit_Start_Time & Unit_End_Time,
	    		//AND Non_Work_End_Time is in between Unit_Start_Time & Unit_End_Time THEN get entire break time
	    		nonWorkTime += TimeUnit.MILLISECONDS.toSeconds(nonWorkEndDate.getTime() - nonWorkStartDate.getTime());
	    	}
	    }
	    return nonWorkTime;
	}
	
	public static Date getDateFromSqlDateAndTime(java.sql.Date sqlDate, java.sql.Time sqlTime) {
		Calendar sqlDateCal = Calendar.getInstance();
		sqlDateCal.setTime(sqlDate);
		Calendar sqlTimeCal = Calendar.getInstance();
		sqlTimeCal.setTime(sqlTime);
		sqlDateCal.set(Calendar.HOUR_OF_DAY, sqlTimeCal.get(Calendar.HOUR_OF_DAY));
		sqlDateCal.set(Calendar.MINUTE, sqlTimeCal.get(Calendar.MINUTE));
		sqlDateCal.set(Calendar.SECOND, sqlTimeCal.get(Calendar.SECOND));
		return sqlDateCal.getTime();
	    
	}
	
	public static boolean compareYearMonthDate(Date startTime, Date endTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String unitStartDate = formatter.format(startTime);
		String unitEndDate = formatter.format(endTime);
		if(unitStartDate.equalsIgnoreCase(unitEndDate)) {
			return true;
		}
		return false;
	}
	
	public static List<java.sql.Date> getAllDates(Date startDate, Date endDate) {
		List<java.sql.Date> dates = new ArrayList<java.sql.Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
		while (cal.getTime().compareTo(endDate)<=0) {
			dates.add(new java.sql.Date(cal.getTime().getTime()));
		    cal.add(Calendar.DATE, 1);
		}
		return dates;
	}
	
	public static double getTotalNonWorkTimeInSeconds(List<DailyDepartmentSchedule> nonWorkScheduleList, Date tillDate) {
		double nonWorkTime = 0.0;
		if(nonWorkScheduleList!=null) {
			for (DailyDepartmentSchedule nonWorkSchedule: nonWorkScheduleList) {
				Date nonWorkStartDate = getDateFromSqlDateAndTime(nonWorkSchedule.getId().getProductionDate(), nonWorkSchedule.getStartTime());
		    	Date nonWorkEndDate = getDateFromSqlDateAndTime(nonWorkSchedule.getId().getProductionDate(), nonWorkSchedule.getEndTime());
		    	if(nonWorkStartDate.before(tillDate)) {
		    		//Date is within this slot
		    		if(nonWorkEndDate.after(tillDate)) {
		    			//End time is after now
		    			nonWorkTime += TimeUnit.MILLISECONDS.toSeconds(tillDate.getTime() - nonWorkStartDate.getTime());
		    		}
		    		else {
		    			nonWorkTime += TimeUnit.MILLISECONDS.toSeconds(nonWorkEndDate.getTime() - nonWorkStartDate.getTime());
		    		}
		    	}
			}
		}
		return nonWorkTime;
	}
	
	public static double getTotalTimeInSeconds(List<DailyDepartmentSchedule> nonWorkScheduleList) {
		Date now = new Date(System.currentTimeMillis());
		double nonWorkTime = getTotalNonWorkTimeInSeconds(nonWorkScheduleList, now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    double secondsPassed = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - cal.getTimeInMillis());
	    return (secondsPassed - nonWorkTime);
	}
}
