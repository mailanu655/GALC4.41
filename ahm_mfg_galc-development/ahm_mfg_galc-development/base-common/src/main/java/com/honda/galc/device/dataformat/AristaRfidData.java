package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;


public class AristaRfidData implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;

	
	private String clientId;
	private String processPointId;
	private String productId;

	
	public AristaRfidData() {
		super();
	}

	public AristaRfidData(String clientId, String processPointId, String productId) {
		super();
		this.clientId = StringUtils.trim(clientId);
		this.processPointId = StringUtils.trim(processPointId);
		this.productId = StringUtils.trim(productId);
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
	
	
}
