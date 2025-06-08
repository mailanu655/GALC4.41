package com.honda.galc.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.InputData;

public class BroadcastContext extends InputData implements Serializable {

	private static final long serialVersionUID = 5516508013836554120L;
	private String id;
	private String deviceId;
	private String deviceMsg;
	private String productId;
	private ProductType productType;
	private String operationName;
	private String processPointId;
	private String sequenceNumber;
	private String destination;
	private Map<String, Object> extraAttribs;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceMsg() {
		return deviceMsg;
	}
	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Map<String, Object> getExtraAttribs() {
		return extraAttribs;
	}
	public void setExtraAttribs(Map<String, Object> extraAttribs) {
		this.extraAttribs = extraAttribs;
	}

	public Map<String, String> getAttributes() {
		Map<String, String> av = new HashMap<String, String>();
		av.put("ID", this.getId() == null? "": this.getId());
		av.put("PRODUCT_ID", this.getProductId() == null? "" : this.getProductId());
		av.put("PRODUCT_TYPE", this.getProductType() == null? "": this.getProductType().toString());
		av.put("DEVICE_ID", this.getDeviceId() == null? "" : this.getDeviceId());
		av.put("OPERATION_NAME", this.getOperationName() == null? "" : this.getOperationName());
		av.put("PROCESS_POINT_ID", this.getProcessPointId() == null? "" : this.getProcessPointId());
		av.put("SEQUENCE_NUMBER", this.getSequenceNumber() == null? "" : this.getSequenceNumber());
		return av;
	}

	public Map<String, String> getAllAttributes() {
		Map<String, String> av = getAttributes();
		Map<String, Object> extraAttributes = this.getExtraAttribs();
		if (extraAttributes != null) {
			for (Map.Entry<String, Object> pair : extraAttributes.entrySet()) {
				av.put(pair.getKey(), pair.getValue().toString());
			}
		}
		return av;
	}

}
