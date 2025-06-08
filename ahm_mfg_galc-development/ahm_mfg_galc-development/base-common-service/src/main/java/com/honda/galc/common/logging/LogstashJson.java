package com.honda.galc.common.logging;



/**
 * @author Fredrick Yessaian
 * Feb 04 2017
 * 
 * */
public class LogstashJson {
	
	private String loggingTime = null;
	private String applicationName = null;
	private String hostName = null;
	private String clientName = null;	
	private String loglevel = null;
	private String threadName = null;
	private String logMessage = null;
	private String[] stackTrace = null;
	
	public LogstashJson(String loggingTime, String appName, String hostName,String clientName, String loglevel, String threadName, String logMessage, String[] stackTrace) {
		this.setLoggingTime(loggingTime);
		this.setApplicationName(appName);
		this.setHostName(hostName);
		this.setClientName(clientName);
		this.setLoglevel(loglevel);
		this.setLogMessage(logMessage);
		this.setThreadName(threadName);
		this.setStackTrace(stackTrace);
	}
	
	public String getLoggingTime() {
		return loggingTime;
	}

	public void setLoggingTime(String loggingTime) {
		this.loggingTime = loggingTime;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getLoglevel() {
		return loglevel;
	}

	public void setLoglevel(String loglevel) {
		this.loglevel = loglevel;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String[] getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String[] stackTrace) {
		this.stackTrace = stackTrace;
	}

}
