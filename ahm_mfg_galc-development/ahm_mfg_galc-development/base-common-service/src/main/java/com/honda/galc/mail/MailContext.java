package com.honda.galc.mail;

import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.property.SmtpMailPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 14, 2015
 */
public class MailContext {

	//comma separated sequence of addresses. Addresses must follow RFC822 syntax.
	private String recipients;
	
	// list of file attachments.  Each item should be a file path 
	private ArrayList<String> fileAttachments = new ArrayList<String>();
	
	private String message;
	private String host;
	private String sender;
	private String subject;
	private int timeout = -1;
	
	public MailContext() {}
	
	public String getRecipients() {
		return StringUtils.trimToNull(recipients) == null ? 
	    		getMailPropertyBean().getRecipients() : StringUtils.trim(recipients);
	}
	
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	
	public ArrayList<String> getFileAttachments() {
		return fileAttachments;
	}

	public void setFileAttachments(ArrayList<String> fileAttachments) {
		this.fileAttachments = fileAttachments;
	}
	
	public String getMessage() {
		return StringUtils.trimToNull(message) == null ? 
	    		getMailPropertyBean().getMessage() : StringUtils.trim(message);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getHost() {
		return StringUtils.trimToNull(host) == null ? 
	    		getMailPropertyBean().getHost() : StringUtils.trim(host);
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public String getSender() {
		return StringUtils.trimToNull(sender) == null ? 
	    		getMailPropertyBean().getSender() : StringUtils.trim(sender);
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getSubject() {
		return StringUtils.trimToNull(subject) == null ? 
	    		getMailPropertyBean().getSubject() : StringUtils.trim(subject);
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * Get the connection, read and write timeout (in milliseconds).
	 */
	public int getTimeout() {
		return timeout;
	}
	
	/**
	 * Set the connection, read and write timeout (in milliseconds).
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	private SmtpMailPropertyBean getMailPropertyBean() {
		return PropertyService.getPropertyBean(SmtpMailPropertyBean.class);
	}
	
	public InternetAddress[] getRecipientAddresses() {
		InternetAddress[] recipientList = null;
		try {
			recipientList = InternetAddress.parse(getRecipients());
		} catch (AddressException ex) {
			ex.printStackTrace();
		}
		return recipientList;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
