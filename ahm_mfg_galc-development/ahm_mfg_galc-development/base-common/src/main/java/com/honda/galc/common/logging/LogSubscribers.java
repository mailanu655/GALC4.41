package com.honda.galc.common.logging;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Subu Kathiresan
 * @date Oct 23, 2014
 */
public class LogSubscribers {

	private static LogSubscribers instance = null;
	
	private ConcurrentHashMap<String, String> logSubscribersMap = new ConcurrentHashMap<String, String>();
	
	private LogSubscribers() {}
	
	public static LogSubscribers getInstance() {
		if (instance == null) {
			instance = new LogSubscribers();
		}
		return instance;
	}
	
	public boolean isSubscriber(String subscriberKey) {
		return logSubscribersMap.containsKey(subscriberKey);
	}
	
	public void add(String key, String val) {
		if (!logSubscribersMap.containsKey(key)) {
			logSubscribersMap.put(key, val);
		}
	}
	
	public void remove(String key) {
		if (logSubscribersMap.containsKey(key)) {
			logSubscribersMap.remove(key);
		}
	}
}
