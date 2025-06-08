package com.honda.galc.system.oif.svc.common;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyHelper;
import com.honda.galc.util.OIFConstants;

/**
 * <h3> 
 * This class is to send EMails to alert technical support about application errors generated from
 * OIF processes. 
 * </h3>
 *  <h4>Description</h4>
 *  <h4>Usage and Example</h4>
 *  <h4>Special Notes</h4>
 *  <h4>Change History</h4>
 *  <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 *  <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 *  <TR>
 *  <TD> Tom Tam </TD>
 *  <TD>10/06/2003</TD>
 *  <TD>Version 1</TD>
 *  <TD>Revision</TD>
 *  <TD>Initial creation</TD>
 *  </TR>
 *  </TABLE>
 *  @see
 *  @version 1.0
 *  @author Tom Tam
 *--------------------------------------------------------------------------------
 * Revision History 
 *--------------------------------------------------------------------------------
 * @version 1.1 - 
 * @author Tom Tam
 * @since Oct 06, 2003
 *--------------------------------------------------------------------------------
 * @version 1.2 
 * @author Larry Karpov 
 * @since March 31, 2014
 ****************************************************************************************************
 */
public class OifServiceEMailHandler extends PropertyHelper {
	private String aEmailAddressList = null;
	private String aMessageBody = null;
	private String aEmailHost = null;
	private String aEmailSender = null;
	private String aEmailSubject = null;
	private Logger logger;
	private boolean debug;

	public OifServiceEMailHandler(String componentId) {
		super(componentId);
		this.logger = Logger.getLogger("OifEMailHandler");
		initialize();
	}
	
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public String getEmailAddressList() {
		return aEmailAddressList;
	}
	public String getMessageBody() {
		return aMessageBody;
	}
	public String getEmailHost() {
		return aEmailHost;
	}
	public String getEmailSender() {
		return aEmailSender;
	}
	public String getEmailSubject() {
		return aEmailSubject;
	}
	public void setEmailAddressList(String pEmailAddressList) {
		aEmailAddressList = pEmailAddressList;

	}
	public void setMessageBody(String pMessageBody) {
		aMessageBody = pMessageBody;
	}
	public void setEmailHost(String pEmailHost) {
		aEmailHost = pEmailHost;
	}
	public void setEmailSender(String pEmailSender) {
		aEmailSender = pEmailSender;
	}
	public void setEmailSubject(String pEmailSubject) {
		aEmailSubject = pEmailSubject;
	}

	/**
	 * Initialize run time parameters.
	 * @return void
	 */
	private void initialize() {
		final String logMethod = "initialize()";
		this.setDebug(getPropertyBoolean(OIFConstants.EMAIL_DEBUG, debug));
		if(this.getEmailHost() == null) {
			this.setEmailHost(getProperty(OIFConstants.EMAIL_HOST));
		}
		if(this.getEmailSender() == null) {
			this.setEmailSender(getProperty(OIFConstants.EMAIL_SENDER));
		}
		if(this.getEmailAddressList() == null) {
			this.setEmailAddressList(
				getProperty(OIFConstants.EMAIL_DISTRIBUTION_LIST));
		}
		if(this.getEmailSubject() == null) {
			this.setEmailSubject(getProperty(OIFConstants.EMAIL_SUBJECT));
		}
		if(this.getMessageBody() == null) {
			this.setMessageBody(getProperty(OIFConstants.EMAIL_MESSAGE));
		}
		if (isDebug()) {
			logger.debug(logMethod, "EMAIL_HOST=" + this.getEmailHost());
			logger.debug(logMethod, "EMAIL_SENDER=" + this.getEmailSender());
			logger.debug(logMethod, "EMAIL_DISTRIBUTION_LIST=" + this.getEmailAddressList());
			logger.debug(logMethod, "SUBJECT=" + this.getEmailSubject());
			logger.debug(logMethod, "MSG=" + this.getMessageBody());
		}
	}
	
	/**
	 * delivery the email message
	 * @param String pSubject sets the email subject
	 * @param String pMessage sets the email body content
	 * @return void
	 */
	public void delivery(String pSubject, String pMessage) {
		InternetAddress[] vInternetAddress = null;
		//Calls the overloading method that uses the default receipient's email address
		delivery(pSubject, pMessage, vInternetAddress);
	}
	
	/**
	 * delivery the email message
	 * @param String pSubject sets the email subject
	 * @param String pMessage sets the email body content
	 * @param InternetAddress[] pDistributionList sets the receipient's email addresses 
	 * @return void
	 */
	public void delivery(
		String pSubject,
		String pMessage,
		InternetAddress[] pDistributionList) {

		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.host", this.getEmailHost());
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(isDebug());
		try {
			// create and send a message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(this.getEmailSender()));
			InternetAddress[] address = null;
			//If a distribution list is passed in, use it as the recipients' address. 
			//Otherwise, use the one set up in configuration file
			if (pDistributionList != null && pDistributionList.length > 0) {
				address = pDistributionList;
			} else {
				address = InternetAddress.parse(this.getEmailAddressList());
			}
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(pSubject);
			msg.setSentDate(new Date());
			msg.setContent(pMessage,"text/html");
			Transport.send(msg);
		} catch (MessagingException mex) {
			//Loop through all messaging exceptions and write them to the log file
			logger.error("\n--Exception handling in OIFEmailHandler");
			mex.printStackTrace();
			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						logger.error("    ** Invalid Addresses");
						if (invalid != null) {
							for (int i = 0; i < invalid.length; i++)
								logger.error("" + invalid[i]);
						}
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						logger.error("    ** ValidUnsent Addresses");
						if (validUnsent != null) {
							for (int i = 0; i < validUnsent.length; i++)
								logger.error("         " + validUnsent[i]);
						}
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						logger.error("    ** ValidSent Addresses");
						if (validSent != null) {
							for (int i = 0; i < validSent.length; i++)
								logger.error("         " + validSent[i]);
						}
					}
				}
				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;
			} while (ex != null);
		} catch (Exception vException ){
			logger.error(vException,
					"Exception in sending error using EMail " + vException.getMessage());
		}
	}

	/**
	 * delivery the email message
	 * @param String pSubject sets the email subject
	 * @param String pMessage sets the email body content
	 * @param String pFilename sets the file name that will be attached in the email
	 * @return void
	 */
	public void delivery(String pSubject, String pMessage, String pFileName) {

		final String logMethod = "delivery()";
		
		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.host",
			getProperty(OIFConstants.EMAIL_HOST));
		if (isDebug()) {
			logger.debug(logMethod, "EMAIL_HOST=" + this.getEmailHost());
			logger.debug(logMethod, "EMAIL_SENDER=" + this.getEmailSender());
			logger.debug(logMethod, "EMAIL_DISTRIBUTION_LIST=" + this.getEmailAddressList());
			logger.debug(logMethod, "SUBJECT=" + this.getEmailSubject());
			logger.debug(logMethod, "MSG=" + this.getMessageBody());
		}
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(isDebug());
		try {
			// create and send a message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(this.aEmailSender));
			InternetAddress[] address = null;
			if (this.getEmailAddressList() == null || this.getEmailAddressList().length() <= 0) {
				address =
					InternetAddress.parse(getProperty(OIFConstants.EMAIL_DISTRIBUTION_LIST));
			}
			else {
				address = InternetAddress.parse(this.getEmailAddressList());
			}


			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(pSubject);
			msg.setSentDate(new Date());
			// If the desired charset is known, you can use
			// setText(text, charset)
			//
			// create and fill the first message part
			MimeBodyPart vMimeBodyPart1 = new MimeBodyPart();
			vMimeBodyPart1.setText(pMessage + "\n\n");

			// create the second message part to attach the file
			MimeBodyPart vMimeBodyPart2 = new MimeBodyPart();

			// attach the file to the message part
			FileDataSource vFileDataSource = new FileDataSource(pFileName);
			vMimeBodyPart2.setDataHandler(new DataHandler(vFileDataSource));
			vMimeBodyPart2.setFileName(vFileDataSource.getName());

			// create the Multipart and its parts to it
			Multipart vMultipart = new MimeMultipart();
			vMultipart.addBodyPart(vMimeBodyPart1);
			vMultipart.addBodyPart(vMimeBodyPart2);

			// add the Multipart to the message
			msg.setContent(vMultipart);

			//msg.setText(pMessage);
			Transport.send(msg);
		} catch (MessagingException mex) {
			//Loop through all messaging exceptions and write them to the log file
			logger.error(logMethod, "Exception handling in OIFEmailHandler");
			mex.printStackTrace();
			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						logger.error(logMethod, "    ** Invalid Addresses");
						if (invalid != null) {
							for (int i = 0; i < invalid.length; i++)
								logger.error(logMethod, "         " + invalid[i]);
						}
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						logger.error(logMethod, "    ** ValidUnsent Addresses");
						if (validUnsent != null) {
							for (int i = 0; i < validUnsent.length; i++)
								logger.error(logMethod, "         " + validUnsent[i]);
						}
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						logger.error(logMethod, "    ** ValidSent Addresses");
						if (validSent != null) {
							for (int i = 0; i < validSent.length; i++)
								logger.error(logMethod, "         " + validSent[i]);
						}
					}
				}
				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;
			} while (ex != null);
		}
	}

}
