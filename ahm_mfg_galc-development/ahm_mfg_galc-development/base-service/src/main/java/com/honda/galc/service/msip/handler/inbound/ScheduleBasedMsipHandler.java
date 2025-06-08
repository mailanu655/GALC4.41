package com.honda.galc.service.msip.handler.inbound;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.msip.dto.inbound.IScheduleDto;
import com.honda.galc.service.msip.handler.inbound.PlanCodeBasedMsipHandler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public abstract class ScheduleBasedMsipHandler<T extends BaseMsipPropertyBean, D extends IScheduleDto> extends PlanCodeBasedMsipHandler<T, D> {
	boolean planCodesConfigured = false;

	protected Map<String, ArrayList<D>> getSchedulesByPlanCode(List<D> recordsList) {
		Map<String, ArrayList<D>> schedulesByPlanCodeMap = new HashMap<String, ArrayList<D>>();

		for(D dailyDeptSched : recordsList) {
			if(planCodesConfigured && !validatePlanCode(dailyDeptSched.getPlanCode()))  {
				continue;
			}
			else if(!planCodesConfigured && !validateLine(dailyDeptSched.getLineNo()))  {
				continue;
			}
			if(!schedulesByPlanCodeMap.containsKey(dailyDeptSched.getPlanCode().trim()))
				schedulesByPlanCodeMap.put(dailyDeptSched.getPlanCode().trim(), new ArrayList<D>());
			schedulesByPlanCodeMap.get(dailyDeptSched.getPlanCode().trim()).add(dailyDeptSched);
		}

		if(schedulesByPlanCodeMap.isEmpty()) {
			String message = "";
			if(planCodesConfigured)  {
				message = String.format("Records matching current plan code(s): %s not found in List",  getPropertyBean().getPlanCodes().toString());				
			}
			else  {
				message = String.format("No schedules found for line id: %s", getPropertyBean().getLineNo());
			}
			getLogger().debug(message);
		}

		return schedulesByPlanCodeMap;
	}
	
	protected boolean isPlansCodeConfigured(){
		String[] planCodes = getPropertyBean().getPlanCodes();
		if (planCodes == null) {
			planCodesConfigured = false;
		}
		else if (planCodes.length == 0) {
			planCodesConfigured = false;
		}
		else {
			planCodesConfigured = true;
		}
		return planCodesConfigured;
	}
	
	protected boolean validateLine(String lineNo)  {
		if(lineNo == null)  return false;
		else if(!lineNo.trim().equalsIgnoreCase(getPropertyBean().getLineNo())) {
			getLogger().debug("Not current line: " + lineNo + ", skipping record");
			return false;
		}
		return true;
	}
	
	protected boolean validateSite(String site)  {
		if(site == null)  return false;
		else if(!site.trim().equalsIgnoreCase(getPropertyBean().getSiteName())) {
			getLogger().debug("Not current site: " + site + ", skipping record");
			return false;
		}
		return true;
	}
	
	/**
	 * This section checks if a record should be kept as next day.<br>
	 * It starts from the record where next day = 1 and goes until<br>
	 * process location or production date changes or period changes to smaller number
	 */
	protected int calculateNextDay(int isNextDay, DailyDepartmentSchedule prevDailyDeptSched, DailyDepartmentSchedule dailyDeptSched) {
		if(prevDailyDeptSched != null && prevDailyDeptSched.getId() != null && dailyDeptSched.getId() != null) {
			if(dailyDeptSched.getNextDay() == 1) {
				isNextDay = 1;
			}
			if(dailyDeptSched.getId().getProcessLocation().equalsIgnoreCase(prevDailyDeptSched.getId().getProcessLocation())
					&& dailyDeptSched.getId().getProductionDate().equals(prevDailyDeptSched.getId().getProductionDate())
					&& dailyDeptSched.getId().getPeriod() >= prevDailyDeptSched.getId().getPeriod()
					&& dailyDeptSched.getId().getPeriod() > 1) {
				if(1 == isNextDay && 1 != dailyDeptSched.getNextDay()) {
					dailyDeptSched.setNextDay((short) 1);
				}
			} else {
				isNextDay = 0;
			}
		}
		return isNextDay;
	}
	
	public short getNextDay(String strValue){
		short value = 0;
		if("Y".equalsIgnoreCase(strValue)) {
			value = (short)1; 
		} else if("N".equalsIgnoreCase(strValue)) {
			value = (short)0;
		} else {
			try {
				value = Short.parseShort(strValue);
			} catch (NumberFormatException ne) {
				getLogger().error(ne, "Cann't parse short value from: " + strValue);
			}
		}
		return value;
	}
	
	protected void setSchedTimestamps(int isNextDay, DailyDepartmentSchedule dailyDeptSched) {
		Date prodDate = dailyDeptSched.getId().getProductionDate();
		Timestamp startTimestamp = MsipUtil.getTimestamp(prodDate, dailyDeptSched.getStartTime(), isNextDay);
		dailyDeptSched.setStartTimestamp(startTimestamp);
		Timestamp endTimestamp = MsipUtil.getTimestamp(prodDate, dailyDeptSched.getEndTime(), isNextDay);
		endTimestamp.setNanos(999999000);
		dailyDeptSched.setEndTimestamp(endTimestamp);
	}
	
	protected void processSchedules(List<DailyDepartmentSchedule> currentDailyDeptSchedList, 
			List<DailyDepartmentSchedule> newDailyDeptSchedList) {
//	Delete future schedules
		for(DailyDepartmentSchedule currentDailyDeptSched : currentDailyDeptSchedList) {
			getDeptSchedDao().remove(currentDailyDeptSched);
			getLogger().debug("Remove Schedule: " + currentDailyDeptSched.toString());
		}
		for(DailyDepartmentSchedule dailyDeptSched : newDailyDeptSchedList) {
			getDeptSchedDao().save(dailyDeptSched);
			getLogger().info("Merge schedule: " + dailyDeptSched.toString());
		}
	}
	
    public DailyDepartmentScheduleDao getDeptSchedDao() {
    	return ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
    }
} 
