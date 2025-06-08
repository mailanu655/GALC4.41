package com.honda.galc.oif.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.oif.dto.CapacityCalendarDTO;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>CapacityCalendarTask Class description</h3>
 * <p> CapacityCalendarTask description </p>
 * Capacity Calendar Task task is an OIF task
 * It retrieves data from file to set capacity. 
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
 * @since Dec 18, 2013
*/

public class CapacityCalendarTask extends BaseDailyDepartmentScheduleTask { 
	
	List<String> configuredPlanCodes = null;
	List<String> configuredDeptCodes = null;
	public CapacityCalendarTask(String name) {
		super(name);
	}
	
	protected void initialize()  {
		super.initialize();  //BaseDailDepartmentSchedule.initialize()
		String departmentSchedule = getProperty("PARSE_LINE_DEFS");
		simpleParseHelper = new OIFSimpleParsingHelper<CapacityCalendarDTO>(
				CapacityCalendarDTO.class, departmentSchedule, logger);
		simpleParseHelper.getParsingInfo();		
		configuredPlanCodes = getPlanCodesAsList();
		configuredDeptCodes = getDepartments();
	}
	
	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current line 
	 * <p>
	 */
	@Override
	protected void processDepartmentScheduleByPlanCode(String receivedFile) {
		logger.info("start to process CapacityCalendar");
//	Process file(s) and update CapacityCalendar data  
		DailyDepartmentScheduleDao dailyDeptSchedDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		List<String> receivedRecords = LoadRecordsFromFile(receivedFile);
		if (receivedRecords.isEmpty()) {
			return;
		}
		List<CapacityCalendarDTO> dailyCapacityList = new ArrayList<CapacityCalendarDTO>();
		for(String receivedRecord : receivedRecords) {
			CapacityCalendarDTO capacity = new CapacityCalendarDTO();
			simpleParseHelper.parseData(capacity, receivedRecord);
			String planCode = capacity.getPlanCode();
			String deptCode = capacity.getDepartmentCode().trim();
			//if plan codes are not configured, then line has to match
			if(!planCodesConfigured && !validateLine(capacity.getLineNo()))  {
				failedRecords++;
				continue;
			}
			if(StringUtils.isBlank(deptCode)) {
				//when dept code is not given...
				//if plan codes are configured, only validate plant/product-line
				if(planCodesConfigured && !isSameLine(planCode))  {
					failedRecords++;
					continue;
				}
				setCapacityPlanCode(capacity, dailyCapacityList);
			}
			else  {
				//if dept code is given..
				//if plan codes are configured, then full plan code has to match
				if(planCodesConfigured && !validatePlanCode(planCode))  {
					failedRecords++;
					continue;
				}
				dailyCapacityList.add(capacity);
			}
		}
		for(CapacityCalendarDTO capacity : dailyCapacityList) {
			List<DailyDepartmentSchedule> scheduleList = dailyDeptSchedDao.findAllByLineAndShift(capacity.getLineNo(), capacity.getProcessLocation(), capacity.getPlantCode(), capacity.getProductionDate(), capacity.getShift());
			processSchedule(capacity, scheduleList);
		}
		logger.info("file processed: " + receivedFile);
	}
	
	public void setCapacityPlanCode(CapacityCalendarDTO capacity, List<CapacityCalendarDTO> dailyCapacityList)
	{
		if(capacity == null) return;
		if(planCodesConfigured)  {
			List<String> subset = getPlanCodesForPlantLine(capacity.getPlanCode());
			for(String eachPlanCode : subset) {
				if(StringUtils.isBlank(eachPlanCode)) continue;
				CapacityCalendarDTO capacityCopy = new CapacityCalendarDTO(capacity);
				if(eachPlanCode.length() >= 9)  {
					String departmentCode = eachPlanCode.substring(7, 9);
					capacityCopy.setDepartmentCode(departmentCode);
				}
				capacityCopy.setPlanCode(eachPlanCode);
				dailyCapacityList.add(capacityCopy);
			}			
		}
		else  {
			for(String eachDept : configuredDeptCodes) {
				if(StringUtils.isBlank(eachDept)) continue;
				CapacityCalendarDTO capacityCopy = new CapacityCalendarDTO(capacity);
				capacityCopy.setDepartmentCode(eachDept);
				dailyCapacityList.add(capacityCopy);
			}						
		}
	}
	
	private void processSchedule(CapacityCalendarDTO capacity, List<DailyDepartmentSchedule> scheduleList)  {
		if(scheduleList.size() == 0) {
			if("01".equals(capacity.getShift())) {
				logger.error("Unable to find period count for production record ");
			} else if("02".equals(capacity.getShift())) {
				String dayOfWeek = capacity.getDayOfWeek();
				if("FRI".equals(dayOfWeek) || "SAT".equals(dayOfWeek) || "SUN".equals(dayOfWeek)) {
					logger.error("Second shift calendar not setup for normal weekday production.  Please verify with CP this is correct.");
				}
			} else if("03".equals(capacity.getShift())) {
				logger.info();
			}
		}
		long totalTime = 0L;
		int lastPeriod = 0; 
		DailyDepartmentSchedule lastSchedule = null;
		for(DailyDepartmentSchedule schedule : scheduleList) {
			if(schedule.getPlan().equals("Y")) {
				totalTime += schedule.getEndTimestamp().getTime() - schedule.getStartTimestamp().getTime();
				int currPeriod = schedule.getId().getPeriod();
				if(lastPeriod < currPeriod) {
					lastPeriod = currPeriod;
					lastSchedule = schedule;
				}
			}
		}
		logger.info("totalTime: " + totalTime + " : " + capacity.getPlantCode() + " : " + capacity.getProductionDate() + " : " + capacity.getProcessLocation() + " : " + capacity.getShift());
		int totalCapacity = 0;
		for(DailyDepartmentSchedule schedule : scheduleList) {
			if(schedule.getPlan().equals("Y")) {
				long currTime = schedule.getEndTimestamp().getTime() - schedule.getStartTimestamp().getTime();
				float ratio = (float)currTime / (float)totalTime;
				int currCapacity = Math.round(ratio * capacity.getCapacity());
				totalCapacity += currCapacity;
				if(lastSchedule == schedule) {
//	To avoid rounding problems adjust the very last capacity
					int adjustment = capacity.getCapacity() - totalCapacity;
					if(adjustment != 0) {
						currCapacity += adjustment;
					}
				}
				if("1".equals(capacity.getOnOffFlag())) {
					schedule.setCapacityOn(currCapacity);
				} else {
					schedule.setCapacity(currCapacity);
				}
			}
			schedule.setDayOfWeek(capacity.getDayOfWeek());
			schedule.setIswork(capacity.getIsWork());
			schedule.setWeekNo(capacity.getWeekNo());
			logger.info(schedule.toString());
			dailyDeptSchedDao.update(schedule);
		}

	}
	
}