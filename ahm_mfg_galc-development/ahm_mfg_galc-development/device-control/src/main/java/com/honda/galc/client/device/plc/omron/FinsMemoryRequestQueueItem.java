/**
 * 
 */
package com.honda.galc.client.device.plc.omron;

import com.honda.galc.client.device.plc.IPlcMemory;

/**
 * @author Subu Kathiresan
 * Nov 23, 2011
 */
public class FinsMemoryRequestQueueItem {

	private IPlcMemory _memoryLoc = null;
	private int _key = -1;
	private long _requestReceivedTime = -1;
	private long _expirationTime = -1;
	private StringBuilder _sendMsg = new StringBuilder();
	private StringBuilder _receivedMsg = new StringBuilder();
	private boolean _logFinsTraffic = true;
	private boolean _processingStarted = false;
	
	public FinsMemoryRequestQueueItem(int key, IPlcMemory plcMemory) {
		_key = key;
		_memoryLoc = plcMemory;
	}
	
	public FinsMemoryRequestQueueItem(IPlcMemory plcMemory, StringBuilder sendMessage) {
		_memoryLoc = plcMemory;
		_sendMsg = sendMessage;
	}
	
	public FinsMemoryRequestQueueItem(int key, IPlcMemory plcMemory, StringBuilder sendMessage, boolean logFinsTraffic) {
		_key = key;
		_memoryLoc = plcMemory;
		_sendMsg = sendMessage;
		_logFinsTraffic = logFinsTraffic;
	}
	
	public void setMemoryLoc(IPlcMemory plcMemory) {
		_memoryLoc = plcMemory;
	}
	
	public IPlcMemory getMemoryLoc() {
		return _memoryLoc;
	}
	
	public void setKey(int key) {
		_key = key;
	}
	
	public int getKey() {
		return _key;
	}
	
	public void setSendMessage(StringBuilder sendMsg) {
		_sendMsg = sendMsg;
	}
	
	public StringBuilder getSendMessage() {
		return _sendMsg;
	}
	
	public void setReceivedMessage(StringBuilder receivedMsg) {
		_receivedMsg = receivedMsg;
	}
	
	public StringBuilder getReceivedMessage() {
		return _receivedMsg;
	}
	
	public void setLogFinsTraffic(boolean logFinsTraffic) {
		_logFinsTraffic = logFinsTraffic;
	}
	
	public boolean isLogFinsTraffic() {
		return _logFinsTraffic;
	}
	
	public boolean isProcessingStarted() {
		return _processingStarted;
	}
	
	public void setProcessingStarted(boolean processingStarted) {
		_processingStarted = processingStarted;
	}
	
	public long getExpirationTime() {
		return _expirationTime;
	}
	
	public void setExpirationTime(long expirationTime) {
		_expirationTime = expirationTime;
	}
	
	public long getRequestReceievedTime() {
		return _requestReceivedTime;
	}
	
	public void setRequestReceivedTime(long requestReceivedTime) {
		_requestReceivedTime = requestReceivedTime;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _key;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FinsMemoryRequestQueueItem other = (FinsMemoryRequestQueueItem) obj;
		if (_key != other._key)
			return false;
		return true;
	}
}
