package com.honda.galc.dto;

import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;

public class FrameSpecDto implements IDto{
	
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String productId = null;
	
	@DtoTag()
	private String productSpecCode = null;
	
	@DtoTag()
	private String productionLot;
	
	@DtoTag()
	private String extColorCode;

	@DtoTag()
	private String intColorCode;

	@DtoTag()
	private String extColorDescription;

	@DtoTag()
	private String intColorDescription;
	
	@DtoTag()
	private String salesModelCode;

	@DtoTag()
	private String salesModelTypeCode;

	@DtoTag()
	private String salesExtColorCode;
	
	@DtoTag()
	private String modelCode;
	
	@DtoTag()
	private String modelYearCode;
	
	@DtoTag()
	private String modelOptionCode;
	
	@DtoTag()
	private String modelTypeCode;
	
	@DtoTag()
	private int sendStatus;
	
	private String userId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getExtColorCode() {
		return extColorCode;
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	public String getIntColorCode() {
		return intColorCode;
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	public String getExtColorDescription() {
		return extColorDescription;
	}

	public void setExtColorDescription(String extColorDescription) {
		this.extColorDescription = extColorDescription;
	}

	public String getIntColorDescription() {
		return intColorDescription;
	}

	public void setIntColorDescription(String intColorDescription) {
		this.intColorDescription = intColorDescription;
	}

	public String getSalesModelCode() {
		return salesModelCode;
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelTypeCode() {
		return salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	public String getSalesExtColorCode() {
		return salesExtColorCode;
	}

	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelYearCode() {
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelOptionCode() {
		return modelOptionCode;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public int getSendStatus() {
		return this.sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}
		
	public PreProductionLotSendStatus getSendStatusString() {
		return PreProductionLotSendStatus.getType(sendStatus);
	}

	public void setSendStatusString(PreProductionLotSendStatus status) {
		this.sendStatus = status.getId();
	}
	
}
