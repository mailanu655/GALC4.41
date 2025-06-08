package com.honda.galc.system.oif.svc.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>OifServiceMinHandler</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Minimum implementation of the service handler
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update Date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 10, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL009</TD>
 * <TD>Initial Version</TD>
 * </TR>
 *
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Jul 09, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL035</TD>
 * <TD>added getApplicationName() for monitoring</TD>
 * </TR>
 *
 * </TABLE>
 */
public abstract class OifServiceMinHandler {

	/**
	 * Generic error message ID
	 */
	protected static final String MSG_GEN_ERR_ID = "HCMOIFE0301";

	/**
	 * Generic information message ID
	 */
	protected static final String MSG_GEN_INFO_ID = "HCMOIFI0302";

	/**
	 * GALC Logger
	 */
	protected Logger logger;
	
	/**
	 * System properties file
	 */
	protected Properties systemProperties;
	
	/**
	 * Debugging flag
	 */
	protected boolean debug;
	/**
	 * Defualt handler name
	 * */
	protected String handlerName = "undefinedName";
	
	/**
	 * GALC user running the process
	 */
	protected String userName = null;

	/**
	 * Constructor of the handler instance
	 * 
	 * @param handlerName
	 */
	public OifServiceMinHandler(String aHandlerName) {
		super();
		final String logMethod = "OifServiceMinHandler(aHandlerName)";
		
		this.logger = Logger.getLogger();
		
		this.handlerName = aHandlerName;
		
		try {	
			PropertyService.refreshComponentProperties(getPropertiesName());
//			setSystemProperties(
//					PropertyService.getPropertyHelper(getPropertiesName()).getProperties());
		} catch (Exception e) {
			logMessage("HCMOIFE0301", logMethod, "Cannot find properties for component: " 
					+ getPropertiesName() + ". Error: " + e.getMessage());
		}
	}

	/**
	 * Setter for system properties
	 * 
	 * @param aSystemProperties - system properties
	 */
	private void setSystemProperties(Properties aSystemProperties) {
		this.systemProperties = aSystemProperties;
	}

	/**
	 * Getter of system properties
	 * 
	 * @return system properties
	 */
	public Properties getSystemProperties() {
		return systemProperties;
	}

	/**
	 * Helper method for getting property as a boolean value
	 * 
	 * @param propertyKey - property key
	 * 
	 * @return property as a boolean value
	 */
	public boolean getSystemPropertyBoolean(String propertyKey) {
		return Boolean.parseBoolean(
				String.valueOf(systemProperties.getProperty(propertyKey)));
	}

	
	/**
	 * Property value getter
	 * 
	 * @param propertyKey - property key
	 * @return property value (String)
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public String getSystemProperty(String propertyKey) {
		return systemProperties.getProperty(propertyKey);
	}

	/**
	 * Setter for the debugging flag
	 * 
	 * @param set debug to true or flase
	 */
	public void setDebug(boolean pDebug) {
		debug = pDebug;
	}

	/**
	 * Getter for the debugging flag
	 * 
	 * @return debugging flag 
	 * 
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Set user name used by logging
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
//		Logger.initializeThread(this.userName, handlerName);
	}

	/**
	 * Write a message to the log file
	 * 
	 * @param msgId - message ID
	 * @param logMethod - method
	 * @param String - a message
	 */
	public void log(String msgId, String logMethod, String aMessage) {
		logMessage(msgId, logMethod, aMessage);
	}

	/**
	 * Write a error message to the log file
	 * 
	 * @param msgId - message ID
	 * @param method - method
	 * @param String - a message
	 */
	public void logError(String msgId, String method, String aMessage) {
		logMessage(msgId, method, "ERROR: " + aMessage);
	}

	/**
	 * Getter for the component ID of the configuration properties
	 * 
	 * @return properties component ID
	 * 
	 */
	public String getPropertiesName() {
		return getHandlerName();
	}

	/**
	 * The handler name getter
	 * 
	 * @return the handler name
	 */
	public String getHandlerName() {
		return handlerName;
	}

	/**
	 * Provide with the application name for monitoring purpose
	 * 
	 * @return the application name for monitoring purpose
	 */
	public String getApplicationName() {
		return handlerName;
	}

	/**
	 * Write message to the log
	 * 
	 * @param msgId - message ID
	 * @param logMethod - method name
	 * @param userMessage - user message
	 * 
	 */
	private void logMessage(String msgId, String logMethod, String userMessage) {
		
		logger.info("Method - " + logMethod + " " + userMessage);

	}

	/**
	 * Converts exception stack trace into the String
	 * 
	 * @param e - input Exception
	 * @return - stack trace as String
	 */
	protected String stackTraceToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

}
