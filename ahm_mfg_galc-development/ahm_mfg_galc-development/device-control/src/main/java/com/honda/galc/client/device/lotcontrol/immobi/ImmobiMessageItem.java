package com.honda.galc.client.device.lotcontrol.immobi;

import com.honda.galc.common.logging.Logger;

/**
 * @author Subu Kathiresan
 * @date March 15, 2017
 */
public class ImmobiMessageItem {
	
	private ImmobiSerialDevice device;
	private String message;
	private Logger logger;
	
	public ImmobiMessageItem(ImmobiSerialDevice device, String message, Logger logger) {
		this.device = device;
		this.message = message;
		this.setLogger(logger);
	}

	public ImmobiSerialDevice getDevice() {
		return device;
	}
	
	public void setDevice(ImmobiSerialDevice device) {
		this.device = device;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
