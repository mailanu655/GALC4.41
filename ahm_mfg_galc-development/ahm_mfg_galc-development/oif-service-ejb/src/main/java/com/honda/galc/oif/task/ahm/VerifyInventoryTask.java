package com.honda.galc.oif.task.ahm;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.net.ftp.FTPClient;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.util.FtpClientHelper;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>VerifyInventoryTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <P><B>Purpose: This class is to read the inventory interface file D--GMG#HMAGAL#AHM010 received from American Honda
 *       to compare the number of vins that have an open status in shipping status table(GAL263TBX). An open status
 *       may contain the following status code:
 *        1 - indicates HMA Shipping
 *        2 - indicates ADC Receiving
 *        3 - indicates ADC Release.
 *       The matching result will be printed to a report file D--GMG#HMAGAL#AHM010.txt. 
 *       This report file along with the AH inventory file will be copied to a PC group folder (configurable)
 *       This process should be scheduled to run right after the inventory file is received.
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
 * <TD>LK</TD>
 * <TD>Jan 06, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Larry Karpov
 * @created Jan 07, 2015
 */
public class VerifyInventoryTask extends OifTask<Object> 
	implements IEventTaskExecutable {
	
	public VerifyInventoryTask(String name) {
		super(name);
		propBean = PropertyService.getPropertyBean(VerifyInventoryBean.class, componentId);
		activeLineURLs = PropertyService.getProperties(
				componentId, "ACTIVE_LINE_URL(?:\\{(.*)})");
	}
	
	private VerifyInventoryBean propBean;
	private List<ComponentProperty> activeLineURLs;
//	String searchPath;
	
	public void execute(Object[] args) {
		try{
			processVerifyInventory();
		}catch(TaskException e) {
			logger.error(e.getMessage());
			errorsCollector.error(e.getMessage());
		}catch(IOException e) {
			logger.error(e.getMessage());
			errorsCollector.error(e.getMessage());
		}catch(Exception e) {
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	/**
	 * .<br>
	 * @throws IOException 
	 */
	private void processVerifyInventory() throws IOException {
		logger.info("Start to process AHMShippingLog");
		Timestamp runTimestamp = null; 
		String searchTime = propBean.getRunTimestamp();
		if(searchTime.length() == 0) {
			runTimestamp = new Timestamp(System.currentTimeMillis());
			searchTime =
				"_"
				+ runTimestamp.toString().substring(0, 4)
				+ runTimestamp.toString().substring(5, 7)
				+ runTimestamp.toString().substring(8, 10);
		} else {
			searchTime =
				"_"
				+ searchTime.substring(0, 4)
				+ searchTime.substring(5, 7)
				+ searchTime.substring(8, 10);
		}
		String searchPath = propBean.getSearchPath();
		String search = propBean.getInventoryInterfaceId() + searchTime;
		List<String> inventoryFiles = OIFFileUtility.locateFiles(searchPath, search, logger);
		if (inventoryFiles == null || inventoryFiles.size() == 0) {
			String errMessage = "No Inventory Files for " + search + " in " + searchPath;
			logger.error(errMessage);
			errorsCollector.error(errMessage);
		} else {
			logger.info("Inventory Files:");
			for(String receivedFile : inventoryFiles) {
				logger.info("               " + receivedFile);
			}
		}
		int totalVIN = 0;
		SortedMap<String, String> vinList = new TreeMap<String, String>();
		for(String receivedFileName : inventoryFiles) {
			if(null == receivedFileName || receivedFileName.trim().length() == 0) {
				logger.error("Not a valid file.");
				errorsCollector.error("Not a valid file.");
				continue;
			}
			String receivedFile = searchPath + receivedFileName;
			List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(receivedFile, logger);
			for(String record : receivedRecords) {
				String strRecord = record.trim();
				if (strRecord.length() == 17) {		// Skip HEADER and TRAILER
					vinList.put(strRecord, strRecord);
					totalVIN++;
				}
			}
			String invFile = receivedFile + ".txt";
			long totalUnMatchedWithAH = checkInventory(invFile, vinList);
			logger.info("Closing Verify Inventory... ");
			if(totalUnMatchedWithAH > 0) {
				sendEmail(invFile);
			}
			if(propBean.isUseFtp()) {
				String invFileName = receivedFileName + ".txt";
				ftp(searchPath, invFileName);
			}
		}
	}
	
	/**
	 * This method matches the VIN from AH inventory and HMA ship status table (GAL263TBX). The matching
	 * result will be written to a report file with the pFileName as the output file name and ".txt" as
	 * extension.
	 * @param inventoryFile String the inventory file name. It will be used to name the report file
	 * @param vinList SortedMap this set contains all inventory records from AH
	 */
	public long checkInventory(String inventoryFile, SortedMap<String, String> vinList) throws IOException {
		long totalALCRecord = 0;
		long totalMatchedWithALC = 0;
		long totalUnMatchedWithALC = 0;
		long totalMatchedWithAH = 0;
		long totalUnMatchedWithAH = 0;
		long totalReturn = 0;
		long totalAHRecord = vinList.size();
		logger.info("The number of vins in AH list  is " + totalAHRecord);

		// First match all ALC VINs with AHMs
		String cutoffTime = getCutoffTimestamp();
		List<ShippingStatus> galcOpenStatusList = getOpenStatusList(Timestamp.valueOf(cutoffTime));
		InventoryOutputHelper ahm = new InventoryOutputHelper(inventoryFile);
		ahm.writeHeader(cutoffTime);
		for(ShippingStatus status : galcOpenStatusList) {
			totalALCRecord++;
			logger.info("Reading record = " + status.toString());
			// If the ALC VIN is found in AH Inventory, it is a match. Remove the record key.
			// Otherwise print a message of "not in AH" to indicate an ALC VIN not found in
			// AHs inventory list. In this case, we need to investigate why a VIN in ALC is not on
			// AH inventory list
			String vin = status.getVin();
			if (vinList.containsKey(vin)) {
				totalMatchedWithAH++;
				totalMatchedWithALC++;
				vinList.remove(vin);
				if (propBean.isPrintMatch()) {
					ahm.writeStatus(status, " found");
				}
			} else {
				ahm.writeStatus(status, " not in AH");
				totalUnMatchedWithAH++;
			}
		}
		ahm.dashLine();
		logger.info(
			"After comparing, the number of vins in AH list not found in ALC  is " + vinList.size());
		
		// Any record left in the AH list does not match a VIN in ALC. This could be the result of
		// one of following:
		// 1) The last transaction timestamp of a vin in ALC is greater than the cutoff time. This
		//    will cause the VIN not to be selected into the pALCVinList.
		// 2) For some reason, AH may hold a vin that may have shipment confirm or return-to-factory status
		//    in ALC. For example, if AH returns a vehicle to HMA after HMA receives a shipment confirm transaction,
		//    the vechile may show up on AH inventory list but not on ALC inventory.
		//
		if (vinList.size() > 0) {
			List<String> unMatchedVinList = new ArrayList<String>(vinList.keySet());
			List<ShippingStatus> galcStatusList = getSelectedVins(unMatchedVinList);
			for(String vin : unMatchedVinList) {
				totalUnMatchedWithALC++;
				boolean vinFound = true;
				// Print out the ship status for every VIN
				for(ShippingStatus shippingStatus : galcStatusList) {
					if(shippingStatus.getVin().equals(vin)) {
						ahm.writeStatus(shippingStatus, " not matched");
						vinFound = true;
						break;
					}
				}
				if(!vinFound) {
					ahm.writeLine(vin + "                            **** VIN in AH not found in ALC ****");
				}
			}
		}
		ahm.dashLine();
		List<ShippingStatus> factoryReturnList = getfactoryReturns();
		for(ShippingStatus factoryReturn : factoryReturnList) {
			totalReturn++;
			ahm.writeStatus(factoryReturn, " returned");
		}
		
		ahm.dashLine();
		ahm.newLine();
		ahm.writeLine("Total number of records with open status in ALC = " + totalALCRecord);
		ahm.writeLine("Total number of records matched with AH = "+ totalMatchedWithAH);
		ahm.writeLine("Total number of records not matched with AH = " + totalUnMatchedWithAH);
		ahm.newLine();
		ahm.writeLine("Total number of inventory records from AH = " + totalAHRecord);
		ahm.writeLine("Total number of records matched with ALC = " + totalMatchedWithALC);
		ahm.writeLine("Total number of records not matched with ALC = " + totalUnMatchedWithALC);
		ahm.newLine();
		ahm.writeLine("Total number of records returned to ALC = " + totalReturn);
		ahm.dashLine();
		ahm.close();
		return totalUnMatchedWithAH;
	}
	
	/**
	 * Copy file via FTP.<br>
	 */
	private void ftp(String searchPath, String inventoryFileName) {
		logger.info("ftp Inventory File");
		FtpClientHelper ftpHelper = new FtpClientHelper(propBean.getFtpServer(), propBean.getFtpPort(),
				propBean.getFtpUser(), propBean.getFtpPassword(), logger);
		FTPClient ftpClient = ftpHelper.getClient();
		InputStream inputStream = null;
		String localFile = searchPath + inventoryFileName;
		try {
			inputStream = new FileInputStream(localFile);
		} catch (FileNotFoundException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
		}
		
		String remoteFile = propBean.getFtpPath() + inventoryFileName;
		try {
			ftpClient.storeFile(remoteFile, inputStream);
		} catch (IOException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
		}
	}
	
	/**
	 * 	email report file
	 */
	private void sendEmail(String inventoryFile) {
		//Create email handler to send out email notification about the mismatch number
		OifServiceEMailHandler eMailHandler = new OifServiceEMailHandler(OIFConstants.OIF_NOTIFICATION_PROPERTIES);
		String emailDistributionList = propBean.getEmailDistributionList(); 
		eMailHandler.setEmailAddressList(emailDistributionList);
		StringBuffer msg = new StringBuffer();
		msg.append("The number of VINs that have an open status in ALC does not match the number of vins in the AH Inventory List \n");
		msg.append("Please review the attached inventory comparison report for details. \n");
		String emailSubject = propBean.getEmailSubject(); 
		eMailHandler.delivery(emailSubject, msg.toString(), inventoryFile);
	}
	
	/**
	 * Get Shipping Status List for List of VINs<br>
	 */
	public List<ShippingStatus> getSelectedVins(List<String> vins) {
		List<ShippingStatus> result = new ArrayList<ShippingStatus>(); 
		for (ComponentProperty activeLineURL : activeLineURLs) {
			if(activeLineURL != null) {
				String activeLine = activeLineURL.getPropertyValue();
				logger.info("Requesting data from " + activeLine);
				ShippingStatusDao dao = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ShippingStatusDao.class);
				result = dao.getSelectedVins(vins);
			}
		}
		return result;
	}
	
	/**
	 * Get Shipping Status List of Factory Returns<br>
	 */
	public List<ShippingStatus> getfactoryReturns() {
		List<ShippingStatus> result = new ArrayList<ShippingStatus>(); 
		for (ComponentProperty activeLineURL : activeLineURLs) {
			if(activeLineURL != null) {
				String activeLine = activeLineURL.getPropertyValue();
				logger.info("Requesting data from " + activeLine);
				ShippingStatusDao dao = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ShippingStatusDao.class);
				result = dao.getfactoryReturns();
			}
		}
		return result;
	}
	
	/**
	 * Get Shipping Status List for Shipped VINs<br>
	 */
	public List<ShippingStatus> getOpenStatusList(Timestamp cutoffTime) {
		List<ShippingStatus> result = new ArrayList<ShippingStatus>(); 
		for (ComponentProperty activeLineURL : activeLineURLs) {
			if(activeLineURL != null) {
				String activeLine = activeLineURL.getPropertyValue();
				logger.info("Requesting data from " + activeLine);
				ShippingStatusDao dao = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ShippingStatusDao.class);
				result = dao.getVinsWithOpenStatus(cutoffTime);
			}
		}
		return result;
	}

	/**
	 * Create timestamp from current timestamp and cutoff time<br>
	 */
	private String getCutoffTimestamp() {
		Timestamp cutoffTimestamp = new Timestamp(System.currentTimeMillis());
		return
			cutoffTimestamp.toString().substring(0, 4)
				+ "-" + cutoffTimestamp.toString().substring(5, 7)
				+ "-" + cutoffTimestamp.toString().substring(8, 10)
				+ " " + propBean.getCutoffTime();
	}

	/**
	 * Inner class to deal with output<br>
	 */
	private class InventoryOutputHelper {
		private FileWriter vFileWriter;
		private BufferedWriter vBufferedWriter;
		
		InventoryOutputHelper(String vFileName) throws IOException {
			try {
				vFileWriter = new FileWriter(vFileName, true);
			} catch (IOException e) {
				logger.error("Error creating FileWriter.");
				throw new IOException();
			}
			vBufferedWriter = new BufferedWriter(vFileWriter);
		}
		
		private void writeHeader(String cutOffTime) throws IOException {
			writeLine(
				"Matching ALC Vin Ship Status with AH Inventory Report - " + cutOffTime);
			writeLine(
				"==========================================================================================");
			writeLine(
				"Vin               Status Actual_Timestamp           Update_Timestamp         Match_Result");
			dashLine();
		}
		
		private void writeStatus(ShippingStatus shippingStatus, String descr) throws IOException {
			String outputFormat = "%1$tF %1$tT.%1$tN";
			String actualTimestamp = String.format(outputFormat, shippingStatus.getActualTimestamp());
			String updateTimestamp = String.format(outputFormat, shippingStatus.getUpdateTimestamp());
			String status = String.format("%-6d", shippingStatus.getStatus());
			String vin = String.format("%-18s", shippingStatus.getVin());
			StringBuffer record = new StringBuffer(vin)
				.append(status)
				.append(actualTimestamp.substring(0, 26))
				.append(" ")
				.append(updateTimestamp.substring(0, 26))
				.append(descr);
			writeLine(record.toString());
		}
		
		private void writeLine(String line) throws IOException {
			vBufferedWriter.write(line);
			vBufferedWriter.newLine();
		}
		
		private void dashLine() throws IOException {
			writeLine("------------------------------------------------------------------------------------------");
		}

		private void newLine() throws IOException {
			vBufferedWriter.newLine();
		}
		
		private void close() throws IOException {
			logger.info("Closing BufferedWriter... ");
			vBufferedWriter.close();
		}
		
	}

}
