package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gpp301Dto;
import com.honda.galc.service.msip.property.inbound.BaseMsipCalendarPropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Gpp301Handler extends BaseMsipCalendarHandler<BaseMsipCalendarPropertyBean, Gpp301Dto> {
	
	protected List<Gpp301Dto> dailyCapacityList = null;
	
	public Gpp301Handler() {}

	public void processDepartmentScheduleByPlanCode(Gpp301Dto capacityCalendarDto) {
		
//		Process file(s) and update CapacityCalendar data  
			DailyDepartmentScheduleDao dailyDeptSchedDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
			String planCode = capacityCalendarDto.getPlanCode();
			String deptCode = capacityCalendarDto.getDepartmentCode().trim();
			//if plan codes are not configured, then line has to match
			if(!planCodesConfigured && !MsipUtil.validateLine(capacityCalendarDto.getLineNo(), getPropertyBean().getLineNo()))  {
				return;
			}
			if(StringUtils.isBlank(deptCode)) {
				//when dept code is not given...
				//if plan codes are configured, only validate plant/product-line
				if(isPlansCodeConfigured() && !MsipUtil.isSameLine(planCode, getPlanCodesForPlantLine(planCode)))  {
					return;
				}
				setCapacityPlanCode(capacityCalendarDto, dailyCapacityList);
			}
			else  {
				//if dept code is given..
				//if plan codes are configured, then full plan code has to match
				if(planCodesConfigured && !MsipUtil.validatePlanCode(planCode, getPropertyBean().getPlanCodes()))  {
					return;
				}
				dailyCapacityList.add(capacityCalendarDto);
			}
			List<DailyDepartmentSchedule> scheduleList = dailyDeptSchedDao.findAllByLineAndShift(
					capacityCalendarDto.getLineNo(), capacityCalendarDto.getProcessLocation(), capacityCalendarDto.getPlantCode(), 
					capacityCalendarDto.getProductionDate(), capacityCalendarDto.getShift());
			processSchedule(capacityCalendarDto, scheduleList);
	}
	

	public void setCapacityPlanCode(Gpp301Dto capacity, List<Gpp301Dto> dailyCapacityList)
	{
		if(capacity == null) return;
		if(getPropertyBean().getPlanCodes()!=null && getPropertyBean().getPlanCodes().length != 0)  {
			List<String> subset = getPlanCodesForPlantLine(capacity.getPlanCode(), getPropertyBean().getPlanCodes());
			for(String eachPlanCode : subset) {
				if(StringUtils.isBlank(eachPlanCode)) continue;
				Gpp301Dto capacityCopy = new Gpp301Dto(capacity);
				if(eachPlanCode.length() >= 9)  {
					String departmentCode = eachPlanCode.substring(7, 9);
					capacityCopy.setDepartmentCode(departmentCode);
				}
				capacityCopy.setPlanCode(eachPlanCode);
				dailyCapacityList.add(capacityCopy);
			}			
		}
		else  {
			for(String eachDept : getPropertyBean().getDepartments()) {
				if(StringUtils.isBlank(eachDept)) continue;
				Gpp301Dto capacityCopy = new Gpp301Dto(capacity);
				capacityCopy.setDepartmentCode(eachDept);
				dailyCapacityList.add(capacityCopy);
			}						
		}
	}
	
	private void processSchedule(Gpp301Dto capacity, List<DailyDepartmentSchedule> scheduleList)  {
		if(scheduleList.size() == 0) {
			if("01".equals(capacity.getShift())) {
				getLogger().error("Unable to find period count for production record ");
			} else if("02".equals(capacity.getShift())) {
				String dayOfWeek = capacity.getDayOfWeek();
				if("FRI".equals(dayOfWeek) || "SAT".equals(dayOfWeek) || "SUN".equals(dayOfWeek)) {
					getLogger().error("Second shift calendar not setup for normal weekday production.  Please verify with CP this is correct.");
				}
			} else if("03".equals(capacity.getShift())) {
				//logger.info();
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
		getLogger().info("totalTime: " + totalTime + " : " + capacity.getPlantCode() + " : " + capacity.getProductionDate() + " : " + capacity.getProcessLocation() + " : " + capacity.getShift());
		updateSchedule(scheduleList, capacity, lastSchedule, totalTime);

	}
	
	public void updateSchedule(List<DailyDepartmentSchedule> scheduleList, Gpp301Dto capacity,
			DailyDepartmentSchedule lastSchedule, long totalTime ){
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
			getLogger().info(schedule.toString());
			getDao(DailyDepartmentScheduleDao.class).update(schedule);
		}
	}
	
	public boolean execute(List<Gpp301Dto> dtoList) {
		dailyCapacityList = new ArrayList<Gpp301Dto>();
		try {
			for(Gpp301Dto capacityCalendarDto : dtoList) {
				processDepartmentScheduleByPlanCode(capacityCalendarDto);
			}
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
