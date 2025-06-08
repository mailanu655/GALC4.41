package com.honda.galc.oif.task.ahm;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>CurrentInventoryTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> 
 * 		This interface compare the input message with information in table GAL263TBX.
 * 		The final result of this interface is an report that contain information of inventory
 * 		The report is sent by email  
 *  </p>
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
 * <TD>DG</TD>
 * <TD>Feb 11, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Daniel Garcia
 * @created Feb 11, 2015
 */
public class CurrentInventoryTask extends OifTask<Object> implements IEventTaskExecutable{
	
//	The list of file names that are received from GPCS(MQ).
	private static final String CUTOFF_TIME = "CUTOFF_TIME";
	private static final String FORMAT_DATE = "yyyy-MM-dd";
	private static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	private static final String DISTRIBUTION_LIST = "DISTRIBUTION_EMAIL_LIST";
	private static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	private static final String OPEN_STATUS = "OPEN_STATUS";	
	private static final String OTHER_STATUS = "OTHER_STATUS";
		
	protected String[] aReceivedFileList;
	
	

	public CurrentInventoryTask(String name) {
		super(name);		
	}

	public void execute(Object[] args) {
		logger.info("Start to process Current Inventory");
		refreshProperties();		
		
		// General properties		
		String cutOffTime = getProperty(CUTOFF_TIME);
		String distributionList = getProperty(DISTRIBUTION_LIST);
		String emailSubject = getProperty(EMAIL_SUBJECT);
		String openStatus = getProperty(OPEN_STATUS);		
		String otherStatus = getProperty(OTHER_STATUS);
		
		
		try{
			//get the MQ message and save it in a file
			aReceivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID),
					getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));
			if (aReceivedFileList == null) {
				return;
			}
			for (int count=0; count<aReceivedFileList.length; count++) {
				// get from properties the path where is saved the file from MQ
				String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
				
				String receivedFile = aReceivedFileList[count];
				//Open the file that was saved from MQ 
				List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger);
				receivedRecords.remove(0);
				receivedRecords.remove(receivedRecords.size() - 1);
	
				ShippingStatusDao dao = ServiceFactory.getDao(ShippingStatusDao.class);
	
				Timestamp cutDate = Timestamp.valueOf(new SimpleDateFormat(FORMAT_DATE).format(new Date()) + " " + cutOffTime);
	
				List<ShippingStatus> openStatusList = dao.getVinByStatus(openStatus, receivedRecords, cutDate);
	
				List<String> outputMessages = new ArrayList<String>();
				
				//Create and form the report
				outputMessages.add("Matching ALC Vin Ship Status with AH Inventory Report - " + cutOffTime);
				outputMessages.add("==========================================================================================");
				outputMessages.add("Vin               Status Actual_Timestamp           Update_Timestamp         Match_Result");
				outputMessages.add("------------------------------------------------------------------------------------------");
				//get all information about vins that not exist into database
				for (ShippingStatus shippingStatus : openStatusList) {
					outputMessages.add(shippingStatus.getId() + " " + ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus()).getName()
							+ "    " + new SimpleDateFormat(FORMAT_TIMESTAMP).format(shippingStatus.getActualTimestamp()) + " " + new SimpleDateFormat(FORMAT_TIMESTAMP).format(shippingStatus.getUpdateTimestamp())
							+ " *** VIN in AHM status in GALC, not in AHM system ***");
				}
				outputMessages.add("------------------------------------------------------------------------------------------");
				//get Vin with other status (-1,0,4)
				List<ShippingStatus> otherStatusList = dao.getVinInOtherStatus(otherStatus, receivedRecords, cutDate);
				for (ShippingStatus shippingStatus : otherStatusList) {
					outputMessages.add(shippingStatus.getId() + " " + ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus()).getName()
							+ "    " + new SimpleDateFormat(FORMAT_TIMESTAMP).format(shippingStatus.getActualTimestamp()) + " " + new SimpleDateFormat(FORMAT_TIMESTAMP).format(shippingStatus.getUpdateTimestamp())
							+ " *** VIN in AHM system, not in AHM status in GALC ***");				
				}
				outputMessages.add("------------------------------------------------------------------------------------------");
				//get all vin with status in -1
				List<ShippingStatus> returned = dao.getfactoryReturns();				
				for (ShippingStatus shippingStatus : returned) {
					outputMessages.add(shippingStatus.getId() + " " + ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus()).getName()
							+ "    " + new SimpleDateFormat(FORMAT_TIMESTAMP).format(shippingStatus.getActualTimestamp()) + " " + new SimpleDateFormat(FORMAT_TIMESTAMP).format(shippingStatus.getUpdateTimestamp())
							+ " factory returned");
				}
				outputMessages.add("------------------------------------------------------------------------------------------");
				Integer totalRecordOpenStatus=dao.countVinOpenStatus("1,2,3", cutDate);
				outputMessages.add("\nTotal number of records with open status in ALC = " + totalRecordOpenStatus);
				outputMessages.add("Total number of records matched with AH = "+(totalRecordOpenStatus - openStatusList.size()));
				outputMessages.add("Total number of records not matched with AH = "+ openStatusList.size());
				
				outputMessages.add("\nTotal number of inventory records from AH = "+ receivedRecords.size());
				outputMessages.add("Total number of records matched with ALC = "+ (receivedRecords.size() - otherStatusList.size()));
				outputMessages.add("Total number of records not matched with ALC = "+ otherStatusList.size());
				
				outputMessages.add("\nTotal number of records returned to ALC = "+ returned.size());
		
				//Create a report file
				OIFFileUtility.writeToFile(outputMessages, resultPath + receivedFile +".txt");
				
				//Send the report file in an email
				sendEmail(resultPath + receivedFile +".txt", distributionList, emailSubject);
								
			}
		} catch (IOException io) {		
			errorsCollector.error("Error creating the report file for Current Inventory Interface"
							+ io.getMessage());
			logger.error("Error creating the report file for Current Inventory Interface"
							+ io.getMessage());			
		}
		finally {
			errorsCollector.sendEmail();
		}
		logger.info("End to process Current Inventory");
	}
	
	/**
	 * 	email report file
	 * @param reportFile	-	this is the path of the report
	 * @param emailList		-	this is the email distribution list
	 * @param emailSubject	-	this is the subject for the email
	 */
	private void sendEmail(String reportFile, String emailList, String emailSubject) {
		OifServiceEMailHandler eMailHandler = new OifServiceEMailHandler(OIFConstants.OIF_NOTIFICATION_PROPERTIES);		
		eMailHandler.setEmailAddressList(emailList);
		StringBuffer msg = new StringBuffer();			
		msg.append("This e-mail has an attached with information about current inventory. \n");
		msg.append("Please review the attached inventory comparison report for details. \n");		
		eMailHandler.delivery(emailSubject, msg.toString(), reportFile);
	}
		
}