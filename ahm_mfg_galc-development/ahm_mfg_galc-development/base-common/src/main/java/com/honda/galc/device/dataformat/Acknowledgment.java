package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

public class Acknowledgment implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;

	@Tag(name = "DATA")
	boolean success;
	
	@Tag(name = "TRANSMIT_EXCEPTION", optional = true)
	String transmitException;

	public Acknowledgment() {
		super();
	}

	public Acknowledgment(boolean success, String transmitException) {
		super();
		this.success = success;
		this.transmitException = transmitException;
	}

	public static Acknowledgment success() {
		return new Acknowledgment(true, null);
	}
	
	public static Acknowledgment failed(String exception) {
		return new Acknowledgment(false, exception);
	}
	
	
	//Getter & Setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getTransmitException() {
		return transmitException;
	}

	public void setTransmitException(String transmitException) {
		this.transmitException = transmitException;
	}

	public DataContainer toDataContainer() {
		DataContainer dc = new DefaultDataContainer();
		dc.put("DATA", success);
		dc.put("TRANSMIT_EXCEPTION", transmitException);
		return dc;
	}
}
