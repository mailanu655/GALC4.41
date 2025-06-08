package com.honda.galc.service.msip.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Mar 28, 2017
 */
public class MsipUtil {
	
	Logger log = Logger.getLogger(this.getClass().getName());
	private static final String timeFormat = getFormat("TIME_FORMAT"); 
	private static final String dateFormat = getFormat("DATE_FORMAT"); 
	private static final String timestampFormat = getFormat("TIMESTAMP_FORMAT"); 
	
	public static final SimpleDateFormat stf1 = createFormat(timeFormat); 
	public static final SimpleDateFormat sdf1 = createFormat(dateFormat); 
	public static final SimpleDateFormat stsf1 = createFormat(timestampFormat); 
	public static final Integer CHARACTER_SET = 819;
	public static final Integer ENCODING_ASCII = 279;
	private static String getFormat(String format) {
		String result = PropertyService.getProperty("OIF_SYSTEM_PROPERTIES", format);
		if(result == null) {
			throw new RuntimeException("Property value <" + format + "> is missing for property key <OIF_SYSTEM_PROPERTIES>.");
		}
		return result;
	}
	
	private static SimpleDateFormat createFormat(String format) {
		try {
			return new SimpleDateFormat(format);
		}catch(final Exception ex){
			throw new RuntimeException("Failed to create <" + format + "> in static block.", ex);
		}
	}
	
//	Calculate java.sql.timestamp from java.sql.date & java.sql.time 
	public static Timestamp getTimestamp(Date datePart, Time timePart, int isNextDay) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(datePart);
		if(1 == isNextDay) {
			dateCal.add(Calendar.DATE, 1);
		}
		Calendar timeCal = Calendar.getInstance();
		timeCal.setTime(timePart);
		dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
		dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
		Timestamp tmsp = new Timestamp(dateCal.getTime().getTime());
		return tmsp; 
	}
	
	public static boolean isCheckDigitNeeded(String productId) {
		
		if (productId.trim().length()>9)			
		{
			String checkDigitChar=Character.toString(productId.charAt(8));
			if(checkDigitChar.trim().equals("*"))
				return true;
		}
		return false;
	}
	
	public static boolean validateLine(String lineNo, String siteLineId)  {
		if(lineNo == null)  return false;
		else if(!lineNo.trim().equalsIgnoreCase(siteLineId)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param planCode
	 * @return boolean
	 * checks if any of the configured plan codes match the plan/line/product
	 */
	public static boolean isSameLine(String planCode, List<String> subset) {
		if(subset != null && !subset.isEmpty())  {
			return true;
		}
		return false;
	}
	
	public static boolean validatePlanCode(String planCode, String[] planCodes) {		
		if(StringUtils.isBlank(planCode)) {
			return false;
		}
		List<String> list = Arrays.asList(planCodes);
		if(!list.contains(planCode.trim())) {
			return false;
		}
		return true;
	}
	
}
