package com.honda.galc.property;

/**
 * @author Subu Kathiresan
 * @date Apr 14, 2015
 */
@PropertyBean(componentId ="SMTP_MAIL")
public interface SmtpMailPropertyBean extends IProperty {
	
	/**
	 * SMTP host 
	 * 
	 *   Examples:
	 *     SMTPGTW1.ham.am.honda.com
	 *     SMTPGTW2.ham.am.honda.com:26
	 */    
	@PropertyBeanAttribute(defaultValue = "")
	public String getHost();
	
	/**
	 * mail sender
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSender();
	
	/**
	 * mail recipients
	 * comma separated sequence of addresses. Addresses must follow RFC822 syntax.
	 *   Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
	 *   if(rfc2822.matcher(EMAIL_ADDRESS).matches()) {
	 *     // Well formed email
	 *   }
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRecipients();
	
	/**
	 * mail subject
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSubject();
	
	/**
	 * mail message
	 * for multi-line messages use \n as the line separator
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getMessage();
	
	/**
	 * mail connection timeout (in seconds)
	 */
	@PropertyBeanAttribute(defaultValue = "30")
	public int getConnectionTimeout();
	
	/**
	 * Indicates whether the straggler service should send an email notification when a straggler is created.
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isStragglerMailEnabled();
	
	/**
	 * Comma-separated sequence of addresses to receive straggler email notifications.<br>
	 * Addresses must follow RFC822 syntax.
	 */
	@PropertyBeanAttribute
	public String getStragglerMailRecipients();
	
	/**
	 * Sender address for straggler email notifications.<br>
	 * Sender address must follow RFC822 syntax.
	 */
	@PropertyBeanAttribute
	public String getStragglerMailSender();
	
	/**
	 * SMTP server for straggler email notifications.
	 */
	@PropertyBeanAttribute
	public String getStragglerMailHost();
}
