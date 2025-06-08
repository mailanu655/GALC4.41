package com.honda.galc.client.events;

public class ProductPlcData extends AbstractPlcDataReadyEvent {
	private String _productId = "";
	private String _productSpecCode = "";
	private String _productionLot = "";
	
	public ProductPlcData(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public void setProductId(String prodId){
		this._productId = prodId;
	}
	
	public String getProductId(){
		return _productId;
	}
	
	public void setProductSpecCode(String prodSpecCode){
		this._productSpecCode = prodSpecCode;
	}
	
	public String getProductSpecCode(){
		return _productSpecCode;
	}
	
	public void setProductionLot(String productionLot){
		this._productionLot = productionLot;
	}
	
	public String getProductionLot(){
		return _productionLot;
	}
}
