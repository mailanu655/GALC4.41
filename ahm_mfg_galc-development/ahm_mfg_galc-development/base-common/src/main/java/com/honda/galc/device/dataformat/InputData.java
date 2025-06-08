package com.honda.galc.device.dataformat;

import javax.persistence.Transient;

import com.honda.galc.device.IDeviceData;

public abstract class InputData implements IDeviceData{
	@Transient
	protected boolean inputData;

	public boolean isInputData() {
		return inputData;
	}

	public void setInputData(boolean inputData) {
		this.inputData = inputData;
	}
	
}
