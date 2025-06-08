package com.honda.galc.oif.task;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.oif.dto.CapacityCalendarDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>BaseDailyDepartmentScheduleTask Class description</h3>
 * <p> BaseDailyDepartmentScheduleTask description </p>
 * Common functionality for 
 * 		Monthly Department Schedule 
 * 		and Daily Department Schedule Tasks
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
 * @since Dec 13, 2013
*/

public abstract class BaseDailyDepartmentScheduleTask extends BasePlanCodeTask<CapacityCalendarDTO> 
	implements IEventTaskExecutable {
	
	protected OIFParsingHelper<DailyDepartmentSchedule> parseHelper;
	protected boolean planCodesConfigured;
	protected List<String> departments = null;
	DailyDepartmentScheduleDao dailyDeptSchedDao = null;
	
	public BaseDailyDepartmentScheduleTask(String name) {
		super(name);
		dailyDeptSchedDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
	}
	
	public void execute(Object[] args) {
		try{
			Timestamp nowTs = getCurrentTime(true);
			initialize();
			logger.info("start to process: " + this.getClass().getSimpleName());
			// Get list of objects created from received file (GPP302) 			
			if(getFilesFromMQ() == 0) {
				return;
			}
			initParseHelper();

			// Process file(s) and update DailyDepartmentSchedule data  
			for (int count=0; count<receivedFileList.length; count++) {
				String receivedFile = receivedFileList[count];
				if (receivedFile == null) {
					continue;
				} 
				processDepartmentScheduleByPlanCode(receivedFile);
			}
			updateLastProcessTimestamp(nowTs);
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			setIncomingJobCount(totalRecords-failedRecords, failedRecords, receivedFileList);
			errorsCollector.sendEmail();
		}
	}

	
	@Override
	protected void initialize() {
		// BasePlanCodeTask.initialize()
		{
			String planCodes = getProperty(PLANCODES);
			if (planCodes == null) {
				logger.warn(String.format("Could not find the %s property of component %s", PLANCODES, componentId));
				planCodesConfigured = false;
			}
			else if (planCodes.length() == 0) {
				logger.warn(String.format("Property %s of component %s does not have a value", PLANCODES, componentId));
				planCodesConfigured = false;
			}
			else {
				setPlanCodes(planCodes);
				planCodesConfigured = true;
			}
			departments = PropertyService.getPropertyList(componentId, "DEPARTMENTS");

			logger.info(String.format("Current Plan Codes: %s", getPlanCodes()));
		}
		// OifTask.initialize()
		{
			// set the current assembly line Id
			siteLineId = getProperty(ASSEMBLY_LINE_ID, PropertyService.getAssemblyLineId());
			if(StringUtils.isEmpty(siteLineId)) {
				if (planCodesConfigured) {
					logger.warn("Could not find the ASSEMBLY_LINE_ID property");
				} else {
					throw new TaskException("Could not find the ASSEMBLY_LINE_ID property");
				}
			}
			logger.info("Current assembly line Id: " + siteLineId);
			siteName = PropertyService.getSiteName();
			if(StringUtils.isEmpty(siteName)) {
				if (planCodesConfigured) {
					logger.warn("Could not find the SITE_NAME property");
				} else {
					throw new TaskException("Could not find the SITE_NAME property");
				}
			}
			logger.info("Current site name: " + siteName);
		}
	}

	/**
	 * @return the departments
	 */
	public List<String> getDepartments() {
		return departments;
	}

	protected int getSchedules() { 
		refreshProperties();
		initialize();

		
//		Get list of objects created from received file
		receivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID),
				getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));
		if (receivedFileList == null) {
			return 0;
		}
		return receivedFileList.length;
	}
	
	protected List<DailyDepartmentSchedule> getSchedules(String fileName) {
		List<DailyDepartmentSchedule> schedules = new ArrayList<DailyDepartmentSchedule>();

		List<String> recordsList = LoadRecordsFromFile(fileName);
		if(recordsList != null && !recordsList.isEmpty())  {
			for(String receivedRecord : recordsList) {
				DailyDepartmentScheduleId dailyDeptSchedId = new DailyDepartmentScheduleId();
				parseHelper.parsePrimaryDataList(dailyDeptSchedId, receivedRecord);
	
				DailyDepartmentSchedule dailyDeptSched = new DailyDepartmentSchedule();
				dailyDeptSched.setId(dailyDeptSchedId);
				parseHelper.parseData(dailyDeptSched, receivedRecord);
	
				schedules.add(dailyDeptSched);
			}
		}
	
		if (schedules.isEmpty()) {
			String message = String.format("DailyDepartmentSchedule records not found in file: %s", fileName);
			logger.debug(message);
			errorsCollector.error(message);
			setIncomingJobStatus(OifRunStatus.RECORDS_MISSING_FOR_CONFIGURED_PLAN_CODE);
		}

		return schedules;
	}

	protected Map<String, ArrayList<DailyDepartmentSchedule>> getSchedulesByPlanCode(String fileName) {
		Map<String, ArrayList<DailyDepartmentSchedule>> schedulesByPlanCodeMap = new HashMap<String, ArrayList<DailyDepartmentSchedule>>();

		List<DailyDepartmentSchedule> recordsList = getSchedules(fileName);
		for(DailyDepartmentSchedule dailyDeptSched : recordsList) {
			if(planCodesConfigured && !validatePlanCode(dailyDeptSched.getPlanCode()))  {
				continue;
			}
			else if(!planCodesConfigured && !validateLine(dailyDeptSched.getId().getLineNo()))  {
				continue;
			}
			if(!schedulesByPlanCodeMap.containsKey(dailyDeptSched.getPlanCode().trim()))
				schedulesByPlanCodeMap.put(dailyDeptSched.getPlanCode().trim(), new ArrayList<DailyDepartmentSchedule>());
			schedulesByPlanCodeMap.get(dailyDeptSched.getPlanCode().trim()).add(dailyDeptSched);
		}

		if(schedulesByPlanCodeMap.isEmpty()) {
			String message = "";
			if(planCodesConfigured)  {
				message = String.format("Records matching current plan code(s): %s not found in file: %s",  getPlanCodes(), fileName);				
			}
			else  {
				message = String.format("No schedules found for line id: %s", this.siteLineId);
			}
			logger.debug(message);
			errorsCollector.error(message);
		}

		return schedulesByPlanCodeMap;
	}


		
	protected void initParseHelper() { 
//	Get configured parsing data 
		String departmentSchedule = getProperty(OIFConstants.PARSE_LINE_DEFS);
		parseHelper = new OIFParsingHelper<DailyDepartmentSchedule>(DailyDepartmentSchedule.class, departmentSchedule, logger);
		parseHelper.getParsingInfo();
	}
	
	protected void processSchedules(List<DailyDepartmentSchedule> currentDailyDeptSchedList, 
			List<DailyDepartmentSchedule> newDailyDeptSchedList) {
//	Delete future schedules
		for(DailyDepartmentSchedule currentDailyDeptSched : currentDailyDeptSchedList) {
			dailyDeptSchedDao.remove(currentDailyDeptSched);
			logger.debug("Remove Schedule: " + currentDailyDeptSched.toString());
		}
		for(DailyDepartmentSchedule dailyDeptSched : newDailyDeptSchedList) {
			dailyDeptSchedDao.save(dailyDeptSched);
			logger.info("Merge schedule: " + dailyDeptSched.toString());
		}
	}
	
	/**
	 * This section checks if a record should be kept as next day.<br>
	 * It starts from the record where next day = 1 and goes until<br>
	 * process location or production date changes or period changes to smaller number
	 * 
	 * Need to calculate the 'Next_day' based on previous schedule as GPCS only sends Next_day=Y for first line and 
	 * assumes GALC to calculate the rest of the shift.
	 */
	protected int calculateNextDay(int isNextDay, DailyDepartmentSchedule prevDailyDeptSched, DailyDepartmentSchedule dailyDeptSched) {
		//No need to intervene 
		if(dailyDeptSched.getNextDay() == -1) return dailyDeptSched.getNextDay();
		
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
			}else {
				isNextDay = 0;
			}
		}
		return isNextDay;
	}

	protected Timestamp setStartTimestamp(Time startTime,Time endTime, Date productionDate,short nextDay ) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(productionDate);
		if(nextDay==-1) {
			dateCal.add(Calendar.DATE,-1);
		}

		if(nextDay== 1 && startTime.before(endTime)) {
			dateCal.add(Calendar.DATE,1);
		}

		Calendar timeCal = Calendar.getInstance();
		timeCal.setTime(startTime);
		
		dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
		dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
		
		
		return new Timestamp(dateCal.getTime().getTime());
		
	}
	
	protected Timestamp setEndTimestamp(Time endTime,Date productionDate,short nextDay ) {
		
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(productionDate);
		if(nextDay==1) {
			dateCal.add(Calendar.DATE,1);
		}
		
		Calendar timeCal = Calendar.getInstance();
		timeCal.setTime(endTime);
		
		dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
		dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
	
		
		return  new Timestamp(dateCal.getTime().getTime());
		
	}


	abstract void processDepartmentScheduleByPlanCode(String receivedFile);

}