package com.honda.galc.client;

import com.honda.galc.client.device.DeviceManager;

public class MQApplicationContext {
	
	private static MQApplicationContext mqApplicationContext;
	private String hostName;
	private Integer port;
	private String queueManager;
	private String channel;
	
	public static MQApplicationContext getInstance() {
		if (mqApplicationContext == null)
			mqApplicationContext = new MQApplicationContext();
	    return mqApplicationContext;
	}
	
	private MQApplicationContext(){
		
	}
	
	public MQApplicationContext(String hostname, Integer por, String qManager, String ch) {
		this.hostName = hostname;
		this.port = por;
		this.queueManager = qManager;
		this.channel = ch;
	}
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getQueueManager() {
		return queueManager;
	}
	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	

}
