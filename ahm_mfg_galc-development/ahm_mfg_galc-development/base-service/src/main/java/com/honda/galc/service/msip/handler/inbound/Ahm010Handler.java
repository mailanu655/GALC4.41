package com.honda.galc.service.msip.handler.inbound;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.mail.MailContext;
import com.honda.galc.mail.MailSender;
import com.honda.galc.service.msip.dto.inbound.Ahm010Dto;
import com.honda.galc.service.msip.handler.inbound.BaseMsipInboundHandler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.property.inbound.Ahm010PropertyBean;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */

/* Its an Outbound interface - EngineToFrameMountStatusTask*/
public class Ahm010Handler extends BaseMsipInboundHandler<Ahm010PropertyBean, Ahm010Dto> {	
	private static final String FORMAT_DATE = "yyyy-MM-dd";
	private static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	ShippingStatusDao dao;
	public Ahm010Handler() {
		
	}
	
	public boolean execute(List<Ahm010Dto> dtoList) {
		List<String> vinList = new ArrayList<String>();
		System.out.println(dtoList.toString());
		try {
			//getPropertyBean().getInitializeTail()
			// General properties		
			String cutOffTime = getPropertyBean().getCutoffTime();
			String distributionList = getPropertyBean().getDistributionList();
			String emailSubject = getPropertyBean().getEmailSubject();
			String openStatus = getPropertyBean().getOpenStatus();		
			String otherStatus = getPropertyBean().getOtherStatus();
			String resultPath = "", receivedFile="";
			dao = ServiceFactory.getDao(ShippingStatusDao.class);
			for (Ahm010Dto dto : dtoList) {
				vinList.add(dto.getVin());
			}
			Timestamp cutDate = Timestamp.valueOf(new SimpleDateFormat(FORMAT_DATE).format(new Date()) + " " + cutOffTime);
			
			List<ShippingStatus> openStatusList = dao.getVinByStatus(openStatus, vinList, cutDate);
			List<String> outputMessages = writeMessage(openStatusList, cutOffTime, distributionList,
					emailSubject,openStatus, otherStatus, vinList, cutDate );
			
			//Create a report file
			writeToFile(outputMessages, resultPath + receivedFile +".txt");
			
			//Send the report file in an email
			sendEmail(resultPath + receivedFile +".txt", distributionList, emailSubject);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 	email report file
	 * @param reportFile	-	this is the path of the report
	 * @param emailList		-	this is the email distribution list
	 * @param emailSubject	-	this is the subject for the email
	 */
	private void sendEmail(String reportFile, String emailList, String emailSubject) {	
		StringBuffer msg = new StringBuffer();			
		msg.append("This e-mail has an attached with information about current inventory. \n");
		msg.append("Please review the attached inventory comparison report for details. \n");	
		MailContext mailContext = new MailContext();
		mailContext.setMessage(reportFile);
		mailContext.setSubject(emailSubject);
		mailContext.setMessage(msg.toString());
		mailContext.setRecipients(getPropertyBean().getAlertMailRecipients());
		Multipart multiPart = new MimeMultipart("attachment");
		try {
			MailSender.addFileAttachment((MimeMultipart) multiPart, reportFile);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MailSender.sendAsync(mailContext); 
	}
	
	private List<String> writeMessage(List<ShippingStatus> openStatusList, String cutOffTime, String distributionList,
			String emailSubject,String openStatus, String otherStatus, List<String> vinList, Timestamp cutDate){
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
		List<ShippingStatus> otherStatusList = dao.getVinInOtherStatus(otherStatus, vinList, cutDate);
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
		
		outputMessages.add("\nTotal number of inventory records from AH = "+ vinList.size());
		outputMessages.add("Total number of records matched with ALC = "+ (vinList.size() - otherStatusList.size()));
		outputMessages.add("Total number of records not matched with ALC = "+ otherStatusList.size());
		
		outputMessages.add("\nTotal number of records returned to ALC = "+ returned.size());
		System.out.println(outputMessages);
		return outputMessages;
	}
}
