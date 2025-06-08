package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.net.HttpClient;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;

public class BearingUsageHandler extends OifAbstractTask implements
		IEventTaskExecutable {

	public BearingUsageHandler(String pObjectName) throws IOException {
		super(pObjectName);
		errorsCollector = new OifErrorsCollector(pObjectName);
	}

	private static long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
	private static final String START_DATETIME = "START_DATETIME";
	private static final String END_DATETIME = "END_DATETIME";
	private static final String BEARING_PART = "BEARING_PART";
	private static final String BEARING_COLOR = "BEARING_COLOR";
	private static final String COUNT = "COUNT";
	private static final String LINE_ID = "LINE_ID";
	private static final String BEARING_USAGE_URL_PART = "/RestWeb/BearingSelectResultDao/getBearingUsageData";
	private static final String USE_NATURAL_TIME_FRAME = "USE_NATURAL_TIME_FRAME";
	private static final String COMPONENT_STATUS_KEY = "LAST_PROCESS_TIMESTAMP";
	private static String COMPONENT_STATUS_ID = "";
	private static final String PROCESS_POINTS = "PROCESS_POINTS";
	private static final String PLAN_CODE = "PLAN_CODE";

	private SimpleDateFormat _dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat _outputDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private DecimalFormat _decimalFormat4 = new DecimalFormat("0000");
	private DecimalFormat _decimalFormat5 = new DecimalFormat("00000");
	private OifErrorsCollector errorsCollector;
	private String interfaceID;
	private boolean isDebug = false;
	private String createTimestamp;

	/**
	 * The timestamp to indicate if its date falls in a working production day.
	 * This is mainly used to determine if the interface file needs to be sent
	 * on Saturday. If the Saturday is a production day, the file will be sent.
	 * Otherwise, it will not call the sendFileToMQ
	 */
	protected java.sql.Timestamp productionTimestamp;

	private class BearingRecord {
		private int recordId = 0;
		private String plantCode = "HMA ";
		private String invLocCode = "BSK01     ";
		private String lineId = ""; // Used to generate 11 character inventory
									// category (e.g. MP01, MP02)
		private String partNo = ""; // 18 character part number
		private String partColorCode = "           ";
		private int partQty = 0;
		private Timestamp reportTime = null; // Used to create 8 character
												// reporting date and 6
												// character reporting time
		private String filler = "                       ";

		public String toString() {
			StringBuffer sb = new StringBuffer();

			sb.append(_decimalFormat4.format(recordId));
			sb.append(plantCode);
			sb.append(invLocCode);
			sb.append(spacePad(getProperty("CATEGORY_CODE{"+lineId+"}", "MP") + lineId, 11));
			sb.append(partNo);
			sb.append(partColorCode);
			sb.append(_decimalFormat5.format(partQty));
			sb.append(_outputDateFormat.format(reportTime));
			sb.append(filler);

			return sb.toString();
		}
	}

	private String spacePad(String inputString, int length) {
		if (inputString == null) {
			inputString = "";
		}
		StringBuffer sb = new StringBuffer(inputString);

		for (int i = inputString.length(); i < length; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * export Bearing Usage records
	 */
	private void createAndSendBearingUsageRecords() {
		// ---------------------------------------------------
		// Get the last update time from the properties file
		// ---------------------------------------------------
		boolean useNaturalTimeFrame = getPropertyBoolean(
				USE_NATURAL_TIME_FRAME, false);
		ComponentStatusDao componentStatusDao = ServiceFactory.getDao(ComponentStatusDao.class); 
		ComponentStatus componentStatus = componentStatusDao.findByKey(COMPONENT_STATUS_ID, COMPONENT_STATUS_KEY);
		String lastUpdateTime = componentStatus != null ? componentStatus.getStatusValue() : null;
		java.util.Date lastUpdateDate = null;
		if (!StringUtils.isBlank(lastUpdateTime)) {
			try {
				lastUpdateDate = _dateFormat.parse(lastUpdateTime);
			} catch (ParseException e1) {
				// Ignore. Let them SQL exception occur, which will be reported
				// by email
			}
		}
		logger.info("LastUpdateDate is " + lastUpdateDate);
		Timestamp startTimestamp = null;
		try {
			startTimestamp = new java.sql.Timestamp(
					lastUpdateDate.getTime());
		} catch (NullPointerException e) {
			String strError = "Unable to handle lastUpdateDate. No data will be sent. Please verify that ComponentStatus " +
					 		  "has been defined for COMPONENT_ID: " + COMPONENT_STATUS_ID + ", STATUS_KEY: " + COMPONENT_STATUS_KEY;
			logger.error(strError);
			errorsCollector.emergency(strError);
			return;
		}
		// get Current time from database
		Timestamp endTimestamp = getCurrentTime(true);
		java.util.Date endDate = new java.util.Date(endTimestamp.getTime());
		if (!useNaturalTimeFrame) {
			// ------------------------------------------------
			// Now create Timestamp for last run date at 2 AM
			// ------------------------------------------------
			GregorianCalendar lastUpdateDateAt2AM = new GregorianCalendar();
			if (lastUpdateDate != null) {
				lastUpdateDateAt2AM.setTime(lastUpdateDate);
			}
			lastUpdateDateAt2AM.set(Calendar.HOUR_OF_DAY, 2);
			lastUpdateDateAt2AM.set(Calendar.MINUTE, 0);
			lastUpdateDateAt2AM.set(Calendar.SECOND, 0);
			lastUpdateDateAt2AM.set(Calendar.MILLISECOND, 0);
			logger.info("2AM on LastUpdateDate is "
					+ _dateFormat.format(lastUpdateDateAt2AM.getTime()));

			// If updated after 2AM, then start at 2AM that day
			if (lastUpdateDate != null
					&& lastUpdateDate.after(lastUpdateDateAt2AM.getTime())) {
				startTimestamp = new Timestamp(
						lastUpdateDateAt2AM.getTimeInMillis());
			}
			// Else if updated before 2AM, then start at 2AM on the day before
			else {
				startTimestamp = new Timestamp(
						lastUpdateDateAt2AM.getTimeInMillis()
								+ ONE_DAY_IN_MILLIS);
			}

			// Now set our end timestamp to exactly one day after start
			endTimestamp = new Timestamp(startTimestamp.getTime()
					+ ONE_DAY_IN_MILLIS);
		}

		ArrayList<BearingRecord> bearingList = new ArrayList<BearingRecord>();
		int recordCount = 0;

		// call the web services on the active lines and get bearing usage data
		// from each line
		String[] lineURLsArr = getProperty("ACTIVE_LINES_URLS").split(",");
		String[] activeLinesArr = getProperty("ACTIVE_LINES").split(",");
		

		int ctr = 0;
		Map<String, String> inputData = new HashMap<String, String>();
		inputData.put(START_DATETIME, startTimestamp.toString());
		inputData.put(END_DATETIME, endTimestamp.toString());
		
		
		List<Map<String, Object>> allRecordsList = new ArrayList<Map<String, Object>>();
		
		for (String url : lineURLsArr) {
			
			String installProcessPoint = getProperty("INSTALL_PROCESS_POINT{"+activeLinesArr[ctr]+"}","  ");
			String planCode = getProperty("PLAN_CODE{"+activeLinesArr[ctr]+"}", "");	
			inputData.put(PLAN_CODE,planCode);
			inputData.put(PROCESS_POINTS,installProcessPoint);
			Gson gson = new Gson();
			String jsonInputStr = gson.toJson(inputData);
			// enclose the data in a DataContainer object
			jsonInputStr = "{\"com.honda.galc.data.DataContainer\" : "
					+ jsonInputStr + "}";
			logger.info("JSON input string to web services:- " + jsonInputStr);
			
			logger.info("calling web service url :- " + url
					+ BEARING_USAGE_URL_PART);
			String responseStr = HttpClient.post(url + BEARING_USAGE_URL_PART,
					jsonInputStr, 201);
			logger.info("JSON output from web service of line "
					+ activeLinesArr[ctr] + " :- " + responseStr);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> responseList = gson.fromJson(responseStr,
					List.class);
			if (responseList != null && responseList.size() > 0) {
				for (Map<String, Object> hashMap : responseList) {
					hashMap.put(LINE_ID, activeLinesArr[ctr]);
				}
				allRecordsList.addAll(responseList);
			}
			ctr++;
		}
		for (Map<String, Object> hashMap : allRecordsList) {
			BearingRecord thisRecord = new BearingRecord();
			thisRecord.recordId = ++recordCount;
			String part = hashMap.get(BEARING_PART).toString();
			@SuppressWarnings("unused")
			String color = hashMap.get(BEARING_COLOR).toString(); // currently
																	// unused
			String count = hashMap.get(COUNT).toString();
			count = count.substring(0, count.indexOf("."));

			thisRecord.lineId = hashMap.get(LINE_ID).toString();

			// part. Bert. need to replace first "-" with blank, etc....
			if (part.length() == 15) { // "12345ABC-A000M1"
				part = part.replaceAll("-", " ");
			} else if (part.length() == 17) { // "12345-ABC-A000-M1"
				part = part.substring(0, 5) + part.substring(6, 9) + " "
						+ part.substring(10, 14) + part.substring(15);
			} else {
				part = part.substring(0, 5) + part.substring(6, 9) + " "
						+ part.substring(10);
			}

			thisRecord.partNo = spacePad(part, 18);
			thisRecord.partQty = Integer.parseInt(count);
			thisRecord.reportTime = endTimestamp;
			thisRecord.plantCode = spacePad(getProperty("PLANT_CODE"), 4);
			thisRecord.invLocCode = spacePad(getProperty("INV_LOC_CODE"), 10);

			bearingList.add(thisRecord);
		}

		String resultPath = PropertyService.getProperty(
				OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
		String exportFileName = this.interfaceID + createTimestamp + ".oif";

		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					new File(resultPath + exportFileName)));
			for (int i = 0; i < bearingList.size(); i++) {
				BearingRecord thisRec = (BearingRecord) bearingList.get(i);
				bufferedWriter.write(thisRec.toString());
				bufferedWriter.newLine();
				if (isDebug)
					logger.info(thisRec.toString());
			}
			bufferedWriter.close();

			if (bearingList.size() == 0) {
				logger.info("No Bearing Usage found for this period.  Interface id="
						+ this.interfaceID + "; file name = " + exportFileName);
			} else {
				if (getProperty("SEND_BEARING_USAGE_DATA_TO_MQ")
						.equalsIgnoreCase("true")) {
					MQUtility mqu = new MQUtility(this);
					logger.info("Calling executeMQSendAPI with send file name  = "
							+ resultPath
							+ exportFileName
							+ " with Interface ID = " + interfaceID);
					String mqConfig = PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.MQ_CONFIG);
					mqu.executeMQSendAPI(interfaceID, mqConfig, resultPath
							+ exportFileName);
				}
			}
		} catch (MQUtilityException e) {
			String errorStr = "MQUtilityException raised when sending interface file for "
					+ resultPath
					+ exportFileName
					+ " for the interface "
					+ interfaceID + ". Exception : " + e.getMessage();
			logger.error(errorStr);
			errorsCollector.emergency(errorStr);
		} catch (IOException e) {
			logger.error(e, "IOException in exporting Bearing Usage data.");
			errorsCollector.emergency(e,
					"IOException in exporting Bearing Usage data.");
		}

		// Update the date with the current date of last execution.
		try {
			String endTimeString = _dateFormat.format(endDate);
			logger.info("Updating the last execution date to " + endTimeString);
			componentStatus.setStatusValue(endTimeString);
			componentStatusDao.save(componentStatus);
		} catch (Exception e) {
			logger.error(e,
					"Exception formatting date in Bearing Usage Interface");
			errorsCollector.emergency(e,
					"Exception formatting date in Bearing Usage Interface");
		}
	}

	public void execute(Object[] args) {
		// Launch application and initialize run time parameters
		logger.info("Launching BearingUsageHandler application.");
		try {
			refreshProperties();
			// Use the current time as the part of file name
			productionTimestamp = new Timestamp(
					(new java.util.Date()).getTime());
			String prodTimestampStr = productionTimestamp.toString();
			createTimestamp = prodTimestampStr.substring(0, 4)
					+ prodTimestampStr.substring(5, 7)
					+ prodTimestampStr.substring(8, 10)
					+ prodTimestampStr.substring(11, 13)
					+ prodTimestampStr.substring(14, 16)
					+ prodTimestampStr.substring(17, 19);

			initialize();
		} catch (Exception e) {
			e.printStackTrace();
			if (this != null) {
				logger.error("Exception raised when executing BearingUsageHandler. Terminating application. Exception:"
						+ e.getMessage());
			} else {
				System.out
						.println("Exception raised when executing the BearingUsageHandler. Terminating application. Exception:"
								+ e.getMessage());
			}
			errorsCollector
					.emergency("Exception raised when executing the BearingUsageHandler. Terminating application. Exception:"
							+ e.getMessage());
		}
		// Now generate Bearing Usage information and send it to a file via MQ
		try {
			logger.info("****Generating Bearing Usage file...");
			createAndSendBearingUsageRecords();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception raised in createAndSendBearingUsageRecords().  Exception:"
					+ e.getMessage());
			errorsCollector
					.emergency("Exception raised in createAndSendBearingUsageRecords().  Exception:"
							+ e.getMessage());
		} finally {
			errorsCollector.sendEmail();
		}
	}

	/**
	 * Initialize runtime parameters
	 * 
	 * @return true or false
	 */
	private void initialize() {
		COMPONENT_STATUS_ID = this.componentId;
		interfaceID = getProperty(OIFConstants.INTERFACE_ID);
		isDebug = getPropertyBoolean("DEBUG_GIV719", true);
		if (isDebug) {
			logger.info("Interface Id=" + interfaceID);
			logger.info(USE_NATURAL_TIME_FRAME + " is set to "
					+ getProperty(USE_NATURAL_TIME_FRAME));
		}
	}

	public Timestamp getCurrentTime(boolean isDBTimestamp) {
		GenericDaoService genericDao = getService(GenericDaoService.class);
		Calendar now = GregorianCalendar.getInstance();
		Timestamp nowTs = null;
		if (isDBTimestamp) {
			nowTs = genericDao.getCurrentDBTime();
		} else {
			nowTs = new Timestamp(now.getTimeInMillis());
		}

		return nowTs;
	}

}
