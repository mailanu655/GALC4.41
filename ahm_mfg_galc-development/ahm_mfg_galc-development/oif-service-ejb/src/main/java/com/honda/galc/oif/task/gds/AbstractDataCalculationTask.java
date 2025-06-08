package com.honda.galc.oif.task.gds;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.oif.task.OifAbstractTask;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.wds.WdsBufferedClient;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;


/**
 * 
 * <h3>AbstractDataCalculationTask Class description</h3>
 * <p> AbstractDataCalculationTask description </p>
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
 * Nov 7, 2012
 *
 *
 */
public abstract class AbstractDataCalculationTask extends OifAbstractTask implements IEventTaskExecutable{

	//Set this property for testing a time other than current timestamp
	private static final String TEST_TIME_KEY = "TEST_TIMESTAMP"; 

	// default delimeter for property value
	protected static final String DEFAULT_DELIMETER = Delimiter.SEMI_COLON;
	
	protected Timestamp testTimestamp;
	protected Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
	
	protected Map<String,List<DailyDepartmentSchedule>> currentDepartmentSchedules;
	protected Map<String,DailyDepartmentSchedule> currentSchedules;
	
	protected List<ProcessPoint> allProcessPoints;
	
	protected WdsBufferedClient wdsClient = new WdsBufferedClient(logger);
	
	public AbstractDataCalculationTask(String name) {
		super(name);
		logger.info("Start to excecute task : " + name);
		setupTestTime();
		fetchAllProcessPoints();
	}

	public void execute(Object[] args) {
		try{
			processCalculation();
		}catch(Exception e) {
			logger.error(e,"unable to process calculation");
		}finally{
			flushUpdate();
		}
	}	
	
	protected void setupTestTime() {
		String testTime = getProperty(TEST_TIME_KEY);
		try{
			testTimestamp = testTime == null ? currentTimestamp : Timestamp.valueOf(testTime);
			logger.info("test timestamp is " + testTimestamp);
		}catch(Exception e) {
			testTimestamp = currentTimestamp;
			logger.error("The format of the TEST_TIMESTAMP " + testTime + " is incorrect. It should be 'YYYY-MM-DD HH:MM:SS:NN.N'" );
		}
	}
	
	protected Timestamp getTestTimestamp(){
		return testTimestamp;
	}
	
	protected Timestamp getCurrentTimestamp() {
		return currentTimestamp;
	}
	
	protected Date getProductionDate(){
		return new Date(testTimestamp.getTime());
	}
	
	
	protected List<String> parseString(String propertyStr, String delimiter) {
		return Arrays.asList(StringUtils.split(propertyStr, delimiter));
	}
	
	protected List<List<String>> parseProcessPoints(String propertyStr){
		List<List<String>> processPoints = new ArrayList<List<String>>();
		List<String> processPointsStr = parseString(propertyStr,DEFAULT_DELIMETER);
		for(String item : processPointsStr) {
			processPoints.add(parseString(item,Delimiter.PLUS));
		}
		return processPoints;
	}
	
	protected List<List<List<String>>> getAllProcessPoints(String prefix){
		List<List<List<String>>> processPoints = new ArrayList<List<List<String>>>();
		for(ComponentProperty property : getProperties()) {
			if(property.getPropertyKey().startsWith(prefix))
				processPoints.add(parseProcessPoints(property.getPropertyValue()));
		}
		return processPoints;
	}
	
	private void fetchAllProcessPoints() {
		allProcessPoints = getDao(ProcessPointDao.class).findAll();
	}
	
	protected String getCombinedProcessPointName(List<String> processPointIds) {
		String name ="";
		for(String processPointId :processPointIds){
			if(name.length() != 0) name += "+";
			name += getProcessPointName(processPointId);
		}
		return name;
	}
	
	private ProcessPoint getProcessPoint(String processPointId) {
		for (ProcessPoint processPoint : allProcessPoints) {
			if(processPoint.getProcessPointId().equals(processPointId)) return processPoint;
		}
		return null;
	}
	
	protected String getProcessPointName(String processPointId) {
		ProcessPoint processPoint = getProcessPoint(processPointId);
		return processPoint == null ? "" : processPoint.getProcessPointName();
	}
	
	protected String getDepartmentName(List<String> processPointIds) {
		for(String processPointId : processPointIds) {
			ProcessPoint processPoint = getProcessPoint(processPointId);
			if(processPoint != null) return processPoint.getDivisionId();
		}
		return null;
	}
	
	
	
	/**
	 * convert a list of String to a string comma separated
	 * @param items
	 * @return
	 */
	protected String convert(List<String> items) {
		String aStr = "";
		boolean isFirst = true;
		for(String item : items) {
			if(isFirst) isFirst = false;
			else  aStr +=Delimiter.COMMA;
			aStr += item;
		}
		return aStr;
	}
	
	protected void fetchCurentDepartmentSchedules(){
		List<DailyDepartmentSchedule> allSchedules = getDao(DailyDepartmentScheduleDao.class).findAllByCurrentTimeStamp(getTestTimestamp());
		List<DailyDepartmentSchedule> schedules = null;
		currentDepartmentSchedules = new LinkedHashMap<String,List<DailyDepartmentSchedule>>(); 
		currentSchedules = new LinkedHashMap<String, DailyDepartmentSchedule>();
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		for(DailyDepartmentSchedule schedule: allSchedules) {
			
			if(schedules != null && !schedule.getId().getProcessLocation().equals(schedules.get(0).getId().getProcessLocation())){
				currentDepartmentSchedules.put(schedules.get(0).getId().getProcessLocation(), schedules);
			};
			if(schedules == null || !schedule.getId().getProcessLocation().equals(schedules.get(0).getId().getProcessLocation())){
				schedules = new ArrayList<DailyDepartmentSchedule>();
			}				
			schedules.add(schedule);
			
			if(!schedule.isAfter(getTestTimestamp()) && !currentSchedules.keySet().contains(schedule.getId().getProcessLocation()))
				currentSchedules.put(schedule.getId().getProcessLocation(), schedule);
		}
		if(schedules != null && !schedules.isEmpty())
			currentDepartmentSchedules.put(schedules.get(0).getId().getProcessLocation(), schedules);
	}
	
	protected List<String> getShifts(String deptName) {
		
		List<String> shifts = new ArrayList<String>();
		
		List<DailyDepartmentSchedule> schedules = currentDepartmentSchedules.get(deptName);
		
		for(DailyDepartmentSchedule schedule : schedules) {
			if (!shifts.contains(schedule.getId().getShift()))
				shifts.add(schedule.getId().getShift());
		}
		
		return shifts;
		
	}
	
	protected String getProductType(String processPointId) {
		ProcessPoint processPoint = getProcessPoint(processPointId);
		if(processPoint == null) return PropertyService.getProductType();
		String productType = PropertyService.getProperty(processPointId, "PRODUCT_TYPE");
		if(!StringUtils.isEmpty(productType)) return productType;
		return PropertyService.getProductType(processPoint.getDivisionId());
	}
	
	protected String getDepartmentName(String processPointId) {
		ProcessPoint processPoint = getProcessPoint(processPointId);
		return processPoint == null ? null : processPoint.getDivisionId();
	}
	
	public abstract void processCalculation();
	
	protected void updateValue(String name, int value){
		wdsClient.updateValue(name, value);
		logger.info("update value : " + name + " - " + value);
	}
	
	protected void updateValue(String name, String value){
		wdsClient.updateValue(name, value);
		logger.info("update value : " + name + " - " + value);
	}
	
	protected void flushUpdate() {
		wdsClient.flush();
	}
	
	protected Date getProductionDateFromDds(String deptName) {
		return currentSchedules.get(deptName).getId().getProductionDate();
				 
		
	}
	
}
