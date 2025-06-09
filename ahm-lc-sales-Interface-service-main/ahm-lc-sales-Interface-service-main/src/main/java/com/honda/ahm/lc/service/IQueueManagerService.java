package com.honda.ahm.lc.service;


public interface IQueueManagerService {
	
	public String send(String destinationQueueName, String message);
	
	public String recv(String sourceQueueName);
}
