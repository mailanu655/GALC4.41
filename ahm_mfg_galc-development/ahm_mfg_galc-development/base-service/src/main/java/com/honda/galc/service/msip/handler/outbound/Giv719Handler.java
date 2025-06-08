package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.honda.galc.net.HttpClient;
import com.honda.galc.service.msip.dto.outbound.Giv719Dto;
import com.honda.galc.service.msip.property.outbound.Giv719PropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Giv719Handler extends BaseMsipOutboundHandler<Giv719PropertyBean> {
	
	private static final String START_DATETIME = "START_DATETIME";
	private static final String END_DATETIME = "END_DATETIME";
	private static final String BEARING_PART = "BEARING_PART";
	private static final String BEARING_COLOR = "BEARING_COLOR";
	private static final String COUNT = "COUNT";
	private static final String LINE_ID = "LINE_ID";
	private static final String BEARING_USAGE_URL_PART = "/RestWeb/BearingSelectResultDao/getBearingUsageData";
	private SimpleDateFormat _outputDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private SimpleDateFormat _dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	String errorMsg = null;
	Boolean isError = false;	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Giv719Dto> fetchDetails(Date startTimestamp, int duration) {
		List<Giv719Dto> dtoList = new ArrayList<Giv719Dto>();
		try{
			return createAndSendBearingUsageRecords(startTimestamp, duration);
		}catch(Exception e){
			dtoList.clear();
			Giv719Dto dto = new Giv719Dto();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	
	/**
	 * export Bearing Usage records
	 */
	private List<Giv719Dto> createAndSendBearingUsageRecords(Date sTs, int duration) {
		// ---------------------------------------------------
		// Get the last update time from the properties file
		// ---------------------------------------------------
		String lastUpdateTime = getPropertyBean().getLastExecutionDate();

		java.util.Date lastUpdateDate = null;
		if (lastUpdateTime != null) {
			try {
				lastUpdateDate = _dateFormat.parse(lastUpdateTime);
			} catch (ParseException e1) {
				// Ignore. Let them SQL exception occur, which will be reported
				// by email
			}
		}
		getLogger().info("LastUpdateDate is " + lastUpdateDate);

		Timestamp startTimestamp = new Timestamp(sTs.getTime());
		Timestamp endTimestamp = getTimestamp(startTimestamp, duration);

		ArrayList<Giv719Dto> bearingList = new ArrayList<Giv719Dto>();
		int recordCount = 0;

		// call the web services on the active lines and get bearing usage data
		// from each line
		String[] lineURLsArr = getPropertyBean().getActiveLineUrls();
		String[] activeLinesArr = getPropertyBean().getActiveLines();

		int ctr = 0;
		Map<String, String> inputData = new HashMap<String, String>();
		inputData.put(START_DATETIME, startTimestamp.toString());
		inputData.put(END_DATETIME, endTimestamp.toString());
		Gson gson = new Gson();
		String jsonInputStr = gson.toJson(inputData);
		// enclose the data in a DataContainer object
		jsonInputStr = "{\"com.honda.galc.data.DataContainer\" : "
				+ jsonInputStr + "}";
		getLogger().info("JSON input string to web services:- " + jsonInputStr);
		List<Map<String, Object>> allRecordsList = new ArrayList<Map<String, Object>>();
		for (String url : lineURLsArr) {
			getLogger().info("calling web service url :- " + url
					+ BEARING_USAGE_URL_PART);
			String responseStr = HttpClient.post(url + BEARING_USAGE_URL_PART,
					jsonInputStr, 201);
			getLogger().info("JSON output from web service of line "
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
			generateRecord(hashMap, bearingList, ++recordCount, endTimestamp);
		}
		return bearingList;
	}
	
	private void generateRecord(Map<String, Object> hashMap, ArrayList<Giv719Dto> bearingList, int recordCount,
			Timestamp endTimestamp){
		Giv719Dto thisRecord = new Giv719Dto();
		thisRecord.setRecordId(recordCount);
		String part = hashMap.get(BEARING_PART).toString();
		@SuppressWarnings("unused")
		String color = hashMap.get(BEARING_COLOR).toString(); // currently
																// unused
		String count = hashMap.get(COUNT).toString();
		count = count.substring(0, count.indexOf("."));

		thisRecord.setLineId(hashMap.get(LINE_ID).toString());

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
		thisRecord.setPartNo(spacePad(part, 18));
		thisRecord.setPartQty(Integer.parseInt(count));
		thisRecord.setReportTime(_outputDateFormat.format(endTimestamp));
		thisRecord.setPlantCode(spacePad(getPropertyBean().getPlantCode(), 4));
		thisRecord.setInvLocCode(spacePad(getPropertyBean().getInvLocCode(), 10));

		bearingList.add(thisRecord);
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

}
