package com.honda.galc.client.logging;

import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.bushe.swing.event.EventBus;

import com.honda.galc.common.logging.AppenderBase;
import com.honda.galc.common.logging.LogRecord;

@Plugin(name = "EventBusAppender", category = "Core", elementType = "appender", printObject = true)
public class EventBusAppender extends AppenderBase {
	
	protected EventBusAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}
	public void append(LogEvent event) {
		LogRecord logRecord = getLogRecord(event);
		EventBus.publish(logRecord);
	}

	public void close() {
	}

	/**
	 * no layout required
	 */
	 public boolean requiresLayout() {
		 return false;
	 }

	public String toString() {
		return this.getClass().getName() + " - " + (getName() == null ? "" : getName());
	}
    @PluginFactory
    public static EventBusAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                              @PluginElement("Layout") Layout<?> layout,
                                              @PluginElement("Filters") Filter filter) {
        return new EventBusAppender(name == null ? "EventBus" : name, filter, layout);
    }
}
