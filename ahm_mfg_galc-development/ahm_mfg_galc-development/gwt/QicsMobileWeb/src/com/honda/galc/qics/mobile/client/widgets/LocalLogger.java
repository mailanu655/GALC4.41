package com.honda.galc.qics.mobile.client.widgets;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.util.DOMUtil;
import com.allen_sauer.gwt.log.shared.LogRecord;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;

/**
 * This is a logger that puts log messages in the record list for displaying by
 * the LogViewerPanel. This logger only keeps a finite set of messages.
 */
public class LocalLogger implements Logger {

	private static volatile LocalLogger localLogger = null;
	private static int MAX_RECORDS = 50;
	private static int nextId = 10000;
	private static final String STACKTRACE_ELEMENT_PREFIX = "&nbsp;&nbsp;&nbsp;&nbsp;at&nbsp;";
	private RecordList logMessageRecordList = new RecordList();
	
	public synchronized static LocalLogger getInstance() {
		if (localLogger == null) {
			localLogger = new LocalLogger();
		}
		return localLogger;
	}



	/**
	 * Default constructor.
	 */
	private LocalLogger() {

	}

	private String getFormatedMessage(LogRecord record) {
		return record.getFormattedMessage().replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	private String makeTitle(LogRecord record) {
		String message = record.getFormattedMessage();
		Throwable throwable = record.getThrowable();
		if (throwable != null) {
			if (throwable.getMessage() == null) {
				message = throwable.getClass().getName();
			} else {
				message = throwable.getMessage().replaceAll(
						throwable.getClass().getName()
								.replaceAll("^(.+\\.).+$", "$1"), "");
			}
		}
		return DOMUtil.adjustTitleLineBreaks(message).replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;").replaceAll("'", "\"");
	}

	private String getFormattedThrowable(LogRecord record) {
		StringBuilder text = new StringBuilder();
		Throwable throwable = record.getThrowable();
		if (throwable != null) {
			while (throwable != null) {
				/*
				 * Use throwable.toString() and not
				 * throwable.getClass().getName() and throwable.getMessage(), so
				 * that instances of UnwrappedClientThrowable, when stack trace
				 * deobfuscation is enabled) display properly
				 */
				text.append("<b>" + throwable.toString() + "</b>");
				StackTraceElement[] stackTraceElements = throwable
						.getStackTrace();
				if (stackTraceElements.length > 0) {
					text.append("<div class='log-stacktrace'>");
					for (StackTraceElement element : stackTraceElements) {
						text.append(STACKTRACE_ELEMENT_PREFIX + element
								+ "<br>");
					}
					text.append("</div>");
				}
				throwable = throwable.getCause();
				if (throwable != null) {
					text.append("Caused by: ");
				}
			}
		}
		return text.toString();
	}

	@Override
	public void log(LogRecord record) {
		// Only add records that are at or above the current log level
		if ( record.getLevel() >= Log.getCurrentLogLevel() ) {
			Record r = new Record();
			r.setAttribute("id", "" + genId() );
			r.setAttribute("title", makeTitle(record));
			r.setAttribute("info", getFormatedMessage(record));
			r.setAttribute("description", getFormattedThrowable(record));
			r.setAttribute("level", record.getLevel());
			logMessageRecordList.add(0, r);
	
			// Only keep a finite number of records. If we go over the limit,
			// delete the oldest record
			if (logMessageRecordList.size() > MAX_RECORDS) {
				logMessageRecordList.remove(MAX_RECORDS - 1);
			}
		}
	}

	protected synchronized static int genId() {
		return nextId++;
	}
	
	public RecordList getLogMessageRecordList() {
		return logMessageRecordList;
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public void setCurrentLogLevel(int level) {
		Log.setCurrentLogLevel(level);

	}

	@Override
	public void clear() {
		this.logMessageRecordList.clear();
	}
}
