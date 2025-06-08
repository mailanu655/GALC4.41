package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

public class EngineLoadVin implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Tag(name="VIN")
	private String vin;

	public EngineLoadVin(){
	}
	
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
}
