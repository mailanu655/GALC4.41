package com.honda.galc.client.device;

import com.honda.galc.client.ui.IEvent;
import com.honda.galc.device.IDevice;

/**
 * @author Subu Kathiresan
 * @date May 21, 2015
 */
public class DeviceStatusWidgetEvent<T extends IDevice> implements IEvent {
	
	private T device = null;
	private boolean show = false;
	
	public DeviceStatusWidgetEvent(T device, boolean show) {
		this.setDevice(device);
		this.setShow(show);
	}

	public T getDevice() {
		return device;
	}

	public void setDevice(T device) {
		this.device = device;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
}
