package com.honda.galc.client.plastics;

import java.io.Serializable;

public class LoadedProductData implements Serializable {
	
	private static final long serialVersionUID = 9071535121492875787L;
	
	private Double sequenceNo;
	private String productId;
	private String modelCode;
	private String extColourCode;
	private String plasticsTypeCode;
	private String carrierId;
	private String modelYearCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private String intColourCode;
	private String carrierPosition;
	public String getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	public String getCarrierPosition() {
		return carrierPosition;
	}
	public void setCarrierPosition(String carrierPosition) {
		this.carrierPosition = carrierPosition;
	}
	public String getModelYearCode() {
		return modelYearCode;
	}
	public void setModelYearCode(String modelYear) {
		this.modelYearCode = modelYear;
	}
	public String getModeTypeCode() {
		return modelTypeCode;
	}
	public void setModelTypeCode(String modelType) {
		this.modelTypeCode = modelType;
	}	
	public String getModelOptionCode() {
		return modelOptionCode;
	}
	public void setModelOptionCode(String modelOption) {
		this.modelOptionCode = modelOption;
	}	
	public String getIntColourCode() {
		return intColourCode;
	}
	public void setIntColourCode(String intColour) {
		this.intColourCode = intColour;
	}
	public String getExtColourCode() {
		return extColourCode;
	}
	public void setExtColourCode(String extColourCode) {
		this.extColourCode = extColourCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Double getSequenceNo() {
		return this.sequenceNo;
	}
	public void setSequenceNo(Double seqNo) {
		this.sequenceNo = seqNo;
	}
	public String getPlasticsTypeCode() {
		return plasticsTypeCode;
	}
	public void setPlasticsTypeCode(String plasticsTypeCode) {
		this.plasticsTypeCode = plasticsTypeCode;
	}
	
}
