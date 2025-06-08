package com.honda.galc.device.dataformat;

public abstract class DeviceResult {

	public static final String OK = "1";
	public static final String NG = "0";
	
	public abstract boolean isOk();
	
	public abstract boolean isSkipOperation();
}
