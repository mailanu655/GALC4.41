package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;


public class DeviceDataResult extends DeviceResult implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	
	
	private String clientId;
	private String processPointId;
	private String productId;
	private String judgement;
	private boolean skipOperation;

	
	public DeviceDataResult() {
		super();
	}

	public DeviceDataResult(String clientId, String processPointId, String productId, String judgement) {
		super();
		this.clientId = StringUtils.trim(clientId);
		this.processPointId = StringUtils.trim(processPointId);
		this.productId = StringUtils.trim(productId);
		this.judgement = StringUtils.trim(judgement);
		
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

	public String getJudgement() {
		return judgement;
	}

	public void setJudgement(String judgement) {
		this.judgement = judgement.trim();
	}

	public boolean getSkipOperation() {
		return skipOperation;
	}

	public void setSkipOperation(boolean skipOperation) {
		this.skipOperation = skipOperation;
	}
	
	public boolean isOk(){
		return OK.equals(judgement);
	}

	public boolean isSkipOperation(){
		return this.skipOperation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "DeviceDataResult [clientId=" + clientId + ", processPointId=" + processPointId + ", productId="
				+ productId + ", judgement=" + judgement + ", skipOperation=" + skipOperation + ", partValue=";
	}
	
}
