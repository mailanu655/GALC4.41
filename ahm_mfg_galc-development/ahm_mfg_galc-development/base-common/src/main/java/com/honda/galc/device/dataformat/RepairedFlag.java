package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;
public class RepairedFlag implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	public static final String OK = "1";
	public static final String NG = "0";
	
	@Tag(name="REPAIRED_FLAG")
	private String completeFlag;

	public RepairedFlag() {
	}
	
	public RepairedFlag(String text) {
		this.completeFlag = text;
	}
	
	
	public String getCompleteFlag() {
		return completeFlag;
	}



	public void setCompleteFlag(String completeFlag) {
		this.completeFlag = completeFlag;
	}

	public String toString() {
		return getClass().getSimpleName() + "(" + getCompleteFlag() + ")";
	}

	public static RepairedFlag OK() {
		return new RepairedFlag(OK);
	}
	
	public static RepairedFlag NG() {
		return new RepairedFlag(NG);
	}
	
	
}
