package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;

public class HubPressData implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	private int stoke;
	private int pressure;
	
	public int getPressure() {
		return pressure;
	}
	
	
	//Getters & Setters
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	public int getStoke() {
		return stoke;
	}
	public void setStoke(int stoke) {
		this.stoke = stoke;
	}

}
