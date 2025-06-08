package com.honda.galc.common.logging;


import java.nio.charset.Charset;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

@Plugin(name = "LogLayout", category = "Core", elementType = "layout", printObject = true)
public class LogLayout extends AbstractStringLayout implements Layout<String> {

 

    public LogLayout(Charset charset) {
		super(charset);
	}

	public String toSerializable(LogEvent event) {
        LogRecord logRecord = AppenderBase.getLogRecord(event);
        return logRecord.getSingleLineMessage();
	}

    public boolean ignoresThrowable() {
        return true;
    }

    public void activateOptions() {
        
    }
    
	public String toString() {
		return this.getClass().getName();
	}

    @PluginFactory
    public static LogLayout createLayout(@PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset) {
        return new LogLayout(charset);
    }
}
