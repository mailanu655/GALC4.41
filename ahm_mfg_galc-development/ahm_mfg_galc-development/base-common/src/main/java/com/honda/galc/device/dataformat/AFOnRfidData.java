package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;


public class AFOnRfidData implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;

	
	public static final String UNIT_PRESENT = "1";
	public static final String UNIT_NOT_PRESENT = "0";
	
	private String clientId;
	private String processPointId;
	private String productId;
	private String unitPresent;
	private String opMode;

	
	public AFOnRfidData() {
		super();
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId.trim();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return this.clientId+","+this.processPointId+","+this.productId;
	}

	public String getUnitPresent() {
		return unitPresent;
	}

	public void setUnitPresent(String unitPresent) {
		this.unitPresent = unitPresent;
	}

	public String getOpMode() {
		return opMode;
	}

	public void setOpMode(String opMode) {
		this.opMode = opMode;
	}
	
	public boolean isUnitPresent(){
		return UNIT_PRESENT.equalsIgnoreCase(this.unitPresent);
	}
	
}
