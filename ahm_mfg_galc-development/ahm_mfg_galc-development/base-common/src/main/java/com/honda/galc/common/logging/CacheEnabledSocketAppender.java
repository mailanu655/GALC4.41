package com.honda.galc.common.logging;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.status.StatusLogger;

/**
 * @author Subu Kathiresan
 * @Date July 12, 2012
 *
 */
@Plugin(name = "CacheEnabledSocketAppender", category = "Core", elementType = "appender", printObject = true)
public class CacheEnabledSocketAppender extends SocketAppender {


	protected CacheEnabledSocketAppender(String name, Filter filter, Layout<? extends Serializable> layout,
										String primaryHost, int primaryPort, String secondaryHost, int secondaryPort, int queueSize) {
		super(name, filter, layout, primaryHost, primaryPort, secondaryHost, secondaryPort, queueSize);
	}

	@Override
	public void activateOptions() {}
	
	@Override
	public void append(LogEvent event) {
		try {
			LogRecord logRecord = getLogRecord(event);
			if (logRecord != null) {
				addToLogEventsCache(logRecord);
			}
		} catch(Exception ex) {
			StatusLogger.getLogger().debug("CacheEnabledSocketAppender failed to enqueue LoggingEvent " + getLogRecord(event).getSingleLineMessage());
			fileWrite(event);
		}
	}

	/**
	 * adds a log event to the append queue
	 * @param logRecord
	 */
	private void addToLogEventsCache(LogRecord logRecord) {
		try {
			LogEventsCacheHandler.getInstance(this).addLogRecordToCache(logRecord);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String toString() {
		return this.getClass().getName() + " - " + (getName() == null ? "" : getName());
	}

	
	@PluginFactory
    public static CacheEnabledSocketAppender createAppender(@PluginAttribute("name") @Required(message = "No name provided for CacheEnabledSocketAppender") String name,
                                              @PluginAttribute("primaryHost") String primaryHost,
                                              @PluginAttribute("primaryPort") int primaryPort,
                                              @PluginAttribute("secondaryHost") String secondaryHost,
                                              @PluginAttribute("secondaryPort") int secondaryPort,
                                              @PluginAttribute("queueSize") int queueSize,
                                              @PluginElement("Layout") Layout<?> layout,
                                              @PluginElement("Filters") Filter filter) {
 
        CacheEnabledSocketAppender appender = new CacheEnabledSocketAppender(name, filter, layout, primaryHost, primaryPort, secondaryHost, secondaryPort, queueSize);
        appender.activateOptions();
        return appender;
    }
}

