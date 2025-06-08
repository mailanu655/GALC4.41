package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.msip.dto.outbound.Aaa112Dto;
import com.honda.galc.service.msip.property.outbound.Aaa112PropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Aaa112Handler extends BaseMsipOutboundHandler<Aaa112PropertyBean> {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Aaa112Dto> fetchDetails(Date startTimestamp, int duration) {
		getLogger().info("Inside  List<Aaa112Dto> ");
		getLogger().info("getComponentId() :: " + getComponentId());
		String processPoint = getPropertyBean().getPropertyProcessPoint();
		getLogger().info("processPoint :: " + processPoint);
		String lineURLs = getPropertyBean().getPropertyActiveLinesUrls();
		getLogger().info("lineURLs :: " + lineURLs);
		List<Aaa112Dto> dtoList = new ArrayList<Aaa112Dto>();
		if(StringUtils.isBlank(processPoint) || StringUtils.isBlank(lineURLs)) {
			getLogger().error("Needed configuration is missing [" + "Counting Process Point: " + processPoint + ", active lines: " + lineURLs +"]");
			getLogger().error("Ignore AFOffMonthlyReportTask");
			dtoList.clear();
			Aaa112Dto dto = new Aaa112Dto();
			dto.setErrorMsg("Unexpected Error Occured: Needed configuration is missing [" + "Counting Process Point: " + processPoint + ", active lines: " + lineURLs +"]");
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
		
		//Get start time and end time
		Date[][] timePeriodArr = null;
		String[] activeLines = lineURLs.split(",");
		boolean isCustomizedTime = getPropertyBean().getPropertyIsCustomizedTime();
		if(isCustomizedTime) {
			//parse the configured start/end time if the customizing time is enabled
			Date[] customizedTime = getCustomizedTimePeriod(startTimestamp, duration);
			if(customizedTime!=null) {
				timePeriodArr = new Date[activeLines.length][2];
				for(Date[] period: timePeriodArr) {
					period[0] = customizedTime[0];
					period[1] = customizedTime[1];
				}
			}
		} 
		
		if(timePeriodArr == null) {
			//use the default start/end data if the customizing time is disabled or incorrect
			//check if the needed configurations are set (active lines and line NO are matching)
			String lineNoStr = getPropertyBean().getPropertyLineNo();
			String[] lineNoArr = lineNoStr.split(",");
			if(activeLines.length!=lineNoArr.length) {
				getLogger().error("The properties 'LINE_NO' and 'ACTIVE_LINES_URLS' are not matching.");
				getLogger().error("Ignore AFOffMonthlyReportTask");
				dtoList.clear();
				Aaa112Dto dto = new Aaa112Dto();
				dto.setErrorMsg("Unexpected Error Occured: The properties 'LINE_NO' and 'ACTIVE_LINES_URLS' are not matching.");
				dto.setIsError(true);
				dtoList.add(dto);
				return  null;
			}
			//get start/end time from gal226tbx(schedule)
			timePeriodArr = getDefaultTimePeriod(activeLines, lineNoArr);
		} 
		return exportData(activeLines, timePeriodArr);
	}
	
	private List<Aaa112Dto> exportData(String[] activeLines, Date[][] timePeriodArr){
		//retrieve and merge data
		HashMap<String, Aaa112Dto> resultMap = new LinkedHashMap<String, Aaa112Dto>();
		DateFormat formater = new SimpleDateFormat("yyyy.MMM");
		for(int i=0; i<activeLines.length; i++) {
			if(timePeriodArr[i] == null || timePeriodArr[i][0] == null || timePeriodArr[i][1] == null) {
				getLogger().error("Time period is null for Line " + activeLines[i] + ", move to next Line");
				continue;
			}
			
			ProductResultDao dao = HttpServiceProvider.getService(activeLines[i] + 
					getPropertyBean().getHttpServiceUrlPart(),ProductResultDao.class);
			if(dao==null) {
				getLogger().error("Can not access Service DAO for Line " + activeLines[i] + ", move to next Line");
				continue;
			}
			
			String startTimeStr = formater.format(timePeriodArr[i][0]).toUpperCase();
			getLogger().info("start collecting data for Line[" + activeLines[i] + "] Month[" + startTimeStr + "]");
			
			List<Object[]> lineResults = dao.getPeriodPassingCountByPP(getPropertyBean().getPropertyProcessPoint(),getPropertyBean().getPropertyScrapProcessPoint(), timePeriodArr[i][0], timePeriodArr[i][1]);
			Aaa112Dto dto = null;
			for(int j=0; lineResults!=null && j<lineResults.size(); j++) {
				Object[] lineObj = lineResults.get(j);
				String key = convertIfNull(lineObj[0], "")  + convertIfNull(lineObj[1], "") + convertIfNull(lineObj[2], "");//key: model_year_code + model_code + model_type_code
				if(resultMap.containsKey(key)) {
					dto = resultMap.get(key);
				} else {
					dto = new Aaa112Dto();
					dto.setTime(startTimeStr);
					dto.setProduct(key);
				}
				dto.setQuantity(dto.getQuantity() + Integer.parseInt(lineObj[3].toString()));
				resultMap.put(key, dto);
			}
			getLogger().info("Collect data for Line[" + activeLines[i] + "] Month[" + startTimeStr + "]");
		}
		
		getLogger().info("Finish AF-Off Monthly Report Task");	
		return (List<Aaa112Dto>)resultMap.values();
	}
	
	private Date[] getCustomizedTimePeriod(Date startTimestamp, int duration) {
		Date[] customizedTime = null;
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = format.parse(String.valueOf(startTimestamp));
			getLogger().info("startTime :: " + startTime);
			//Date endTime = format.parse(getPropertyBean().getPropertyCustomizedEndTime());
			Date endTime = getEndDate(startTimestamp, duration);
			getLogger().info("endTime :: " + endTime);
			if(startTime==null || endTime==null || startTime.after(endTime)) {
				getLogger().warn("Customized Start/End Time is incorrect, so generating file with default option");
			} else {
				//check if the customized start time and end time are in the same month
				Calendar cal = Calendar.getInstance();
				cal.setTime(startTime);
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().before(endTime)) {
					getLogger().warn("Customized Start/End Time is not in the same month, so generating file with default option");
				} else {
					customizedTime = new Date[2];
					customizedTime[0] = startTime;
					customizedTime[1] = endTime;
				}
			}
		} catch (ParseException e) {
			getLogger().warn("Parse Customized Start/End Time error, so generating file with default option");
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
		String plantCode = getPropertyBean().getPropertyPlantCode();
		String processLocation = getPropertyBean().PropertyProcessLocation();
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
			DailyDepartmentScheduleDao scheduleDao = HttpServiceProvider.getService(activeLines[i] 
					+ getPropertyBean().getHttpServiceUrlPart()
					, DailyDepartmentScheduleDao.class);
			if(scheduleDao==null) {
				getLogger().info("Can not access Schedule DAO for server [" + activeLines[i] + "]");
				continue;
			}
			DailyDepartmentSchedule start = scheduleDao.getFirstPeriodTimestamp(lineNoArr[i], plantCode, processLocation, dateFormater.format(firstDayInLastMonth.getTime()));
			DailyDepartmentSchedule end  = scheduleDao.getFirstPeriodTimestamp(lineNoArr[i], plantCode, processLocation, dateFormater.format(firstDayInThisMonth.getTime()));
			timePeriodArr[i][0] = (start==null? null : start.getStartTimestamp());
			timePeriodArr[i][1] = (end==null? null : end.getStartTimestamp());
		}
		return timePeriodArr;
	}
	public Date getEndDate(Date startTimestamp, int seconds) {
		Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTimestamp.getTime());
        cal.add(Calendar.SECOND, seconds);
        return new Timestamp(cal.getTime().getTime());
 	}
}
