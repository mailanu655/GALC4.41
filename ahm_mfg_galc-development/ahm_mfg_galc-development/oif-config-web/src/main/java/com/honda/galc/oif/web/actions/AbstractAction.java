package com.honda.galc.oif.web.actions;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.struts.action.Action;

import com.honda.galc.common.logging.Logger;

public abstract class AbstractAction extends Action {

   protected Logger logger;

	/**
	 * Default constructor
	 */
	public AbstractAction() {
		this.logger = Logger.getLogger();
	}

	/**
	 * Write a error message to the log file
	 * @param msgId message ID
	 * @param logMethod method
	 * @param String a message
	 */
	protected void logError(String logMethod, String aMessage) {
		logger.error("Method : " + logMethod + " " +  aMessage);
	}

	protected void logError(String source,String logMethod, String aMessage) {
		logger.error(source + " Method : " + logMethod + " " +  aMessage);
	}

	/**
	 * Write a error message to the log file
	 * @param msgId message ID
	 * @param logMethod method
	 * @param String a message
	 */
	protected void logException(String logMethod, Throwable e) {
		logger.error(e, "Log Method : " + logMethod);
	}

	protected void logException(String source,String logMethod, Throwable e) {
		logger.error(e, source + " Method : " + logMethod);
	}
	/**
	 * @param msgId - message ID
	 * @param logMethod - method name
	 */
	protected void logMessage(String logMethod, String userMessage) {

		logger.info("Log Method : " + logMethod + " ", userMessage);
	}
	
	protected void logMessage(String source,String logMethod, String userMessage) {

		logger.info(source + " Method : " + logMethod + " ", userMessage);
	}
	/**
	 * Coverts exception stack trace into one String
	 * 
	 * @param e - input Exception
	 * @return - stack trace in String
	 */
	protected static String stackTraceToString(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}


}
