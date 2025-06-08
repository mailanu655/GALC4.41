package com.honda.galc.common.logging;


import org.apache.logging.log4j.core.LogEvent;

import com.honda.galc.util.QueueProcessor;

public class SocketLogProcessor extends QueueProcessor<LogEvent> {
	public static final int RECONNECT_DELAY = 180000;
	public static final int MAX_RETRY_NUMBER = 6;
	
	private SocketAppenderBase appender;
	
	public SocketLogProcessor(int capacity, SocketAppenderBase appender) {
		super(capacity);
		this.appender = appender;
	}

	@Override
	public void run() {
    	LogEvent item;
        
        while(isRunning) {
            try {
            	if(appender.isConnected() || switchConnection()) {
                    item = queue.take();
                    processItem(item);
            	} else {
            		System.err.println("Cannot connect to log server(s). Retry after " + (RECONNECT_DELAY / 1000) + " seconds...");
            		sleep(RECONNECT_DELAY);
            	}
            } catch (InterruptedException e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
    }
	
	public boolean switchConnection() {
		int numberOfRetries = 0;
		while((!appender.isConnected()) && numberOfRetries++ < MAX_RETRY_NUMBER) {
			appender.switchConnection();
		}
		
		return appender.isConnected();
	}
	
	public void processItem(LogEvent event) {
		if(event == null) {
			return;
		}
		
		boolean result = false;
		if(appender.isConnected()) {
			result = appender.socketWrite(event);
			if(!result) {
				result = switchConnection() && appender.socketWrite(event);
			}
		} 
		
		if(!result) {
			appender.fileWrite(event);
		}
	}

 }
