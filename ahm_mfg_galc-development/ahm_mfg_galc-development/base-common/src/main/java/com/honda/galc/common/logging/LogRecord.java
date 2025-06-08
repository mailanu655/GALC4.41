package com.honda.galc.common.logging;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class LogRecord implements Serializable{
    
    private static final long serialVersionUID = 1L;
    public static LogLevel defaultLevel = LogLevel.DATABASE;
    
    private char STX = 2;
    private char ETX = 3;
    private LogContext logContext;
    private String message;
    private LogLevel logLevel = defaultLevel;
    private long timestamp;
    private String[] stackTrace;
    private long executionTime = -1;
    
    public LogRecord(LogContext logContext, String message) {
        this.message = message;
        this.logContext = logContext;
        this.timestamp = System.currentTimeMillis();
    }
       
    public LogRecord(LogContext logContext, long executionTime, String message) {
    	this(logContext, message);
    	this.executionTime = executionTime;
    }
    
    public LogRecord(String message) {
        this.message = message;
        this.logContext = LogContext.getThreadLocalContext();
        this.timestamp = System.currentTimeMillis();
    }
    
    public LogRecord(LogLevel level, String message){
        this(message);
        this.logLevel = level;
    }
    
    public LogContext getLogContext() {
        return logContext;
    }

    public void setLogContext(LogContext logContext) {
        this.logContext = logContext;
    }

    public String getMessage() {
    	if (message == null)
    		message = "";
    	
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
    
    public String[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getStackTraceString() {
        if (stackTrace == null) return null;
        StringBuilder builder = new StringBuilder();
        for(String str : stackTrace) {
            builder.append(str);
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }
    
    public String getSingleLineStackTraceString() {
        if (stackTrace == null) return null;
        StringBuilder builder = new StringBuilder(" -- Exception occurred: ");
        for(String str : stackTrace) {
        	builder.append(str);
        }
        return builder.toString();
    }
    
    public boolean hasStackTrace() {
    	return stackTrace != null && stackTrace.length > 0;
    }
    
    public boolean hasExecutionTime() {
    	return executionTime != -1;
    }
    
    public String toString() {
        return message;
    }
    
    public boolean needLog(LogLevel level) {
        return logLevel.isHigher(level);
    }
    
    public boolean isMultipleLine() {
    	if(hasStackTrace()) return true;
    	return logContext != null && logContext.isMultipleLine();
    }
    
    public boolean isCenterLog() {
    	return logContext != null && logContext.isCenterLog();
    }
    
    public String getSingleLineMessage() {
        StringBuilder result = new StringBuilder();
        try {
            result.append(DateFormatUtils.format(System.currentTimeMillis(),"HH:mm:ss"));
            result.append(" ");
            result.append(DateFormatUtils.format(timestamp,"yy-MM-dd HH:mm:ss.SSS"));
            result.append(" ");
            result.append(logLevel.getDescription());
            result.append(" {");
            result.append(logContext.getApplicationName());
            if(logContext.getHostName() != null){
                result.append("-");
                result.append(logContext.getHostName());
            }
            result.append("} ");
            result.append(replaceNonPrintableCharacters(message.replace("__{", "{").replace("}__", "}").replace("^", "  ")));
        }
        catch (Exception e) {
            if(result.length()>0) return result.toString();
        }
        return result.toString();
    }
    
    public String getSingleLineMessage(int limit) {
        String str = this.getSingleLineMessage();
        if(limit > 0 && str.length() > limit) return str.substring(0,limit -1) + "...(see detail log)";
        else return str;
    }
    
    public String getMultiLineMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(DateFormatUtils.format(System.currentTimeMillis(),"yy-MM-dd HH:mm:ss.SSS"));
        builder.append(System.getProperty("line.separator"));
        appendLine("MESSAGE TYPE",logLevel.getDescription(),builder);
        appendLine("MESSAGE",message,builder);
        appendLine("TIMESTAMP",DateFormatUtils.format(timestamp,"yy-MM-dd HH:mm:ss.SSS"),builder);
        appendLine("APPLICATION NAME", (logContext == null ? "" : logContext.getApplicationName()),builder);
        if(logContext.getClientName()!=null)
            appendLine("TERMINAL NAME",logContext.getClientName(),builder);
        if(logContext.getHostName()!=null)
            appendLine("HOST NAME",logContext.getHostName(),builder);
        if(logContext.getThreadName()!=null)
            appendLine("THREAD NAME",logContext.getThreadName(),builder);
        if(hasExecutionTime()) {
            appendLine("EXECUTION TIME", Long.toString(getExecutionTime()) + " ms", builder);
        }
        if(stackTrace != null) {
            for(String str : stackTrace) {
                builder.append(str);
                builder.append(System.getProperty("line.separator"));
            } 
        }
        return builder.toString();
    }
    
    public void appendLine(String messageName,String message,StringBuilder builder){
        builder.append("[");
        builder.append(messageName);
        int length = messageName.length();
        if(length < 20) builder.append(StringUtils.repeat(" ",20 - length));
        builder.append("] = ");
        builder.append(message);
        builder.append(System.getProperty("line.separator"));
    }
    
    public String getClassicLogString(){
        String message = replaceNonPrintableCharacters(getMessage());
        
    	String logString = new String(
        		"FILENAME"
                + STX
                + getLogContext().getApplicationName()
                + ETX
                + "ASSOCIATENO"
                + STX
                + ""
                + ETX
                + "MESSAGEID"
                + STX
                + ""
                + ETX
                + "MESSAGEPRIORITY"
                + STX
                + ""
                + ETX
                + ""
                + STX
                + ""
                + ETX
                + "MESSAGETYPE"
                + STX
                + getLogLevel().getType()
                + ETX
                + "SOURCECLASSNAME"
                + STX
                + ""
                + ETX
                + "SOURCEMETHODNAME"
                + STX
                + ""
                + ETX
                + "THREADID"
                + STX
                + getLogContext().getThreadName()
                + ETX
                + "TIMESTAMP"
                + STX
                + new java.sql.Timestamp(new java.util.Date().getTime())
                + ETX
                + "USERMESSAGE"
                + STX
                + message
                + (hasStackTrace()? getSingleLineStackTraceString(): "")
    			+ ETX
    			+ (hasExecutionTime()? ("EXECUTION_TIME" + STX + executionTime + " ms" + ETX) : "")
    			+ "\n");
    	
    	return logString;
    }

    /**
     * replaces all non-printable characters with a hexadecimal string equivalent
     * @param message
     * @return
     */
	public static String replaceNonPrintableCharacters(String message) {
		if (message == null)
			return "";
		
		StringBuilder strB = new StringBuilder();
		for (char singleChar: message.toCharArray()) {
        	if (singleChar < 32 || singleChar > 126)		// ASCII values between 32 and 126 are printable
        		strB.append("[" + Integer.toHexString(singleChar)+ "]");
        	else
        		strB.append(singleChar);
        }
		return strB.toString();
	}
	
    /**
     * replaces all non-printable characters with a hexadecimal string equivalent
     * @param message
     * @return
     */
	public static String replaceNonPrintableCharacters(StringBuffer strBuf) {
		if (strBuf == null)
			return "";
		
		StringBuilder strB = new StringBuilder();
		for (int i = 0; i < strBuf.length(); i++) {
			char singleChar = strBuf.charAt(i);
        	if (singleChar < 32 || singleChar > 126)		// ASCII values between 32 and 126 are printable
        		strB.append("[" + Integer.toHexString(singleChar)+ "]");
        	else
        		strB.append(singleChar);
        }
		return strB.toString();
	}
}
