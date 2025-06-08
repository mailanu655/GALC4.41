package com.honda.galc.client.device.lotcontrol;

import com.honda.galc.device.events.DeviceStatusInfo;

public class LaserEtcherDeviceStatusInfo extends DeviceStatusInfo {

	private String _displayMsg = null;
	private String _seqNumber = null;
	private String _vin = null;

	public void setDisplayMessage(String displayMsg) {
		_displayMsg = displayMsg;
	}

	public String getDisplayMessage() {
		return _displayMsg;
	}
	
	public void setSeqNumber(String seq) {
		_seqNumber = seq;
	}

	public String getSeqNumber() {
		return _seqNumber;
	}
	
	public void setVIN(String vin) {
		_vin = vin;
	}

	public String getVIN() {
		return _vin;
	}
}
