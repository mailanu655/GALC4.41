package com.honda.galc.oif.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.HttpClient;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants.DEPARTMENT_CODE;

/**
 * 
 * <h3>ProductionProgressCommon</h3>
 * <p> ProductionProgressCommon is a class containing common methods used in Production Progress Handlers </p>
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
 * @author Ratul Chakravarty<br>
 * April 14, 2014
 *
 */


public class ProductionProgressCommon {
	
	private static Logger logger = Logger.getLogger("ProductionProgressCommon");
	private static final String PRODUCTION_PROGRESS_DATA_URL_PART = "/RestWeb/ProductionLotService/";

	/**
	 * Export production result records.
	 * 
	 * @param File prodProgressFile
	 *            file object in which the production progress records for engine will be written.
	 * @param String url
	 *            The base URL string of the web service to be called.
	 * @param String prodProgressType
	 *            The type of files being generated. Accepted values are either GPP102 or GIV707.
	 * @param String div
	 *            The department of plant for which data is to be retrieved.
	 * @param OifErrorsCollector errorsCollector
	 *            The objects which collects error messages when any exception occurs to e-mailed to support team.
	 * @param File updatedRecsLogFile
	 *            The file where the production lot records that have been updated will be logged.
	 * @return int 
	 * 			  number of records that were written to the interface file, applicable only to GPP102.
	 * @throws Exception 
	 * @author Ratul Chakravarty
	 */
	public int exportProductionProgress(File prodProgressFile, String url, String interfaceId, String prodProgressType, DEPARTMENT_CODE department, OifErrorsCollector errorsCollector, File updatedRecsLogFile, boolean allowDBUpdate) throws Exception {

		logger.info("prodProgressFile.getName() = " + prodProgressFile.getName());

		Gson gson = new Gson();
		int recordCount = 0;
		// Get Production Progress
		int progType = 0;
		if(prodProgressType.equals("GPP102")) {
			progType = 102;
		}
		else if(prodProgressType.equals("GIV707")) {
			progType = 707;
		}
		switch(department) {
		case AE:
			progType += 1000;
			break;
		case AF:
			progType += 2000;
			break;
		case PA:
			progType += 3000;
			break;
		case WE:
			progType += 4000;
			break;
		case IA:
			progType += 5000;
			break;
		}
		
		String requestUrl = url + PRODUCTION_PROGRESS_DATA_URL_PART + "getProductionProgress";
		String jsonSTR = "{\"java.lang.String\":\"" + interfaceId + "\", \"java.lang.Integer\":\"" + progType + "\", \"java.lang.Boolean\":\"" + allowDBUpdate + "\"}";
		String reponseStr = HttpClient.post(requestUrl, jsonSTR, 201);
		try {
			@SuppressWarnings("unchecked")
			Map<String, List<String>> aeProdProgMap = gson.fromJson(reponseStr, Map.class);
			if(aeProdProgMap != null) {
				List<String> prodProgressRecs = aeProdProgMap.get(prodProgressType);
				recordCount = prodProgressRecs == null ? 0 : prodProgressRecs.size();
				if(recordCount > 0) {
					writeDataToFile(prodProgressRecs, prodProgressFile);
				}
			} else {
				logger.info("No data return from " + requestUrl + " for " + jsonSTR);
			}
			
			// write a log of updated records
			if(aeProdProgMap != null && updatedRecsLogFile != null) {
				List<String> updatedRecsList = aeProdProgMap.get("UPDATED_PROD_LOTS");
				if(updatedRecsList != null) {
					BufferedWriter updatedRecsWriter = new BufferedWriter(new FileWriter(updatedRecsLogFile.getAbsolutePath(), true));
					updatedRecsWriter.write("Line URL used : " + url + " for " + prodProgressType + " and div " + department.name());
					updatedRecsWriter.newLine();
					if(!updatedRecsList.isEmpty()) {
						for (String string : updatedRecsList) {
							updatedRecsWriter.write(string);
							updatedRecsWriter.newLine();
						}
					} else {
						updatedRecsWriter.write("No records were updated.");
						updatedRecsWriter.newLine();
					}
					updatedRecsWriter.close();
				}
			}
			
		} catch (IOException ioe) {
			logger.error(ioe, "IOException in exportProductionProgress() method, Line URL used : " + url + " for " + prodProgressType);
			errorsCollector.emergency(ioe, "IOException in exportAEProductionProgress() method, Line URL used :-" + url + " for " + prodProgressType);
			throw ioe;
		} catch (Exception e) {
			logger.error(e, "Exception in exportProductionProgress() method, Line URL used : " + url + " for " + prodProgressType);
			errorsCollector.emergency(e, "IOException in exportAEProductionProgress() method, Line URL used : " + url + " for " + prodProgressType);
			throw e;
		}
		return recordCount;
	}

	/**
	 * Get the current timestamp in a decided format as per given logic as a string.
	 * 
	 * @return a specially formatted string value representing the current timestamp.
	 * @author Ratul Chakravarty
	 */
	public String getCurrentTimestamp() {
		java.sql.Timestamp vCreateTimestamp = new java.sql.Timestamp((new java.util.Date()).getTime());
		return vCreateTimestamp.toString().substring(0, 4) 
				+ vCreateTimestamp.toString().substring(5, 7)
				+ vCreateTimestamp.toString().substring(8, 10)
				+ vCreateTimestamp.toString().substring(11, 13)
				+ vCreateTimestamp.toString().substring(14, 16)
				+ vCreateTimestamp.toString().substring(17, 19);
	}

	private int writeDataToFile(List<String> prodProgressRecs, File prodProgressFile) throws IOException {
		int resultCount = prodProgressRecs.size();
		BufferedWriter progProgressRecWriter;
		try {
			progProgressRecWriter = new BufferedWriter(new FileWriter(prodProgressFile.getAbsolutePath(), true));
			for (String string : prodProgressRecs) {
				progProgressRecWriter.write(string);
				progProgressRecWriter.newLine();
			}
			progProgressRecWriter.close();
		} catch (IOException ioe) {
			logger.error(ioe, "IOException in writeDataToFile()");
			throw ioe;
		}
		return resultCount;
	}
}
