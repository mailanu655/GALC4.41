package com.honda.galc.client.device.lotcontrol.immobi;

import com.honda.galc.device.events.DeviceStatusInfo;




/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public class ImmobiDeviceStatusInfo extends DeviceStatusInfo 
{
	private String _displayMsg = null;
	private String _seqNumber = null;
	private String _vin = null;
	private Integer _state = null;

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

	public Integer getState() {
		return _state;
	}

	public void setState(Integer _state) {
		this._state = _state;
	}
	
}
