package com.honda.galc.checkers;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Subu Kathiresan
 * @date Oct 2, 2014
 */
@SuppressWarnings("rawtypes") 
public class CheckPointsRegistry {

	private static CheckPointsRegistry instance = null;
	private ConcurrentHashMap<ICheckPoint, String> registryMap = new ConcurrentHashMap<ICheckPoint, String>();
	
	private CheckPointsRegistry() {}
	
	public static CheckPointsRegistry getInstance() {
		if (instance == null) {
			instance = new CheckPointsRegistry();
		}
		return instance;
	}
	
	public boolean isCheckPointConfigured(ICheckPoint checkPoint) {
		return registryMap.containsKey((checkPoint));
	}
	
	public String get(ICheckPoint key) {
		return registryMap.get(key);
	}
	
	public void register(ICheckPoint key, String value) {
		registryMap.put(key, value);
	}
	
	public void unregister(ICheckPoint key) {
		registryMap.remove(key);
	}
}
