package com.honda.galc.common.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.message.ObjectMessage;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Logger 
 * <P>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Jan 10, 2010</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */

public class Logger {

	private static final String className = Logger.class.getName();
    private static Map<String, Logger> loggers = new HashMap<String, Logger>();
    private org.apache.logging.log4j.Logger delegate = LogManager.getLogger(className);
    private LogContext logContext = null;
    private LogLevel logLevel = LogLevel.DATABASE;
	
	/**
	 * Private constructor to enforce factory methods use
	 * 
	 * @param argDelegate
	 */
	private Logger(String loggerName) {
		this.logContext = LogContext.getContext().clone();
		if(!className.equalsIgnoreCase(loggerName))
			this.logContext.setApplicationName(loggerName);
		if(logContext.getApplicationLogLevel() != null) {
			this.logLevel = logContext.getApplicationLogLevel();
		}
	}
	
	
	/**
	 * get default logger 
	 * @return
	 */
	public static Logger getLogger() {
	    return getLogger(className);
	}
	
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		logContext.setApplicationLogLevel(logLevel);
	}
	
	public static void updateLogLevel(LogLevel logLevel) {
		for(Logger logger : loggers.values()) {
			logger.setLogLevel(logLevel);
		}
	}
	
	public static void addAppenders(Appender newAppender){
		for(Logger logger : loggers.values()) {
			logger.addAppender(newAppender);
		}
	}
	
	public void addAppender(Appender newAppender){
		Logger.getCurrentConfiguration().addAppender(newAppender);
	}
	
	public static boolean isAppenderSubscribed(Appender appender){
		return (getCurrentConfiguration().getAppender(appender.getName()) != null ) ? true : false;
	}
	
	public static void removeAppender(Appender appender) {
		getCurrentConfiguration().getAppenders().remove(appender.getName());
	}

	public static Configuration getCurrentConfiguration() {
		return LoggerContext.getContext().getConfiguration();
	}
	
	
	/**
	 * Factory method to obtain logger
	 * 
	 * @param category
	 * @return
	 */
	public static Logger getLogger(String loggerName) {
		Logger logger = loggers.get(loggerName);
		if(logger == null) {
			synchronized (loggers) {
				logger = loggers.get(loggerName);
				if(logger == null) {
					logger = new Logger(loggerName);
					loggers.put(loggerName, logger);
				}
			}
		}
		return logger;
	}
	
	public static boolean hasLogger(String loggingClass) {
		return getLogger(loggingClass) != null;
	}
	
	
	public LogContext getLogContext() {
		return logContext;
	}

	public void setLogContext(LogContext logContext) {
		this.logContext = logContext;
	}

	/**
	 * @param userMessage
	 */
	public void debug(String... userMessage) {
		if(LogLevel.DEBUG.isHigher(logLevel)) {
			delegate.debug(getObjectMessage(userMessage));
		}
	}
	
	public void debug(long executionTime, String... userMessage) {
		if(LogLevel.DEBUG.isHigher(logLevel)) {
			delegate.debug(getObjectMessage(executionTime, userMessage));
		}
	}

	/**
	 * @param userMessage
	 * @param t - exception
	 */
	public void debug(Throwable t,String... userMessage) {
		if(LogLevel.DEBUG.isHigher(logLevel)) {
			delegate.debug(getObjectMessage(userMessage),t);
		}
	}
	
	public void debug(Throwable t,LogRecord logRecord) {
		if(LogLevel.DEBUG.isHigher(logLevel)) {
			delegate.debug(getObjectMessage(logRecord), t);
		}
	}

	public void debug(LogRecord logRecord) {
		if(LogLevel.DEBUG.isHigher(logLevel)) {
			delegate.debug(getObjectMessage(logRecord));
		}
	}
	/**
	 * Log error message
	 * 
	 * @param message
	 */
	public void error(String... message) {
		if(LogLevel.ERROR.isHigher(logLevel)) {
			delegate.error(getObjectMessage(message));
		}
	}
	
	/**
	 * Log error message
	 * @param t - exception
	 * @param message
	 */
	public void error(Throwable t,String... userMessage) {
		if(LogLevel.ERROR.isHigher(logLevel)) {
			delegate.error(getObjectMessage(userMessage),t);
		}
	}
	
	public void error(Throwable t,LogRecord logRecord) {
		if(LogLevel.ERROR.isHigher(logLevel)) {
			delegate.error(getObjectMessage(logRecord), t);
		}
	}

	public void error(LogRecord logRecord) {
		if(LogLevel.ERROR.isHigher(logLevel)) {
			delegate.error(getObjectMessage(logRecord));
		}
	}
	
	public void error(long executionTime, String... userMessage) {
		if(LogLevel.ERROR.isHigher(logLevel)) {
			delegate.error(getObjectMessage(executionTime, userMessage));
		}
	}
	
	/**
	 * Log fatal message
	 * 
	 * @param userMessage
	 */
	public void emergency(String... userMessage) {
		if(LogLevel.EMERGENCY.isHigher(logLevel)) {
			delegate.fatal(getObjectMessage(userMessage));
		}
	}
	
	/**
	 * Log fatal message
	 * @param userMessage
	 * @param t - exception
	 */
	public void emergency(Throwable t,String ... userMessage ) {
		if(LogLevel.EMERGENCY.isHigher(logLevel)) {
			delegate.fatal(getObjectMessage(userMessage),t);
		}
	}
	
	public void emergency(LogRecord logRecord) {
		if(LogLevel.EMERGENCY.isHigher(logLevel)) {
			delegate.fatal(getObjectMessage(logRecord));
		}
	}
	
	public void emergency(Throwable t,LogRecord logRecord) {
		if(LogLevel.EMERGENCY.isHigher(logLevel)) {
			delegate.fatal(getObjectMessage(logRecord),t);
		}
	}
	
	public void emergency(long executionTime, String... userMessage) {
		if(LogLevel.EMERGENCY.isHigher(logLevel)) {
			delegate.fatal(getObjectMessage(executionTime, userMessage));
		}
	}

	/**
	 * Log info message
	 * 
	 * @param userMessage
	 */
    public void info(String... userMessage) {
		if(LogLevel.INFO.isHigher(logLevel)) {
			delegate.info(getObjectMessage(userMessage));
		}
    }

    public void info(long executionTime, String... userMessage) {
		if(LogLevel.INFO.isHigher(logLevel)) {
			delegate.info(getObjectMessage(executionTime, userMessage));
		}
    }
    
	/**
	 * Log info message
	 * 
	 * @param userMessage
	 * @param t - exception
	 */
	public void info(Throwable t,String userMessage) {
		if(LogLevel.INFO.isHigher(logLevel)) {
			delegate.info(getObjectMessage(userMessage),t);
		}
	}
	
	public void info(LogRecord logRecord) {
		if(LogLevel.INFO.isHigher(logLevel)) {
			delegate.info(getObjectMessage(logRecord));
		}
	}
	
	public void info(Throwable t,LogRecord logRecord) {
		if(LogLevel.INFO.isHigher(logLevel)) {
			delegate.info(getObjectMessage(logRecord),t);
		}
	}

	/**
	 * Log check message
	 * 
	 * @param userMessage
	 */
    public void check(String... userMessage) {
		if(LogLevel.CHECK.isHigher(logLevel)) {
			delegate.log(LogLevel.CHECK.getLevel(), getObjectMessage(userMessage));
		}
    }
	
	/**
	 * Log check message
	 * 
	 * @param userMessage
	 * @param t - exception
	 */
	public void check(Throwable t,String userMessage) {
		if(LogLevel.CHECK.isHigher(logLevel)) {
			delegate.log(LogLevel.CHECK.getLevel(), getObjectMessage(userMessage), t);
		}
	}
	
	public void check(LogRecord logRecord) {
		if(LogLevel.CHECK.isHigher(logLevel)) {
			delegate.log(LogLevel.CHECK.getLevel(), getObjectMessage(logRecord));
		}
	}
	
	public void check(Throwable t,LogRecord logRecord) {
		if(LogLevel.CHECK.isHigher(logLevel)) {
			delegate.log(LogLevel.CHECK.getLevel(), getObjectMessage(logRecord), t);
		}
	}

	public void check(long executionTime, String... userMessage) {
		if(LogLevel.CHECK.isHigher(logLevel)) {
			delegate.log(LogLevel.CHECK.getLevel(), getObjectMessage(executionTime, userMessage));
		}
	}
	
	/**
	 * Log GUI message
	 * 
	 * @param userMessage
	 */
    public void gui(String... userMessage) {
		if(LogLevel.GUI.isHigher(logLevel)) {
			delegate.log(LogLevel.GUI.getLevel(), getObjectMessage(userMessage));
		}
    }
	
	/**
	 * Log Test message
	 * 
	 * @param userMessage
	 * @param t - exception
	 */
	public void gui(Throwable t,String userMessage) {
		if(LogLevel.GUI.isHigher(logLevel)) {
			delegate.log(LogLevel.GUI.getLevel(), getObjectMessage(userMessage),t);
		}
	}
	
	public void gui(LogRecord logRecord) {
		if(LogLevel.GUI.isHigher(logLevel)) {
			delegate.log(LogLevel.GUI.getLevel(), getObjectMessage(logRecord));
		}
	}
	
	public void gui(Throwable t,LogRecord logRecord) {
		if(LogLevel.GUI.isHigher(logLevel)) {
			delegate.log(LogLevel.GUI.getLevel(), getObjectMessage(logRecord), t);
		}
	}
	
	public void gui(long executionTime, String... userMessage) {
		if(LogLevel.GUI.isHigher(logLevel)) {
			delegate.log(LogLevel.GUI.getLevel(), getObjectMessage(executionTime, userMessage));
		}
	}
	
	/**
	 * Log database message
	 * 
	 * @param userMessage
	 */
    public void database(String... userMessage) {
		if(LogLevel.DATABASE.isHigher(logLevel)) {
			delegate.log(LogLevel.DATABASE.getLevel(), getObjectMessage(userMessage));
		}
    }
	
	/**
	 * Log check message
	 * 
	 * @param userMessage
	 * @param t - exception
	 */
	public void database(Throwable t,String userMessage) {
		if(LogLevel.DATABASE.isHigher(logLevel)) {
			delegate.log(LogLevel.DATABASE.getLevel(), getObjectMessage(userMessage), t);
		}
	}
	
	public void database(LogRecord logRecord) {
		if(LogLevel.DATABASE.isHigher(logLevel)) {
			delegate.log(LogLevel.DATABASE.getLevel(), getObjectMessage(logRecord));
		}
	}
	
	public void database(Throwable t,LogRecord logRecord) {
		if(LogLevel.DATABASE.isHigher(logLevel)) {
			delegate.log(LogLevel.DATABASE.getLevel(), getObjectMessage(logRecord), t);
		}
	}
	
	public void database(long executionTime, String... userMessage) {
		if(LogLevel.DATABASE.isHigher(logLevel)) {
			delegate.log(LogLevel.DATABASE.getLevel(), getObjectMessage(executionTime, userMessage));
		}
	}
	
	/**
	 * Check if debug is enabled
	 * 
	 * @return
	 */
	public boolean isDebugEnabled() {
		return delegate.isDebugEnabled();
	}

	/**
	 * Check if info enabled
	 * 
	 * @return
	 */
	public boolean isInfoEnabled() {
		return delegate.isInfoEnabled();
	}
	
	/**
	 * @param userMessage
	 */
	public void warn(String... userMessage) {
		if(LogLevel.WARNING.isHigher(logLevel)) {
			delegate.warn(getObjectMessage(userMessage));
		}
	}
	
	/**
	 * @param userMessage
	 * @param t - exception
	 */
	public void warn(Throwable t, String... userMessage) {
		if(LogLevel.WARNING.isHigher(logLevel)) {
			delegate.warn(getObjectMessage(userMessage), t);
		}
	}
	
	public void warn(LogRecord logRecord) {
		if(LogLevel.WARNING.isHigher(logLevel)) {
			delegate.warn(getObjectMessage(logRecord));
		}

	}
	public void warn(Throwable t, LogRecord logRecord) {
		if(LogLevel.WARNING.isHigher(logLevel)) {
			delegate.warn(getObjectMessage(logRecord), t);
		}
	}
	
	public void warn(long executionTime, String... userMessage) {
		if(LogLevel.WARNING.isHigher(logLevel)) {
			delegate.warn(getObjectMessage(executionTime, userMessage));
		}
	}
	
	private ObjectMessage getObjectMessage(LogRecord logRecord) {
		return new ObjectMessage(logRecord);
	}
	
	private ObjectMessage getObjectMessage(String... userMessage) {
		return new ObjectMessage(getLogRecord(userMessage));
	}
	
	private ObjectMessage getObjectMessage(long executionTime, String... userMessage) {
		return new ObjectMessage(getLogRecord(executionTime, userMessage));
	}

	private LogRecord getLogRecord(String... userMessage) {
		return new LogRecord(logContext, StringUtils.join(userMessage));
	}
	
	private LogRecord getLogRecord(long executionTime, String... userMessage) {
		return new LogRecord(logContext, executionTime, StringUtils.join(userMessage));
	}
	
}

