
package com.honda.galc.oif.task;
import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.oif.property.DepartmentSchedulePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>DepatmentScheduleTask Class description</h3>
 * <p> DepatmentScheduleTask description </p>
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
 * @author Jeffray Huang<br>
 * Oct 25, 2012
 *
 *
 */
public class DepatmentScheduleFromStagingTableTask extends OifAbstractTask implements IEventTaskExecutable{

	private DepartmentSchedulePropertyBean property;
	
	public DepatmentScheduleFromStagingTableTask(String name){
		super(name);
		PropertyService.refreshComponentProperties(getComponentId());
		property = PropertyService.getPropertyBean(DepartmentSchedulePropertyBean.class, name);
	}

	public void execute(Object[] args) {
		try{
			createSchedules();
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
		}
	}

	private void createSchedules() {
	   Date startDate = CommonUtil.getDate(property.getStartDateOffset());
	   Date endDate = CommonUtil.getDate(property.getStartDateOffset() + property.getDays());
	   
	   logger.info("Start Date = " + startDate + " , End Date =" + endDate);
	   
	   List<DailyDepartmentSchedule> allSchedules = new ArrayList<DailyDepartmentSchedule>();
	   
	   Set<String> processLocations = new HashSet<String>();
	   
	   if(property.useStandardSchedule()) {
		   allSchedules =  getDao(DailyDepartmentScheduleDao.class)
		   		.generateSchedulesFromStandard(startDate, endDate);
		   processLocations = getProcessLocations(allSchedules);
		   logger.info("Create schedule from standard schedule for departments "  + processLocations);
	   }
	   
	   List<DailyDepartmentSchedule> schedules = getDao(DailyDepartmentScheduleDao.class)
	   		.deriveDailyDepartmentSchedulesFromGPCS(startDate, endDate);
	   
	   adjustSchedules(schedules);
	   
	   int count = deleteOldSchedules(startDate);
	   logger.info("deleted " + count + " rows of old schedules before insert");
	   
	   getDao(DailyDepartmentScheduleDao.class).saveAll(schedules);
	   logger.info("inserted " +allSchedules.size() + " rows of schedules" );
	   
	}
	
	private void adjustSchedules(List<DailyDepartmentSchedule> schedules) {
		
		DailyDepartmentScheduleId sameShiftKey = null;
		float workTime = 0;
		List<DailyDepartmentSchedule> schedulesByDay = new ArrayList<DailyDepartmentSchedule>();
		
		for(DailyDepartmentSchedule schedule : schedules) {
	        if(sameShiftKey != null && !schedule.getId().isSameShift(sameShiftKey)){
	        	adjustSchedules(schedulesByDay, workTime);
				workTime = 0;
				schedulesByDay.clear();
			}
	        schedulesByDay.add(schedule);
			sameShiftKey = schedule.getId();
	        if(schedule.getPlan().equals("Y")) {
				workTime += schedule.getDuration();
			}
		}

	}
	
	private Set<String> getProcessLocations(List<DailyDepartmentSchedule> schedules) {
		Set<String> processLocations = new HashSet<String>();
		  
		for(DailyDepartmentSchedule schedule : schedules) {
			processLocations.add(schedule.getId().getProcessLocation());
		}
		return processLocations;
	}
	
	private void adjustSchedules(List<DailyDepartmentSchedule> schedules,float workTime) {
		
		float value = 0;
		float valueCapacityOn = 0;
		boolean isNextDay = false;
		for(DailyDepartmentSchedule schedule : schedules) {
			if(schedule.isPlan() && workTime > 0) {
				value += schedule.getCapacity() * schedule.getDuration() / workTime;
				valueCapacityOn += schedule.getCapacityOn() * schedule.getDuration() / workTime;
			}
			schedule.setCapacity(schedule.isPlan()? Math.round(value): 0);
			schedule.setCapacityOn(schedule.isPlan()? Math.round(valueCapacityOn): 0);
			if(schedule.isPlan()) {
				value -= Math.round(value);
				valueCapacityOn -= Math.round(valueCapacityOn);
			}	
			if(isNextDay) schedule.setNextDay((short)1);
			isNextDay = schedule.isNextDay();
		}

	}
	
	private int deleteOldSchedules(Date startDate) {
		return getDao(DailyDepartmentScheduleDao.class).deleteSchedules(startDate);
	}
	

}
