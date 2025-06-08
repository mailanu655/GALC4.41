package com.honda.galc.client.device.scanless;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.dataformat.InputData;

public class ScanlessTorqueMessage extends InputData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String IN_ZONE = "IN";
	public static final String ENTER_ZONE="ENTER";
	
	private String clientId;
	private String processPointId;
	private String toolProductId ="";
	private String toolEventType = "";//ENTER or LEAVE
	private String toolId = "";//processid-deviceid
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getToolProductId() {
		return toolProductId;
	}
	public void setToolProductId(String toolProductId) {
		this.toolProductId = toolProductId;
	}
	public String getToolEventType() {
		return toolEventType;
	}
	public void setToolEventType(String toolEventType) {
		this.toolEventType = toolEventType;
	}
	public String getToolId() {
		return toolId;
	}
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	
	public boolean isSameToolInZone() { 
		return StringUtils.equalsIgnoreCase(ENTER_ZONE, toolEventType)|| StringUtils.equalsIgnoreCase(IN_ZONE, toolEventType);
	}
	
	public boolean isSameProductId(String productId) {
		return toolProductId.equalsIgnoreCase(productId);
	}
	public boolean isSameDeviceId(String processDeviceId) {
		// TODO Auto-generated method stub
		return StringUtils.isEmpty(toolId) || StringUtils.equalsIgnoreCase(toolId, processDeviceId);
	}
	@Override
	public String toString() {
		return "ScanlessTorqueMessage [clientId=" + clientId + ", processPointId=" + processPointId + ", toolProductId="
				+ toolProductId + ", toolEventType=" + toolEventType + ", toolId=" + toolId + "]";
	}
	
	public ScanlessTorqueMessage clone()  {
		ScanlessTorqueMessage clone = new ScanlessTorqueMessage();
		clone.setClientId(this.getClientId());
		clone.setProcessPointId(this.getProcessPointId());
		clone.setToolId(this.getToolId());
		clone.setToolProductId(this.getToolProductId());
		clone.setToolEventType(this.getToolEventType());
		return clone;
	}
}
