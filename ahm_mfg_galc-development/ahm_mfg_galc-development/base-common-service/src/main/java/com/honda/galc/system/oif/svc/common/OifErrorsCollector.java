package com.honda.galc.system.oif.svc.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.oif.RunHistory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LDAPService;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>OifErrorsCollector.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OifErrorsCollector.java description </p>
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
 * <TD>March 31, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Larry Karpov
 * @created March 31, 2014
 */
public class OifErrorsCollector {

	
	private String serviceId;
	private LogLevel notificationLevel;
	private boolean isNotification = true;
	private List<String> errorList = new ArrayList<String>();
	private RunHistory runHistory = null;
	private Boolean bErrorExists = false;

	
	public OifErrorsCollector() {
		initialize();
	}
	
	
	public RunHistory getRunHistory() {
		if(runHistory==null) runHistory = new RunHistory();
		return runHistory;
	}

	public void setRunHistory(RunHistory runHistory) {
		this.runHistory = runHistory;
	}
	
	
	public OifErrorsCollector(String serviceId) {
		this.serviceId = serviceId;
		runHistory = new RunHistory(); 
		initialize();
	}
	
	private void initialize() {
		String nl = getEmailProperty(OIFConstants.EMAIL_NOTIFICATION_LEVEL);
		if("EMERGENCY".equalsIgnoreCase(nl)) {
			this.notificationLevel = LogLevel.EMERGENCY;
		} else if("NONE".equalsIgnoreCase(nl)) {
			isNotification = false;
		} else {
			this.notificationLevel = LogLevel.ERROR;
		}
	}

	public LogLevel getNotificationLevel() {
		return notificationLevel;
	}

	public void setNotificationLevel(LogLevel notificationLevel) {
		this.notificationLevel = notificationLevel;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	OifErrorsCollector(LogLevel notificationLevel) {
		this.notificationLevel = notificationLevel;
	}
	
//	Collect errors
	public void info(String... userMessages) {
		//if notification property is set: if it is error or higher, don't log the message
		if(isNotification && !notificationLevel.isHigher(LogLevel.ERROR)) { 
			StringBuffer message = new StringBuffer("OIF Info. ");
			for (String userMessage : userMessages) {
				message.append(userMessage);
			}
			errorList.add(message.toString());
		}
	}
	
	public void infoMessage(String... userMessages) {
		//if notification property is set: if it is error or higher, don't log the message
		if(isNotification) { 
			StringBuffer message = new StringBuffer("OIF Info. ");
			for (String userMessage : userMessages) {
				message.append(userMessage);
			}
			errorList.add(message.toString());
		}
	}

	public void error(String... userMessages) {
		if(isNotification && !notificationLevel.isHigher(LogLevel.EMERGENCY)) { 
			StringBuffer message = new StringBuffer("OIF Error. ");
			for (String userMessage : userMessages) {
				message.append(userMessage);
			}
			errorList.add(message.toString());
		}
		setRunStatus(OifRunStatus.FAILURE);
	
	}

//	Collect errors
	public void error(Throwable t, String... userMessages) {
		if(isNotification && !notificationLevel.isHigher(LogLevel.EMERGENCY)) { 
			StringBuffer message = new StringBuffer("OIF Error. ");
			message.append(t).append(", ");
			for (String userMessage : userMessages) {
				message.append(userMessage);
			}
			errorList.add(message.toString());
		}
		setRunStatus(OifRunStatus.FAILURE);
	}

//	Collect Emergency errors
	public void emergency(String... userMessages) {
		if(isNotification) { 
			StringBuffer message = new StringBuffer("OIF Emergency. ");
			for (String userMessage : userMessages) {
				message.append(userMessage);
			}
			errorList.add(message.toString());
		}
		setRunStatus(OifRunStatus.FAILURE);
	}

//	Collect Emergency errors
	public void emergency(Throwable t, String... userMessages) {
		if(isNotification) { 
			StringBuffer message = new StringBuffer("OIF Emergency. ");
			message.append(t).append(", ");
			for (String userMessage : userMessages) {
				message.append(userMessage);
			}
			errorList.add(message.toString());
		}
		setRunStatus(OifRunStatus.FAILURE);
	}

//	Prepare message body and call emailHandler
	public void sendEmail() {
		if(isNotification && !errorList.isEmpty()) {
			OifServiceEMailHandler eMailHandler = new OifServiceEMailHandler(OIFConstants.OIF_NOTIFICATION_PROPERTIES);
			eMailHandler.setEmailAddressList(getEmailProperty(OIFConstants.EMAIL_DISTRIBUTION_LIST));
			StringBuffer msg = new StringBuffer();
			String strLine = getEmailProperty(OIFConstants.EMAIL_ID);
			if(strLine != null) {
				msg.append(strLine).append(".\n");
			}
			strLine = getEmailProperty(OIFConstants.MESSAGE_LINE1);
			if(strLine != null) {
				msg.append(strLine).append("\n");
			}
			strLine = getEmailProperty(OIFConstants.MESSAGE_LINE2);
			if(strLine != null) {
				msg.append(strLine).append("\n");
			}
			for(String error : errorList) {
				msg.append(error).append("\n");
			}
			eMailHandler.delivery(getEmailProperty(OIFConstants.EMAIL_SUBJECT), msg.toString());
		}
		
	}

//	Prepare message body and call emailHandler including to the user who started the job
	public void sendEmail(String jobRanBy) {
		if(isNotification && !errorList.isEmpty()) {
			StringBuffer emailDistributionList = new StringBuffer(getEmailProperty(OIFConstants.EMAIL_DISTRIBUTION_LIST));
			
			OifServiceEMailHandler eMailHandler = new OifServiceEMailHandler(OIFConstants.OIF_NOTIFICATION_PROPERTIES);
			if(StringUtils.isNotEmpty(jobRanBy)) {
				emailDistributionList.append( ","+ LDAPService.getInstance().getAssociateEmail(jobRanBy));
			}
			
			eMailHandler.setEmailAddressList(emailDistributionList.toString());
			StringBuffer msg = new StringBuffer();
			String strLine = getEmailProperty(OIFConstants.EMAIL_ID);
			if(strLine != null) {
				msg.append(strLine).append(".\n");
			}
			strLine = getEmailProperty(OIFConstants.MESSAGE_LINE1);
			if(strLine != null) {
				msg.append(strLine).append("\n");
			}
			for(String error : errorList) {
				msg.append(error).append("\n");
			}
			eMailHandler.delivery(getEmailProperty(OIFConstants.EMAIL_SUBJECT), msg.toString());
		}
		
	}
//	Get a property for key/serviceId or for key if key/serviceId is not specified 
	private String getEmailProperty(String key) {
		String propValue = null;
		if(serviceId != null) {
			String propKey = new StringBuilder(key).append("{").append(serviceId).append("}").toString();
			propValue = PropertyService.getProperty(OIFConstants.OIF_NOTIFICATION_PROPERTIES, propKey);
		}
//		if there is no specific property for specified serviceId ( <propKey>{<serviceId>} )
//		then check if there is a property common for all (<propKey>)
		if(propValue == null) {
			propValue = PropertyService.getProperty(OIFConstants.OIF_NOTIFICATION_PROPERTIES, key);
		}
		if(propValue == null) {
			propValue = new StringBuffer(OIFConstants.UNDEFINED_PROPERTIES).append(" for ").append(key).append(" and ").append(serviceId).toString();
		}
		return propValue; 
	}


	public Boolean isErrorExists() {
		return bErrorExists;
	}


	public void setErrorExists(Boolean bErrorExists) {
		this.bErrorExists = bErrorExists;
	}
	
	private void setRunStatus(OifRunStatus status) {
		bErrorExists =true;
		if(getRunHistory().getStatus() == null)	runHistory.setStatus(status);
	}
	


	
}
