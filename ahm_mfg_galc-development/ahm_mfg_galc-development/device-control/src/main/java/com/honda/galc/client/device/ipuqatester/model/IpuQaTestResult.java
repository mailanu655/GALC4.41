/**
 * 
 */
package com.honda.galc.client.device.ipuqatester.model;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde
 * @Date Apr 27, 2012
 *
 */
public class IpuQaTestResult implements IDeviceData {
	
	private InstalledPart _installedPart;
	
	public IpuQaTestResult() {
		super();
	}

	public IpuQaTestResult(InstalledPart part) {
		super();
		_installedPart = part;
	}

	public InstalledPart getInstalledPart() {
		return _installedPart;
	}

	public void setInstalledPart(InstalledPart installedPart) {
		_installedPart = installedPart;
	}
}
