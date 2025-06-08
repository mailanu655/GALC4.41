package com.honda.galc.common.logging;

import java.io.Serializable;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.message.ObjectMessage;

public abstract class AppenderBase extends AbstractAppender {

	protected AppenderBase(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}

	public static LogRecord getLogRecord(LogEvent event) {
	      Object obj = event.getMessage();
	      LogRecord logRecord;
	      if(obj instanceof ObjectMessage) {
	    	  Object parameter = ((ObjectMessage) obj).getParameter();
	    	  if(parameter instanceof LogRecord) {
		    	  logRecord = (LogRecord) parameter;
	    	  } else {
	    		  logRecord = createLogRecord(parameter);
	    	  }
	      } else {
	    	  logRecord = createLogRecord(obj);
	      }
	      logRecord.setLogLevel(getLogLevel(event));	      
	      if(event.getThrown() != null) {
	    	  logRecord.setStackTrace(ExceptionUtils.getStackFrames(event.getThrown()));
	      }
	      return logRecord;
	}
	
	public static LogRecord createLogRecord(Object obj) {
		return new LogRecord(obj == null ? "" : obj.toString());
	}

	public static LogLevel getLogLevel(LogEvent event) {
		for(LogLevel level : LogLevel.values()) {
			if(level.getLevel().equals(event.getLevel())) {
				return level;
			}
		}
		return LogLevel.INFO;
	}
}
