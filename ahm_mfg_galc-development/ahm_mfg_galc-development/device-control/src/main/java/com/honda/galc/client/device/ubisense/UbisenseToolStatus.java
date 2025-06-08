package com.honda.galc.client.device.ubisense;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.dataformat.InputData;

/**
 * Ubisense (ACS) tool telegram message
 * 
 * @author Bernard Leong
 * @date Jun 21, 2017
 */
public class UbisenseToolStatus extends InputData implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String toolInCell = "1";
	private String toolProductId ="";
	private String zoneId = "";
	private String toolLocation = "";
	private String errorCode = "0";
	private boolean connect = false;
	
	public UbisenseToolStatus() {}
	
	public UbisenseToolStatus connected(boolean connectError) {
		connect = connectError;
		return this;
	}
	
	public String getToolProductId() {
		return toolProductId;
	}
	
	public String getZoneId() {
		return zoneId;
	}
	
	public String getToolLocation() {
		return toolLocation;
	}
	
	public boolean isInError() {
		return Integer.valueOf(errorCode) > 1;
	}
	
	public boolean isConnected() {
		return connect;
	}
	
	public void setConnect(boolean connectError) {
		connect = connectError;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setToolProductId(String toolProductId) {
		this.toolProductId = toolProductId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public void setToolLocation(String toolLocation) {
		this.toolLocation = toolLocation;
	}

	public boolean isToolInZone() {
		return toolLocation.equalsIgnoreCase(toolInCell);
	}
	
	public boolean isSameZone(String deviceId) { 
		return StringUtils.isEmpty(deviceId) || StringUtils.equals(zoneId, deviceId);
	}
	
	public boolean isSameProductId(String productId) {
		return toolProductId.equalsIgnoreCase(productId);
	}
	
	public UbisenseToolStatus parseTelegram(String telegram) {
		connect = true;
		errorCode = telegram.substring(23, 24);
		toolProductId = telegram.substring(71, 88);
		zoneId = StringUtils.replace(telegram.substring(88, 120), "*", "");
		toolLocation = telegram.substring(120, 121);
		return this;
	}

	public boolean isToolInZone(String deviceId) {
		return isSameZone(deviceId) && isToolInZone();
	}

	public boolean isForCurrentZone(String productId, String deviceId) {
		
		return isSameProductId(productId) && isSameZone(deviceId);
	}

	@Override
	public String toString() {
		StringBuffer sb = new  StringBuffer();
		sb.append("[");
		sb.append("\"").append("toolProductId:").append(toolProductId).append("\"");
		sb.append(",\"").append("zoneId:").append(zoneId).append("\"");
		sb.append(",\"").append("toolLocation:").append(toolLocation).append("\"");
		sb.append(",\"").append("connect:").append(connect).append("\"");
		sb.append(",\"").append("errorCode:").append(errorCode).append("\"");
		sb.append("]");
		return sb.toString();
	}

	public UbisenseToolStatus clone()  {
		UbisenseToolStatus clone = new UbisenseToolStatus();
		clone.setConnect(this.isConnected());
		clone.setErrorCode(this.getErrorCode());
		clone.setToolProductId(this.getToolProductId());
		clone.setToolLocation(this.getToolLocation());
		clone.setZoneId(this.getZoneId());
		return clone;
	}
	
	
}
