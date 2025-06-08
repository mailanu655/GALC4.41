package com.honda.galc.common.logging;

import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


import org.apache.logging.log4j.status.StatusLogger;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2015
 */
public class LogEventsCacheHandler {
	
	public static final int CACHE_PROCESS_INTERVAL = 1;	
	public static final int QUEUE_FULL_ERR_MSG_LOG_INTERVAL = 10;
	public static final int QUEUE_CAPACITY = 5000;	
	public static final int CONNECTION_TIMEOUT = 4000;

	private static volatile LogEventsCacheHandler instance = null;
	private static volatile ReentrantLock reentrant = new ReentrantLock(true);
	
	private volatile LinkedBlockingQueue<LogRecord> appendQueue = null; 
	private volatile CacheEnabledSocketAppender appender = null;
	private volatile Thread logEventsProcessThread = null;
	private volatile int droppedMessageCount = -1;
	private volatile long queueMaxErrorLastLoggedAt = -1;

	private LogEventsCacheHandler(CacheEnabledSocketAppender appender) {
		this.appender = appender;
	}

	/**
	 * starts the daemon process that clears the log events cache periodically
	 */
	private void startCacheProcessor() {
		try {
			logEventsProcessThread = new LogEventsCacheProcessor(this);
			logEventsProcessThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void processCachedItems() {
		
		LogRecord logRecord = getNextLogRecord();
		if (logRecord != null) {
			if (!isValidConnection()) {
				switchConnection();
				return;
			}
			if (tryWrite(logRecord)) {
				removeElementFromCache();
			}
		}
	}
	
	private boolean tryWrite(LogRecord logRecord) {
		boolean successfulWrite = false;
		try {
			if (appender.socketWrite(logRecord)) {
				successfulWrite = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			StatusLogger.getLogger().error("Could not send LogRecord: " + logRecord.toString());
		}
		return successfulWrite;
	}
	
	/**
	 * removes LogRecord from head of queue
	 */
	private void removeElementFromCache() {
		try {
			LogRecord logRecord = getAppendQueue().remove();
			StatusLogger.getLogger().debug("Removed LogRecord " + logRecord.getMessage());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String getCurrentHostAndPort() {
		String hostAndPort = "";
		if(appender.getCurrentAppender() == CacheEnabledSocketAppender.APPENDER_SOCKET_PRIMARY) {
			hostAndPort = getPrimaryHost() + ":" + getPrimaryPort();	
		} else {
			hostAndPort = getSecondaryHost() + ":" + getSecondaryPort();
		}
		return hostAndPort;
	}

	private LogRecord getNextLogRecord() {
		LogRecord logRecord = null;
		
		try {
			logRecord = getAppendQueue().peek();
		} catch (Exception ex) {
			StatusLogger.getLogger().error("Error retrieving item from Append queue: " + ex.getMessage());
			ex.printStackTrace();
		}
		return logRecord;
	}

	public static LogEventsCacheHandler getInstance(CacheEnabledSocketAppender appender) {
		if (instance == null) {
			instance = new LogEventsCacheHandler(appender);
		}
		return instance;
	}

	public void addLogRecordToCache(LogRecord logRecord) {
		reentrant.lock();
		try {
			if(logEventsProcessThread == null) { 
				startCacheProcessor();
			}
		} finally {
			reentrant.unlock();
		}	
		if (getAppendQueue().remainingCapacity() > 0) {
			getAppendQueue().add(logRecord);
		} else {
			droppedMessageCount++;
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (getQueueMaxErrorLastLoggedAt() == -1 || (getQueueMaxErrorLastLoggedAt() + getErrorMsgLogInterval()) < currentTime) {
				StatusLogger.getLogger().error("Reached " + QUEUE_CAPACITY + " (max capacity) items in the in-memory queue. Dropped message count is " + droppedMessageCount);
				setQueueMaxErrorLastLoggedAt(currentTime);
			}
		}
	}

	public boolean isValidConnection() {
		if (appender.getSocket() != null 
				&& !appender.getSocket().isClosed() 
				&& appender.getSocket().isConnected()) {
			return true; 
		} else {
			StatusLogger.getLogger().warn(Thread.currentThread().getName()
					+ " Not connected to log server on " 
					+ getCurrentHostAndPort());
			return false;
		}
	}
	
	public void switchConnection() {
		appender.switchConnection();
		StatusLogger.getLogger().warn(Thread.currentThread().getName()
				+ " Switched connection to " 
				+ getCurrentHostAndPort()
				+ ". Waiting for " 
				+ CONNECTION_TIMEOUT 
				+ " milliseconds");
		sleep(CONNECTION_TIMEOUT);
	}
	
	public LinkedBlockingQueue<LogRecord> getAppendQueue() {
		if (appendQueue == null) {
			appendQueue = new LinkedBlockingQueue<LogRecord>(QUEUE_CAPACITY);
		}
		return appendQueue;
	}
	
	public int getErrorMsgLogInterval() {
		return QUEUE_FULL_ERR_MSG_LOG_INTERVAL * 60000;
	}
	
	public long getQueueMaxErrorLastLoggedAt() {
		return queueMaxErrorLastLoggedAt;
	}

	public void setQueueMaxErrorLastLoggedAt(long queueMaxErrorLastLoggedAt) {
		this.queueMaxErrorLastLoggedAt = queueMaxErrorLastLoggedAt;
	}

	public int getDroppedMessageCount() {
		return droppedMessageCount;
	}

	public void setDroppedMessageCount(int droppedMessageCount) {
		this.droppedMessageCount = droppedMessageCount;
	}
	
	private String getPrimaryHost() {
		return appender.getPrimaryHost();
	}
	
	private String getSecondaryHost() {
		return appender.getSecondaryHost();
	}
	
	private int getPrimaryPort() {
		return appender.getPrimaryPort();
	}
	
	private int getSecondaryPort() {
		return appender.getSecondaryPort();
	}
	
	public static void sleep(int sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
