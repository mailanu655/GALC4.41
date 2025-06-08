package com.honda.galc.common.logging;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "ConsoleAppender", category = "Core", elementType = "appender", printObject = true)
public class ConsoleAppender extends AppenderBase {


	protected ConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}

	public void append(LogEvent event) {
        System.out.println(getLayout().toSerializable(event));
 	}

	public void close() {
		
	}

	public boolean requiresLayout() {
		return false;
	}
	
	private String getMessage(LogEvent event) {
		LogRecord logRecord = getLogRecord(event);
		return logRecord.hasStackTrace() ? logRecord.getMultiLineMessage():logRecord.getSingleLineMessage();
	}

	public String toString() {
		return this.getClass().getName() + " - " + (getName() == null ? "" : getName());
	}

    @PluginFactory
    public static ConsoleAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                              @PluginElement("Layout") Layout<?> layout,
                                              @PluginElement("Filters") Filter filter) {
 
        if (layout == null) {
            layout = LogLayout.createLayout(null);
        }
        return new ConsoleAppender(name == null ? "Console" : name, filter, layout);
    }

}
