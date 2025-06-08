package com.honda.galc.service.msip.handler.inbound;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.service.msip.dto.inbound.Gpp303Dto;
import com.honda.galc.service.msip.property.inbound.BaseMsipCalendarPropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Gpp303Handler extends ScheduleBasedMsipHandler<BaseMsipCalendarPropertyBean, Gpp303Dto> {
	
	public Gpp303Handler() {}

	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current plan codes 
	 * <p>
	 */
	protected void processDepartmentScheduleByPlanCode(List<Gpp303Dto> dtoList) {
		
		Map<String, ArrayList<Gpp303Dto>> schedulesMap = getSchedulesByPlanCode(dtoList);
		
		for(String planCode : schedulesMap.keySet()) {
			
			DailyDepartmentSchedule prevDailyDeptSched = new DailyDepartmentSchedule();
			DailyDepartmentScheduleId prevDailyDeptSchedId = null;
			List<DailyDepartmentSchedule> dailyDeptSchedList = new ArrayList<DailyDepartmentSchedule>();
			int isNextDay = 0;
			Date currentProductionDate = null;
			Date previousProductionDate = null;
			boolean sameSet = false;
			boolean firstLine = true;
			String currentProcessLocation = null;
			List<Gpp303Dto> scheduleListByPlanCode = schedulesMap.get(planCode);
			for (Gpp303Dto gpp303Dto : scheduleListByPlanCode) {
				DailyDepartmentSchedule dailyDeptSched = deriveDailyDepartmentSchedule(gpp303Dto);
				if (dailyDeptSched == null) continue;
				DailyDepartmentScheduleId dailyDeptSchedId = dailyDeptSched.getId();
				if(prevDailyDeptSched != null)   {
					prevDailyDeptSchedId = prevDailyDeptSched.getId();
				}
				//	process location or production date changes or period changes to smaller number
				isNextDay = calculateNextDay(isNextDay, prevDailyDeptSched, dailyDeptSched);
				if(firstLine) {
					firstLine = false;
				} else {
					currentProcessLocation = dailyDeptSchedId.getProcessLocation();
					currentProductionDate = dailyDeptSchedId.getProductionDate();
					previousProductionDate = prevDailyDeptSchedId.getProductionDate();
					sameSet = currentProductionDate.equals(prevDailyDeptSchedId.getProductionDate()); 
					if(!sameSet) {
						List<DailyDepartmentSchedule> scheduleList = 
								getDeptSchedDao().getAllByProductionDateAndLocation(previousProductionDate, dailyDeptSchedId.getProcessLocation());
						processSchedules(scheduleList, dailyDeptSchedList); // production date changed save schedules for current plan code
						dailyDeptSchedList = new ArrayList<DailyDepartmentSchedule>();
					}
				}
				setSchedTimestamps(isNextDay, dailyDeptSched);
				Date prodDate = dailyDeptSchedId.getProductionDate();
				dailyDeptSched.setDayOfWeek((new SimpleDateFormat("EEE")).format(prodDate).toUpperCase());
				dailyDeptSchedList.add(dailyDeptSched);
				prevDailyDeptSched = dailyDeptSched;
			}
			if(!firstLine) {
				List<DailyDepartmentSchedule> scheduleList = 
						getDeptSchedDao().getAllByProductionDateAndLocation(currentProductionDate, currentProcessLocation);
				processSchedules(scheduleList, dailyDeptSchedList);
			}
			getLogger().info(String.format("File processed for plan code %s", planCode));
		}
	}
	
	public boolean execute(List<Gpp303Dto> dtoList) {
		try {
			processDepartmentScheduleByPlanCode(dtoList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	public DailyDepartmentSchedule deriveDailyDepartmentSchedule(Gpp303Dto dto) {
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
			dailyDepartmentSchedule.setEndTime(sTime);
			dailyDepartmentSchedule.setStartTime(eTime);
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
