package com.honda.galc.service.printing;

import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.Device;

public interface IPrinterDevice {
	
	public void print(byte[] printData,Device device, DataContainer dc);
	
}
