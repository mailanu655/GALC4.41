package com.honda.galc.client.device.plc;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.product.InstalledPart;

public class DataCollectionInputData implements IDeviceData {
	
	private InstalledPart _installedPart;
	
	public DataCollectionInputData() {
		super();
	}

	public DataCollectionInputData(InstalledPart part) {
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
