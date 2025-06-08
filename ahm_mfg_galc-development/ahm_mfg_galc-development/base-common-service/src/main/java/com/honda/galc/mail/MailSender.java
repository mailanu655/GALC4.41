package com.honda.galc.mail;

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
/**
 * @author Subu Kathiresan
 * @date Apr 14, 2015
 * 
 */
public class MailSender {

	private static final String MESSAGE_BODY_SEPARATOR = "\n\n";
	private static final String PROP_SMTP_HOST = "mail.smtp.host";
	private static final String PROP_SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
	private static final String PROP_SMTP_READ_TIMEOUT = "mail.smtp.timeout";
	private static final String PROP_SMTP_WRITE_TIMEOUT = "mail.smtp.writetimeout";
	
	private static Logger logger;
	
	/**
	 * send mail asynchronously
	 * 
	 * @param context
	 */
	public static void sendAsync(final MailContext context) {
		try {
			Runnable asyncSend = new Runnable() {
				public void run() {
					send(context);
				}
			};
			new Thread(asyncSend).start();
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to send email asynchronously for " + context.toString());
		}	
	}
	
	/**
	 * Based on the passed in MailContext this method will send 
	 * a single part or a multi-part email message
	 * 
	 * @param context
	 * @return
	 */
	public static boolean send(MailContext context) {
	    Properties props = new Properties();
	    props.put(PROP_SMTP_HOST, context.getHost());
	    if (context.getTimeout() > -1) {
	    	props.put(PROP_SMTP_CONNECTION_TIMEOUT, context.getTimeout());
	    	props.put(PROP_SMTP_READ_TIMEOUT, context.getTimeout());
	    	props.put(PROP_SMTP_WRITE_TIMEOUT, context.getTimeout());
	    }
	    Session session = Session.getDefaultInstance(props, null);

	    try {
	        MimeMessage msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(context.getSender()));
	        msg.setRecipients(Message.RecipientType.TO, context.getRecipientAddresses());
	        msg.setSubject(context.getSubject());
	        msg.setSentDate(new Date());
	        
	        if (context.getFileAttachments().size() > 0) {		// Attachments present, send multipart msg
				createMultipartMessage(context, msg);
	        } else {
	        	msg.setText(context.getMessage());
	        }
	        Transport.send(msg);
	        getLogger().check("Email sent " + context.toString());
	        return true;
	    } catch (MessagingException mex) {
	        handleMessagingException(mex);
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	        getLogger().error(ex, "Unable to send mail: " + ex);
	    }
	    return false;
	}

	/**
	 * Create a MimeMultipart and add to MimeMessage
	 * 
	 * 1  Add MimeBodyPart for message body
	 * 2  Add MimeBodyPart for every file attachment
	 * 
	 * @param context
	 * @param msg
	 * @throws MessagingException
	 */
	public static void createMultipartMessage(MailContext context, MimeMessage msg) throws MessagingException {
		// create MimeMultipart.  Add message body and file attachments 
		MimeMultipart multipart = new MimeMultipart();
		addMessageBody(context, multipart);
		
		for (String filePath: context.getFileAttachments()) {
			addFileAttachment(multipart, filePath);
		}
		msg.setContent(multipart);
	}

	public static void addMessageBody(MailContext context, Multipart multipart) throws MessagingException {
		// create message body part
		MimeBodyPart msgBodyPart = new MimeBodyPart();
		msgBodyPart.setText(context.getMessage() + MESSAGE_BODY_SEPARATOR);
		multipart.addBodyPart(msgBodyPart);
	}

	public static void addFileAttachment(MimeMultipart multiPart, String filePath) throws MessagingException {
		// create file attachment body part
		MimeBodyPart attachment = new MimeBodyPart();
		FileDataSource fileDataSource = new FileDataSource(filePath);
		attachment.setDataHandler(new DataHandler(fileDataSource));
		attachment.setFileName(fileDataSource.getName());
		multiPart.addBodyPart(attachment);
	}
	
	public static void handleMessagingException(MessagingException mex) {
		
		getLogger().error(mex, "MessagingException occured");
		mex.printStackTrace();
		
		Exception ex = mex;
		do {
			//Loop through all messaging exceptions and create log entries for each
			if (ex instanceof SendFailedException) {
				SendFailedException sfex = (SendFailedException) ex;
				Address[] invalid = sfex.getInvalidAddresses();
				if (invalid != null) {
					getLogger().error(ex, "    ** Invalid Addresses");
					if (invalid != null) {
						for (int i = 0; i < invalid.length; i++)
							getLogger().error(ex, "" + invalid[i]);
					}
				}
				Address[] validUnsent = sfex.getValidUnsentAddresses();
				if (validUnsent != null) {
					getLogger().error(ex, "    ** Valid Unsent Addresses");
					if (validUnsent != null) {
						for (int i = 0; i < validUnsent.length; i++)
							getLogger().error(ex, "         " + validUnsent[i]);
					}
				}
				Address[] validSent = sfex.getValidSentAddresses();
				if (validSent != null) {
					getLogger().error(ex, "    ** Valid Sent Addresses");
					if (validSent != null) {
						for (int i = 0; i < validSent.length; i++)
							getLogger().error(ex, "         " + validSent[i]);
					}
				}
			}
			if (ex instanceof MessagingException)
				ex = ((MessagingException) ex).getNextException();
			else
				ex = null;
		} while (ex != null);
	}
	
	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger("MailSender");
		}
		return logger;
	}
}
