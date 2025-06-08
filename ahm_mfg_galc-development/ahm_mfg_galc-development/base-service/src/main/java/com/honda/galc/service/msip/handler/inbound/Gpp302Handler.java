package com.honda.galc.service.msip.handler.inbound;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.service.msip.dto.inbound.Gpp302Dto;
import com.honda.galc.service.msip.property.inbound.BaseMsipCalendarPropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Gpp302Handler extends ScheduleBasedMsipHandler<BaseMsipCalendarPropertyBean, Gpp302Dto> {
	
	protected List<Gpp302Dto> dailyDeptScheduleList = null;
	
	public Gpp302Handler() {}

	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current plan codes 
	 * <p>
	 */
	protected void processDepartmentScheduleByPlanCode(List<Gpp302Dto> dtoList) {
		Map<String, ArrayList<Gpp302Dto>> schedulesMap = getSchedulesByPlanCode(dtoList);
		for(String planCode : schedulesMap.keySet()) {
			DailyDepartmentSchedule prevDailyDeptSched = new DailyDepartmentSchedule();
			List<DailyDepartmentSchedule> dailyDeptSchedList = new ArrayList<DailyDepartmentSchedule>();
			int isNextDay = 0;
			
			List<Gpp302Dto> listByPlanCode = schedulesMap.get(planCode);
			
			for (Gpp302Dto gpp302Dto : listByPlanCode) {
				DailyDepartmentSchedule dailyDeptSched = deriveDailyDepartmentSchedule(gpp302Dto);
				if (dailyDeptSched == null) continue;
				if(!validateSite(dailyDeptSched.getId().getPlantCode()))  continue;
				isNextDay = calculateNextDay(isNextDay, prevDailyDeptSched, dailyDeptSched);
				setSchedTimestamps(isNextDay, dailyDeptSched);
				dailyDeptSchedList.add(dailyDeptSched);
				prevDailyDeptSched = dailyDeptSched;
			}

			Calendar todayDate = getTodayDate();
			List<DailyDepartmentSchedule> dailyDeptSchedMonthList = buildDailyDeptSchedMonthList(dailyDeptSchedList, todayDate);

			// Get a list of existing objects with future production date  			
			List<DailyDepartmentSchedule> futureDailyDeptSchedList =
					getDeptSchedDao().getAllFutureByPlanCodeAndProductionDate(planCode, new Date(todayDate.getTimeInMillis()));
			processSchedules(futureDailyDeptSchedList, dailyDeptSchedMonthList);
			getLogger().info(String.format("Data processed for plan code %s", planCode));
		}
	}

	private static Calendar getTodayDate() {
		Calendar todayDate = Calendar.getInstance();
		todayDate.setTime(new java.util.Date(System.currentTimeMillis()));
		todayDate.set(Calendar.HOUR_OF_DAY, 0);
		todayDate.set(Calendar.MINUTE, 0);
		todayDate.set(Calendar.SECOND, 0);
		todayDate.set(Calendar.MILLISECOND, 0);
		return todayDate;
	}

	private List<DailyDepartmentSchedule> buildDailyDeptSchedMonthList(List<DailyDepartmentSchedule> dailyDeptSchedList, Calendar todayDate) {
		List<DailyDepartmentSchedule> dailyDeptSchedMonthList = new ArrayList<DailyDepartmentSchedule>();

		sortDailyDeptSchedList(dailyDeptSchedList);
		for(DailyDepartmentSchedule dailyDeptSched : dailyDeptSchedList) {
			DailyDepartmentScheduleId dailyDeptSchedId = dailyDeptSched.getId();
			if(dailyDeptSchedId != null) {
				Calendar targetDate = (Calendar) todayDate.clone();
				Date effectiveDate = dailyDeptSchedId.getProductionDate();
				for(int i=0; i<4; i++) {
					if(i==0) {
						targetDate.add(Calendar.DATE, getDaysDiff(dailyDeptSched.getDayOfWeek()));
					} else {
						targetDate.add(Calendar.DATE, 7);
					}
					Date tDate = new Date(targetDate.getTimeInMillis());
					if(!effectiveDate.after(tDate)) {
						DailyDepartmentSchedule copy = copyEntity(
								tDate, dailyDeptSchedId, dailyDeptSched);
						dailyDeptSchedMonthList.add(copy);
						getLogger().debug("Processing: " + copy.toString());
					}
				}
			} else {
				getLogger().error("dailyDeptSchedId was not found: " + dailyDeptSchedId);
			}
		}

		return dailyDeptSchedMonthList;
	}


	private void sortDailyDeptSchedList(List<DailyDepartmentSchedule> dailyDeptSchedList) {
		Collections.sort(dailyDeptSchedList, new Comparator<DailyDepartmentSchedule>() {
			public int compare(DailyDepartmentSchedule s1, DailyDepartmentSchedule s2) {
				DailyDepartmentScheduleId sId1 = s1.getId(); 
				DailyDepartmentScheduleId sId2 = s2.getId();
				int result = 0;
				result = sId1.getPlantCode().compareTo(sId2.getPlantCode());
				if(0 == result) {
					result = sId1.getLineNo().compareTo(sId2.getLineNo());
					if(0 == result) {
						result = sId1.getProcessLocation().compareTo(sId2.getProcessLocation());
						if(0 == result) {
							Date sDate1 = sId1.getProductionDate();
							Date sDate2 = sId2.getProductionDate();
							result = sDate1.before(sDate2) ? -1 : sDate1.after(sDate2) ? 1 : 0;
							if(0 == result) {
								result = sId1.getPeriod() - sId2.getPeriod();
							}
						}
					}
				}
				return result;
			}
		});
	}

	// next production date based on the Day of Week from incoming data
	private int getDaysDiff(String dayOfWeek) {
		// get all US week days
		String[] weekdaysUS = (new DateFormatSymbols(Locale.US)).getShortWeekdays();
		Calendar tDate = Calendar.getInstance();
		tDate.setTime(new java.util.Date(System.currentTimeMillis()));
		int dayIndex = 1;
		while (dayIndex < weekdaysUS.length) {
			if (dayOfWeek.compareTo(weekdaysUS[dayIndex].toUpperCase(Locale.US)) == 0) {
				break;
			}
			dayIndex++;
		}
		int daysDifference = dayIndex - tDate.get(Calendar.DAY_OF_WEEK);
		return daysDifference >= 0 ? daysDifference : daysDifference + 7;
	}

	// Could not find a better way to copy objects.
	// I guess I need detach method of Entity Manager 
	// which is not avail prior to JPA 2
	private DailyDepartmentSchedule copyEntity(
			Date targetDate, DailyDepartmentScheduleId dailyDeptSchedId, DailyDepartmentSchedule dailyDeptSched) {
		DailyDepartmentScheduleId copyId = new DailyDepartmentScheduleId();
		copyId.setLineNo(dailyDeptSchedId.getLineNo());
		copyId.setPeriod(dailyDeptSchedId.getPeriod());
		copyId.setPlantCode(dailyDeptSchedId.getPlantCode());
		copyId.setProcessLocation(dailyDeptSchedId.getProcessLocation());
		copyId.setShift(dailyDeptSchedId.getShift());
		DailyDepartmentSchedule copy = new DailyDepartmentSchedule();
		copy.setCapacity(dailyDeptSched.getCapacity());
		copy.setCapacityOn(dailyDeptSched.getCapacityOn());
		copy.setDayOfWeek(dailyDeptSched.getDayOfWeek());
		copy.setStartTime(dailyDeptSched.getStartTime());
		copy.setEndTime(dailyDeptSched.getEndTime());
		copy.setNextDay(dailyDeptSched.getNextDay());
		copy.setPeriodLabel(dailyDeptSched.getPeriodLabel());
		copy.setPlan(dailyDeptSched.getPlan());
		copy.setPlanCode(dailyDeptSched.getPlanCode());
		copy.setType(dailyDeptSched.getType());
		copy.setWeekNo(dailyDeptSched.getWeekNo());
		copy.setIswork(dailyDeptSched.getIswork());
		if(copyId != null) {
			copyId.setProductionDate(targetDate);
			if(copy != null) {
				copy.setId(copyId);
			}
		}
		return copy;
	}
	
	public boolean execute(List<Gpp302Dto> dtoList) {
		dailyDeptScheduleList = new ArrayList<Gpp302Dto>();
		try {
				processDepartmentScheduleByPlanCode(dtoList);
		} catch (Exception ex) {
			getLogger().error("Unable to process GPP302 request");
			return false;
		}
		return true;
	}
	
	
	public DailyDepartmentSchedule deriveDailyDepartmentSchedule(Gpp302Dto dto) {
		DailyDepartmentSchedule dailyDepartmentSchedule = new DailyDepartmentSchedule();
		DailyDepartmentScheduleId id = new DailyDepartmentScheduleId();
		Date sqlDate;
		Time sTime, eTime;
		try {
			sqlDate = new java.sql.Date(MsipUtil.sdf1.parse(dto.getProductionDate()).getTime());
			sTime = new java.sql.Time(MsipUtil.stf1.parse(dto.getStartTime()).getTime()); 
			eTime = new java.sql.Time(MsipUtil.stf1.parse(dto.getEndTime()).getTime()); 
			id.setLineNo(dto.getLineNo());
			id.setPeriod(dto.getPeriod());
			id.setPlantCode(dto.getPlantCode());
			id.setProcessLocation(dto.getProcessLocation());
			id.setProductionDate(sqlDate);
			id.setShift(dto.getShift());
			
			dailyDepartmentSchedule.setId(id);
			dailyDepartmentSchedule.setDayOfWeek(dto.getDayOfWeek());
			dailyDepartmentSchedule.setEndTime(eTime);
			dailyDepartmentSchedule.setStartTime(sTime);
			dailyDepartmentSchedule.setNextDay(getNextDay(dto.getNextDay()));
			dailyDepartmentSchedule.setPeriodLabel(dto.getPeriodLabel());
			dailyDepartmentSchedule.setPlan(dto.getPlan());
			dailyDepartmentSchedule.setType(dto.getType());
			dailyDepartmentSchedule.setPlanCode(dto.getPlanCode());
		} catch (ParseException e) {
			e.printStackTrace();
			getLogger().error("Unable to parse GPP302 request");
		} 
		return dailyDepartmentSchedule;
	}
}
