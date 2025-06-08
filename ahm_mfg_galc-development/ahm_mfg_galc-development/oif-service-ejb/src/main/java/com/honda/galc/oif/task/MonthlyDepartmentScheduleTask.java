package com.honda.galc.oif.task;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>MonthlyDepartmentScheduleTask Class description</h3>
 * <p> MonthlyDepartmentScheduleTask description </p>
 * Monthly Department Schedule Task task is an OIF task
 * It retrieves data from file to set template for capacity. 
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
 * @author Larry Karpov 
 * @since Nov 05, 2013
 */

public class MonthlyDepartmentScheduleTask extends BaseDailyDepartmentScheduleTask {

	public MonthlyDepartmentScheduleTask(String name) {
		super(name);
	}

	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current plan codes 
	 * <p>
	 */
	protected void processDepartmentScheduleByPlanCode(String receivedFile) {
		Map<String, ArrayList<DailyDepartmentSchedule>> schedulesMap = getSchedulesByPlanCode(receivedFile);
		for(String planCode : schedulesMap.keySet()) {
			DailyDepartmentSchedule prevDailyDeptSched = new DailyDepartmentSchedule();
			List<DailyDepartmentSchedule> dailyDeptSchedList = new ArrayList<DailyDepartmentSchedule>();
			int isNextDay = 0;
			List<DailyDepartmentSchedule> listByPlanCode = schedulesMap.get(planCode);
			for (DailyDepartmentSchedule dailyDeptSched : listByPlanCode) {
				if (dailyDeptSched == null) continue;
				if(!validateSite(dailyDeptSched.getId().getPlantCode())) {
					failedRecords++;
					continue;
				}
				isNextDay = calculateNextDay(isNextDay, prevDailyDeptSched, dailyDeptSched);
				dailyDeptSchedList.add(dailyDeptSched);
				prevDailyDeptSched = dailyDeptSched;
			}

			Calendar todayDate = getTodayDate();
			List<DailyDepartmentSchedule> dailyDeptSchedMonthList = buildDailyDeptSchedMonthList(dailyDeptSchedList, todayDate);

			// Get a list of existing objects with future production date  			
			DailyDepartmentScheduleDao dailyDeptSchedDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
			List<DailyDepartmentSchedule> futureDailyDeptSchedList =
					dailyDeptSchedDao.getAllFutureByPlanCodeAndProductionDate(planCode, new Date(todayDate.getTimeInMillis()));
			processSchedules(futureDailyDeptSchedList, dailyDeptSchedMonthList);
			logger.info(String.format("File %s processed for plan code %s", receivedFile, planCode));
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
						logger.debug("Processing: " + copy.toString());
					}
				}
			} else {
				failedRecords++;
				logger.error("dailyDeptSchedId was not found: " + dailyDeptSchedId);
				errorsCollector.error("dailyDeptSchedId was not found: " + dailyDeptSchedId);
			}
		}

		return dailyDeptSchedMonthList;
	}

	/**
	 * Sort objects from received file by:<br>
	 * PlantCode, LineNo, ProcessLocation, ProductionDate, Period
	 */
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
		//Have to set the target date as production_date as GPCS sents only effective date for the schedule not the Production_date
		copyId.setProductionDate(targetDate);
			
		DailyDepartmentSchedule copy = new DailyDepartmentSchedule();
		copy.setCapacity(dailyDeptSched.getCapacity());
		copy.setCapacityOn(dailyDeptSched.getCapacityOn());
		copy.setDayOfWeek(dailyDeptSched.getDayOfWeek());
		copy.setStartTime(dailyDeptSched.getStartTime());
		copy.setEndTime(dailyDeptSched.getEndTime());
		copy.setNextDay(dailyDeptSched.getNextDay());
		//set start and end timestamp as trigger to update these column removed from 2.46
		copy.setPeriodLabel(dailyDeptSched.getPeriodLabel());
		copy.setPlan(dailyDeptSched.getPlan());
		copy.setPlanCode(dailyDeptSched.getPlanCode());
		copy.setType(dailyDeptSched.getType());
		copy.setWeekNo(dailyDeptSched.getWeekNo());
		copy.setIswork(dailyDeptSched.getIswork());
		copy.setStartTimestamp(setStartTimestamp(copy.getStartTime(),copy.getEndTime(),copyId.getProductionDate(),copy.getNextDay()));
		Timestamp endTimestamp = setEndTimestamp(copy.getEndTime(),copyId.getProductionDate(),copy.getNextDay());
		endTimestamp.setNanos(999999000);
		copy.setEndTimestamp(endTimestamp);
		
		if(copy != null) {
			copy.setId(copyId);
		}
	
		return copy;
	}
	
	
}