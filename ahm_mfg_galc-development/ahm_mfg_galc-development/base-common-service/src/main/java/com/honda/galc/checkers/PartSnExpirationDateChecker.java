package com.honda.galc.checkers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.dataformat.PartSerialScanData;

public class PartSnExpirationDateChecker extends AbstractBaseChecker<PartSerialScanData> {

	public static final String BATTERY_EXPIRED_ON = "Battery expired on ";

	private static String DATE_FORMAT = "yyyyMMdd";
	
	private Map<String, String> yearCode = new HashMap<String, String>();
	private Map<String, String> monthCode = new HashMap<String, String>();
	private Date parsedExpDate = null;

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Part;
	}

	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(PartSerialScanData partSerialScanData) {
		initYearMonthMap();
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		String msg = "";
		String expirationDate = "";
		
		String expirationYear = "";
		String expirationMonth = "";
		String expirationDay = "";
		
		String partSn = partSerialScanData.getSerialNumber();
		if (partSn == null ||partSn.length() < 8) msg = "Invalid Part SN.";
		else { 
			expirationYear = partSn.substring(4, 5);
			expirationYear = yearCode.get(expirationYear);

			expirationMonth = partSn.substring(5, 6);
			expirationMonth = monthCode.get(expirationMonth);
			
			expirationDay = partSn.substring(6, 8);
			
			expirationDate = expirationYear + expirationMonth + expirationDay;
			
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setLenient(false);
			
			try {
				parsedExpDate = sdf.parse(expirationDate);
				if (getCutoffExpDate().after(parsedExpDate))  {
					msg = BATTERY_EXPIRED_ON + parsedExpDate;
				}
			} catch (ParseException e) {
				msg = "Invalid Expiration Date Format.";
			    e.printStackTrace();
			}
		}
		
		if (StringUtils.isNotBlank(msg)) {
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage(msg);
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
		}
		return checkResults;
	}

	private Date getCutoffExpDate() {
		return new Date();
	}
	
	private void initYearMonthMap() {
		yearCode.put("P", "2015");
		yearCode.put("O", "2016");
		yearCode.put("N", "2017");
		yearCode.put("M", "2018");
		yearCode.put("L", "2019");
		yearCode.put("K", "2020");
		yearCode.put("J", "2021");
		yearCode.put("I", "2022");
		yearCode.put("H", "2023");
		yearCode.put("G", "2024");
		yearCode.put("F", "2025");
		yearCode.put("E", "2026");
		yearCode.put("D", "2027");
		yearCode.put("C", "2028");
		yearCode.put("B", "2029");
		yearCode.put("A", "2030");
		yearCode.put("Z", "2031");
		yearCode.put("Y", "2032");
		
		monthCode.put("A", "01");
		monthCode.put("B", "02");
		monthCode.put("C", "03");
		monthCode.put("D", "04");
		monthCode.put("E", "05");
		monthCode.put("F", "06");
		monthCode.put("G", "07");
		monthCode.put("H", "08");
		monthCode.put("I", "09");
		monthCode.put("J", "10");
		monthCode.put("K", "11");
		monthCode.put("L", "12");
	}
	
	public Date getExpirationDate() {
		return parsedExpDate;
	}
}
