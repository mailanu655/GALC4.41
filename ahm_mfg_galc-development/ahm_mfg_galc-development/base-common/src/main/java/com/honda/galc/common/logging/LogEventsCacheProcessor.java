package com.honda.galc.common.logging;

import org.apache.logging.log4j.status.StatusLogger;


/**
 * @author Subu Kathiresan
 * @date Jul 13, 2015
 */
public class LogEventsCacheProcessor extends Thread {
	
	private LogEventsCacheHandler cacheHandler = null;
	
	public LogEventsCacheProcessor(LogEventsCacheHandler cacheHandler) {
		this.cacheHandler = cacheHandler;
		this.setPriority(Thread.MIN_PRIORITY);
		StatusLogger.getLogger().warn("Creating new LogEventsCacheProcessor " + this.getName());
	}

	public void run() {
		StatusLogger.getLogger().debug("Entering logEventsCacheProcessor thread.");
		
		while (true) {
			LogEventsCacheHandler.sleep(LogEventsCacheHandler.CACHE_PROCESS_INTERVAL);
			try {
				cacheHandler.processCachedItems();
			} catch (Exception ex) {
				ex.printStackTrace();
				StatusLogger.getLogger().error("Failed to process cached log items: " + (ex.getMessage() == null ? "" : ex.getMessage()));
			}
		}
	}
}
