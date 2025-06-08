package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

public class EngineLoadSnValidation implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Tag(name="PRODUCT_ID")
	private String productId;
	
	@Tag(name="SHORT_AF_ON_SEQUENCE_NUMBER")
	private String shortAfOnSequenceNumber;
	
	@Tag(name="FRAME_YMTOC")
	private String frameYmtoc;
	
	@Tag(name="FRAME_MODEL_YEAR_CODE")
	private String frameModelYearCode;
	
	@Tag(name="FRAME_MODEL_CODE")
	private String frameModelCode;
	
	@Tag(name="FRAME_MODEL_TYPE_CODE")
	private String frameModelTypeCode;
	
	@Tag(name="EXT_COL_CODE")
	private String extColCode;
	
	@Tag(name="INT_COL_CODE")
	private String intColCode;
	
	@Tag(name="ENGINE_SERIAL_NO")
	private String engineSerialNo;
	
	@Tag(name="ENGINE_FIRED")
	private Short engineFired;
	
	@Tag(name="ENGINE_MTO")
	private String engineMto;
	
	@Tag(name="IS_ENGINE_VALID")
	private Boolean isEngineValid;
	

	public EngineLoadSnValidation(){
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getShortAfOnSequenceNumber() {
		return shortAfOnSequenceNumber;
	}

	public void setShortAfOnSequenceNumber(String shortAfOnSequenceNumber) {
		this.shortAfOnSequenceNumber = shortAfOnSequenceNumber;
	}
	
	public String getFrameYmtoc() {
		return frameYmtoc;
	}

	public void setFrameYmtoc(String frameYmtoc) {
		this.frameYmtoc = frameYmtoc;
	}
	
	public String getFrameModelYearCode() {
		return frameModelYearCode;
	}

	public void setFrameModelYearCode(String frameModelYearCode) {
		this.frameModelYearCode = frameModelYearCode;
	}
	
	public String getFrameModelCode() {
		return frameModelCode;
	}

	public void setFrameModelCode(String frameModelCode) {
		this.frameModelCode = frameModelCode;
	}
	
	public String getFrameModelTypeCode() {
		return frameModelTypeCode;
	}

	public void setFrameModelTypeCode(String frameModelTypeCode) {
		this.frameModelTypeCode = frameModelTypeCode;
	}
	
	public String getExtColCode() {
		return extColCode;
	}

	public void setExtColCode(String extColCode) {
		this.extColCode = extColCode;
	}
	
	public String getIntColCode() {
		return intColCode;
	}

	public void setIntColCode(String intColCode) {
		this.intColCode = intColCode;
	}
	
	public String getEngineSerialNo() {
		return engineSerialNo;
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}
	
	public Short getEngineFired() {
		return engineFired;
	}

	public void setEngineFired(Short engineFired) {
		this.engineFired = engineFired;
	}
	
	public String getEngineMto() {
		return engineMto;
	}

	public void setEngineMto(String engineMto) {
		this.engineMto = engineMto;
	}
	
	public Boolean getIsEngineValid() {
		return isEngineValid;
	}

	public void setIsEngineValid(Boolean isEngineValid) {
		this.isEngineValid = isEngineValid;
	}
}
