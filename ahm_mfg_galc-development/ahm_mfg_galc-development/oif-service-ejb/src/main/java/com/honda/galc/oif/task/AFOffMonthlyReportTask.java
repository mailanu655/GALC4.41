package com.honda.galc.oif.task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.AFOffMonthlyPassingDTO;
import com.honda.galc.oif.dto.IOutputFormat;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
/**
 * 
 * <h3>AFOffMonthlyReportTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Generate a monthly flat file with given format, which is the statistics for the product passing count of a given process point in the last month </p>
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
 * <TR>
 * <TD>YX</TD>
 * <TD>Aug 05, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author YX
 * @created Aug 05, 2014
 */
public class AFOffMonthlyReportTask extends OifTask<Object> implements IEventTaskExecutable {
	private static final Logger logger = Logger.getLogger();
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	
	private static final String PROPERTY_PROCESS_POINT = "PROCESS_POINT"; //property key for the Process point id, which is the point the passing count is for 
	private static final String PROPERTY_SCRAP_PROCESS_POINT = "SCRAP_PROCESS_POINT";//property key for scrap ProcessPoint
	private static final String PROPERTY_ACTIVE_LINES_URLS = "ACTIVE_LINES_URLS"; //property key for the data sources needed to collect data from, which is separated by ','
	//properties to get production schedule
	private static final String PROPERTY_LINE_NO = "LINE_NO";
	private static final String PROPERTY_PLANT_CODE = "PLANT_CODE";
	private static final String PROPERTY_PROCESS_LOCATION = "PROCESS_LOCATION";
	//properties to customize file time period 
	private static final String PROPERTY_IS_CUSTOMIZED_TIME = "IS_CUSTOMIZED_TIME";
	private static final String PROPERTY_CUSTOMIZED_START_TIME = "START_TIME";
	private static final String PROPERTY_CUSTOMIZED_END_TIME = "END_TIME";
	
	public AFOffMonthlyReportTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		
		//check if the needed configurations are set (the process point and lines which the data is generated from) 
		Map<String,String> offProcessPointMap = PropertyService.getPropertyMap(this.componentId,PROPERTY_PROCESS_POINT);
		Map<String,String> scrapProcessPointMap =PropertyService.getPropertyMap(this.componentId,PROPERTY_SCRAP_PROCESS_POINT);
		String lineURLs = getProperty(PROPERTY_ACTIVE_LINES_URLS,"");
		String lineNoStr = getProperty(PROPERTY_LINE_NO, "");
		
		if(offProcessPointMap == null || StringUtils.isBlank(lineURLs)|| StringUtils.isBlank(lineNoStr)) {
			logger.error("Needed configuration is missing [" + "Counting Process Point: " + offProcessPointMap.size() + ", active lines: " + lineURLs +"]");
			logger.error("Ignore AFOffMonthlyReportTask");
			return;
		}
		
		//Get start time and end time
		Date[][] timePeriodArr = null;
		String[] activeLines = lineURLs.split(",");
		boolean isCustomizedTime = getPropertyBoolean(PROPERTY_IS_CUSTOMIZED_TIME, false);
		if(isCustomizedTime) {
			//parse the configured start/end time if the customizing time is enabled
			Date[] customizedTime = getCustomizedTimePeriod();
			if(customizedTime!=null) {
				timePeriodArr = new Date[activeLines.length][2];
				for(Date[] period: timePeriodArr) {
					period[0] = customizedTime[0];
					period[1] = customizedTime[1];
				}
			}
		} 
		String[] lineNoArr = lineNoStr.split(",");
		if(timePeriodArr == null) {
			//use the default start/end data if the customizing time is disabled or incorrect
			//check if the needed configurations are set (active lines and line NO are matching)
				
			if(activeLines.length!=lineNoArr.length) {
				logger.error("The properties 'LINE_NO' and 'ACTIVE_LINES_URLS' are not matching.");
				logger.error("Ignore AFOffMonthlyReportTask");
				return;
			}
			//get start/end time from gal226tbx(schedule)
			timePeriodArr = getDefaultTimePeriod(activeLines, lineNoArr);
		} 
		
		//retrieve and merge data
		HashMap<String, AFOffMonthlyPassingDTO> resultMap = new LinkedHashMap<String, AFOffMonthlyPassingDTO>();
		DateFormat formater = new SimpleDateFormat("yyyy.MMM");
		for(int i=0; i<lineNoArr.length; i++) {
			if(timePeriodArr[i] == null || timePeriodArr[i][0] == null || timePeriodArr[i][1] == null) {
				logger.error("Time period is null for Line " + activeLines[i] + ", move to next Line");
				continue;
			}
			
			ProductResultDao dao = HttpServiceProvider.getService(activeLines[i] + HTTP_SERVICE_URL_PART,ProductResultDao.class);
			if(dao==null) {
				logger.error("Can not access Service DAO for Line " + activeLines[i] + ", move to next Line");
				continue;
			}
			
			String startTimeStr = formater.format(timePeriodArr[i][0]).toUpperCase();
			logger.info("start collecting data for Line[" + activeLines[i] + "] Month[" + startTimeStr + "]");
			String offProcessPoint = offProcessPointMap.get(lineNoArr[i]);
			String scrapProcessPoint = scrapProcessPointMap.get(lineNoArr[i]);
			
			if(StringUtils.isBlank(offProcessPoint) || StringUtils.isBlank(scrapProcessPoint)) {
				logger.error(" OffProcessPoint and ScrapProcessPoint configuration is missing for line -" + lineNoArr[i]);
				logger.error("Ignore AFOffMonthlyReportTask");
				return;
			}
			List<Object[]> lineResults = dao.getPeriodPassingCountByPP(offProcessPoint,scrapProcessPoint, timePeriodArr[i][0], timePeriodArr[i][1]);
			AFOffMonthlyPassingDTO dto = null;
			for(int j=0; lineResults!=null && j<lineResults.size(); j++) {
				Object[] lineObj = lineResults.get(j);
				String key = lineObj[0].toString() + lineObj[1].toString() + lineObj[2].toString();//key: model_year_code + model_code + model_type_code
				if(resultMap.containsKey(key)) {
					dto = resultMap.get(key);
				} else {
					dto = new AFOffMonthlyPassingDTO();
					dto.setTime(startTimeStr);
					dto.setProduct(key);
				}
				dto.setQuantity(dto.getQuantity() + Integer.parseInt(lineObj[3].toString()));
				resultMap.put(key, dto);
			}
			logger.info("Collect data for Line[" + activeLines[i] + "] Month[" + startTimeStr + "]");
		}
		
		//format and export data
		exportDataByOutputFormatHelper(AFOffMonthlyPassingDTO.class, new ArrayList<AFOffMonthlyPassingDTO>(resultMap.values()));
		logger.info("Finish AF-Off Monthly Report Task");		
	}
	
	@Override
	protected <K extends IOutputFormat> void exportDataByOutputFormatHelper(Class<K> outputClass,
			List<K> outputData) {
		String exportPath = getProperty(OIFConstants.EXPORT_FILE_PATH);		
		String exportFileName =  getProperty(OIFConstants.EXPORT_FILE_NAME) + ".oif";
		String mqConfig = getProperty(OIFConstants.MQ_CONFIG);
		String opFormatDefKey = this.componentId;
		exportDataByOutputFormatHelper(outputClass, outputData, exportPath, exportFileName, mqConfig, opFormatDefKey);
	}
	
	/**
	 * get customized time period from db properties
	 * @return
	 */
	private Date[] getCustomizedTimePeriod() {
		Date[] customizedTime = null;
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = format.parse(getProperty(PROPERTY_CUSTOMIZED_START_TIME,""));
			Date endTime = format.parse(getProperty(PROPERTY_CUSTOMIZED_END_TIME,""));
			
			if(startTime==null || endTime==null || startTime.after(endTime)) {
				logger.warn("Customized Start/End Time is incorrect, so generating file with default option");
			} else {
				//check if the customized start time and end time are in the same month
				Calendar cal = Calendar.getInstance();
				cal.setTime(startTime);
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().before(endTime)) {
					logger.warn("Customized Start/End Time is not in the same month, so generating file with default option");
				} else {
					customizedTime = new Date[2];
					customizedTime[0] = startTime;
					customizedTime[1] = endTime;
				}
			}
		} catch (ParseException e) {
			logger.warn("Parse Customized Start/End Time error, so generating file with default option");
		}
		return customizedTime;
	}
	
	/**
	 * get default time period from schedule table
	 * @param lineNoArr line No list separated by ","
	 * @return start/end time for last month schedule
	 */
	private Date[][] getDefaultTimePeriod(String[] activeLines, String[] lineNoArr) {
		Date[][] timePeriodArr = new Date[lineNoArr.length][2];
		String plantCode = getProperty(PROPERTY_PLANT_CODE, "");
		String processLocation = getProperty(PROPERTY_PROCESS_LOCATION,"");
		DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		//The first day of last month
		Calendar firstDayInLastMonth =  Calendar.getInstance();
		firstDayInLastMonth.setTimeInMillis(System.currentTimeMillis());
		firstDayInLastMonth.add(Calendar.MONTH, -1);
		firstDayInLastMonth.set(Calendar.DAY_OF_MONTH, 1);
		firstDayInLastMonth.set(Calendar.HOUR_OF_DAY, 0);
		firstDayInLastMonth.set(Calendar.MINUTE, 0);
		firstDayInLastMonth.set(Calendar.SECOND, 0);
		//the first day of this month
		Calendar firstDayInThisMonth =  Calendar.getInstance();
		firstDayInThisMonth.setTimeInMillis(System.currentTimeMillis());
		firstDayInThisMonth.set(Calendar.DAY_OF_MONTH, 1);
		firstDayInThisMonth.set(Calendar.HOUR_OF_DAY, 0);
		firstDayInThisMonth.set(Calendar.MINUTE, 0);
		firstDayInThisMonth.set(Calendar.SECOND, 0);
		
		for(int i=0; i<lineNoArr.length; i++) {
			//get schedule for each Line
			DailyDepartmentScheduleDao scheduleDao = HttpServiceProvider.getService(activeLines[i] + HTTP_SERVICE_URL_PART, DailyDepartmentScheduleDao.class);
			if(scheduleDao==null) {
				logger.info("Can not access Schedule DAO for server [" + activeLines[i] + "]");
				continue;
			}
			DailyDepartmentSchedule start = scheduleDao.getFirstPeriodTimestamp(lineNoArr[i], plantCode, processLocation, dateFormater.format(firstDayInLastMonth.getTime()));
			DailyDepartmentSchedule end  = scheduleDao.getFirstPeriodTimestamp(lineNoArr[i], plantCode, processLocation, dateFormater.format(firstDayInThisMonth.getTime()));
			timePeriodArr[i][0] = (start==null? null : start.getId().getProductionDate());
			timePeriodArr[i][1] = (end==null? null : end.getId().getProductionDate());
		}
		return timePeriodArr;
	}

}
