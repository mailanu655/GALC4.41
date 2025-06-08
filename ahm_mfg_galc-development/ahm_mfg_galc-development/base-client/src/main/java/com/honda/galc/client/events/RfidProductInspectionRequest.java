package com.honda.galc.client.events;

public class RfidProductInspectionRequest {
	private String _productId = "";
	private boolean _rfidValidationStatus = false;
	
	public RfidProductInspectionRequest(String productId, boolean rfidValidationStatus) {
		_productId = productId;
		_rfidValidationStatus = rfidValidationStatus;
	}
	
	public String getProductId() {
		return _productId;
	}
	public void setProductId(String id) {
		_productId = id;
	}
	public boolean isRfidValidationStatus() {
		return _rfidValidationStatus;
	}
	public void setRfidValidationStatus(boolean rfidValidationStatus) {
		this._rfidValidationStatus = rfidValidationStatus;
	}
}
