package com.honda.galc.common.logging;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Fredrick Yessaian
 * Feb 04 2017
 * 
 * */
@Plugin(name = "GALCJsonLayout", category = "Core", elementType = "layout", printObject = true)
public class GALCJsonLayout extends AbstractStringLayout implements Layout<String> {

	protected GALCJsonLayout(Charset charset) {
		super(charset);
	}

	private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private static SimpleDateFormat loggerTimeFmt = new SimpleDateFormat("EEE d MMM yyyy HH-mm-ss SSS z");
		
	public void activateOptions() {}

	public boolean ignoresThrowable() {
		return false;
	}

	public String toSerializable(LogEvent event) {
		LogRecord logRec = AppenderBase.getLogRecord(event);
		return createJsonString(logRec);
    }
	
	protected String createJsonString(LogRecord logRec){
		Timestamp logTs = new Timestamp( logRec.getTimestamp());
		LogContext logContext =  logRec.getLogContext();
		
		LogstashJson jsonObj = new LogstashJson(loggerTimeFmt.format(logTs), logContext.getApplicationName(), logContext.getHostName(), logContext.getClientName(), logRec.getLogLevel().getDescription().toUpperCase(), logContext.getThreadName() , logRec.getMessage().toString(), logRec.getStackTrace());
		logTs = null;
		logContext = null;
		return gson.toJson(jsonObj) +"\n";
	}
	
	public String toString() {
		return "GALCJsonLayout";
	}

    @PluginFactory
    public static GALCJsonLayout createLayout(@PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset) {
        return new GALCJsonLayout(charset);
    }
}
