package com.honda.galc.oif.task;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;

/**
 * 
 * <h3>DailyDepartmentScheduleTask Class description</h3>
 * <p> DailyDepartmentScheduleTask description </p>
 * Daily Department Schedule Task task is an OIF task
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

public class DailyDepartmentScheduleTask extends MonthlyDepartmentScheduleTask { 
	
	public DailyDepartmentScheduleTask(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.oif.task.MonthlyDepartmentScheduleTask#processDepartmentScheduleByPlanCode(java.lang.String)
	 */
	@Override
	protected void processDepartmentScheduleByPlanCode(String receivedFile) {
		
		Map<String, ArrayList<DailyDepartmentSchedule>> schedulesMap = getSchedulesByPlanCode(receivedFile);
		
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
			String previousProcessLocation = null;
			String currentLineNo = null;
			String previousLineNo = null;
			List<DailyDepartmentSchedule> scheduleListByPlanCode = schedulesMap.get(planCode);
			for (DailyDepartmentSchedule dailyDeptSched : scheduleListByPlanCode) {
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
					currentLineNo  = dailyDeptSchedId.getLineNo();
					previousProductionDate = prevDailyDeptSchedId.getProductionDate();
					previousProcessLocation  = prevDailyDeptSchedId.getProcessLocation();
					previousLineNo =prevDailyDeptSchedId.getLineNo();
					sameSet = (currentProductionDate.equals(previousProductionDate) && currentProcessLocation.equals(previousProcessLocation) 
							&& currentLineNo.equals(previousLineNo)); 
					if(!sameSet) {
						List<DailyDepartmentSchedule> scheduleList = 
							dailyDeptSchedDao.getAllByProductionDateLocationAndLineNo(previousProductionDate,previousProcessLocation,previousLineNo);
						processSchedules(scheduleList, dailyDeptSchedList); // production date changed save schedules for current plan code
						dailyDeptSchedList = new ArrayList<DailyDepartmentSchedule>();
					}
				}
				Date prodDate = dailyDeptSchedId.getProductionDate();
				dailyDeptSched.setDayOfWeek((new SimpleDateFormat("EEE")).format(prodDate).toUpperCase());
				//calculate start and end timestamps now that Triggers on the table is removed
				dailyDeptSched.setStartTimestamp(setStartTimestamp(dailyDeptSched.getStartTime(), dailyDeptSched.getEndTime(), dailyDeptSchedId.getProductionDate(), dailyDeptSched.getNextDay()));
				Timestamp endTimestamp = setEndTimestamp(dailyDeptSched.getEndTime(), dailyDeptSchedId.getProductionDate(), dailyDeptSched.getNextDay());
				endTimestamp.setNanos(999999000);
				dailyDeptSched.setEndTimestamp(endTimestamp);
				
				dailyDeptSchedList.add(dailyDeptSched);
				prevDailyDeptSched = dailyDeptSched;
			}
			if(!firstLine) {
				List<DailyDepartmentSchedule> scheduleList = 
					dailyDeptSchedDao.getAllByProductionDateLocationAndLineNo(currentProductionDate, currentProcessLocation, currentLineNo);
				processSchedules(scheduleList, dailyDeptSchedList);
			}
			logger.info(String.format("File %s processed for plan code %s", receivedFile, planCode));
		}
	}
	
	
}
